package com.developerali.masterstroke.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.Adapters.ChildVotersAdapter;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.WardStudentVoterModel;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.databinding.ActivityChildOrNewVoterBinding;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildOrNewVoter extends AppCompatActivity {

    ActivityChildOrNewVoterBinding binding;
    PhoneAddressModel.Item  details;
    ProgressDialog progressDialog;
    String typeTxt;
    ArrayList<String> classes = new ArrayList<>();
    Calendar calendar;
    String[] types = {"Student", "Upcoming Voter"};
    int nextPageToken, currentPage = 0;
    String searchOn, searchKeyword;
    boolean normalNextPage, counted;
    private boolean isLoading = false;
    private boolean noMoreLoad = false;
    int total;
    ChildVotersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildOrNewVoterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.students);

        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        Helper.WARD = sharedPreferences.getString("ward_id", "0");

        progressDialog = new ProgressDialog(ChildOrNewVoter.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("updating data...");

        ArrayAdapter<String> obj3 = new ArrayAdapter<String>(ChildOrNewVoter.this, R.layout.layout_spinner_item, types);
        binding.spinnerS.setAdapter(obj3);
        typeTxt = types[0];

        Intent intent = getIntent();
        if (intent.hasExtra("details")){
            details = intent.getParcelableExtra("details");
            searchOn = "single";

        } else if (intent.hasExtra("type")) {
            typeTxt = intent.getStringExtra("type");
            searchOn ="all";

            binding.typeLabel.setVisibility(View.GONE);
            getSupportActionBar().setTitle("All " + typeTxt + " List");
        }

        Helper.getWardName(Helper.WARD, binding.wardName);

        binding.spinnerS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeTxt = types[position];
                getSupportActionBar().setTitle(typeTxt + " List");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (searchOn.equalsIgnoreCase("all")){
            getAllDetails(0);
        }else {
            getDetails(0);
        }
        setupRecyclerView();


    }

    void getDetails(int nextToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loadMore.setVisibility(View.GONE);
        normalNextPage = true;

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<WardStudentVoterModel> call = apiService.getStudentNewVoter(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                details.getConPhoneId(),
                typeTxt);

        call.enqueue(new Callback<WardStudentVoterModel>() {
            @Override
            public void onResponse(Call<WardStudentVoterModel> call, Response<WardStudentVoterModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WardStudentVoterModel apiResponse = response.body();

                    if (apiResponse.getItem() == null){
                        Toast.makeText(ChildOrNewVoter.this, "not found", Toast.LENGTH_SHORT).show();
                    }else {
                        if (adapter == null) {
                            adapter = new ChildVotersAdapter(ChildOrNewVoter.this, apiResponse.getItem());
                            binding.recView.setAdapter(adapter);
                        } else {
                            adapter.addItems(apiResponse.getItem()); // Modify your adapter to support adding new items
                        }
                        nextPageToken = Integer.parseInt(apiResponse.getNextToken());
                        currentPage = (nextPageToken/50);
                        binding.pageText.setText(String.valueOf((currentPage+1)));
                        getSupportActionBar().setSubtitle("Ward no - "+apiResponse.getItem().get(0).getWard());

                        if (apiResponse.getTotalItems() != null && !counted){
                            total = Integer.parseInt(apiResponse.getTotalItems());
                            Helper.startCounter(total, binding.totalItems);
                            counted = true;
                        }

                        if (apiResponse.getItem().size() < 50){
                            noMoreLoad = true;
                        }

                        isLoading = false;
                    }
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(ChildOrNewVoter.this, "Failed...!", Toast.LENGTH_SHORT).show();
                }

                Log.d("SearchActivity.this", "URL: " + call.request().url());
            }

            @Override
            public void onFailure(Call<WardStudentVoterModel> call, Throwable t) {
                Toast.makeText(ChildOrNewVoter.this, "Error 404...!", Toast.LENGTH_SHORT).show();
                Toast.makeText(ChildOrNewVoter.this, call.request().url().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    void getAllDetails(int nextToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loadMore.setVisibility(View.GONE);
        normalNextPage = true;

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<WardStudentVoterModel> call = apiService.getAllStudentNewVoter(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                typeTxt);

        call.enqueue(new Callback<WardStudentVoterModel>() {
            @Override
            public void onResponse(Call<WardStudentVoterModel> call, Response<WardStudentVoterModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WardStudentVoterModel apiResponse = response.body();

                    if (apiResponse.getItem() == null){
                        Toast.makeText(ChildOrNewVoter.this, "not found", Toast.LENGTH_SHORT).show();
                    }else {
                        if (adapter == null) {
                            adapter = new ChildVotersAdapter(ChildOrNewVoter.this, apiResponse.getItem());
                            binding.recView.setAdapter(adapter);
                        } else {
                            adapter.addItems(apiResponse.getItem()); // Modify your adapter to support adding new items
                        }
                        nextPageToken = Integer.parseInt(apiResponse.getNextToken());
                        currentPage = (nextPageToken/50);
                        binding.pageText.setText(String.valueOf((currentPage+1)));
                        getSupportActionBar().setSubtitle("Ward no - "+apiResponse.getItem().get(0).getWard());

                        if (apiResponse.getTotalItems() != null && !counted){
                            total = Integer.parseInt(apiResponse.getTotalItems());
                            Helper.startCounter(total, binding.totalItems);
                            counted = true;
                        }

                        if (apiResponse.getItem().size() < 50){
                            noMoreLoad = true;
                        }

                        isLoading = false;
                    }
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(ChildOrNewVoter.this, "Failed...!", Toast.LENGTH_SHORT).show();
                }

                Log.d("SearchActivity.this", "URL: " + call.request().url());
            }

            @Override
            public void onFailure(Call<WardStudentVoterModel> call, Throwable t) {
                Toast.makeText(ChildOrNewVoter.this, "Error 404...!", Toast.LENGTH_SHORT).show();
                Toast.makeText(ChildOrNewVoter.this, call.request().url().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onStart() {
        Helper.getUserLogin(ChildOrNewVoter.this);
        super.onStart();
    }

    public void hideKeyboard() {
        Activity activity = ChildOrNewVoter.this;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        //binding.pageText.setCursorVisible(false);
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
                        if (searchOn.equalsIgnoreCase("all")){
                            getAllDetails(nextPageToken);
                        }else {
                            getDetails(nextPageToken);
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addSomething){

            Intent i = new Intent(ChildOrNewVoter.this, ChildActivity.class);
            i.putExtra("details", details);
            startActivity(i);
        }else if (item.getItemId() == R.id.refreshSomething){
            if (adapter != null){
                adapter.removeItems();
            }
            if (searchOn.equalsIgnoreCase("all")){
                getAllDetails(0);
            }else {
                getDetails(0);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}