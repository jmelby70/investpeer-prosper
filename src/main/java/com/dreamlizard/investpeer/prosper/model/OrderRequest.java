package com.dreamlizard.investpeer.prosper.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest
{
    private List<BidRequest> bid_requests;
}
