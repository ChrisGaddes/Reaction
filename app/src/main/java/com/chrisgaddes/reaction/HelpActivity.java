package com.chrisgaddes.reaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rubengees.introduction.IntroductionBuilder;
import com.rubengees.introduction.IntroductionConfiguration;
import com.rubengees.introduction.entity.Slide;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        new IntroductionBuilder(this).withSlides(generateSlides())
                .withOnSlideListener(new IntroductionConfiguration.OnSlideListener() {
                    @Override
                    protected void onSlideInit(int position, TextView title,
                                               ImageView image, TextView description) {
                        switch (position) {
                            case 0:
                                Glide.with(image.getContext()).load(R.drawable.giphy).placeholder(R.drawable.loading_placeholder).into(image);
                                break;
                            case 1:
                                Glide.with(image.getContext()).load(R.drawable.prob1).into(image);
                                break;
                        }
                    }
                }).introduceMyself();
    }


    private List<Slide> generateSlides() {
        List<Slide> result = new ArrayList<>();

        result.add(new Slide().withTitle("Title").withDescription("Description").
                withColorResource(R.color.material_green_700));

        result.add(new Slide().withTitle("Gif").withDescription("This is a Gif")
                .withColorResource(R.color.material_blue_700));

        return result;
    }

}
