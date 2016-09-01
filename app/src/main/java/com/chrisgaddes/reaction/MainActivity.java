package com.chrisgaddes.reaction;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public TinyDB tinydb;
    private int problem_number;
    private String part_letter;

    private Toolbar toolbar;
    private boolean first_launch;
    private Button btn_load_prob_1;
    private Button btn_load_prob_2;
    private Button btn_load_prob_3;

    private Animation slideUp;
    private Animation slideDown;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // loads tinydb database
        tinydb = new TinyDB(this);
        tinydb.remove("TotalForegroundTime"); //TODO: remove this

        // checks if first run or upgrade
        checkFirstRun();

        setupWindowAnimations();
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryLight));
        }

        // Sets toolbar title
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        btn_load_prob_1 = (Button) findViewById(R.id.btn_load_prob_1);
        btn_load_prob_1.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_load_prob_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                problem_number = 1;
                part_letter = "A";
                tinydb.putInt("problem_number", problem_number);
                tinydb.putString("part_letter", part_letter);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent, options.toBundle());
            }
        });

        btn_load_prob_2 = (Button) findViewById(R.id.btn_load_prob_2);
        btn_load_prob_2.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_load_prob_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                problem_number = 2;
                part_letter = "A";
                tinydb.putInt("problem_number", problem_number);
                tinydb.putString("part_letter", part_letter);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent, options.toBundle());
            }
        });

        btn_load_prob_3 = (Button) findViewById(R.id.btn_load_prob_3);
        btn_load_prob_3.setVisibility(View.INVISIBLE);
        findViewById(R.id.btn_load_prob_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                problem_number = 3;
                part_letter = "A";
                tinydb.putInt("problem_number", problem_number);
                tinydb.putString("part_letter", part_letter);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent, options.toBundle());
            }
        });

        slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_load_prob_1.startAnimation(slideUp);
                btn_load_prob_1.setVisibility(View.VISIBLE);
            }
        }, 50);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_load_prob_2.startAnimation(slideUp);
                btn_load_prob_2.setVisibility(View.VISIBLE);
            }
        }, 150);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_load_prob_3.startAnimation(slideUp);
                btn_load_prob_3.setVisibility(View.VISIBLE);
            }
        }, 200);

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

        ////exclude toolbar
        //        explode.excludeTarget(R.id.toolbar, true);
        //exclude status bar
        explode.excludeTarget(android.R.id.statusBarBackground, true);
        //exclude navigation bar
        explode.excludeTarget(android.R.id.navigationBarBackground, true);

        getWindow();
        getWindow().setEnterTransition(fade);
        getWindow().setReturnTransition(explode);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.third_menu, menu);
        Long tmpTime = tinydb.getLong("TotalForegroundTime", 0);

        invalidateOptionsMenu();

        if (tmpTime == 0) {
//            tinydb.getLong("TotalForegroundTime", 0) + (pauseTime - resumeTime);
            tinydb.putLong("TotalForegroundTime", 0);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.timer).setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, MyPreferencesActivity.class);
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    public View getActionBarView() {
        Window window = getWindow();
        View v = window.getDecorView();
        int resId = getResources().getIdentifier("action_bar_container", "id", "android");
        return v.findViewById(resId);
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

    private void checkFirstRun() {
        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;
        // Get current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            // handle exception
            e.printStackTrace();
            return;
        }
        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);
        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            // This is just a normal run
            return;
        } else if (savedVersionCode == DOESNT_EXIST) {
            tinydb.putBoolean("key_first_launch", true);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
            Intent intent = new Intent(MainActivity.this, CanteenIntroActivity.class);
            startActivity(intent, options.toBundle());

            // TODO This is a new install (or the user cleared the shared preferences)
        } else if (currentVersionCode > savedVersionCode) {
            // TODO This is an upgrade
        }
        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();
    }

    private void fadeOutAndHideImage(final ImageView img) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.GONE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(fadeOut);
    }

    private void fadeInAndShowImage(final ImageView img) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(1000);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                img.setVisibility(View.VISIBLE);
            }
        });

        img.startAnimation(fadeIn);
    }

}