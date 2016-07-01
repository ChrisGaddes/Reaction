package com.chrisgaddes.reaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";

    //TODO add pinch to zoom http://stackoverflow.com/questions/30979647/how-to-draw-by-finger-on-canvas-after-pinch-to-zoom-coordinates-changed-in-andro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        final Button button = (Button) findViewById(R.id.btn_done);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Log.d(TAG, "Button Clicked");
                // TODO JAMES SENTELL: I need this button to trigger a method in DrawArrowsView.java.
                // Perform action on click
            }
        });

    }
}