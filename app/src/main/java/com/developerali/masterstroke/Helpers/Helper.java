package com.developerali.masterstroke.Helpers;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.developerali.masterstroke.Activities.ChartsActivity;
import com.developerali.masterstroke.Adapters.myListAdapter;
import com.developerali.masterstroke.ApiModels.ConstitutionModel;
import com.developerali.masterstroke.ApiModels.PhoneAddressModel;
import com.developerali.masterstroke.ApiService;
import com.developerali.masterstroke.R;
import com.developerali.masterstroke.RetrofitClient;
import com.developerali.masterstroke.databinding.CustomDialogBinding;
import com.developerali.masterstroke.databinding.DialogListSearchBinding;
import com.developerali.masterstroke.databinding.DialogTextInputBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.graphics.pdf.PdfDocument;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.ParcelFileDescriptor;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.app.Activity;
import android.content.Context;
import android.os.CancellationSignal;
import android.print.PageRange;
import android.print.PrintDocumentInfo;

public class Helper {

    public static String API_TOKEN = "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d";
    public static String USER_ID;
    public static String NAME;
    public static String PHONE;
    public static String PASSWORD;
    public static String USER_NAME;
    public static String WARD;
    public static String SPLASH_LINK;
    public static String HOME_LINK;
    public static String CANDIDATE;
    public static String LANGUAGE;
    public static String MIN_AGE;
    public static String MAX_AGE;
    public static String PART_NO;
    public static boolean RemoveMarked = false;
    public static boolean MARKING_ENABLE;
    public static boolean ADMIN_APPLICATION = false;
    public static boolean WB = true;

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

