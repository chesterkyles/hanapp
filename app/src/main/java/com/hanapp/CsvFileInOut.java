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
    String database = "/sdcard/CSV_Files/items_new.csv";
    String loc_database = "/sdcard/CSV_Files/loc_database.csv";
    String search = "/sdcard/CSV_Files/search_result.csv";

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

            ArrayList<String> barcode = new ArrayList<String>();
            ArrayList<String> product_name = new ArrayList<String>();
            ArrayList<String> company = new ArrayList<String>();
            ArrayList<String> place_name = new ArrayList<String>();
            ArrayList<String> price = new ArrayList<String>();
            ArrayList<String> path = new ArrayList<String>();
            ArrayList<String> location_latitude = new ArrayList<String>();
            ArrayList<String> location_longitude = new ArrayList<String>();

            int index=0;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] row = csvLine.split(",");
                barcode.add(row[0]);
                product_name.add(row[1]);
                company.add(row[2]);
                place_name.add(row[3]);
                price.add(row[4]);
                path.add(row[5]);
                location_latitude.add(row[6]);
                location_longitude.add(row[7]);
                index += 1;
            }
            ArrayList<String> index_str = new ArrayList<>();
            index_str.add(Integer.toString(index));
            switch(return_type){
                case "barcode":
                    return barcode;
                case "product":
                    return product_name;
                case "company":
                    return company;
                case "place":
                    return place_name;
                case "price":
                    return price;
                case "path":
                    return path;
                case "latitude":
                    return location_latitude;
                case "longitude":
                    return location_longitude;
                case "index":
                    return index_str;
            }

        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        return null;
    }
    public void search_item(String search_this) {
        try {
            File file = new File(path + fileName);
            FileInputStream fileInputStream = new FileInputStream (file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            File file_s = new File(search);
            FileOutputStream fileOutputStream_s = new FileOutputStream(file_s);
            OutputStreamWriter outputStreamWriter_s = new OutputStreamWriter(fileOutputStream_s);
            BufferedWriter bufferedWriter_s = new BufferedWriter (outputStreamWriter_s);

            String csvLine;
            String printToSearchFile;

            ArrayList<String> barcode = new ArrayList<String>();
            ArrayList<String> product_name = new ArrayList<String>();
            ArrayList<String> company = new ArrayList<String>();
            ArrayList<String> place_name = new ArrayList<String>();
            ArrayList<String> price = new ArrayList<String>();
            ArrayList<String> path = new ArrayList<String>();
            ArrayList<String> location_latitude = new ArrayList<String>();
            ArrayList<String> location_longitude = new ArrayList<String>();

            int index=0;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] row = csvLine.split(",");
                barcode.add(row[0]);
                product_name.add(row[1]);
                company.add(row[2]);
                place_name.add(row[3]);
                price.add(row[4]);
                path.add(row[5]);
                location_latitude.add(row[6]);
                location_longitude.add(row[7]);
                index += 1;
            }
            if (file_s.exists()) {
                file.delete();
            }
            file.createNewFile();

            for (int ind=0; ind<index; ind++){
                if(product_name.get(ind).equals(search_this)) {
                    printToSearchFile = barcode.get(ind) + "," + product_name.get(ind) + "," + company.get(ind) + "," + place_name.get(ind) + "," + price.get(ind) + "," + path.get(ind) + "," + location_latitude.get(ind) + "," + location_longitude.get(ind) + ",";
                    bufferedWriter_s.write(printToSearchFile);
                }
            }
            bufferedWriter_s.flush();
            bufferedWriter_s.close();

        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
    }
    public void to_database() {
        try {
            File file = new File(path + fileName);
            FileInputStream fileInputStream = new FileInputStream (file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            File file_db = new File(database);
            FileOutputStream fileOutputStream_db = new FileOutputStream(file_db);
            OutputStreamWriter outputStreamWriter_db = new OutputStreamWriter(fileOutputStream_db);
            BufferedWriter bufferedWriter_db = new BufferedWriter (outputStreamWriter_db);

            String csvLine;

            ArrayList<String> barcode = new ArrayList<String>();
            ArrayList<String> product_name = new ArrayList<String>();
            ArrayList<String> company = new ArrayList<String>();
            ArrayList<String> place_name = new ArrayList<String>();
            ArrayList<String> price = new ArrayList<String>();
            ArrayList<String> path = new ArrayList<String>();
            ArrayList<String> location_latitude = new ArrayList<String>();
            ArrayList<String> location_longitude = new ArrayList<String>();

            int index_db=0;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] row = csvLine.split(",");
                barcode.add(row[0]);
                product_name.add(row[1]);
                company.add(row[2]);
                place_name.add(row[3]);
                price.add(row[4]);
                path.add(row[5]);
                location_latitude.add(row[6]);
                location_longitude.add(row[7]);

                bufferedWriter_db.write(csvLine);
                index_db += 1;
            }
            bufferedWriter_db.flush();
            bufferedWriter_db.close();
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
    }
}
