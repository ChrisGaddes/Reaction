package com.chrisgaddes.reaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.udojava.evalex.Expression;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DrawArrowsView extends ImageView {
    private static final String TAG = "ThirdActivity";

    final FloatingActionButton btn_check_done;
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
    private double len_from_btn_to_touch;
    private double len_arrow_head;
    private double dim_moment_radius;
    private final float dim_btn_radius;
    private final float dim_btn_radius_buffer;
    private long time_anim_arrow_dur;

    private int rectList_indice;
    private int rectListArrowHead_indice;
    private boolean clicked_on_arrow_head;
    private boolean moved_outside_radius_already;

    int eventaction;
    private int i;
    private int k;
    private double opp_ang;
    private double rounded_opp_ang;
    private int offset;
    private float stepAngle;
    private float touchAngle;
    private float deltaAngle;

    private boolean allArrowsCorrect;

    public TinyDB tinydb;

    private int btn_chosen;
    private int u;

    //TODO consider http://stackoverflow.com/questions/32324876/how-to-save-an-answer-in-a-riddle-game-without-creating-a-database

    // initialize ArrayLists for paths and points
    private ArrayList<Point> pointList = new ArrayList<>();
    private List<List<List<Double>>> checkMatrix = new ArrayList<>();
    private ArrayList<Rect> rectListButtons;
    private ArrayList<Rect> rectListArrowHead;
    private ArrayList<Path> pathList;
    private ArrayList<Path> pathListCorrect;
    private ArrayList<Point> pointListArrowHead;
    private ArrayList<Double> angleListArrowHead;
    private ArrayList<Integer> linkList;
    private ArrayList<Boolean> isMomentList;
    private ArrayList<Double> isClockwiseList;
    private List<ArrayList<Integer>> linkList2;

    private RectF oval_moment;

    private Path path_arrow;
    private Path null_path;

    private int x_b;
    private int y_b;

    private Rect rectDone;

    private int X;
    private int Y;
    private int x_tmp;
    private int y_tmp;
    int last_touch_x;
    int last_touch_y;

    private int btn_loc_x;
    private int btn_loc_y;
    private int loc_arrow_point_x;
    private int loc_arrow_point_y;

    private double len_arrow_shaft_current;
    private double angle;
    private double angle_difference;
    private float sweep_angle;
    private double startAngle;
    private boolean used_already2;

    private double arrow_animated_fraction;

    private double angle_arrow_head_right;
    private double angle_arrow_head_left;

    private boolean clicked_on_button;
    private boolean inside_button;
    private boolean able_to_click;
    private boolean SetButtonPoints_RunAlready;
    private boolean match;

    private boolean debuggingTextToggle;
    private boolean moving_clockwise;

    int value;

    private long viewHeight;
    private long viewWidth;
    private Drawable mFocusedImage;
    private Drawable mGrayedImage;

    private int problem_number;
    private String part_letter;

    private String str_angleRow;
    private String str_usedRow;
    private String str_forceAngleRow;
    private String str_oppositeAllowedRow;
    private String str_clockwiseRow;
    private String str_finishedRow;
    private String str_dependencyRow;
    private String str_usedSameOrOpRow;

    private String[] mangleRow;
    private String[] musedRow;
    private String[] mforceAngleRow;
    private String[] moppositeAllowedRow;
    private String[] mclockwiseRow;
    private String[] mfinishedRow;
    private String[] mdependencyRow;
    private String[] musedSameOrOpRow;

    private BigDecimal tmp0;
    private BigDecimal tmp1;
    private BigDecimal tmp2;
    private BigDecimal tmp3;
    private BigDecimal tmp4;
    private BigDecimal tmp5;
    private BigDecimal tmp6;
    private BigDecimal tmp7;

    private Double val0;
    private Double val1;
    private Double val2;
    private Double val3;
    private Double val4;
    private Double val5;
    private Double val6;
    private Double val7;

    /**
     * Description of what this Constructor does/is used for...
     *
     * @param context What is the context argument used for?
     * @param attrs   What is the attrs argument used for?
     */
    public DrawArrowsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        btn_check_done = (FloatingActionButton) findViewById(R.id.btn_check_done);


        // Sets image for problem
//        IV_problem = (ImageView) findViewById(R.id.problem);
//        Glide.with(this)
//                .load(getResources().getIdentifier(str_prob_file_name, "drawable", getPackageName()))
//                .into(mGrayedImage);

        // TODO replace depreciated "getDrawable" with something else
//        mFocusedImage = context.getResources().getDrawable(R.drawable.prob1_parta);
//        mGrayedImage = context.getResources().getDrawable(R.drawable.prob1_parta);

        pointList = new ArrayList<>();
        checkMatrix = new ArrayList<>();
        rectListButtons = new ArrayList<>();
        rectListArrowHead = new ArrayList<>();

        pathList = new ArrayList<>();
        pathListCorrect = new ArrayList<>();

        pointListArrowHead = new ArrayList<>();
        angleListArrowHead = new ArrayList<>();
        linkList = new ArrayList<>();
        isMomentList = new ArrayList<>();
        isClockwiseList = new ArrayList<>();

        // Initializes linkList2 to have three rows
        linkList2 = new ArrayList<>();
        linkList2.add(new ArrayList<Integer>());
        linkList2.add(new ArrayList<Integer>());
        linkList2.add(new ArrayList<Integer>());

        // create new paints
        paint_arrow = new Paint();
        paint_arrow_correct_location = new Paint();
        path_arrow = new Path();
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
        SetButtonPoints_RunAlready = false;

        // sets dimensions of arrow, nodes, and touch areas
        len_arrow_shaft = dpToPx(62);

        // set dimensions of moments
        dim_moment_radius = dpToPx(30);
        len_arrow_shaft_spring = dpToPx(7);
        len_arrow_head = dpToPx(19); //TODO: Consider not using dp on arrows to prevent them from getting stuck if they overlap
        dim_btn_radius = dpToPx(4);
        dim_btn_radius_buffer = dpToPx(19);

        setArrowStyle();
    }

    /**
     * @param context //TODO add what context is used for
     */
    private void setButtonPoints(Context context) {
        // this method sets the location of the points
        // TODO: import these from database

//
//        // set node locations - touch "button" zones will be placed in boxes around these nodes
//        PointF pointOne = new PointF((float) 18, (float) 31.5);
//        PointF pointTwo = new PointF((float) 18, (float) 62.1);
//        PointF pointThree = new PointF((float) 53.5, (float) 62.1);
//        PointF pointFour = new PointF((float) 87.55, (float) 62.1);
//
//        // pointList.add(percentToPx(pointOne));
//        pointList.add(percentToPx(pointTwo));
//        pointList.add(percentToPx(pointThree));
//        pointList.add(percentToPx(pointFour));

        // create Rects from pointList to create buttons at nodes

        loadArrowCheckLocations();
    }


    // TODO put this on a runnable so it doesn't slow down UI thread
    private void loadArrowCheckLocations() {

        // Load problem number and part letter
        problem_number = tinydb.getInt("problem_number");
        part_letter = tinydb.getString("part_letter");

        // Load variable to see if loaded in TinyDB database yet.
//        String str_loadedInDatabaseYet = "loadedInDatabaseYet_" + "prob" + problem_number + "_part" + part_letter;
//        String[] mloadedInDatabaseYet = getResources().getStringArray(getResId(str_loadedInDatabaseYet, R.array.class));
//        int loadedInDatabaseYet = Integer.parseInt(mloadedInDatabaseYet[0]);

        String str_number_of_buttons = "number_of_buttons_" + "prob" + problem_number + "_part" + part_letter;
        String[] mnumberOfButtons = getResources().getStringArray(getResId(str_number_of_buttons, R.array.class));
        int number_of_buttons = Integer.parseInt(mnumberOfButtons[0]);

        for (int btn = 0; btn < number_of_buttons; btn++) {

            // Adds new ArrayLists o checkMatrix
            checkMatrix.add(new ArrayList<List<Double>>());

            checkMatrix.get(btn).add(new ArrayList<Double>());
            checkMatrix.get(btn).add(new ArrayList<Double>());
            checkMatrix.get(btn).add(new ArrayList<Double>());
            checkMatrix.get(btn).add(new ArrayList<Double>());
            checkMatrix.get(btn).add(new ArrayList<Double>());
            checkMatrix.get(btn).add(new ArrayList<Double>());
            checkMatrix.get(btn).add(new ArrayList<Double>());
            checkMatrix.get(btn).add(new ArrayList<Double>());

            // Loads Button locations
            String str_locationButton = "location_" + "prob" + problem_number + "_part" + part_letter + "_" + "btn" + btn;
            String[] mlocationButton = getResources().getStringArray(getResId(str_locationButton, R.array.class));

            // Adds button locations to pointList
            PointF pointToAdd = new PointF(Float.parseFloat(mlocationButton[0]), Float.parseFloat(mlocationButton[1]));
            pointList.add(percentToPx(pointToAdd));

            for (int c = 0; c < 4; c++) { // TODO remove hardcoded 5 here

                str_angleRow = "angleRow_" + "prob" + problem_number + "_part" + part_letter + "_" + "btn" + btn;
                str_usedRow = "usedRow_" + "prob" + problem_number + "_part" + part_letter + "_" + "btn" + btn;
                str_forceAngleRow = "forceAngleRow_" + "prob" + problem_number + "_part" + part_letter + "_" + "btn" + btn;
                str_oppositeAllowedRow = "oppositeAllowedRow_" + "prob" + problem_number + "_part" + part_letter + "_" + "btn" + btn;
                str_clockwiseRow = "clockwiseRow_" + "prob" + problem_number + "_part" + part_letter + "_" + "btn" + btn;
                str_finishedRow = "finishedRow_" + "prob" + problem_number + "_part" + part_letter + "_" + "btn" + btn;
                str_dependencyRow = "dependencyRow_" + "prob" + problem_number + "_part" + part_letter + "_" + "btn" + btn;
                str_usedSameOrOpRow = "usedSameOrOpRow_" + "prob" + problem_number + "_part" + part_letter + "_" + "btn" + btn;

                mangleRow = getResources().getStringArray(getResId(str_angleRow, R.array.class));
                musedRow = getResources().getStringArray(getResId(str_usedRow, R.array.class));
                // mforceAngleRow is defined below
                moppositeAllowedRow = getResources().getStringArray(getResId(str_oppositeAllowedRow, R.array.class));
                mclockwiseRow = getResources().getStringArray(getResId(str_clockwiseRow, R.array.class));
                mfinishedRow = getResources().getStringArray(getResId(str_finishedRow, R.array.class));
                mdependencyRow = getResources().getStringArray(getResId(str_dependencyRow, R.array.class));
                musedSameOrOpRow = getResources().getStringArray(getResId(str_usedSameOrOpRow, R.array.class));

                tmp0 = new Expression(mangleRow[c]).eval();
                tmp1 = new Expression(musedRow[c]).eval();
                // tmp2 is defined below
                tmp3 = new Expression(moppositeAllowedRow[c]).eval();
                tmp4 = new Expression(mclockwiseRow[c]).eval();
                tmp5 = new Expression(mfinishedRow[c]).eval();
                // tmp6 is defined below
                tmp7 = new Expression(musedSameOrOpRow[c]).eval();

                val0 = Double.valueOf(String.valueOf(tmp0));
                val1 = Double.valueOf(String.valueOf(tmp1));
                // val2 is defined below
                val3 = Double.valueOf(String.valueOf(tmp3));
                val4 = Double.valueOf(String.valueOf(tmp4));
                val5 = Double.valueOf(String.valueOf(tmp5));

                val7 = Double.valueOf(String.valueOf(tmp7));

                // Sets forceAngleRow from previous problem
                if (mdependencyRow[c].equals("0.0")) {

                    mforceAngleRow = getResources().getStringArray(getResId(str_forceAngleRow, R.array.class));
                    tmp2 = new Expression(mforceAngleRow[c]).eval();
                    tmp6 = new Expression(mdependencyRow[c]).eval();

                    val2 = Double.valueOf(String.valueOf(tmp2));
                    val6 = Double.valueOf(String.valueOf(tmp6));

                    checkMatrix.get(btn).get(2).add(val2);
                    checkMatrix.get(btn).get(6).add(val6);
                } else {
                    mforceAngleRow[c] = tinydb.getString("usedSameOrOpRow_" + String.valueOf(mdependencyRow[c]));
                    tmp2 = new Expression(mforceAngleRow[c]).eval();
                    val2 = Double.valueOf(String.valueOf(tmp2));

                    // Flips value
                    if (val2.equals(1.0)) {
                        val2 = 2.0;
                    } else if (val2.equals(2.0)) {
                        val2 = 1.0;
                    }


                    val6 = 0.0;
                    checkMatrix.get(btn).get(2).add(val2);
                    checkMatrix.get(btn).get(6).add(val6);
                }


                checkMatrix.get(btn).get(0).add(val0);
                checkMatrix.get(btn).get(1).add(val1);

                checkMatrix.get(btn).get(3).add(val3);
                checkMatrix.get(btn).get(4).add(val4);
                checkMatrix.get(btn).get(5).add(val5);

                checkMatrix.get(btn).get(7).add(val7);

            }
        }

        SetButtonPoints_RunAlready = true;
        match = false;

        for (Point g : pointList) {
            rectListButtons.add(new Rect(g.x - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), g.y - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), g.x + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), g.y + ((int) dim_btn_radius + (int) dim_btn_radius_buffer)));
        }
    }

    /**
     * Describe the setArrowStyle function
     */
    private void setArrowStyle() {

        // gets shared preferences
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());

        // loads tinydb database
        tinydb = new TinyDB(getContext());
