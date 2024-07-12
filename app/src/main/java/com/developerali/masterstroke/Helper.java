package com.developerali.masterstroke;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.developerali.masterstroke.databinding.CustomDialogBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Helper {

    public static String API_TOKEN = "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d";
    public static String USER_ID;
    public static String ACTIVE_STATUS;
    public static String START_DATE;
    public static String END_DATE;
    public static boolean DELETE;
    public static int PresentDays;
    public static boolean EDIT;
    public static boolean MANAGER;
    public static boolean EMPLOYEE;
    public static String formatDate (Long date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd LLL yyyy");
        return simpleDateFormat.format(date);
    }

    public static String formatTime (Long date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        return simpleDateFormat.format(date);
    }
    public static String formatTime (Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        return simpleDateFormat.format(date);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd LLL yyyy");
        return dateFormat.format(date);
    }

    public static String calculatePercentage(int target, int achievement) {
        // Check if target is zero to avoid division by zero
        if (target == 0) {
            return "Target cannot be zero";
        }
        double percentage = ((double) achievement / target) * 100;
        return String.format("%.2f%%", percentage);
    }

    public static String convertDate(String dateString) {
        // Define the input format
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        // Define the output format
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        Date date;
        String formattedDate = "";

        try {
            // Parse the input date string into a Date object
            date = inputFormat.parse(dateString);
            // Format the Date object into the desired output format
            formattedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }
    public static String formatDateByMonth(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy");
        return dateFormat.format(date);
    }
    public static String formatDateByWeek(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM, yyyy");
        return dateFormat.format(date);
    }
    public static String formatDateTime (Long date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd LLL yyyy hh:mm a");
        return simpleDateFormat.format(date);
    }

    public static String getDateKey(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd");
        String date = simpleDateFormat.format(new Date());
        return date;
    }
    public static String getDateKey(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd");
        return simpleDateFormat.format(date);
    }


    public static String getDateKey(Long date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd");
        return simpleDateFormat.format(date);
    }

    public static String getFirstDateOfMonth(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMM");
        String d = dateFormat.format(date) + "01";
        return d;
    }
    public static String getFirstDateOfMonth(Long date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMM");
        return simpleDateFormat.format(date)+"01";
    }
    public static String getLastDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return Helper.getDateKey(calendar.getTime());
    }

    public static Date getAddLessFromDate(long currentTimeMillis, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMillis);
        calendar.add(Calendar.DAY_OF_YEAR, num); // Subtract 7 days
        return calendar.getTime();
    }

    public static String getFormattedDuration(long startTime, long endTime) {
        // Ensure that endTime is after startTime
        if (endTime < startTime) {
            return "NA";
        }
        long durationInMillis = endTime - startTime;
        long seconds = durationInMillis / 1000 % 60;
        long minutes = durationInMillis / (1000 * 60) % 60;
        long hours = durationInMillis / (1000 * 60 * 60) % 24;
        long days = durationInMillis / (1000 * 60 * 60 * 24);

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(" day").append(days > 1 ? "s " : " ");
        }
        if (hours > 0) {
            sb.append(hours).append(" hour").append(hours > 1 ? "s " : " ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(" min").append(minutes > 1 ? "s " : " ");
        }
        if (seconds > 0 || sb.length() == 0) { // include seconds if it's the only measurement or add it if there are other non-zero values
            sb.append(seconds).append(" sec").append(seconds > 1 ? "s" : "");
        }

        return sb.toString().trim();
    }

    public static void changeSem(String id, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Ids", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.apply();
    }
    public static String getSem(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("Ids", Context.MODE_PRIVATE);
        String id = sharedPreferences.getString("id", null);
        return id;
    }

    public static boolean isChromeCustomTabsSupported(@NonNull final Context context) {
        Intent serviceIntent = new Intent("android.support.customtabs.action.CustomTabsService");
        serviceIntent.setPackage("com.android.chrome");
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentServices(serviceIntent, 0);
        return !resolveInfos.isEmpty();
    }
    public static void openChromeTab(String link, Activity activity){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(activity, R.color.purple));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(activity, Uri.parse(link));
    }


    @SuppressLint("ResourceAsColor")
    public static void showLoginDialog(Activity activity, String title) {
        CustomDialogBinding dialogBinding = CustomDialogBinding.inflate(LayoutInflater.from(activity));

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogBinding.getRoot())
                .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

        dialogBinding.titleText.setText(title);
        dialogBinding.messageText.setText("Please login to continue this option. We' re waiting for your response!");

        dialogBinding.yesBtnText.setText("Login");

        dialogBinding.noBtn.setVisibility(View.VISIBLE);
        dialogBinding.noBtnText.setText("Later");

        dialogBinding.noBtn.setOnClickListener(v->{
            dialog.dismiss();
        });

        dialog.show();
    }
    @SuppressLint("ResourceAsColor")
    public static void showCustomMessage(Activity activity, String title, String message) {
        CustomDialogBinding dialogBinding = CustomDialogBinding.inflate(LayoutInflater.from(activity));

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialogBinding.getRoot())
                .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

        dialogBinding.titleText.setText(title);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dialogBinding.messageText.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
        } else {
            dialogBinding.messageText.setText(Html.fromHtml(message));
        }

        dialogBinding.yesBtnText.setText("Okay");

        dialogBinding.loginBtn.setOnClickListener(v->{
            dialog.dismiss();
        });

        dialog.show();
    }

    public static String getMainAddress(String address) {
        if (address.contains("+")){
            int firstCommaIndex = address.indexOf(',');
            if (firstCommaIndex != -1) {
                return address.substring(firstCommaIndex + 1).trim();
            }
        }
        return address;
    }
    public static String formatThousand(long number) {
        if (number < 1000) return Long.toString(number);
        int exp = (int) (Math.log(number) / Math.log(1000));
        char pre = "kMGTPE".charAt(exp - 1);
        return String.format("%.1f%c", number / Math.pow(1000, exp), pre);
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

}
