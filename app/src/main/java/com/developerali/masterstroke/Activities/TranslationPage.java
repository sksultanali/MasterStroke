package com.developerali.masterstroke.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.MainActivity;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ActivityTranslationPageBinding;
import com.mannan.translateapi.Language;

public class TranslationPage extends AppCompatActivity {

    ActivityTranslationPageBinding chooseBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chooseBinding = ActivityTranslationPageBinding.inflate(getLayoutInflater());
        setContentView(chooseBinding.getRoot());
        getSupportActionBar().setTitle("Choose Any Language");

        if (Helper.getLanguagePreference(TranslationPage.this).equalsIgnoreCase("hi")){
            chooseBinding.hindi.setChecked(true);
        } else if (Helper.getLanguagePreference(TranslationPage.this).equalsIgnoreCase("bn")) {
            chooseBinding.bengali.setChecked(true);
        }else if (Helper.getLanguagePreference(TranslationPage.this).equalsIgnoreCase("en")){
            chooseBinding.english.setChecked(true);
        }

        chooseBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.english) {
                    Helper.saveLanguagePreference(TranslationPage.this, "en");
                    Helper.updateLocale(TranslationPage.this, Helper.getLanguagePreference(TranslationPage.this));
                } else if (checkedId == R.id.hindi) {
                    Helper.saveLanguagePreference(TranslationPage.this, "hi");
                    Helper.updateLocale(TranslationPage.this, Helper.getLanguagePreference(TranslationPage.this));
                }else {
                    Helper.saveLanguagePreference(TranslationPage.this, "bn");
                    Helper.updateLocale(TranslationPage.this, Helper.getLanguagePreference(TranslationPage.this));
                }
                startActivity(new Intent(TranslationPage.this, MainActivity.class));
                finish();
            }
        });


    }
}