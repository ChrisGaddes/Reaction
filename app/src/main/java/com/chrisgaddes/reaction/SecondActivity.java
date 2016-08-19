package com.chrisgaddes.reaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;


public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";

    //TODO add pinch to zoom http://stackoverflow.com/questions/30979647/how-to-draw-by-finger-on-canvas-after-pinch-to-zoom-coordinates-changed-in-andro

    private DrawArrowsView mDrawArrowsView;
    private ImageView mProblem;
    private Context mContext;
    private ListView listView;

    private RelativeLayout view;

    private TextView tv_problem_statement;
    private TextView tv_problem_number;
    private TextView tv_part_letter;
    private TextView tv_part_statement;

    private int problem_number;
    private String part_letter;

    private String str_problem_number;
    private String[] str_problem_statement;
    private String str_part_letter;
    private String[] str_part_statement;

    private Button btn_start_part;

    public TinyDB tinydb;


//    private Data data = new Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        btn_start_part = (Button) findViewById(R.id.btn_start_part);


        tinydb = new TinyDB(this);

//        tinydb.putInt("problem_number", 9);

        problem_number = tinydb.getInt("problem_number");
        part_letter = tinydb.getString("part_letter");
        str_part_letter = "Part " + part_letter;

        str_problem_number = "Problem #" + problem_number;

        str_problem_statement = getResources().getStringArray(getResId("mainProblemStatement_" + "prob" + problem_number + "_part" + part_letter, R.array.class));
        str_part_statement = getResources().getStringArray(getResId("problemStatement_" + "prob" + problem_number + "_part" + part_letter, R.array.class));

//        tv_part_letter = (TextView) findViewById(R.id.tv_part_letter);
//        tv_part_letter.setText(str_part_letter);
//
//        tv_part_statement = (TextView) this.findViewById(R.id.tv_part_statement);
//        tv_part_statement.setText(str_part_statement[0]);

//        tv_problem_number = (TextView) findViewById(R.id.tv_problem_number);
//        tv_problem_number.setText(str_problem_number);

        tv_problem_statement = (TextView) this.findViewById(R.id.tv_problem_statement2);
        tv_problem_statement.setText(str_problem_statement[0]);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(str_problem_number);

        mDrawArrowsView = (DrawArrowsView) findViewById(R.id.idDrawArrowsView);
        view = (RelativeLayout) findViewById(R.id.id_2ndRelativeLayout);


        btn_start_part.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//                // Saves layout to bitmap
//                RelativeLayout view = (RelativeLayout)findViewById(R.id.id_2ndRelativeLayout);
//                view.setDrawingCacheEnabled(true);
//                view.buildDrawingCache();
//                Bitmap peekImage = view.getDrawingCache();
//
//
//                String strPeekImage = BitMapToString(peekImage);
//                tinydb.putString("PeekImage", strPeekImage);

                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus)
            // Saves layout to bitmap

            view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap peekImage = view.getDrawingCache();


        String strPeekImage = BitMapToString(peekImage);
        tinydb.putString("PeekImage", strPeekImage);

    }

    private void helpDialog() {
        new MaterialStyledDialog(this)
                .setTitle("Help")
                .setDescription(R.string.str_help_dialog_description)
                .setIcon(R.drawable.ic_question_mark)
                .setStyle(Style.HEADER_WITH_ICON)

                .setPositive(getResources().getString(R.string.str_get_started), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Log.d("MaterialStyledDialogs", "Do something!");
                    }
                })

                .show();
    }

    public static int getResId(String variableName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.third_menu, menu);
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent i = new Intent(this, MyPreferencesActivity.class);
                startActivity(i);
                return true;

            case R.id.action_help:
                // User chose the "Help" action, shows help popup
                helpDialog();
                return true;

            case R.id.action_startover:
                // User chose the "Start over" action, resets all arrows
                mDrawArrowsView.resetAllValues();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

/**
 * react to the user tapping/selecting an options menu item
 */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.btn_main_menu:
//                //Toast.makeText(this, "ADD!", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(this, MyPreferencesActivity.class);
//                startActivity(i);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }