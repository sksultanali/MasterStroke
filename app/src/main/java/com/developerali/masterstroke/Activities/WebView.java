package com.developerali.masterstroke.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developerali.masterstroke.databinding.ActivityWebViewBinding;

public class WebView extends AppCompatActivity {

    ActivityWebViewBinding binding;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        link = getIntent().getStringExtra("share");
        binding.progressWeb.setMax(100);

        if (link != null){

            binding.webView.setWebViewClient(new WebViewClient());
            WebSettings settings = binding.webView.getSettings();
//            settings.setLoadWithOverviewMode(true);
//            settings.setUseWideViewPort(true);
//            settings.setJavaScriptEnabled(true);
//            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//            settings.setDatabaseEnabled(false);
//            settings.setDomStorageEnabled(false);
//            settings.setGeolocationEnabled(false);
//            settings.setSaveFormData(false);

            binding.webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(android.webkit.WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    binding.progressWeb.setProgress(newProgress);
                }

                @Override
                public void onReceivedTitle(android.webkit.WebView view, String title) {
                    super.onReceivedTitle(view, title);
                }

                @Override
                public void onReceivedIcon(android.webkit.WebView view, Bitmap icon) {
                    super.onReceivedIcon(view, icon);
                }
            });

            binding.webView.loadUrl(link);
        }else {
            Toast.makeText(this, "Noting Found or Link Broken", Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}