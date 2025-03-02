package com.developerali.masterstroke.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.UpdateModel;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.databinding.ActivityChildBinding;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildActivity extends AppCompatActivity {

    ActivityChildBinding binding;
    PhoneAddressModel.Item  details;
    ProgressDialog progressDialog;
    String type, childClass, genderTxt, typeTxt;
    ArrayList<String> classes = new ArrayList<>();
    String[] gender = {"Male", "Female", "Other"};
    String[] types = {"Student", "Upcoming Voter"};
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChildBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.add_people);

        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        Helper.WARD = sharedPreferences.getString("ward_id", "0");

        progressDialog = new ProgressDialog(ChildActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("updating data...");

        calendar = Calendar.getInstance();

        Intent intent = getIntent();
        if (intent.hasExtra("details")){
            details = intent.getParcelableExtra("details");
        }

        classes.clear();
        int year = calendar.getWeekYear();
        //year -= 20;
        for (int i = 0; i <= 50; i++){
            classes.add(String.valueOf(year));
            year++;
        }

        ArrayAdapter<String> obj2 = new ArrayAdapter<String>(ChildActivity.this, R.layout.layout_spinner_item, classes);
        binding.spinnerSem.setAdapter(obj2);
        childClass = classes.get(0);

        binding.spinnerSem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                childClass = classes.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> obj = new ArrayAdapter<String>(ChildActivity.this, R.layout.layout_spinner_item, gender);
        binding.spinnerGen.setAdapter(obj);
        genderTxt = gender[0];

        binding.spinnerGen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderTxt = gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> obj3 = new ArrayAdapter<String>(ChildActivity.this, R.layout.layout_spinner_item, types);
        binding.spinnerS.setAdapter(obj3);
        typeTxt = types[0];

        binding.spinnerS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeTxt = types[position];
                getSupportActionBar().setTitle("Add " + typeTxt);
                if (position == 1){
                    binding.classLabel.setVisibility(View.GONE);
                    binding.classContainer.setVisibility(View.GONE);
                    childClass = null;
                }else {
                    binding.classLabel.setVisibility(View.VISIBLE);
                    binding.classContainer.setVisibility(View.VISIBLE);
                    childClass = classes.get(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.selectDate.setOnClickListener(v->{
            DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    binding.selectDate.setText(Helper.formatDate(calendar.getTime()));
                }
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ChildActivity.this,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            //datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.show();
        });



        binding.addDetails.setOnClickListener(v->{
            String fName = binding.fname.getText().toString();
            String lName = binding.lname.getText().toString();
            String Dob = binding.selectDate.getText().toString();

            if (fName.isEmpty()){
                binding.fname.setError("*");
            }else if (lName.isEmpty()){
                binding.lname.setError("*");
            }else if (Dob.equalsIgnoreCase("DD/MM/YYYY")){
                binding.selectDate.setError("*");
            }else {
                String phone = binding.mobileBox.getText().toString();

//                Toast.makeText(this, details.getConPhoneId(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, details.getConstitutionId(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, fName, Toast.LENGTH_SHORT).show();

//
//                WardWiseChildVoters wardWiseChild = new WardWiseChildVoters();
//
//                wardWiseChild.setConPhoneId(details.getConPhoneId());
//                wardWiseChild.setConstitutionId(details.getConstitutionId());
//                wardWiseChild.setPartNo(details.getPartNo());
//                wardWiseChild.setSection(details.getSection());
//                wardWiseChild.setName(name);
//                wardWiseChild.setAddress(details.getAddress());
//                wardWiseChild.setPollingStation(details.getPollingStation());
//                wardWiseChild.setSex(genderTxt);
//                wardWiseChild.setHouse(details.getHouse());
//                wardWiseChild.setWard(details.getWard());
//                wardWiseChild.setLname(details.getLname());
//                wardWiseChild.setDob(Dob);
//
//                if (!phone.isEmpty()){
//                    wardWiseChild.setMobile(phone);
//                }
//
//
//                // Make the API call
//                ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
//                Call<UpdateModel> call = apiService.insertWardWiseChild(
//                        Helper.API_TOKEN,
//                        wardWiseChild);
//
//                call.enqueue(new Callback<UpdateModel>() {
//                    @Override
//                    public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            String status = response.body().getStatus();
//                            String message = response.body().getMessage();
//                            Toast.makeText(ChildActivity.this, "Status: " + status + ", Message: " + message, Toast.LENGTH_LONG).show();
//                        } else {
//                            Toast.makeText(ChildActivity.this, "Response failed", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UpdateModel> call, Throwable t) {
//                        Toast.makeText(ChildActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });

                ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
//                WardWiseChildVoters wardWiseChild = new WardWiseChildVoters(
//                        "conPhoneId", "constitutionId", "partNo", "section",
//                        "name", "address", "religion", "pollingStation",
//                        "mobile", "sex", "house", "ward", "language",
//                        "lname", "dob", "type", "class"
//                );

                Call<UpdateModel> call = apiService.insertWardWiseChild(
                        "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                        details.getConPhoneId(), details.getConstitutionId(), details.getPartNo(), details.getSection(),
                        fName, details.getAddress(), details.getReligion(), details.getPollingStation(),
                        genderTxt, details.getHouse(), details.getWard(), details.getLanguage(), lName,
                        Dob, phone, typeTxt, childClass);

                call.enqueue(new Callback<UpdateModel>() {
                    @Override
                    public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                String responseBody = response.body().getMessage();
                                // Handle JSON response here (Convert to JSON if required)
                                binding.fname.setText("");
                                binding.lname.setText("");
                                binding.mobileBox.setText("");

                                Toast.makeText(ChildActivity.this, "Response: " + responseBody, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ChildActivity.this, "Response failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateModel> call, Throwable t) {
                        Toast.makeText(ChildActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }

        });







    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.addSomething){
//
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.add, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}