    public static String maskPhone(String Phone){
        return Phone.substring(0,1) + "XXXXXX" + Phone.substring((Phone.length()-4),Phone.length());
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

    public interface TranslationCallback {
        void onTranslationSuccess(String translatedText);
        void onTranslationFailure(String errorText);
    }

    public static void translateText(Activity activity, String text, TranslationCallback callback) {
        TranslateAPI translateAPI = new TranslateAPI(
                Language.ENGLISH,   // Source Language
                Helper.getLanguagePreference(activity),       // Target Language
                text                     // Query Text
        );

        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
            @Override
            public void onSuccess(String translatedText) {
                callback.onTranslationSuccess(translatedText); // Return the translated text through callback
            }

            @Override
            public void onFailure(String errorText) {
                callback.onTranslationFailure(errorText); // Return the error through callback
            }
        });
    }

    public static String getLanguagePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("language_code", "null"); // default to "en" if not set
    }

    public static void saveLanguagePreference(Context context, String languageCode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language_code", languageCode);
        //editor.apply(); // or commit()
        editor.commit();
    }

    public static void updateLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }


    public static void searchDialog(Activity activity, String searchOn,ArrayList<String> arrayListChoose, DialogCallback callback) {
        DialogListSearchBinding dialogBinding = DialogListSearchBinding.inflate(activity.getLayoutInflater());
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setView(dialogBinding.getRoot());
        // Create and show the dialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();

        dialogBinding.searchOn.setText(searchOn);

        myListAdapter adapter = new myListAdapter(activity, arrayListChoose, false);
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

    public static double calculatePercentage(String obtained1, String total1) {
        // Check to avoid division by zero
        double obtained = Double.parseDouble(obtained1);
        double total = Double.parseDouble(total1);
        if (total == 0) {
            return 0;
        }
        return (int) Math.round((obtained / total) * 100);
    }

    public static void searchDialog(Activity activity, String searchOn, DialogCallback callback) {
        DialogTextInputBinding dialogBinding = DialogTextInputBinding.inflate(activity.getLayoutInflater());
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        builder.setView(dialogBinding.getRoot());
        // Create and show the dialog
        androidx.appcompat.app.AlertDialog dialog = builder.create();

        dialogBinding.inputHint.setHint(searchOn);
        dialogBinding.btn.setText("Search");

        dialogBinding.btn.setOnClickListener(v -> {
            String text = dialogBinding.textInputL.getText().toString();
            if (text.isEmpty()) {
                dialogBinding.textInputL.setError("*");
            } else {
                dialog.dismiss();
                callback.onResult(text);
            }
        });

        dialog.show();
    }

    public static String getTextBeforeParenthesis(String address) {
        int index = address.indexOf('(');
        if (index != -1) {
            return address.substring(0, index).trim();
        }
        return address;
    }

    public interface DialogCallback {
        void onResult(String keyword);
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

    public static String getToday(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    public static double getSimilarityPercentage(String str1, String str2) {
        // Calculate the Levenshtein distance
        int distance = calculateLevenshteinDistance(str1, str2);

        // Get the maximum possible length between the two strings
        int maxLength = Math.max(str1.length(), str2.length());

        // If both strings are empty, they are 100% similar
        if (maxLength == 0) {
            return 100.0;
        }

        // Calculate the similarity percentage
        double similarity = ((double)(maxLength - distance) / maxLength) * 100;

        return similarity;
    }

    // Levenshtein Distance algorithm implementation
    private static int calculateLevenshteinDistance(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();

        // Create a 2D array to store distances
        int[][] dp = new int[len1 + 1][len2 + 1];

        // Initialize the first row and column
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        // Fill the rest of the dp array
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, // Deletion
                                dp[i][j - 1] + 1), // Insertion
                        dp[i - 1][j - 1] + cost); // Substitution
            }
        }

        // The final value in the dp array is the Levenshtein distance
        return dp[len1][len2];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getTomorrowDate() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return tomorrow.format(formatter);
    }

    public static String getDateKey(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYYMMdd");
        return simpleDateFormat.format(date);
    }

    public static String wishToHof(String lan, String name){
        if (lan.equalsIgnoreCase("hi")){
            return "नमस्कार, "+ name +"\n" +
                    "\n" +
                    "आप और आपका पूरा परिवार वार्ड की बेहतरी के लिए हमारे साथ जुड़ें। मैं आपके सुख-दुख में आपके साथ हूं.\n" +
                    "\n" +
                    "वेबसाइट - https://aitcofficial.org/\n" +
                    "\n" +
                    "धन्यवाद,\n" +
                    Helper.CANDIDATE;
        }else if (lan.equalsIgnoreCase("bn")){
            return "নমস্কার, " + name +"\n" +
                    "\n" +
                    "আপনি এবং আপনার পুরো পরিবার ওয়ার্ডের উন্নতির জন্য আমাদের সঙ্গে যুক্ত হয়েছেন। আপনার সুখ দুঃখে আমি আপনাদের পাশে আছি। \n" +
                    "\n" +
                    "website - https://aitcofficial.org/\n" +
                    "\n" +
                    "ধন্নবাদন্তে, \n" +
                    Helper.CANDIDATE;
        }else {
            return "Hello, "+ name +"\n" +
                    "\n" +
                    "You and your entire family join us for the betterment of the ward. I am with you in your happiness and sadness.\n" +
                    "\n" +
                    "website - https://aitcofficial.org/\n" +
                    "\n" +
                    "Thank you,\n" +
                    Helper.CANDIDATE;
        }
    }

    public static String wishBirthday(String lan, String name){
        if (lan.equalsIgnoreCase("hi")){
            return "जन्मदिन मुबारक हो, " + name + " 🎉 नए अनुभवों, ढेर सारी मुस्कान और सपनों को साकार करने वाले साल की शुभकामनाएं। आज और हमेशा आपको सेलिब्रेट कर रहे हैं! 🎂✨";
        }else if (lan.equalsIgnoreCase("bn")){
            return "শুভ জন্মদিন, " + name + " 🎉 নতুন অভিজ্ঞতা, অজস্র হাসি, আর স্বপ্ন পূরণের এক অসাধারণ বছর হোক তোমার জন্য। আজ এবং সবসময় তোমাকে উদযাপন করছি! 🎂✨";
        }else {
            return "Happy Birthday, " + name + " 🎉 Wishing you a year full of new adventures, endless smiles, and dreams that turn into reality. Here's to celebrating you today and always! 🎂✨";
        }
    }

    public static String wishAnniversary(String lan, String name) {
        if (lan.equalsIgnoreCase("hi")) {
            return "शादी की सालगिरह मुबारक हो, " + name + " 🎉 प्यार, खुशियों और नए यादगार पलों से भरे साल की शुभकामनाएं। आपका रिश्ता सदा मजबूत और खूबसूरत बना रहे! 💍✨";
        } else if (lan.equalsIgnoreCase("bn")) {
            return "বিবাহবার্ষিকীর শুভেচ্ছা, " + name + " 🎉 ভালোবাসা, আনন্দ এবং নতুন স্মৃতি দিয়ে ভরপুর একটি বছর কাটুক। তোমাদের সম্পর্ক সবসময় মজবুত ও সুন্দর থাকুক! 💍✨";
        } else {
            return "Happy Anniversary, " + name + " 🎉 Wishing you a year filled with love, joy, and beautiful new memories. May your bond continue to grow stronger and more beautiful! 💍✨";
        }
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

    public static void accountDetails(String username, String password,
                                 String userId, String name, String phone,
                                      String ward, String link, String homeLink,
                                      String party_slogan, String candidate,
                                      Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putString("password", password);
        editor.putString("userId", userId);
        editor.putString("ward_id", ward);
        editor.putString("splash_link", link);
        editor.putString("home_link", homeLink);
        editor.putString("party_suggest", homeLink);
        editor.putString("candidate_name", candidate);
        editor.apply();
    }

    public static void saveFavourText(String text, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("favour", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("text", text);
        editor.apply();
    }

    public static boolean getUserLogin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("account", Context.MODE_PRIVATE);
        Helper.USER_NAME = sharedPreferences.getString("username", "NA");
        Helper.NAME = sharedPreferences.getString("name", "NA");
        Helper.PHONE = sharedPreferences.getString("phone", "NA");
        Helper.PASSWORD = sharedPreferences.getString("password", "NA");
        Helper.USER_ID = sharedPreferences.getString("userId", "NA");
        Helper.WARD = sharedPreferences.getString("ward_id", "0");
        Helper.SPLASH_LINK = sharedPreferences.getString("splash_link", "NA");
        Helper.HOME_LINK = sharedPreferences.getString("home_link", "NA");
        Helper.saveFavourText(sharedPreferences.getString("party_suggest", "Please vote in favour of party."), context);
        Helper.CANDIDATE = sharedPreferences.getString("candidate_name", "NA");

        if (Helper.USER_NAME.equalsIgnoreCase("NA")){
            return false;
        }else {
            return true;
        }
    }

    public static String getFavourText(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("favour", Context.MODE_PRIVATE);
        return sharedPreferences.getString("text", "Please vote in favour of ");
    }

    public static void startCounter(int countTill, TextView textView){
        ValueAnimator animator = ValueAnimator.ofInt(0, countTill);
        animator.setDuration(3000); // Animation duration in milliseconds
        animator.addUpdateListener(valueAnimator -> {
            int animatedValue = (int) valueAnimator.getAnimatedValue();
            textView.setText(String.valueOf(animatedValue));
        });
        animator.start();
    }

    public static void getWardName(String ward, TextView textView){
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<ConstitutionModel> call = apiService.getConstitutionDetails(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                Integer.parseInt(ward)
        );

        call.enqueue(new Callback<ConstitutionModel>() {
            @Override
            public void onResponse(Call<ConstitutionModel> call, Response<ConstitutionModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ConstitutionModel apiResponse = response.body();
                    if (apiResponse.getItem() != null){
                        textView.setText(apiResponse.getItem().get(0).getConstitution_name());
                    }
                }

                Log.d("SearchActivity.this", "URL: " + call.request().url());
            }

            @Override
            public void onFailure(Call<ConstitutionModel> call, Throwable t) {

            }
        });
    }

    public static void getWardNo(String ward, Activity activity, androidx.appcompat.app.ActionBar supportActionBar) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<PhoneAddressModel> call = apiService.getVoters(
                "fa3b2c9c-a96d-48a8-82ad-0cb775dd3e5d",
                0,
                Integer.parseInt(ward)
        );

        call.enqueue(new Callback<PhoneAddressModel>() {
            @Override
            public void onResponse(Call<PhoneAddressModel> call, Response<PhoneAddressModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PhoneAddressModel apiResponse = response.body();
                    //ActionBar actionBar = activity.getActionBar();
                    if (supportActionBar != null) {
                        supportActionBar.setSubtitle("Ward no - " + apiResponse.getItem().get(0).getWard());
                    } else {
                        Toast.makeText(activity, "Action bar is not available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PhoneAddressModel> call, Throwable t) {
            }
        });

    }

    public static void clearSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
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

    private static final String PREFS_NAME = "my_prefs";
    private static final String KEY_ARRAY_LIST = "my_array_list";

    // Save ArrayList to SharedPreferences
    public static void savePartyList(Context context, ArrayList<String> list) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(KEY_ARRAY_LIST, json);
        editor.apply();
    }

    // Retrieve ArrayList from SharedPreferences
    public static ArrayList<String> getPartyList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(KEY_ARRAY_LIST, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Clear the ArrayList from SharedPreferences
    public static void clearPartyList(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_ARRAY_LIST);
        editor.apply();
    }

    public static String slipText(PhoneAddressModel.Item details, Activity context){
        String text =
                "*******************************\n\n"+
                "Name: "+ details.getName() + "\n\n" +
                        "Part No: "+ details.getPartNo() + "  |  Sl No: " + details.getSl_no() + "\n\n" +
                        "Gender: "+ details.getSex() + "  |  Age: " + details.getAge() + "\n\n" +
                        "Voter Id: "+ details.getVoter_id() + "\n\n" +
                        "Address: "+ details.getHouse() + ", " +Helper.getTextBeforeParenthesis(details.getAddress()) + "\n\n" +
                        "Booth: " + details.getPollingStation() + "\n\n\n" +

                        "*********************\n"+Helper.getFavourText(context)+
                        "\nGenerated By Master Stroke\n*********************\n\n";
        return text;
    }

    public static String slipTextWithoutLogo(PhoneAddressModel.Item details, Activity context){
        String text =
                "*******************************\n\n"+
                        "Name: "+ details.getName() + "\n\n" +
                        "Part No: "+ details.getPartNo() + "  |  Sl No: " + details.getSl_no() + "\n\n" +
                        "Gender: "+ details.getSex() + "  |  Age: " + details.getAge() + "\n\n" +
                        "Voter Id: "+ details.getVoter_id() + "\n\n" +
                        "Address: "+ details.getHouse() + ", " +Helper.getTextBeforeParenthesis(details.getAddress()) + "\n\n" +
                        "Booth: " + details.getPollingStation()+ "\n\n" +

                        "*******************************\n\n";
        return text;
    }

    public static String phoneNoWithCountryCode(String phone){
        if (phone.length() == 10){
            return "+91"+phone;
        } else if (phone.length() == 12) {
            return "+"+phone;
        }else {
            return phone;
        }
    }

    public static void printText(Activity activity, String textToPrint, boolean logo){
        Helper.translateText(activity, textToPrint, new TranslationCallback() {
            @Override
            public void onTranslationSuccess(String translatedText) {
                Helper.executePrint(activity, translatedText, logo);
            }

            @Override
            public void onTranslationFailure(String errorText) {
                Helper.executePrint(activity, textToPrint, logo);
            }
        });
    }

    public static void executePrint(Activity activity, String textToPrint, boolean logo) {
        // Get the PrintManager system service
        PrintManager printManager = (PrintManager) activity.getSystemService(Context.PRINT_SERVICE);

        // Name for the print job
        String jobName = activity.getString(R.string.app_name) + " Document";

        // Create a PrintDocumentAdapter with the text content
        PrintDocumentAdapter printAdapter = new PrintDocumentAdapter() {
            PrintedPdfDocument pdfDocument;

            @Override
            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes,
                                 CancellationSignal cancellationSignal,
                                 LayoutResultCallback callback, android.os.Bundle extras) {
                // Create a new PdfDocument with the requested attributes
                pdfDocument = new PrintedPdfDocument(activity, newAttributes);

                if (cancellationSignal.isCanceled()) {
                    callback.onLayoutCancelled();
                    return;
                }

                // Pass the layout result
                callback.onLayoutFinished(
                        new PrintDocumentInfo.Builder(jobName)
                                .setPageCount(1) // Assuming 1 page
                                .build(),
                        true);
            }

            @Override
            public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor destination,
                                CancellationSignal cancellationSignal, WriteResultCallback callback) {
                // Start a page
                PdfDocument.Page page = pdfDocument.startPage(0);

                // Draw content on the page
                if (page != null) {
                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    paint.setColor(android.graphics.Color.BLACK);
                    paint.setTextSize(16);

                    // Step 1: Draw the image first
                    int xPos = 10;  // Starting x position
                    int yPos = 25;  // Starting y position for image

                    if (logo){
                        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.slip_logo);
                        // Resize the bitmap if necessary
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                        int pageWidth = canvas.getWidth();
                        int xPos1 = (pageWidth - resizedBitmap.getWidth()) / 2;
                        canvas.drawBitmap(resizedBitmap, xPos1, yPos, null);
                        yPos += resizedBitmap.getHeight() + 20;
                    }

                    // Step 2: Draw the text after the image
                    String[] lines = textToPrint.split("\n");
                    for (String line : lines) {
                        canvas.drawText(line, xPos, yPos, paint);
                        yPos += paint.descent() - paint.ascent(); // Move the Y position down for the next line
                    }

                    // Finish the page
                    pdfDocument.finishPage(page);

                    try {
                        // Write the document content to the file descriptor
                        pdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
                        callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                    } catch (IOException e) {
                        callback.onWriteFailed(e.toString());
                    } finally {
                        pdfDocument.close();
                    }
                }
            }
        };

        // Start a print job
        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
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
