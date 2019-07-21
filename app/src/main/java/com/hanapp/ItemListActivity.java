package com.hanapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.contains;

public class ItemListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Dialog promptDialog;
    ImageButton submitBtn;
    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        ArrayList<Item> itemsArrayList = generateItemsList();

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new CustomListAdapter_OneRow(this, itemsArrayList));

        promptDialog = new Dialog(this);

        submitBtn = (ImageButton) findViewById(R.id.submit_items);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrompt(false);
            }
        });

        /*
        String path = "/sdcard/CSV_Files/" ;
        String fileName = "itemlist.csv";
        CsvFileInOut csvFile = new CsvFileInOut(path,fileName);
        String[] string_value = csvFile.read(null);

        if (string_value != null) {

        }*/
    }

    @Override
    public void onBackPressed(){
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showPrompt(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){ return true;}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { return true;}

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showPrompt(Boolean prompt_exit) {
        ImageButton cancel;
        ImageButton submit;
        TextView prompt;

        promptDialog.setContentView(R.layout.prompt);
        prompt = (TextView) promptDialog.findViewById(R.id.prompt_text);
        cancel = (ImageButton) promptDialog.findViewById(R.id.submit_no);
        submit = (ImageButton) promptDialog.findViewById(R.id.submit_yes);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                promptDialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                promptDialog.dismiss();
                Intent input_to_home_intent = new Intent(ItemListActivity.this,HomeActivity.class);
                startActivity(input_to_home_intent);
                finish();
            }
        });

        if(prompt_exit) prompt.setText("Are you sure you want to exit?");
        promptDialog.show();
    }

    private ArrayList<Item> generateItemsList() {
        ArrayList<Item> array;
        array = new ArrayList<Item>();

        String path = "/sdcard/CSV_Files/";
        String fileName = "ocr.csv";
        String item_name;
        String item_location;
        String item_price;
        String item_path;
        int num_entries = 0;

        CsvFileInOut items_csv = new CsvFileInOut(path, fileName);

        try {
            File file = new File(path + fileName);
            FileInputStream fileInputStream = new FileInputStream (file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String csvLine;

            ArrayList<String> name = new ArrayList<String>();
            ArrayList<Double> price = new ArrayList<Double>();

            int index=0;
            while ((csvLine = bufferedReader.readLine()) != null) {
                Log.d("CSV", csvLine);
                if(csvLine.matches("\\d+(?:\\.\\d+)?")){
                        price.add(Double.valueOf(csvLine));
                        index += 1;
                        Log.d("NUMBER!", csvLine);
                }
                else {
                    if (contains(csvLine.toLowerCase(), "total")) {
                        continue;
                    } else if (contains(csvLine.toLowerCase(), "change")) {
                        continue;
                    } else if (contains(csvLine.toLowerCase(), "due")) {
                        continue;
                    } else if (contains(csvLine.toLowerCase(), "member")) {
                        continue;
                    } else if (contains(csvLine.toLowerCase(), "card")) {
                        continue;
                    } else if (contains(csvLine.toLowerCase(), "cash")){
                        continue;
                    }
                    else {
                            name.add(csvLine);
                            Log.d("TEXT!", csvLine);
                    }

                }
            }

//            ArrayList<String> values=new ArrayList<String>();
//            HashSet<String> hashSet = new HashSet<String>();
//            hashSet.addAll(values);
//            values.clear();
//            values.addAll(hashSet);

            int i = 0;
            double total = 0;
            while ((i < index) && (price.get(i) != total)) {
                total = total + price.get(i);
                i++;
            }

            num_entries = i-1;

            for (int ind=0; ind<num_entries; ind++){
                item_name = name.get(ind);
                item_location = "Robinson's Manila";
                item_price = Double.toString(price.get(ind));
                item_path = "/sdcard/ImageAndroid/Tomi_Super_Sweet_Corn.jpg";

                Item item_holder = new Item(item_name, item_location, item_price, item_path);
                array.add(item_holder);
            }



        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }




        return array;
    }
}

