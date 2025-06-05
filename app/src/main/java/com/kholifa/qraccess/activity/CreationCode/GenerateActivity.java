package com.kholifa.qraccess.activity.CreationCode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.card.MaterialCardView;
import com.kholifa.qraccess.AdsDialog;
import com.kholifa.qraccess.R;


public class GenerateActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialCardView cv_text_create, cv_cont_create, cv_url_create, cv_wifi_create, cv_clip_create,
            cv_fb_create, cv_yt_create, cv_tel_create, cv_email_create, cv_insta_create, cv_card_create,
            cv_wa_create, cv_sms_create, cv_viber_create, cv_pay_create, cv_twitter_create, cv_calendar_create, cv_spotify_create;
    ImageView img_back_creator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creater);
        admobInt();
        admobBanner();
        cv_text_create = findViewById(R.id.cv_text_create);
        cv_cont_create = findViewById(R.id.cv_cont_create);
        cv_url_create = findViewById(R.id.cv_url_create);
        cv_wifi_create = findViewById(R.id.cv_wifi_create);
        cv_clip_create = findViewById(R.id.cv_clip_create);
        cv_fb_create = findViewById(R.id.cv_fb_create);
        cv_yt_create = findViewById(R.id.cv_yt_create);
        cv_tel_create = findViewById(R.id.cv_tel_create);
        cv_email_create = findViewById(R.id.cv_email_create);
        cv_insta_create = findViewById(R.id.cv_insta_create);
        cv_card_create = findViewById(R.id.cv_card_create);
        cv_wa_create = findViewById(R.id.cv_wa_create);
        cv_sms_create = findViewById(R.id.cv_sms_create);
        cv_viber_create = findViewById(R.id.cv_viber_create);
        cv_pay_create = findViewById(R.id.cv_pay_create);
        cv_twitter_create = findViewById(R.id.cv_twitter_create);
        cv_calendar_create = findViewById(R.id.cv_calendar_create);
        cv_spotify_create = findViewById(R.id.cv_spotify_create);
        img_back_creator = findViewById(R.id.img_back_creator);


        cv_text_create.setOnClickListener(this);
        cv_cont_create.setOnClickListener(this);
        cv_url_create.setOnClickListener(this);
        cv_wifi_create.setOnClickListener(this);
        cv_clip_create.setOnClickListener(this);
        cv_fb_create.setOnClickListener(this);
        cv_yt_create.setOnClickListener(this);
        cv_tel_create.setOnClickListener(this);
        cv_email_create.setOnClickListener(this);
        cv_insta_create.setOnClickListener(this);
        cv_card_create.setOnClickListener(this);
        cv_wa_create.setOnClickListener(this);
        cv_sms_create.setOnClickListener(this);
        cv_viber_create.setOnClickListener(this);
        cv_pay_create.setOnClickListener(this);
        cv_twitter_create.setOnClickListener(this);
        cv_calendar_create.setOnClickListener(this);
        cv_spotify_create.setOnClickListener(this);
        img_back_creator.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_text_create:
                Intent intentText = new Intent(GenerateActivity.this, CreateActivity.class);
                intentText.putExtra("flag", "Text");
                startActivityes(intentText);
                break;
            case R.id.cv_cont_create:
                Intent intentCont = new Intent(GenerateActivity.this, CreateActivity.class);
                intentCont.putExtra("flag", "Contact");
                startActivityes(intentCont);
                break;
            case R.id.cv_url_create:
                Intent intentUrl = new Intent(GenerateActivity.this, CreateActivity.class);
                intentUrl.putExtra("flag", "URL");
                startActivityes(intentUrl);
                break;
            case R.id.cv_wifi_create:
                Intent intentWifi = new Intent(GenerateActivity.this, CreateActivity.class);
                intentWifi.putExtra("flag", "Wi-Fi");
                startActivityes(intentWifi);
                break;
            case R.id.cv_clip_create:
                Intent intentClip = new Intent(GenerateActivity.this, CreateActivity.class);
                intentClip.putExtra("flag", "Clipboard");
                startActivityes(intentClip);
                break;
            case R.id.cv_fb_create:
                Intent intentFb = new Intent(GenerateActivity.this, CreateActivity.class);
                intentFb.putExtra("flag", "Facebook");
                startActivityes(intentFb);
                break;
            case R.id.cv_yt_create:
                Intent intentYT = new Intent(GenerateActivity.this, CreateActivity.class);
                intentYT.putExtra("flag", "Youtube");
                startActivityes(intentYT);
                break;
            case R.id.cv_tel_create:
                Intent intentTel = new Intent(GenerateActivity.this, CreateActivity.class);
                intentTel.putExtra("flag", "Telephone");
                startActivityes(intentTel);
                break;
            case R.id.cv_email_create:
                Intent intentEmail = new Intent(GenerateActivity.this, CreateActivity.class);
                intentEmail.putExtra("flag", "Email");
                startActivityes(intentEmail);
                break;
            case R.id.cv_insta_create:
                Intent intentInsta = new Intent(GenerateActivity.this, CreateActivity.class);
                intentInsta.putExtra("flag", "Instagram");
                startActivityes(intentInsta);
                break;
            case R.id.cv_card_create:
                Intent intentCard = new Intent(GenerateActivity.this, CreateActivity.class);
                intentCard.putExtra("flag", "My Card");
                startActivityes(intentCard);
                break;
            case R.id.cv_wa_create:
                Intent intentWA = new Intent(GenerateActivity.this, CreateActivity.class);
                intentWA.putExtra("flag", "Whatsapp");
                startActivityes(intentWA);
                break;
            case R.id.cv_sms_create:
                Intent intentSMS = new Intent(GenerateActivity.this, CreateActivity.class);
                intentSMS.putExtra("flag", "SMS");
                startActivityes(intentSMS);
                break;
            case R.id.cv_viber_create:
                Intent intentViber = new Intent(GenerateActivity.this, CreateActivity.class);
                intentViber.putExtra("flag", "Viber");
                startActivityes(intentViber);
                break;
            case R.id.cv_pay_create:
                Intent intentPay = new Intent(GenerateActivity.this, CreateActivity.class);
                intentPay.putExtra("flag", "Paypal");
                startActivityes(intentPay);
                break;
            case R.id.cv_twitter_create:
                Intent intentTwitter = new Intent(GenerateActivity.this, CreateActivity.class);
                intentTwitter.putExtra("flag", "Twitter");
                startActivityes(intentTwitter);
                break;
            case R.id.cv_calendar_create:
                Intent intentCalendar = new Intent(GenerateActivity.this, CreateActivity.class);
                intentCalendar.putExtra("flag", "Calendar");
                startActivityes(intentCalendar);
                break;
            case R.id.cv_spotify_create:
                Intent intentSpotify = new Intent(GenerateActivity.this, CreateActivity.class);
                intentSpotify.putExtra("flag", "Spotify");
                startActivityes(intentSpotify);
                break;
            case R.id.img_back_creator:
                onBackPressed();
                break;

        }
    }

    public void startActivityes(Intent intent){
        startActivity(intent);
        admobShow();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private InterstitialAd m_interstitial_ad;

    public void admobInt() {
        if (m_interstitial_ad != null)
            return;
        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(
                this,
                getResources().getString(R.string.admob_int),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {


                        m_interstitial_ad = interstitialAd;
                        m_interstitial_ad.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        m_interstitial_ad = null;
                                        admobInt();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        m_interstitial_ad = null;
                                        admobInt();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        m_interstitial_ad = null;
                                        AdsDialog.getInstance().dismissLoader();
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        m_interstitial_ad = null;
                    }
                });
    }

    public void admobShow() {

        if (m_interstitial_ad != null) {
            AdsDialog.getInstance().showLoader(this);
            m_interstitial_ad.show(this);
        }

    }
    private AdView m_ad_view;

    public void admobBanner() {

        m_ad_view = findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        m_ad_view.loadAd(adRequest);
        m_ad_view.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
    }
}