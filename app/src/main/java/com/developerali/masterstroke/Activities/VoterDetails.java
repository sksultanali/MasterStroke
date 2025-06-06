package com.developerali.masterstroke.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.developerali.masterstroke.Adapters.myListAdapter;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiModels.UpdateModel;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.databinding.ActivityVoterDetailsBinding;
import com.developerali.masterstroke.databinding.DialogListSearchBinding;
import com.developerali.masterstroke.databinding.DialogShareSlipBinding;
import com.developerali.masterstroke.databinding.DialogTextInputBinding;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoterDetails extends AppCompatActivity {

    ActivityVoterDetailsBinding binding;
    PhoneAddressModel.Item  details;
    private static final int REQUEST_CALL_PERMISSION = 1;
    ProgressDialog progressDialog;
    String note;
    int ageBack;
    Animation blinkAnimation;
    int worksDone = 0;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVoterDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        Helper.WARD = sharedPreferences.getString("ward_id", "0");

        progressDialog = new ProgressDialog(VoterDetails.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("updating data...");

        Intent intent = getIntent();
        if (intent.hasExtra("details")){
           details = intent.getParcelableExtra("details");
        }

        if (details != null){
            binding.name.setText("Name : " + details.getName());
            if (details.getGname() != null){
                binding.gname.setText("Guardian Name : " + details.getGname());
            }else {
                binding.gname.setText("Guardian Name : NA");
            }
            binding.serialNo.setText("Sl No : "+ details.getSl_no());
            binding.voterId.setText("VoterId : " + details.getVoter_id());
            binding.partGenderAge.setText("Part : " + details.getPartNo() + " | Gender : " + details.getSex() + " | Age : " + details.getAge());
            binding.section.setText("Section : " + details.getSection());
            binding.houseNo.setText("House No : " + details.getHouse());
            binding.address.setText("Address : " + details.getAddress());
            binding.boothName.setText("Booth : " + details.getPollingStation());

            if (details.getMobile() != null && details.getMobile().length() > 9) {
                if (Helper.ADMIN_APPLICATION) {
                    binding.oldPhone.setText("Old Mobile No : " + details.getMobile());
                } else {
                    binding.oldPhone.setText("Old Mobile No : " + Helper.maskPhone(details.getMobile()));
                }
            }

            if (details.getIntereset_party() == null || details.getIntereset_party().isEmpty()){
                blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_animation);
                binding.surveyBtn.startAnimation(blinkAnimation);
            }

            if (details.getStat() == null || !details.getStat().equalsIgnoreCase("edited")){
                blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_animation);
                binding.scrutiny.startAnimation(blinkAnimation);
            }

            ageBack = Integer.parseInt(details.getAge());

            if (details.getNew_mobile() != null){
                if (Helper.ADMIN_APPLICATION){
                    binding.phone.setText("New Mobile No : "+ details.getNew_mobile());
                }else {
                    binding.phone.setText("New Mobile No : "+ Helper.maskPhone(details.getNew_mobile()));
                }
            }else {
                binding.phone.setText("New Mobile No : ");
            }


            if (details.getDoa() != null){
                binding.doa.setText("DOA : "+ details.getDoa());
            }else {
                binding.doa.setText("DOA :__/__/____");
            }

            if (details.getDob() != null){
                binding.dob.setText("DOB : "+ details.getDob());
            }else {
                binding.dob.setText("DOB :__/__/____");
            }

            if (details.getLanguage() != null && !details.getLanguage().isEmpty()){
                if (details.getLanguage().equalsIgnoreCase("Bengali")){
                    binding.bangla.setChecked(true);
                }else if (details.getLanguage().equalsIgnoreCase("Hindi")){
                    binding.hindi.setChecked(true);
                }else if (details.getLanguage().equalsIgnoreCase("Urdu")){
                    binding.none.setChecked(true);
                }else {
                    binding.bangla.setChecked(false);
                    binding.hindi.setChecked(false);
                    binding.none.setChecked(false);
                }
            }else {
                binding.none.setChecked(true);
            }

            if (details.getStatus() != null && !details.getStatus().isEmpty()){
                if (details.getStatus().equalsIgnoreCase("Relocated")){
                    binding.relocated.setChecked(true);
                } else if (details.getStatus().equalsIgnoreCase("Dead")) {
                    binding.dead.setChecked(true);
                }else if (details.getStatus().equalsIgnoreCase("Fake Voter")) {
                    binding.fakeVoter.setChecked(true);
                }else {
                    binding.present.setChecked(true);
                }
            }else {
                binding.present.setChecked(true);
            }

            if (details.getNote() != null){
                note = details.getNote();
            }else {
                note = " ";
            }

            if (details.getDob() != null && !details.getDob().isEmpty() && details.getDob().length() > 5){
                String today = details.getDob().substring(0, details.getDob().length()-5);
                if (today.equalsIgnoreCase(Helper.getTodayS())){
                    binding.wishBirthday.setVisibility(View.VISIBLE);
                }
            }else {
                binding.wishBirthday.setVisibility(View.GONE);
            }



            if (details.getDoa() != null && !details.getDoa().isEmpty() && details.getDoa().length() > 5){
                String today = details.getDoa().substring(0, details.getDoa().length()-5);
                if (today.equalsIgnoreCase(Helper.getTodayS())){
                    binding.wishAnni.setVisibility(View.VISIBLE);
                }
            }else {
                binding.wishAnni.setVisibility(View.GONE);
            }

            if (details.getHof() != null && !details.getHof().isEmpty()){
                if (details.getHof().equalsIgnoreCase("Yes")){
                    binding.fYes.setChecked(true);
                }else {
                    binding.fNo.setChecked(true);
                }
            }else {
                binding.fNo.setChecked(true);
            }

            if (details.getParty_worker() != null && !details.getParty_worker().isEmpty()){
                if (details.getParty_worker().equalsIgnoreCase("Yes")){
                    binding.wYes.setChecked(true);
                }else {
                    binding.wNo.setChecked(true);
                }
            }else {
                binding.wNo.setChecked(true);
            }

            if (Helper.ADMIN_APPLICATION){
                handleMobileNumbers(details);
            }
        }
        translatePage();

        binding.favourText.setText(Helper.getFavourText(VoterDetails.this));

