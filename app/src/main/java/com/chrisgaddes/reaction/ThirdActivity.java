package com.chrisgaddes.reaction;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;


public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";
    private DrawArrowsView mDrawArrowsView;
    private ImageView IV_peek_probMain_view;
    private ImageView IV_peek_parta;
    private ImageView IV_peek_partb;

    private ImageView IV_peek_parta_arrows;
    private ImageView IV_peek_partb_arrows;

    private String description;
    private String strDialogNextButton;

    private TextView TV_time_display;

    private TextView tv_statement;

    private int problem_number;
    private String part_letter;

    private String str_toolbar_partCurrent_title;
    private String[] str_part_statement;
    private String[] str_problem_statement;
    private String str_parta_statement[];
    private String str_partb_statement[];

    private FloatingActionButton btn_check_done;
    private SquareImageView btn_peek_probMain;
    private SquareImageView btn_peek_parta;
    private SquareImageView btn_peek_partb;

    private SquareImageView btn_peek_parta_arrows;
    private SquareImageView btn_peek_partb_arrows;

    private TinyDB tinydb;
    private int eventaction;

    private Toolbar toolbar;
    private ImageView IV_problem_part;
    private String str_partCurrent_file_name;
    private String str_parta_file_name;
    private String str_partb_file_name;
    private String str_partc_file_name;

    private String str_parta_title;
    private String str_partb_title;
    private String str_partc_title;

    private String str_toolbar_problem_title;

    private String str_probCurrent_file_name;

    private long pauseTime;
    private long resumeTime;
    private long totalForgroundTime;

    private ChronometerView rc;
    private String time_string_for_dialog;

    private Boolean enable_peek_a;
    private Boolean enable_peek_b;
    private Snackbar snackbar;

    private Animation scaleIn;
    private Animation scaleOut;

    private Animation scaleInFast;
    private Animation scaleOutFast;

    private Animation fadeIn;
    private Animation fadeOut;

    private Bitmap bm_A;
    private Bitmap bm_B;

    private Handler mHandler = new Handler();

    private AsyncTaskLoadBitmapFromDataBase asyncLoadBitmapPartA;
    private AsyncTaskLoadBitmapFromDataBase asyncLoadBitmapPartB;

    private AsyncTaskSaveViewToBitmap asyncSaveToBitmapPartA;
    private AsyncTaskSaveViewToBitmap asyncSaveToBitmapPartB;
    private MyCache cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        setContentView(R.layout.activity_third);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

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

        asyncSaveToBitmapPartA = new AsyncTaskSaveViewToBitmap();
        asyncLoadBitmapPartA = new AsyncTaskLoadBitmapFromDataBase();

        asyncSaveToBitmapPartB = new AsyncTaskSaveViewToBitmap();
        asyncLoadBitmapPartB = new AsyncTaskLoadBitmapFromDataBase();

        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        fadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

        scaleIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_fab_in);
        scaleOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_fab_out);
        scaleInFast = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_in_fast);
        scaleOutFast = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_out_fast);

        btn_check_done = (FloatingActionButton) findViewById(R.id.btn_check_done);
//        btn_check_done.hide();

        // Sets database
        tinydb = new TinyDB(this);

        cache = new MyCache();
        cache.OpenOrCreateCache(this, "ArrowBitmaps");

        // Loads method to (re)startPart
        startPart();


        mDrawArrowsView = (DrawArrowsView) findViewById(R.id.idDrawArrowsView);

        mDrawArrowsView.setObserver(new TheObserver() {
                                        public void callback() {
//                                            Log.d("Clicked", "Woo Hoo!");
                                        }
                                    }
        );

