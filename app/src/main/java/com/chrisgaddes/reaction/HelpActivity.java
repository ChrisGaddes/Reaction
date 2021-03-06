package com.chrisgaddes.reaction;

import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class HelpActivity extends IntroActivity {

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
                .title("Tutorial")
                .description("Swipe to get started")
                .image(R.drawable.ic_launcher_large)
                .background(R.color.material_light_white)
                .backgroundDark(R.color.material_light_white)
                .layout(R.layout.slide_canteen)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.material_light_white)
                .backgroundDark(R.color.material_light_white)
                .fragment(R.layout.gif_layout_1, R.style.Theme_Intro)
                .build());


        addSlide(new FragmentSlide.Builder()
                .background(R.color.material_light_white)
                .backgroundDark(R.color.material_light_white)
                .fragment(R.layout.gif_layout_2, R.style.Theme_Intro)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("Immediate Feedback")
                .description("Arrows and moments turn from gray to black if placed correctly. Incorrect (gray) arrows must be removed.")
                .image(R.drawable.correct_incorrect)
                .background(R.color.material_light_white)
                .backgroundDark(R.color.material_light_white)
                .layout(R.layout.slide_canteen)
                .build());

        addSlide(new FragmentSlide.Builder()
                .background(R.color.material_light_white)
                .backgroundDark(R.color.material_light_white)
                .fragment(R.layout.gif_layout_3, R.style.Theme_Intro)
                .build());
    }
}