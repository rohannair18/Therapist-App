package com.example.etherealtherapist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {
    Animation anim;
    GifImageView pandasplash;
    Intent Intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pandasplash = findViewById(R.id.pandasplash);
        anim = AnimationUtils.loadAnimation(this,R.anim.transition_anim);
        pandasplash.startAnimation(anim);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                overridePendingTransition(R.anim.fadeinsplash, R.anim.fadeoutsplash);
                finish();
            }
        }, 3000);
    }

}