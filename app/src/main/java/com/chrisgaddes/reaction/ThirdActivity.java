package com.chrisgaddes.reaction;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;


public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";

    //TODO add pinch to zoom http://stackoverflow.com/questions/30979647/how-to-draw-by-finger-on-canvas-after-pinch-to-zoom-coordinates-changed-in-andro

    private DrawArrowsView mDrawArrowsView;
    private ImageView mProblem;
    private Context mContext;
//    private Data data = new Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);


//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        binding.setData(data);


        mDrawArrowsView = (DrawArrowsView) findViewById(R.id.idDrawArrowsView);

        final FloatingActionButton btn_check_done = (FloatingActionButton) findViewById(R.id.btn_check_done);
        btn_check_done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDrawArrowsView.runCheckIfFinished();
            }
        });

        final Button mbtn_refresh = (Button) findViewById(R.id.btn_refresh);
        mbtn_refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mDrawArrowsView.resetAllValues();
            }
        });

//        final Button mbtn_change_color = (Button) findViewById(R.id.btn_change_color);
//        mbtn_change_color.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//            }
//        });

        final FloatingActionButton mbtn_help = (FloatingActionButton) findViewById(R.id.btn_help);
        mbtn_help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                helpDialog();
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        helpDialog();
        return true;
    }

}