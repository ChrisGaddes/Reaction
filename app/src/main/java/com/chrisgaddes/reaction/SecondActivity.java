package com.chrisgaddes.reaction;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import java.lang.reflect.Field;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";
    private DrawArrowsView mDrawArrowsView;

    private AutoResizeTextView tv_problem_statement;

    private int problem_number;
    private String str_prob_file_name;
    private String part_letter;

    private FloatingActionButton btn_start_part;

    private TinyDB tinydb;

    private long pauseTime;
    private long resumeTime;
    private Handler mHandler = new Handler();

    private TextView TV_time_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupWindowAnimations();
        setContentView(R.layout.activity_second);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Initializes database
        tinydb = new TinyDB(this);

        // Loads problem information
        problem_number = tinydb.getInt("problem_number");
        part_letter = tinydb.getString("part_letter");

        // Generates strings from problem information
        str_prob_file_name = "prob" + problem_number;
        String str_toolbar_problem_title = "Problem #" + problem_number;

        // load problem statement and part statement TODO: Remove loading part statement from this activity
        String[] str_problem_statement = getResources().getStringArray(getResId("mainProblemStatement_" + "prob" + problem_number + "_part" + part_letter, R.array.class));

        // Sets text for problem statement
        tv_problem_statement = (AutoResizeTextView) this.findViewById(R.id.tv_problem_statement2);
        tv_problem_statement.setText(str_problem_statement[0]);
        // set max text size
        tv_problem_statement.setMaxTextSize(56);

        // Loads image for problem
        ImageView IV_problem = (ImageView) findViewById(R.id.problem);
        Glide.with(this)
                .load(getResources().getIdentifier(str_prob_file_name, "drawable", getPackageName()))
                .into(IV_problem);

        IV_problem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Snackbar.make(findViewById(R.id.secondActivityCoordLayout), "Press the red button to begin", Snackbar.LENGTH_SHORT).show();
                return false;
            }
        });

        // Loads views
        mDrawArrowsView = (DrawArrowsView) findViewById(R.id.idDrawArrowsView);

        // Sets toolbar title
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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

    private void showIntro1() {
        new MaterialIntroView.Builder(this)
//                .enableDotAnimation(true)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.ALL)
                .setDelayMillis(500)
                .enableFadeAnimation(true)
                .setTargetPadding(30)
                .setInfoText("This is the main problem statement for Problem 1. Read it carefully and then tap in the circle to continue.")
                .setTarget(findViewById(R.id.tv_problem_statement2))
                .setUsageId("IntroSecondAct1_" + tinydb.getString("ID_IntroView"))
//                .setConfiguration(matIntroConfig)
                .setListener(new MaterialIntroListener() {
                    @Override
                    public void onUserClicked(String materialIntroViewId) {
                        showIntro2();
                    }
                })
                .show();
    }

    private void showIntro2() {
        new MaterialIntroView.Builder(this)
//                .enableDotAnimation(true)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.ALL)
                .setDelayMillis(0)
                .enableFadeAnimation(true)
                .setInfoText("This is the main problem diagram of the whole mechanical system. You will be able to reference it later as needed. Tap in the circle to continue.")
                .setTarget(findViewById(R.id.problem))
                .setUsageId("IntroSecondAct2_" + tinydb.getString("ID_IntroView"))
//                .setConfiguration(matIntroConfig)
                .setListener(new MaterialIntroListener() {
                    @Override
                    public void onUserClicked(String materialIntroViewId) {
                        showIntro3();
                    }
                })
                .show();
    }

    private void showIntro3() {
        new MaterialIntroView.Builder(this)
                .enableDotAnimation(true)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.ALL)
                .setDelayMillis(0)
                .enableFadeAnimation(true)
                .setInfoText("That's all for this screen! Tap on this button to start part A!")
                .setTarget(findViewById(R.id.btn_start_part))
                .setUsageId("IntroSecondAct3_" + tinydb.getString("ID_IntroView"))
                .setListener(new MaterialIntroListener() {
                    @Override
                    public void onUserClicked(String materialIntroViewId) {
                        btn_start_part.performClick();
                    }
                })
                .show();
    }

    private void startThirdActivity() {
        Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SecondActivity.this);
//        startActivity(intent, options.toBundle());
        startActivity(intent);//, options.toBundle());
        finish();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Explode explode = new Explode();
        explode.setDuration(250);

        // TODO remove this stuff if it doesn't do anything
        //exclude status bar
        explode.excludeTarget(android.R.id.statusBarBackground, true);

        //exclude navigation bar
        explode.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow();
        getWindow().setEnterTransition(explode);
        getWindow().setReturnTransition(explode);
        getWindow().setAllowEnterTransitionOverlap(false);
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
            tinydb.putLong("TotalForegroundTime", 0);
        }

        MenuItem startover = menu.findItem(R.id.action_startover);
        startover.setVisible(false);


        MenuItem help = menu.findItem(R.id.action_help);
        help.setVisible(false);

        MenuItem timer = menu.findItem(R.id.timer);
        timer.setVisible(false);

        showIntro1();

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

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime = System.currentTimeMillis();
        long totalForgroundTime = tinydb.getLong("TotalForegroundTime", 0) + (pauseTime - resumeTime);
        tinydb.putLong("TotalForegroundTime", totalForgroundTime);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //update foreground time
        resumeTime = System.currentTimeMillis();
    }
}