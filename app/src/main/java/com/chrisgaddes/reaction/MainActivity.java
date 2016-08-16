package com.chrisgaddes.reaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity { // implements View.OnTouchListener {

    private static final String TAG = "MainActivity";
    private ListView listView;
    public TinyDB tinydb;
    private TextView str_problem_number;
    private int problem_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tinydb = new TinyDB(this);


        findViewById(R.id.btn_load_prob_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                str_problem_number = (TextView)findViewById(R.id.problem_number);
//                str_problem_number.setText("1");
                problem_number = 1;
                tinydb.putInt("problem_number", problem_number);

                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_load_prob_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                str_problem_number = (TextView)findViewById(R.id.problem_number);
//                str_problem_number.setText("2");
                problem_number = 2;
                tinydb.putInt("problem_number", problem_number);

                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_load_prob_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                str_problem_number = (TextView)findViewById(R.id.problem_number);
//                str_problem_number.setText("3");
                problem_number = 3;
                tinydb.putInt("problem_number", problem_number);

                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
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



}