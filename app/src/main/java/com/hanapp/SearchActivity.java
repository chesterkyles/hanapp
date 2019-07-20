package com.hanapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    Dialog promptDialog;
    ImageButton mapBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);

        ArrayList<Item> itemsArrayList = generateItemsList();

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new CustomListAdapter(this, itemsArrayList));

        mapBtn = (ImageButton) findViewById(R.id.map1);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map_intent = new Intent(SearchActivity.this, Maps.class);
                startActivity(map_intent);
                finish();
            }
        });

    }

    private ArrayList<Item> generateItemsList() {
        ArrayList<Item> array;
        array = new ArrayList<Item>();

        String path = "/sdcard/CSV_Files/";
        String fileName = "search_results.csv";
        String item_name;
        String item_location;
        String item_price;
        String item_path;

        CsvFileInOut items_csv = new CsvFileInOut(path, fileName);
        int max_index = Integer.parseInt(items_csv.search("index").get(0));
        for (int ind=0; ind<max_index; ind++){
            item_name = items_csv.search("product").get(ind);
            item_location = items_csv.search("place").get(ind);
            item_price = items_csv.search("price").get(ind);
            item_path = items_csv.search("path").get(ind);

            Item item_holder = new Item(item_name, item_location, item_price, item_path);
            array.add(item_holder);
        }

        return array;
    }

    @Override
    public void onBackPressed() {
        Intent input_to_home_intent = new Intent(SearchActivity.this,HomeActivity.class);
        startActivity(input_to_home_intent);
        finish();
    }

}



