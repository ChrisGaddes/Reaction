package com.chrisgaddes.reaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DrawArrowsView extends ImageView {

    private static final String TAG = "ThirdActivity";

    final double pi = Math.PI;

    //double angles[] = {-pi, -3 * pi / 4, -pi / 2, -pi / 4, 0, pi / 4, pi / 2, 3 * pi / 4, pi};
    final double[] angles = {-pi, -5 * pi / 6, -2 * pi / 3, -pi / 2, -pi / 3, -pi / 6, 0, pi / 6, pi / 3, pi / 2, 2 * pi / 3, 5 * pi / 6, pi, 2 * pi};

    // Declare variables

    // TODO: Add descriptions of what each member variable is used for
    /**
     * Description for what paint_points does...
     */
    private final Paint paint_points;

    /**
     * Description for what paint_arrow does...
     */
    private final Paint paint_arrow;

    /**
     * Description for what paint_arrow_head_box does...
     */
    private final Paint paint_arrow_head_box;

    /**
     * Pai
     */

    private final Paint paint_arrow_correct_location;
    private final Paint paint_box;
    private final Paint paint_text;
    private final Paint paint_angle_check;
    private final double len_arrow_shaft;
    private final double len_arrow_shaft_spring;
    private double len_btn_to_touch;
    private double len_arrow_head;
    private double len_arrow_shaft_start;
    private double dim_moment_radius;
    private double len_final_anim;
    private final float dim_btn_radius;
    private final float dim_btn_radius_buffer;
    private long time_anim_arrow_dur;

    private int rectList_indice;
    private int rectListArrowHead_indice;
    private boolean clicked_on_arrow_head;
    private boolean bool_moved_past_moment_radius_already;



    //TODO consider http://stackoverflow.com/questions/32324876/how-to-save-an-answer-in-a-riddle-game-without-creating-a-database

    // initialize ArrayLists for paths and points


    // TODO figure out how to do this without breaking code sicne they are no longer private if moved inside constructor
    /**
     * Chris:
     * Per comment above at line 42, I would recommend initializing these in the constructor
     * (public DrawArrowsView(Context, context, AttributeSet attrs)
     */
    private ArrayList<Point> pointList = new ArrayList<>();

    List<List<List<Double>>> checkMatrix = new ArrayList<>();


    private ArrayList<Rect> rectListButtons;
    private ArrayList<Rect> rectListArrowHead;
    private ArrayList<Path> pathList;
    private ArrayList<Path> pathListWrong;
    private ArrayList<Path> pathListMoments;
    private ArrayList<Path> pathListMomentsWrong;
    private ArrayList<Point> pointListArrowHead;
    private ArrayList<Double> angleListArrowHead;
    private ArrayList<Integer> linkList;
    private ArrayList<Boolean> isMomentList;
    List<ArrayList<Integer>> linkList2;

    private int moment_radius;

    private RectF oval_moment;
    private int startAngleMoment;
    private int sweepAngleMoment;

    private Path path_arrow;
//    private Path path_moment;
    private Path null_path;

    private int X;
    private int Y;

    private int btn_loc_x;
    private int btn_loc_y;
    private int loc_arrow_point_x;
    private int loc_arrow_point_y;

    private double len_arrow_shaft_current;
    private double angle;
    private double opp_ang;
    private double angle_difference;
    private double arrow_animated_fraction;

    private boolean clicked_on_button;
    private boolean inside_button;
    private boolean able_to_click;
    private boolean already_done;
    private boolean match;

    private boolean debuggingTextToggle;

    private long viewHeight;
    private long viewWidth;
    private Drawable mFocusedImage;
    private Drawable mGrayedImage;


    /**
     * Description of what this Constructor does/is used for...
     *
     * @param context What is the context argument used for?
     * @param attrs   What is the attrs argument used for?
     */
    public DrawArrowsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // TODO replace depreciated "getDrawable" with something else
        mFocusedImage = context.getResources().getDrawable(R.drawable.fbd_2_bar);
        mGrayedImage = context.getResources().getDrawable(R.drawable.fbd_2_greyed);

        pointList = new ArrayList<>();
        checkMatrix = new ArrayList<>();
        rectListButtons = new ArrayList<>();
        rectListArrowHead = new ArrayList<>();

        pathListMoments = new ArrayList<>();
        pathListMomentsWrong = new ArrayList<>();
        pathList = new ArrayList<>();
        pathListWrong = new ArrayList<>();

        pointListArrowHead = new ArrayList<>();
        angleListArrowHead = new ArrayList<>();
        linkList = new ArrayList<>();
        isMomentList = new ArrayList<>();

        linkList2 = new ArrayList<>();
        // add three rows total to linklist2
        linkList2.add(new ArrayList<Integer>());
        linkList2.add(new ArrayList<Integer>());
        linkList2.add(new ArrayList<Integer>());

        // create new paints
        paint_arrow = new Paint();
        paint_arrow_correct_location = new Paint();
        path_arrow = new Path();
//        path_moment = new Path();
        paint_box = new Paint();
        paint_arrow_head_box = new Paint();
        paint_text = new Paint();
        paint_points = new Paint();
        paint_angle_check = new Paint();
        oval_moment = new RectF();

        // set values to false to begin
        clicked_on_button = false;
        inside_button = false;
        clicked_on_arrow_head = false;
        able_to_click = true;
        already_done = false;

        // sets dimensions of arrow, nodes, and touch areas
        len_arrow_shaft = dpToPx(62);
        dim_moment_radius = dpToPx(30);
        len_arrow_shaft_spring = dpToPx(7);
        len_arrow_head = dpToPx(19);
        dim_btn_radius = dpToPx(4);
        dim_btn_radius_buffer = dpToPx(19);

        // set dimensions of moments
        moment_radius = dpToPx(30);
        startAngleMoment = 120;
        sweepAngleMoment = 243;

        setArrowStyle();
    }

    /**
     * @param context //TODO add what context is used for
     */
    private void setButtonPoints(Context context) {
        // this method sets the location of the points
        // TODO: import these from database

        already_done = true;
        match = false;

        // set node locations - touch "button" zones will be placed in boxes around these nodes
        PointF pointOne = new PointF((float) 18, (float) 31.5);
        PointF pointTwo = new PointF((float) 18, (float) 62.1);
        PointF pointThree = new PointF((float) 53.5, (float) 62.1);
        PointF pointFour = new PointF((float) 87.55, (float) 62.1);

        // pointList.add(percentToPx(pointOne));
        pointList.add(percentToPx(pointTwo));
        pointList.add(percentToPx(pointThree));
        pointList.add(percentToPx(pointFour));

        // create Rects from pointList to create buttons at nodes
        for (Point g : pointList) {
            rectListButtons.add(new Rect(g.x - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), g.y - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), g.x + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), g.y + ((int) dim_btn_radius + (int) dim_btn_radius_buffer)));
        }

        loadArrowCheckLocations();
    }

    private void loadArrowCheckLocations() {
        // add buttons layers
        checkMatrix.add(new ArrayList<List<Double>>());
        checkMatrix.add(new ArrayList<List<Double>>());
        checkMatrix.add(new ArrayList<List<Double>>());

        // add 2nd layer
        int btn = 0;
        checkMatrix.get(btn).add(new ArrayList<Double>());
        checkMatrix.get(btn).add(new ArrayList<Double>());
        checkMatrix.get(btn).add(new ArrayList<Double>());
        checkMatrix.get(btn).add(new ArrayList<Double>());
        checkMatrix.get(btn).add(new ArrayList<Double>());
        btn = 1;
        checkMatrix.get(btn).add(new ArrayList<Double>());
        checkMatrix.get(btn).add(new ArrayList<Double>());
        checkMatrix.get(btn).add(new ArrayList<Double>());
        checkMatrix.get(btn).add(new ArrayList<Double>());
        checkMatrix.get(btn).add(new ArrayList<Double>());
        btn = 2;
        checkMatrix.get(btn).add(new ArrayList<Double>());
        checkMatrix.get(btn).add(new ArrayList<Double>());
        checkMatrix.get(btn).add(new ArrayList<Double>());
        checkMatrix.get(btn).add(new ArrayList<Double>());
        checkMatrix.get(btn).add(new ArrayList<Double>());

        // add third layer (contains angles)
        // [btn#, 0] - "angle"
        btn = 0;
        int type_num = 0;
        checkMatrix.get(btn).get(type_num).add(pi / 2);
        checkMatrix.get(btn).get(type_num).add(pi);
        checkMatrix.get(btn).get(type_num).add(100.0);
        checkMatrix.get(btn).get(type_num).add(100.0);

        // [btn#, 1] - Used
        btn = 0;
        type_num = 1;
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);

        // [btn#, 1] - Force angle
        btn = 0;
        type_num = 2;
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);

        // [btn#, 3] - Opposite Allowed
        btn = 0;
        type_num = 3;
        checkMatrix.get(btn).get(type_num).add(1.0);
        checkMatrix.get(btn).get(type_num).add(1.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);

        // [btn#, 4] - Link data
        btn = 0;
        type_num = 4;
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);

        // 2nd button

        // add third layer (contains angles)
        // [btn#, 0] - "angle"
        btn = 1;
        type_num = 0;
        checkMatrix.get(btn).get(type_num).add(pi / 2);
        checkMatrix.get(btn).get(type_num).add(100.0);
        checkMatrix.get(btn).get(type_num).add(100.0); // set to 100
        checkMatrix.get(btn).get(type_num).add(100.0);

        // [btn#, 1] - Used
        btn = 1;
        type_num = 1;
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);

        // [btn#, 1] - Force angle
        btn = 1;
        type_num = 2;
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);

        // [btn#, 3] - Opposite Allowed
        btn = 1;
        type_num = 3;
        checkMatrix.get(btn).get(type_num).add(1.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);

        // [btn#, 4] - Link data
        btn = 1;
        type_num = 4;
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);

        // 3rd button

        // add third layer (contains angles)
        // [btn#, 0] - "angle"
        btn = 2;
        type_num = 0;

        checkMatrix.get(btn).get(type_num).add(-5 * pi / 6);
        checkMatrix.get(btn).get(type_num).add(100.0);
        checkMatrix.get(btn).get(type_num).add(100.0); // set to 100
        checkMatrix.get(btn).get(type_num).add(100.0);

        // [btn#, 1] - Used
        btn = 2;
        type_num = 1;
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);

        // [btn#, 1] - Force angle
        btn = 2;
        type_num = 2;
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);

        // [btn#, 3] - Opposite Allowed
        btn = 2;
        type_num = 3;
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);

        // [btn#, 4] - Link data
        btn = 2;
        type_num = 4;
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
        checkMatrix.get(btn).get(type_num).add(0.0);
    }

    /**
     * Describe the setArrowStyle function
     */
    private void setArrowStyle() {

        // gets shared preferences
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());

        // toggles debugging text - this value can be changed in menu in app
        debuggingTextToggle = SP.getBoolean("debuggingTextToggle", false);

        // toggles visibility of dots at nodes - this value can be changed in menu in app
        boolean nodeDotsToggle = SP.getBoolean("nodeDotsToggle", false);
        if (!nodeDotsToggle) {
            paint_points.setAlpha(0);
        }

        // sets style of angle check indicator
        paint_angle_check.setStyle(Paint.Style.FILL);
        paint_angle_check.setStrokeWidth(dpToPx(6));
        paint_angle_check.setColor(Color.RED);
        paint_angle_check.setStyle(Paint.Style.STROKE);
        paint_angle_check.setStrokeCap(Paint.Cap.ROUND);

        // sets style of arrows
        paint_arrow.setStyle(Paint.Style.FILL);
        paint_arrow.setStrokeWidth(dpToPx(4));
        paint_arrow.setColor(Color.BLACK);
        paint_arrow.setAlpha(40);
        paint_arrow.setStyle(Paint.Style.STROKE);
        paint_arrow.setStrokeCap(Paint.Cap.ROUND);

        // sets style of arrow if placed at wrong location
        paint_arrow_correct_location.setStyle(Paint.Style.FILL);
        paint_arrow_correct_location.setStrokeWidth(dpToPx(6));
        paint_arrow_correct_location.setColor(Color.RED);
        paint_arrow_correct_location.setStyle(Paint.Style.STROKE);
        paint_arrow_correct_location.setStrokeCap(Paint.Cap.ROUND);

        // toggles arrow animation - this value can be changed in menu in app
        boolean animationToggle = SP.getBoolean("animationToggle", false);
        if (animationToggle) {
            // arrow animations enabled
            time_anim_arrow_dur = 150;
        } else {
            // arrow animations disabled
            time_anim_arrow_dur = 0;
        }

        // sets style of boxes around arrow heads
        paint_arrow_head_box.setStyle(Paint.Style.FILL);
        paint_arrow_head_box.setStrokeWidth(dpToPx(2));
        paint_arrow_head_box.setStyle(Paint.Style.STROKE);
        paint_arrow_head_box.setPathEffect(new DashPathEffect(new float[]{10, 10, 10, 10}, 0));

        // toggles visibility of boxes around arrow heads - this value can be changed in menu in app
        boolean arrowBoxesToggle = SP.getBoolean("arrowBoxesToggle", false);
        if (arrowBoxesToggle) {
            paint_arrow_head_box.setColor(Color.GREEN);
        } else {
            paint_arrow_head_box.setColor(Color.TRANSPARENT);
        }

        // sets text size for debugging text
        paint_text.setTextSize(23f);

        // sets style of boxes around nodes
        paint_box.setStyle(Paint.Style.FILL);
        paint_box.setStrokeWidth(5f);
        paint_box.setStyle(Paint.Style.STROKE);
        paint_box.setPathEffect(new DashPathEffect(new float[]{10, 10, 10, 10}, 0));

        // toggles visibility of boxes around nodes
        boolean nodeBoxesToggle = SP.getBoolean("nodeBoxesToggle", false);
        if (nodeBoxesToggle) {
            paint_box.setColor(Color.GRAY);
            paint_box.setAlpha(80); // TODO remove hard coded alpha
        } else {
            paint_box.setColor(Color.TRANSPARENT);
            paint_box.setAlpha(0);
        }
    }

    /**
     * @param xNew
     * @param yNew
     * @param xOld
     * @param yOld
     */
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        // gets width of height of the view
        viewWidth = xNew;
        viewHeight = yNew;

        // I needed the size of the canvas in order to calculate the percentage the points were accross the screen, but it won't properly get the size until the view is drawn. So, I trigger setButtonPoints from here once the view is drawn and use the if statement below to only allow it to run once. This seems like a terrible was to do it but it works. I'd love advice on how to improve this if you think it matters"

        if (!already_done) {
            setButtonPoints(getContext());
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Paint paint_moments = new Paint();
//        final RectF rect = new RectF();
//        //Example values
//
//        int mWidth = 500;
//        int mRadius = 80;
//        int mHeight = 500;
//
//        rect.set(mWidth/2- mRadius, mHeight/2 - mRadius, mWidth/2 + mRadius, mHeight/2 + mRadius);
//        paint_moments.setColor(Color.GREEN);
//        paint_moments.setStrokeWidth(20);
//        paint_moments.setAntiAlias(true);
////        paint_moments.setStrokeCap(Paint.Cap.ROUND);
//        paint_moments.setStyle(Paint.Style.STROKE);
//        canvas.drawArc(rect, -70, 140, false, paint_moments);


        // TODO look into clippath. May be able to use it to isolate the members

        // TODO move some of this outside of onDraw for efficiency
        // JAMES SENTELL: "I don't have a deep understanding on getClipBounds(). DO you? If so, do you have any advice on how to set the bounds of the image to the full size of the view? I have a func"

        /**
         * Not entirely sure, perhaps try the following (straight from stackoverflow - use at own
         * risk...)
         *
         * ImageView imageView = (ImageView)findViewById(R.id.imageview);
         * Drawable drawable = imageView.getDrawable();
         * Rect imageBounds = drawable.getBounds();
         */
        Rect imageBounds = canvas.getClipBounds();  // Adjust this for where you want it
        mGrayedImage.setBounds(imageBounds);
        mFocusedImage.setBounds(imageBounds);

        mGrayedImage.draw(canvas);
        mFocusedImage.draw(canvas);

        // draws rectangles around arrowheads
        for (Rect rect1 : rectListArrowHead) {
            canvas.drawRect(rect1, paint_arrow_head_box);
        }

//        canvas.drawArc(50.0, );

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

        // draws wrong arrows from pathlist pthLst_arrows
        for (Path pthLst_arrows : pathListWrong) {
            canvas.drawPath(pthLst_arrows, paint_arrow_correct_location);
        }

        // draws arrows from pathlist pthLst_arrows
        for (Path pthLst_moment : pathListMoments) {
            canvas.drawPath(pthLst_moment, paint_arrow_correct_location);
        }

//        if (match) {
//            paint_angle_check.setColor(Color.GREEN);
//        } else {
//            paint_angle_check.setColor(Color.RED);
//        }
//        canvas.drawCircle(1000, 500, 50, paint_angle_check);

        if (debuggingTextToggle) {

            canvas.drawText("inside_button = " + String.valueOf(inside_button), 20, 100, paint_text);
            canvas.drawText("rectList_indice = " + String.valueOf(rectList_indice), 20, 160, paint_text);
            canvas.drawText("rectListArrowHead_indice = " + String.valueOf(rectListArrowHead_indice), 20, 220, paint_text);
            canvas.drawText("linkList = " + String.valueOf(linkList), 20, 280, paint_text);

            canvas.drawText("rectListArrowHead.size = " + String.valueOf(rectListArrowHead.size()), 20, 460, paint_text);
            canvas.drawText("clicked_on_button = " + String.valueOf(clicked_on_button), 20, 580, paint_text);
            canvas.drawText("clicked_on_arrow_head = " + String.valueOf(clicked_on_button), 20, 640, paint_text);

            canvas.drawText("linkList2 first row = " + String.valueOf(linkList2.get(0)), 20, 700, paint_text);
            canvas.drawText("linkList2 second row = " + String.valueOf(linkList2.get(1)), 20, 760, paint_text);
            canvas.drawText("linkList2 third row = " + String.valueOf(linkList2.get(2)), 20, 820, paint_text);

            canvas.drawText("pointListArrowHead = " + String.valueOf(pointListArrowHead), 20, 880, paint_text);
            canvas.drawText("angleListArrowHead = " + String.valueOf(angleListArrowHead), 20, 940, paint_text);
            canvas.drawText("linkList = " + String.valueOf(linkList), 20, 1000, paint_text);
            canvas.drawText("rectListButtons = " + String.valueOf(rectListButtons), 20, 1060, paint_text);

            canvas.drawText("inside_button = " + String.valueOf(inside_button), 600, 100, paint_text);
            canvas.drawText("size of pointlist = " + String.valueOf(pointList.size()), 600, 160, paint_text);
            canvas.drawText("size of pathlist = " + String.valueOf(pathList.size()), 600, 220, paint_text);
            canvas.drawText("size of linklist = " + String.valueOf(linkList.size()), 600, 280, paint_text);
            canvas.drawText("size of pointListArrowHead = " + String.valueOf(pointListArrowHead.size()), 600, 340, paint_text);
            canvas.drawText("size of angleListArrowHead = " + String.valueOf(angleListArrowHead.size()), 600, 400, paint_text);
            canvas.drawText("size of rectListArrowHead = " + String.valueOf(rectListArrowHead.size()), 600, 460, paint_text);
            canvas.drawText("arrow_animated_fraction = " + String.valueOf(arrow_animated_fraction), 600, 520, paint_text);
            canvas.drawText("able_to_click = " + String.valueOf(able_to_click), 600, 580, paint_text);
            canvas.drawText("isMomentList = " + String.valueOf(isMomentList), 600, 640, paint_text);

            canvas.drawText("btn 1, checkMatrix first row = " + String.valueOf(checkMatrix.get(0).get(0)), 20, 1340, paint_text);
            canvas.drawText("btn 1, checkMatrix second row = " + String.valueOf(checkMatrix.get(0).get(1)), 20, 1400, paint_text);
            canvas.drawText("btn 1, checkMatrix third row = " + String.valueOf(checkMatrix.get(0).get(2)), 20, 1460, paint_text);

            canvas.drawText("btn 2, checkMatrix first row = " + String.valueOf(checkMatrix.get(1).get(0)), 20, 1580, paint_text);
            canvas.drawText("btn 2, checkMatrix second row = " + String.valueOf(checkMatrix.get(1).get(1)), 20, 1640, paint_text);
            canvas.drawText("btn 2, checkMatrix third row = " + String.valueOf(checkMatrix.get(1).get(2)), 20, 1700, paint_text);

            canvas.drawText("btn 3, checkMatrix first row = " + String.valueOf(checkMatrix.get(2).get(0)), 20, 1820, paint_text);
            canvas.drawText("btn 3, checkMatrix second row = " + String.valueOf(checkMatrix.get(2).get(1)), 20, 1880, paint_text);
            canvas.drawText("btn 3, checkMatrix third row = " + String.valueOf(checkMatrix.get(2).get(2)), 20, 1940, paint_text);
            //TODO fix this so this last line is still on screen
        }
    }

    // triggers long press
    final Handler handler = new Handler();

    Runnable mLongPressed = new Runnable() {
        public void run() {

            // forces minimum size of moment until touch is moved past mimimum
            bool_moved_past_moment_radius_already = false;

            Toast.makeText(getContext(), "Moment Added", Toast.LENGTH_SHORT).show();

            // TODO add moment on long press
            // remove arrow on long press
            len_btn_to_touch = len_arrow_shaft;
            path_arrow.reset();
            pointListArrowHead.remove(rectListArrowHead_indice);
            angleListArrowHead.remove(rectListArrowHead_indice);
            linkList.remove(rectListArrowHead_indice);
//            isMomentList.remove(rectListArrowHead_indice);

            //TODO 4f4f add get or something
            linkList2.get(0).remove(rectListArrowHead_indice);
            linkList2.get(1).remove(rectListArrowHead_indice);
            linkList2.get(2).remove(rectListArrowHead_indice);
            rectListArrowHead.remove(rectListArrowHead_indice);
            pathList.remove(rectListArrowHead_indice);
            pathListWrong.remove(rectListArrowHead_indice);

            // set booleans to false state
//            clicked_on_button = true;
//            clicked_on_arrow_head = false;
//            inside_button = false;
            invalidate();
            isMomentList.remove(rectListArrowHead_indice);
            isMomentList.add(rectListArrowHead_indice, true);

//            path_moment = new Path();
//            pathListMoments.add(path_moment);

            null_path = new Path();
            pathListWrong.add(null_path);

//            path_moment.reset();
            loc_arrow_point_x = X;
            loc_arrow_point_y = Y;


//            angle = Math.atan2(loc_arrow_point_y - btn_loc_y, loc_arrow_point_x - btn_loc_x);
//            pointListArrowHead.add(new Point(loc_arrow_point_x, loc_arrow_point_y));
//            angleListArrowHead.add(angle);
//            linkList.add(rectList_indice);
//
//            linkList2.get(0).add(null);//add(linkList.get(rectListArrowHead_indice));
//            linkList2.get(1).add(null);
//            // add angle indice to first row in linklist2
//            linkList2.get(2).add(null);
//
//            rectListArrowHead.add(new Rect(loc_arrow_point_x - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_x + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y + ((int) dim_btn_radius + (int) dim_btn_radius_buffer)));

            onActionDown();
            invalidate();

        }
    };

    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();
        X = (int) event.getX();
        Y = (int) event.getY();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                // add false to isMomentList. This is outside onActionDown
                bool_moved_past_moment_radius_already = true;
                onActionDown();
                break;

            case MotionEvent.ACTION_MOVE:
                onActionMove();
                break;

            case MotionEvent.ACTION_UP:
                if (clicked_on_button || clicked_on_arrow_head) {

                    // TODO move this into loop right below. Seems redundant
                    // checks if release is inside button
                    if (rectListButtons.get(linkList.get(rectListArrowHead_indice)).contains(X, Y) && bool_moved_past_moment_radius_already) {
                        inside_button = true;
                    }

                    // break if released inside button
                    if (inside_button && bool_moved_past_moment_radius_already) {
                        path_arrow.reset();

                        // cancel long press handler
                        handler.removeCallbacks(mLongPressed);

                        // remove arrow
                        pointListArrowHead.remove(rectListArrowHead_indice);
                        angleListArrowHead.remove(rectListArrowHead_indice);
                        isMomentList.remove(rectListArrowHead_indice);
                        linkList.remove(rectListArrowHead_indice);

                        linkList2.get(0).remove(rectListArrowHead_indice);
                        linkList2.get(1).remove(rectListArrowHead_indice);
                        linkList2.get(2).remove(rectListArrowHead_indice);

                        rectListArrowHead.remove(rectListArrowHead_indice);
                        pathList.remove(rectListArrowHead_indice);
                        pathListWrong.remove(rectListArrowHead_indice);

                        // reset booleans to false state
                        clicked_on_button = false;
                        clicked_on_arrow_head = false;
                        inside_button = false;
                        invalidate();
                        break;
                    } else  {
                        bool_moved_past_moment_radius_already = true;
                        handler.removeCallbacks(mLongPressed);
                    }

                    path_arrow.reset();
                    loc_arrow_point_x = X;
                    loc_arrow_point_y = Y;

                    // calculates the angle of arrow at release
                    final double angle_start = Math.atan2(loc_arrow_point_y - btn_loc_y, loc_arrow_point_x - btn_loc_x);


                    // snaps arrow to pi/6 (30 degree) increments
                    double angle_dist = Math.abs(angles[0] - angle_start);
                    int idx = 0;
                    for (int c = 1; c < angles.length; c++) {
                        double tmp_angle_dist = Math.abs(angles[c] - angle_start);
                        if (tmp_angle_dist < angle_dist) {
                            idx = c;
                            angle_dist = tmp_angle_dist;
                        }
                    }

                    // calculates angle between released angle and the nearest 30 degree increment
                    angle_difference = angles[idx] - angle_start;

                    // breaks if arrows are being stacked
                    for (Point point5 : pointListArrowHead) {
                        int x_tmp = (int) (len_arrow_shaft * Math.cos(angles[idx]) + btn_loc_x);
                        int y_tmp = (int) (len_arrow_shaft * Math.sin(angles[idx]) + btn_loc_y);
                        if (point5.equals(x_tmp, y_tmp)) {
                            //TODO there is a bug where if you tap right on the tip of a arrow it will remove it
                            Snackbar.make(this, "You can't stack arrows", Snackbar.LENGTH_SHORT).show();
                            path_arrow.reset();

                            // cancel long press handler
                            handler.removeCallbacks(mLongPressed);

                            // remove arrow from all arraylists
                            pointListArrowHead.remove(rectListArrowHead_indice);
                            angleListArrowHead.remove(rectListArrowHead_indice);
                            isMomentList.remove(rectListArrowHead_indice);
                            linkList.remove(rectListArrowHead_indice);

                            linkList2.get(0).remove(rectListArrowHead_indice);
                            linkList2.get(1).remove(rectListArrowHead_indice);
                            linkList2.get(2).remove(rectListArrowHead_indice);

                            rectListArrowHead.remove(rectListArrowHead_indice);
                            pathList.remove(rectListArrowHead_indice);
                            pathListWrong.remove(rectListArrowHead_indice);

                            // reset booleans to false state
                            clicked_on_button = false;
                            clicked_on_arrow_head = false;
                            inside_button = false;
                            invalidate();
                            return true; // breaks out of case switch loop
                        }
                    }

//                    // calculates the length of the arrow shaft upon release
//                    if ( bool_moved_past_moment_radius_already) {
//                        len_arrow_shaft_start = dim_moment_radius;
//                    } else {
                        len_arrow_shaft_start = Math.hypot((loc_arrow_point_x - btn_loc_x), (loc_arrow_point_y - btn_loc_y));
//                    }

                    // animator which decreases in length and angle

                    if (isMomentList.get(rectListArrowHead_indice)) {
                        len_final_anim = dim_moment_radius;
                    } else {
                        len_final_anim = len_arrow_shaft;
                    }

                    final ValueAnimator animator = ValueAnimator.ofFloat((float) len_arrow_shaft_start, (float) len_final_anim);


                    if (isMomentList.get(rectListArrowHead_indice)) {
                        // adds different animation effect to moment
                        animator.setInterpolator(new DecelerateInterpolator());
                        animator.setDuration(time_anim_arrow_dur);
                    } else {
                        // Adds bounce effect if path is arrow
                        animator.setInterpolator(new OvershootInterpolator());
                        animator.setDuration(time_anim_arrow_dur);
                    }

                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator animation) {
                            arrow_animated_fraction = animation.getAnimatedFraction();
                            len_arrow_shaft_current = (Float) animation.getAnimatedValue();
                            angle = angle_start + arrow_animated_fraction * angle_difference;
                            loc_arrow_point_y = (int) (len_arrow_shaft_current * Math.sin(angle) + btn_loc_y);
                            loc_arrow_point_x = (int) (len_arrow_shaft_current * Math.cos(angle) + btn_loc_x);
                            path_arrow.reset();

                            // replace arrow position with new value each iteration
                            pointListArrowHead.set(rectListArrowHead_indice, new Point(loc_arrow_point_x, loc_arrow_point_y));
                            angleListArrowHead.set(rectListArrowHead_indice, angle);

                            rectListArrowHead.set(rectListArrowHead_indice, new Rect(loc_arrow_point_x - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_x + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y + ((int) dim_btn_radius + (int) dim_btn_radius_buffer)));

                            // prevents user from clicking while the arrow is animating. THis eliminates a bug where "ghost arrows" are created when user click rapidly
                            able_to_click = false;

                            if (isMomentList.size() > 0) {
                                if (isMomentList.get(rectListArrowHead_indice)) {
                                    drawMoment();
                                    invalidate();
                                } else {
                                    drawArrow();
                                    invalidate();
                                }
                            }
                        }
                    });

                    // runs at end of arrow animation
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            clicked_on_button = false;
                            clicked_on_arrow_head = false;

                            // allow user to click again
                            able_to_click = true;

                            // checks if arrow is correct
                            checkArrowIsCorrect();
                        }
                    });

                    // starts animator to snap arrow into position
                    animator.start();
