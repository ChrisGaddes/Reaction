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
import android.support.design.widget.Snackbar;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.florent37.viewanimator.ViewAnimator;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

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

    private CardView card_load_prob1;
    private CardView card_load_prob2;
    private CardView card_load_prob3;
    private CardView card_survey;
    private CardView card_reset_everything;
    private CardView card_reset_everything_always_shown;
    private CardView card_choose_problem_below;

    private Button btn_prob1_start;
    private Button btn_prob2_start;
    private Button btn_prob3_start;

    private Button btn_prob1_startover;
    private Button btn_prob2_startover;
    private Button btn_prob3_startover;

    private TextView subtitle_prob1;
    private TextView subtitle_prob2;
    private TextView subtitle_prob3;

    private ImageView image_prob2_lock;
    private ImageView image_prob3_lock;

    private Animation slideUp;
    private Animation slideDown;

    private TextView TV_time_display;

    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        LeakCanary.install(getApplication());


        // loads tinydb database
        tinydb = new TinyDB(this);


        // initializes part_letter on first run
//        if (tinydb.getString("part_letter") == null) {
//            tinydb.putString("part_letter", "A");
//        }

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


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            View decor = getWindow().getDecorView();
//            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryLight));
//        }

//        // Sets toolbar title
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");


//        reset_everything = (ImageButton) findViewById(R.id.reset_everything);
//        reset_everything.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resetEverything();
//            }
//        });

        card_choose_problem_below = (CardView) findViewById(R.id.card_choose_problem_below);
        card_choose_problem_below.setVisibility(View.VISIBLE);
