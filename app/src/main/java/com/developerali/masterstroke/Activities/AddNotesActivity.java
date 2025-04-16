package com.developerali.masterstroke.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerali.masterstroke.Helpers.DB_Helper;
import com.developerali.masterstroke.Models.NotesModel;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ActivityAddNotesBinding;

public class AddNotesActivity extends AppCompatActivity {

    ActivityAddNotesBinding binding;
    DB_Helper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Add New Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        dbHelper = new DB_Helper(AddNotesActivity.this);


        if (getIntent().hasExtra("title")){
            String title = getIntent().getStringExtra("title");
            String des = getIntent().getStringExtra("des");

            binding.title.setText(title);
            binding.description.setText(des);
            getSupportActionBar().setTitle("Previously Added Note");
        }


        binding.saveBtn.setOnClickListener(v->{
            String title = binding.title.getText().toString();
            String des = binding.description.getText().toString();

            if (title.isEmpty()){
                binding.title.setError("*");
            } else if (des.isEmpty()) {
                binding.description.setError("*");
            }else {
                NotesModel recentSearchModel = new NotesModel();
                recentSearchModel.setTitle(title);
                recentSearchModel.setDescription(des);

                dbHelper.addSearchQuery(recentSearchModel);

                Toast.makeText(this, "Note Added...!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddNotesActivity.this, NotesActivity.class));
                finish();
            }

        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}