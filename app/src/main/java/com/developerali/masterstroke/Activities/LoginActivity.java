package com.developerali.masterstroke.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developerali.masterstroke.ApiModels.LoginModel;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.MainActivity;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    LoginModel apiResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Login");


        binding.btnLog.setOnClickListener(v->{
            String username = binding.emailBox.getText().toString();
            String password = binding.passwordBox.getText().toString();

            if (username.isEmpty()){
                binding.emailBox.setError("*");
            } else if (password.isEmpty()) {
                binding.passwordBox.setError("*");
            } else {
                getDetails(username, password);
            }
        });




    }

    private void getDetails(String userName, String password) {
        binding.progressBar.setVisibility(View.VISIBLE);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<LoginModel> call = apiService.getLoginCredentials(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                userName
        );

        call.enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    apiResponse = response.body();

                    if (apiResponse.getItem() != null){
                        if (!apiResponse.getItem().isEmpty()){
                            if (apiResponse.getItem().get(0).getPassword().equalsIgnoreCase(password)){
                                LoginModel.Item userDetails = apiResponse.getItem().get(0);
                                Helper.accountDetails(userDetails.getUsername(), userDetails.getName(), userDetails.getPhone(),
                                        userDetails.getPassword(), userDetails.getUserId(), userDetails.getWard_id(),
                                        userDetails.getSplash_link(), userDetails.getHome_link(), userDetails.getParty_suggest(),
                                        userDetails.getCandidate_name(), LoginActivity.this);
//                                if (Helper.getUserLogin(LoginActivity.this)){
//                                    if (Helper.getLanguagePreference(LoginActivity.this).equalsIgnoreCase("null")){
//                                        startActivity(new Intent(LoginActivity.this, TranslationPage.class));
//                                    }else {
//                                        startActivity(new Intent(LoginActivity.this, SplashScree.class));
//
//                                    }
//                                    finish();
//                                }
                                startActivity(new Intent(LoginActivity.this, SplashScree.class));
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, "wrong password..!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(LoginActivity.this, "No user found", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, apiResponse.getStatus(), Toast.LENGTH_SHORT).show();
                    }

                    binding.progressBar.setVisibility(View.GONE);

                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Failed...!", Toast.LENGTH_SHORT).show();
                }
                Log.d("LoginActivity.this", "URL: " + call.request().url());
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Log.d("LoginActivity.this", "URL: " + call.request().url());
                Toast.makeText(LoginActivity.this, "Error 404...!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
//        if (Helper.getUserLogin(LoginActivity.this)){
//            if (Helper.getLanguagePreference(LoginActivity.this).equalsIgnoreCase("null")){
//                startActivity(new Intent(LoginActivity.this, TranslationPage.class));
//            }else {
//                startActivity(new Intent(LoginActivity.this, SplashScree.class));
//            }
//        finish();
//        }

        startActivity(new Intent(LoginActivity.this, SplashScree.class));
        finish();
        super.onStart();
    }
}