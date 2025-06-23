package com.developerali.masterstroke.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developerali.masterstroke.ApiModels.ApiResponse;
import com.developerali.masterstroke.ApiModels.UpdateModel;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.Helpers.FingerprintHelper;
import com.developerali.masterstroke.Helpers.Helper;
import com.developerali.masterstroke.MainActivity;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.databinding.ActivityStatPageBinding;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatPageActivity extends AppCompatActivity {

    ActivityStatPageBinding binding;
    private FingerprintHelper fingerprintHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE);
        Helper.WARD = sharedPreferences.getString("ward_id", "0");

        //Toast.makeText(this, Helper.WARD, Toast.LENGTH_SHORT).show();
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<ApiResponse> call = apiService.getAllCounts(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                Helper.WARD
        );

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    //Log.d("API_ERROR", "Status: " + apiResponse.getStatus());

                    if (apiResponse.getStatus().equalsIgnoreCase("success")) {
//                        Log.d("API_ERROR", "Edited Count: " + apiResponse.getEdited_count());
//                        Log.d("API_ERROR", "Printed Count: " + apiResponse.getPrinted_count());
//                        Log.d("API_ERROR", "Birthday Count: " + apiResponse.getToday_birthday_with_mobile());
//                        Log.d("API_ERROR", "New Mobile Count: " + apiResponse.getNew_mobile_count());
//
//                        Toast.makeText(StatPageActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                        Helper.startCounter(apiResponse.getEdited_count(), binding.editNo, "", "+");
                        Helper.startCounter(apiResponse.getPrinted_count(), binding.slipNo, "", "+");
                        Helper.startCounter(apiResponse.getToday_birthday_with_mobile(), binding.birthNo, "", "+");
                        Helper.startCounter(apiResponse.getNew_mobile_count(), binding.phoneNo, "", "+");

                    }

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                //Log.e("API_ERROR", "Network Error: " + t.getLocalizedMessage(), t);
                //Toast.makeText(StatPageActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        binding.nextBtn.setOnClickListener(v->{
            startActivity(new Intent(StatPageActivity.this, MainActivity.class));
        });

        binding.phoneLayout.setOnClickListener(v->{
            Intent i = new Intent(StatPageActivity.this, SearchActivity.class);
            i.putExtra("keyword", "");
            i.putExtra("searchOn", "mobile");
            startActivity(i);
        });
        binding.meetLayout.setOnClickListener(v->{
            startActivity(new Intent(StatPageActivity.this, NotesActivity.class));
        });
        binding.birthLayout.setOnClickListener(v->{
            Intent i = new Intent(StatPageActivity.this, SearchActivity.class);
            i.putExtra("keyword", Helper.getTodayS());
            i.putExtra("searchOn", "dob");
            startActivity(i);
        });
        binding.editLayout.setOnClickListener(v->{
            Intent i = new Intent(StatPageActivity.this, CounterActivity.class);
            i.putExtra("msg", "edited");
            startActivity(i);
        });
        binding.slipLayout.setOnClickListener(v->{
            Intent i = new Intent(StatPageActivity.this, CounterActivity.class);
            i.putExtra("msg", "printed");
            startActivity(i);
        });


        fingerprintHelper = new FingerprintHelper(this);

//        binding.btnAuthFail.setOnClickListener(v -> {
//            // Implement your fallback authentication method here
//            showFallbackAuthentication();
//        });

        if (fingerprintHelper.isFingerprintAvailable()) {
            authenticateWithFingerprint();
        } else {
            showFallbackAuthentication();
        }




    }

    private void authenticateWithFingerprint() {
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // User clicked on negative button (fallback)
                    showFallbackAuthentication();
                } else {
                    //Toast.makeText(StatPageActivity.this, "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //Toast.makeText(StatPageActivity.this, "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                // Proceed with your app's logic after successful authentication
                startAuthenticatedActivity();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                //Toast.makeText(StatPageActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        };

        fingerprintHelper.authenticate(this, callback);
    }

    private void showFallbackAuthentication() {
        // Implement your fallback authentication method here
        // For example: PIN, password, or pattern authentication
        //Toast.makeText(this, "Using fallback authentication method", Toast.LENGTH_SHORT).show();

        // Example: Show a dialog for PIN entry
        // You would typically implement your own PIN/Password dialog here
        startAuthenticatedActivity(); // For demo purposes, we just proceed
    }

    private void startAuthenticatedActivity() {
        // Start your main authenticated activity here
        // Intent intent = new Intent(this, AuthenticatedActivity.class);
        // startActivity(intent);
        // finish();

        // For demo, just show a success message
        //Toast.makeText(this, "Authentication successful! Proceeding to app...", Toast.LENGTH_SHORT).show();
    }

    private void openSecuritySettings() {
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fingerprintHelper.cancelAuthentication();
    }

}