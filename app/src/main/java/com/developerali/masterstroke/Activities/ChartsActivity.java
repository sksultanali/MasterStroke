package com.developerali.masterstroke.Activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ActivityChartsBinding;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;

public class ChartsActivity extends AppCompatActivity {

    ActivityChartsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChartsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TranslateAPI translateAPI = new TranslateAPI(
                Language.AUTO_DETECT,   //Source Language
                Language.BENGALI,         //Target Language
                binding.editTextText.getText().toString());//Query Text

        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
            @Override
            public void onSuccess(String translatedText) {
                binding.textView.setText(translatedText);
            }

            @Override
            public void onFailure(String ErrorText) {
                Toast.makeText(ChartsActivity.this, ErrorText, Toast.LENGTH_SHORT).show();
            }
        });


        binding.button.setOnClickListener(v->{
            TranslateAPI translateAPI2 = new TranslateAPI(
                    Language.BENGALI,   //Source Language
                    Language.ENGLISH,         //Target Language
                    binding.editTextText.getText().toString());           //Query Text

            translateAPI2.setTranslateListener(new TranslateAPI.TranslateListener() {
                @Override
                public void onSuccess(String translatedText) {
                    binding.textView.setText(translatedText);
                }

                @Override
                public void onFailure(String ErrorText) {
                    Toast.makeText(ChartsActivity.this, ErrorText, Toast.LENGTH_SHORT).show();
                }
            });
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}