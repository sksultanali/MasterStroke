package com.developerali.masterstroke.Helpers;
import android.content.Context;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class FingerprintHelper {

    private Context context;
    private BiometricPrompt biometricPrompt;
    private CancellationSignal cancellationSignal;
    private Executor executor;

    public FingerprintHelper(Context context) {
        this.context = context;
        this.executor = ContextCompat.getMainExecutor(context);
    }

    public boolean isFingerprintAvailable() {
        BiometricManager biometricManager = BiometricManager.from(context);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                return true;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(context, "No biometric features available on this device", Toast.LENGTH_SHORT).show();
                return false;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(context, "Biometric features are currently unavailable", Toast.LENGTH_SHORT).show();
                return false;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(context, "No fingerprints enrolled", Toast.LENGTH_SHORT).show();
                return false;
            default:
                return false;
        }
    }

    public void authenticate(FragmentActivity activity, BiometricPrompt.AuthenticationCallback callback) {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authenticate with fingerprint")
                .setSubtitle("Place your finger on the sensor")
                .setDescription("Use your fingerprint to authenticate")
                .setNegativeButtonText("Use PIN instead")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
                .build();

        biometricPrompt = new BiometricPrompt(activity, executor, callback);
        cancellationSignal = new CancellationSignal();
        biometricPrompt.authenticate(promptInfo);
    }

    public void cancelAuthentication() {
        if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
            cancellationSignal.cancel();
        }
    }
}
