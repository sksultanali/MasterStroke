package com.developerali.masterstroke.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blikoon.qrcodescanner.QrCodeActivity;
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
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int CAMERA_PERMISSION_CODE = 100;

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

        binding.btnQr.setOnClickListener(v->{
            checkCameraPermission();
        });



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            if (validateInput(result).contains("Error")){
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Mismatch QR");
                alertDialog.setMessage(result);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
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

                                if (userDetails.getSuspend().equalsIgnoreCase("yes")){
                                    Toast.makeText(LoginActivity.this, "Account Suspended..!", Toast.LENGTH_LONG).show();
                                }else {
                                    Helper.accountDetails(userDetails.getUsername(), userDetails.getName(), userDetails.getPhone(),
                                            userDetails.getPassword(), userDetails.getUserId(), userDetails.getWard_id(),
                                            userDetails.getSplash_link(), userDetails.getHome_link(), userDetails.getParty_suggest(),
                                            userDetails.getCandidate_name(), LoginActivity.this);

//                                }
                                    startActivity(new Intent(LoginActivity.this, SplashScree.class));
                                    finish();
                                }
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

    private void checkCameraPermission() {
        // Check if the Camera permission is already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(LoginActivity.this, QrCodeActivity.class);
            startActivityForResult( i,REQUEST_CODE_QR_SCAN);
        } else {
            // Request Camera permission
            requestCameraPermission();
        }
    }

    public String validateInput(String input) {
        String expectedPrefix = "i.groundreport.master_Stroke/worker_login/";

        try {
            if (!input.startsWith(expectedPrefix)) {
                return "Error: Input format is invalid. Prefix mismatch.";
            }
            String[] parts = input.substring(expectedPrefix.length()).split("/");
            if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
                return "Error: Input format is invalid. Missing username or password.";
            }
            String username = parts[0];
            String password = parts[1];
            //return "Username: " + username + "\nPassword: " + password;
            getDetails(username, password);

            return "Success";
        } catch (Exception e) {
            return "Error: An unexpected error occurred. " + e.getMessage();
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Camera permission is required to use the camera.", Toast.LENGTH_LONG).show();
        }

        // Request the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    // Handle the user's response to the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(LoginActivity.this, QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);
            } else {
                // Permission denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onStart() {
        if (Helper.getUserLogin(LoginActivity.this)){
            startActivity(new Intent(LoginActivity.this, SplashScree.class));
            finish();
        }
        super.onStart();
    }
}