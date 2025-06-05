package com.kholifa.qraccess.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.kholifa.qraccess.AdsDialog;
import com.kholifa.qraccess.R;
import com.kholifa.qraccess.RoomDatabaseStore.HistoryList;
import com.kholifa.qraccess.RoomDatabaseStore.HistoryListViewModel;
import com.oginotihiro.cropview.CropView;

import java.io.IOException;
import java.util.List;

import static com.kholifa.qraccess.utillScan.matchSinglePrefixedField;
import static com.kholifa.qraccess.activity.MainQrScannerActivity.orignalbitmap;
import static com.kholifa.qraccess.activity.MainQrScannerActivity.string;


public class ImageGalScannerActivity extends AppCompatActivity {
    MaterialCardView cv_scan;
    HistoryListViewModel history_view_model;
    ImageView img_gallery_back, img_gallerypick;
    TextView txt_gallerypick;
    CropView crop_view;
    Bitmap bitmaptest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gal_scanner);
        history_view_model = new ViewModelProvider(this).get(HistoryListViewModel.class);
        admobInt();
        admobBanner();

        img_gallery_back = findViewById(R.id.img_gallery_back);
        img_gallerypick = findViewById(R.id.img_gallerypick);
        txt_gallerypick = findViewById(R.id.txt_gallerypick);

        cv_scan = findViewById(R.id.cv_scan);
        cv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Scanmethod();
            }
        });
        img_gallery_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        img_gallerypick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryimgpick();
            }
        });
        txt_gallerypick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_gallerypick.performClick();
            }
        });
        crop_view = findViewById(R.id.crop_view);
        crop_view.of(string)
                .withAspect(240, 240)
                .withOutputSize(240, 240)
                .initialize(this);
    }

    public void galleryimgpick() {
        Intent photo_picker_intent = new Intent();
        photo_picker_intent.setType("image/*");
        photo_picker_intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(photo_picker_intent, "Select Picture"), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == -1) {

            if (data != null) {
                try {
                    orignalbitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), data.getData());
                    string = data.getData();

                    crop_view.of(string)
                            .withAspect(240, 240)
                            .withOutputSize(240, 240)
                            .initialize(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {

        }
    }

    public void Scanmethod() {
        bitmaptest = crop_view.getOutput();

        InputImage image = InputImage.fromBitmap(bitmaptest, 0);
        BarcodeScanner scanner = BarcodeScanning.getClient();

        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener((OnSuccessListener<? super List<Barcode>>) new OnSuccessListener<List<Barcode>>() {

                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        if (barcodes.size() <= 0) {
                            Toast.makeText(ImageGalScannerActivity.this, "please,select another Image.. \nor set into the box..", Toast.LENGTH_SHORT).show();

                        }
                        HistoryList history = null;
                        for (Barcode barcode : barcodes) {
                            Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();

                            String rawValue = barcode.getRawValue();

                            Intent intent = new Intent(ImageGalScannerActivity.this, ShareScannerActivity.class);
                            int valueType = barcode.getValueType();

                            switch (valueType) {
                                case Barcode.TYPE_WIFI:
                                    String securityType = "UNKNOWN";
                                    String ssid = barcode.getWifi().getSsid();
                                    String password = barcode.getWifi().getPassword();
                                    int type = barcode.getWifi().getEncryptionType();
                                    if (type == barcode.getWifi().TYPE_OPEN) {
                                        securityType = "OPEN";
                                    } else if (type == barcode.getWifi().TYPE_WPA) {
                                        securityType = "WPA";
                                    } else if (type == barcode.getWifi().TYPE_WEP) {
                                        securityType = "WEP";
                                    }
                                    intent.putExtra("result", ssid);
                                    intent.putExtra("resultType", "Wi-Fi");
                                    intent.putExtra("password", password);
                                    intent.putExtra("type", securityType);

                                    history = new HistoryList(ssid, password, securityType, null, null, null, null, null, null, null, null, null, null, "Wi-Fi");
                                    break;
                                case Barcode.TYPE_URL:

                                    if (rawValue.contains("facebook.com")) {
                                        String text;
                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Facebook");

                                        if (rawValue.contains("fb://page/")) {
                                            text = rawValue.replace("fb://page/", "");

                                        } else if (rawValue.contains("fb://profile/")) {
                                            text = rawValue.replace("fb://profile/", "");

                                        } else if (rawValue.contains("http://www.facebook.com/")) {
                                            text = rawValue.replace("http://www.facebook.com/", "");
                                        } else {
                                            text = rawValue;
                                        }
                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Facebook");

                                    } else if (rawValue.contains("youtube.com")) {
                                        String text;
                                        if (rawValue.contains("http://www.youtube.com/watch?v=")) {
                                            text = rawValue.replace("http://www.youtube.com/watch?v=", "");
                                        } else {
                                            text = rawValue;
                                        }
                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Youtube");

                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Youtube");

                                    } else if (rawValue.contains("instagram.com")) {
                                        String text;
                                        if (rawValue.contains("instagram://user?username=")) {
                                            text = rawValue.replace("instagram://user?username=", "");

                                        } else if (rawValue.contains("http://www.instagram.com/")) {
                                            text = rawValue.replace("http://www.instagram.com/", "");

                                        } else {
                                            text = rawValue;
                                        }

                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Instagram");
                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Instagram");

                                    } else if (rawValue.contains("whatsapp.com") || rawValue.contains("https://wa.me/")) {
                                        String text;
                                        if (rawValue.contains("whatsapp://send?phone=")) {
                                            text = rawValue.replace("whatsapp://send?phone=", "");
                                        } else if (rawValue.contains("https://api.whatsapp.com/send?phone=")) {
                                            text = rawValue.replace("https://api.whatsapp.com/send?phone=", "");
                                        } else if (rawValue.contains("https://wa.me/")) {
                                            text = rawValue.replace("https://wa.me/", "");
                                        } else {
                                            text = rawValue;
                                        }
                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Whatsapp");

                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Whatsapp");

                                    } else if (rawValue.contains("https://www.paypal.me/")) {
                                        String text;
                                        if (rawValue.contains("https://www.paypal.me/")) {
                                            text = rawValue.replace("https://www.paypal.me/", "");
                                        } else {
                                            text = rawValue;
                                        }

                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Paypal");

                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Paypal");

                                    } else if (rawValue.contains("twitter.com")) {
                                        String text;
                                        if (rawValue.contains("twitter://user?screen_name=")) {
                                            text = rawValue.replace("twitter://user?screen_name=", "");
                                        } else if (rawValue.contains("https://twitter.com/")) {
                                            text = rawValue.replace("https://twitter.com/", "");
                                        } else {
                                            text = rawValue;
                                        }

                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Twitter");

                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Twitter");

                                    } else {
                                        String title = barcode.getUrl().getTitle();
                                        String url = barcode.getUrl().getUrl();
                                        intent.putExtra("result", url);
                                        intent.putExtra("resultType", "URL");

                                        history = new HistoryList(url, null, null, null, null, null, null, null, null, null, null, null, null, "URL");
                                    }


                                    break;
                                case Barcode.TYPE_CONTACT_INFO:
                                    String name = barcode.getContactInfo().getName().getFormattedName();
                                    String cont = barcode.getContactInfo().getPhones().get(0).getNumber();
                                    String email = barcode.getContactInfo().getEmails().get(0).getAddress();
                                    String org = barcode.getContactInfo().getOrganization();
                                    String title = barcode.getContactInfo().getTitle();


                                    String add, note, birth, urls;
                                    add = matchSinglePrefixedField("ADR:", barcode.getRawValue(), ';', true);
                                    note = matchSinglePrefixedField("NOTE:", barcode.getRawValue(), ';', true);
                                    birth = matchSinglePrefixedField("BDAY:", barcode.getRawValue(), ';', true);
                                    urls = matchSinglePrefixedField("URL:", barcode.getRawValue(), ';', true);
                                    if (add == null) {
                                        add = "";
                                    }
                                    if (note == null) {
                                        note = "";
                                    }
                                    if (birth == null) {
                                        birth = "";
                                    }
                                    if (urls == null) {
                                        urls = "";
                                    }
                                    intent.putExtra("result", name);
                                    intent.putExtra("contact", cont);
                                    intent.putExtra("email", email);
                                    intent.putExtra("org", org);
                                    intent.putExtra("add", add);
                                    intent.putExtra("note", note);
                                    intent.putExtra("birth", birth);
                                    intent.putExtra("url", urls);
                                    intent.putExtra("title", title);

                                    intent.putExtra("resultType", "Contact");
                                    history = new HistoryList(name, cont, email, org, add, note, birth, urls, title, null, null, null, null, "Contact");
                                    break;
                                case Barcode.TYPE_PHONE:
                                    String phone = barcode.getPhone().getNumber();
                                    intent.putExtra("result", phone);
                                    intent.putExtra("resultType", "Telephone");

                                    history = new HistoryList(phone, null, null, null, null, null, null, null, null, null, null, null, null, "Telephone");
                                    break;

                                case Barcode.TYPE_PRODUCT:
                                    String result = barcode.getDisplayValue();
                                    intent.putExtra("result", result);
                                    intent.putExtra("resultType", "Product");

                                    history = new HistoryList(result, null, null, null, null, null, null, null, null, null, null, null, null, "Product");

                                    break;

                                case Barcode.TYPE_TEXT:
                                    if (rawValue.contains("MECARD")) {
                                        String tname, tcont, temail, torg, tadd, tnote, tbirth, tUrl, ttitle;
                                        tname = matchSinglePrefixedField("N:", barcode.getRawValue(), ';', true);
                                        tcont = matchSinglePrefixedField("TEL:", barcode.getRawValue(), ';', true);
                                        temail = matchSinglePrefixedField("EMAIL:", barcode.getRawValue(), ';', true);
                                        torg = matchSinglePrefixedField("ORG:", barcode.getRawValue(), ';', true);
                                        tadd = matchSinglePrefixedField("ADR:", barcode.getRawValue(), ';', true);
                                        tnote = matchSinglePrefixedField("NOTE:", barcode.getRawValue(), ';', true);
                                        tbirth = matchSinglePrefixedField("BDAY:", barcode.getRawValue(), ';', true);
                                        tUrl = matchSinglePrefixedField("URL:", barcode.getRawValue(), ';', true);
                                        ttitle = matchSinglePrefixedField("TITLE:", barcode.getRawValue(), ';', true);

                                        if (tname == null) {
                                            tname = "";
                                        }
                                        if (tcont == null) {
                                            tcont = "";
                                        }
                                        if (temail == null) {
                                            temail = "";
                                        }
                                        if (torg == null) {
                                            torg = "";
                                        }
                                        if (tadd == null) {
                                            tadd = "";
                                        }
                                        if (tnote == null) {
                                            tnote = "";
                                        }
                                        if (tbirth == null) {
                                            tbirth = "";
                                        }
                                        if (tUrl == null) {
                                            tUrl = "";
                                        }
                                        if (ttitle == null) {
                                            ttitle = "";
                                        }

                                        intent.putExtra("result", tname);
                                        intent.putExtra("contact", tcont);
                                        intent.putExtra("email", temail);
                                        intent.putExtra("org", torg);
                                        intent.putExtra("add", tadd);
                                        intent.putExtra("note", tnote);
                                        intent.putExtra("birth", tbirth);
                                        intent.putExtra("url", tUrl);
                                        intent.putExtra("title", ttitle);

                                        intent.putExtra("resultType", "Contact");
                                        history = new HistoryList(tname, tcont, temail, torg, tadd, tnote, tbirth, tUrl, ttitle, null, null, null, null, "Contact");

                                    } else if (rawValue.contains(".com") || rawValue.contains("www.")) {
                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "URL");
                                        history = new HistoryList(rawValue, null, null, null, null, null, null, null, null, null, null, null, null, "URL");

                                    } else if (rawValue.contains("facebook.com") || rawValue.contains("fb://profile/") || rawValue.contains("fb://page/")) {
                                        String text;
                                        if (rawValue.contains("fb://page/")) {
                                            text = rawValue.replace("fb://page/", "");

                                        } else if (rawValue.contains("fb://profile/")) {
                                            text = rawValue.replace("fb://profile/", "");

                                        } else if (rawValue.contains("http://www.facebook.com/")) {
                                            text = rawValue.replace("http://www.facebook.com/", "");
                                        } else {
                                            text = rawValue;
                                        }
                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Facebook");

                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Facebook");
                                    } else if (rawValue.contains("youtube.com")) {
                                        String text;
                                        if (rawValue.contains("http://www.youtube.com/watch?v=")) {
                                            text = rawValue.replace("http://www.youtube.com/watch?v=", "");
                                        } else {
                                            text = rawValue;
                                        }
                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Youtube");

                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Youtube");

                                    } else if (rawValue.contains("instagram.com") || rawValue.contains("instagram://user?username=")) {

                                        String text;
                                        if (rawValue.contains("instagram://user?username=")) {
                                            text = rawValue.replace("instagram://user?username=", "");

                                        } else if (rawValue.contains("http://www.instagram.com/")) {
                                            text = rawValue.replace("http://www.instagram.com/", "");

                                        } else {
                                            text = rawValue;
                                        }

                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Instagram");
                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Instagram");

                                    } else if (rawValue.contains("whatsapp://send?phone=") || rawValue.contains("whatsapp.com")) {

                                        String text;
                                        if (rawValue.contains("whatsapp://send?phone=")) {
                                            text = rawValue.replace("whatsapp://send?phone=", "");
                                        } else if (rawValue.contains("https://api.whatsapp.com/send?phone=")) {
                                            text = rawValue.replace("https://api.whatsapp.com/send?phone=", "");
                                        } else if (rawValue.contains("https://wa.me/")) {
                                            text = rawValue.replace("https://wa.me/", "");
                                        } else {
                                            text = rawValue;
                                        }
                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Whatsapp");

                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Whatsapp");

                                    } else if (rawValue.contains("viber://add?number=")) {

                                        String text;
                                        if (rawValue.contains("viber://add?number=")) {
                                            text = rawValue.replace("viber://add?number=", "");
                                        } else {
                                            text = rawValue;
                                        }
                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Viber");

                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Viber");
                                    } else if (rawValue.contains("paypal.me")) {
                                        String text;
                                        if (rawValue.contains("https://www.paypal.me/")) {
                                            text = rawValue.replace("https://www.paypal.me/", "");
                                        } else {
                                            text = rawValue;
                                        }

                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Paypal");

                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Paypal");


                                    } else if (rawValue.contains("twitter.com") || rawValue.contains("twitter://user?screen_name=")) {

                                        String text;
                                        if (rawValue.contains("twitter://user?screen_name=")) {
                                            text = rawValue.replace("twitter://user?screen_name=", "");
                                        } else if (rawValue.contains("https://twitter.com/")) {
                                            text = rawValue.replace("https://twitter.com/", "");
                                        } else {
                                            text = rawValue;
                                        }

                                        intent.putExtra("result", rawValue);
                                        intent.putExtra("resultType", "Twitter");

                                        history = new HistoryList(rawValue, text, null, null, null, null, null, null, null, null, null, null, null, "Twitter");

                                    } else if (rawValue.contains("spotify:")) {

                                        String artist = getSubString(barcode.getRawValue(), ";", "search:");
                                        String songName = rawValue.substring(rawValue.lastIndexOf(';') + 1);
                                        intent.putExtra("result", artist);
                                        intent.putExtra("song", songName);
                                        intent.putExtra("rawvalue", rawValue);
                                        intent.putExtra("resultType", "Spotify");

                                        history = new HistoryList(artist, songName, rawValue, null, null, null, null, null, null, null, null, null, null, "Spotify");
                                    } else {
                                        String text = barcode.getDisplayValue();
                                        intent.putExtra("result", text);
                                        intent.putExtra("resultType", "Text");
                                        history = new HistoryList(text, null, null, null, null, null, null, null, null, null, null, null, null, "Text");
                                    }
                                    break;
                                case Barcode.TYPE_EMAIL:
                                    String emailAdd = barcode.getEmail().getAddress();
                                    String sub = barcode.getEmail().getSubject();
                                    String body = barcode.getEmail().getBody();
                                    intent.putExtra("result", emailAdd);
                                    intent.putExtra("sub", sub);
                                    intent.putExtra("body", body);
                                    intent.putExtra("resultType", "Email");

                                    history = new HistoryList(emailAdd, sub, body, null, null, null, null, null, null, null, null, null, null, "Email");
                                    break;
                                case Barcode.TYPE_SMS:
                                    String sms = barcode.getSms().getMessage();
                                    String phonesms = barcode.getSms().getPhoneNumber();
                                    intent.putExtra("result", sms);
                                    intent.putExtra("phone", phonesms);
                                    intent.putExtra("resultType", "SMS");

                                    history = new HistoryList(sms, phonesms, null, null, null, null, null, null, null, null, null, null, null, "SMS");
                                    break;
                                case Barcode.TYPE_UNKNOWN:
                                    String text = barcode.getDisplayValue();
                                    intent.putExtra("result", text);
                                    intent.putExtra("resultType", "Text");
                                    history = new HistoryList(text, null, null, null, null, null, null, null, null, null, null, null, null, "Text");

                                    break;
                                case Barcode.TYPE_CALENDAR_EVENT:
                                    String titlecal = barcode.getCalendarEvent().getSummary();
                                    String loc = barcode.getCalendarEvent().getLocation();
                                    String disc = barcode.getCalendarEvent().getDescription();
                                    String sTimeHour = String.valueOf(barcode.getCalendarEvent().getStart().getHours());
                                    String sTimeMin = String.valueOf(barcode.getCalendarEvent().getStart().getMinutes());
                                    String sDateDay = String.valueOf(barcode.getCalendarEvent().getStart().getDay());
                                    String sDateMonth = String.valueOf(barcode.getCalendarEvent().getStart().getMonth());
                                    String sDateYear = String.valueOf(barcode.getCalendarEvent().getStart().getYear());
                                    String eTimeHour = String.valueOf(barcode.getCalendarEvent().getEnd().getHours());
                                    String eTimeMin = String.valueOf(barcode.getCalendarEvent().getEnd().getMinutes());
                                    String eDateDay = String.valueOf(barcode.getCalendarEvent().getEnd().getDay());
                                    String eDateMonth = String.valueOf(barcode.getCalendarEvent().getEnd().getMonth());
                                    String eDateYear = String.valueOf(barcode.getCalendarEvent().getEnd().getYear());

                                    intent.putExtra("result", titlecal);
                                    intent.putExtra("loc", loc);
                                    intent.putExtra("disc", disc);
                                    intent.putExtra("sTimeHour", sTimeHour);
                                    intent.putExtra("sTimeMin", sTimeMin);
                                    intent.putExtra("sDateDay", sDateDay);
                                    intent.putExtra("sDateMonth", sDateMonth);
                                    intent.putExtra("sDateYear", sDateYear);
                                    intent.putExtra("eTimeHour", eTimeHour);
                                    intent.putExtra("eTimeMin", eTimeMin);
                                    intent.putExtra("eDateDay", eDateDay);
                                    intent.putExtra("eDateMonth", eDateMonth);
                                    intent.putExtra("eDateYear", eDateYear);
                                    intent.putExtra("resultType", "Calendar");

                                    history = new HistoryList(titlecal, loc, disc, sTimeHour, sTimeMin, sDateDay, sDateMonth, sDateYear, eTimeHour, eTimeMin, eDateDay, eDateMonth, eDateYear, "Calendar");
                                    break;
                                case Barcode.TYPE_ISBN:
                                    intent.putExtra("result", barcode.getRawValue());
                                    intent.putExtra("resultType", "Book");
                                    history = new HistoryList(barcode.getRawValue(), null, null, null, null, null, null, null, null, null, null, null, null, "Book");

                                    break;
                                case Barcode.TYPE_GEO:
                                    String latitude = String.valueOf(barcode.getGeoPoint().getLat());
                                    String longitude = String.valueOf(barcode.getGeoPoint().getLng());

                                    intent.putExtra("result", latitude);
                                    intent.putExtra("longitude", longitude);
                                    intent.putExtra("resultType", "Geo");
                                    history = new HistoryList(latitude, longitude, null, null, null, null, null, null, null, null, null, null, null, "Geo");

                                    break;
                                case Barcode.TYPE_DRIVER_LICENSE:
                                    String fname = barcode.getDriverLicense().getFirstName();
                                    String lname = barcode.getDriverLicense().getLastName();
                                    String mname = barcode.getDriverLicense().getMiddleName();
                                    String street = barcode.getDriverLicense().getAddressStreet();
                                    String state = barcode.getDriverLicense().getAddressState();
                                    String city = barcode.getDriverLicense().getAddressCity();
                                    String zip = barcode.getDriverLicense().getAddressZip();
                                    String birthdate = barcode.getDriverLicense().getBirthDate();
                                    String docType = barcode.getDriverLicense().getDocumentType();
                                    String expiryDate = barcode.getDriverLicense().getExpiryDate();
                                    String issueDate = barcode.getDriverLicense().getIssueDate();
                                    String gender = barcode.getDriverLicense().getGender();
                                    String issuingCountry = barcode.getDriverLicense().getIssuingCountry();
                                    String licenseNumber = barcode.getDriverLicense().getLicenseNumber();

                                    String fullName = fname + " " + mname + " " + lname;
                                    String address = street + " " + city + " " + state + " " + zip;

                                    intent.putExtra("result", fullName);
                                    intent.putExtra("street", street);
                                    intent.putExtra("city", city);
                                    intent.putExtra("state", state);
                                    intent.putExtra("zip", zip);
                                    intent.putExtra("birthdate", birthdate);
                                    intent.putExtra("docType", docType);
                                    intent.putExtra("issueDate", issueDate);
                                    intent.putExtra("expiryDate", expiryDate);
                                    intent.putExtra("gender", gender);
                                    intent.putExtra("issuingCountry", issuingCountry);
                                    intent.putExtra("licenseNumber", licenseNumber);
                                    intent.putExtra("resultType", "Driver License");
                                    history = new HistoryList(fullName, street, city, state, zip, birthdate, docType, issueDate, expiryDate, gender, issuingCountry, licenseNumber, null, "Driver License");


                                    break;
                                default:
                                    break;
                            }
                            history_view_model.insert(history);
                            startActivity(intent);
                            finish();
                            admobShow();
                        }
                    }
                })
                .
                        addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ImageGalScannerActivity.this, "Barcode Detection Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
    }


    public static String getSubString(String mainString, String lastString, String startString) {
        String end_string = "";
        int end_index = mainString.indexOf(lastString);
        int start_index = mainString.indexOf(startString);
        Log.d("message", "" + startString.length());
        end_string = mainString.substring(start_index + startString.length(), end_index);
        return end_string;
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
        AdRequest ad_request = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(
                this,
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
            AdsDialog.getInstance().showLoader(this);
            m_interstitial_ad.show(this);
        }

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