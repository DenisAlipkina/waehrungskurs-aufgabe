package com.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;

public class IataCsvReader {

    private String filepath;
    private FileReader fileReader;
    private BufferedReader reader;
    private final static String delimiter = ";";

    /**
     * Initializing BufferedReader
     * @param filepath path to csv-file for initializing FileReader
     */
    private void init(String filepath) {
         this.filepath = filepath;
        try {
            this.fileReader = new FileReader(this.filepath);
            this.reader = new BufferedReader(this.fileReader);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
    }

    /**
     * reads a csv-file and creates a {@link IataExchangeRateDataManager}
     * @param filepath filepath to csv-file for initializing
     * @param dateFormat {@link DateFormat} used for parsing csv Dates
     * @return all csv-data in one datastructure
     */
    public IataExchangeRateDataManager read(String filepath, DateFormat dateFormat) {
        String line = "";
        String[] token;
        IataExchangeRateDataManager dataManager = new IataExchangeRateDataManager();
        init(filepath);
        try {
            while ((line = reader.readLine()) != null) {
                token = line.split(delimiter);
                dataManager.putData(token[2], Double.parseDouble(token[1].replace(',', '.')), dateFormat.parse(token[3]), dateFormat.parse(token[4]));  
            }
        } catch (IOException | NumberFormatException | ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dataManager;
    }
}
