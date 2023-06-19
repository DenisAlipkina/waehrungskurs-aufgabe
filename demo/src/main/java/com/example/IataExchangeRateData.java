package com.example;

import java.util.Date;
/**
 * Holds valuable Information about exchangerate and dates together
 */
public class IataExchangeRateData {

    public double value;
    public Date startDate;
    public Date endDate;
    //No description, because it has to be ignored 

    public IataExchangeRateData(Double value, Date startDate, Date endDate) {
        this.value = value;
        this.startDate = startDate;
        this.endDate = endDate;
    }  

    public boolean isEqualTo(IataExchangeRateData data) {
        return data.value == value && startDate.compareTo(data.startDate) == 0 && endDate.compareTo(data.endDate) == 0;
    }
}
