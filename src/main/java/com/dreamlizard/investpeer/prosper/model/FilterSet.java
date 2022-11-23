package com.dreamlizard.investpeer.prosper.model;

import lombok.Data;

@Data
public class FilterSet
{
    private String[] grades;
    private int employmentLengthOver;
    private int inquiriesUnder;
    private double paymentIncomeRatioUnder;
    private int loanCountOver;

    public boolean checkGrade(String grade)
    {
        for (String acceptedGrade : grades)
        {
            if (acceptedGrade.equals(grade))
            {
                return true;
            }
        }
        return false;
    }

    public boolean checkEmploymentLength(double monthsEmployed)
    {
        return monthsEmployed >= employmentLengthOver * 12;
    }

    public boolean checkInquiries(double inquiries)
    {
        return inquiries <= inquiriesUnder;
    }

    public boolean checkPaymentIncomeRatio(double payment, double monthlyIncome)
    {
        return (payment / monthlyIncome) <= paymentIncomeRatioUnder;
    }

}
