package com.developerali.masterstroke.Activities;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developerali.masterstroke.Adapters.VoterAdapter;
import com.developerali.masterstroke.ApiModels.BoothReportModel;
import com.developerali.masterstroke.ApiModels.ConstitutionModel;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helper;
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
    ArrayList<String> searchParameters = new ArrayList<>();
    VoterAdapter adapter;
    int nextPageToken, currentPage = 0;
    String searchOn, searchKeyword;
    boolean normalNextPage, counted;
    private boolean isLoading = false;
    private boolean noMoreLoad = false;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        searchParameters.clear();
//        searchParameters.add("0-50");
//        searchParameters.add("51-100");
//        searchParameters.add("101-150");
//        searchParameters.add("151-200");
//        searchParameters.add("201");
//        searchParameters.add("age");
//        searchParameters.add("polling_station");
//        searchParameters.add("house");
//        searchParameters.add("address");
//        searchParameters.add("mobile");
//        searchParameters.add("ward");
//
//        ArrayAdapter<String> obj2 = new ArrayAdapter<String>(SearchActivity.this, R.layout.layout_spinner_item, searchParameters);
//        binding.spinnerSem.setAdapter(obj2);
//        binding.spinnerSem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                searchOn = searchParameters.get(position);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

//        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                nextPageToken = 0; searchKeyword = s;
//                getSearchVoters(nextPageToken, searchKeyword, searchOn);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });

        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        Helper.WARD = sharedPreferences.getString("ward_id", "0");

        searchOn = "name";
        normalNextPage = true;
        counted = false;

        Intent i = getIntent();
        if (i.hasExtra("searchOn")){
            normalNextPage = false;
            searchOn = i.getStringExtra("searchOn");
            searchKeyword = i.getStringExtra("keyword");

            getSupportActionBar().setTitle("Searching By " + searchOn + " : " + searchKeyword);
            nextPageToken = 0;
            getSearchVoters(nextPageToken, searchKeyword, searchOn);
        }else {
            normalNextPage = true;
            getDetails(nextPageToken);
        }

        Helper.getWardName(Helper.WARD, binding.wardName);

        binding.loadMore.setOnClickListener(v->{
            if (!normalNextPage){
                getSearchVoters(nextPageToken, searchKeyword, searchOn);
            }else {
                getDetails(nextPageToken);
            }
        });

        binding.goPage.setOnClickListener(v->{
            int page = Integer.parseInt(binding.pageText.getText().toString());
            if (page > 0){
                int nextTok = (page-1)*50;
                if (total >= nextTok){
                    if (!normalNextPage){
                        getSearchVoters(nextTok, searchKeyword, searchOn);
                        //Toast.makeText(this, "token : "+ nextTok, Toast.LENGTH_LONG).show();
                    }else {
                        getDetails(nextTok);
                    }
                    binding.recView.smoothScrollToPosition(nextTok);
                }else {
                    Toast.makeText(this, "last page reached..!", Toast.LENGTH_SHORT).show();
                }
            }
            hideKeyboard();
        });

        binding.pageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String txt = binding.pageText.getText().toString();
                if (!txt.isEmpty()){
                    int page = Integer.parseInt(txt);
                    if ((page-1) == currentPage){
                        binding.goPage.setVisibility(View.GONE);
                    }else {
                        binding.goPage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        setupRecyclerView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.saveBtn){
            if (!normalNextPage){
                adapter.removeItems();
                getSearchVoters((nextPageToken-50), searchKeyword, searchOn);
            }else {
                adapter.removeItems();
                getDetails((nextPageToken-50));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        Helper.getUserLogin(SearchActivity.this);
        super.onStart();
    }

    public void hideKeyboard() {
        Activity activity = SearchActivity.this;
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
                        if (!normalNextPage) {
                            getSearchVoters(nextPageToken, searchKeyword, searchOn);
                        } else {
                            getDetails(nextPageToken);
                        }
                    }
                }
            }
        });
    }

    private void getSearchVoters(int nextToken, String keyword, String field) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loadMore.setVisibility(View.GONE);
        normalNextPage = false;

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<PhoneAddressModel> call;
        if (field.equalsIgnoreCase("mobile")){
            call = apiService.getOnlyMobileNo(
                    "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                    nextToken,
                    Integer.parseInt(Helper.WARD)
            );
        }else {
            call = apiService.SearchVoters(
                    "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                    nextToken,
                    Integer.parseInt(Helper.WARD),
                    keyword,
                    field
            );
        }


        call.enqueue(new Callback<PhoneAddressModel>() {
            @Override
            public void onResponse(Call<PhoneAddressModel> call, Response<PhoneAddressModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PhoneAddressModel apiResponse = response.body();

                    if (apiResponse.getItem() == null){
                        Toast.makeText(SearchActivity.this, "not found", Toast.LENGTH_SHORT).show();
                    }else {
                        if (adapter == null) {
                            adapter = new VoterAdapter(SearchActivity.this, apiResponse.getItem());
                            binding.recView.setAdapter(adapter);
                        } else {
                            adapter.addItems(apiResponse.getItem()); // Modify your adapter to support adding new items
                        }

                        nextPageToken = Integer.parseInt(apiResponse.getNextToken());
                        currentPage = (nextPageToken/50);
                        binding.pageText.setText(String.valueOf((currentPage)));
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

    }

    private void getDetails(int nextToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loadMore.setVisibility(View.GONE);
        normalNextPage = true;

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<PhoneAddressModel> call = apiService.getVoters(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                nextToken,
                Integer.parseInt(Helper.WARD)
        );

        call.enqueue(new Callback<PhoneAddressModel>() {
            @Override
            public void onResponse(Call<PhoneAddressModel> call, Response<PhoneAddressModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PhoneAddressModel apiResponse = response.body();

                    if (apiResponse.getItem() == null){
                        Toast.makeText(SearchActivity.this, "not found", Toast.LENGTH_SHORT).show();
                    }else {
                        if (adapter == null) {
                            adapter = new VoterAdapter(SearchActivity.this, apiResponse.getItem());
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

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}