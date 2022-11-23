package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.OrdersRequest;
import com.dreamlizard.investpeer.prosper.model.OrdersResponse;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log
@Service
public class OrdersRestService extends ProsperRestService<OrdersResponse>
{
    public OrdersRestService(RestTemplate restTemplate, ProsperConfig prosperConfig)
    {
        super(restTemplate, prosperConfig);
    }

    public OrdersResponse sumbitOrder(OrdersRequest ordersRequest) throws ProsperRestServiceException
    {
        String url = getBaseUrl() + "/v1/orders/";
        return postEntity(url, ordersRequest, OrdersResponse.class);
    }

}
