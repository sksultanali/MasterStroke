package com.developerali.masterstroke.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
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
import com.developerali.masterstroke.Adapters.WorksAdapter;
import com.developerali.masterstroke.ApiModels.FamilyCountResponse;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.UpdateModel;
import com.developerali.masterstroke.ApiModels.WorksModel;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.SelectionListner;
import com.developerali.masterstroke.databinding.ActivityNewMobileBinding;
import com.developerali.masterstroke.databinding.DialogShareSlipBinding;
import com.developerali.masterstroke.databinding.DialogTextInputBinding;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMobileActivity extends AppCompatActivity implements SelectionListner {

    ActivityNewMobileBinding binding;
    int nextPageToken, currentPage = 0;
    boolean normalNextPage, counted;
    private boolean isLoading = false;
    private boolean noMoreLoad = false;
    private boolean dualSearch = false;
    int total;
    ProgressDialog progressDialog;
    VoterAdapter adapter;
    String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNewMobileBinding.inflate(getLayoutInflater());
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

        progressDialog = new ProgressDialog(NewMobileActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("printing in process...");

        setupRecyclerView();
        getDetails(nextPageToken);

        binding.shareSlip.setOnClickListener(v->{
            editDialog();
        });

        binding.btnFamily.setOnClickListener(v->{

            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<FamilyCountResponse> call = apiService.getFamilyCount(
                    "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                    Integer.parseInt(Helper.WARD)
            );

            progressDialog.show();

            call.enqueue(new Callback<FamilyCountResponse>() {
                @Override
                public void onResponse(Call<FamilyCountResponse> call, Response<FamilyCountResponse> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        FamilyCountResponse apiResponse = response.body();
                        progressDialog.show();
                        ArrayList<PhoneAddressModel.Item> items = adapter.getSelectedRows();
                        updateItemsSequentially(items, "family", apiResponse.getCount(), 0);
                    } else {
                        Toast.makeText(NewMobileActivity.this, "Failed here...!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("PartSecAd.this", "URL: " + call.request().url());
                }

                @Override
                public void onFailure(Call<FamilyCountResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d("PartSecAd.this", "URL: " + call.request().url());
                    Toast.makeText(NewMobileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.goHouse.setOnClickListener(v->{
            String hNo = binding.homeNoText.getText().toString();
            if (hNo.isEmpty()){
                binding.homeNoText.setError("*");
            }else {
                Intent ij = new Intent(getApplicationContext(), SearchActivity.class);
                ij.putExtra("keyword", hNo);
                ij.putExtra("dualSearch", Helper.PART_NO);
                ij.putExtra("searchOn", "house");
                startActivity(ij);
            }
        });




    }

    private void updateItemsSequentially(ArrayList<PhoneAddressModel.Item> items, String fieldname, int keyword, int index) {
        if (index < items.size()) {
            // Process the current item and move to the next on completion
            updateAllData(items.get(index).getConPhoneId(), fieldname, "H-" + items.get(index).getHouse() +"/F" + keyword, index, success -> {
                // Proceed to the next item after the current update is complete
                updateItemsSequentially(items, fieldname, keyword, index + 1);
            });
        } else {
            // All updates are done
            UpdateFamilyCount(keyword);
        }
    }

    private void UpdateFamilyCount(int newCount) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<UpdateModel> call = apiService.updateFamilyCount(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                Integer.parseInt(Helper.WARD),
                (newCount+1)
        );

        progressDialog.show();
        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateModel apiResponse = response.body();
                    Toast.makeText(NewMobileActivity.this, "Consideration as family is completed!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewMobileActivity.this, "Failed here...!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(NewMobileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateAllData(String queryText, String fieldname, String changeValue, int position, PartSectionActivity.UpdateCallback callback) {
        progressDialog.setMessage(position + ". " + queryText + " changing to " + changeValue + "...");
        progressDialog.show();

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<UpdateModel> call = apiService.updateAllData(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                "con_phone_id",
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
                    Toast.makeText(NewMobileActivity.this, position + " updated", Toast.LENGTH_SHORT).show();
                    callback.onUpdateComplete(true); // Notify success
                } else {
                    Toast.makeText(NewMobileActivity.this, "Failed here...!", Toast.LENGTH_SHORT).show();
                    callback.onUpdateComplete(false); // Notify failure
                }
                Log.d("PartSecAd.this", "URL: " + call.request().url());
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("PartSecAd.this", "URL: " + call.request().url());
                Toast.makeText(NewMobileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                callback.onUpdateComplete(false); // Notify failure
            }
        });
    }

    public void RecordLocation(String note, String partNo, String slNo, String address){
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<UpdateModel> call = apiService.insertWorkRecord(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                Helper.USER_NAME,
                Helper.getCurrentAddress(NewMobileActivity.this, Helper.UPDATED_LOCATION),
                Helper.formatDateTime(new Date().getTime()),
                Helper.WARD,
                note,
                partNo,
                slNo,
                Helper.getTextBeforeParenthesis(address),
                Helper.UPDATED_DISTANCE
        );

        progressDialog.show();
        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                if (response.isSuccessful()) {
                    UpdateModel apiResponse = response.body();
//                    Toast.makeText(VoterDetails.this, apiResponse.getStatus()
//                            + " : " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(VoterDetails.this, "Failed here...!", Toast.LENGTH_SHORT).show();
                }
                Log.d("VoterDetails.this", "URL: " + call.request().url());
                Helper.UPDATED_DISTANCE = "0m";
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("VoterDetails.this", "URL: " + call.request().url());
                //Toast.makeText(VoterDetails.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void shareDialog(String phoneNumber, String slipText){
        DialogShareSlipBinding dialogBinding = DialogShareSlipBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());
        // Create and show the dialog
        AlertDialog dialog = builder.create();

        // For regular WhatsApp
        dialogBinding.whatsapp.setOnClickListener(v -> {
            // Format phone number for WhatsApp URL (example: "911234567890" for India)
            String formattedPhoneNumber = phoneNumber.replace("+", "").replace(" ", "");

            String url = "https://wa.me/" + Helper.phoneNoWithCountryCode(phoneNumber) + "?text=" + Uri.encode(slipText);

            // Create an intent to open WhatsApp using the URL
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse(url));

            try {
                startActivity(sendIntent);
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exception if WhatsApp is not installed or any other error
            }
        });

// For WhatsApp Business
        dialogBinding.businessWhatsapp.setOnClickListener(v -> {
            // Format phone number for WhatsApp Business URL
            String formattedPhoneNumber = phoneNumber.replace("+", "").replace(" ", "");

            // Create the wa.me URL with the phone number
            String url = "https://wa.me/" + Helper.phoneNoWithCountryCode(phoneNumber) + "?text=" + Uri.encode(slipText);

            // Create an intent to open WhatsApp Business using the URL
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse(url));

            // Start activity directly without contact selection
            try {
                startActivity(sendIntent);
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle exception if WhatsApp Business is not installed or any other error
            }
        });



        dialogBinding.sms.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:" + phoneNumber));
            sendIntent.putExtra("sms_body", slipText);
            startActivity(sendIntent);
        });

        dialogBinding.gmail.setVisibility(View.GONE);
        dialogBinding.gmailViewLine.setVisibility(View.GONE);
        dialogBinding.gmail.setOnClickListener(v->{
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("message/rfc822");
            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Voter Slip");
            sendIntent.putExtra(Intent.EXTRA_TEXT, slipText);
            try {
                startActivity(Intent.createChooser(sendIntent, "Send mail..."));
                dialog.dismiss();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        });

        dialogBinding.otherShare.setOnClickListener(v->{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, slipText);
            startActivity(Intent.createChooser(sendIntent, "Share Voter Slip Via..."));
            dialog.dismiss();
        });

        dialog.show();
    }

    private void editDialog() {
        DialogTextInputBinding dialogBinding = DialogTextInputBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());
        // Create and show the dialog
        AlertDialog dialog = builder.create();

        dialogBinding.textInputL.setInputType(InputType.TYPE_CLASS_PHONE);
        dialogBinding.btn.setText("Share slip");
        dialogBinding.printLayout.setVisibility(View.VISIBLE);

        dialogBinding.btn.setOnClickListener(v->{
            String text = dialogBinding.textInputL.getText().toString();
            if (text.isEmpty()){
                dialogBinding.textInputL.setError("*");
            }else {
                ArrayList<PhoneAddressModel.Item> items = adapter.getSelectedRows();
                RecordLocation("Slip SMS Delivered : Family (" + items.size() + ")",
                        items.get(0).getPartNo(), items.get(0).getSl_no(), items.get(0).getAddress());
                StringBuilder stringBuilder = new StringBuilder();
                for (int ij = 0; ij < items.size(); ij++){
                    if (ij == 0){
                        stringBuilder.append((ij+1)+".\n").append(Helper.slipText(items.get(ij), this));
                    }else {
                        stringBuilder.append("\n\n\n").append((ij+1)+".\n").append(Helper.slipText(items.get(ij), this));
                    }
                }
                //Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                shareDialog(text, stringBuilder.toString());

                dialog.dismiss();
            }
        });

        dialogBinding.btnPrint.setOnClickListener(v->{
            ArrayList<PhoneAddressModel.Item> items = adapter.getSelectedRows();
            RecordLocation("Printed : Family (" + items.size() + ")",
                    items.get(0).getPartNo(), items.get(0).getSl_no(), items.get(0).getAddress());
            StringBuilder stringBuilder = new StringBuilder();
            for (int ij = 0; ij < items.size(); ij++){
                progressDialog.setMessage("Slip no " + (ij+1) + " is printing in process...");
                progressDialog.show();
                if (dialogBinding.logo1.isChecked()){
                    if (ij == 0){
                        stringBuilder.append(Helper.slipText(items.get(ij), this));
                    }else {
                        stringBuilder.append("\n\n").append(Helper.slipText(items.get(ij), this));
                    }
                    Helper.printText(NewMobileActivity.this, stringBuilder.toString(), true, false);
                }if (dialogBinding.logo2.isChecked()){
                    if (ij == 0){
                        stringBuilder.append(Helper.slipText(items.get(ij), this));
                    }else {
                        stringBuilder.append("\n\n").append(Helper.slipText(items.get(ij), this));
                    }
                    Helper.printText(NewMobileActivity.this, stringBuilder.toString(), true, true);
                }else {
                    if (ij == 0){
                        stringBuilder.append(Helper.slipTextWithoutLogo(items.get(ij), this));
                    }else {
                        stringBuilder.append("\n\n").append(Helper.slipTextWithoutLogo(items.get(ij), this));
                    }
                    Helper.printText(NewMobileActivity.this, stringBuilder.toString(), false, false);
                }

                progressDialog.dismiss();
            }

            dialog.dismiss();
            progressDialog.dismiss();
        });

        dialogBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });

        dialog.show();
    }

    void getDetails(int nextToken) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loadMore.setVisibility(View.GONE);
        normalNextPage = true;

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<PhoneAddressModel> call;
        call = apiService.getNewPhone(
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
                        Toast.makeText(NewMobileActivity.this, "not found", Toast.LENGTH_SHORT).show();
                    }else {
                        if (adapter == null) {
                            adapter = new VoterAdapter(NewMobileActivity.this, apiResponse.getItem(), NewMobileActivity.this);
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
                    Toast.makeText(NewMobileActivity.this, "Failed...!", Toast.LENGTH_SHORT).show();
                }

                Log.d("SearchActivity.this", "URL: " + call.request().url());
            }

            @Override
            public void onFailure(Call<PhoneAddressModel> call, Throwable t) {
                Toast.makeText(NewMobileActivity.this, "Error 404...!", Toast.LENGTH_SHORT).show();
                Toast.makeText(NewMobileActivity.this, call.request().url().toString(), Toast.LENGTH_LONG).show();
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


    @Override
    public void onShowAction(Boolean isSelected) {
        if (isSelected){
            binding.shareSlip.setVisibility(View.VISIBLE);
            binding.selectedNo.setVisibility(View.VISIBLE);
            binding.btnFamily.setVisibility(View.VISIBLE);
            binding.selectedNo.setText(adapter.getSelectedRows().size() + " voters selected");
        }else {
            binding.shareSlip.setVisibility(View.GONE);
            binding.selectedNo.setVisibility(View.GONE);
            binding.btnFamily.setVisibility(View.GONE);
        }
    }
}