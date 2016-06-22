package com.chrisgaddes.reaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

public class MainActivity extends AppCompatActivity { // implements View.OnTouchListener {

    private static final String TAG = "MainActivity";


    // TODO Learn how to draw in Canvas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        final Intent pIntent = new Intent(MainActivity.this, SecondActivity.class);

        findViewById(R.id.btn_load_second_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(intent);
                //startActivity(SecondActivity.newIntent(MainActivity.this));




                }
        });
//
//
//        //final View popupContent = getLayoutInflater().inflate(R.layout.popup_main, null);
//        //final View popupContent = getLayoutInflater().inflate(R.layout.popup_main, mmain_activity_Relative_Layout, false);
//
//        // set dimension of popup window. The xml file is set to match parent
//        final int dim_popup_window = 500; //TODO change this to relative size
//        //final PopupWindow window = new PopupWindow(popupContent, dim_popup_window, dim_popup_window);
//
//        //final PopupWindow window = new PopupWindow(popupContent, ViewGroup.LayoutParams.WRAP_CONTENT,
//        //ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        // b is the name of the button that
//
//        final Button b = (Button) findViewById(R.id.btn_pt_1);
//
//        if (b == null) return;
//        b.setOnTouchListener(new View.OnTouchListener() {
//                                 @Override
//                                 public boolean onTouch(View v, MotionEvent event) {
//                                     switch (event.getAction()) {
//                                         case MotionEvent.ACTION_DOWN:
//
//                                             // TODO check if button is at a point first
//
//                                             // gets location of touch
//                                             float[] loc_raw_touch = {event.getRawX(), event.getRawY()};
//
//
//                                             int num_btns = 4;
//
//                                             // virtual location of "btn_1"
//                                             int[] arr_btns_x = new int[num_btns];
//                                             int[] arr_btns_y = new int[num_btns];
//
//
//                                             // Get the screen's density scale
//                                             //final float scale = getResources().getDisplayMetrics().density;
//// Convert the dps to pixels, based on density scale
//                                             //  mGestureThreshold = (int) (GESTURE_THRESHOLD_DP * scale + 0.5f);
//
//// Use mGestureThreshold as a distance in pixels...
//
//
//                                             arr_btns_x[0] = 226;
//                                             arr_btns_y[0] = 865;
//
//                                             arr_btns_x[1] = 230;
//                                             arr_btns_y[1] = 1817;
//
//                                             arr_btns_x[2] = 1250;
//                                             arr_btns_y[2] = 1820;
//
//
////                        pIntent.putExtra("sampleKey","THis message is being sent");
////                        startActivity(pIntent);
//
//
//                                             int len_arr_btns = arr_btns_x.length;
//
//                                             //TODO convert px to dp
//                                             // width and height of buttons
//                                             final int btn_dim = 300;
//
//
//                                             for (int i = 0; i < len_arr_btns; i++) {
//
//                                                 // this rect is the button
//
//                                                 //public Rect
//
//                                                 Rect rect_btn_1_spot = new Rect(arr_btns_x[i] - btn_dim / 2, arr_btns_y[i] - btn_dim / 2,
//                                                         arr_btns_x[i] + btn_dim / 2, arr_btns_y[i] + btn_dim / 2);
//                                                 Log.d(TAG, "Rectangle Coordinates" + rect_btn_1_spot);
//
//                                                 if (rect_btn_1_spot.contains((int) event.getRawX(), (int) event.getRawY())) {
//                                                     Snackbar.make(v, "Clicked on button in spot", Snackbar.LENGTH_SHORT).show();
//                                                     OnPtBtnActionDown(v, dim_popup_window, window, loc_raw_touch);
//
//                                                 } else {
//                                                     // do stuff
//                                                 }
//                                             }
//
//                                             Log.d(TAG, "Point clicked, x:" + loc_raw_touch[0]);
//                                             Log.d(TAG, "Point clicked, y:" + loc_raw_touch[1]);
//
//                                             break;
//                                         case MotionEvent.ACTION_UP:
//
//                                             // TODO http://stackoverflow.com/questions/21872464/get-button-coordinates-and-detect-if-finger-is-over-them-android
//
//                                             //TODO put these all in an array
//
//                                             // Initialize popup buttons
//
//                                             Button b1 = (Button) popupContent.findViewById(R.id.btn_1_1);
//                                             Button b2 = (Button) popupContent.findViewById(R.id.btn_1_2);
//                                             Button b3 = (Button) popupContent.findViewById(R.id.btn_1_3);
//                                             Button b4 = (Button) popupContent.findViewById(R.id.btn_1_4);
//                                             Button b5 = (Button) popupContent.findViewById(R.id.btn_1_5);
//
//                                             int[] b1Location = new int[2];
//                                             b1.getLocationOnScreen(b1Location);
//                                             Rect b1Rect = new Rect(b1Location[0], b1Location[1],
//                                                     b1Location[0] + b1.getWidth(), b1Location[1] + b1.getHeight());
//
//                                             int[] b2Location = new int[2];
//                                             b2.getLocationOnScreen(b2Location);
//                                             Rect b2Rect = new Rect(b2Location[0], b2Location[1],
//                                                     b2Location[0] + b2.getWidth(), b2Location[1] + b2.getHeight());
//
//                                             int[] b3Location = new int[2];
//                                             b3.getLocationOnScreen(b3Location);
//                                             Rect b3Rect = new Rect(b3Location[0], b3Location[1],
//                                                     b3Location[0] + b3.getWidth(), b3Location[1] + b3.getHeight());
//
//                                             int[] b4Location = new int[2];
//                                             b4.getLocationOnScreen(b4Location);
//                                             Rect b4Rect = new Rect(b4Location[0], b4Location[1],
//                                                     b4Location[0] + b4.getWidth(), b4Location[1] + b4.getHeight());
//
//                                             int[] b5Location = new int[2];
//                                             b5.getLocationOnScreen(b5Location);
//                                             Rect b5Rect = new Rect(b5Location[0], b5Location[1],
//                                                     b5Location[0] + b5.getWidth(), b5Location[1] + b5.getHeight());
//
//                                             if (b1Rect.contains((int) event.getRawX(), (int) event.getRawY())) {
//                                                 Log.d("MainActivty", "Released on Button1");
//                                                 // Toast.makeText(MainActivity.this, "Released on Button1", Toast.LENGTH_SHORT).show();
//                                                 Snackbar.make(v, "Released on Button 1", Snackbar.LENGTH_SHORT).show();
//
//                                             } else if (b2Rect.contains((int) event.getRawX(), (int) event.getRawY())) {
//                                                 Log.d("MainActivty", "Released on Button2");
//                                                 Snackbar.make(v, "Released on Button 2", Snackbar.LENGTH_SHORT).show();
//
//                                             } else if (b3Rect.contains((int) event.getRawX(), (int) event.getRawY())) {
//                                                 Log.d("MainActivty", "Released on Button3");
//                                                 Snackbar.make(v, "Released on Button 3", Snackbar.LENGTH_SHORT).show();
//
//                                             } else if (b4Rect.contains((int) event.getRawX(), (int) event.getRawY())) {
//                                                 Log.d("MainActivty", "Released on Button4");
//                                                 Snackbar.make(v, "Released on Button 4", Snackbar.LENGTH_SHORT).show();
//
//                                             } else if (b5Rect.contains((int) event.getRawX(), (int) event.getRawY())) {
//                                                 Log.d("MainActivty", "Released on Moment");
//                                                 Snackbar.make(v, "Released on Moment", Snackbar.LENGTH_SHORT).show();
//
//                                             } else {
//                                                 Log.d("MainActivity", "Didn't drag to a button");
//                                                 Snackbar.make(v, "Tap and hold to select button", Snackbar.LENGTH_LONG).show();
//                                             }
//                                             window.dismiss();
//                                             break;
//                                     }
//                                     return v.onTouchEvent(event);
//                                 }
//                             }
//        );
    }

    private void OnPtBtnActionDown(View v, int dim_popup_window, PopupWindow window, float loc_raw_touch[]) {

        int offset_center_bRect_x = (int) loc_raw_touch[0] - dim_popup_window / 2;
        int offset_center_bRect_y = (int) loc_raw_touch[1] - dim_popup_window / 2;

        Log.d(TAG, "Center of button, x:" + offset_center_bRect_x);
        Log.d(TAG, "Center of button, y:" + offset_center_bRect_y);

        // TODO change location of this popup to center of button pressed in database
        window.showAtLocation(v, Gravity.NO_GRAVITY, offset_center_bRect_x, offset_center_bRect_y);
    }
}