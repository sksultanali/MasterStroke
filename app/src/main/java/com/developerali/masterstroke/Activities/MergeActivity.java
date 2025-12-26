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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.developerali.masterstroke.Adapters.MergeAdapter;
import com.developerali.masterstroke.Adapters.PartSecAdapter;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.UpdateModel;
import com.developerali.masterstroke.ApiModels.WardClass;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.SelectionListner;
import com.developerali.masterstroke.databinding.ActivityMergeBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MergeActivity extends AppCompatActivity implements SelectionListner {

    ActivityMergeBinding binding;
    MergeAdapter adapter;
    ArrayList<String> sort = new ArrayList<>();
    ProgressDialog progressDialog;
    ArrayList<String> arrayListChoose;
    String sortType, txt;
    ArrayList<String> casteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMergeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sort.clear();
        sort.add("Large to Small");
        sort.add("Small to Large");
        sort.add("A-Z");
        sort.add("Z-A");

        ArrayAdapter<String> obj2 = new ArrayAdapter<String>(MergeActivity.this, R.layout.layout_spinner_item, sort);
        binding.spinnerSem.setAdapter(obj2);
        sortType = sort.get(0);

        progressDialog = new ProgressDialog(MergeActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("updating data...");

        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        Helper.WARD = sharedPreferences.getString("ward_id", "0");

        Intent intent = getIntent();
        txt = intent.getStringExtra("name");

        getSupportActionBar().setTitle("Search on " + txt);
        Helper.getWardName(Helper.WARD, binding.wardName);
        Helper.getWardNo(Helper.WARD, MergeActivity.this, getSupportActionBar());
        LinearLayoutManager lnm = new LinearLayoutManager(MergeActivity.this);
        binding.recView.setLayoutManager(lnm);


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
        arrayListChoose.add("Bengali/Hindu");
        arrayListChoose.add("Hindi/Hindu");
        arrayListChoose.add("Urdu/Muslim");

        binding.shareSlip.setOnClickListener(v -> {
            Helper.searchDialog(MergeActivity.this, "Set Language", arrayListChoose, keyword -> {
                ArrayList<WardClass.Item> items = adapter.getSelectedRows();
                progressDialog.show();

                if (keyword.equalsIgnoreCase("Urdu/Muslim")){
                    updateItemsSequentiallyLname(items, "language", "Urdu", 0);
                    updateItemsSequentiallyLname(items, "religion", "Muslim", 0);
                }else if (keyword.equalsIgnoreCase("Hindi/Hindu")){
                    updateItemsSequentiallyLname(items, "language", "Hindi", 0);
                    updateItemsSequentiallyLname(items, "religion", "Hindu", 0);
                }else {
                    updateItemsSequentiallyLname(items, "language", "Bengali", 0);
                    updateItemsSequentiallyLname(items, "religion", "Hindu", 0);
                }

            });
        });

        binding.surveyOurParty.setOnClickListener(v->{
            progressDialog.show();
            ArrayList<WardClass.Item> items = adapter.getSelectedRows();
            progressDialog.show();
            updateItemsSequentiallyLname(items, "intereset_party", "Our Party", 0);
        });
        binding.oppositionParty.setOnClickListener(v->{
            progressDialog.show();
            ArrayList<WardClass.Item> items = adapter.getSelectedRows();
            progressDialog.show();
            updateItemsSequentiallyLname(items, "intereset_party", "Opposition Party", 0);
        });
        binding.doubtfulParty.setOnClickListener(v->{
            progressDialog.show();
            ArrayList<WardClass.Item> items = adapter.getSelectedRows();
            progressDialog.show();
            updateItemsSequentiallyLname(items, "intereset_party", "Doubtful", 0);
        });

        casteArray = new ArrayList<>();
        casteArray.clear();
        casteArray.add("Upper");
        casteArray.add("OBC");
        casteArray.add("SC/ST");

        binding.casteUpdate.setOnClickListener(v -> {
            Helper.searchDialog(MergeActivity.this, "Set Caste", casteArray, keyword -> {
                ArrayList<WardClass.Item> items = adapter.getSelectedRows();
                progressDialog.show();

                if (keyword.equalsIgnoreCase("Upper")){
                    updateItemsSequentiallyLname(items, "caste", "Upper", 0);
                }else if (keyword.equalsIgnoreCase("OBC")){
                    updateItemsSequentiallyLname(items, "caste", "OBC", 0);
                }else {
                    updateItemsSequentiallyLname(items, "caste", "SC/ST", 0);
                }

            });
        });

        getRequestedData("lname", null);






    }


    private void updateItemsSequentiallyLname(ArrayList<WardClass.Item> items, String fieldname, String keyword, int index) {
        if (index < items.size()) {
            WardClass.Item currentItem = items.get(index);
            ArrayList<String> subnames = currentItem.getSubnames();

            // Process each subname sequentially
            updateSubnamesSequentially2(subnames, fieldname, keyword, 0, () -> {
                // Once all subnames are processed, proceed to the next main item
                updateItemsSequentiallyLname(items, fieldname, keyword, index + 1);
            });
        } else {
            // All updates are done
            progressDialog.dismiss();
            Toast.makeText(this, "All updates completed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSubnamesSequentially2(ArrayList<String> subnames, String fieldname, String keyword, int subIndex, Runnable onComplete) {
        if (subIndex < subnames.size()) {
            // Update the current subname
            updateAllData(subnames.get(subIndex), fieldname, keyword, subIndex, success -> {
                // Proceed to the next subname after the current update is complete
                updateSubnamesSequentially2(subnames, fieldname, keyword, subIndex + 1, onComplete);
            });
        } else {
            // All subnames are done, call the completion callback
            onComplete.run();
        }
    }


    interface UpdateCallback {
        void onUpdateComplete(boolean success);
    }

    public void updateAllData(String queryText, String fieldname, String changeValue, int position, MergeActivity.UpdateCallback callback) {
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
                    Toast.makeText(MergeActivity.this, apiResponse.getStatus()
                            + " : " + position + " updated", Toast.LENGTH_SHORT).show();
                    callback.onUpdateComplete(true); // Notify success
                } else {
                    Toast.makeText(MergeActivity.this, "Failed here...!", Toast.LENGTH_SHORT).show();
                    callback.onUpdateComplete(false); // Notify failure
                }
                Log.d("PartSecAd.this", "URL: " + call.request().url());
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("PartSecAd.this", "URL: " + call.request().url());
                Toast.makeText(MergeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MergeActivity.this, "not found", Toast.LENGTH_SHORT).show();
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
                                    // Assuming item.getName() returns the string field to be sorted alphabetically
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

                        adapter = new MergeAdapter(MergeActivity.this, apiResponse.getItem(), apiResponse.getGrossTotal(), MergeActivity.this);
                        binding.recView.setAdapter(adapter);
                        Helper.startCounter(apiResponse.getItem().size(), binding.totalCount);

                        //getSupportActionBar().setSubtitle("Ward no - "+apiResponse.getItem().get(0).getWard());
                    }
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MergeActivity.this, "Failed...!", Toast.LENGTH_SHORT).show();
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
            call = apiService.getUniqueWardsFF(
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
                    txt, //lname
                    "language",
                    t //''
            );
        }


        call.enqueue(new Callback<WardClass>() {
            @Override
            public void onResponse(Call<WardClass> call, Response<WardClass> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WardClass apiResponse = response.body();

                    if (apiResponse.getItem() == null){
                        Toast.makeText(MergeActivity.this, "not found", Toast.LENGTH_SHORT).show();
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
                                    // Assuming item.getName() returns the string field to be sorted alphabetically
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

                        adapter = new MergeAdapter(MergeActivity.this, apiResponse.getItem(), apiResponse.getGrossTotal(), MergeActivity.this);
                        binding.recView.setAdapter(adapter);
                        Helper.startCounter(apiResponse.getItem().size(), binding.totalCount);

                        //getSupportActionBar().setSubtitle("Ward no - "+apiResponse.getItem().get(0).getWard());
                    }
                    binding.progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MergeActivity.this, "Failed...!", Toast.LENGTH_SHORT).show();
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
        Helper.getUserLogin(MergeActivity.this);
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
            binding.surveyLayout.setVisibility(View.VISIBLE);
//            binding.selectedNo.setVisibility(View.VISIBLE);
//            binding.selectedNo.setText(adapter.getSelectedRows().size() + " voters selected");
        }else {
            binding.shareSlip.setVisibility(View.GONE);
            binding.surveyLayout.setVisibility(View.GONE);
//            binding.selectedNo.setVisibility(View.GONE);
        }
    }
}