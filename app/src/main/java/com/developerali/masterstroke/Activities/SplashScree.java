package com.developerali.masterstroke.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.MainActivity;
import com.developerali.masterstroke.databinding.ActivitySplashScreeBinding;

public class SplashScree extends AppCompatActivity {

    ActivitySplashScreeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Helper.SPLASH_LINK != null && !Helper.SPLASH_LINK.isEmpty()){
            Glide.with(SplashScree.this)
                    .load(Helper.SPLASH_LINK)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.discoverProfileImage);
        }

        if (Helper.CANDIDATE != null){
            binding.candidateName.setText(Helper.CANDIDATE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScree.this, MainActivity.class));
                finish();
            }
        }, 2500);



    }

    @Override
    protected void onStart() {
        Helper.getUserLogin(SplashScree.this);
        super.onStart();
    }
}