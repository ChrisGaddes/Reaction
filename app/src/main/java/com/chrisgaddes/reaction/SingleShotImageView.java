package com.chrisgaddes.reaction;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by cagad on 9/7/2016.
 */
public class SingleShotImageView extends ImageView {

    public SingleShotImageView(Context context) {
        super(context);
    }

    public SingleShotImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleShotImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
        setBackgroundDrawable(null);
        setImageBitmap(null);
        System.gc();
    }

}