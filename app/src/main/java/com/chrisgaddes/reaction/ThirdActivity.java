package com.chrisgaddes.reaction;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";

    //TODO add pinch to zoom http://stackoverflow.com/questions/30979647/how-to-draw-by-finger-on-canvas-after-pinch-to-zoom-coordinates-changed-in-andro

    DrawArrowsView mDrawArrowsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        mDrawArrowsView = (DrawArrowsView) findViewById(R.id.idDrawArrowsView);

        final FloatingActionButton btn_check_done = (FloatingActionButton) findViewById(R.id.btn_check_done);
        btn_check_done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDrawArrowsView.runCheckIfFinished();
            }
        });

        final Button mbtn_refresh = (Button) findViewById(R.id.btn_refresh);
        mbtn_refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDrawArrowsView.resetAllValues();
            }
        });
    }
}