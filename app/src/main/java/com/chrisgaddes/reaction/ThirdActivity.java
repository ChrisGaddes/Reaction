package com.chrisgaddes.reaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";

    public int screenWidth;

    //TODO add pinch to zoom http://stackoverflow.com/questions/30979647/how-to-draw-by-finger-on-canvas-after-pinch-to-zoom-coordinates-changed-in-andro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);


//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;


//
//

//            Display display = getWindowManager().getDefaultDisplay();
//        String displayName = display.getName();  // minSdkVersion=17+
//        Log.i(TAG, "displayName  = " + displayName);
//
//// display size in pixels
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;
//        Log.i(TAG, "width        = " + width);
//        Log.i(TAG, "height       = " + height);
//
//// pixels, dpi
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int heightPixels = metrics.heightPixels;
//        int widthPixels = metrics.widthPixels;
//        int densityDpi = metrics.densityDpi;
//        float xdpi = metrics.xdpi;
//        float ydpi = metrics.ydpi;
//        Log.i(TAG, "widthPixels  = " + widthPixels);
//        Log.i(TAG, "heightPixels = " + heightPixels);
//        Log.i(TAG, "densityDpi   = " + densityDpi);
//        Log.i(TAG, "xdpi         = " + xdpi);
//        Log.i(TAG, "ydpi         = " + ydpi);
//
//// deprecated
//        int screenHeight = display.getHeight();
//        screenWidth = display.getWidth();
//        Log.i(TAG, "screenHeight = " + screenHeight);
//        Log.i(TAG, "screenWidth  = " + screenWidth);
//
//// orientation (either ORIENTATION_LANDSCAPE, ORIENTATION_PORTRAIT)
//        int orientation = getResources().getConfiguration().orientation;
//        Log.i(TAG, "orientation  = " + orientation);



    }
}