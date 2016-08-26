package com.chrisgaddes.reaction;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
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
    private String str_prob_file_name;
    private String part_letter;
    private String str_toolbar_title;

    private String str_problem_number;
    private String[] str_problem_statement;
    private String str_part_letter;
    private String[] str_part_statement;

    private FloatingActionButton btn_start_part;

    public TinyDB tinydb;
    public Context context;

    private Toolbar toolbar;
    private ImageView IV_problem;
    private int resID;
    private ProgressBar progressBar;

    private long timer;
    private ChronometerView rc;

    private long pauseTime;
    private long resumeTime;
    private long totalForgroundTime;


    private Chronometer focus;


//    private Data data = new Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupWindowAnimations();
        setContentView(R.layout.activity_second);

        context = getApplicationContext();

        // Sets database
        tinydb = new TinyDB(this);

        // Loads problem information
        problem_number = tinydb.getInt("problem_number");
        part_letter = tinydb.getString("part_letter");
        str_part_letter = "Part " + part_letter;

        // Generates strings from problem information
        str_problem_number = "Problem #" + problem_number;
        str_prob_file_name = "prob" + problem_number;
        str_toolbar_title = "Problem #" + problem_number;

        // load problem statement and part statement TODO: Remove loading part statement from this activity
        str_problem_statement = getResources().getStringArray(getResId("mainProblemStatement_" + "prob" + problem_number + "_part" + part_letter, R.array.class));
        str_part_statement = getResources().getStringArray(getResId("problemStatement_" + "prob" + problem_number + "_part" + part_letter, R.array.class));

        // Sets text for problem statement
        tv_problem_statement = (TextView) this.findViewById(R.id.tv_problem_statement2);
        tv_problem_statement.setText(str_problem_statement[0]);

        // Sets image for problem
        IV_problem = (ImageView) findViewById(R.id.problem);
        Glide.with(this)
                .load(getResources().getIdentifier(str_prob_file_name, "drawable", getPackageName()))
                .into(IV_problem);


        // Loads views
        mDrawArrowsView = (DrawArrowsView) findViewById(R.id.idDrawArrowsView);
        view = (RelativeLayout) findViewById(R.id.id_2ndRelativeLayout);

        // Sets toolbar title
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(str_toolbar_title);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);


        // Sets listener on start button
        btn_start_part = (FloatingActionButton) findViewById(R.id.btn_start_part);
        btn_start_part.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SecondActivity.this);
                startActivity(intent, options.toBundle());
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(250);
        slide.setSlideEdge(Gravity.RIGHT);

        Fade fade = new Fade();
        fade.setDuration(250);

        Explode explode = new Explode();
        explode.setDuration(250);


        // TODO remove this stuff if it doesn't do anything
////exclude toolbar
//        explode.excludeTarget(R.id.toolbar, true);
//exclude status bar
        explode.excludeTarget(android.R.id.statusBarBackground, true);
//exclude navigation bar
        explode.excludeTarget(android.R.id.navigationBarBackground, true);
//
////        explode.excludeTarget(MainActivity), true);

        getWindow();
        getWindow().setEnterTransition(explode);
        getWindow().setReturnTransition(explode);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
    }


    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus)
            // Saves layout to bitmap
            view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap peekImage = view.getDrawingCache();
//
//
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
        Long tmpTime = tinydb.getLong("TotalForegroundTime", 0);

        if (tmpTime == 0) {
//            tinydb.getLong("TotalForegroundTime", 0) + (pauseTime - resumeTime);
            tinydb.putLong("TotalForegroundTime", 0);
        }

        startTimer(menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void startTimer(Menu menu) {
        rc = (ChronometerView) menu
                .findItem(R.id.timer)
                .getActionView();

        rc.setBeginTime(tinydb.getLong("TotalForegroundTime", 0));
        rc.setOverallDuration(2 * 60);
        rc.setWarningDuration(90);
        rc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        rc.setTextColor(Color.WHITE);
        rc.reset();
        rc.run();
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

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime = System.currentTimeMillis();
        totalForgroundTime = tinydb.getLong("TotalForegroundTime", 0) + (pauseTime - resumeTime);
        tinydb.putLong("TotalForegroundTime", totalForgroundTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeTime = System.currentTimeMillis();
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

    public ViewGroup getActionBar(View view) {
        try {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;

                if (viewGroup instanceof android.support.v7.widget.Toolbar) {
                    return viewGroup;
                }

                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    ViewGroup actionBar = getActionBar(viewGroup.getChildAt(i));

                    if (actionBar != null) {
                        return actionBar;
                    }
                }
            }
        } catch (Exception e) {
        }

        return null;
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