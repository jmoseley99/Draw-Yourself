package com.example.drawingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * This class is the homepage of the app. The layout is very simple as it is only the first
 * page. A button is used to move to the main activity, initiated by the 'changeActivity' method
 * in this class.
 */
public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }

    public void changeActivity(View view){
        startActivity(new Intent(Home.this, MainActivity.class));
    }
}
