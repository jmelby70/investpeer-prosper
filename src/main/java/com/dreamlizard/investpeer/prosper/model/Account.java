package com.dreamlizard.investpeer.prosper.model;

import lombok.Data;

import java.util.Map;

@Data
public class Account
{
    private double available_cash_balance;
    private double pending_investments_primary_market;
    private double pending_investments_secondary_market;
    private double pending_quick_invest_orders;
    private double total_principal_received_on_active_notes;
    private double total_amount_invested_on_active_notes;
    private double outstanding_principal_on_active_notes;
    private double total_account_value;
    private double pending_deposit;
    private String external_user_id;
    private String prosper_account_digest;
    private Map<String, Double> invested_notes;
    private Map<String, Double> pending_bids;
}
