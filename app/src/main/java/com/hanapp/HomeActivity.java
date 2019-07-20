package com.hanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private ImageButton cam_button;

    //Navigation Pane
    private TextView email_ad_str;
    private TextView username_str;
    private TextView reward_pt;
    private ImageButton settings_btn;
    private ImageButton logout_btn;

    private GridView gridview;

    String login = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);

        cam_button = (ImageButton) findViewById(R.id.camera_button);

        cam_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent cam_page_intent = new Intent(HomeActivity.this, CameraActivity.class);
                startActivity(cam_page_intent);
                finish();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id){
                // Send intent to SingleViewActivity
                Intent i = new Intent(getApplicationContext(), ProductPageActivity.class);
                // Pass image index
                i.putExtra("id", position);
                startActivity(i);
            }
        });

        Bundle data = getIntent().getExtras();
        if (data != null) {
            login = data.getString(LoginActivity.LoginObject);
            email_ad_str.setText(login);

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

        int i = 0;

        for (i = 0; i < 10; i++){
            Item item_holder = new Item("Test".concat(Integer.toString(i)), "Tested".concat(Integer.toString(i)));
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
