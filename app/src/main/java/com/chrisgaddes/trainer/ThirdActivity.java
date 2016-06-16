package com.chrisgaddes.trainer;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";

    //TODO add pinch to zoom http://stackoverflow.com/questions/30979647/how-to-draw-by-finger-on-canvas-after-pinch-to-zoom-coordinates-changed-in-andro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        DrawArrowsView ev = new DrawArrowsView(this);

        setContentView(ev);
    }

    public class DrawArrowsView extends View {

        // initialize variables
        private Paint paint_points;
        private Paint paint_arrow;
        private Paint paint_box;
        private Path path_arrow;

        private int loc_arrow_point_x;
        private int loc_arrow_point_y;
        private int btn_loc_x;
        private int btn_loc_y;

        private double len_arrow_shaft;
        private double len_arrow_shaft_start;
        private double len_arrow_head;
        private double len_arrow_shaft_current;

        private float dim_btn_radius;
        private float dim_btn_radius_buffer;
        private long time_anim_arrow_dur;

        private double angle;
        private double angle_dif;

        private double angle_degrees;
        private double tmp_angle_dist;
        private double angle_dist;
        private double arrow_animated_fraction;

        private double angle_arrow_head_left;
        private double angle_arrow_head_right;
        private float loc_arrow_head_left_x;
        private float loc_arrow_head_left_y;
        private float loc_arrow_head_right_x;
        private float loc_arrow_head_right_y;

        private boolean clicked_in_button;

        double pi = Math.PI;

        // angles the force arrows snap to


        //double angles[] = {-pi, -3 * pi / 4, -pi / 2, -pi / 4, 0, pi / 4, pi / 2, 3 * pi / 4, pi};
        double angles[] = {-pi, -5*pi/6, -2*pi/3, -pi/2, -pi/3, -pi/6, 0, pi/6, pi/3, pi/2, 2*pi/3, 5*pi/6, pi};


        // initialize ArrayLists for paths and points
        private ArrayList<Point> pointList = new ArrayList<>();
        private ArrayList<Rect> rectList = new ArrayList<>();


        private ArrayList<Path> pathList = new ArrayList<>();

        public DrawArrowsView(Context context) {
            super(context);

            paint_arrow = new Paint();
            path_arrow = new Path();
            paint_box = new Paint();

            // set point locations TODO: import these from database
            // TODO convert these to dp or percentages (note, aspect ratio may not always be same)
            Point pointOne = new Point(275, 500);
            Point pointTwo = new Point(730, 700);
            Point pointThree = new Point(1150, 700);
            Point pointFour = new Point(275, 1200);
            pointList.add(pointOne);
            pointList.add(pointTwo);
            pointList.add(pointThree);
            pointList.add(pointFour);

            paint_points = new Paint();
            btn_loc_x = pointList.get(1).x;
            btn_loc_y = pointList.get(1).y;
            loc_arrow_point_x = btn_loc_x;
            loc_arrow_point_y = btn_loc_y;

            // TODO move these inside loop
            loc_arrow_head_left_x = btn_loc_x;
            loc_arrow_head_left_y = btn_loc_y;
            loc_arrow_head_right_x = btn_loc_x;
            loc_arrow_head_right_y = btn_loc_y;

            clicked_in_button = false;

            // sets constants  // TODO: change these constants to dp of f
            len_arrow_shaft = 200;
            len_arrow_head = 60;
            dim_btn_radius = 30f;
            dim_btn_radius_buffer = 40f;
            time_anim_arrow_dur = 200;

            // create Rects from pointList
            for (Point g : pointList) {
                rectList.add(new Rect(g.x - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), g.y - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), g.x + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), g.y + ((int) dim_btn_radius + (int) dim_btn_radius_buffer)));
            }

            // sets style of arrows
            paint_arrow.setStyle(Paint.Style.FILL);
            paint_arrow.setStrokeWidth(20f);
            paint_arrow.setColor(Color.RED);
            paint_arrow.setStyle(Paint.Style.STROKE);
            paint_arrow.setStrokeCap(Paint.Cap.ROUND);

            //TODO set beginning of shaft to transparent so arrow appears to be at surface
            paint_box.setStyle(Paint.Style.FILL);
            paint_box.setStrokeWidth(5f);
            paint_box.setColor(Color.GRAY);
            paint_box.setStyle(Paint.Style.STROKE);
            paint_box.setPathEffect(new DashPathEffect(new float[]{10, 10, 10, 10}, 0));
            paint_box.setAlpha(70);
        }

        @Override

        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // draws rectangles around points
            for (Rect rect7 : rectList) {
                canvas.drawRect(rect7, paint_box);
            }

            // draws black circle at points in ArrayList pointList
            for (Point ptLst_dots : pointList) {
                canvas.drawCircle(ptLst_dots.x, ptLst_dots.y, dim_btn_radius, paint_points);
            }

            // draws arrows from pathlist pthLst_arrows
            for (Path pthLst_arrows : pathList) {
                canvas.drawPath(pthLst_arrows, paint_arrow);
            }

        }

        public boolean onTouchEvent(MotionEvent event) {
            int eventaction = event.getAction();
            int X = (int) event.getX();
            int Y = (int) event.getY();

            switch (eventaction) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, " ACTION_DOWN Clicked: " + clicked_in_button);

                    for (Rect rect7 : rectList) {
                        if (rect7.contains(X, Y)) {
                            Log.d(TAG, "click contains xy ");
                            clicked_in_button = true;

                            btn_loc_x = rect7.centerX();
                            btn_loc_y = rect7.centerY();
                        }
                    }

                    if (clicked_in_button) {
                        Log.d(TAG, " ACTION_DOWN if was true");
                        path_arrow = new Path();
                        pathList.add(path_arrow); // <-- Add this line.
                        path_arrow.reset();
                        loc_arrow_point_x = X;
                        loc_arrow_point_y = Y;
                        angle = Math.atan2(loc_arrow_point_y - btn_loc_y, loc_arrow_point_x - btn_loc_x);
                        drawArrow();
                        invalidate();// call invalidate to refresh the draw
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    Log.d(TAG, " ACTION_MOVE: " + clicked_in_button);
                    if (clicked_in_button) {

                        path_arrow.reset();
                        loc_arrow_point_x = X;
                        loc_arrow_point_y = Y;
                        angle = Math.atan2(loc_arrow_point_y - btn_loc_y, loc_arrow_point_x - btn_loc_x);
                        drawArrow();
                        invalidate();// call invalidate to refresh the draw
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    Log.d(TAG, " ACTION_UP: " + clicked_in_button);
                    if (clicked_in_button) {
                        clicked_in_button = false;
                        path_arrow.reset();
                        loc_arrow_point_x = X;
                        loc_arrow_point_y = Y;

                        // calculates the angle of arrow at release
                        final double angle_start = Math.atan2(loc_arrow_point_y - btn_loc_y, loc_arrow_point_x - btn_loc_x);

                        // snaps arrow to pi/4 increments
                        angle_dist = Math.abs(angles[0] - angle_start);
                        int idx = 0;
                        for (int c = 1; c < angles.length; c++) {
                            tmp_angle_dist = Math.abs(angles[c] - angle_start);
                            if (tmp_angle_dist < angle_dist) {
                                idx = c;
                                angle_dist = tmp_angle_dist;
                            }
                        }
                        angle_dif = angles[idx] - angle_start;

                        len_arrow_shaft_start = Math.hypot((loc_arrow_point_x - btn_loc_x), (loc_arrow_point_y - btn_loc_y));

                        // animates decrease in length and angle
                        ValueAnimator animator = ValueAnimator.ofFloat((float) len_arrow_shaft_start, (float) len_arrow_shaft);
                        animator.setDuration(time_anim_arrow_dur);
                        animator.setInterpolator(new OvershootInterpolator());
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            public void onAnimationUpdate(ValueAnimator animation) {
                                arrow_animated_fraction = animation.getAnimatedFraction();
                                len_arrow_shaft_current = (Float) animation.getAnimatedValue();
                                angle = angle_start + arrow_animated_fraction * angle_dif;
                                loc_arrow_point_y = (int) (len_arrow_shaft_current * Math.sin(angle) + btn_loc_y);
                                loc_arrow_point_x = (int) (len_arrow_shaft_current * Math.cos(angle) + btn_loc_x);
                                path_arrow.reset();
                                drawArrow();
                                invalidate(); // TODO: Change to invalidate("just the arrow drawn")
                            }
                        });
                        animator.start();

                        // prints angle snapped to in degrees to snackbar
                        // All angles are inverted, so this if statement shows 0.0 instead of -0.0
                        if (angles[idx] == 0.0) {
                            angle_degrees = Math.round(Math.toDegrees(angles[idx]));
                        } else {
                            angle_degrees = Math.round(Math.toDegrees(-angles[idx]));
                        }

                        Snackbar.make(this, "Created force at " + angle_degrees + "\u00B0", Snackbar.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        }

        private void drawArrow() {
            // sets angle of arrow head
            angle_arrow_head_left = 4 * pi / 3 - angle;
            angle_arrow_head_right = -pi / 3 - angle;

            // calculates location of points for both sides of arrow head
            loc_arrow_head_left_x = (float) ((float) len_arrow_head * Math.sin(angle_arrow_head_left) + loc_arrow_point_x);
            loc_arrow_head_left_y = (float) ((float) len_arrow_head * Math.cos(angle_arrow_head_left) + loc_arrow_point_y);
            loc_arrow_head_right_x = (float) ((float) len_arrow_head * Math.sin(angle_arrow_head_right) + loc_arrow_point_x);
            loc_arrow_head_right_y = (float) ((float) len_arrow_head * Math.cos(angle_arrow_head_right) + loc_arrow_point_y);

            // draws arrow shaft
            path_arrow.moveTo(btn_loc_x, btn_loc_y);
            path_arrow.lineTo(loc_arrow_point_x, loc_arrow_point_y);
            path_arrow.moveTo(loc_arrow_point_x, loc_arrow_point_y);

            // draws arrow head
            path_arrow.moveTo(loc_arrow_head_left_x, loc_arrow_head_left_y);
            path_arrow.lineTo(loc_arrow_point_x, loc_arrow_point_y);
            path_arrow.lineTo(loc_arrow_head_right_x, loc_arrow_head_right_y);

        }
    }

//
//    Runnable myRunnable = new Runnable() {
//        @Override
//        public void run() {
//            while (len_arrow_shaft_current > len_arrow_shaft) {
//                Thread.sleep(1000); // Waits for 1 second (1000 milliseconds)
////                String updateWords = updateAuto(); // make updateAuto() return a string
//                loc_arrow_point_y = (int) (len_arrow_shaft * Math.sin(angle3) + btn_loc_y);
//                loc_arrow_point_x = (int) (len_arrow_shaft * Math.cos(angle3) + btn_loc_x);
//
//                drawArrow();
//                invalidate();
//
//                myTextView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        myTextView.setText(updateWords);
//                    }
//
//                });
//            }
//        }
//
//        ;
//
//    };
}