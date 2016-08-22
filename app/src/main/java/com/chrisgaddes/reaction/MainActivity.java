package com.chrisgaddes.reaction;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private ListView listView;
    public TinyDB tinydb;
    private TextView str_problem_number;
    private int problem_number;
    private String part_letter;
    private View decor;
    private int actionBarId;
    private View actionBarView;
    private int mtoolbar;
    private Toolbar toolbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        setContentView(R.layout.activity_main);

//        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);
//
//        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        // specify an adapter (see also next example)
//        mAdapter = new MyAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);


        decor = getWindow().getDecorView();

//        mtoolbar = R.id.toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        actionBarView = getActionBarView();

//        mtoolbar = getActionBar(getWindow().getDecorView());

        tinydb = new TinyDB(this);


        findViewById(R.id.btn_load_prob_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                str_problem_number = (TextView)findViewById(R.id.problem_number);
//                str_problem_number.setText("1");
                problem_number = 1;
                part_letter = "A";
                tinydb.putInt("problem_number", problem_number);
                tinydb.putString("part_letter", part_letter);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent, options.toBundle());
            }
        });

        findViewById(R.id.btn_load_prob_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                str_problem_number = (TextView)findViewById(R.id.problem_number);
//                str_problem_number.setText("2");
                problem_number = 2;
                part_letter = "A";
                tinydb.putInt("problem_number", problem_number);
                tinydb.putString("part_letter", part_letter);

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent, options.toBundle());

            }
        });

        findViewById(R.id.btn_load_prob_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                str_problem_number = (TextView)findViewById(R.id.problem_number);
//                str_problem_number.setText("3");
                problem_number = 3;
                part_letter = "A";
                tinydb.putInt("problem_number", problem_number);
                tinydb.putString("part_letter", part_letter);


                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * react to the user tapping/selecting an options menu item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //Toast.makeText(this, "ADD!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MyPreferencesActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    public static View getActionBarView(final Activity activity) {
//        if (activity instanceof IToolbarHolder)
//            return ((IToolbarHolder) activity).getToolbar();
//        final String packageName = activity instanceof ActionBarActivity ? activity.getPackageName() : "android";
//        final int resId = activity.getResources().getIdentifier("action_bar_container", "id", packageName);
//        final View view = activity.findViewById(resId);
//        return view;
//    }
//
//    public interface IToolbarHolder {
//        public android.support.v7.widget.Toolbar getToolbar();
//    }

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