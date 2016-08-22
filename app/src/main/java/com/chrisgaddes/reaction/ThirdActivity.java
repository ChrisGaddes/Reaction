package com.chrisgaddes.reaction;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;


public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";

    //TODO add pinch to zoom http://stackoverflow.com/questions/30979647/how-to-draw-by-finger-on-canvas-after-pinch-to-zoom-coordinates-changed-in-andro

    private DrawArrowsView mDrawArrowsView;
    private ImageView mProblem;
    private ImageView IV_peek;
    private Context mContext;
    private Context context;
    private ListView listView;
    private Bitmap peekImage;

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
    private String str_combined_title;

    public static FloatingActionButton btn_check_done;
    private ImageButton btn_peek;

    public TinyDB tinydb;
    private int eventaction;
    private int X;
    private int Y;
    private Toolbar toolbar;
    private int mtoolbar;
    private View decor;

//    private Data data = new Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        setContentView(R.layout.activity_third);

        context = getApplicationContext();

        decor = getWindow().getDecorView();


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        btn_check_done = (FloatingActionButton) findViewById(R.id.btn_check_done);
        btn_peek = (ImageButton) findViewById(R.id.btn_peek);

//        IV_peek = context.getResources().getDrawable(R.mipmap.beam_l_shape);

        IV_peek = (ImageView) findViewById(R.id.IV_peek);


        tinydb = new TinyDB(this);

//        tinydb.putInt("problem_number", 9);

        problem_number = tinydb.getInt("problem_number");
        part_letter = tinydb.getString("part_letter");
        str_part_letter = "Part " + part_letter;

        str_problem_number = "Problem #" + problem_number;

        str_combined_title = str_problem_number + " - " + str_part_letter;

        str_problem_statement = getResources().getStringArray(getResId("mainProblemStatement_" + "prob" + problem_number + "_part" + part_letter, R.array.class));
        str_part_statement = getResources().getStringArray(getResId("problemStatement_" + "prob" + problem_number + "_part" + part_letter, R.array.class));

//        tv_part_letter = (TextView) findViewById(R.id.tv_part_letter);
//        tv_part_letter.setText(str_part_letter);
//
//        tv_part_statement = (TextView) this.findViewById(R.id.tv_part_statement);
//        tv_part_statement.setText(str_part_statement[0]);

//        tv_problem_number = (TextView) findViewById(R.id.tv_problem_number);
//        tv_problem_number.setText(str_problem_number);

        tv_problem_statement = (TextView) this.findViewById(R.id.tv_problem_statement);
        tv_problem_statement.setText(str_problem_statement[0]);

//        toolbar = getSupportActionBar();
//        assert toolbar != null;
        getSupportActionBar().setTitle(str_combined_title);

        mDrawArrowsView = (DrawArrowsView) findViewById(R.id.idDrawArrowsView);


        btn_check_done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                boolean mrunCheckIfFinished = mDrawArrowsView.runCheckIfFinished();
                if (mrunCheckIfFinished) {
                    showDialogArrowsCorrect();
                } else {
//                    Snackbar.make(this, "Not Finished Yet...", Snackbar.LENGTH_SHORT).show();
                }

            }
        });


        String strPeekImage = tinydb.getString("PeekImage");
        Bitmap peekImage = StringToBitMap(strPeekImage);
        IV_peek.setImageBitmap(peekImage);

        btn_peek.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                eventaction = event.getAction();

                switch (eventaction) {
                    case MotionEvent.ACTION_DOWN:
                        getSupportActionBar().setTitle(str_problem_number);
                        IV_peek.setAlpha((float) 1.0);
                        break;

                    case MotionEvent.ACTION_MOVE:
//                onActionMove();
                        break;

                    case MotionEvent.ACTION_UP:
                        getSupportActionBar().setTitle(str_combined_title);
                        IV_peek.setAlpha((float) 0.0);
                        break;
                }
                return true;
            }
        });


        // dialog that says arrows were placed correctly


//        final Button mbtn_refresh = (Button) findViewById(R.id.action_refresh);
//        mbtn_refresh.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                mDrawArrowsView.resetAllValues();
//            }
//        });

//        final Button mbtn_change_color = (Button) findViewById(R.id.btn_change_color);
//        mbtn_change_color.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//            }
//        });

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
        getWindow().setEnterTransition(slide);
        getWindow().setReturnTransition(explode);
        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
    }

//    public boolean onTouch(, MotionEvent event) {
//        eventaction = event.getAction();
//
//        switch (eventaction) {
//            case MotionEvent.ACTION_DOWN:
//                String strPeekImage = tinydb.getString("PeekImage");
//
//                Bitmap peekImage = StringToBitMap(strPeekImage);
//                IV_peek.setAlpha((float) 1.0);
//                IV_peek.setImageBitmap(peekImage);
//                break;
//
//            case MotionEvent.ACTION_MOVE:
////                onActionMove();
//                break;
//
//            case MotionEvent.ACTION_UP:
//                IV_peek.setAlpha((float) 0.0);
//                break;
//        }
//        return true;
//    }


    private void showDialogArrowsCorrect() {
        new MaterialStyledDialog(this)
                .setTitle("Correct!")
                .setDescription(R.string.str_all_arrows_placed_correctly)
                .setIcon(R.drawable.ic_check)
                .setStyle(Style.HEADER_WITH_ICON)

                .setPositive(getResources().getString(R.string.str_next_problem), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Log.d("MaterialStyledDialogs", "Do something!");

                        Intent intent = getIntent();

                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ThirdActivity.this);

                        switch (part_letter) {
                            case "A":
                                tinydb.putString("part_letter", "B");
                                finish();
                                startActivity(intent, options.toBundle());
                                break;
                            case "B":
                                tinydb.putString("part_letter", "C");
                                finish();
                                startActivity(intent, options.toBundle());
                                break;
                            case "C":
                                tinydb.putString("part_letter", "A");
//                                part_letter = "A";
//                                problem_number++;
                                // TODO put logic in here so it knows how many problems there are
                                tinydb.putInt("problem_number", problem_number++);
                                startActivity(intent, options.toBundle());
                                break;
                        }

//                        switch (part_letter) {
//
//                            case "A":
//                                tinydb.putString("part_letter", "B");
//                                finish();
//                                startActivity(intent);
//                            case "B":
//                                tinydb.putString("part_letter", "C");
//                                finish();
//                                startActivity(intent);
//                            case "C":
//                                tinydb.putString("part_letter", "A");
////                                problem_number++;
//                                // TODO put logic in here so it knows how many problems there are
//                                tinydb.putInt("problem_number", problem_number++);
//                                Intent i = new Intent(getApplicationContext(), SecondActivity.class);
//                                startActivity(i);
//                        }


//                        Context context = getApplicationContext();
//                        Intent l = new Intent(context, ThirdActivity.class);
//                        startActivity(l);

//                        Intent i = new Intent(getApplicationContext(), ThirdActivity.class);
//                        startActivity(i);

                    }
                })

                .show();
    }

    public void changeFabColor() {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed}  // pressed
        };

        int[] colors = new int[]{
                Color.GREEN,
                Color.GREEN,
                Color.GREEN,
                Color.BLUE
        };
        ColorStateList myList = new ColorStateList(states, colors);
        btn_check_done.setBackgroundTintList(myList);
//        return(0);
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