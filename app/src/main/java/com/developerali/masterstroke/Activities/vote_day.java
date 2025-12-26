package com.developerali.masterstroke.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerali.masterstroke.ApiModels.UpdateModel;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.Models.PartResponse;
import com.developerali.masterstroke.Models.VotingStatsResponse;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.databinding.ActivityVoteDayBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class vote_day extends AppCompatActivity {

    ActivityVoteDayBinding binding;
    ProgressDialog progressDialog;
    ApiService apiService;
    private ArrayAdapter<PartResponse.Item> adapter;
    private List<PartResponse.Item> partList;
    String selectedPart = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        binding = ActivityVoteDayBinding.inflate(getLayoutInflater());
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        progressDialog = new ProgressDialog(vote_day.this);
        progressDialog.setMessage("updating vote status");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Vote Count");

        binding.text0.setOnClickListener(v-> type(0, true));
        binding.text1.setOnClickListener(v-> type(1, true));
        binding.text2.setOnClickListener(v-> type(2, true));
        binding.text3.setOnClickListener(v-> type(3, true));
        binding.text4.setOnClickListener(v-> type(4, true));
        binding.text5.setOnClickListener(v-> type(5, true));
        binding.text6.setOnClickListener(v-> type(6, true));
        binding.text7.setOnClickListener(v-> type(7, true));
        binding.text8.setOnClickListener(v-> type(8, true));
        binding.text9.setOnClickListener(v-> type(9, true));
        binding.cross.setOnClickListener(v-> type(0, false));

        binding.btn.setOnClickListener(v->{
            String txt = binding.edTxt.getText().toString();
            if (txt.isEmpty()){
                binding.edTxt.setError("*");
            }else {
                binding.edTxt.setError(null);
                binding.edTxt.setText("");
                updateData(txt, "vote_done", "1", "");
            }
        });


        partList = new ArrayList<>();

        // Default item
        partList.add(new PartResponse.Item() {{
            setPart_no("Select Part No");
        }});

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, partList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);

        // Load data from API
        //loadVoterStats();
        loadPartNos();

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // Skip "Select Part No"
                    selectedPart = partList.get(position).getPart_no();
                    loadVoterStats();
                    //Toast.makeText(vote_day.this, "Selected: " + selectedPart, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        binding.oPLay.setOnClickListener(view -> {
            Intent i = new Intent(vote_day.this, SearchActivity.class);
            i.putExtra("party_interest", "Our Party");
            i.putExtra("partNo", selectedPart);
            startActivity(i);
        });

        binding.oPPLay.setOnClickListener(view -> {
            Intent i = new Intent(vote_day.this, SearchActivity.class);
            i.putExtra("party_interest", "Opposition Party");
            i.putExtra("partNo", selectedPart);
            startActivity(i);
        });

        binding.dLay.setOnClickListener(view -> {
            Intent i = new Intent(vote_day.this, SearchActivity.class);
            i.putExtra("party_interest", "Doubtful");
            i.putExtra("partNo", selectedPart);
            startActivity(i);
        });






    }

    private void loadVoterStats() {
        Call<VotingStatsResponse> call = apiService.getVoterStatus("fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                Helper.WARD, selectedPart);

        progressDialog.setMessage("loading polled status...");
        progressDialog.show();
        call.enqueue(new Callback<VotingStatsResponse>() {
            @Override
            public void onResponse(Call<VotingStatsResponse> call, Response<VotingStatsResponse> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().getStatus())) {
                    VotingStatsResponse.Stats stats = response.body().getStats();

                    // Update UI
                    binding.totalVoter.setText(stats.getTotal_voters());

                    String doneCount = stats.getVote_done().getTotal();
                    double donePerc = stats.getVote_done().getPercentage();
                    binding.pendingPoll.setText(doneCount);
                    binding.pendingPoll.setText(doneCount+"");
                    binding.percentage.setText("(" + donePerc + "%)");

                    // ---------- OUR PARTY ----------
                    int ourTotal = Integer.parseInt(stats.getOur_party().getTotal());
                    int ourPending = Integer.parseInt(stats.getOur_party().getPending());

                    double ourPendingPer = stats.getOur_party().getPercentage();

                    int ourPolled = ourTotal - ourPending;
                    double ourPolledPer = ourTotal > 0
                            ? (ourPolled * 100.0) / ourTotal
                            : 0;

                    binding.ourParty.setText(
                            ourPending + "\n\n" + String.format("%.1f", (100 - ourPolledPer)) + "%"
                    );



                    binding.ourParty1.setText(
                            ourPolled + "\n\n" + String.format("%.1f", ourPolledPer) + "%"
                    );


                    // ---------- OPPOSITION PARTY ----------
                    int opoTotal = Integer.parseInt(stats.getOpposition_party().getTotal());
                    int opoPending = Integer.parseInt(stats.getOpposition_party().getPending());

                    double opoPendingPer = stats.getOpposition_party().getPercentage();

                    int opoPolled = opoTotal - opoPending;
                    double opoPolledPer = opoTotal > 0
                            ? (opoPolled * 100.0) / opoTotal
                            : 0;

                    binding.oppositionParty.setText(
                            opoPending + "\n\n" + String.format("%.1f", (100 - opoPolledPer)) + "%"
                    );



                    binding.oppositionParty1.setText(
                            opoPolled + "\n\n" + String.format("%.1f", opoPolledPer) + "%"
                    );


                    // ---------- DOUBTFUL ----------
                    int douTotal = Integer.parseInt(stats.getDoubtful().getTotal());
                    int douPending = Integer.parseInt(stats.getDoubtful().getPending());

                    double douPendingPer = stats.getDoubtful().getPercentage();



                    int douPolled = douTotal - douPending;
                    double douPolledPer = douTotal > 0
                            ? (douPolled * 100.0) / douTotal
                            : 0;

                    binding.doubtfulParty.setText(
                            douPending + "\n\n" + String.format("%.1f", (100 - douPolledPer)) + "%"
                    );

                    binding.doubtfulParty1.setText(
                            douPolled + "\n\n" + String.format("%.1f", douPolledPer) + "%"
                    );


                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<VotingStatsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(vote_day.this, "Stats load failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void type(int num, boolean add){
        String txt = binding.edTxt.getText().toString();
        if (add) {
            if (txt.equalsIgnoreCase("0")) {
                binding.edTxt.setText(String.valueOf(num));
            }else {
                binding.edTxt.setText(txt + num);
            }
        }else {
            if (txt != null && !txt.isEmpty() && txt.length() > 1) {
                binding.edTxt.setText(txt.substring(0, (txt.length() - 1)));
            }else {
                binding.edTxt.setText("0");
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void loadPartNos() {
        Call<PartResponse> call = apiService.getPartNo(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                Helper.WARD
        );

        call.enqueue(new Callback<PartResponse>() {
            @Override
            public void onResponse(Call<PartResponse> call, Response<PartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if ("success".equals(response.body().getStatus())) {
                        partList.clear();
                        partList.add(new PartResponse.Item() {{
                            setPart_no("Select Part No");
                        }});
                        partList.addAll(response.body().getItem());
                        adapter.notifyDataSetChanged();
                        binding.spinner.setSelection(0);
                    } else {
                        Toast.makeText(vote_day.this, "API Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PartResponse> call, Throwable t) {
                Toast.makeText(vote_day.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateData(String con_phone_id, String fieldName, String value, String note){
        if (selectedPart == null){
            Toast.makeText(this, "Please select Booth", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.setMessage("updating data...");
        Call<UpdateModel> call = apiService.updateVoteDone(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                con_phone_id,
                selectedPart,
                fieldName,
                value,
                note
        );

        progressDialog.show();
        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UpdateModel apiResponse = response.body();

                    Toast.makeText(vote_day.this, apiResponse.getStatus()
                            + " : " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    loadVoterStats();
                } else {
                    Toast.makeText(vote_day.this, "Failed here...!", Toast.LENGTH_SHORT).show();
                }
                Log.d("VoterDetails.this", "URL: " + call.request().url());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("VoterDetails.this", "URL: " + call.request().url());
                Toast.makeText(vote_day.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}