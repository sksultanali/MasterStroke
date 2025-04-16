package com.developerali.masterstroke.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerali.masterstroke.MainActivity;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ActivityStatPageBinding;

public class StatPageActivity extends AppCompatActivity {

    ActivityStatPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.nextBtn.setOnClickListener(v->{
            startActivity(new Intent(StatPageActivity.this, MainActivity.class));
        });




    }
}