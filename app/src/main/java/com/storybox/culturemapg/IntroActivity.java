package com.storybox.culturemapg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by sunghee on 2016-10-31.
 */

public class IntroActivity extends AppCompatActivity {
    Animation translateAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ImageView anim_logo_g = (ImageView)findViewById(R.id.anim_logo_g);
        translateAnim = AnimationUtils.loadAnimation(this, R.anim.intro_animation);

        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        translateAnim.setAnimationListener(animationListener);

        anim_logo_g.startAnimation(translateAnim);
    }
}
