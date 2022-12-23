package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.constants.AppConstants;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.Account;
import com.dreamlizard.investpeer.prosper.model.BidRequest;
import com.dreamlizard.investpeer.prosper.model.FilterSet;
import com.dreamlizard.investpeer.prosper.model.FilterSetProperties;
import com.dreamlizard.investpeer.prosper.model.Listing;
import com.dreamlizard.investpeer.prosper.model.Listings;
import com.dreamlizard.investpeer.prosper.model.OrdersList;
import com.dreamlizard.investpeer.prosper.model.OrdersRequest;
import com.dreamlizard.investpeer.prosper.model.OrdersResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProsperBuyNotesService
{
    private final ProsperConfig prosperConfig;
    private final FilterSetProperties filterSetProperties;
    private final AccountsRestService accountsRestService;
    private final ListingsRestService listingsRestService;
    private final OrdersRestService ordersRestService;
    private final OrdersListRestService ordersListRestService;
    private final NotificationService notificationService;
    @Getter
    @Setter
    private String runMode;

    public void buyNotes() throws ProsperRestServiceException, IOException
    {
        // Determine how much cash is available to invest
        log.info("Gathering Account info...");
        Account account = accountsRestService.getAccount();
        log.debug("Account:\n{}", account.toString());
        log.info("Cash available: " + account.getAvailable_cash_balance());
        if (AppConstants.TEST_MODE.equalsIgnoreCase(getRunMode()) || (account.getAvailable_cash_balance() >= prosperConfig.getMinimumInvestmentAmount()))
        {
            // Get current listings
            log.info("Getting current Listings...");
            Listings listings = listingsRestService.getListings();
            log.info("Listing count retrieved: {}", listings.getResult_count());

            if (listings.getResult_count() > 0)
            {
                // Filter listings
                DecimalFormat df = new DecimalFormat("###");
                df.setRoundingMode(RoundingMode.DOWN);
                int maxLoanCount = Integer.parseInt(df.format(account.getAvailable_cash_balance() / prosperConfig.getMinimumInvestmentAmount()));
                log.info("Max investment count possible: {}", maxLoanCount);
                Set<Listing> filteredListings = filterListings(listings, maxLoanCount);
                if ((filteredListings != null) && (filteredListings.size() > 0))
                {
                    log.info("Filtered listing count: {}", filteredListings.size());

                    // Trim out Listings already pending on orders
                    Set<Listing> trimmedListings = trimFilteredListing(filteredListings, maxLoanCount);
                    log.info("Trimmed listing count: {}", trimmedListings.size());
                    log.debug("Final Listings: {}", trimmedListings.toString());
                    OrdersRequest ordersRequest = createOrderRequest(trimmedListings, maxLoanCount);

                    // If runMode is prod, submit orders for filtered listings
                    if (AppConstants.PROD_MODE.equalsIgnoreCase(runMode) && trimmedListings.size() > 0)
                    {
                        log.info("Submitting order...");
                        OrdersResponse ordersResponse = ordersRestService.sumbitOrder(ordersRequest);
                        log.info("Order submitted: {}", ordersResponse.getOrder_id());
                        notificationService.sendOrderNotification(trimmedListings, ordersResponse);
                    }
                }
                else
                {
                    log.info("No matching listings found.");
                }
            }
            else
            {
                log.info("No listings available.");
            }
        }
        else
        {
            log.info("Not enough cash to invest.");
        }
    }

    private Set<Listing> filterListings(Listings listings, int maxLoanCount)
    {
        HashSet<Listing> filteredListings = new HashSet<>();
        if (listings.getResult() != null)
        {
            for (FilterSet filterSet : filterSetProperties.getFilterSetList())
            {
                log.info("FilterSet in effect if Loan Count Over: {}", filterSet.getLoanCountOver());
                if (filterSet.getLoanCountOver() <= maxLoanCount)
                {
                    log.info("FilterSet in effect: {}", filterSet.toString());
                    for (Listing listing : listings.getResult())
                    {
                        if (filterSet.checkGrade(listing.getProsper_rating()) &&
                                filterSet.checkEmploymentLength(listing.getMonths_employed()) &&
                                (listing.getCredit_bureau_values_transunion_indexed() != null) &&
                                filterSet.checkInquiries(listing.getCredit_bureau_values_transunion_indexed().getG980s_inquiries_in_the_last_6_months()) &&
                                (listing.getStated_monthly_income() > 0) &&
                                filterSet.checkPaymentIncomeRatio(listing.getListing_monthly_payment(), listing.getStated_monthly_income()))
                        {
                            filteredListings.add(listing);
                            log.info("Adding Listing: {}", listing.getListing_number());
                        }
                    }
                }
                else
                {
                    log.info("FilterSet skipped: {}", filterSet.toString());
                }
            }
        }

        return filteredListings;
    }

    private Set<Listing> trimFilteredListing(Set<Listing> filteredListings, int maxLoanCount) throws ProsperRestServiceException
    {
        OrdersList ordersList = ordersListRestService.getOrdersList();
        HashSet<Listing> trimmedListings = new HashSet<>(filteredListings);
        if (ordersList != null)
        {
            // Remove listings we have already ordered
            for (OrdersResponse ordersResponse : ordersList.getResult())
            {
                if ("IN_PROGRESS".equals(ordersResponse.getOrder_status()) && (ordersResponse.getBid_requests() != null))
                {
                    for (BidRequest bidRequest : ordersResponse.getBid_requests())
                    {
                        for (Listing listing : filteredListings)
                        {
                            if (bidRequest.getListing_id() == listing.getListing_number())
                            {
                                log.info("Removing Listing already ordered: {}", listing.getListing_number());
                                trimmedListings.remove(listing);
                            }
                        }
                    }
                }
            }

            // Truncate the listings if we found more than we can afford
            if (trimmedListings.size() > maxLoanCount)
            {
                log.info("Truncating order to max listing count of {}", maxLoanCount);
                HashSet<Listing> truncatedListings = new HashSet<>();
                int i = 0;
                for (Listing listing: trimmedListings)
                {
                    if (i < maxLoanCount)
                    {
                        truncatedListings.add(listing);
                        i++;
                    }
                }
                trimmedListings = truncatedListings;
            }
        }

        return trimmedListings;
    }

    private OrdersRequest createOrderRequest(Set<Listing> listings, int maxLoanCount)
    {
        OrdersRequest ordersRequest = new OrdersRequest();
        ArrayList<BidRequest> bidRequests = new ArrayList<>();
        int i = 0;
        for (Listing listing : listings)
        {
            if ((i < maxLoanCount) && (i < 100))
            {
                BidRequest bidRequest = new BidRequest();
                bidRequest.setBid_amount(prosperConfig.getMinimumInvestmentAmount());
                bidRequest.setListing_id(listing.getListing_number());
                bidRequests.add(bidRequest);
                i++;
            }
        }

        ordersRequest.setBid_requests(bidRequests);
        log.info("Created OrdersRequest: {}", ordersRequest.toString());
        return ordersRequest;
    }
}
