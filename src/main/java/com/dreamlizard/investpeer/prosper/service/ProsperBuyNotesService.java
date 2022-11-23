package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.constants.AppConstants;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.Account;
import com.dreamlizard.investpeer.prosper.model.FilterSet;
import com.dreamlizard.investpeer.prosper.model.FilterSetProperties;
import com.dreamlizard.investpeer.prosper.model.Listing;
import com.dreamlizard.investpeer.prosper.model.Listings;
import com.dreamlizard.investpeer.prosper.model.OrderResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

@Log
@Service
@RequiredArgsConstructor
public class ProsperBuyNotesService
{
    private final ProsperConfig prosperConfig;
    private final FilterSetProperties filterSetProperties;
    private final AccountsRestService accountsRestService;
    private final ListingsRestService listingsRestService;
    private final OrdersRestService ordersRestService;
    @Getter
    @Setter
    private String runMode;

    public void buyNotes() throws ProsperRestServiceException
    {
        // Determine how much cash is available to invest
        log.info("Gathering Account info...");
        Account account = accountsRestService.getAccount();
        log.fine("Account:\n" + account.toString());
        log.info("Cash available: " + account.getAvailable_cash_balance());
        if (AppConstants.TEST_MODE.equalsIgnoreCase(getRunMode()) || (account.getAvailable_cash_balance() >= prosperConfig.getMinimumInvestmentAmount()))
        {
            // Get current listings
            log.info("Getting current Listings...");
            Listings listings = listingsRestService.getListings();
            log.info("Listing count retrieved: " + listings.getResult_count());

            if (listings.getResult_count() > 0)
            {
                // Filter listings
                DecimalFormat df = new DecimalFormat("###");
                df.setRoundingMode(RoundingMode.DOWN);
                int maxLoanCount = Integer.parseInt(df.format(account.getAvailable_cash_balance() / prosperConfig.getMinimumInvestmentAmount()));
                log.info("Max investment count possible: " + maxLoanCount);
                Set<Listing> filteredListings = filterListings(listings, maxLoanCount);
                if ((filteredListings != null) && (filteredListings.size() > 0))
                {
                    log.info("Filtered listing count: " + filteredListings.size());
                    log.fine("Filtered listings: " + filteredListings.toString());

                    // If runMode is prod, submit orders for filtered listings
                    if (AppConstants.PROD_MODE.equalsIgnoreCase(runMode))
                    {
                        log.info("Submitting order...");
                        OrderResponse orderResponse = ordersRestService.sumbitOrder(filteredListings);
                        log.info("Order submitted: " + orderResponse.getOrder_id());
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
                log.info("FilterSet in effect if Loan Count Over: " + filterSet.getLoanCountOver());
                if (filterSet.getLoanCountOver() <= maxLoanCount)
                {
                    log.info("FilterSet in effect: " + filterSet.toString());
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
                            log.info("Adding Listing: " + listing.getListing_number());
                        }
                    }
                }
                else
                {
                    log.info("FilterSet skipped: " + filterSet.toString());
                }
            }
        }

        return filteredListings;
    }
}
