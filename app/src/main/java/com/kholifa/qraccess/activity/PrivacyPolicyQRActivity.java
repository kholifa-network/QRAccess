package com.kholifa.qraccess.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.kholifa.qraccess.R;


public class PrivacyPolicyQRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_privacy_code);

        WebView web_view = (WebView) findViewById(R.id.webview);

        WebSettings web_settings = web_view.getSettings();
        web_settings.setJavaScriptEnabled(true);

        web_view.loadUrl(getResources().getString(R.string.policy));
    }
}
