package com.hanapp;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class ProductPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        // Get intent data
        Intent i = getIntent();

        // Selected image id
        int position = i.getExtras().getInt("id");
//        CustomListAdapter adapter = new CustomListAdapter(this, );
        //ImageAdapter imageAdapter = new ImageAdapter(this);

        ImageView imageView = (ImageView) findViewById(R.id.product_image);
        //imageView.setImageResource(imageAdapter.mThumbIds[position]);
    }

}