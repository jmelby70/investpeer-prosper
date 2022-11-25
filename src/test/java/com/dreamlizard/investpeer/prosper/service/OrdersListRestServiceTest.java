package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.config.ProsperConfig;
import com.dreamlizard.investpeer.prosper.model.OrdersList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OrdersListRestServiceTest
{
    @Autowired
    private OrdersListRestService ordersListRestService;
    @Autowired
    private ProsperConfig prosperConfig;

    @Test
    public void testOrdersListWithPagination() throws Exception
    {
//        prosperConfig.setOrderListLimit(2);
//        OrdersList ordersList = ordersListRestService.getOrdersList();
//        System.out.println(ordersList.toString());
//        assertTrue(ordersList.getResult().size() > 2);
    }
}
