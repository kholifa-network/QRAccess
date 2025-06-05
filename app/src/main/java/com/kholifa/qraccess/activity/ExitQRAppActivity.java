package com.kholifa.qraccess.activity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.kholifa.qraccess.R;


public class ExitQRAppActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txt_yes, txt_no;
    LinearLayout ly_rate, ly_share;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_app_exit);
        admobBanner();
        txt_yes = findViewById(R.id.txt_yes);
        txt_no = findViewById(R.id.txt_no);
        ly_rate = (LinearLayout) findViewById(R.id.ly_rate);
        ly_share = (LinearLayout) findViewById(R.id.ly_share);
        bind();
    }

    private void bind() {
        txt_no.setOnClickListener(this);
        txt_yes.setOnClickListener(this);
        ly_rate.setOnClickListener(this);
        ly_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.txt_no:
                finish();
                break;
            case R.id.txt_yes:
                ((ResultReceiver) getIntent().getParcelableExtra("activityexit")).send(1, new Bundle());
                finish();
                break;
            case R.id.ly_rate:
                rate_us();
                break;
            case R.id.ly_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + "\nCreated By : " + getResources().getString(R.string.app_link));
                sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                        BitmapFactory.decodeResource(getResources(), R.mipmap.banner), null, null)));
                startActivity(Intent.createChooser(sharingIntent, "Share App using"));
                break;

        }
    }


    private void rate_us() {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent go_to_market = new Intent(Intent.ACTION_VIEW, uri);
        go_to_market.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(go_to_market);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + getApplicationContext().getPackageName())));
        }
    }


    public boolean isOnline() {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }


    private AdView m_ad_view;
    ImageView img_banner;

    public void admobBanner() {

        m_ad_view = findViewById(R.id.ad_view);
        img_banner =findViewById(R.id.img_banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        m_ad_view.loadAd(adRequest);
        m_ad_view.setAdListener(new AdListener() {
            @Override
            public void onAdOpened() {
                super.onAdOpened();
                img_banner.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                img_banner.setVisibility(View.GONE);
            }
        });
    }
}
