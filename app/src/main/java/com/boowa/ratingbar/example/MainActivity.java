package com.boowa.ratingbar.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.boowa.ratingbar.RatingBar;

public class MainActivity extends Activity {
    private RatingBar mRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRb = (RatingBar) findViewById(R.id.ratingBar);


    }

}
