package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.BidRequest;
import com.dreamlizard.investpeer.prosper.model.Listing;
import com.dreamlizard.investpeer.prosper.model.OrdersRequest;
import com.dreamlizard.investpeer.prosper.model.OrdersResponse;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Set;

@Log
@Service
public class OrdersRestService extends ProsperRestService<OrdersResponse>
{
    public OrdersRestService(RestTemplate restTemplate, ProsperConfig prosperConfig)
    {
        super(restTemplate, prosperConfig);
    }

    public OrdersResponse sumbitOrder(Set<Listing> listings) throws ProsperRestServiceException
    {
        String url = getBaseUrl() + "/v1/orders/";
        OrdersRequest ordersRequest = createOrderRequest(listings);
        return postEntity(url, ordersRequest, OrdersResponse.class);
    }

    private OrdersRequest createOrderRequest(Set<Listing> listings)
    {
        OrdersRequest ordersRequest = new OrdersRequest();
        ArrayList<BidRequest> bidRequests = new ArrayList<>();
        for (Listing listing : listings)
        {
            BidRequest bidRequest = new BidRequest();
            bidRequest.setBid_amount(getProsperConfig().getMinimumInvestmentAmount());
            bidRequest.setListing_id(listing.getListing_number());
            bidRequests.add(bidRequest);
        }

        ordersRequest.setBid_requests(bidRequests);
        log.info("Created OrdersRequest: " + ordersRequest.toString());
        return ordersRequest;
    }

}
