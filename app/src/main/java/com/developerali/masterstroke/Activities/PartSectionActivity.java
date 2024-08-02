package com.developerali.masterstroke.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.developerali.masterstroke.Adapters.PartSecAdapter;
import com.developerali.masterstroke.Adapters.VoterAdapter;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.WardClass;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helper;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.databinding.ActivityPartSectionBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartSectionActivity extends AppCompatActivity {

    ActivityPartSectionBinding binding;
    String txt;
    PartSecAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPartSectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        Helper.WARD = sharedPreferences.getString("ward_id", "0");

        Intent intent = getIntent();
        txt = intent.getStringExtra("name");

        getSupportActionBar().setTitle("Search on " + txt);

        Helper.getWardName(Helper.WARD, binding.wardName);
        Helper.getWardNo(Helper.WARD, PartSectionActivity.this, getSupportActionBar());
        LinearLayoutManager lnm = new LinearLayoutManager(PartSectionActivity.this);
        binding.recView.setLayoutManager(lnm);
        getRequestedData(txt);


    }

    private void getRequestedData(String txt) {
        binding.progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<WardClass> call = apiService.getUniqueWards(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                Integer.parseInt(Helper.WARD),
                txt
        );

        call.enqueue(new Callback<WardClass>() {
            @Override
            public void onResponse(Call<WardClass> call, Response<WardClass> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WardClass apiResponse = response.body();

                    if (apiResponse.getItem() == null){
                        Toast.makeText(PartSectionActivity.this, "not found", Toast.LENGTH_SHORT).show();
                    }else {
                        adapter = new PartSecAdapter(PartSectionActivity.this, apiResponse.getItem(), txt);
                        binding.recView.setAdapter(adapter);

                        //getSupportActionBar().setSubtitle("Ward no - "+apiResponse.getItem().get(0).getWard());
                    }
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(PartSectionActivity.this, "Failed...!", Toast.LENGTH_SHORT).show();
                }

                Log.d("SearchActivity.this", "URL: " + call.request().url());
            }

            @Override
            public void onFailure(Call<WardClass> call, Throwable t) {

            }
        });
    }
    @Override
    protected void onStart() {
        Helper.getUserLogin(PartSectionActivity.this);
        super.onStart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}