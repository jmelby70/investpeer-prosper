package com.dreamlizard.investpeer.prosper.model;

import lombok.Data;

@Data
public class CreditBureauValues
{
    private String credit_report_date;
    private double at02s_open_accounts;
    private double g041s_accounts_30_or_more_days_past_due_ever;
    private double g093s_number_of_public_records;
    private double g094s_number_of_public_record_bankruptcies;
    private double g095s_months_since_most_recent_public_record;
    private double g218b_number_of_delinquent_accounts;
    private double g980s_inquiries_in_the_last_6_months;
    private double re20s_age_of_oldest_revolving_account_in_months;
    private double s207s_months_since_most_recent_public_record_bankruptcy;
    private double re33s_balance_owed_on_all_revolving_accounts;
    private double at57s_amount_delinquent;
    private double g099s_public_records_last_24_months;
    private double at20s_oldest_trade_open_date;
    private double at03s_current_credit_lines;
    private double re101s_revolving_balance;
    private double bc34s_bankcard_utilization;
    private double at01s_credit_lines;
    private double g102s_months_since_most_recent_inquiry;
    private String fico_score;
}
