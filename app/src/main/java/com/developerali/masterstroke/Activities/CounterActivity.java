package com.developerali.masterstroke.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.Adapters.VoterAdapter;
import com.developerali.masterstroke.Adapters.WorksAdapter;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.WorksModel;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.databinding.ActivityCounterBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CounterActivity extends AppCompatActivity {

    ActivityCounterBinding binding;
    int nextPageToken, currentPage = 0;
    boolean normalNextPage, counted;
    private boolean isLoading = false;
    private boolean noMoreLoad = false;
    private boolean dualSearch = false;
    int total;
    ProgressDialog progressDialog;
    WorksAdapter adapter;
    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCounterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        Helper.WARD = sharedPreferences.getString("ward_id", "0");
        Helper.getWardName(Helper.WARD, binding.wardName);

        keyword = getIntent().getStringExtra("msg");
        normalNextPage = true;
        counted = false;

        progressDialog = new ProgressDialog(CounterActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("printing in process...");

        setupRecyclerView();
        getDetails(nextPageToken);





    }

    void getDetails(int nextToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loadMore.setVisibility(View.GONE);
        normalNextPage = true;

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<WorksModel> call;
        call = apiService.getWorkDetails(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                nextToken,
                Integer.parseInt(Helper.WARD),
                keyword
        );


        call.enqueue(new Callback<WorksModel>() {
            @Override
            public void onResponse(Call<WorksModel> call, Response<WorksModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WorksModel apiResponse = response.body();

                    if (apiResponse.getItems() == null){
                        Toast.makeText(CounterActivity.this, "not found", Toast.LENGTH_SHORT).show();
                    }else {
                        if (adapter == null) {
                            adapter = new WorksAdapter(CounterActivity.this, apiResponse.getItems());
                            binding.recView.setAdapter(adapter);
                        } else {
                            adapter.addItems(apiResponse.getItems()); // Modify your adapter to support adding new items
                        }
                        nextPageToken = Integer.parseInt(apiResponse.getNextToken());
                        currentPage = (nextPageToken/50);
                        binding.pageText.setText(String.valueOf((currentPage+1)));
                        getSupportActionBar().setSubtitle("Ward no - "+apiResponse.getItems().get(0).getWard());

                        if (apiResponse.getTotalItems() != null && !counted){
                            total = Integer.parseInt(apiResponse.getTotalItems());
                            Helper.startCounter(total, binding.totalItems);
                            counted = true;
                        }

                        if (apiResponse.getItems().size() < 50){
                            noMoreLoad = true;
                        }

                        isLoading = false;
                    }
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(CounterActivity.this, "Failed...!", Toast.LENGTH_SHORT).show();
                }

                Log.d("SearchActivity.this", "URL: " + call.request().url());
            }

            @Override
            public void onFailure(Call<WorksModel> call, Throwable t) {
                Toast.makeText(CounterActivity.this, "Error 404...!", Toast.LENGTH_SHORT).show();
                Toast.makeText(CounterActivity.this, call.request().url().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.recView.setLayoutManager(layoutManager);

        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    // Load more data here
                    if (!noMoreLoad){
                        isLoading = true;
                        binding.progressBar.setVisibility(View.VISIBLE);
                        getDetails(nextPageToken);
                    }
                }
            }
        });
    }
}