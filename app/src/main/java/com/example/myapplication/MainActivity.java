package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 5000; // 5 seconds

    private ImageView logoImageView;
    private TextView appNameTextView;
    private TextView taglineTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainActivity.this, TripListActivity.class);
        startActivity(intent);

        finish(); 
    }
}

    private void startAnimations() {
        // Logo fade in and scale animation
        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_logo_animation);
        logoImageView.startAnimation(logoAnimation);

        // App name slide in from top
       Animation titleAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_title_animation);
        appNameTextView.startAnimation(titleAnimation);

        // Tagline fade in with delay
        Animation taglineAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_tagline_animation);
        taglineTextView.startAnimation(taglineAnimation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Clear animations when activity pauses
        if (logoImageView != null) logoImageView.clearAnimation();
        if (appNameTextView != null) appNameTextView.clearAnimation();
        if (taglineTextView != null) taglineTextView.clearAnimation();
    }
}