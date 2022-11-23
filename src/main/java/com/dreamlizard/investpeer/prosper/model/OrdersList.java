package com.dreamlizard.investpeer.prosper.model;

import lombok.Data;

import java.util.List;

@Data
public class OrdersList
{
    private List<OrdersResponse> result;
    private int result_count;
    private int total_count;
}
