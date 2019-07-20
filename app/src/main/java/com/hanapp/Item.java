package com.hanapp;

public class Item {
    private String itemName;
    private String itemLocation;
    private String itemPrice;
    private String itemPath;


    public Item(String name, String location, String price, String path) {
        this.itemName = name;
        this.itemLocation = location;
        this.itemPrice = price;
        this.itemPath = path;
    }

    public String getItemName() {
        return this.itemName;
    }

    public String getItemLocation() {
        return this.itemLocation;
    }

    public String getItemPrice() {
        return this.itemPrice;
    }

    public String getItemPath() {
        return this.itemPath;
    }
}