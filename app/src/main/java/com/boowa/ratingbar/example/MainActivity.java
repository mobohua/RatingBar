package com.boowa.ratingbar.example;

import android.app.Activity;
import android.os.Bundle;

import com.boowa.ratingbar.RatingBar;

public class MainActivity extends Activity {
    private RatingBar mRa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRa = (RatingBar) findViewById(R.id.ratingBar);

    }

}