//        mDrawArrowsView.setAlpha((float) 1.0);


        btn_check_done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean mrunCheckIfFinished = mDrawArrowsView.runCheckIfFinished();
                if (mrunCheckIfFinished) {
                    showDialogArrowsCorrect();
                } else {
                    Snackbar.make(findViewById(R.id.coordinatorLayout), "Not Finished Yet...", Snackbar.LENGTH_SHORT).setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            switch (event) {
                                case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                    btn_peek_probMain.setAlpha((float) 1.0);
                                    if (enable_peek_a) {
                                        btn_peek_parta.setAlpha((float) 1.0);
                                        btn_peek_parta_arrows.setAlpha((float) 1.0);
                                    }
                                    if (enable_peek_b) {
                                        btn_peek_partb.setAlpha((float) 1.0);
                                        btn_peek_partb_arrows.setAlpha((float) 1.0);
                                    }
                                    break;
                                case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                                    btn_peek_probMain.setAlpha((float) 1.0);
                                    if (enable_peek_a) {
                                        btn_peek_parta.setAlpha((float) 1.0);
                                        btn_peek_parta_arrows.setAlpha((float) 1.0);
                                    }
                                    if (enable_peek_b) {
                                        btn_peek_partb.setAlpha((float) 1.0);
                                        btn_peek_partb_arrows.setAlpha((float) 1.0);
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onShown(Snackbar snackbar) {
                            btn_peek_probMain.setAlpha((float) 0.0);
                            if (enable_peek_a) {
                                btn_peek_parta.setAlpha((float) 0.0);
                                btn_peek_parta_arrows.setAlpha((float) 0.0);
                            }
                            if (enable_peek_b) {
                                btn_peek_partb.setAlpha((float) 0.0);
                                btn_peek_partb_arrows.setAlpha((float) 0.0);
                            }
                        }
//                    }).setAction("Start Over", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            btn_peek_probMain.setAlpha((float) 1.0);
//                            if (enable_peek_a) {
//                                btn_peek_parta.setAlpha((float) 1.0);
//                            }
//                            if (enable_peek_b) {
//                                btn_peek_partb.setAlpha((float) 1.0);
//                            }
//                            mDrawArrowsView.resetAllValues();
//                        }
                    }).show();
                }
            }
        });

        btn_peek_probMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                eventaction = event.getAction();
                switch (eventaction) {
                    // TODO add if currently touched to

                    case MotionEvent.ACTION_DOWN:
                        getSupportActionBar().setTitle(str_toolbar_problem_title);
                        IV_peek_probMain_view.setAlpha((float) 1.0);
                        IV_problem_part.setAlpha((float) 0.0);
                        mDrawArrowsView.setAlpha((float) 0.0);
                        tv_statement.setText(str_problem_statement[0]);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            btn_peek_probMain.setElevation(dpToPx(8));
                        }

                        if (enable_peek_a) {
//                            YoYo.with(Techniques.SlideOutDown)
//                                    .duration(200)
//                                    .playOn(btn_peek_parta);
                            btn_peek_parta.startAnimation(scaleOut);
                            btn_peek_parta_arrows.startAnimation(scaleOut);
                        }

//                        btn_peek_parta.startAnimation(scaleOut);
//                        btn_peek_partb.startAnimation(scaleOut);

                        if (enable_peek_b) {
//                            YoYo.with(Techniques.SlideOutDown)
//                                    .duration(200)
//                                    .playOn(btn_peek_partb);
                            btn_peek_partb.startAnimation(scaleOut);
                            btn_peek_partb_arrows.startAnimation(scaleOut);
                        }

//                        YoYo.with(Techniques.SlideOutRight)
//                                .duration(200)
//                                .playOn(btn_check_done);

                        btn_check_done.hide();
                        break;

                    case MotionEvent.ACTION_UP:
                        getSupportActionBar().setTitle(str_toolbar_partCurrent_title);
                        IV_peek_probMain_view.setAlpha((float) 0.0);
                        IV_problem_part.setAlpha((float) 1.0);

                        mDrawArrowsView.setAlpha((float) 1.0);
                        tv_statement.setText(str_part_statement[0]);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            btn_peek_probMain.setElevation(dpToPx(4));
                        }
//                        btn_peek_probMain.setAlpha((float) 1.0);
                        if (enable_peek_a) {

//                            YoYo.with(Techniques.SlideInUp)
//                                    .duration(200)
//                                    .playOn(btn_peek_parta);
                            btn_peek_parta.startAnimation(scaleIn);
                            btn_peek_parta_arrows.startAnimation(scaleIn);
                        }
                        if (enable_peek_b) {
                            btn_peek_partb.startAnimation(scaleIn);
                            btn_peek_partb_arrows.startAnimation(scaleIn);
//                            YoYo.with(Techniques.SlideInUp)
//                                    .duration(200)
//                                    .playOn(btn_peek_partb);
                        }

