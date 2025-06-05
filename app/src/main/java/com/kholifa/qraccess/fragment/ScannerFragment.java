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
import com.kholifa.qraccess.activity.ShareScannerActivity;
import com.kholifa.qraccess.adapter.ScannerAdapter;


public class ScannerFragment extends Fragment {
    protected View v;
    RecyclerView rec_history;
    ScannerAdapter scan_adapter;
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
        history_view_model.deleteDuplicates();
        admobInt();
        rec_history = v.findViewById(R.id.rec_history);
        txt_data_check = v.findViewById(R.id.txt_data_check);
        scan_adapter = new ScannerAdapter(getContext(), new ScannerAdapter.interface_history_fun() {

            @Override
            public void onDeleteById(int id, View view, int position) {

                PopupMenu popup = new PopupMenu(getContext(), view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.item_open:
                                scan_adapter.onItemClick(position);

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

                Intent intent = new Intent(getContext(), ShareScannerActivity.class);
                if (dataType.equalsIgnoreCase("Calendar")) {
                    intent.putExtra("result", data);
                    intent.putExtra("loc", data1);
                    intent.putExtra("disc", data2);
                    intent.putExtra("sTimeHour", data3);
                    intent.putExtra("sTimeMin", data4);
                    intent.putExtra("sDateDay", data5);
                    intent.putExtra("sDateMonth", data6);
                    intent.putExtra("sDateYear", data7);
                    intent.putExtra("eTimeHour", data8);
                    intent.putExtra("eTimeMin", data9);
                    intent.putExtra("eDateDay", data10);
                    intent.putExtra("eDateMonth", data11);
                    intent.putExtra("eDateYear", data12);
                    intent.putExtra("resultType", dataType);

                } else if (dataType.equalsIgnoreCase("Wi-Fi")) {
                    intent.putExtra("result", data);
                    intent.putExtra("password", data1);
                    intent.putExtra("type", data2);
                    intent.putExtra("resultType", dataType);

                } else if (dataType.equalsIgnoreCase("Spotify")) {
                    intent.putExtra("result", data);
                    intent.putExtra("song", data1);
                    intent.putExtra("rawvalue", data2);
                    intent.putExtra("resultType", dataType);

                } else if (dataType.equalsIgnoreCase("Email")) {
                    intent.putExtra("result", data);
                    intent.putExtra("sub", data1);
                    intent.putExtra("body", data2);
                    intent.putExtra("resultType", dataType);

                } else if (dataType.equalsIgnoreCase("Contact")) {
                    intent.putExtra("result", data);
                    intent.putExtra("contact", data1);
                    intent.putExtra("email", data2);
                    intent.putExtra("org", data3);
                    intent.putExtra("add", data4);
                    intent.putExtra("note", data5);
                    intent.putExtra("birth", data6);
                    intent.putExtra("url", data7);
                    intent.putExtra("title", data8);
                    intent.putExtra("resultType", dataType);

                } else if (dataType.equalsIgnoreCase("SMS")) {
                    intent.putExtra("result", data);
                    intent.putExtra("phone", data1);
                    intent.putExtra("resultType", dataType);

                } else if (dataType.equalsIgnoreCase("Geo")) {
                    intent.putExtra("result", data);
                    intent.putExtra("longitude", data1);
                    intent.putExtra("resultType", dataType);

                } else if (dataType.equalsIgnoreCase("Driver License")) {
                    intent.putExtra("result", data);
                    intent.putExtra("street", data1);
                    intent.putExtra("city", data2);
                    intent.putExtra("state", data3);
                    intent.putExtra("zip", data4);
                    intent.putExtra("birthdate", data5);
                    intent.putExtra("docType", data6);
                    intent.putExtra("issueDate", data7);
                    intent.putExtra("expiryDate", data8);
                    intent.putExtra("gender", data9);
                    intent.putExtra("issuingCountry", data10);
                    intent.putExtra("licenseNumber", data11);
                    intent.putExtra("resultType", dataType);

                } else {
                    intent.putExtra("result", data);
                    intent.putExtra("resultType", dataType);
                }

                startActivity(intent);
                admobShow();
            }


            @Override
            public void onCreateItemClick(String data, String dataType, String bitmap) {

            }


        });
        rec_history.setLayoutManager(new LinearLayoutManager(getContext()));
        rec_history.setItemAnimator(new DefaultItemAnimator());
        rec_history.setAdapter(scan_adapter);

        history_view_model.getHistory_data().observe(this, histories ->
        {
            scan_adapter.addAll(histories);
            if (histories.size() == 0) {
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

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext(),
                R.style.AlertDialogTheme);
        builder.setTitle(Html.fromHtml("<font color='#2C99FD'>Delete</font>"));
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                history_view_model.deleteById(id);
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
        AdRequest ad_request = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(
                getContext(),
                getResources().getString(R.string.admob_int),
                ad_request,
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