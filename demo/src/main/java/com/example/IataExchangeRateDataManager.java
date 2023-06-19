package com.example;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IataExchangeRateDataManager {
    private Map<String, List<IataExchangeRateData>> allExchangeRateObservations;

    public IataExchangeRateDataManager() {
        this.allExchangeRateObservations = new HashMap<>();
    }

    
    /**
     * Gets valuable data and puts it into the {@link IataExchangeRateDataManager#allExchangeRateObservations map}
     */
    public void putData(String currencyIsoCode, Double exchangeRate, Date from, Date to) throws NumberFormatException, ParseException {
            IataExchangeRateData data = new IataExchangeRateData(exchangeRate, from, to);

            if(this.allExchangeRateObservations.get(currencyIsoCode) == null) {
                this.allExchangeRateObservations.put(currencyIsoCode, new LinkedList<>(Collections.singletonList(data)));
            } else {
                List<IataExchangeRateData> list = new LinkedList<>();
                list = addDataToList(this.allExchangeRateObservations.get(currencyIsoCode), data);
                this.allExchangeRateObservations.put(currencyIsoCode, list);
            }
            
    }
    
    /**
     * puts data into the list and returns the list
     * while putting data into list, the dates in the list can change
     * @param list
     * @param data
     * @return
     */
    private List<IataExchangeRateData> addDataToList(List<IataExchangeRateData> list, IataExchangeRateData data) {
        
        if(list.get(0).startDate.after(data.endDate)) {
            list.add(0, data);
            return list;
        }

        if(list.get(list.size() - 1).endDate.before(data.startDate)) {
            list.add(data);
            return list;
        }
        IataExchangeRateData entry;
        List<IataExchangeRateData> deletableEntries = new LinkedList<>();
        Date tmpEntryEndDate;
        boolean running = true;
        //works because list is sorted
        //not complete
        for(int i = 0; i < list.size() && running; i++) {
            entry = list.get(i);

            if(entry.startDate.compareTo(data.startDate) == 0) {
                if (entry.endDate.after(data.endDate)) {
                    entry.startDate = minusDays(data.endDate, -1);
                    list.add(i,data);
                    running = false;
                } else if (entry.endDate.compareTo(data.endDate) == 0) {
                    entry.value = data.value;
                    running = false;
                } else {
                    deletableEntries.add(entry);
                    if (i == list.size()-1) {
                        list.add(i+1, data);
                        running = false;
                    }
                }
            } else if(entry.startDate.after(data.startDate) && !entry.startDate.after(data.endDate)) {     
                
                if (entry.endDate.after(data.endDate)) {  
                    entry.startDate = minusDays(data.endDate, -1);
                    list.add(i, data);
                    running = false;
                } else if (entry.endDate.compareTo(data.endDate) == 0) {
                    deletableEntries.add(entry);
                    entry = data;
                    running = false;
                } else {
                    deletableEntries.add(entry);
                    if (i == list.size()-1) {
                        list.add(i+1, data);
                        running = false;
                    }
                }
            } else if(entry.startDate.before(data.startDate) && !entry.endDate.before(data.startDate)) {
                if (entry.endDate.after(data.endDate)) {
                    tmpEntryEndDate = entry.endDate;
                    entry.endDate = minusDays(data.startDate, 1);
                    list.add(i+1, data);
                    list.add(i+2, new IataExchangeRateData(entry.value, minusDays(data.endDate, -1), tmpEntryEndDate));
                    running = false;
                } else if (entry.endDate.compareTo(data.endDate) == 0) {
                    entry.endDate = minusDays(data.startDate, 1);
                    list.add(i+1, data);
                    running = false;
                } else {
                    entry.endDate = minusDays(data.startDate, 1);
                    if (i == list.size()-1) {
                        list.add(i+1, data);
                        running = false;
                    }
                }
            }
        }

        for (IataExchangeRateData entry1 : deletableEntries) {
            list.remove(entry1);
        }
        
        return list;
    }

    public Double getIataExchangeRate(String currencyIsoCode, Date date) {
        List<IataExchangeRateData> data = this.allExchangeRateObservations.get(currencyIsoCode);
        for(IataExchangeRateData entry : data) {
            if (entry.startDate.compareTo(date) * date.compareTo(entry.endDate) >= 0) {
                return entry.value;
            }
        }
        return null;
    }

    public String show(String isocode) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String result = isocode + "\n";
        for(IataExchangeRateData entry : allExchangeRateObservations.get(isocode)) {
            result += entry.value + "\t" + dateFormat.format(entry.startDate)  + " - " + dateFormat.format(entry.endDate) + "\n";
        }
        return result;
    }

    private Date minusDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }
}
