package com.dreamlizard.investpeer.prosper.model;

import lombok.Data;

@Data
public class BidRequest
{
    private long listing_id;
    private double bid_amount;
    private String bid_status;
}