//                    invalidate();

                    // TODO remove this if unwanted
                    // prints angle snapped to in degrees to snackbar
//                     All angles are inverted, so this if statement shows 0.0 instead of -0.0
//                    double angle_degrees;
//                    if (angles[idx] == 0.0) {
//                        angle_degrees = Math.round(Math.toDegrees(angles[idx]));
//                    } else {
//                        angle_degrees = Math.round(Math.toDegrees(-angles[idx]));
//                    }
//
//                    Snackbar.make(this, "Created force at " + angle_degrees + "\u00B0", Snackbar.LENGTH_SHORT).show();
                    inside_button = false;
                }
                break;
        }
        return true;
    }

    private void onActionMove() {
        match = false;
        if (clicked_on_button || clicked_on_arrow_head) {
            // checks if touch is inside button
            if (rectListButtons.get(rectList_indice).contains(X, Y)) {
                inside_button = true;
            } else {
                // cancels long press handler if touch is dragged outside box
                handler.removeCallbacks(mLongPressed);
                inside_button = false;
            }

            path_arrow.reset();
            loc_arrow_point_x = X;
            loc_arrow_point_y = Y;

            angle = Math.atan2(loc_arrow_point_y - btn_loc_y, loc_arrow_point_x - btn_loc_x);
            pointListArrowHead.set(rectListArrowHead_indice, new Point(loc_arrow_point_x, loc_arrow_point_y));
            angleListArrowHead.set(rectListArrowHead_indice, angle);

            rectListArrowHead.set(rectListArrowHead_indice, new Rect(loc_arrow_point_x - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_x + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y + ((int) dim_btn_radius + (int) dim_btn_radius_buffer)));

            if (isMomentList.size() > 0) {
                if (isMomentList.get(rectListArrowHead_indice)) {
                    drawMoment();
                    invalidate();
                } else {
                    drawArrow();
                    invalidate();
                }
            }
        }
    }

    private void onActionDown() {
        invalidate();


        // able_to_click is used to eliminate rapid clicks which can cause problems
        if (able_to_click) {

            int k = 0;
            for (Rect rect_tmp1 : rectListButtons) {
                if (rect_tmp1.contains(X, Y)) {
                    // touch is inside button
                    clicked_on_button = true;
                    // touch is NOT on arrow head
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

            // check if arrow head has just been clicked on
            k = 0;
            if (!clicked_on_button) {
                for (Rect rect_tmp2 : rectListArrowHead) {
                    if (rect_tmp2.contains(X, Y)) {
                        // TODO: I belive the next two lines can be removed. Check if they can safely
                        clicked_on_arrow_head = true;
                        clicked_on_button = false;
                        rectListArrowHead_indice = k;
                        btn_loc_x = rectListButtons.get(linkList.get(rectListArrowHead_indice)).centerX();
                        btn_loc_y = rectListButtons.get(linkList.get(rectListArrowHead_indice)).centerY();
                    }
                    k++;
                }
            }

            if (clicked_on_button) {
                path_arrow = new Path();
                pathList.add(path_arrow);

                null_path = new Path();
                pathListWrong.add(null_path);
                path_arrow.reset();
                loc_arrow_point_x = X;
                loc_arrow_point_y = Y;
                angle = Math.atan2(loc_arrow_point_y - btn_loc_y, loc_arrow_point_x - btn_loc_x);
                pointListArrowHead.add(new Point(loc_arrow_point_x, loc_arrow_point_y));
                angleListArrowHead.add(angle);
                if (isMomentList.size() < angleListArrowHead.size()) {
                    isMomentList.add(false);
                }

                // did not click on moment


                linkList.add(rectList_indice);

                linkList2.get(0).add(null);//add(linkList.get(rectListArrowHead_indice));
                linkList2.get(1).add(null);
                // add angle indice to first row in linklist2
                linkList2.get(2).add(null);

                rectListArrowHead.add(new Rect(loc_arrow_point_x - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_x + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y + ((int) dim_btn_radius + (int) dim_btn_radius_buffer)));

                if (isMomentList.size() > 0) {
                    if (isMomentList.get(rectListArrowHead_indice)) {
                        drawMoment();
                        invalidate();
                    } else {
                        drawArrow();
                        invalidate();
                    }
                }

            } else if (clicked_on_arrow_head) {
                path_arrow = new Path();
                pathList.set(rectListArrowHead_indice, path_arrow); // <-- Add this line.
                pathListWrong.set(rectListArrowHead_indice, null_path);

                if (linkList2.get(2).get(rectListArrowHead_indice) != null) {
                    checkMatrix.get(linkList2.get(0).get(rectListArrowHead_indice)).get(linkList2.get(1).get(rectListArrowHead_indice)).set(linkList2.get(2).get(rectListArrowHead_indice), 0.0);


                    // this part works fine
                    linkList2.get(0).set(rectListArrowHead_indice, null);
                    linkList2.get(1).set(rectListArrowHead_indice, null);
                    linkList2.get(2).set(rectListArrowHead_indice, null);
                }

                path_arrow.reset();
                loc_arrow_point_x = X;
                loc_arrow_point_y = Y;

                btn_loc_x = rectListButtons.get(linkList.get(rectListArrowHead_indice)).centerX();
                btn_loc_y = rectListButtons.get(linkList.get(rectListArrowHead_indice)).centerY();

                angle = Math.atan2(loc_arrow_point_y - btn_loc_y, loc_arrow_point_x - btn_loc_x);

                if (isMomentList.size() > 0) {
                    if (isMomentList.get(rectListArrowHead_indice)) {
                        drawMoment();
                        invalidate();
                    } else {
                        drawArrow();
                        invalidate();
                    }
                }


            }
        } else {
            // this is to prevent rapid clicks causing problems
            Snackbar.make(this, "Don't Tap so quickly!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void checkArrowIsCorrect() {
        // checks if arrow placed is in a correct location

        // this if statement changes 2 pi to pi and -pi to pi respectively. This removed "duplicate" angles at the right and left side of the coordinate system while still allowing the arrow to snap to these points from both directions
        if (angle == 2 * pi) {
            angle = 0;
        } else if (angle == -pi) {
            angle = pi;
        }

        int u = 0;
        int btn_chosen = linkList.get(rectListArrowHead_indice);

        invalidate();

        for (Double val_ang_mat : checkMatrix.get(btn_chosen).get(0)) {

            // if not used already
            Double angle_row = checkMatrix.get(btn_chosen).get(0).get(u);
            Double used_row = checkMatrix.get(btn_chosen).get(1).get(u);
            Double force_ang_row = checkMatrix.get(btn_chosen).get(2).get(u);
            Double oppos_allowed_row = checkMatrix.get(btn_chosen).get(3).get(u);

            // run if opposite angle is chosen
            if (oppos_allowed_row == 1.0 || used_row == 0.0) {
                // calculate opposite angle
                if (angle_row < 0.0) {
                    opp_ang = angle_row + pi;
                } else {
                    opp_ang = angle_row - pi;
                }

                // run if opposite angle is chosen
                if (opp_ang == angle) {
                    if (used_row.equals(1.0)) {
                        Snackbar.make(this, "Opposite Already Used", Snackbar.LENGTH_SHORT).show();
                    } else if (used_row.equals(0.0)) {
                        if (opp_ang == angle) {
                            match = true;
                            checkMatrix.get(btn_chosen).get(1).set(u, 1.0); // mark as used
                            linkList2.get(0).set(rectListArrowHead_indice, linkList.get(rectListArrowHead_indice));
                            linkList2.get(1).set(rectListArrowHead_indice, 1);
                            linkList2.get(2).set(rectListArrowHead_indice, u);
                        }
                    }
                }
            }

            if (angle_row.equals(angle)) {
                if (used_row.equals(1.0)) {
                    Snackbar.make(this, "Opposite Already Used", Snackbar.LENGTH_SHORT).show();
                } else if (used_row.equals(0.0)) {
                    if (angle_row.equals(angle)) {
                        match = true;
                        checkMatrix.get(btn_chosen).get(1).set(u, 1.0); // mark as used
                        linkList2.get(0).set(rectListArrowHead_indice, linkList.get(rectListArrowHead_indice));
                        linkList2.get(1).set(rectListArrowHead_indice, 1);
                        linkList2.get(2).set(rectListArrowHead_indice, u);
                    }
                }
            }
            u++;
        }
        showSnackBarArrowPlaced();
    }

    private void showSnackBarArrowPlaced() {
        // overlays gray arrow over red arrow if in correct location
        if (match) {
            pathListWrong.set(rectListArrowHead_indice, path_arrow);
            SpannableStringBuilder snackbarText = new SpannableStringBuilder();
//            snackbarText.append("" + convertRadToDegreeAndInvert(angle) + "\u00B0" + " is a ");
            snackbarText.append("");
            int boldStart = snackbarText.length();
            snackbarText.append("Correct Location");
            snackbarText.setSpan(new ForegroundColorSpan(Color.GREEN), boldStart, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), boldStart, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.append("");

            Snackbar.make(this, snackbarText, Snackbar.LENGTH_SHORT).show();
        } else {
            SpannableStringBuilder snackbarText = new SpannableStringBuilder();
//            snackbarText.append("" + convertRadToDegreeAndInvert(angle) + "\u00B0" + " is a ");
            snackbarText.append("");
            int boldStart = snackbarText.length();
            snackbarText.append("Wrong Location");
            snackbarText.setSpan(new ForegroundColorSpan(Color.YELLOW), boldStart, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), boldStart, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.append(" - Try again");

            Snackbar.make(this, snackbarText, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void drawArrow() {
        // TODO make this modular - inputs = btn_loc_x, btn_loc_y, angle - outputs = path_arrow

        // sets angle of arrow head
        double angle_arrow_head_left = 4 * pi / 3 - angle;
        double angle_arrow_head_right = -pi / 3 - angle;

        // calculates location of points for both sides of arrow head
        float loc_arrow_head_left_x = (float) ((float) len_arrow_head * Math.sin(angle_arrow_head_left) + loc_arrow_point_x);
        float loc_arrow_head_left_y = (float) ((float) len_arrow_head * Math.cos(angle_arrow_head_left) + loc_arrow_point_y);
        float loc_arrow_head_right_x = (float) ((float) len_arrow_head * Math.sin(angle_arrow_head_right) + loc_arrow_point_x);
        float loc_arrow_head_right_y = (float) ((float) len_arrow_head * Math.cos(angle_arrow_head_right) + loc_arrow_point_y);

        // draws arrow shaft
        path_arrow.moveTo(btn_loc_x, btn_loc_y);
        path_arrow.lineTo(loc_arrow_point_x, loc_arrow_point_y);
        path_arrow.moveTo(loc_arrow_point_x, loc_arrow_point_y);

        // draws arrow head
        path_arrow.moveTo(loc_arrow_head_left_x, loc_arrow_head_left_y);
        path_arrow.lineTo(loc_arrow_point_x, loc_arrow_point_y);
        path_arrow.lineTo(loc_arrow_head_right_x, loc_arrow_head_right_y);
    }

    private void drawMoment() {
        Log.d(TAG,"angle = " + angle);
        double angle_deg =  Math.toDegrees(angle);

        // sets angle of arrow head
        path_arrow.moveTo(btn_loc_x, btn_loc_y);

        len_btn_to_touch = Math.hypot(loc_arrow_point_x-btn_loc_x, loc_arrow_point_y-btn_loc_y);


        if (!bool_moved_past_moment_radius_already && len_btn_to_touch <=dim_moment_radius + len_arrow_shaft_spring) {
            len_btn_to_touch = dim_moment_radius + len_arrow_shaft_spring;
        } else {
            bool_moved_past_moment_radius_already = true;
        }

        if (len_btn_to_touch >= dim_moment_radius + len_arrow_shaft_spring){
            len_btn_to_touch = dim_moment_radius + len_arrow_shaft_spring;
        }

        loc_arrow_point_x = (int) (btn_loc_x + len_btn_to_touch * Math.sin(pi/2 + angle));
        loc_arrow_point_y = (int) (btn_loc_y - len_btn_to_touch * Math.cos(pi/2 + angle));

        // pi/17 is used to twist the arrow head such that it looks better



        double angle_arrow_head_right = -pi / 14 + -pi / 3 - (3*pi/2 + angle);
        double angle_arrow_head_left = -pi / 14 + 4 * pi / 3 - (3*pi/2 + angle);

        // calculates location of points for both sides of arrow head
        float loc_arrow_head_left_x = (float) ((float) len_arrow_head * Math.sin(angle_arrow_head_left) + loc_arrow_point_x);
        float loc_arrow_head_left_y = (float) ((float) len_arrow_head * Math.cos(angle_arrow_head_left) + loc_arrow_point_y);
        float loc_arrow_head_right_x = (float) ((float) len_arrow_head * Math.sin(angle_arrow_head_right) + loc_arrow_point_x);
        float loc_arrow_head_right_y = (float) ((float) len_arrow_head * Math.cos(angle_arrow_head_right) + loc_arrow_point_y);

        // draws moment shaft
        oval_moment.set((float) (btn_loc_x - len_btn_to_touch), (float) (btn_loc_y - len_btn_to_touch), (float) (btn_loc_x + len_btn_to_touch), (float) (btn_loc_y + len_btn_to_touch));
        path_arrow.arcTo(oval_moment, (float) angle_deg, (float) 184, true);

        // draws arrow head
        path_arrow.moveTo(loc_arrow_head_left_x, loc_arrow_head_left_y);
        path_arrow.lineTo(loc_arrow_point_x, loc_arrow_point_y);
        path_arrow.lineTo(loc_arrow_head_right_x, loc_arrow_head_right_y);
    }


    // converts dp to pixels
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    // converts screen percentage to pixels
    public Point percentToPx(PointF per) {
        return new Point(Math.round(per.x * viewWidth / 100), Math.round((per.y * viewHeight / 100)));
    }

    // TODO: remove this before release if unused
    // converts pixels to dp
    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    // This method keeps the aspect ration of the view constant. This is necessary since I am placing points on the canvas by percentage of the way across the view
    // code from https://github.com/jesperborgstrup/buzzingandroid/blob/master/src/com/buzzingandroid/ui/ViewAspectRatioMeasurer.java
    // The aspect ratio to be respected by the measurer
    private static final double VIEW_ASPECT_RATIO = .75; // Do not change this or you will have to re place all the points at the corect location!!
    private ViewAspectRatioMeasurer varm = new ViewAspectRatioMeasurer(VIEW_ASPECT_RATIO);


    public double convertRadToDegreeAndInvert(Double angle) {
        // prints angle snapped to in degrees to snackbar
        if (angle == 0.0) {
            // All angles are inverted, so this if statement shows 0.0 instead of -0.0
            return (double) Math.round(Math.toDegrees(angle));
        } else {
            return (double) Math.round(Math.toDegrees(-angle));
        }
    }

    // Snackbar.make(this, "Created force at " + angle_degrees + "\u00B0", Snackbar.LENGTH_SHORT).show();


    void onAnimationEnd(Animator animation) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        varm.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(varm.getMeasuredWidth(), varm.getMeasuredHeight());
    }


//    @Override
//    public boolean equals(Object obj) {
//        return !super.equals(obj);
//    }
//
//    @Override
//    public int hashCode() {
//        return getName().hashCode();
//    }

}