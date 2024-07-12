package com.developerali.masterstroke.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.developerali.masterstroke.Adapters.VoterAdapter;
import com.developerali.masterstroke.ApiModels.BoothReportModel;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.databinding.ActivitySearchBinding;
import com.developerali.masterstroke.databinding.DialogShareSlipBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    ActivitySearchBinding binding;
    PhoneAddressModel apiResponse;
    ArrayList<String> arrayList = new ArrayList<>();
    VoterAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);




        getDetails();





    }

    private void getDetails() {
        binding.progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<PhoneAddressModel> call = apiService.getVoters(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                60,
                118
        );

        call.enqueue(new Callback<PhoneAddressModel>() {
            @Override
            public void onResponse(Call<PhoneAddressModel> call, Response<PhoneAddressModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    apiResponse = response.body();

                    LinearLayoutManager lnm = new LinearLayoutManager(SearchActivity.this);
                    binding.recView.setLayoutManager(lnm);

                    adapter = new VoterAdapter(SearchActivity.this, apiResponse.getItem());

                    Toast.makeText(SearchActivity.this, apiResponse.getItem().get(0).getName(), Toast.LENGTH_SHORT).show();

                    binding.recView.setAdapter(adapter);
                    binding.progressBar.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(SearchActivity.this, "Failed...!", Toast.LENGTH_SHORT).show();
                }

                Log.d("SearchActivity.this", "URL: " + call.request().url());
            }

            @Override
            public void onFailure(Call<PhoneAddressModel> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Error 404...!", Toast.LENGTH_SHORT).show();
                Toast.makeText(SearchActivity.this, call.request().url().toString(), Toast.LENGTH_LONG).show();
            }
        });



//        arrayList.clear();
//        arrayList.add("Debdutta Maji");
//        arrayList.add("Akash Jaiswal");
//        arrayList.add("Bhola Jaiswal");
//        arrayList.add("Narendra Modi");
//        arrayList.add("Amit Shah");
//        arrayList.add("Sukanta Bhatyacharya");
//        arrayList.add("Suvendhu Adhikary");
//        arrayList.add("Santanu Thakur");
//        arrayList.add("JP Nadda");
//        arrayList.add("Harsh Vardhan Singhla");
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.progressBar.setVisibility(View.GONE);
//                adapter.notifyDataSetChanged();
//            }
//        }, 2000);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}