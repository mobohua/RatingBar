package com.boowa.ratingbar.example;

import android.app.Activity;
import android.os.Bundle;

import com.boowa.ratingbar.StarBar;

public class MainActivity extends Activity {
    private StarBar mStarBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStarBar = (StarBar) findViewById(R.id.starBar);
        mStarBar.setStarMark(1.5f);
    }

}
