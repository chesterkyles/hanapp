package com.hanapp;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanapp.R;

import java.util.ArrayList;


public class CustomListAdapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Item> items; //data source of the list adapter

    //public constructor
    public CustomListAdapter(Context context, ArrayList<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.layout_list_view_row_items, parent, false);
        }

        // get current item to be displayed
        Item currentItem = (Item) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.text_view_item_name);
        TextView textViewItemLocation = (TextView)
                convertView.findViewById(R.id.text_view_item_location);
        TextView textViewItemPrice = (TextView)
                convertView.findViewById(R.id.text_view_item_price);

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem.getItemName());
        textViewItemLocation.setText(currentItem.getItemLocation());
        textViewItemPrice.setText(currentItem.getItemPrice());

        // returns the view for the current row
        return convertView;
    }
}