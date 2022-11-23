package com.dreamlizard.investpeer.prosper.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse
{
    private String order_id;
    private String order_date;
    private List<BidRequest> bid_requests;
    private String order_status;
    private String source;
}
