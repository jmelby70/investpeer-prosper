package com.dreamlizard.investpeer.prosper.model;

import lombok.Data;

@Data
public class Listings
{
    private Listing[] result;
    private int result_count;
    private int total_count;
}
