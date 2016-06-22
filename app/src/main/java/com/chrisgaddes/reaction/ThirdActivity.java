package com.chrisgaddes.reaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";

    //TODO add pinch to zoom http://stackoverflow.com/questions/30979647/how-to-draw-by-finger-on-canvas-after-pinch-to-zoom-coordinates-changed-in-andro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
    }
}