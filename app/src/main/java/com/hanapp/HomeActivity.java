package com.hanapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Button cam_button;
    private ImageButton search_button;

    //Navigation Pane
    private TextView email_ad_str;
    private TextView username_str;
    private TextView reward_pt;
    private Button settings_btn;
    private Button logout_btn;

    String login = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_info);

        cam_button = (Button) findViewById(R.id.camera_button);
        cam_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent cam_page_intent = new Intent(HomeActivity.this, CameraActivity.class);
                startActivity(cam_page_intent);
                finish();
            }
        });

        search_button = (ImageButton) findViewById(R.id.searchBtn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        username_str = (TextView) navigationView.findViewById(R.id.username);
        reward_pt = (TextView) navigationView.findViewById(R.id.reward_point);
        //settings_btn = (Button) navigationView.findViewById(R.id.settings);
        //logout_btn = (Button) navigationView.findViewById(R.id.logout);

        Bundle data = getIntent().getExtras();
        if (data != null) {
            login = data.getString(LoginActivity.LoginObject);
            //email_ad_str.setText(login);

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
