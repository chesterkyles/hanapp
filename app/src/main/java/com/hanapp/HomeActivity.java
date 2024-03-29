package com.hanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import android.app.Activity;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ImageButton cam_button;
    private ImageButton search_button;

    //Navigation Pane
    private TextView email_ad_str;
    private TextView username_str;
    private TextView reward_pt;
    private ImageButton settings_btn;
    private ImageButton logout_btn;

    private GridView gridview;

    String login = null;

    String search_str;
    String search_path = "/sdcard/CSV_Files/";
    String search_fileName = "items_new.csv";

    EditText search_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);

        File clear_this = new File ("/sdcard/CSV_Files/search_result.csv");
        clear_this.delete();
        try {
            clear_this.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cam_button = (ImageButton) findViewById(R.id.camera_button);
        search_button = (ImageButton) findViewById(R.id.search_button);
        search_view = (EditText) findViewById(R.id.search_edit);
        
        cam_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent cam_page_intent = new Intent(HomeActivity.this, CameraActivity.class);
                startActivity(cam_page_intent);
                finish();
            }
        });

        search_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                search_str = search_view.getText().toString();
                CsvFileInOut csv_file = new CsvFileInOut(search_path, search_fileName);
                csv_file.search_item(search_str);
                Intent maps_intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(maps_intent);
                finish();
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

//        email_ad_str = (TextView) navigationView.findViewById(R.id.email_ad);
        username_str = (TextView) navigationView.findViewById(R.id.username);
        reward_pt = (TextView) navigationView.findViewById(R.id.reward_point);
        settings_btn = (ImageButton) navigationView.findViewById(R.id.settings);
        logout_btn = (ImageButton) navigationView.findViewById(R.id.logout);

        ArrayList<Item> itemsArrayList = generateItemsList();

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new CustomListAdapter(this, itemsArrayList));

//        gridview.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent,
//                                    View v, int position, long id){
//                // Send intent to SingleViewActivity
//                Intent i = new Intent(getApplicationContext(), ProductPageActivity.class);
//                // Pass image index
//                i.putExtra("id", position);
//
////                Bundle extras = new Bundle();
////                extras.putString("EXTRA_USERNAME","my_username");
////                extras.putString("EXTRA_PASSWORD","my_password");
////                i.putExtras(extras);
//
//                startActivity(i);
//            }
//        });

        Bundle data = getIntent().getExtras();
        if (data != null) {
            login = data.getString(LoginActivity.LoginObject);

            String path = "/sdcard/CSV_Files/";
            String fileName = "user.csv";
            CsvFileInOut csvFile = new CsvFileInOut(path,fileName);
            String[] string_value = csvFile.read(login);

            if (string_value != null) {
                username_str.setText(string_value[2]);
                reward_pt.setText(string_value[3]);
            }
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private ArrayList<Item> generateItemsList() {
        ArrayList<Item> array;
        array = new ArrayList<Item>();

        String path = "/sdcard/CSV_Files/";
        String fileName = "items_new.csv";
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
}