//        top_view.setVisibility(View.VISIBLE);
        card_choose_problem_below.setOnClickListener(new View.OnClickListener() {
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


        card_survey = (CardView) findViewById(R.id.card_survey);
        card_survey.setVisibility(View.GONE);
//        top_view.setVisibility(View.VISIBLE);
        card_survey.setOnClickListener(new View.OnClickListener() {
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

        card_reset_everything_always_shown = (CardView) findViewById(R.id.card_reset_everything_always_shown);
//        card_reset_everything_always_shown.setVisibility(View.GONE);
        card_reset_everything_always_shown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialStyledDialog(MainActivity.this)
                        .setTitle("Reset App for new user?")
                        .setDescription("Are you sure you want to reset this app? ")
                        .setIcon(R.drawable.ic_replay_light)
                        .setStyle(Style.HEADER_WITH_ICON)
                        .setScrollable(true)
                        .setCancelable(true)
                        .setPositive("yes", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                resetEverything();
                            }
                        })

                        .setNegative("no", new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {

                                    }
                                }
                        ).show();
            }
        });


        card_reset_everything = (CardView) findViewById(R.id.card_reset_everything);
        card_reset_everything.setVisibility(View.GONE);
        card_reset_everything.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialStyledDialog(MainActivity.this)
                        .setTitle("Reset App?")
                        .setDescription("Are you sure you want to reset this app?")
                        .setIcon(R.drawable.ic_replay_light)
                        .setStyle(Style.HEADER_WITH_ICON)
                        .setScrollable(true)
                        .setCancelable(true)
                        .setPositive("yes", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                resetEverything();
                            }
                        })

                        .setNegative("no", new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {

                                    }
                                }
                        ).show();
            }
        });

        subtitle_prob1 = (TextView) findViewById(R.id.subtitle_prob1);
        card_load_prob1 = (CardView) findViewById(R.id.card_load_prob1);
        card_load_prob1.setVisibility(View.INVISIBLE);
        card_load_prob1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLoadProb1();
            }
        });

        btn_prob1_startover = (Button) findViewById(R.id.btn_prob1_startover);
        btn_prob1_startover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStartover(1);
            }
        });

        btn_prob1_start = (Button) findViewById(R.id.btn_prob1_start);
        btn_prob1_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLoadProb1();
            }
        });


        image_prob2_lock = (ImageView) findViewById(R.id.image_prob2_lock);
        subtitle_prob2 = (TextView) findViewById(R.id.subtitle_prob2);
        card_load_prob2 = (CardView) findViewById(R.id.card_load_prob2);
        card_load_prob2.setVisibility(View.INVISIBLE);
        card_load_prob2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLoadProb2();
            }
        });

        btn_prob2_startover = (Button) findViewById(R.id.btn_prob2_startover);
        btn_prob2_startover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tinydb.getBoolean("prob1_completed")) {
                    showDialogStartover(2);
                } else {
                    problem_number = 3;
                    int previous_prob_num = problem_number - 1;
                    Snackbar.make(findViewById(R.id.main_activity_Relative_Layout), "Problem " + problem_number + " is locked. Complete problem " + previous_prob_num + " first.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        btn_prob2_start = (Button) findViewById(R.id.btn_prob2_start);
        btn_prob2_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLoadProb2();
            }
        });


        image_prob3_lock = (ImageView) findViewById(R.id.image_prob3_lock);
        subtitle_prob3 = (TextView) findViewById(R.id.subtitle_prob3);
        card_load_prob3 = (CardView) findViewById(R.id.card_load_prob3);
        card_load_prob3.setVisibility(View.INVISIBLE);
        card_load_prob3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLoadProb3();
            }
        });

        btn_prob3_startover = (Button) findViewById(R.id.btn_prob3_startover);
        btn_prob3_startover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tinydb.getBoolean("prob2_completed")) {
                    showDialogStartover(3);
                } else {
                    int previous_prob_num = problem_number - 1;
                    Snackbar.make(findViewById(R.id.main_activity_Relative_Layout), "Problem " + problem_number + " is locked. Complete problem " + previous_prob_num + " first.", Snackbar.LENGTH_LONG).show();
                    }
            }
        });

        btn_prob3_start = (Button) findViewById(R.id.btn_prob3_start);
        btn_prob3_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLoadProb3();
            }
        });

        slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);


        ViewAnimator
                .animate(card_load_prob1)
                .dp().translationY(200, 0)
                .duration(500)
                .accelerate()
                .start();

        ViewAnimator
                .animate(card_load_prob2)
                .dp().translationY(250, 0)
                .duration(500)
                .accelerate()
                .start();

        ViewAnimator
                .animate(card_load_prob3)
                .dp().translationY(300, 0)
                .duration(500)
                .accelerate()
                .start();

        card_load_prob1.setVisibility(View.VISIBLE);
        card_load_prob2.setVisibility(View.VISIBLE);
        card_load_prob3.setVisibility(View.VISIBLE);

    }

    private void clickLoadProb3() {
        problem_number = 3;
        //checks if problem is unlocked yet
        if (tinydb.getBoolean("prob2_completed")) {
            tinydb.putInt("problem_number", problem_number);

            String str = tinydb.getString("prob" + problem_number + "_part_letter");
            if (str == null || str.equals("")) {
                part_letter = "A";
                tinydb.putString("prob" + problem_number + "_part_letter", part_letter);
                tinydb.putString("part_letter", part_letter);
                loadSecondActivity();
            } else if (tinydb.getString("prob" + problem_number + "_part_letter").equals("Done")) {
                showDialogProblemAlreadyFinished(problem_number);
            } else {
                part_letter = tinydb.getString("prob" + problem_number + "_part_letter");
                tinydb.putString("part_letter", part_letter);
                loadThirdActivity();
            }
        } else {
            int previous_prob_num = problem_number - 1;
            Snackbar.make(findViewById(R.id.main_activity_Relative_Layout), "Problem " + problem_number + " is locked. Complete problem " + previous_prob_num + " first.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void clickLoadProb2() {
        problem_number = 2;
        //checks if problem is unlocked yet
        if (tinydb.getBoolean("prob1_completed")) {
            tinydb.putInt("problem_number", problem_number);
            String str = tinydb.getString("prob" + problem_number + "_part_letter");
            if (str == null || str.equals("")) {
                part_letter = "A";
                tinydb.putString("prob" + problem_number + "_part_letter", part_letter);
                tinydb.putString("part_letter", part_letter);
                loadSecondActivity();
            } else if (tinydb.getString("prob" + problem_number + "_part_letter").equals("Done")) {
                showDialogProblemAlreadyFinished(problem_number);
            } else {
                part_letter = tinydb.getString("prob" + problem_number + "_part_letter");
                tinydb.putString("part_letter", part_letter);
                loadThirdActivity();
            }
        } else {
            problem_number = 2;
            int previous_prob_num = problem_number - 1;
            Snackbar.make(findViewById(R.id.main_activity_Relative_Layout), "Problem " + problem_number + " is locked. Complete problem " + previous_prob_num + " first.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void clickLoadProb1() {
        problem_number = 1;
        // no need to check if this problem is unlocked. It is always unlocked
        tinydb.putInt("problem_number", problem_number);
        String str = tinydb.getString("prob" + problem_number + "_part_letter");
        if (str == null || str.equals("")) {
            part_letter = "A";
            tinydb.putString("prob" + problem_number + "_part_letter", part_letter);
            tinydb.putString("part_letter", part_letter);
            loadSecondActivity();
        } else if (tinydb.getString("prob" + problem_number + "_part_letter").equals("Done")) {
            showDialogProblemAlreadyFinished(problem_number);
        } else {
            part_letter = tinydb.getString("prob" + problem_number + "_part_letter");
            tinydb.putString("part_letter", part_letter);
            loadThirdActivity();
        }
    }

    private void loadSecondActivity() {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        startActivity(intent, options.toBundle());
    }

    private void loadThirdActivity() {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
        Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
        startActivity(intent, options.toBundle());
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
    protected void onResume() {
        super.onResume();

        //update foreground time
        resumeTime = System.currentTimeMillis();

        // sets various values for the cards such as subtitles and visibility of lock images
        setCardValues();
    }

    private void setCardValues() {
        // TODO remove hardcoded problem number here


        String str = tinydb.getString("prob1_part_letter");
        if (tinydb.getString("prob1_part_letter").equals("Done")) {
            subtitle_prob1.setText("Finished");
            btn_prob1_start.setVisibility(View.GONE);
            btn_prob1_startover.setVisibility(View.VISIBLE);
        } else if (str != null && !str.equals("")) {
            String text_to_set = "Working on Part " + tinydb.getString("prob1_part_letter");
            subtitle_prob1.setText(text_to_set);
            btn_prob1_start.setText("Continue");
        } else if (tinydb.getString("prob1_part_letter").equals("A") || str.equals("")) {
            subtitle_prob1.setText("Click to start problem");
            btn_prob1_startover.setVisibility(View.GONE);
            btn_prob1_start.setText("Start");
        }

        str = tinydb.getString("prob2_part_letter");
        if (tinydb.getString("prob2_part_letter").equals("Done")) {
            subtitle_prob2.setText("Finished");
            btn_prob2_start.setVisibility(View.GONE);
            btn_prob2_startover.setVisibility(View.VISIBLE);
            image_prob2_lock.setVisibility(ImageView.GONE);
        } else if (str != null && !str.equals("")) {
            String text_to_set = "Working on Part " + tinydb.getString("prob2_part_letter");
            subtitle_prob2.setText(text_to_set);
            btn_prob2_start.setText("Continue");
            image_prob2_lock.setVisibility(ImageView.GONE);
            btn_prob2_start.setVisibility(View.VISIBLE);
            btn_prob2_startover.setVisibility(View.VISIBLE);
        } else if (!tinydb.getBoolean("prob1_completed")) {
            subtitle_prob2.setText("Locked");
            image_prob2_lock.setVisibility(ImageView.VISIBLE);
            btn_prob2_start.setVisibility(View.GONE);
            btn_prob2_startover.setVisibility(View.GONE);
        } else if (tinydb.getString("prob2_part_letter").equals("A") || str.equals("")) {
            subtitle_prob2.setText("Click to start problem");
            btn_prob2_start.setText("Start");
            image_prob2_lock.setVisibility(ImageView.GONE);
            btn_prob2_startover.setVisibility(View.GONE);
            btn_prob2_start.setVisibility(View.VISIBLE);
        }


        str = tinydb.getString("prob3_part_letter");
        if (tinydb.getString("prob3_part_letter").equals("Done")) {
            subtitle_prob3.setText("Finished");
            btn_prob3_start.setVisibility(View.GONE);
            btn_prob3_startover.setVisibility(View.VISIBLE);
            image_prob3_lock.setVisibility(ImageView.GONE);
        } else if (str != null && !str.equals("")) {
            String text_to_set = "Working on Part " + tinydb.getString("prob3_part_letter");
            subtitle_prob3.setText(text_to_set);
            btn_prob3_start.setText("Continue");
            image_prob3_lock.setVisibility(ImageView.GONE);
            btn_prob3_start.setVisibility(View.VISIBLE);
            btn_prob3_startover.setVisibility(View.VISIBLE);
        } else if (!tinydb.getBoolean("prob2_completed")) {
            subtitle_prob3.setText("Locked");
            image_prob3_lock.setVisibility(ImageView.VISIBLE);
            btn_prob3_start.setVisibility(View.GONE);
            btn_prob3_startover.setVisibility(View.GONE);
        } else if (tinydb.getString("prob3_part_letter").equals("A") || str.equals("")) {
            subtitle_prob3.setText("Click to start problem");
            btn_prob3_start.setText("Start");
            image_prob3_lock.setVisibility(ImageView.GONE);
            btn_prob3_startover.setVisibility(View.GONE);
            btn_prob3_start.setVisibility(View.VISIBLE);
        }


        if (tinydb.getBoolean("survey_allowed")) {
            // sets survey button to visible
            card_survey.setVisibility(View.VISIBLE);
            card_reset_everything.setVisibility(View.VISIBLE);
//            card_choose_problem_below.setVisibility(View.INVISIBLE);
        } else {
            card_survey.setVisibility(View.GONE);
            card_reset_everything.setVisibility(View.GONE);
        }


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

//    private void fadeInAndShowImage(final ImageView img) {
//        Animation fadeIn = new AlphaAnimation(0, 1);
//        fadeIn.setInterpolator(new AccelerateInterpolator());
//        fadeIn.setDuration(1000);
//
//        fadeIn.setAnimationListener(new Animation.AnimationListener() {
//            public void onAnimationEnd(Animation animation) {
//            }
//
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            public void onAnimationStart(Animation animation) {
//                img.setVisibility(View.VISIBLE);
//            }
//        });
//
//        img.startAnimation(fadeIn);
//    }

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

    private void resetEverything() {
        Snackbar.make(findViewById(R.id.main_activity_Relative_Layout), "App was reset successfully", Snackbar.LENGTH_LONG).show();
        tinydb.clear();
        resumeTime = System.currentTimeMillis();
        tinydb = new TinyDB(this);
        setCardValues();
    }

    private void showDialogProblemAlreadyFinished(final int mproblem_number) {
        new MaterialStyledDialog(MainActivity.this)
                .setTitle("Problem already finished")
                .setDescription("Would you like to rework this problem?")
                .setIcon(R.drawable.ic_check)
                .setStyle(Style.HEADER_WITH_ICON)
                .setScrollable(true)
                .setCancelable(true)
                .setPositive("yes", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        part_letter = "A";
                        tinydb.putString("prob" + mproblem_number + "_part_letter", part_letter);
//                        tinydb.putString("part_letter_prob" + mproblem_number, part_letter);
                        loadSecondActivity();
                    }
                })

                .setNegative("no", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                // returns to main menu
                            }
                        }
                ).show();
    }

    private void showDialogStartover(final int mproblem_number) {
        new MaterialStyledDialog(MainActivity.this)
                .setTitle("Startover")
                .setDescription("Would you like to restart this problem?")
                .setIcon(R.drawable.ic_replay_light)
                .setStyle(Style.HEADER_WITH_ICON)
                .setScrollable(true)
                .setCancelable(true)
                .setPositive("yes", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        part_letter = "A";
                        tinydb.putString("part_letter", part_letter);
                        tinydb.putString("prob" + mproblem_number + "_part_letter", part_letter);
//                        tinydb.putString("part_letter_prob" + mproblem_number, part_letter);
                        loadSecondActivity();
                    }
                })

                .setNegative("no", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                // returns to main menu
                            }
                        }
                ).show();
    }

}

