package com.hanapp;

public class Item {
    private String itemName;
    private String itemLocation;
    private String itemPrice;


    public Item(String name, String location, String price) {
        this.itemName = name;
        this.itemLocation = location;
        this.itemPrice = price;
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
}