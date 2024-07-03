package com.developerali.masterstroke.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.developerali.masterstroke.Adapters.VoterAdapter;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.databinding.ActivitySearchBinding;
import com.developerali.masterstroke.databinding.DialogShareSlipBinding;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    ArrayList<String> arrayList = new ArrayList<>();
    VoterAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        adapter = new VoterAdapter(SearchActivity.this, arrayList);
        LinearLayoutManager lnm = new LinearLayoutManager(SearchActivity.this);
        binding.recView.setLayoutManager(lnm);
        binding.recView.setAdapter(adapter);

        getDetails();





    }

    private void getDetails() {
        binding.progressBar.setVisibility(View.VISIBLE);
        arrayList.clear();
        arrayList.add("Debdutta Maji");
        arrayList.add("Akash Jaiswal");
        arrayList.add("Bhola Jaiswal");
        arrayList.add("Narendra Modi");
        arrayList.add("Amit Shah");
        arrayList.add("Sukanta Bhatyacharya");
        arrayList.add("Suvendhu Adhikary");
        arrayList.add("Santanu Thakur");
        arrayList.add("JP Nadda");
        arrayList.add("Harsh Vardhan Singhla");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}