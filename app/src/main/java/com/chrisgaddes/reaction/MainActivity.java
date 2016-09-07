package com.chrisgaddes.reaction;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.florent37.viewanimator.ViewAnimator;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.TimeUnit;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TinyDB tinydb;
    private int problem_number;
    private String part_letter;

    private long pauseTime;
    private long resumeTime;
    private long totalForgroundTime;

    private Toolbar toolbar;
    private boolean first_launch;
    private ImageButton top_view;
    private ImageButton reset_timer;
    private CardView card_load_prob_1;
    private CardView card_load_prob_2;
    private CardView card_load_prob_3;

    private Animation slideUp;
    private Animation slideDown;

    private TextView TV_time_display;

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LeakCanary.install(getApplication());


        // loads tinydb database
        tinydb = new TinyDB(this);


        // checks if first run or upgrade
        checkFirstRun();

        setupWindowAnimations();
        setContentView(R.layout.activity_main);


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

        // enables overscroll animation like in IOS
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollview_main_activity);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);


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

//        // Sets toolbar title
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");


        top_view = (ImageButton) findViewById(R.id.top_view);
//        top_view.setVisibility(View.VISIBLE);
        top_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringYouExtracted = Long.toString(tinydb.getLong("TotalForegroundTime", 0));

                setClipboard(stringYouExtracted);
                String url = "https://goo.gl/forms/0wl3LGhqtNYC4oyA2";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(MainActivity.this, Uri.parse(url));

//                Intent openSurveyUrl= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(openSurveyUrl);

            }
        });

        reset_timer = (ImageButton) findViewById(R.id.reset_timer);
        reset_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinydb.putLong("TotalForegroundTime", 0L);
                resumeTime = System.currentTimeMillis();
            }
        });


        card_load_prob_1 = (CardView) findViewById(R.id.card_load_prob_1);
        card_load_prob_1.setVisibility(View.INVISIBLE);
        card_load_prob_1.setOnClickListener(new View.OnClickListener() {
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

        card_load_prob_2 = (CardView) findViewById(R.id.card_load_prob_2);
        card_load_prob_2.setVisibility(View.INVISIBLE);
        card_load_prob_2.setOnClickListener(new View.OnClickListener() {
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

        card_load_prob_3 = (CardView) findViewById(R.id.card_load_prob_3);
        card_load_prob_3.setVisibility(View.INVISIBLE);
        card_load_prob_3.setOnClickListener(new View.OnClickListener() {
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


        ViewAnimator
                .animate(card_load_prob_1)
                .dp().translationY(200, 0)
                .duration(500)
                .accelerate()
                .start();

        ViewAnimator
                .animate(card_load_prob_2)
                .dp().translationY(250, 0)
                .duration(500)
                .accelerate()
                .start();

        ViewAnimator
                .animate(card_load_prob_3)
                .dp().translationY(300, 0)
                .duration(500)
                .accelerate()
                .start();

        card_load_prob_1.setVisibility(View.VISIBLE);
        card_load_prob_2.setVisibility(View.VISIBLE);
        card_load_prob_3.setVisibility(View.VISIBLE);

//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                card_load_prob_1.startAnimation(slideUp);
//                card_load_prob_1.setVisibility(View.VISIBLE);
//            }
//        }, 50);
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                card_load_prob_2.startAnimation(slideUp);
//                card_load_prob_2.setVisibility(View.VISIBLE);
//            }
//        }, 150);
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                card_load_prob_3.startAnimation(slideUp);
//                card_load_prob_3.setVisibility(View.VISIBLE);
//            }
//        }, 200);

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

    // TODO fix this onPause on Stop nonsense
    @Override
    protected void onPause() {
        super.onPause();
        pauseTime = System.currentTimeMillis();
        totalForgroundTime = tinydb.getLong("TotalForegroundTime", 0) + (pauseTime - resumeTime);
        tinydb.putLong("TotalForegroundTime", totalForgroundTime);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //update foreground time
        resumeTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG + "onDestroy", "Destroyed");
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

//            tinydb.remove("TotalForegroundTime"); //TODO: remove this

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

    private void setClipboard(String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Text : ", text);
            clipboard.setPrimaryClip(clip);
        }
    }


}