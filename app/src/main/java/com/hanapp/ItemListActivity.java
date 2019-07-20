package com.hanapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.File;

public class ItemListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private TextView barcodeValue;
    private EditText itemName;
    private EditText manufacturer;
    private EditText itemPrice;
    private EditText locationName;
    private Button submitInfo;
    private ImageView itemPicture;
    Dialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        promptDialog = new Dialog(this);

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
}