//        binding.slip.setOnClickListener(v->{
//            if (mobileReturn(details) != null){
//                Helper.saveFavourText(binding.favourText.getText().toString(), VoterDetails.this);
//                shareDialog(mobileReturn(details));
//            }else {
//                Helper.showCustomMessage(VoterDetails.this, "Error 405",
//                        "Mobile number is not available for this voter. Please make sure voter has a valid mobile number.");
//            }
//        });

        binding.scrutiny.setOnClickListener(v->{
            //updateData(details.getConPhoneId(), "stat", "edited", note + "\n" + Helper.UPDATED_DISTANCE + "  |  From- " + Helper.getCurrentAddress(VoterDetails.this, Helper.UPDATED_LOCATION));
            updateData(details.getConPhoneId(), "stat", "edited", note + "  |  From- " + Helper.UPDATED_DISTANCE);
            RecordLocation("edited : " + details.getName());
            binding.scrutiny.setAnimation(null);
        });



        binding.share.setOnClickListener(v->{
            if (mobileReturn(details) != null){
                Helper.saveFavourText(binding.favourText.getText().toString(), VoterDetails.this);
                shareDialog(mobileReturn(details), " ");
            }else {
                Helper.showCustomMessage(VoterDetails.this, "Error 405",
                        "Mobile number is not available for this voter. Please make sure voter has a valid mobile number.");
            }
        });

        ArrayList<String> parties = Helper.getPartyList(VoterDetails.this);

        binding.surveyBtn.setOnClickListener(v->{
            if (parties == null){
                Helper.showCustomMessage(VoterDetails.this, "No Parties!",
                        "Party list is empty. Please add some parties before proceed");
            }else {
                surveyDialog("Select from below parties", parties, keyword->{
                    note = " _updated survey by " + Helper.USER_NAME;
                    worksDone++;
                    updateData(details.getConPhoneId(), "intereset_party", keyword, note);

                    updateData(details.getConPhoneId(), "update_date", Helper.formatDate(new Date().getTime()), note);
                    RecordLocation("survey : " + details.getName());
                    binding.surveyBtn.setAnimation(null);
                });
            }
        });
        binding.editBtn.setOnClickListener(v->{
            print();
            Helper.saveFavourText(binding.favourText.getText().toString(), VoterDetails.this);
            //Helper.printText(VoterDetails.this, Helper.slipText(details, VoterDetails.this));
        });
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.bangla){
                    note = " _updated language no by " + Helper.USER_NAME;
                    updateData(details.getConPhoneId(), "language", "Bengali", note);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.hindi) {
                    note = " _updated language no by " + Helper.USER_NAME;
                    updateData(details.getConPhoneId(), "language", "Hindi", note);
                } else {
                    note = " _updated language no by " + Helper.USER_NAME;
                    updateData(details.getConPhoneId(), "language", "Urdu", note);
                }

                worksDone++;
                updateData(details.getConPhoneId(), "update_date", Helper.formatDate(new Date().getTime()), note);
            }
        });

        binding.radioGroupP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.present){
                    note = " _updated relocated no by " + Helper.USER_NAME;
                    updateData(details.getConPhoneId(), "status", "Present", note);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.relocated) {
                    note = " _updated relocated no by " + Helper.USER_NAME;
                    updateData(details.getConPhoneId(), "status", "Relocated", note);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.fakeVoter) {
                    note = " _updated relocated no by " + Helper.USER_NAME;
                    updateData(details.getConPhoneId(), "status", "Fake Voter", note);
                }else {
                    note = " _updated relocated no by " + Helper.USER_NAME;
                    updateData(details.getConPhoneId(), "status", "Dead", note);
                }
                worksDone++;
                updateData(details.getConPhoneId(), "update_date", Helper.formatDate(new Date().getTime()), note);
            }
        });

        binding.radioGroupF.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.fYes){
                    note = " _updated HOF by " + Helper.USER_NAME;
                    updateData(details.getConPhoneId(), "hof", "Yes", note);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.fNo) {
                    note = " _updated HOF by " + Helper.USER_NAME;
                    updateData(details.getConPhoneId(), "hof", "No", note);
                }
                worksDone++;
                updateData(details.getConPhoneId(), "update_date", Helper.formatDate(new Date().getTime()), note);
            }
        });

        binding.radioGroupW.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.wYes){
                    note = " _updated party worker by " + Helper.USER_NAME;
                    updateData(details.getConPhoneId(), "party_worker", "Yes", note);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.wNo) {
                    note = " _updated party worker by " + Helper.USER_NAME;
                    updateData(details.getConPhoneId(), "party_worker", "No", note);
                }
                worksDone++;
                updateData(details.getConPhoneId(), "update_date", Helper.formatDate(new Date().getTime()), note);
            }
        });

        binding.phone.setOnClickListener(v->{
            editDialog();
        });

        binding.dob.setOnClickListener(v->{
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR) - ageBack;
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    VoterDetails.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year1, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());
                        String selectedDateText = "DOB : " + formattedDate;
                        note = " _updated DOB by " + Helper.USER_NAME;
                        updateData(details.getConPhoneId(), "dob", formattedDate, note);
                        updateData(details.getConPhoneId(), "update_date", Helper.formatDate(new Date().getTime()), note);
                        binding.dob.setText(selectedDateText);
                        worksDone++;
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        binding.doa.setOnClickListener(v->{
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    VoterDetails.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year1, monthOfYear, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());
                        String selectedDateText = "DOA : " + formattedDate;
                        note = " _updated DOA by " + Helper.USER_NAME;
                        updateData(details.getConPhoneId(), "doa", formattedDate, note);
                        updateData(details.getConPhoneId(), "update_date", Helper.formatDate(new Date().getTime()), note);
                        binding.doa.setText(selectedDateText);
                        worksDone++;
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        binding.wishAnni.setOnClickListener(v->{
            if (details.getMobile() != null && !details.getMobile().isEmpty()){
                if (binding.hindi.isChecked()){
                    wishDialog(details.getMobile(), Helper.wishAnniversary("hi", details.getName()));
                }else if (binding.bangla.isChecked()){
                    wishDialog(details.getMobile(), Helper.wishAnniversary("bn", details.getName()));
                }else {
                    wishDialog(details.getMobile(), Helper.wishAnniversary("en", details.getName()));
                }
            }else if (details.getNew_mobile() != null && !details.getNew_mobile().isEmpty()){
                if (binding.hindi.isChecked()){
                    wishDialog(details.getNew_mobile(), Helper.wishAnniversary("hi", details.getName()));
                }else if (binding.bangla.isChecked()){
                    wishDialog(details.getNew_mobile(), Helper.wishAnniversary("bn", details.getName()));
                }else {
                    wishDialog(details.getNew_mobile(), Helper.wishAnniversary("en", details.getName()));
                }
            }else {
                Helper.showCustomMessage(VoterDetails.this, "Error 405",
                        "Mobile number is not available for this voter. Please make sure voter has a valid mobile number.");
            }
        });

        binding.wishBirthday.setOnClickListener(v->{
            if (details.getMobile() != null && !details.getMobile().isEmpty()){
                if (binding.hindi.isChecked()){
                    wishDialog(details.getMobile(), Helper.wishBirthday("hi", details.getName()));
                }else if (binding.bangla.isChecked()){
                    wishDialog(details.getMobile(), Helper.wishBirthday("bn", details.getName()));
                }else {
                    wishDialog(details.getMobile(), Helper.wishBirthday("en", details.getName()));
                }
            }else if (details.getNew_mobile() != null && !details.getNew_mobile().isEmpty()){
                if (binding.hindi.isChecked()){
                    wishDialog(details.getNew_mobile(), Helper.wishBirthday("hi", details.getName()));
                }else if (binding.bangla.isChecked()){
                    wishDialog(details.getNew_mobile(), Helper.wishBirthday("bn", details.getName()));
                }else {
                    wishDialog(details.getNew_mobile(), Helper.wishBirthday("en", details.getName()));
                }
            }else {
                Helper.showCustomMessage(VoterDetails.this, "Error 405",
                        "Mobile number is not available for this voter. Please make sure voter has a valid mobile number.");
            }
        });





        try {
            getLatLongFromAddress(Helper.getTextBeforeParenthesis(details.getAddress()));
        }catch (Exception e){

        }



    }

    private void getLatLongFromAddress(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            // Get a list of addresses (usually one) for the provided address string
            List<Address> addresses = geocoder.getFromLocationName(address, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                //Toast.makeText(this, latitude + "  +  " + longitude , Toast.LENGTH_SHORT).show();

                LatLng start = new LatLng(latitude, longitude);
                LatLng end = new LatLng(Helper.UPDATED_LOCATION.getLatitude(), Helper.UPDATED_LOCATION.getLongitude());

                Helper.UPDATED_DISTANCE = Helper.formattedHaversineDistance(start, end);


                Toast.makeText(this, Helper.UPDATED_DISTANCE, Toast.LENGTH_SHORT).show();
                Log.d("Geocoder", "Address: " + address + "\nLatitude: " + latitude + "\nLongitude: " + longitude);
                // You can use latitude and longitude as needed in your app
            } else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                Log.e("Geocoder", "No location found for address: " + address);
            }
        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Geocoder", "Geocoding error", e);
        }
    }

    private void translatePage() {
        Helper.translateText(VoterDetails.this, binding.name.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                binding.name.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });
        Helper.translateText(VoterDetails.this, binding.partGenderAge.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                binding.partGenderAge.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });
        Helper.translateText(VoterDetails.this, binding.address.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                binding.address.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });
        Helper.translateText(VoterDetails.this, binding.boothName.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                binding.boothName.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });
        Helper.translateText(VoterDetails.this, binding.gname.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                binding.gname.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });
        Helper.translateText(VoterDetails.this, binding.serialNo.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                binding.serialNo.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });
        Helper.translateText(VoterDetails.this, binding.houseNo.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                binding.houseNo.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });
        Helper.translateText(VoterDetails.this, binding.section.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                binding.section.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });
        Helper.translateText(VoterDetails.this, binding.oldPhone.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                binding.oldPhone.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });
        Helper.translateText(VoterDetails.this, binding.doa.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                binding.doa.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });
        Helper.translateText(VoterDetails.this, binding.dob.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                binding.dob.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });
        Helper.translateText(VoterDetails.this, binding.phone.getText().toString(), new Helper.TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                binding.phone.setText(translatedText);
            }

            @Override
            public void onTranslationFailure(String errorText) {

            }
        });
    }

    public void updateData(String con_phone_id, String fieldName, String value, String note){
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<UpdateModel> call = apiService.UpdateVoter(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                con_phone_id,
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

                    Toast.makeText(VoterDetails.this, apiResponse.getStatus()
                            + " : " + apiResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    if (fieldName.equalsIgnoreCase("hof") && value.equalsIgnoreCase("Yes")){
                        if (details.getMobile() != null && !details.getMobile().isEmpty()){
                            if (binding.hindi.isChecked()){
                                wishDialog(details.getMobile(), Helper.wishToHof("hi", details.getName()));
                            }else if (binding.bangla.isChecked()){
                                wishDialog(details.getMobile(), Helper.wishToHof("bn", details.getName()));
                            }else {
                                wishDialog(details.getMobile(), Helper.wishToHof("en", details.getName()));
                            }
                        }else if (details.getNew_mobile() != null && !details.getNew_mobile().isEmpty()){
                            if (binding.hindi.isChecked()){
                                wishDialog(details.getNew_mobile(), Helper.wishToHof("hi", details.getName()));
                            }else if (binding.bangla.isChecked()){
                                wishDialog(details.getNew_mobile(), Helper.wishToHof("bn", details.getName()));
                            }else {
                                wishDialog(details.getNew_mobile(), Helper.wishToHof("en", details.getName()));
                            }
                        }else {
                            Helper.showCustomMessage(VoterDetails.this, "Error 405",
                                    "Mobile number is not available for this voter. Please make sure voter has a valid mobile number.");
                        };
                    }

                } else {
                    Toast.makeText(VoterDetails.this, "Failed here...!", Toast.LENGTH_SHORT).show();
                }
                Log.d("VoterDetails.this", "URL: " + call.request().url());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("VoterDetails.this", "URL: " + call.request().url());
                Toast.makeText(VoterDetails.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void RecordLocation(String note){
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<UpdateModel> call = apiService.insertWorkRecord(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                Helper.USER_NAME,
                Helper.getCurrentAddress(VoterDetails.this, Helper.UPDATED_LOCATION),
                Helper.formatDateTime(new Date().getTime()),
                Helper.WARD,
                note,
                details.getPartNo(),
                details.getSl_no(),
                String.valueOf(worksDone),
                details.getHouse() + ", " +Helper.getTextBeforeParenthesis(details.getAddress()),
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

    public void print(){
        DialogTextInputBinding dialogBinding = DialogTextInputBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());
        // Create and show the dialog
        AlertDialog dialog = builder.create();

        dialogBinding.otherLayout.setVisibility(View.GONE);
        dialogBinding.printLayout.setVisibility(View.VISIBLE);


        dialogBinding.btnPrint.setOnClickListener(v->{
            if (dialogBinding.logo1.isChecked()){
                Helper.printText(VoterDetails.this, Helper.slipText(details, VoterDetails.this), true, false);
            }else if (dialogBinding.logo2.isChecked()){
                Helper.printText(VoterDetails.this, Helper.slipText(details, VoterDetails.this), true, true);
            }else {
                Helper.printText(VoterDetails.this, Helper.slipTextWithoutLogo(details, VoterDetails.this), false, false);
            }
            RecordLocation("printed : " + details.getName());
            dialog.dismiss();
        });

        dialogBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
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
        dialogBinding.btn.setOnClickListener(v->{
            String text = dialogBinding.textInputL.getText().toString();
            if (text.isEmpty()){
                dialogBinding.textInputL.setError("*");
            }else {
                note = " _updated phone no by " + Helper.USER_NAME;
                worksDone++;
                updateData(details.getConPhoneId(), "new_mobile", text, note);
                binding.phone.setText("New Mobile No : " + text);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void surveyDialog(String searchOn,ArrayList<String> arrayListChoose, ListActivity.DialogCallback callback) {
        DialogListSearchBinding dialogBinding = DialogListSearchBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialogBinding.searchOn.setText(searchOn);

        myListAdapter adapter = new myListAdapter(VoterDetails.this, arrayListChoose, false);
        dialogBinding.chooseList.setAdapter(adapter);

        dialogBinding.chooseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog.dismiss();
                callback.onResult(arrayListChoose.get(i));
            }
        });

        dialog.show();
    }

    public void wishDialog(String phoneNumber, String slipText){
        DialogShareSlipBinding dialogBinding = DialogShareSlipBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());
        // Create and show the dialog
        AlertDialog dialog = builder.create();

        dialogBinding.whatsapp.setOnClickListener(v -> {
            String url = "https://wa.me/" + Helper.phoneNoWithCountryCode(phoneNumber) + "?text=" + Uri.encode(slipText);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse(url));
            try {
                startActivity(sendIntent);
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        dialogBinding.businessWhatsapp.setOnClickListener(v -> {
            String url = "https://wa.me/" + Helper.phoneNoWithCountryCode(phoneNumber) + "?text=" + Uri.encode(slipText);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse(url));
            try {
                startActivity(sendIntent);
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
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
        });

        dialog.show();
    }


    public void shareDialog(String phoneNumber, String text){
        DialogShareSlipBinding dialogBinding = DialogShareSlipBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogBinding.getRoot());
        // Create and show the dialog
        AlertDialog dialog = builder.create();

        dialogBinding.whatsapp.setOnClickListener(v -> {
            String url = "https://wa.me/" + Helper.phoneNoWithCountryCode(phoneNumber) + "?text=" + Uri.encode(text);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse(url));
            try {
                startActivity(sendIntent);
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        dialogBinding.businessWhatsapp.setOnClickListener(v -> {
            String url = "https://wa.me/" + Helper.phoneNoWithCountryCode(phoneNumber) + "?text=" + Uri.encode(text);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse(url));
            try {
                startActivity(sendIntent);
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        dialogBinding.sms.setOnClickListener(v -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:" + phoneNumber));
            sendIntent.putExtra("sms_body", text);
            startActivity(sendIntent);
            dialog.dismiss();
        });

        dialogBinding.gmail.setOnClickListener(v->{
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("message/rfc822");
            sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Voter Slip");
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
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
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(sendIntent, "Share Voter Slip Via..."));
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.child_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.childMenu){
            Intent i = new Intent(VoterDetails.this, ChildOrNewVoter.class);
            i.putExtra("details", details);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleMobileNumbers(PhoneAddressModel.Item details) {
        String mobile = details.getMobile();
        String newMobile = details.getNew_mobile();

        if (mobile != null && mobile.length() > 9) {
            setUpCallButton(mobile);
        } else if (newMobile != null && newMobile.length() > 9) {
            setUpCallButton(newMobile);
        } else {
            binding.call.setVisibility(View.GONE);
        }
    }

    public String mobileReturn(PhoneAddressModel.Item details){
        String mobile = details.getMobile();
        String newMobile = details.getNew_mobile();

        if (mobile != null && mobile.length() > 9) {
            return mobile;
        } else if (newMobile != null && newMobile.length() > 9) {
            return newMobile;
        } else {
            return null;
        }
    }

    private void setUpCallButton(final String phoneNumber) {
        if (Helper.ADMIN_APPLICATION){
            binding.call.setVisibility(View.VISIBLE);
        }else {
            binding.call.setVisibility(View.GONE);
        }
        binding.call.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request permission if not granted
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
            } else {
                // Make the call if permission is already granted
                makeCall(phoneNumber);
            }
        });
    }

    private void makeCall(String phoneNumber) {
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            startActivity(phoneIntent);
        } else {
            Toast.makeText(this, "Permission required to make a call", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, reattempt the call
                // You can store the phone number somewhere and make the call
            } else {
                Toast.makeText(this, "Permission denied to make a call", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}