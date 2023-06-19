package com.example;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.CsvSource;


public class AppTest {
    private static final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private IataExchangeRateDataManager manager = new IataCsvReader().read("src/test/java/com/example/KursExportTest.csv", dateFormat);
    private IataExchangeRateDataManager emptyManager = new IataExchangeRateDataManager();
    
    @ParameterizedTest
    @CsvSource({
        "'Test', '100', '01.01.2000', '01.02.2000', '31.12.1999'",
        "'Test', '100', '01.01.2000', '01.02.2000', '02.02.2000'"
    })
    public void shouldNotFind_test(String isocode, double exchangeRate, String from, String to, String testdate) throws NumberFormatException, ParseException {
        emptyManager.putData(isocode, exchangeRate, dateFormat.parse(from), dateFormat.parse(to));
        assertNull(emptyManager.getIataExchangeRate(isocode, dateFormat.parse(testdate)));
    }
    

    @ParameterizedTest
    @CsvSource({
        "'Test', '100', '01.01.2000', '01.02.2000', '15.01.2000'",
        "'Test', '100', '01.01.2000', '01.02.2000', '01.01.2000'",
        "'Test', '100', '01.01.2000', '01.02.2000', '01.02.2000'"
    })
    public void putData_getExchangerate_compare_test(String isocode, double exchangeRate, String from, String to, String testdate) throws NumberFormatException, ParseException {
        emptyManager.putData(isocode, exchangeRate, dateFormat.parse(from), dateFormat.parse(to));
        assertTrue(emptyManager.getIataExchangeRate(isocode, dateFormat.parse(testdate)) ==  exchangeRate);
    }

    @ParameterizedTest
    @CsvSource({
        "'15.02.2000', '15.03.2000', '90', '15.03.2000', '90', '16.03.2000', '100'", //cuts the beginning
        "'15.06.2000', '15.07.2000', '140', '12.06.2000', '130', '02.07.2000', '140'" //cuts the end
    })
    public void cutting_dates_test1(String from, String to, double exchangeRate, String testdate1, double rate1, String testdate2, double rate2) throws NumberFormatException, ParseException {
        manager.putData("Test", exchangeRate, dateFormat.parse(from), dateFormat.parse(to));
        assertTrue(manager.getIataExchangeRate("Test", dateFormat.parse(testdate1)) ==  rate1 &&
        manager.getIataExchangeRate("Test", dateFormat.parse(testdate2)) ==  rate2);
    }

    @ParameterizedTest
    @CsvSource({
        "'15.03.2000', '15.04.2000', '105', '14.03.2000', '100', '16.04.2000', '110', '30.03.2000', '105'", //cuts the between
        "'02.05.2000', '02.06.2000', '105', '01.05.2000', '120', '03.06.2000', '130', '18.05.2000', '105'"
    })
    public void cutting_dates_test2(String from, String to, double exchangeRate, String testdate1, double rate1,
     String testdate2, double rate2, String testdate3, double rate3) throws NumberFormatException, ParseException {
        System.out.println("before:\n" + manager.show("Test"));
        manager.putData("Test", exchangeRate, dateFormat.parse(from), dateFormat.parse(to));
        System.out.println("after:\n" + manager.show("Test"));
        assertTrue(manager.getIataExchangeRate("Test", dateFormat.parse(testdate1)) ==  rate1 &&
        manager.getIataExchangeRate("Test", dateFormat.parse(testdate2)) ==  rate2 &&
        manager.getIataExchangeRate("Test", dateFormat.parse(testdate3)) ==  rate3);
    }

    @ParameterizedTest
    @CsvSource({
        "'12.03.2000', '18.03.2000', '105', '11.03.2000', '100', '19.03.2000', '100', '15.03.2000', '105'", //cuts the middle
        "'12.06.2000', '18.06.2000', '105', '11.06.2000', '130', '19.06.2000', '130', '15.06.2000', '105'"
    })
    public void cutting_dates_test3(String from, String to, double exchangeRate, String testdate1, double rate1,
     String testdate2, double rate2, String testdate3, double rate3) throws NumberFormatException, ParseException {
        System.out.println("before:\n" + manager.show("Test"));
        manager.putData("Test", exchangeRate, dateFormat.parse(from), dateFormat.parse(to));
        System.out.println("after:\n" + manager.show("Test"));
        assertTrue(manager.getIataExchangeRate("Test", dateFormat.parse(testdate1)) ==  rate1 &&
        manager.getIataExchangeRate("Test", dateFormat.parse(testdate2)) ==  rate2 &&
        manager.getIataExchangeRate("Test", dateFormat.parse(testdate3)) ==  rate3);
    }

    
    @ParameterizedTest
    @CsvSource({
        "'12.03.2000', '18.05.2000', '105', '11.03.2000', '100', '18.04.2000', '105', '15.03.2000', '105'", //"eats" some data in middle
        "'12.05.2000', '18.07.2000', '105', '11.05.2000', '120', '18.07.2000', '105', '15.06.2000', '105'",
    })
    public void cutting_dates_test4(String from, String to, double exchangeRate, String testdate1, double rate1,
     String testdate2, double rate2, String testdate3, double rate3) throws NumberFormatException, ParseException {
        System.out.println("before:\n" + manager.show("Test"));
        manager.putData("Test", exchangeRate, dateFormat.parse(from), dateFormat.parse(to));
        System.out.println("after:\n" + manager.show("Test"));
        assertTrue(manager.getIataExchangeRate("Test", dateFormat.parse(testdate1)) ==  rate1 &&
        manager.getIataExchangeRate("Test", dateFormat.parse(testdate2)) ==  rate2 &&
        manager.getIataExchangeRate("Test", dateFormat.parse(testdate3)) ==  rate3);
    }
}
