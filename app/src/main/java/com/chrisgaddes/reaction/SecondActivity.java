package com.chrisgaddes.reaction;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;


public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";

    //TODO add pinch to zoom http://stackoverflow.com/questions/30979647/how-to-draw-by-finger-on-canvas-after-pinch-to-zoom-coordinates-changed-in-andro

    private DrawArrowsView mDrawArrowsView;

    private AutoResizeTextView tv_problem_statement;

    private int problem_number;
    private String str_prob_file_name;
    private String part_letter;
    private String str_toolbar_problem_title;

    private String[] str_problem_statement;

    private FloatingActionButton btn_start_part;

    private TinyDB tinydb;
    private Context context;

    private Toolbar toolbar;
    private ImageView IV_problem;
    private ChronometerView rc;

    private long pauseTime;
    private long resumeTime;
    private long totalForgroundTime;
    private Handler mHandler = new Handler();
    private Handler mHandler2 = new Handler();

    private Animation fadeIn;
    private Animation fadeOut;

    private Animation scaleIn;
    private Animation scaleOut;

    private boolean rc_run_yet;

    private TextView TV_time_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupWindowAnimations();
        setContentView(R.layout.activity_second);


        TV_time_display = (TextView) findViewById(R.id.time_display);

//        // Thread which updates timer
//        Thread t = new Thread() {
//
//            @Override
//            public void run() {
//                try {
//                    while (!isInterrupted()) {
//                        Thread.sleep(100);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                updateTextView();
//                            }
//                        });
//                    }
//                } catch (InterruptedException e) {
//                }
//            }
//        };
//        t.start();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

        scaleIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_fab_in);
        scaleOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_fab_out);

        context = getApplicationContext();

        // Sets database
        tinydb = new TinyDB(this);


        // Loads problem information
        problem_number = tinydb.getInt("problem_number");
        part_letter = tinydb.getString("part_letter");

        // skips second activity if not part A
//        if (!part_letter.equals("A")) {
//            startThirdActivity();


        // Generates strings from problem information

        str_prob_file_name = "prob" + problem_number;
        str_toolbar_problem_title = "Problem #" + problem_number;

        // load problem statement and part statement TODO: Remove loading part statement from this activity
        str_problem_statement = getResources().getStringArray(getResId("mainProblemStatement_" + "prob" + problem_number + "_part" + part_letter, R.array.class));


        // Sets text for problem statement
        tv_problem_statement = (AutoResizeTextView) this.findViewById(R.id.tv_problem_statement2);
        tv_problem_statement.setText(str_problem_statement[0]);
        // set max text size
        tv_problem_statement.setMaxTextSize(56);

        // Loads image for problem
        IV_problem = (ImageView) findViewById(R.id.problem);
//        IV_problem.setAlpha((float) 0.0);
        Glide.with(this)
                .load(getResources().getIdentifier(str_prob_file_name, "drawable", getPackageName()))
                .into(IV_problem);
//        IV_problem.startAnimation(scaleIn);
//        IV_problem.setAlpha((float) 1.0);

        IV_problem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Toast.makeText(SecondActivity.this, "Press Red button to begin!", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.secondActivityCoordLayout), "Press the red button to begin", Snackbar.LENGTH_SHORT).show();
                return false;
            }
        });

        // Loads views
        mDrawArrowsView = (DrawArrowsView) findViewById(R.id.idDrawArrowsView);

        // Sets toolbar title
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(str_toolbar_problem_title);

        // Sets listener on start button
        btn_start_part = (FloatingActionButton) findViewById(R.id.btn_start_part);
        btn_start_part.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                rc.stop();
                startThirdActivity();
            }
        });

        btn_start_part.hide();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_start_part.show();
            }
        }, 350);


    }

    private void startThirdActivity() {
        Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SecondActivity.this);
//        startActivity(intent, options.toBundle());
        startActivity(intent);//, options.toBundle());
        finish();
//        overridePendingTransition(0,0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Slide slide = new Slide();
        slide.setDuration(250);
        slide.setSlideEdge(Gravity.END);

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
//        getWindow().setAllowReturnTransitionOverlap(false);
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

    private int getResId(String variableName, Class<?> c) {
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

        MenuItem startover = menu.findItem(R.id.action_startover);
        startover.setVisible(false);


        MenuItem timer = menu.findItem(R.id.timer);
        timer.setVisible(false);

//        startTimer(menu);

        return super.onCreateOptionsMenu(menu);
    }

//    private void startTimer(Menu menu) {
//        rc = (ChronometerView) menu
//                .findItem(R.id.timer)
//                .getActionView();
//
//        rc.setPauseTimeOffset(tinydb.getLong("TotalForegroundTime", 0L));
//        rc.setOverallDuration(2 * 60);
//        rc.setWarningDuration(90);
//        rc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
//        rc.reset();
//        rc.run();
//
//        rc_run_yet = true;
//    }

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

//        if (rc != null) {
//            rc.stop();
            pauseTime = System.currentTimeMillis();
            totalForgroundTime = tinydb.getLong("TotalForegroundTime", 0) + (pauseTime - resumeTime);
            tinydb.putLong("TotalForegroundTime", totalForgroundTime);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //update foreground time
        resumeTime = System.currentTimeMillis();

//        // skips second activity if not part A
//        if (!part_letter.equals("A")) {
//            startThirdActivity();
//        }

//        if (rc_run_yet) {
//            // prevents rc from running before startTimer begins
//            rc.run();
//        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG + "onDestroy", "Destroyed");
    }

    private void updateTextView() {
        pauseTime = System.currentTimeMillis();
        long time_string = TimeUnit.MILLISECONDS.toSeconds(tinydb.getLong("TotalForegroundTime", 0) + (pauseTime - resumeTime));

        long days = time_string / 86400;
        long hours = (time_string % 86400) / 3600;
        long minutes = ((time_string % 86400) % 3600) / 60;
        long seconds = ((time_string % 86400) % 3600) % 60;

        pauseTime = System.currentTimeMillis();

        if (TV_time_display != null) {
            TV_time_display.setText(String.format("%d:%02d:%02d", hours, minutes, seconds));
        }
    }

}