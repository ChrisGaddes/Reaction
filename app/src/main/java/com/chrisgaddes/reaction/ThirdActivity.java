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
        DrawArrowsView ev = new DrawArrowsView(this);
//        ev.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
        setContentView(ev);

        //TODO: learn how to generate view Id https://gist.github.com/omegasoft7/fdf7225a5b2955a1aba8
        //ev.generateViewId();

        //Resources resources = getResources();
//        ImageView imagehey = new ImageView(this);
//        imagehey.setImageDrawable(resources.getDrawable(R.drawable.fbd_1));
    }


}