//                        YoYo.with(Techniques.SlideInRight)
//                                .duration(200)
//                                .playOn(btn_check_done);
                        btn_check_done.show();
                        break;
                }

                return true;
            }
        });


        // clicked on part a button
        btn_peek_parta.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                eventaction = event.getAction();
                if (enable_peek_a) {
                    switch (eventaction) {
                        case MotionEvent.ACTION_DOWN:
                            getSupportActionBar().setTitle(str_parta_title);
                            IV_peek_parta.setAlpha((float) 1.0);
                            IV_peek_parta_arrows.setAlpha((float) 1.0);
                            IV_problem_part.setAlpha((float) 0.0);
                            mDrawArrowsView.setAlpha((float) 0.0);
                            tv_statement.setText(str_parta_statement[0]);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                btn_peek_parta.setElevation(dpToPx(8));
                                btn_peek_parta_arrows.setElevation(dpToPx(8));
                            }
                            btn_peek_probMain.startAnimation(scaleOut);
                            btn_peek_partb.startAnimation(scaleOut);
                            btn_peek_partb_arrows.startAnimation(scaleOut);
                            btn_check_done.hide();
                            break;

                        case MotionEvent.ACTION_UP:
                            getSupportActionBar().setTitle(str_toolbar_partCurrent_title);
                            IV_peek_parta.setAlpha((float) 0.0);
                            IV_peek_parta_arrows.setAlpha((float) 0.0);
                            IV_problem_part.setAlpha((float) 1.0);
                            mDrawArrowsView.setAlpha((float) 1.0);
                            tv_statement.setText(str_part_statement[0]);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                btn_peek_parta.setElevation(dpToPx(4));
                                btn_peek_parta_arrows.setElevation(dpToPx(4));
                            }
