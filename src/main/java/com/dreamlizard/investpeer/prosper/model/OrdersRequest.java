package com.dreamlizard.investpeer.prosper.model;

import lombok.Data;

import java.util.List;

@Data
public class OrdersRequest
{
    private List<BidRequest> bid_requests;
}
