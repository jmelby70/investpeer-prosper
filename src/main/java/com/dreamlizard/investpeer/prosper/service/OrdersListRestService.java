package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.exception.ProsperRestServiceException;
import com.dreamlizard.investpeer.prosper.model.OrdersList;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrdersListRestService extends ProsperRestService<OrdersList>
{
    public OrdersListRestService(RestTemplate restTemplate, ProsperConfig prosperConfig, OAuthTokenHolder oAuthTokenHolder)
    {
        super(restTemplate, prosperConfig, oAuthTokenHolder);
    }

    public OrdersList getOrdersList() throws ProsperRestServiceException
    {
        // todo handle pagination; default page is 25
        String url = getBaseUrl() + "/v1/orders/";
        return getEntity(url, OrdersList.class);
    }
}
