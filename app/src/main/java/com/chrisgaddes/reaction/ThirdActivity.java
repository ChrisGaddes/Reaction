package com.chrisgaddes.reaction;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;


public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";

    //TODO add pinch to zoom http://stackoverflow.com/questions/30979647/how-to-draw-by-finger-on-canvas-after-pinch-to-zoom-coordinates-changed-in-andro

    DrawArrowsView mDrawArrowsView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

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
                .setDescription("Tap and drag at appropriate locations to add forces and long press to add moments\n\nPress the Restart button to remove all arrows and start over\n\nPress the check button when finished")
                .setIcon(R.drawable.help)
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