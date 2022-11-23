package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.BidRequest;
import com.dreamlizard.investpeer.prosper.model.Listing;
import com.dreamlizard.investpeer.prosper.model.OrderRequest;
import com.dreamlizard.investpeer.prosper.model.OrderResponse;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Set;

@Log
@Service
public class OrdersRestService extends ProsperRestService<OrderResponse>
{
    public OrdersRestService(RestTemplate restTemplate, ProsperConfig prosperConfig)
    {
        super(restTemplate, prosperConfig);
    }

    public OrderResponse sumbitOrder(Set<Listing> listings) throws ProsperRestServiceException
    {
        String url = getBaseUrl() + "/v1/orders/";
        OrderRequest orderRequest = createOrderRequest(listings);
        return postEntity(url, orderRequest, OrderResponse.class);
    }

    private OrderRequest createOrderRequest(Set<Listing> listings)
    {
        OrderRequest orderRequest = new OrderRequest();
        ArrayList<BidRequest> bidRequests = new ArrayList<>();
        for (Listing listing : listings)
        {
            BidRequest bidRequest = new BidRequest();
            bidRequest.setBid_amount(getProsperConfig().getMinimumInvestmentAmount());
            bidRequest.setListing_id(listing.getListing_number());
            bidRequests.add(bidRequest);
        }

        orderRequest.setBid_requests(bidRequests);
        log.info("Created OrderRequest: " + orderRequest.toString());
        return orderRequest;
    }

}
