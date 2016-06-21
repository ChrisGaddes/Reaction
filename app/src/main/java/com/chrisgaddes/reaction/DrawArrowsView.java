package com.chrisgaddes.reaction;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class DrawArrowsView extends ImageView {

    private static final String TAG = "ThirdActivity";

    final double pi = Math.PI;

    //double angles[] = {-pi, -3 * pi / 4, -pi / 2, -pi / 4, 0, pi / 4, pi / 2, 3 * pi / 4, pi};
    final double[] angles = {-pi, -5 * pi / 6, -2 * pi / 3, -pi / 2, -pi / 3, -pi / 6, 0, pi / 6, pi / 3, pi / 2, 2 * pi / 3, 5 * pi / 6, pi};

    // initialize variables
    private final Paint paint_points;
    private final Paint paint_arrow;
    private final Paint paint_arrow_head_box;
    private final Paint paint_box;
    private final Paint paint_text;
    private final double len_arrow_shaft;
    private final double len_arrow_head;
    private final float dim_btn_radius;
    private final float dim_btn_radius_buffer;
    private final long time_anim_arrow_dur;

    private int k;
    private int b;
    private int rectList_indice;
    private int rectListArrowHead_indice;
    private boolean clicked_on_arrow_head;
    private long mLastClickTime;

    // initialize ArrayLists for paths and points
    private ArrayList<Point> pointList = new ArrayList<>();
    private ArrayList<Rect> rectListButtons = new ArrayList<>();
    private ArrayList<Rect> rectListArrowHead = new ArrayList<>();
    private ArrayList<Path> pathList = new ArrayList<>();
    private ArrayList<Point> pointListArrowHead = new ArrayList<>();
    private ArrayList<Integer> linkList = new ArrayList<>();

    private Path path_arrow;

    private int btn_loc_x;
    private int loc_arrow_point_x;
    private int loc_arrow_point_y;
    private int btn_loc_y;
    private double len_arrow_shaft_start;
    private double len_arrow_shaft_current;
    private double angle;
    private double angle_dif;
    private double angle_degrees;
    private double tmp_angle_dist;
    private double angle_dist;
    private double arrow_animated_fraction;
    private double angle_arrow_head_left;
    private double angle_arrow_head_right;
    private float loc_arrow_head_left_x;

    // angles the force arrows snap to
    private float loc_arrow_head_left_y;
    private float loc_arrow_head_right_x;
    private float loc_arrow_head_right_y;
    private boolean clicked_in_button;
    private boolean inside_button;
    private boolean able_to_click;

    private int pointerCount;

    public DrawArrowsView(Context context) {
        super(context);

        // create new paints
        paint_arrow = new Paint();
        path_arrow = new Path();
        paint_box = new Paint();
        paint_arrow_head_box = new Paint();
        paint_text = new Paint();

        // set point locations TODO: import these from database
        // TODO convert these to dp or percentages (note, aspect ratio may not always be same)
        Point pointOne = new Point(190, 719);
        Point pointThree = new Point(730, 1407);
        Point pointFour = new Point(1303, 1407);
        Point pointTwo = new Point(199, 1407);
        pointList.add(pointOne);
        pointList.add(pointTwo);
        pointList.add(pointThree);
        pointList.add(pointFour);

        paint_points = new Paint();
        btn_loc_x = pointList.get(1).x;
        btn_loc_y = pointList.get(1).y;
        loc_arrow_point_x = btn_loc_x;
        loc_arrow_point_y = btn_loc_y;

        loc_arrow_head_left_x = btn_loc_x;
        loc_arrow_head_left_y = btn_loc_y;
        loc_arrow_head_right_x = btn_loc_x;
        loc_arrow_head_right_y = btn_loc_y;

        clicked_in_button = false;
        inside_button = false;
        clicked_on_arrow_head = false;
        able_to_click = true;

        // sets constants  // TODO: change these constants to dp of f
        len_arrow_shaft = 200;
        len_arrow_head = 60;
        dim_btn_radius = 15f;
        dim_btn_radius_buffer = 60f;

        // TODO allow user to set this value to disable animations
        time_anim_arrow_dur = 150;

        mLastClickTime = 0;

        // create Rects from pointList to create buttons at nodes
        for (Point g : pointList) {
            rectListButtons.add(new Rect(g.x - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), g.y - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), g.x + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), g.y + ((int) dim_btn_radius + (int) dim_btn_radius_buffer)));
        }

        // sets style of arrows
        paint_arrow.setStyle(Paint.Style.FILL);
        paint_arrow.setStrokeWidth(20f);
        paint_arrow.setColor(Color.RED);
        paint_arrow.setStyle(Paint.Style.STROKE);
        paint_arrow.setStrokeCap(Paint.Cap.ROUND);

        paint_arrow_head_box.setStyle(Paint.Style.FILL);
        paint_arrow_head_box.setStrokeWidth(5f);
        paint_arrow_head_box.setColor(Color.GREEN);
        paint_arrow_head_box.setStyle(Paint.Style.STROKE);
        paint_arrow_head_box.setPathEffect(new DashPathEffect(new float[]{10, 10, 10, 10}, 0));

        paint_text.setTextSize(23f);

        //TODO set beginning of shaft to transparent so arrow appears to be at surface
        paint_box.setStyle(Paint.Style.FILL);
        paint_box.setStrokeWidth(5f);
        paint_box.setColor(Color.GRAY);
        paint_box.setStyle(Paint.Style.STROKE);
        paint_box.setPathEffect(new DashPathEffect(new float[]{10, 10, 10, 10}, 0));
        paint_box.setAlpha(80); // TODO remove hard coded alpha
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setImageResource(R.drawable.fbd_1);

        // draws rectangles around arrowheads
        for (Rect rect1 : rectListArrowHead) {
            canvas.drawRect(rect1, paint_arrow_head_box);
        }

        // draws rectangles around points
        for (Rect rect7 : rectListButtons) {
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

        // prints variables for debugging
        canvas.drawText("inside_button = " + String.valueOf(inside_button), 20, 100, paint_text);
        canvas.drawText("rectList_indice = " + String.valueOf(rectList_indice), 20, 160, paint_text);
        canvas.drawText("rectListArrowHead_indice = " + String.valueOf(rectListArrowHead_indice), 20, 220, paint_text);
        canvas.drawText("linkList = " + String.valueOf(linkList), 20, 280, paint_text);
        canvas.drawText("pointListArrowHead = " + String.valueOf(pointListArrowHead), 20, 1840, paint_text);
        canvas.drawText("rectListArrowHead.size = " + String.valueOf(rectListArrowHead.size()), 20, 460, paint_text);
        canvas.drawText("clicked_in_button = " + String.valueOf(clicked_in_button), 20, 580, paint_text);
        canvas.drawText("clicked_on_arrow_head = " + String.valueOf(clicked_in_button), 20, 640, paint_text);

        canvas.drawText("inside_button = " + String.valueOf(inside_button), 600, 100, paint_text);
        canvas.drawText("size of pointlist = " + String.valueOf(pointList.size()), 600, 160, paint_text);
        canvas.drawText("size of pathlist = " + String.valueOf(pathList.size()), 600, 220, paint_text);
        canvas.drawText("size of linklist = " + String.valueOf(linkList.size()), 600, 280, paint_text);
        canvas.drawText("size of pointListArrowHead = " + String.valueOf(pointListArrowHead.size()), 600, 340, paint_text);
        canvas.drawText("size of rectListArrowHead = " + String.valueOf(rectListArrowHead.size()), 600, 400, paint_text);
        canvas.drawText("arrow_animated_fraction = " + String.valueOf(arrow_animated_fraction), 600, 460, paint_text);
        canvas.drawText("able_to_click = " + String.valueOf(able_to_click), 600, 520, paint_text);
        canvas.drawText("mLastClickTime = " + String.valueOf(mLastClickTime), 600, 580, paint_text);
        canvas.drawText("event.getPointerCount(); = " + String.valueOf(pointerCount), 600, 640, paint_text);
    }

    // triggers long press
    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            Log.i("", "Long press!");
            Toast.makeText(getContext(), "Long Pressed!", Toast.LENGTH_SHORT).show();

            // TODO: Add snackbar. FIgure out view
            // Snackbar.make(findViewById(), "Created Moment" , Snackbar.LENGTH_SHORT).show();

            path_arrow.reset();
            pointListArrowHead.remove(rectListArrowHead_indice);
            linkList.remove(rectListArrowHead_indice);
            rectListArrowHead.remove(rectListArrowHead_indice);
            pathList.remove(rectListArrowHead_indice);

            // set booleans to false state
            clicked_in_button = false;
            clicked_on_arrow_head = false;
            inside_button = false;
            invalidate();
        }
    };

    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        int X = (int) event.getX();
        int Y = (int) event.getY();


        pointerCount = event.getPointerCount();

        //TODO use getPointerId to prevent two fingers from touching

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:

                Log.d(TAG, "ACTION_DOWN");

                invalidate();
                if (able_to_click) {
                    // TODO add error if lengths of lists are not the same
//                    if (pointListArrowHead.size() == pathList.size() && pathList.size() == linkList.size() ){
//                        Toast.makeText(getApplicationContext(), "Error: Not the same length", Toast.LENGTH_SHORT).show();
//                    }

                    k = 0;
                    for (Rect rect_tmp1 : rectListButtons) {
                        if (rect_tmp1.contains(X, Y)) {
                            clicked_in_button = true;
                            clicked_on_arrow_head = false;

                            // starts long press timer
                            handler.postDelayed(mLongPressed, 500);

                            rectList_indice = k;
                            btn_loc_x = rect_tmp1.centerX();
                            btn_loc_y = rect_tmp1.centerY();
                            rectListArrowHead_indice = rectListArrowHead.size();
                        }
                        k++;
                    }

                    // check if arrow head is clicked on
                    b = 0;
                    if (!clicked_in_button) {
                        for (Rect rect_tmp2 : rectListArrowHead) {
                            if (rect_tmp2.contains(X, Y)) {
                                clicked_on_arrow_head = true;
                                clicked_in_button = false;
                                rectListArrowHead_indice = b;
                                btn_loc_x = rectListButtons.get(linkList.get(rectListArrowHead_indice)).centerX();
                                btn_loc_y = rectListButtons.get(linkList.get(rectListArrowHead_indice)).centerY();
                            }
                            b++;
                        }
                    }

                    if (clicked_in_button) {
                        path_arrow = new Path();
                        pathList.add(path_arrow); // <-- Add this line.
                        path_arrow.reset();
                        loc_arrow_point_x = X;
                        loc_arrow_point_y = Y;
                        angle = Math.atan2(loc_arrow_point_y - btn_loc_y, loc_arrow_point_x - btn_loc_x);

                        pointListArrowHead.add(new Point(loc_arrow_point_x, loc_arrow_point_y));
                        linkList.add(rectList_indice);
                        rectListArrowHead.add(new Rect(loc_arrow_point_x - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_x + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y + ((int) dim_btn_radius + (int) dim_btn_radius_buffer)));

                        drawArrow();
                        invalidate();

                    } else if (clicked_on_arrow_head) {
                        // replace arrow
                        path_arrow = new Path();
                        pathList.set(rectListArrowHead_indice, path_arrow); // <-- Add this line.
                        path_arrow.reset();
                        loc_arrow_point_x = X;
                        loc_arrow_point_y = Y;

                        btn_loc_x = rectListButtons.get(linkList.get(rectListArrowHead_indice)).centerX();
                        btn_loc_y = rectListButtons.get(linkList.get(rectListArrowHead_indice)).centerY();

                        angle = Math.atan2(loc_arrow_point_y - btn_loc_y, loc_arrow_point_x - btn_loc_x);
                        drawArrow();
                        invalidate();
                    }
                } else {
                    Toast.makeText(getContext(), "Don't tap so quickly", Toast.LENGTH_SHORT).show();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (clicked_in_button || clicked_on_arrow_head) {

                    // checks if touch is inside button
                    if (rectListButtons.get(rectList_indice).contains(X, Y)) {
                        inside_button = true;
                    } else {
                        inside_button = false;
                        // cancels long press handler if touch is dragged outside box
                        handler.removeCallbacks(mLongPressed);
                    }

                    path_arrow.reset();
                    loc_arrow_point_x = X;
                    loc_arrow_point_y = Y;
                    angle = Math.atan2(loc_arrow_point_y - btn_loc_y, loc_arrow_point_x - btn_loc_x);
                    pointListArrowHead.set(rectListArrowHead_indice, new Point(loc_arrow_point_x, loc_arrow_point_y));
                    rectListArrowHead.set(rectListArrowHead_indice, new Rect(loc_arrow_point_x - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_x + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y + ((int) dim_btn_radius + (int) dim_btn_radius_buffer)));

                    drawArrow();
                    invalidate();

                }
                break;

            case MotionEvent.ACTION_UP:
                if (clicked_in_button || clicked_on_arrow_head) {

                    Log.d(TAG, "ACTION_UP");

                    // checks if released inside button

//                        rectListButtons.get(linkList.get(rectListArrowHead_indice));

//                        k = 0;
//                        for (Rect rect_tmp3 : rectListButtons) {
                    if (rectListButtons.get(linkList.get(rectListArrowHead_indice)).contains(X, Y)) {
                        inside_button = true;
                    }
//                            k++;
//                        }

                    if (!inside_button) {
                        inside_button = false; // TODO why us this here?
                    }

                    // break if released inside button
                    if (inside_button) {
                        path_arrow.reset();
                        handler.removeCallbacks(mLongPressed);
                        //TODO add this back long_press
                        pointListArrowHead.remove(rectListArrowHead_indice);
                        linkList.remove(rectListArrowHead_indice);
                        rectListArrowHead.remove(rectListArrowHead_indice);
                        pathList.remove(rectListArrowHead_indice);

                        // set booleans to false state
                        clicked_in_button = false;
                        clicked_on_arrow_head = false;

                        inside_button = false;
                        break;
                    }

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

                            pointListArrowHead.set(rectListArrowHead_indice, new Point(loc_arrow_point_x, loc_arrow_point_y));
                            rectListArrowHead.set(rectListArrowHead_indice, new Rect(loc_arrow_point_x - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_x + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y + ((int) dim_btn_radius + (int) dim_btn_radius_buffer)));

                            able_to_click = false;
                            // stops loop at end of animation
                            if (arrow_animated_fraction == 1) {
                                clicked_in_button = false;
                                clicked_on_arrow_head = false;
                                able_to_click = true;
                            }

                            drawArrow();
                            invalidate(); // TODO: Change to invalidate("just the arrow drawn")
                        }
                    });

                    animator.start();
                    invalidate();
                    // prints angle snapped to in degrees to snackbar
                    // All angles are inverted, so this if statement shows 0.0 instead of -0.0
                    if (angles[idx] == 0.0) {
                        angle_degrees = Math.round(Math.toDegrees(angles[idx]));
                    } else {
                        angle_degrees = Math.round(Math.toDegrees(-angles[idx]));
                    }

                    Snackbar.make(this, "Created force at " + angle_degrees + "\u00B0", Snackbar.LENGTH_SHORT).show();
                    inside_button = false;
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