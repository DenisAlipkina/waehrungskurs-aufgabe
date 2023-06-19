package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IataExchangeRateApplication {

	private IataExchangeRateDataManager dataManager;
	private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	
	public void run() throws Exception { 
		readIataExchangeRates();
		
		displayMenu();
		
		boolean exitRequested = false;
		
		while(!exitRequested) {
			String userInput = getUserInput();
			
			exitRequested = processUserInputAndCheckForExitRequest(userInput);
		}
		
		System.out.println("Auf Wiedersehen!");
	}
	
	private void readIataExchangeRates() throws NumberFormatException, ParseException {
		System.out.println("CSV wird gelesen...");

		this.dataManager = new IataCsvReader().read("demo/src/main/java/com/example/KursExport.csv", dateFormat);

		System.out.println("CSV erfolgreich eingelesen");
		System.out.println();
	}
	
	private void displayMenu() {
		System.out.println("IATA W�hrungskurs-Beispiel");
		System.out.println();
		
		System.out.println("W�hlen Sie eine Funktion durch Auswahl der Zifferntaste und Dr�cken von 'Return'");
		System.out.println("[1] W�hrungskurs anzeigen");
		System.out.println("[2] Neuen W�hrungskurs eingeben");
		System.out.println();
		
		System.out.println("[0] Beenden");
	}
	
	private String getUserInput() throws Exception {
		BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
		
		return consoleInput.readLine();
	}
	
	//Returns true when the user wants to exit the application 
	private boolean processUserInputAndCheckForExitRequest(String userInput) throws Exception {
		if(userInput.equals("0")) {
			return true;
		}
		
		if(userInput.equals("1")) {
			displayIataExchangeRate();
		} else if(userInput.equals("2")) {
			enterIataExchangeRate();
		} else {
			System.out.println("Falsche Eingabe. Versuchen Sie es bitte erneut.");
		}
		
		return false;
	}
	
	private void displayIataExchangeRate() throws Exception {
		String currencyIsoCode = getUserInputForStringField("W�hrung");
		Date date = getUserInputForDateField("Datum");

		Double exchangeRate = dataManager.getIataExchangeRate(currencyIsoCode, date);
		if (exchangeRate == null) System.out.println("Keine Information zu dem Datum vorhanden");
		else System.out.println(String.format("1 %s entspricht %f Euro", currencyIsoCode, exchangeRate));

	}
	
	private void enterIataExchangeRate() throws Exception {
		String currencyIsoCode = getUserInputForStringField("W�hrung");
		Date from = getUserInputForDateField("Von");
		Date to = getUserInputForDateField("Bis");
		Double exchangeRate = getUserInputForDoubleField("Euro-Kurs f�r 1 " + currencyIsoCode);
		dataManager.putData(currencyIsoCode, exchangeRate, from, to);
	}
	
	private String getUserInputForStringField(String fieldName) throws Exception {
		System.out.print(fieldName + ": ");
		return getUserInput();
	}
	
	private Date getUserInputForDateField(String fieldName) throws Exception {
		System.out.print(fieldName + " (tt.mm.jjjj): ");
		String dateString = getUserInput();
		
		return this.dateFormat.parse(dateString);
	}
	
	private Double getUserInputForDoubleField(String fieldName) throws Exception {
		String doubleString = getUserInputForStringField(fieldName);
		return Double.valueOf(doubleString);
	}
}