//        tinydb.clear(); //TODO

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
        paint_angle_check.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        paint_angle_check.setStyle(Paint.Style.STROKE);
        paint_angle_check.setStrokeCap(Paint.Cap.ROUND);

        // sets style of arrows
        paint_arrow.setStyle(Paint.Style.FILL);
        paint_arrow.setStrokeWidth(dpToPx(4));
        paint_arrow.setColor(Color.BLACK);
        paint_arrow.setAlpha(40);
        paint_arrow.setStyle(Paint.Style.STROKE);
        paint_arrow.setStrokeCap(Paint.Cap.ROUND);

        // sets style of arrow if placed at correct location
        paint_arrow_correct_location.setStyle(Paint.Style.FILL);
        paint_arrow_correct_location.setStrokeWidth(dpToPx(6));
        paint_arrow_correct_location.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
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
            paint_arrow_head_box.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryGreen));
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

        // TODO run this off main thread IMPORTANT
        if (!SetButtonPoints_RunAlready) {
            setButtonPoints(getContext());
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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
//        mGrayedImage.setBounds(imageBounds);
//        mFocusedImage.setBounds(imageBounds);

//        mGrayedImage.draw(canvas);
//        mFocusedImage.draw(canvas);

        // draws rectangles around arrowheads
        for (Rect rect1 : rectListArrowHead) {
            canvas.drawRect(rect1, paint_arrow_head_box);
        }

        x_b = (int) dim_btn_radius * 2;
        y_b = (int) dim_btn_radius * 2;

        // TODO remove in final build
        rectDone = new Rect(x_b - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), y_b - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), x_b + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), y_b + ((int) dim_btn_radius + (int) dim_btn_radius_buffer));

        canvas.drawRect(rectDone, paint_arrow_head_box);

        // draws rectangles around points
        for (Rect rect7 : rectListButtons) {
            canvas.drawRect(rect7, paint_box);
        }

        // draws black circle at points in ArrayList pointList
        for (Point ptLst_dots : pointList) {
            canvas.drawCircle(ptLst_dots.x, ptLst_dots.y, dim_btn_radius, paint_points);
        }

        // draws arrows and moments from pathlist pthLst_arrows
        for (Path pthLst_arrows : pathList) {
            canvas.drawPath(pthLst_arrows, paint_arrow);
        }

        // draws correct arrows from pathlist pthLst_arrows
        for (Path pthLst_arrows : pathListCorrect) {
            canvas.drawPath(pthLst_arrows, paint_arrow_correct_location);
        }

        if (debuggingTextToggle) {
//            canvas.drawText("problem # = " + String.valueOf(hey), 20, 100, paint_text);
//            canvas.drawText("inside_button = " + String.valueOf(inside_button), 20, 100, paint_text);
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
//            canvas.drawText("rectListButtons = " + String.valueOf(rectListButtons), 20, 1060, paint_text);

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
            canvas.drawText("isClockwiseList = " + String.valueOf(isClockwiseList), 600, 700, paint_text);
            canvas.drawText("angle = " + String.valueOf(angle), 600, 760, paint_text);
            canvas.drawText("startAngle = " + String.valueOf(startAngle), 600, 820, paint_text);
            canvas.drawText("touchAngle = " + String.valueOf(touchAngle), 600, 880, paint_text);
            canvas.drawText("deltaAngle = " + String.valueOf(deltaAngle), 600, 940, paint_text);
            canvas.drawText("stepAngle = " + String.valueOf(stepAngle), 1100, 820, paint_text);
            canvas.drawText("offset = " + String.valueOf(offset), 1100, 880, paint_text);
            canvas.drawText("value = " + String.valueOf(value), 1100, 940, paint_text);
            canvas.drawText("allArrowsCorrect = " + String.valueOf(allArrowsCorrect), 1100, 1000, paint_text);

            canvas.drawText("used_already2 = " + String.valueOf(used_already2), 600, 1120, paint_text);


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

//            boolean moment_already_used = false;
//            for (int v = 1; v < isMomentList; v++) {
//                if (isMomentList.get(v)){
//                    moment_already_used = true;
//                }
//            }

            // breaks if moment has already been used
//            if (!moment_already_used) {
            //sets clockwise to true
            moving_clockwise = true;

            // first loop in drawMoment has not occured yet for this particular moment
            used_already2 = false;

            // forces minimum size of moment until touch is moved past mimimum
            moved_outside_radius_already = false;

//            Snackbar.make(getContext(), "Moment Added", Snackbar.LENGTH_SHORT).show();
//            Toast.makeText(getContext(), "Moment Added", Toast.LENGTH_SHORT).show();

            // remove arrow on long press
            len_from_btn_to_touch = len_arrow_shaft;

            removeValuesFromArraylists(rectListArrowHead_indice);

            isMomentList.add(rectListArrowHead_indice, true);


            isClockwiseList.add(rectListArrowHead_indice, 200.0);
            null_path = new Path();
            pathListCorrect.add(null_path);

            //path_moment.reset();
            loc_arrow_point_x = X;
            loc_arrow_point_y = Y;

            onActionDown();
            invalidate();
//            } else {
//
//            }
        }
    };

    void removeValuesFromArraylists(int mRectListArrowHead_indice) {
        // removes values from Arraylists
        path_arrow.reset();
        angleListArrowHead.remove(mRectListArrowHead_indice);
        pointListArrowHead.remove(mRectListArrowHead_indice);
        isMomentList.remove(mRectListArrowHead_indice);
        isClockwiseList.remove(mRectListArrowHead_indice);
        linkList.remove(mRectListArrowHead_indice);

        linkList2.get(0).remove(mRectListArrowHead_indice);
        linkList2.get(1).remove(mRectListArrowHead_indice);
        linkList2.get(2).remove(mRectListArrowHead_indice);

        rectListArrowHead.remove(mRectListArrowHead_indice);
        pathList.remove(mRectListArrowHead_indice);
        pathListCorrect.remove(mRectListArrowHead_indice);
    }

    public boolean onTouchEvent(MotionEvent event) {
        eventaction = event.getAction();
        X = (int) event.getX();
        Y = (int) event.getY();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                moved_outside_radius_already = true;
                onActionDown();
                break;

            case MotionEvent.ACTION_MOVE:
                onActionMove();
                break;

            case MotionEvent.ACTION_UP:

                if (clicked_on_button || clicked_on_arrow_head) {

                    // Checks all arrows that are currently placed
//                    checkAllArrows();

                    // TODO move this into loop right below. Seems redundant
                    // checks if release is inside button
                    if (rectListButtons.get(linkList.get(rectListArrowHead_indice)).contains(X, Y) && moved_outside_radius_already) {
                        inside_button = true;
                    }

                    // break if released inside button
                    if (inside_button && moved_outside_radius_already) {

                        // cancel long press handler
                        handler.removeCallbacks(mLongPressed);

                        removeValuesFromArraylists(rectListArrowHead_indice);

                        // reset booleans to false state
                        clicked_on_button = false;
                        clicked_on_arrow_head = false;
                        inside_button = false;
                        invalidate();
                        break;
                    } else {
                        moved_outside_radius_already = true;
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

                    // Gets locations to check if arrows are stacked on another
                    if (isMomentList.size() > 0) {
                        if (isMomentList.get(rectListArrowHead_indice)) {
                            // gets coordinates of
                            x_tmp = (int) (dim_moment_radius * Math.cos(angles[idx]) + btn_loc_x);
                            y_tmp = (int) (dim_moment_radius * Math.sin(angles[idx]) + btn_loc_y);

                        } else {
                            x_tmp = (int) (len_arrow_shaft * Math.cos(angles[idx]) + btn_loc_x);
                            y_tmp = (int) (len_arrow_shaft * Math.sin(angles[idx]) + btn_loc_y);
                        }
                    }

                    // checks if arrow is stacked on another
                    for (Point point5 : pointListArrowHead) {
                        if (point5.equals(x_tmp, y_tmp)) {
                            if (last_touch_x == x_tmp && last_touch_y == y_tmp) {
                                // Tapped on arrow head (Released over same button that was touched)
                                // This is not a reliable way to detect arrow head taps
                                Toast.makeText(getContext(), "Tapped on Arrow Head", Toast.LENGTH_SHORT).show();
                            } else {
                                // Have not tapped on arrow head
                                Snackbar.make(this, "You can't stack arrows", Snackbar.LENGTH_SHORT).show();

                                // cancel long press handler
                                handler.removeCallbacks(mLongPressed);

                                // removes values from ArrayLists
                                removeValuesFromArraylists(rectListArrowHead_indice);

                                // reset booleans to false state
                                clicked_on_button = false;
                                clicked_on_arrow_head = false;
                                inside_button = false;
                                invalidate();
                                return true; // breaks out of case switch loop
                            }
                        }
                    }

                    // sets different starting and ending values for moments and forces
                    double len_arrow_shaft_start;
                    double len_final_anim;
                    if (isMomentList.get(rectListArrowHead_indice)) {
                        // sets animation start and end values for moments
                        len_arrow_shaft_start = dim_moment_radius;
                        len_final_anim = dim_moment_radius;
                    } else {
                        // sets animation start and end values for force arrows
                        len_arrow_shaft_start = Math.hypot((loc_arrow_point_x - btn_loc_x), (loc_arrow_point_y - btn_loc_y));
                        len_final_anim = len_arrow_shaft;
                    }

                    // animator which decreases in length and angle
                    final ValueAnimator animator = ValueAnimator.ofFloat((float) len_arrow_shaft_start, (float) len_final_anim);
                    // determines which path type (forces or moment) has just been drawn
                    if (isMomentList.get(rectListArrowHead_indice)) {
                        // adds smooth animation effect if path is moment
                        animator.setInterpolator(new DecelerateInterpolator());
                        animator.setDuration(time_anim_arrow_dur);
                    } else {
                        // Adds bounce effect if path is a force arrow
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

                            // replace force arrow or moment position with new value each iteration
                            pointListArrowHead.set(rectListArrowHead_indice, new Point(loc_arrow_point_x, loc_arrow_point_y));
                            angleListArrowHead.set(rectListArrowHead_indice, angle);

                            rectListArrowHead.set(rectListArrowHead_indice, new Rect(loc_arrow_point_x - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y - ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_x + ((int) dim_btn_radius + (int) dim_btn_radius_buffer), loc_arrow_point_y + ((int) dim_btn_radius + (int) dim_btn_radius_buffer)));

                            // prevents user from clicking while the force arrow or moment is animating. This eliminates a bug where "ghost arrows" are created when user click rapidly
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

                            // checks if force arrow is correct

                            // TODO fix it so it can tell which arrow was just placed
//                            setAllToUnused();
//                            checkArrowIsCorrect(rectListArrowHead_indice);

                            checkAllArrows();

                            showSnackBarArrowPlaced(rectListArrowHead_indice);
                        }
                    });

                    // starts animator to snap arrow into position
                    animator.start();

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

    public boolean runCheckIfFinished() {
        checkAllArrows();

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

        if (allArrowsCorrect = checkIfFinished()) {

            // Writes SameOrOpRow non-zero values to a tinyDB database
            writeSameOrOpRowToDatabase();

//            Snackbar.make(this, "Finished!", Snackbar.LENGTH_SHORT).show();
//            btn_check_done.setBackgroundTintList(myList);
//            showDialogArrowsCorrect();
            return true;
        } else {
            Snackbar.make(this, "Not Finished Yet...", Snackbar.LENGTH_SHORT).show();
            return false;
//            invalidate();

        }
    }

//    // dialog that says arrows were placed correctly
//    private boolean showDialogArrowsCorrect() {
//        new MaterialStyledDialog(getContext())
//                .setTitle("Correct!")
//                .setDescription(R.string.str_all_arrows_placed_correctly)
//                .setIcon(R.drawable.ic_check)
//                .setStyle(Style.HEADER_WITH_ICON)
//
//                .setPositive(getResources().getString(R.string.str_next_problem), new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog dialog, DialogAction which) {
//                        Log.d("MaterialStyledDialogs", "Do something!");
//                        tinydb.putString("part_letter", "B");
//                        Context context = getContext();
//                        Intent i = new Intent(context, ThirdActivity.class);
//                        context.startActivity(i);
//                    }
//                })
//
//                .show();
//    }


    public boolean checkIfFinished() {
        // checks if all arrows have been placed in the correct locations

        // resets AllArrowsCorrect boolean
        boolean allArrowsCorrect = true;

        int number_arrows = 0;
        for (int btn = 0; btn < rectListButtons.size(); btn++) {
            for (int ang = 0; ang < 4; ang++) { // 4 is the number of correct arrows or moments allowed on a point

                if (checkMatrix.get(btn).get(5).get(ang).equals(0.0) && !checkMatrix.get(btn).get(0).get(ang).equals(100.0)) {
                    allArrowsCorrect = false;
                }

                if (checkMatrix.get(btn).get(1).get(ang).equals(1.0)) {
                    number_arrows++;
                }
            }
        }

        allArrowsCorrect = allArrowsCorrect && number_arrows == pointListArrowHead.size();

        return allArrowsCorrect;
    }

    public void message() {
        System.out.println("Test");
    }


    private void onActionDown() {
        invalidate();

        if (rectDone.contains(X, Y)) {
            // Checks all arrows that are currently placed
//            checkAllArrows();
//            if (allArrowsCorrect = checkIfFinished()) {
//                Snackbar.make(this, "Finished!", Snackbar.LENGTH_SHORT).show();
//            } else {
            Snackbar.make(this, "Not Finished Yet...", Snackbar.LENGTH_SHORT).show();
//            }
//
//            new MaterialIntroView.Builder(this)
//                    .enableDotAnimation(true)
//                    .enableIcon(false)
//                    .setFocusGravity(FocusGravity.CENTER)
//                    .setFocusType(Focus.MINIMUM)
//                    .setDelayMillis(500)
//                    .enableFadeAnimation(true)
//                    .performClick(true)
//                    .setInfoText("Hi There! Click this card and see what happens.")
//                    .setTarget(view)
//                    .setUsageId("intro_card") //THIS SHOULD BE UNIQUE ID
//                    .show();


        }

        // able_to_click is used to eliminate rapid clicks which can cause problems
        if (able_to_click) {
            k = 0;
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
                        clicked_on_arrow_head = true;
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
                pathListCorrect.add(null_path);
                path_arrow.reset();
                loc_arrow_point_x = X;
                loc_arrow_point_y = Y;
                angle = Math.atan2(loc_arrow_point_y - btn_loc_y, loc_arrow_point_x - btn_loc_x);
                pointListArrowHead.add(new Point(loc_arrow_point_x, loc_arrow_point_y));
                angleListArrowHead.add(angle);
                if (isMomentList.size() < angleListArrowHead.size()) {
                    // is not a moment, so add false
                    isMomentList.add(false);
                    // is not a moment, so add null
                    isClockwiseList.add(null);
                }

                linkList.add(rectList_indice);
                linkList2.get(0).add(null);
                linkList2.get(1).add(null);
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

                // Checks all arrows

                path_arrow = new Path();
                pathList.set(rectListArrowHead_indice, path_arrow); // <-- Add this line.
                pathListCorrect.set(rectListArrowHead_indice, null_path);

                if (linkList2.get(2).get(rectListArrowHead_indice) != null) {
                    checkMatrix.get(linkList2.get(0).get(rectListArrowHead_indice)).get(linkList2.get(1).get(rectListArrowHead_indice)).set(linkList2.get(2).get(rectListArrowHead_indice), 0.0);

                    linkList2.get(0).set(rectListArrowHead_indice, null);
                    linkList2.get(1).set(rectListArrowHead_indice, null);
                    linkList2.get(2).set(rectListArrowHead_indice, null);
                }

                path_arrow.reset();
                loc_arrow_point_x = X;
                loc_arrow_point_y = Y;

                btn_loc_x = rectListButtons.get(linkList.get(rectListArrowHead_indice)).centerX();
                btn_loc_y = rectListButtons.get(linkList.get(rectListArrowHead_indice)).centerY();

                last_touch_x = rectListArrowHead.get(rectListArrowHead_indice).centerX();
                last_touch_y = rectListArrowHead.get(rectListArrowHead_indice).centerY();

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

    private void checkAllArrows() {
        setAllToUnused();
        for (int k = 0; k < linkList.size(); k++) {
            checkArrowIsCorrect(k);
        }
    }

    private void writeSameOrOpRowToDatabase() {
        for (int btn_chosen = 0; btn_chosen < pointList.size(); btn_chosen++) {
            for (int k = 0; k < checkMatrix.get(btn_chosen).get(7).size(); k++)
                if (String.valueOf(checkMatrix.get(btn_chosen).get(7).get(k)).equals("0.0")) {

                } else {
                    str_usedSameOrOpRow = "usedSameOrOpRow_" + "prob" + problem_number + "_part" + part_letter + "_" + "btn" + btn_chosen + "_row" + k;
                    tinydb.putString(str_usedSameOrOpRow, String.valueOf(checkMatrix.get(btn_chosen).get(7).get(k)));
                }
        }
    }


    private void showSnackBarArrowPlaced(int mrectListArrowHead_indice) {
        // overlays gray arrow over red arrow if in correct location
        if (match) {
            SpannableStringBuilder snackbarText = new SpannableStringBuilder();
//            snackbarText.append("" + convertRadToDegreeAndInvert(angle) + "\u00B0" + " is a ");
            snackbarText.append("");
            int boldStart = snackbarText.length();
            snackbarText.append("Correct Direction");
            snackbarText.setSpan(new ForegroundColorSpan(Color.WHITE), boldStart, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), boldStart, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.append("");
//            Snackbar.make(this, snackbarText, Snackbar.LENGTH_SHORT).show();
        } else {
            SpannableStringBuilder snackbarText = new SpannableStringBuilder();
//            snackbarText.append("" + convertRadToDegreeAndInvert(angle) + "\u00B0" + " is a ");
            snackbarText.append("");
            int boldStart = snackbarText.length();
            snackbarText.append("Wrong Direction");
            snackbarText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorAccent)), boldStart, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), boldStart, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.append(" - Try again");
//            Snackbar.make(this, snackbarText, Snackbar.LENGTH_SHORT).show();
        }
    }


    private void drawArrow() {
        // TODO make this modular - inputs = btn_loc_x, btn_loc_y, angle - outputs = path_arrow

        // sets angle of arrow head
        angle_arrow_head_left = 4 * pi / 3 - angle;
        angle_arrow_head_right = -pi / 3 - angle;

        // calculates location of points for both sides of arrow head
        float loc_arrow_head_left_x = (float) ((float) len_arrow_head * Math.sin(angle_arrow_head_left) + loc_arrow_point_x);
        float loc_arrow_head_left_y = (float) ((float) len_arrow_head * Math.cos(angle_arrow_head_left) + loc_arrow_point_y);
        float loc_arrow_head_right_x = (float) ((float) len_arrow_head * Math.sin(angle_arrow_head_right) + loc_arrow_point_x);
        float loc_arrow_head_right_y = (float) ((float) len_arrow_head * Math.cos(angle_arrow_head_right) + loc_arrow_point_y);

        // draws arrow head
        path_arrow.moveTo(loc_arrow_head_left_x, loc_arrow_head_left_y);
        path_arrow.lineTo(loc_arrow_point_x, loc_arrow_point_y);
        path_arrow.lineTo(loc_arrow_head_right_x, loc_arrow_head_right_y);

        // draws arrow shaft
        path_arrow.moveTo(btn_loc_x, btn_loc_y);
        path_arrow.lineTo(loc_arrow_point_x, loc_arrow_point_y);
        path_arrow.moveTo(loc_arrow_point_x, loc_arrow_point_y);
    }

    private void drawMoment() {
        // get angle in degrees
        double angle_deg = Math.toDegrees(angle);

        if (!used_already2) {
            startAngle = 0;
            // specifies the increment the moment flips on
            stepAngle = 150;
            value = 0;
            offset = 0;
        }

        touchAngle = (float) angle_deg;

        deltaAngle = (float) ((360 + touchAngle - startAngle + 180) % 360 - 180);
        if (Math.abs(deltaAngle) > stepAngle) {
            offset = (int) deltaAngle / (int) stepAngle;
            startAngle = touchAngle;
            value += offset;

            if (offset == 1) {
                moving_clockwise = true;
                isClockwiseList.set(rectListArrowHead_indice, 200.0);
                if (i != offset) {
//                    Snackbar.make(this, "Moment was flipped", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                moving_clockwise = false;
                isClockwiseList.set(rectListArrowHead_indice, 300.0);
                if (i != offset) {
//                    Snackbar.make(this, "Moment was flipped", Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        i = offset;

        used_already2 = true;

        // calculates the length from the center of the button which was touched to the current touch location of your finger
        len_from_btn_to_touch = Math.hypot(loc_arrow_point_x - btn_loc_x, loc_arrow_point_y - btn_loc_y);

        // checks if moved outside radius yet
        if (!moved_outside_radius_already && len_from_btn_to_touch <= dim_moment_radius + len_arrow_shaft_spring) {
            len_from_btn_to_touch = dim_moment_radius + len_arrow_shaft_spring;
        } else {
            moved_outside_radius_already = true;
        }

        // limits radius of moment
        if (len_from_btn_to_touch >= dim_moment_radius + len_arrow_shaft_spring) {
            len_from_btn_to_touch = dim_moment_radius + len_arrow_shaft_spring;
        }

        if (isClockwiseList.get(rectListArrowHead_indice).equals(200.0)) {
            //counter clockwise
            sweep_angle = -180;
//             this moves arrow head to other side. Once released, shift touch point to opposite side
            loc_arrow_point_x = (int) (btn_loc_x + len_from_btn_to_touch * Math.sin(pi / 2 + angle));
            loc_arrow_point_y = (int) (btn_loc_y - len_from_btn_to_touch * Math.cos(pi / 2 + angle));
            // pi/14 is used to twist the arrow head such that it looks better
            angle_arrow_head_right = +pi / 14 + -pi / 3 - (pi / 2 + angle);
            angle_arrow_head_left = +pi / 14 + 4 * pi / 3 - (pi / 2 + angle);
        } else {
            // clockwise
            sweep_angle = 180;
            loc_arrow_point_x = (int) (btn_loc_x + len_from_btn_to_touch * Math.sin(pi / 2 + angle));
            loc_arrow_point_y = (int) (btn_loc_y - len_from_btn_to_touch * Math.cos(pi / 2 + angle));
            // pi/14 is used to twist the arrow head such that it looks better
            angle_arrow_head_right = -pi / 14 + -pi / 3 - (3 * pi / 2 + angle);
            angle_arrow_head_left = -pi / 14 + 4 * pi / 3 - (3 * pi / 2 + angle);
        }

        // calculates location of points for both sides of arrow head
        float loc_arrow_head_left_x = (float) ((float) len_arrow_head * Math.sin(angle_arrow_head_left) + loc_arrow_point_x);
        float loc_arrow_head_left_y = (float) ((float) len_arrow_head * Math.cos(angle_arrow_head_left) + loc_arrow_point_y);
        float loc_arrow_head_right_x = (float) ((float) len_arrow_head * Math.sin(angle_arrow_head_right) + loc_arrow_point_x);
        float loc_arrow_head_right_y = (float) ((float) len_arrow_head * Math.cos(angle_arrow_head_right) + loc_arrow_point_y);

        // draws arrow head
        path_arrow.moveTo(loc_arrow_head_left_x, loc_arrow_head_left_y);
        path_arrow.lineTo(loc_arrow_point_x, loc_arrow_point_y);
        path_arrow.lineTo(loc_arrow_head_right_x, loc_arrow_head_right_y);

        // draws moment shaft
        oval_moment.set((float) (btn_loc_x - len_from_btn_to_touch), (float) (btn_loc_y - len_from_btn_to_touch), (float) (btn_loc_x + len_from_btn_to_touch), (float) (btn_loc_y + len_from_btn_to_touch));
        path_arrow.arcTo(oval_moment, (float) angle_deg, sweep_angle, true);
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
    private static final double VIEW_ASPECT_RATIO = 1; // Do not change this or you will have to re place all the points at the corect location!!
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        varm.measure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(varm.getMeasuredWidth(), varm.getMeasuredHeight());
    }

    private float touchAngle(float touchX, float touchY) {
        float dX = touchX - btn_loc_x;
        float dY = btn_loc_y - touchY;
        return (float) (270 - Math.toDegrees(Math.atan2(dY, dX))) % 360 - 180;
    }

    private void checkArrowIsCorrect(int mrectListArrowHead_indice) {
        double mAngle = angleListArrowHead.get(mrectListArrowHead_indice);

        //reset match variable
        match = false;

        // checks if arrow placed is in a correct location
        // this if statement changes 2 pi to pi and -pi to pi respectively. This removed "duplicate" angles at the right and left side of the coordinate system while still allowing the arrow to snap to these points from both directions
        if (mAngle == 2 * pi) {
            mAngle = 0;
        } else if (mAngle == -pi) {
            mAngle = pi;
        }

        btn_chosen = linkList.get(mrectListArrowHead_indice);

        invalidate();

        int u = 0;
        // iterates through the list of correct angles at the button btn_chosen
        for (Double r : checkMatrix.get(btn_chosen).get(0)) {
            // if not used already
            Double angle_row = checkMatrix.get(btn_chosen).get(0).get(u);
            Double used_row = checkMatrix.get(btn_chosen).get(1).get(u);
            Double force_ang_row = checkMatrix.get(btn_chosen).get(2).get(u); // forces opposite or not. If 0.0, no forcing. If 1.0, angle is forced. If 2.0, opoposite angle is forced
            Double oppos_allowed_row = checkMatrix.get(btn_chosen).get(3).get(u);
            Double clockwise_row = checkMatrix.get(btn_chosen).get(4).get(u);
            Double finished_row = checkMatrix.get(btn_chosen).get(5).get(u);
            Double used_same_or_op_row = checkMatrix.get(btn_chosen).get(7).get(u);
//            Double moment_used_row = checkMatrix.get(btn_chosen).get(6).get(u);


            if (isMomentList.get(mrectListArrowHead_indice)) {
                // sets match to true if

                //resets opp_angle
                opp_ang = 100.0;

                if (checkMatrix.get(btn_chosen).get(3).get(u) == 1.0 && checkMatrix.get(btn_chosen).get(1).get(u) == 0.0) {
                    // calculate opposite angle
                    if (checkMatrix.get(btn_chosen).get(0).get(u) == 200.0) { // TODO changed from angle row
                        opp_ang = 300.0;

                        // if angle row equals 300.0
                    } else if (checkMatrix.get(btn_chosen).get(0).get(u) == 300.0) {
                        opp_ang = 200.0;
                    }
                }

                if (isClockwiseList.get(mrectListArrowHead_indice).equals(checkMatrix.get(btn_chosen).get(0).get(u))) {
                    match = true;
                    checkMatrix.get(btn_chosen).get(5).set(u, 1.0);
                    checkMatrix.get(btn_chosen).get(1).set(u, 1.0);

                    //sets usedSameOrOp to same
                    checkMatrix.get(btn_chosen).get(7).set(u, 1.0);

                } else if (isClockwiseList.get(mrectListArrowHead_indice).equals(opp_ang)) {
                    match = true;
                    checkMatrix.get(btn_chosen).get(5).set(u, 1.0);
                    checkMatrix.get(btn_chosen).get(1).set(u, 1.0);

                    //sets usedSameOrOp to Op
                    checkMatrix.get(btn_chosen).get(7).set(u, 2.0);
                }
                u++;

            } else {

                //resets opp_angle
                opp_ang = 100.0;
                rounded_opp_ang = 100.0;

                // checks if opposite allowed row and used row
                if (checkMatrix.get(btn_chosen).get(3).get(u) == 1.0 && checkMatrix.get(btn_chosen).get(1).get(u) == 0.0) {
                    // calculate opposite angle
                    if (checkMatrix.get(btn_chosen).get(0).get(u) <= 0.0) { // TODO I changed this to or equals from just less than. If this breaks things then remove it
                        opp_ang = checkMatrix.get(btn_chosen).get(0).get(u) + pi;
                        rounded_opp_ang = ((double) (Math.round(opp_ang * 1000)) / 1000);
                    } else {
                        opp_ang = checkMatrix.get(btn_chosen).get(0).get(u) - pi;
                        rounded_opp_ang = ((double) (Math.round(opp_ang * 1000)) / 1000);
                    }
                }

                if (checkMatrix.get(btn_chosen).get(0).get(u).equals(100.0)) {
                    // set finished row
                    checkMatrix.get(btn_chosen).get(5).set(u, 1.0);
                }

//                double rounded_angle_row = ((double) (Math.round(angle_row * 1000)) / 1000);
//                double rounded_mAngle = ((double) (Math.round(mAngle * 1000)) / 1000);

                // checks if force angle row equals 1.0 or 2.0
                if (checkMatrix.get(btn_chosen).get(2).get(u) == 0.0 || checkMatrix.get(btn_chosen).get(2).get(u) == 1.0) {

                    if (((double) (Math.round(checkMatrix.get(btn_chosen).get(0).get(u) * 1000)) / 1000) == ((double) (Math.round(mAngle * 1000)) / 1000)) {
                        // set finished row
                        checkMatrix.get(btn_chosen).get(5).set(u, 1.0);


                        // if used_row equals 1.0
                        if (checkMatrix.get(btn_chosen).get(1).get(u).equals(1.0)) {

                            // TODO figure out why this fires every time
//                            Snackbar.make(this, "Opposite Already Used", Snackbar.LENGTH_SHORT).show();
                        } else if (checkMatrix.get(btn_chosen).get(1).get(u).equals(0.0)) {

//                        rounded_angle_row = ((double) (Math.round(angle_row * 1000)) / 1000);
//                        rounded_mAngle = ((double) (Math.round(mAngle * 1000)) / 1000);

                            if (((double) (Math.round(checkMatrix.get(btn_chosen).get(0).get(u) * 1000)) / 1000) == ((double) (Math.round(mAngle * 1000)) / 1000)) {
                                match = true;
                                checkMatrix.get(btn_chosen).get(1).set(u, 1.0); // mark as used

                                //sets usedSameOrOp to same
                                checkMatrix.get(btn_chosen).get(7).set(u, 1.0);
                                linkList2.get(0).set(mrectListArrowHead_indice, linkList.get(mrectListArrowHead_indice));
                                linkList2.get(1).set(mrectListArrowHead_indice, 1);
                                linkList2.get(2).set(mrectListArrowHead_indice, u);
                            }
                        }
                    }
                }

                // checks if force angle row equals 0.0 or 2.0
                if (checkMatrix.get(btn_chosen).get(2).get(u) == 0.0 || checkMatrix.get(btn_chosen).get(2).get(u) == 2.0) {
                    if (rounded_opp_ang == ((double) (Math.round(mAngle * 1000)) / 1000)) {
                        // run if opposite angle is chosen

                        // set finished row
                        checkMatrix.get(btn_chosen).get(5).set(u, 1.0);
                        if (checkMatrix.get(btn_chosen).get(1).get(u).equals(1.0)) {
                            Snackbar.make(this, "Opposite Already Used", Snackbar.LENGTH_SHORT).show();
                        } else if (checkMatrix.get(btn_chosen).get(1).get(u).equals(0.0)) {
                            if (((double) (Math.round(opp_ang * 1000)) / 1000) == ((double) (Math.round(mAngle * 1000)) / 1000)) {
                                match = true;
                                checkMatrix.get(btn_chosen).get(1).set(u, 1.0); // mark as used

                                //sets usedSameOrOp to Op
                                checkMatrix.get(btn_chosen).get(7).set(u, 2.0);

                                linkList2.get(0).set(mrectListArrowHead_indice, linkList.get(mrectListArrowHead_indice));
                                linkList2.get(1).set(mrectListArrowHead_indice, 1);
                                linkList2.get(2).set(mrectListArrowHead_indice, u);
                            }
                        }
                    }
                }
                u++;
            }
        }


        if (match) {
            pathListCorrect.set(mrectListArrowHead_indice, pathList.get(mrectListArrowHead_indice));

        } else {
            pathListCorrect.set(mrectListArrowHead_indice, null_path);
        }
    }

    private void setAllToUnused() {
        for (int btn = 0; btn < checkMatrix.size(); btn++) {
            for (int ang = 0; ang < checkMatrix.get(btn).get(0).size(); ang++) {
                // resets used_row to all zeros
                checkMatrix.get(btn).get(1).set(ang, 0.0);

                //resets finished row to all zeros
                checkMatrix.get(btn).get(5).set(ang, 0.0);
            }
        }
    }


    public void resetAllValues() {
        setAllToUnused();

        // resets variables to zero (this may not be necessary)
        value = 0;
        offset = 0;
        deltaAngle = 0;
        touchAngle = 0;

        int size = pointListArrowHead.size();

        for (int loop_indice = 0; loop_indice < size; loop_indice++) {
            if (pointListArrowHead.size() > 0) {
                removeValuesFromArraylists(pointListArrowHead.size() - 1);
            }
        }
        invalidate();
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


}