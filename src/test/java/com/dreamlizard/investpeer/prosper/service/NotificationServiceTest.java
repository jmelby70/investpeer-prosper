package com.dreamlizard.investpeer.prosper.service;

import com.dreamlizard.investpeer.prosper.model.Listing;
import com.dreamlizard.investpeer.prosper.model.OrdersResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class NotificationServiceTest
{
    @Autowired
    private NotificationService notificationService;

    @Test
    public void testSendOrderNotification() throws Exception
    {
//        Listing listing = new Listing();
//        listing.setProsper_rating("M");
//        listing.setListing_number(1);
//        Set<Listing> listingSet = new HashSet<>();
//        listingSet.add(listing);
//
//        OrdersResponse ordersResponse = new OrdersResponse();
//        ordersResponse.setOrder_id("ORDER00001");
//        ordersResponse.setOrder_status("COMPLETED");
//        ordersResponse.setOrder_date("2025-03-24");
//
//        notificationService.sendOrderNotification(listingSet, ordersResponse);
    }
}