//                            btn_peek_probMain.setAlpha((float) 1.0);

                            btn_peek_probMain.startAnimation(scaleIn);
                            if (enable_peek_b) {
                                btn_peek_partb.startAnimation(scaleIn);
                                btn_peek_partb_arrows.startAnimation(scaleIn);
                            }
                            btn_check_done.show();
                            break;
                    }
                }
                return true;
            }
        });

        // clicked on part b button
        btn_peek_partb.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                eventaction = event.getAction();
                if (enable_peek_b) {
                    switch (eventaction) {
                        case MotionEvent.ACTION_DOWN:

                            getSupportActionBar().setTitle(str_partb_title);
                            IV_peek_partb.setAlpha((float) 1.0);
                            IV_peek_partb_arrows.setAlpha((float) 1.0);
                            IV_problem_part.setAlpha((float) 0.0);
                            mDrawArrowsView.setAlpha((float) 0.0);
                            tv_statement.setText(str_partb_statement[0]);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                btn_peek_partb.setElevation(dpToPx(8));
                                btn_peek_partb_arrows.setElevation(dpToPx(8));
                            }
                            btn_peek_probMain.startAnimation(scaleOut);
                            btn_peek_parta.startAnimation(scaleOut);
                            btn_peek_parta_arrows.startAnimation(scaleOut);
                            btn_check_done.hide();

                            break;

                        case MotionEvent.ACTION_UP:
                            getSupportActionBar().setTitle(str_toolbar_partCurrent_title);
                            IV_peek_partb.setAlpha((float) 0.0);
                            IV_peek_partb_arrows.setAlpha((float) 0.0);
                            IV_problem_part.setAlpha((float) 1.0);
                            mDrawArrowsView.setAlpha((float) 1.0);
                            tv_statement.setText(str_part_statement[0]);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                btn_peek_partb.setElevation(dpToPx(4));
                                btn_peek_partb_arrows.setElevation(dpToPx(4));
                            }
                            btn_peek_probMain.startAnimation(scaleIn);
                            btn_peek_parta.startAnimation(scaleIn);
                            btn_peek_parta_arrows.startAnimation(scaleIn);
                            btn_check_done.show();

                            break;
                    }
                }
                return true;
            }
        });

    }

    private void startPart() {
        // Loads problem information
        problem_number = tinydb.getInt("problem_number");
        part_letter = tinydb.getString("part_letter");

        // resets booleans to false (hidden)
        enable_peek_a = false;
        enable_peek_b = false;

        // shows peek image buttons once previous part is finished
        if (part_letter.toUpperCase().equals("A")) {
            enable_peek_a = false;
            enable_peek_b = false;
        }

        if (part_letter.toUpperCase().equals("B")) {
            enable_peek_a = true;
            enable_peek_b = false;
        }

        if (part_letter.toUpperCase().equals("C")) {
            enable_peek_a = true;
            enable_peek_b = true;
        }

        // load problem statement and part statement
        str_problem_statement = getResources().getStringArray(getResId("mainProblemStatement_" + "prob" + problem_number + "_part" + part_letter, R.array.class));

        str_part_statement = getResources().getStringArray(getResId("problemStatement_" + "prob" + problem_number + "_part" + part_letter, R.array.class));
        str_parta_statement = getResources().getStringArray(getResId("problemStatement_" + "prob" + problem_number + "_part" + "A", R.array.class));
        str_partb_statement = getResources().getStringArray(getResId("problemStatement_" + "prob" + problem_number + "_part" + "B", R.array.class));


        // converts to lower case
        part_letter = part_letter.toLowerCase();

        // Generates strings from problem information
        str_toolbar_partCurrent_title = "#" + problem_number + " - Part " + part_letter.toUpperCase();
        str_partCurrent_file_name = "prob" + problem_number + "_part" + part_letter;
        str_parta_file_name = "prob" + problem_number + "_part" + "a";
        str_partb_file_name = "prob" + problem_number + "_part" + "b";
        str_partc_file_name = "prob" + problem_number + "_part" + "c";

        str_probCurrent_file_name = "prob" + problem_number;
        str_toolbar_problem_title = "Problem #" + problem_number;
        str_parta_title = "#" + problem_number + " - Part " + "A";
        str_partb_title = "#" + problem_number + " - Part " + "B";
        str_partc_title = "#" + problem_number + " - Part " + "C";

        // Sets text for problem statement
        tv_statement = (TextView) this.findViewById(R.id.tv_statement);
        tv_statement.setText(str_part_statement[0]);

        // Sets image for part
        IV_problem_part = (ImageView) findViewById(R.id.problem_part);
        Glide.with(this)
                .load(getResources().getIdentifier(str_partCurrent_file_name, "drawable", getPackageName()))
                .into(IV_problem_part);

        // Sets image for Main problem and sets "invisible"
        IV_peek_probMain_view = (ImageView) findViewById(R.id.peek_probCurrent_view);
        IV_peek_probMain_view.setAlpha((float) 0.0);
        btn_peek_probMain = (SquareImageView) findViewById(R.id.btn_peek_prob);
        btn_peek_probMain.setAlpha((float) 0.0);
        Glide.with(this)
                .load(getResources().getIdentifier(str_probCurrent_file_name, "drawable", getPackageName()))
                .load(getResources().getIdentifier(str_probCurrent_file_name, "drawable", getPackageName()))
                .into(IV_peek_probMain_view);
        Glide.with(this)
                .load(getResources().getIdentifier(str_probCurrent_file_name, "drawable", getPackageName()))
                .into(btn_peek_probMain);

        // Sets image for part a
        IV_peek_parta = (ImageView) findViewById(R.id.peek_parta);
        IV_peek_parta_arrows = (ImageView) findViewById(R.id.peek_parta_arrows);
        IV_peek_parta.setAlpha((float) 0.0);
        IV_peek_parta_arrows.setAlpha((float) 0.0);
        btn_peek_parta = (SquareImageView) findViewById(R.id.btn_peek_parta);
        btn_peek_parta_arrows = (SquareImageView) findViewById(R.id.btn_peek_parta_arrows);
        btn_peek_parta.setAlpha((float) 0.0);
        btn_peek_parta_arrows.setAlpha((float) 0.0);
        Glide.with(this)
                .load(getResources().getIdentifier(str_parta_file_name, "drawable", getPackageName()))
                .into(IV_peek_parta);
        Glide.with(this)
                .load(getResources().getIdentifier(str_parta_file_name, "drawable", getPackageName()))
                .into(btn_peek_parta);

        // Sets image for part b
        IV_peek_partb = (ImageView) findViewById(R.id.peek_partb);
        IV_peek_partb_arrows = (ImageView) findViewById(R.id.peek_partb_arrows);
        IV_peek_partb.setAlpha((float) 0.0);
        IV_peek_partb_arrows.setAlpha((float) 0.0);
        btn_peek_partb = (SquareImageView) findViewById(R.id.btn_peek_partb);
        btn_peek_partb_arrows = (SquareImageView) findViewById(R.id.btn_peek_partb_arrows);
        btn_peek_partb.setAlpha((float) 0.0);
        btn_peek_partb_arrows.setAlpha((float) 0.0);
        Glide.with(this)
                .load(getResources().getIdentifier(str_partb_file_name, "drawable", getPackageName()))
                .into(IV_peek_partb);
        Glide.with(this)
                .load(getResources().getIdentifier(str_partb_file_name, "drawable", getPackageName()))
                .into(btn_peek_partb);

        if (part_letter.toUpperCase().equals("B")) {
            asyncLoadBitmapPartA.execute();
        }

        if (part_letter.toUpperCase().equals("C")) {
            asyncLoadBitmapPartB.execute();
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btn_peek_probMain.startAnimation(scaleIn);
                btn_peek_probMain.setAlpha((float) 1.0);

                // shows appropriate peek buttons
                if (enable_peek_a) {
                    btn_peek_parta.startAnimation(scaleIn);
                    btn_peek_parta_arrows.startAnimation(scaleIn);
                    btn_peek_parta.setAlpha((float) 1.0);
                    btn_peek_parta_arrows.setAlpha((float) 1.0);
                } else {
                    btn_peek_parta.setAlpha((float) 0.0);
                    btn_peek_parta_arrows.setAlpha((float) 0.0);
                }

                if (enable_peek_b) {
                    btn_peek_parta.startAnimation(scaleIn);
                    btn_peek_parta_arrows.startAnimation(scaleIn);
                    btn_peek_partb.setAlpha((float) 1.0);
                    btn_peek_partb_arrows.setAlpha((float) 1.0);
                } else {
                    btn_peek_partb.setAlpha((float) 0.0);
                    btn_peek_partb_arrows.setAlpha((float) 0.0);
                }
            }
        }, 750);


        // Sets toolbar title
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(str_toolbar_partCurrent_title);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG + "onDestroy", "Destroyed");
//        RefWatcher refWatcher = ExampleApplication.getRefWatcher(ThirdActivity.this);

