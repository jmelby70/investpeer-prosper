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
        OrdersList ordersList = invokeOrdersList();
        if (ordersList.getResult().size() < ordersList.getTotal_count())
        {
            int i = ordersList.getResult().size();
            while (i < ordersList.getTotal_count())
            {
                OrdersList ordersList1 = invokeOrdersListOffset(i);
                ordersList.getResult().addAll(ordersList1.getResult());
                i = i + ordersList1.getResult().size();
            }
        }
        return ordersList;
    }

    protected OrdersList invokeOrdersList() throws ProsperRestServiceException
    {
        String url = getBaseUrl() + "/v1/orders/?limit=" + getProsperConfig().getOrderListLimit();
        return getEntity(url, OrdersList.class);
    }

    protected OrdersList invokeOrdersListOffset(int offset) throws ProsperRestServiceException
    {
        String url = getBaseUrl() + "/v1/orders/?limit=" + getProsperConfig().getOrderListLimit() + "&offset=" + offset;
        return getEntity(url, OrdersList.class);
    }
}
