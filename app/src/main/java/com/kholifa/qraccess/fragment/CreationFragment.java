package com.kholifa.qraccess.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kholifa.qraccess.AdsDialog;
import com.kholifa.qraccess.R;
import com.kholifa.qraccess.RoomDatabaseStore.HistoryListViewModel;
import com.kholifa.qraccess.utillScan;
import com.kholifa.qraccess.activity.CreationCode.CreateSaveActivity;
import com.kholifa.qraccess.adapter.CreationAdapter;
import com.kholifa.qraccess.adapter.ScannerAdapter;


public class CreationFragment extends Fragment {
    protected View v;

    RecyclerView rec_history;
    CreationAdapter create_adapter;
    HistoryListViewModel history_view_model;
    TextView txt_data_check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_history_list, container, false);

        history_view_model = new ViewModelProvider(this).get(HistoryListViewModel.class);
        history_view_model.createdDeleteDuplicates();
        admobInt();
        rec_history = v.findViewById(R.id.rec_history);
        txt_data_check = v.findViewById(R.id.txt_data_check);
        create_adapter = new CreationAdapter(getContext(), new ScannerAdapter.interface_history_fun() {

            @Override
            public void onDeleteById(int id, View view, int position) {


                PopupMenu popup = new PopupMenu(getContext(), view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.item_open:
                                create_adapter.onCreateItemClick(position);
                                break;
                            case R.id.item_delete:
                                DeleteDialog(id);
                                break;

                        }
                        return false;
                    }
                });
                popup.inflate(R.menu.main_menu_list);
                popup.show();

            }

            @Override
            public void onItemClick(String data, String data1, String data2, String data3, String data4, String data5, String data6, String data7, String data8, String data9, String data10, String data11, String data12, String dataType) {

            }

            @Override
            public void onCreateItemClick(String data, String dataType, String s) {
                utillScan.string_data = data;
                utillScan.string_data_type = dataType;
                utillScan.generate_bitmap = utillScan.StringToBitMap(s);

                Intent intent = new Intent(getContext(), CreateSaveActivity.class);
                startActivity(intent);
                admobShow();
            }

        });
        rec_history.setLayoutManager(new LinearLayoutManager(getContext()));
        rec_history.setItemAnimator(new DefaultItemAnimator());
        rec_history.setAdapter(create_adapter);

        history_view_model.getAllCreateHistoryData().observe(this, createHistories -> {

            create_adapter.addAll(createHistories);
            if (createHistories.size() == 0) {
                txt_data_check.setVisibility(View.VISIBLE);
                rec_history.setVisibility(View.GONE);
            } else {
                txt_data_check.setVisibility(View.GONE);
                rec_history.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }


    public void DeleteDialog(int id) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(),R.style.AlertDialogTheme);
        builder.setTitle(Html.fromHtml("<font color='#2C99FD'>Delete</font>"));
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                history_view_model.deleteByIdcreate(id);
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage("Are you sure want to delete?").show();

    }
    private InterstitialAd m_interstitial_ad;

    public void admobInt() {
        if (m_interstitial_ad != null)
            return;
        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(
                getContext(),
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
            AdsDialog.getInstance().showLoader(getActivity());
            m_interstitial_ad.show(getActivity());
        }

    }

}