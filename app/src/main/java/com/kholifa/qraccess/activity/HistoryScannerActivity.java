package com.kholifa.qraccess.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kholifa.qraccess.R;
import com.kholifa.qraccess.adapter.ViewPagerScanAdapter;
import com.kholifa.qraccess.fragment.CreationFragment;
import com.kholifa.qraccess.fragment.ScannerFragment;

public class HistoryScannerActivity extends AppCompatActivity {
    TabLayout tabs;
    ViewPager2 view_pager;
    ImageView img_back_history;
    ViewPagerScanAdapter view_pager_adapter;
    public static final String[] tab_name = {"Scan List", "Create List"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);
        admobBanner();
        tabs = findViewById(R.id.tabs);
        view_pager = findViewById(R.id.view_pager);
        img_back_history = findViewById(R.id.img_back_history);

        view_pager_adapter = new ViewPagerScanAdapter(this);
        view_pager_adapter.addFragment(new ScannerFragment());
        view_pager_adapter.addFragment(new CreationFragment());
        view_pager.setAdapter(view_pager_adapter);

        new TabLayoutMediator(tabs, view_pager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tab_name[position]);
            }
        }).attach();

        img_back_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private AdView m_ad_view;

    public void admobBanner() {

        m_ad_view = findViewById(R.id.ad_view);
        AdRequest ad_request = new AdRequest.Builder().build();
        m_ad_view.loadAd(ad_request);
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