package com.dreamlizard.investpeer.prosper.model;

import lombok.Data;

@Data
public class Listing
{
    private CreditBureauValues credit_bureau_values_transunion_indexed;
    private String listing_start_date;
    private int listing_status;
    private String listing_status_reason;
    private int verification_stage;
    private double listing_amount;
    private double amount_funded;
    private double amount_remaining;
    private double percent_funded;
    private boolean partial_funding_indicator;
    private double funding_threshold;
    private String prosper_rating;
    private double lender_yield;
    private double borrower_rate;
    private double borrower_apr;
    private int listing_term;
    private double listing_monthly_payment;
    private int prosper_score;
    private int listing_category_id;
    private String listing_title;
    private int income_range;
    private String income_range_description;
    private double stated_monthly_income;
    private boolean income_verifiable;
    private double dti_wprosper_loan;
    private String employment_status_description;
    private String occupation;
    private String borrower_state;
    private int prior_prosper_loans_active;
    private int prior_prosper_loans;
    private double prior_prosper_loans_principal_borrowed;
    private double prior_prosper_loans_principal_outstanding;
    private double prior_prosper_loans_balance_outstanding;
    private int prior_prosper_loans_cycles_billed;
    private int prior_prosper_loans_ontime_payments;
    private int prior_prosper_loans_late_cycles;
    private int prior_prosper_loans_late_payments_one_month_plus;
    private double max_prior_prosper_loan;
    private double min_prior_prosper_loan;
    private int prior_prosper_loan_earliest_pay_off;
    private int lender_indicator;
    private String channel_code;
    private double amount_participation;
    private int investment_typeid;
    private String investment_type_description;
    private String last_updated_date;
    private boolean invested;
    private boolean biddable;
    private boolean has_mortgage;
    private double historical_return;
    private double historical_return_10th_pctl;
    private double historical_return_90th_pctl;
    private double estimated_monthly_housing_expense;
    private boolean co_borrower_application;
    private double months_employed;
    private long listing_number;
    private int investment_product_id;
    private String decision_bureau;
    private String member_key;
    private String listing_creation_date;
}