//        Glide.clear(IV_peek_probMain_view);
//        GLide.clear(IV_peek_parta
//        IV_peek_partb
//        IV_peek_parta_arrows
//        IV_peek_partb_arrows
//        IV_peek_probMain_view

        unbindDrawables(findViewById(R.id.relativeLayout_Main));
//        System.gc();


//        if (refWatcher != null) {
//            refWatcher.watch(this);
////            refWatcher.watch(IV_peek, "IV_peek");
//            refWatcher.watch(IV_peek_parta);
//            refWatcher.watch(IV_peek_partb);
//            refWatcher.watch(IV_peek_parta_arrows);
//            refWatcher.watch(IV_peek_partb_arrows);
//            refWatcher.watch(IV_peek_probMain_view);
//            refWatcher.watch(bm_A);
//            refWatcher.watch(bm_B);
//        }


//        if (bm_A != null && ! bm_A.isRecycled()) {
//            bm_A.recycle();
//        }
//
//        if (bm_B != null && ! bm_B.isRecycled()) {
//            bm_B.recycle();
//        }
//
//        IV_peek_parta.setImageDrawable(null);
//        IV_peek_partb.setImageDrawable(null);
//        IV_peek_parta_arrows.setImageDrawable(null);
//        IV_peek_partb_arrows.setImageDrawable(null);
//        IV_peek_probMain_view.setImageDrawable(null);

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
        explode.excludeTarget(R.id.toolbar, true);
//exclude status bar
        explode.excludeTarget(android.R.id.statusBarBackground, true);
//exclude navigation bar
        explode.excludeTarget(android.R.id.navigationBarBackground, true);
//
////        explode.excludeTarget(MainActivity), true);

