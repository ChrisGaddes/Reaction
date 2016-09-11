package com.chrisgaddes.reaction;

import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class CanteenIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(false);
        setButtonCtaVisible(false);
        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_BACKGROUND);
        TypefaceSpan labelSpan = new TypefaceSpan(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? "sans-serif-medium" : "sans serif");
        SpannableString label = SpannableString.valueOf(getString(R.string.label_button_cta_canteen_intro));
        label.setSpan(labelSpan, 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setButtonCtaLabel(label);

        setPageScrollDuration(500);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setPageScrollInterpolator(android.R.interpolator.fast_out_slow_in);
        }

        addSlide(new SimpleSlide.Builder()
                .title("Welcome to Reaction")
                .description("Swipe to get started")
                .image(R.drawable.ic_launcher_large)
                .background(R.color.color_canteen)
                .backgroundDark(R.color.color_dark_canteen)
                .layout(R.layout.slide_canteen)
                .build());


        addSlide(new SimpleSlide.Builder()
                .title("Mobile")
                .description("Practice Free-body Diagrams on the go!")
                .image(R.drawable.prob1)
                .background(R.color.color_canteen)
                .backgroundDark(R.color.color_dark_canteen)
                .layout(R.layout.slide_canteen)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Touch and drag")
                .description("Tap on appropriate locations and drag outward to create force arrows")
                .image(R.drawable.prob1)
                .background(R.color.color_canteen)
                .backgroundDark(R.color.color_dark_canteen)
                .layout(R.layout.slide_canteen)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Instant Feedback")
                .description("Correctly placed arrows turn from grey to black")
                .image(R.drawable.art_canteen_intro3)
                .background(R.color.color_canteen)
                .backgroundDark(R.color.color_dark_canteen)
                .layout(R.layout.slide_canteen)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Add Forces")
                .description("Swipe and drag to create force arrows")
                .image(R.drawable.art_canteen_intro3)
                .background(R.color.color_canteen)
                .backgroundDark(R.color.color_dark_canteen)
                .layout(R.layout.slide_canteen)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Add Moments")
                .description("Long press to create mohoments")
                .image(R.drawable.art_canteen_intro3)
                .background(R.color.color_canteen)
                .backgroundDark(R.color.color_dark_canteen)
                .layout(R.layout.slide_canteen)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_canteen_intro3)
                .description(R.string.description_canteen_intro3)
                .image(R.drawable.art_canteen_intro3)
                .background(R.color.color_canteen)
                .backgroundDark(R.color.color_dark_canteen)
                .layout(R.layout.slide_canteen)
                .build());

//        autoplay(2500, INFINITE);
    }

}