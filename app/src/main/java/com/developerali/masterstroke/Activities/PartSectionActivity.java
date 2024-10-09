package com.developerali.masterstroke.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.developerali.masterstroke.Adapters.PartSecAdapter;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.UpdateModel;
import com.developerali.masterstroke.ApiModels.WardClass;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.SelectionListner;
import com.developerali.masterstroke.databinding.ActivityPartSectionBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartSectionActivity extends AppCompatActivity implements SelectionListner {

    ActivityPartSectionBinding binding;
    String txt, sortType, adType;
    PartSecAdapter adapter;
    ArrayList<String> sort = new ArrayList<>();
    ProgressDialog progressDialog;
    ArrayList<String> arrayListChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPartSectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sort.clear();
        sort.add("Large to Small");
        sort.add("Small to Large");
        sort.add("A-Z");
        sort.add("Z-A");

        ArrayAdapter<String> obj2 = new ArrayAdapter<String>(PartSectionActivity.this, R.layout.layout_spinner_item, sort);
        binding.spinnerSem.setAdapter(obj2);
        sortType = sort.get(0);

        progressDialog = new ProgressDialog(PartSectionActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("updating data...");

        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        Helper.WARD = sharedPreferences.getString("ward_id", "0");

        Intent intent = getIntent();
        txt = intent.getStringExtra("name");
        adType = null;
        if (intent.hasExtra("merge")){
            adType = intent.getStringExtra("merge");
        }

        getSupportActionBar().setTitle("Search on " + txt);

        Helper.getWardName(Helper.WARD, binding.wardName);
        Helper.getWardNo(Helper.WARD, PartSectionActivity.this, getSupportActionBar());
        LinearLayoutManager lnm = new LinearLayoutManager(PartSectionActivity.this);
        binding.recView.setLayoutManager(lnm);

        if (txt.equalsIgnoreCase("language_Part")){
            Helper.LANGUAGE = intent.getStringExtra("lan");
            binding.lanChange.setVisibility(View.VISIBLE);

            if (!binding.lanChange.isChecked()){
                getLanPartData("language", Helper.LANGUAGE );
                getSupportActionBar().setTitle("Search on " + Helper.LANGUAGE + "vasi partwise");
                txt = "language_Part";
            }else {
                getRequestedData("language", Helper.LANGUAGE);
                getSupportActionBar().setTitle("Search on " + Helper.LANGUAGE + "vasi by L_Name");
                Helper.MARKING_ENABLE = false;
                txt = "lname";
            }
        }else if (txt.equalsIgnoreCase("religion_Part")){
            Helper.LANGUAGE = intent.getStringExtra("lan");
            getLanPartData("religion", Helper.LANGUAGE );
            getSupportActionBar().setTitle("Search on " + Helper.LANGUAGE + " partwise");
        }else if (txt.equalsIgnoreCase("family_Part")){

            Helper.LANGUAGE = intent.getStringExtra("lan");
            getLanPartData("family", Helper.LANGUAGE );
            getSupportActionBar().setTitle("Search on family");

        } else if (txt.equalsIgnoreCase("family_Part_Part")){

            Helper.LANGUAGE = intent.getStringExtra("lan"); // part_no
            String F = Helper.LANGUAGE;
            getRequestedData(txt, "family");
            getSupportActionBar().setTitle("Search on family");

        } else if (txt.equalsIgnoreCase("Dead_Part") || txt.equalsIgnoreCase("Relocated_Part")){
            Helper.LANGUAGE = intent.getStringExtra("lan");
            getLanPartData("status", Helper.LANGUAGE );
            getSupportActionBar().setTitle("Search on " + Helper.LANGUAGE + " partwise");
        }else if (txt.equalsIgnoreCase("Doa_Part")){
            Helper.LANGUAGE = intent.getStringExtra("lan");
            getLanPartData("doa", Helper.LANGUAGE );
            getSupportActionBar().setTitle("DOA " + Helper.LANGUAGE + " partwise");
        }else if (txt.equalsIgnoreCase("Dob_Part")){
            Helper.LANGUAGE = intent.getStringExtra("lan");
            getLanPartData("dob", Helper.LANGUAGE );
            getSupportActionBar().setTitle("DOB " + Helper.LANGUAGE + " partwise");
        }else if (txt.equalsIgnoreCase("intereset_party")){
            Helper.LANGUAGE = intent.getStringExtra("lan");
            getLanPartData("intereset_party", Helper.LANGUAGE );
            getSupportActionBar().setTitle("Search on " + Helper.LANGUAGE + " partwise");
        }else if (txt.equalsIgnoreCase("hof_part")){
            Helper.LANGUAGE = intent.getStringExtra("lan");
            getLanPartData("hof", Helper.LANGUAGE );
            getSupportActionBar().setTitle("Head of Family Partwise");
        }else {
            getRequestedData(txt, null);
        }

        binding.lanChange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!binding.lanChange.isChecked()){
                    getLanPartData("language", Helper.LANGUAGE );
                    getSupportActionBar().setTitle("Search on " + Helper.LANGUAGE + "vasi partwise");
                    txt = "language_Part";
                }else {
                    getRequestedData("lname", Helper.LANGUAGE);
                    getSupportActionBar().setTitle("Search on " + Helper.LANGUAGE + "vasi by L_Name");
                    Helper.MARKING_ENABLE = false;
                    txt = "lname";
                }
            }
        });

        binding.spinnerSem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortType = sort.get(position);
                getRequestedData(txt, null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (txt.equalsIgnoreCase("lname")){
            binding.sLay.setVisibility(View.VISIBLE);
        }else {
            binding.sLay.setVisibility(View.GONE);
        }

        arrayListChoose = new ArrayList<>();
        arrayListChoose.clear();
        arrayListChoose.add("Bengali");
        arrayListChoose.add("Hindi");
        arrayListChoose.add("Urdu/Muslim");

        binding.shareSlip.setOnClickListener(v -> {
            Helper.searchDialog(PartSectionActivity.this, "Set Language", arrayListChoose, keyword -> {
                ArrayList<WardClass.Item> items = adapter.getSelectedRows();
                progressDialog.show();

                if (keyword.equalsIgnoreCase("Urdu/Muslim")){
                    updateItemsSequentially(items, "language", "Urdu", 0);
                    updateItemsSequentially(items, "religion", "Muslim", 0);
                }else {
                    updateItemsSequentially(items, "language", keyword, 0);
                }

            });
        });



    }

    private void updateItemsSequentially(ArrayList<WardClass.Item> items, String fieldname, String keyword, int index) {
        if (index < items.size()) {
            // Process the current item and move to the next on completion
            updateAllData(items.get(index).getTxt(), fieldname, keyword, index, success -> {
                // Proceed to the next item after the current update is complete
                updateItemsSequentially(items, fieldname, keyword, index + 1);
            });
        } else {
            // All updates are done
            progressDialog.dismiss();
            Toast.makeText(this, "All updates completed!", Toast.LENGTH_SHORT).show();
        }
    }

    interface UpdateCallback {
        void onUpdateComplete(boolean success);
    }

    public void updateAllData(String queryText, String fieldname, String changeValue, int position, UpdateCallback callback) {
        progressDialog.setMessage(position + ". " + queryText + " changing to " + changeValue + "...");
        progressDialog.show();

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<UpdateModel> call = apiService.updateAllData(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                "lname",
                queryText,
                fieldname,
                changeValue,
                Integer.parseInt(Helper.WARD)
        );

        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    UpdateModel apiResponse = response.body();
                    Toast.makeText(PartSectionActivity.this, apiResponse.getStatus()
                            + " : " + position + " updated", Toast.LENGTH_SHORT).show();
                    callback.onUpdateComplete(true); // Notify success
                } else {
                    Toast.makeText(PartSectionActivity.this, "Failed here...!", Toast.LENGTH_SHORT).show();
                    callback.onUpdateComplete(false); // Notify failure
                }
                Log.d("PartSecAd.this", "URL: " + call.request().url());
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("PartSecAd.this", "URL: " + call.request().url());
                Toast.makeText(PartSectionActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                callback.onUpdateComplete(false); // Notify failure
            }
        });
    }


    private void getLanPartData(String field, String values) {
        binding.progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<WardClass> call = apiService.getUniquePartLan(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                Integer.parseInt(Helper.WARD), field, values
        );

        call.enqueue(new Callback<WardClass>() {
            @Override
            public void onResponse(Call<WardClass> call, Response<WardClass> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WardClass apiResponse = response.body();

                    if (apiResponse.getItem() == null){
                        Toast.makeText(PartSectionActivity.this, "not found", Toast.LENGTH_SHORT).show();
                    }else {

                        if (sortType.equalsIgnoreCase("Large to Small") && txt.equalsIgnoreCase("lname")){
                            Collections.sort(apiResponse.getItem(), new Comparator<WardClass.Item>() {
                                @Override
                                public int compare(WardClass.Item item, WardClass.Item t1) {
                                    return Integer.compare(Integer.parseInt(t1.getTotal()), Integer.parseInt(item.getTotal()));
                                }
                            });
                        }else if (sortType.equalsIgnoreCase("A-Z") && txt.equalsIgnoreCase("lname")){
                            Collections.sort(apiResponse.getItem(), new Comparator<WardClass.Item>() {
                                @Override
                                public int compare(WardClass.Item item1, WardClass.Item item2) {
                                    // Assuming `item.getName()` returns the string field to be sorted alphabetically
                                    return item1.getTxt().compareToIgnoreCase(item2.getTotal());
                                }
                            });
                        }else if (txt.equalsIgnoreCase("lname")){
                            Collections.sort(apiResponse.getItem(), new Comparator<WardClass.Item>() {
                                @Override
                                public int compare(WardClass.Item item1, WardClass.Item item2) {
                                    // Reverse the comparison to get Z-A sorting
                                    return item2.getTxt().compareToIgnoreCase(item1.getTxt());
                                }
                            });

                        }

                        adapter = new PartSecAdapter(PartSectionActivity.this, apiResponse.getItem(), txt, adType,
                                apiResponse.getGrossTotal(), PartSectionActivity.this);
                        binding.recView.setAdapter(adapter);
                        Helper.startCounter(apiResponse.getItem().size(), binding.totalCount);

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

    private void getRequestedData(String txt, String t) {
        binding.progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<WardClass> call;
        if (t == null){
            call = apiService.getUniqueWards(
                    "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                    Integer.parseInt(Helper.WARD),
                    txt
            );
        } else if (t.equalsIgnoreCase("family")) {
            call = apiService.getUniqueValuesWithQuery(
                    "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                    Integer.parseInt(Helper.WARD),
                    "family",
                    "part_no",
                    Helper.LANGUAGE
            );
        } else {
            call = apiService.getUniqueValuesWithQuery(
                    "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                    Integer.parseInt(Helper.WARD),
                    txt,
                    "language",
                    t
            );
        }


        call.enqueue(new Callback<WardClass>() {
            @Override
            public void onResponse(Call<WardClass> call, Response<WardClass> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WardClass apiResponse = response.body();

                    if (apiResponse.getItem() == null){
                        Toast.makeText(PartSectionActivity.this, "not found", Toast.LENGTH_SHORT).show();
                    }else {

                        if (sortType.equalsIgnoreCase("Large to Small") && txt.equalsIgnoreCase("lname")){
                            Collections.sort(apiResponse.getItem(), new Comparator<WardClass.Item>() {
                                @Override
                                public int compare(WardClass.Item item, WardClass.Item t1) {
                                    return Integer.compare(Integer.parseInt(t1.getTotal()), Integer.parseInt(item.getTotal()));
                                }
                            });
                        }else if (sortType.equalsIgnoreCase("Small to Large") && txt.equalsIgnoreCase("lname")){
                            Collections.sort(apiResponse.getItem(), new Comparator<WardClass.Item>() {
                                @Override
                                public int compare(WardClass.Item item, WardClass.Item t1) {
                                    return Integer.compare(Integer.parseInt(item.getTotal()), Integer.parseInt(t1.getTotal()));
                                }
                            });
                        }else if (sortType.equalsIgnoreCase("A-Z") && txt.equalsIgnoreCase("lname")){
                            Collections.sort(apiResponse.getItem(), new Comparator<WardClass.Item>() {
                                @Override
                                public int compare(WardClass.Item item1, WardClass.Item item2) {
                                    // Assuming `item.getName()` returns the string field to be sorted alphabetically
                                    return item1.getTxt().compareToIgnoreCase(item2.getTotal());
                                }
                            });
                        }else if (txt.equalsIgnoreCase("lname")){
                            Collections.sort(apiResponse.getItem(), new Comparator<WardClass.Item>() {
                                @Override
                                public int compare(WardClass.Item item1, WardClass.Item item2) {
                                    // Reverse the comparison to get Z-A sorting
                                    return item2.getTxt().compareToIgnoreCase(item1.getTxt());
                                }
                            });

                        }

                        adapter = new PartSecAdapter(PartSectionActivity.this, apiResponse.getItem(), txt, adType, apiResponse.getGrossTotal(), PartSectionActivity.this);
                        binding.recView.setAdapter(adapter);
                        Helper.startCounter(apiResponse.getItem().size(), binding.totalCount);

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

    @Override
    public void onShowAction(Boolean isSelected) {
        if (isSelected){
            binding.shareSlip.setVisibility(View.VISIBLE);
//            binding.selectedNo.setVisibility(View.VISIBLE);
//            binding.selectedNo.setText(adapter.getSelectedRows().size() + " voters selected");
        }else {
            binding.shareSlip.setVisibility(View.GONE);
//            binding.selectedNo.setVisibility(View.GONE);
        }
    }
}