//        getWindow();
//        getWindow().setEnterTransition(explode);
//        getWindow().setReturnTransition(explode);
//        getWindow().setAllowEnterTransitionOverlap(false);
//        getWindow().setAllowReturnTransitionOverlap(false);
    }

    private void showDialogArrowsCorrect() {
        // stop counter
        rc.stop();
        long timer_time = rc.getCurrentTime();
        long minutes = timer_time / 60;
        long seconds = timer_time - (60 * minutes);
        if (minutes > 0) {
            time_string_for_dialog = minutes + " minutes, " + seconds + " seconds!";
        } else if (minutes <= 0 && seconds == 1) {
            time_string_for_dialog = seconds + " second... Inconceivable!";
        } else if (minutes <= 0 && seconds != 1) {
            time_string_for_dialog = seconds + " seconds!";
        }

        // animate buttons out
        btn_peek_probMain.startAnimation(scaleOut);
        btn_peek_parta.startAnimation(scaleOut);
        btn_peek_parta_arrows.startAnimation(scaleOut);
        btn_peek_partb.startAnimation(scaleOut);
        btn_peek_partb_arrows.startAnimation(scaleOut);
        btn_check_done.hide();


        if (part_letter.toUpperCase().equals("C")) {
            description = "All forces placed correctly! You finished in " + time_string_for_dialog + "Problem " + problem_number + " is now complete.";
            strDialogNextButton = "Main Menu";

            new MaterialStyledDialog(this)
                    .setTitle("Correct! Problem " + problem_number + " finished!")
                    .setDescription(description)
                    .setIcon(R.drawable.ic_check)
                    .setStyle(Style.HEADER_WITH_ICON)
                    .setScrollable(true)
                    .setCancelable(false)

                    .setPositive("Next Problem", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            mDrawArrowsView.setAlpha((float) 0.0);
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ThirdActivity.this);

                            tinydb.putString("part_letter", "A");
                            // TODO put logic in here so it knows how many problems there are

                            tinydb.putBoolean("prob" + problem_number + "_completed", true);

                            // put code here that redirects them to survey if they finish all 3 problems

                            if (problem_number < 3) {
                                tinydb.putBoolean("prob" + problem_number + "_completed", true);
                                problem_number++;
                                tinydb.putInt("problem_number", problem_number);
                            }


                            Intent mainIntent = new Intent(ThirdActivity.this, SecondActivity.class);
                            startActivity(mainIntent, options.toBundle());
                        }

                    })
                    .setNegative("Main Menu", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            Intent intent = getIntent();
                            mDrawArrowsView.setAlpha((float) 0.0);
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ThirdActivity.this);

                            // TODO put logic in here so it knows how many problems there are instead of hard coding in 3
                            if (problem_number < 3) {
                                tinydb.putBoolean("prob" + problem_number + "_completed", true);
                                Intent mainIntent = new Intent(ThirdActivity.this, MainActivity.class);
                                startActivity(mainIntent, options.toBundle());
                                finish();
                            }
                        }
                    })
                    .show();

        } else {
            description = "All forces placed correctly! You finished in " + time_string_for_dialog;
            strDialogNextButton = "Next Part";

//        String strDialogNextButton = "hi";

            new MaterialStyledDialog(this)
                    .setTitle("Correct!")
                    .setDescription(description)
                    .setIcon(R.drawable.ic_check)
                    .setStyle(Style.HEADER_WITH_ICON)
                    .setScrollable(true)
                    .setCancelable(false)

                    .setPositive(strDialogNextButton, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            Intent intent = getIntent();
                            mDrawArrowsView.setAlpha((float) 0.0);
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ThirdActivity.this);

                            switch (part_letter.toUpperCase()) {
                                case "A":
                                    asyncSaveToBitmapPartA.execute();
                                    break;
                                case "B":
                                    asyncSaveToBitmapPartB.execute();
                                    break;
                                case "C":
                                    tinydb.putString("part_letter", "A");
                                    // TODO put logic in here so it knows how many problems there are
                                    tinydb.putInt("problem_number", problem_number++);
                                    Intent mainIntent = new Intent(ThirdActivity.this, MainActivity.class);
                                    startActivity(mainIntent, options.toBundle());
                                    finish();
                                    break;
                            }
                        }
                    })
                    .show();
        }

    }

    private void helpDialog() {
        new MaterialStyledDialog(this)
                .setTitle("Help")
                .setDescription(R.string.str_help_dialog_description)
                .setIcon(R.drawable.ic_question_mark)
                .setStyle(Style.HEADER_WITH_ICON)
                .setScrollable(true)
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
//        Long tmpTime = tinydb.getLong("TotalForegroundTime", 0);
//        if (tmpTime == 0) {
//            tinydb.putLong("TotalForegroundTime", System.currentTimeMillis());
//        }
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
//        rc.setTextColor(Color.WHITE);
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
                mDrawArrowsView.setAllToUnused();
                mDrawArrowsView.resetAllValues();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private Bitmap getBitmapFromView(View view) {
        // defines a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        // binds a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);

        // saves arrows from view to bitmap
        view.draw(canvas);

        // returns the bitmap
        return returnedBitmap;
    }

    // converts dp to pixels
    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    private class AsyncTaskSaveViewToBitmap extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            Bitmap bm = getBitmapFromView(mDrawArrowsView);

            String db_title_A = "prob" + problem_number + "_part" + "A" + "_arrows";
            String db_title_B = "prob" + problem_number + "_part" + "B" + "_arrows";

            if (part_letter.toUpperCase().equals("A")) {
                cache.SaveBitmap(db_title_A, bm);
                tinydb.putString("part_letter", "B");
                if (bm != null) {
//                    bm.recycle();
                    bm = null;
                }
            }

            if (part_letter.toUpperCase().equals("B")) {
                cache.SaveBitmap(db_title_B, bm);
                tinydb.putString("part_letter", "C");
                if (bm != null) {
//                    bm.recycle();
                    bm = null;
                }

            }

            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
//            finalResult.setText(result);
            tinydb.putBoolean("finished_saving_bitmaps", true);
//            myBitmap.recycle();
//            myBitmap = null;

            mDrawArrowsView.resetForNextPart();
//            mDrawArrowsView.resetAllValues();
            mDrawArrowsView.loadArrowCheckLocations();
            mDrawArrowsView.setAlpha((float) 1.0);
            btn_check_done.show();
            startPart();

//            recreate();
        }

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        @Override
        protected void onProgressUpdate(String... text) {
//            finalResult.setText(text[0]);
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }

    private class AsyncTaskLoadBitmapFromDataBase extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {

            String db_title_A = "prob" + problem_number + "_part" + "A" + "_arrows";
            String db_title_B = "prob" + problem_number + "_part" + "B" + "_arrows";

            if (part_letter.toUpperCase().equals("B")) {
                bm_A = cache.OpenBitmap(db_title_A);
            }

            if (part_letter.toUpperCase().equals("C")) {
                bm_A = cache.OpenBitmap(db_title_A);
                bm_B = cache.OpenBitmap(db_title_B);
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            if (enable_peek_a) {
                IV_peek_parta_arrows.setImageBitmap(bm_A);
                btn_peek_parta_arrows.setImageBitmap(bm_A);
                if (bm_A != null) {
//                    bm_A.recycle();
                    bm_A = null;
                }
            }

            if (enable_peek_b) {
                IV_peek_parta_arrows.setImageBitmap(bm_A);
                btn_peek_parta_arrows.setImageBitmap(bm_A);
                if (bm_A != null) {
//                    bm_A.recycle();
                    bm_A = null;
                }

                IV_peek_partb_arrows.setImageBitmap(bm_B);
                btn_peek_partb_arrows.setImageBitmap(bm_B);
                if (bm_B != null) {
//                    bm_B.recycle();
                    bm_B = null;
                }
            }
        }

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        @Override
        protected void onProgressUpdate(String... text) {
//            finalResult.setText(text[0]);
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
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


    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    private class MyCache {

        private String DiretoryName;

        private void OpenOrCreateCache(Context _context, String _directoryName) {
            File file = new File(_context.getCacheDir().getAbsolutePath() + "/" + _directoryName);
            if (!file.exists()) {
                file.mkdir();
            }
            DiretoryName = file.getAbsolutePath();
        }

        private void SaveBitmap(String fileName, Bitmap bmp) {
            try {
                File file = new File(DiretoryName + "/" + fileName);
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream Filestream = new FileOutputStream(DiretoryName + "/" + fileName);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Filestream.write(byteArray);
                Filestream.close();
//                bmp.recycle();
                bmp = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private Bitmap OpenBitmap(String name) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                File file = new File(DiretoryName + "/" + name);
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(DiretoryName + "/" + name, options);
                    return bitmap;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

