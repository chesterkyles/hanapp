package com.hanapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class CsvFileInOut {
    String path;
    String fileName;

    public CsvFileInOut(String path, String fileName){
        this.path = path;
        this.fileName = fileName;
    }

    public String read(String email_ad, String password){
        String resultRow = null;
        try {
            File file = new File(path + fileName);
            FileInputStream fileInputStream = new FileInputStream (file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String csvLine = null;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] row = csvLine.split(",");
                 if((row[0].equals(email_ad))) {
                     if (row[1].equals(password)){
                         resultRow = "success";
                     } else {
                         resultRow = "invalid_password";
                     }
                     break;
                 } else {
                     resultRow = "invalid_username";
                 }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        return resultRow;
    }

    public String[] read(String search_item){
        String[] resultRow = null;
        try {
            File file = new File(path + fileName);
            FileInputStream fileInputStream = new FileInputStream (file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String csvLine = null;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] row = csvLine.split(",");
                 if(row[0].equals(search_item)) {
                    resultRow = row;
                    break;
                 }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        return resultRow;
    }

    public void write(String word){
        try {
            File file = new File(path + fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter (outputStreamWriter);
            bufferedWriter.write(word);
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in writing to CSV file: "+ex);
        }
    }
    public ArrayList<String> read_loc(String return_type) {
        try {
            File file = new File(path + fileName);
            FileInputStream fileInputStream = new FileInputStream (file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String csvLine;
            ArrayList<String> latitude = new ArrayList<String>();
            ArrayList<String> longitude = new ArrayList<String>();
            ArrayList<String> loc_name = new ArrayList<String>();
            ArrayList<String> price = new ArrayList<String>();
            int index=0;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] row = csvLine.split(",");
                latitude.add(row[0]);
                longitude.add(row[1]);
                loc_name.add(row[2]);
                price.add(row[3]);
                index += 1;
            }
            ArrayList<String> index_str = new ArrayList<>();
            index_str.add(Integer.toString(index));
            switch(return_type){
                case "latitude":
                    return latitude;
                case "longitude":
                    return longitude;
                case "name":
                    return loc_name;
                case "price":
                    return price;
                case "index":
                    return index_str;
            }

        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        return null;
    }

    public ArrayList<String> search(String return_type) {
        try {
            File file = new File(path + fileName);
            FileInputStream fileInputStream = new FileInputStream (file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String csvLine;

            ArrayList<String> latitude = new ArrayList<String>();
            ArrayList<String> longitude = new ArrayList<String>();
            ArrayList<String> loc_name = new ArrayList<String>();
            ArrayList<String> price = new ArrayList<String>();

            int index=0;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] row = csvLine.split(",");
                latitude.add(row[0]);
                longitude.add(row[1]);
                loc_name.add(row[2]);
                price.add(row[3]);
                index += 1;
            }
            ArrayList<String> index_str = new ArrayList<>();
            index_str.add(Integer.toString(index));
            switch(return_type){
                case "latitude":
                    return latitude;
                case "longitude":
                    return longitude;
                case "name":
                    return loc_name;
                case "price":
                    return price;
                case "index":
                    return index_str;
            }

        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        return null;
    }

}
