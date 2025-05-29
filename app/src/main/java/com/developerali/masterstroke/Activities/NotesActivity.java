package com.developerali.masterstroke.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.Adapters.NotesAdapter;
import com.developerali.masterstroke.Helpers.DB_Helper;
import com.developerali.masterstroke.Models.NotesModel;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ActivityNotesBinding;

import java.util.ArrayList;
import java.util.Collections;

public class NotesActivity extends AppCompatActivity {

    ActivityNotesBinding binding;
    DB_Helper dbHelper;
    ArrayList<NotesModel> recentSearches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Notes Section");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        dbHelper = new DB_Helper(NotesActivity.this);
        ArrayList<NotesModel> searchModelArrayList = dbHelper.getAllSearchQueries();
        recentSearches = new ArrayList<>();
        recentSearches.clear();
        for (NotesModel searchQuery : searchModelArrayList) {
            //Do something with each search query
            NotesModel recentSearchModel = new NotesModel();
            recentSearchModel.setTitle(searchQuery.getTitle());
            recentSearchModel.setDescription(searchQuery.getDescription());
            recentSearchModel.setDate(searchQuery.getDate());

            recentSearches.add(recentSearchModel);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(NotesActivity.this);
        //layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.recView.setLayoutManager(layoutManager);
        if (recentSearches != null){
            Collections.reverse(recentSearches);
            NotesAdapter adapterSearch = new NotesAdapter(NotesActivity.this, recentSearches);
            binding.recView.setAdapter(adapterSearch);
            binding.recView.setVisibility(View.VISIBLE);
            binding.noData.setVisibility(View.GONE);
        }

        if (recentSearches.isEmpty()){
            binding.recView.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }else {
            binding.recView.setVisibility(View.VISIBLE);
            binding.noData.setVisibility(View.GONE);
        }

        binding.floatingActionButton.setOnClickListener(v->{
            startActivity(new Intent(NotesActivity.this, AddNotesActivity.class));
        });





    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}