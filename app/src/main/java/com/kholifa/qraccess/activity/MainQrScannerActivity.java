package com.kholifa.qraccess.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.kholifa.qraccess.AdsDialog;
import com.kholifa.qraccess.R;
import com.kholifa.qraccess.RoomDatabaseStore.HistoryList;
import com.kholifa.qraccess.RoomDatabaseStore.HistoryListViewModel;
import com.kholifa.qraccess.activity.CreationCode.GenerateActivity;

import java.io.IOException;
import java.lang.reflect.Field;


import static com.kholifa.qraccess.utillScan.matchSinglePrefixedField;

public class MainQrScannerActivity extends AppCompatActivity implements View.OnClickListener {
    SurfaceView surface_view;
    private BarcodeDetector barcode_detector;
    private CameraSource camera_source;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    ImageView img_gallery, img_history, img_create, img_flash;
    TextView txt_gallery, txt_create, txt_history, txt_flash;

    HistoryListViewModel history_view_model;
    public static Bitmap orignalbitmap;
    public static Uri string;
    Camera.Parameters params;
    Camera camera;
    boolean is_flash = false;
    HistoryList history;
    Intent intent;
    String dialogdata, dialogdata1, dialogcon, dialogcon1;
    boolean checkbarcode = false;
    Dialog rename_dialog;
    ImageView privacy_policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        history_view_model = new ViewModelProvider(this).get(HistoryListViewModel.class);
        admobInt();
        bind();
    }

    private void bind() {
        privacy_policy = findViewById(R.id.privacy_policy);
        surface_view = findViewById(R.id.surface_view);
        img_gallery = findViewById(R.id.img_gallery);
        img_history = findViewById(R.id.img_history);
        img_create = findViewById(R.id.img_create);
        img_flash = findViewById(R.id.img_flash);
        txt_gallery = findViewById(R.id.txt_gallery);
        txt_create = findViewById(R.id.txt_create);
        txt_history = findViewById(R.id.txt_history);
        txt_flash = findViewById(R.id.txt_flash);
        img_create.setOnClickListener(this);
        img_gallery.setOnClickListener(this);
        img_history.setOnClickListener(this);
        img_flash.setOnClickListener(this);
        txt_gallery.setOnClickListener(this);
        txt_create.setOnClickListener(this);
        txt_history.setOnClickListener(this);
        txt_flash.setOnClickListener(this);
        privacy_policy.setOnClickListener(this);
    }

    private void initialiseDetectorsAndSources() throws IOException {
        barcode_detector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        camera_source = new CameraSource.Builder(this, barcode_detector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();


        surface_view.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                try {
                    if (ActivityCompat.checkSelfPermission(MainQrScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        camera_source.start(surface_view.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(MainQrScannerActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                camera_source.stop();
                camera_source.release();
            }

        });

        barcode_detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }


            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (checkbarcode) {
                    return;
                }
                if (barcodes.size() != 0) {
                    checkbarcode = true;

                    history = null;
                    int valueType = barcodes.valueAt(0).valueFormat;
                    Barcode barcode = barcodes.valueAt(0);
                    String raw_value = barcode.rawValue;


                    intent = new Intent(MainQrScannerActivity.this, ShareScannerActivity.class);
                    switch (valueType) {
                        case com.google.mlkit.vision.barcode.Barcode.TYPE_WIFI:
                            String security_type = "UNKNOWN";
                            String ssid = barcode.wifi.ssid;
                            String password = barcode.wifi.password;
                            int type = barcode.wifi.encryptionType;
                            if (type == barcode.wifi.OPEN) {
                                security_type = "OPEN";
                            } else if (type == barcode.wifi.WPA) {
                                security_type = "WPA";
                            } else if (type == barcode.wifi.WEP) {
                                security_type = "WEP";
                            }
                            intent.putExtra("result", ssid);
                            intent.putExtra("resultType", "Wi-Fi");
                            intent.putExtra("password", password);
                            intent.putExtra("type", security_type);
                            history = new HistoryList(ssid, password, security_type, null, null, null, null, null, null, null, null, null, null, "Wi-Fi");
                            dialogdata = "WiFi ssid : ";
                            dialogdata1 = "";
                            dialogcon = ssid;
                            dialogcon1 = "";
                            break;
                        case com.google.mlkit.vision.barcode.Barcode.TYPE_URL:

                            if (raw_value.contains("facebook.com")) {
                                String text;
                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Facebook");

                                if (raw_value.contains("http://www.facebook.com/")) {
                                    text = raw_value.replace("http://www.facebook.com/", "");
                                } else {
                                    text = raw_value;
                                }
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Facebook");
                                dialogdata = "Facebook : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";
                            } else if (raw_value.contains("youtube.com")) {
                                String text;
                                if (raw_value.contains("http://www.youtube.com/watch?v=")) {
                                    text = raw_value.replace("http://www.youtube.com/watch?v=", "");
                                } else {
                                    text = raw_value;
                                }
                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Youtube");
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Youtube");
                                dialogdata = "Youtube : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";
                            } else if (raw_value.contains("instagram.com")) {
                                String text;
                                if (raw_value.contains("http://www.instagram.com/")) {
                                    text = raw_value.replace("http://www.instagram.com/", "");

                                } else {
                                    text = raw_value;
                                }
                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Instagram");
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Instagram");
                                dialogdata = "Instagram : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";

                            } else if (raw_value.contains("whatsapp.com")) {
                                String text;
                                if (raw_value.contains("https://api.whatsapp.com/send?phone=")) {
                                    text = raw_value.replace("https://api.whatsapp.com/send?phone=", "");
                                } else if (raw_value.contains("https://wa.me/")) {
                                    text = raw_value.replace("https://wa.me/", "");
                                } else {
                                    text = raw_value;
                                }
                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Whatsapp");
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Whatsapp");
                                dialogdata = "Whatsapp : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";

                            } else if (raw_value.contains("https://www.paypal.me/")) {
                                String text;
                                if (raw_value.contains("https://www.paypal.me/")) {
                                    text = raw_value.replace("https://www.paypal.me/", "");
                                } else {
                                    text = raw_value;
                                }
                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Paypal");
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Paypal");
                                dialogdata = "Paypal : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";

                            } else if (raw_value.contains("twitter.com")) {
                                String text;
                                if (raw_value.contains("https://twitter.com/")) {
                                    text = raw_value.replace("https://twitter.com/", "");
                                } else {
                                    text = raw_value;
                                }

                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Twitter");
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Twitter");
                                dialogdata = "Twitter : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";

                            } else {
                                String url = barcode.url.url;
                                intent.putExtra("result", url);
                                intent.putExtra("resultType", "URL");
                                history = new HistoryList(url, null, null, null, null, null, null, null, null, null, null, null, null, "URL");
                                dialogdata = "URL : ";
                                dialogdata1 = "";
                                dialogcon = url;
                                dialogcon1 = "";
                            }

                            break;
                        case com.google.mlkit.vision.barcode.Barcode.TYPE_CONTACT_INFO:

                            String name = barcode.contactInfo.name.formattedName;
                            Barcode.Phone[] phones = barcode.contactInfo.phones;
                            Barcode.Email[] emails = barcode.contactInfo.emails;
                            String[] urls = barcode.contactInfo.urls;
                            Barcode.Address[] addresses = barcode.contactInfo.addresses;
                            String org = barcode.contactInfo.organization;
                            String title = barcode.contactInfo.title;
                            String cont = "", email = "", add = "", Url = "", note, birth;
                            if (phones.length > 0) {
                                Barcode.Phone phone = phones[0];
                                cont = phone.number;
                            }
                            if (emails.length > 0) {
                                Barcode.Email email1 = emails[0];
                                email = email1.address;
                            }
                            if (addresses.length > 0) {
                                Barcode.Address add1 = addresses[0];
                                add = add1.addressLines[0];
                            }
                            if (urls.length > 0) {
                                Url = urls[0];
                            }

                            if (raw_value.contains("NOTE:") || raw_value.contains("BDAY:")) {
                                note = matchSinglePrefixedField("NOTE:", barcode.rawValue, ';', true);
                                birth = matchSinglePrefixedField("BDAY:", barcode.rawValue, ';', true);
                                if (note == null) {
                                    note = "";
                                }
                                if (birth == null) {
                                    birth = "";
                                }
                            } else {
                                note = "";
                                birth = "";
                            }
                            intent.putExtra("result", name);
                            intent.putExtra("contact", cont);
                            intent.putExtra("email", email);
                            intent.putExtra("org", org);
                            intent.putExtra("add", add);
                            intent.putExtra("note", note);
                            intent.putExtra("birth", birth);
                            intent.putExtra("url", Url);
                            intent.putExtra("title", title);

                            intent.putExtra("resultType", "Contact");
                            history = new HistoryList(name, cont, email, org, add, note, birth, Url, title, null, null, null, null, "Contact");
                            dialogdata = "Contact Name : ";
                            dialogdata1 = "Contact Num. : ";
                            dialogcon = name;
                            dialogcon1 = cont;
                            break;
                        case com.google.mlkit.vision.barcode.Barcode.TYPE_PHONE:
                            String phone = barcode.phone.number;
                            intent.putExtra("result", phone);
                            intent.putExtra("resultType", "Telephone");
                            history = new HistoryList(phone, null, null, null, null, null, null, null, null, null, null, null, null, "Telephone");
                            dialogdata = "Phone : ";
                            dialogdata1 = "";
                            dialogcon = phone;
                            dialogcon1 = "";
                            break;

                        case com.google.mlkit.vision.barcode.Barcode.TYPE_PRODUCT:

                            String result = barcode.displayValue;
                            intent.putExtra("result", result);
                            intent.putExtra("resultType", "Product");
                            history = new HistoryList(result, null, null, null, null, null, null, null, null, null, null, null, null, "Product");
                            dialogdata = "Product : ";
                            dialogdata1 = "";
                            dialogcon = result;
                            dialogcon1 = "";
                            break;

                        case com.google.mlkit.vision.barcode.Barcode.TYPE_TEXT:
                            if (raw_value.contains("MECARD")) {
                                String tname, tcont, temail, torg, tadd, tnote, tbirth, tUrl, ttitle;
                                tname = matchSinglePrefixedField("N:", barcode.rawValue, ';', true);
                                tcont = matchSinglePrefixedField("TEL:", barcode.rawValue, ';', true);
                                temail = matchSinglePrefixedField("EMAIL:", barcode.rawValue, ';', true);
                                torg = matchSinglePrefixedField("ORG:", barcode.rawValue, ';', true);
                                tadd = matchSinglePrefixedField("ADR:", barcode.rawValue, ';', true);
                                tnote = matchSinglePrefixedField("NOTE:", barcode.rawValue, ';', true);
                                tbirth = matchSinglePrefixedField("BDAY:", barcode.rawValue, ';', true);
                                tUrl = matchSinglePrefixedField("URL:", barcode.rawValue, ';', true);
                                ttitle = matchSinglePrefixedField("TITLE:", barcode.rawValue, ';', true);

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
                                dialogdata = "Contact Name : ";
                                dialogdata1 = "Contact Num. : ";
                                dialogcon = tname;
                                dialogcon1 = tcont;
                            } else if (raw_value.contains(".com") || raw_value.contains("www.") && !raw_value.contains("MECARD")) {
                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "URL");
                                history = new HistoryList(raw_value, null, null, null, null, null, null, null, null, null, null, null, null, "URL");
                                dialogdata = "URL : ";
                                dialogdata1 = "";
                                dialogcon = raw_value;
                                dialogcon1 = "";

                            } else if (raw_value.contains("facebook.com") || raw_value.contains("fb://profile/") || raw_value.contains("fb://page/")) {
                                String text;
                                if (raw_value.contains("fb://page/")) {
                                    text = raw_value.replace("fb://page/", "");

                                } else if (raw_value.contains("fb://profile/")) {
                                    text = raw_value.replace("fb://profile/", "");

                                } else if (raw_value.contains("http://www.facebook.com/")) {
                                    text = raw_value.replace("http://www.facebook.com/", "");
                                } else {
                                    text = raw_value;
                                }
                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Facebook");
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Facebook");
                                dialogdata = "Facebook : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";
                            } else if (raw_value.contains("youtube.com")) {
                                String text;
                                if (raw_value.contains("http://www.youtube.com/watch?v=")) {
                                    text = raw_value.replace("http://www.youtube.com/watch?v=", "");
                                } else {
                                    text = raw_value;
                                }
                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Youtube");
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Youtube");
                                dialogdata = "Youtube : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";
                            } else if (raw_value.contains("instagram.com") || raw_value.contains("instagram://user?username=")) {

                                String text;
                                if (raw_value.contains("instagram://user?username=")) {
                                    text = raw_value.replace("instagram://user?username=", "");

                                } else if (raw_value.contains("http://www.instagram.com/")) {
                                    text = raw_value.replace("http://www.instagram.com/", "");

                                } else {
                                    text = raw_value;
                                }

                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Instagram");
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Instagram");
                                dialogdata = "Instagram : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";
                            } else if (raw_value.contains("whatsapp://send?phone=") || raw_value.contains("whatsapp.com")) {

                                String text;
                                if (raw_value.contains("whatsapp://send?phone=")) {
                                    text = raw_value.replace("whatsapp://send?phone=", "");
                                } else if (raw_value.contains("https://api.whatsapp.com/send?phone=")) {
                                    text = raw_value.replace("https://api.whatsapp.com/send?phone=", "");
                                } else if (raw_value.contains("https://wa.me/")) {
                                    text = raw_value.replace("https://wa.me/", "");
                                } else {
                                    text = raw_value;
                                }
                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Whatsapp");
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Whatsapp");
                                dialogdata = "Whatsapp : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";
                            } else if (raw_value.contains("viber://add?number=")) {
                                String text;
                                if (raw_value.contains("viber://add?number=")) {
                                    text = raw_value.replace("viber://add?number=", "");
                                } else {
                                    text = raw_value;
                                }
                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Viber");
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Viber");
                                dialogdata = "Viber : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";
                            } else if (raw_value.contains("paypal.me")) {
                                String text;
                                if (raw_value.contains("https://www.paypal.me/")) {
                                    text = raw_value.replace("https://www.paypal.me/", "");
                                } else {
                                    text = raw_value;
                                }

                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Paypal");
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Paypal");
                                dialogdata = "Paypal : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";

                            } else if (raw_value.contains("twitter.com") || raw_value.contains("twitter://user?screen_name=")) {

                                String text;
                                if (raw_value.contains("twitter://user?screen_name=")) {
                                    text = raw_value.replace("twitter://user?screen_name=", "");
                                } else if (raw_value.contains("https://twitter.com/")) {
                                    text = raw_value.replace("https://twitter.com/", "");
                                } else {
                                    text = raw_value;
                                }
                                intent.putExtra("result", raw_value);
                                intent.putExtra("resultType", "Twitter");
                                history = new HistoryList(raw_value, text, null, null, null, null, null, null, null, null, null, null, null, "Twitter");
                                dialogdata = "Twitter : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";

                            } else if (raw_value.contains("spotify:")) {
                                String artist = matchSinglePrefixedField("search:", barcode.rawValue, ';', true);
                                String songName = raw_value.substring(raw_value.lastIndexOf(';') + 1);
                                intent.putExtra("result", artist);
                                intent.putExtra("song", songName);
                                intent.putExtra("rawvalue", raw_value);
                                intent.putExtra("resultType", "Spotify");
                                history = new HistoryList(artist, songName, raw_value, null, null, null, null, null, null, null, null, null, null, "Spotify");
                                dialogdata = "artist : ";
                                dialogdata1 = "song : ";
                                dialogcon = artist;
                                dialogcon1 = songName;
                            } else {
                                String text = barcode.displayValue;
                                intent.putExtra("result", text);
                                intent.putExtra("resultType", "Text");
                                history = new HistoryList(text, null, null, null, null, null, null, null, null, null, null, null, null, "Text");
                                dialogdata = "Text : ";
                                dialogdata1 = "";
                                dialogcon = text;
                                dialogcon1 = "";
                            }
                            break;
                        case com.google.mlkit.vision.barcode.Barcode.TYPE_EMAIL:
                            String email_add = barcode.email.address;
                            String sub = barcode.email.subject;
                            String body = barcode.email.body;
                            intent.putExtra("result", email_add);
                            intent.putExtra("sub", sub);
                            intent.putExtra("body", body);
                            intent.putExtra("resultType", "Email");
                            history = new HistoryList(email_add, sub, body, null, null, null, null, null, null, null, null, null, null, "Email");
                            dialogdata = "Email : ";
                            dialogdata1 = "Sub : ";
                            dialogcon = email_add;
                            dialogcon1 = sub;
                            break;
                        case com.google.mlkit.vision.barcode.Barcode.TYPE_SMS:
                            String sms = barcode.sms.message;
                            String phonesms = barcode.sms.phoneNumber;
                            intent.putExtra("result", sms);
                            intent.putExtra("phone", phonesms);
                            intent.putExtra("resultType", "SMS");
                            history = new HistoryList(sms, phonesms, null, null, null, null, null, null, null, null, null, null, null, "SMS");
                            dialogdata = "SMS : ";
                            dialogdata1 = "";
                            dialogcon = sms;
                            dialogcon1 = "";
                            break;
                        case com.google.mlkit.vision.barcode.Barcode.TYPE_UNKNOWN:

                            String text = barcode.displayValue;
                            intent.putExtra("result", text);
                            intent.putExtra("resultType", "Text");
                            history = new HistoryList(text, null, null, null, null, null, null, null, null, null, null, null, null, "Text");
                            dialogdata = "Text : ";
                            dialogdata1 = "";
                            dialogcon = text;
                            dialogcon1 = "";
                            break;
                        case com.google.mlkit.vision.barcode.Barcode.TYPE_CALENDAR_EVENT:
                            String s = barcode.calendarEvent.summary;
                            String loc = barcode.calendarEvent.location;
                            String disc = barcode.calendarEvent.description;
                            String sTimeHour = String.valueOf(barcode.calendarEvent.start.hours);
                            String sTimeMin = String.valueOf(barcode.calendarEvent.start.minutes);
                            String sDateDay = String.valueOf(barcode.calendarEvent.start.day);
                            String sDateMonth = String.valueOf(barcode.calendarEvent.start.month);
                            String sDateYear = String.valueOf(barcode.calendarEvent.start.year);
                            String eTimeHour = String.valueOf(barcode.calendarEvent.end.hours);
                            String eTimeMin = String.valueOf(barcode.calendarEvent.end.minutes);
                            String eDateDay = String.valueOf(barcode.calendarEvent.end.day);
                            String eDateMonth = String.valueOf(barcode.calendarEvent.end.month);
                            String eDateYear = String.valueOf(barcode.calendarEvent.end.year);

                            intent.putExtra("result", s);
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
                            history = new HistoryList(s, loc, disc, sTimeHour, sTimeMin, sDateDay, sDateMonth, sDateYear, eTimeHour, eTimeMin, eDateDay, eDateMonth, eDateYear, "Calendar");
                            dialogdata = "Calendar : ";
                            dialogdata1 = "";
                            dialogcon = s;
                            dialogcon1 = "";
                            break;
                        case com.google.mlkit.vision.barcode.Barcode.TYPE_ISBN:
                            intent.putExtra("result", barcode.rawValue);
                            intent.putExtra("resultType", "Book");
                            history = new HistoryList(barcode.rawValue, null, null, null, null, null, null, null, null, null, null, null, null, "Book");
                            dialogdata = "Book : ";
                            dialogdata1 = "";
                            dialogcon = barcode.rawValue;
                            dialogcon1 = "";
                            break;
                        case com.google.mlkit.vision.barcode.Barcode.TYPE_GEO:
                            String latitude = String.valueOf(barcode.geoPoint.lat);
                            String longitude = String.valueOf(barcode.geoPoint.lng);

                            intent.putExtra("result", latitude);
                            intent.putExtra("longitude", longitude);
                            intent.putExtra("resultType", "Geo");
                            history = new HistoryList(latitude, longitude, null, null, null, null, null, null, null, null, null, null, null, "Geo");
                            dialogdata = "Latitude : ";
                            dialogdata1 = "Longitude : ";
                            dialogcon = latitude;
                            dialogcon1 = longitude;
                            break;
                        case com.google.mlkit.vision.barcode.Barcode.TYPE_DRIVER_LICENSE:
                            String fname = barcode.driverLicense.firstName;
                            String lname = barcode.driverLicense.lastName;
                            String mname = barcode.driverLicense.middleName;
                            String street = barcode.driverLicense.addressStreet;
                            String state = barcode.driverLicense.addressState;
                            String city = barcode.driverLicense.addressCity;
                            String zip = barcode.driverLicense.addressZip;
                            String birthdate = barcode.driverLicense.birthDate;
                            String doc_type = barcode.driverLicense.documentType;
                            String expiry_date = barcode.driverLicense.expiryDate;
                            String issue_date = barcode.driverLicense.issueDate;
                            String gender = barcode.driverLicense.gender;
                            String issuing_country = barcode.driverLicense.issuingCountry;
                            String license_number = barcode.driverLicense.licenseNumber;

                            String full_name = fname + " " + mname + " " + lname;
                            String address = street + " " + city + " " + state + " " + zip;

                            intent.putExtra("result", full_name);
                            intent.putExtra("street", street);
                            intent.putExtra("city", city);
                            intent.putExtra("state", state);
                            intent.putExtra("zip", zip);
                            intent.putExtra("birthdate", birthdate);
                            intent.putExtra("docType", doc_type);
                            intent.putExtra("issueDate", issue_date);
                            intent.putExtra("expiryDate", expiry_date);
                            intent.putExtra("gender", gender);
                            intent.putExtra("issuingCountry", issuing_country);
                            intent.putExtra("licenseNumber", license_number);
                            intent.putExtra("resultType", "Driver License");
                            history = new HistoryList(full_name, street, city, state, zip, birthdate, doc_type, issue_date, expiry_date, gender, issuing_country, license_number, null, "Driver License");
                            dialogdata = "Driver License : ";
                            dialogdata1 = "";
                            dialogcon = full_name;
                            dialogcon1 = "";
                            break;

                        default:
                            break;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rename(dialogdata, dialogdata1, dialogcon, dialogcon1);
                        }
                    });


                } else {


                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            initialiseDetectorsAndSources();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_gallery:
                Intent photoPickerIntent = new Intent();
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), 1);
                break;
            case R.id.img_history:
                Intent intent = new Intent(MainQrScannerActivity.this, HistoryScannerActivity.class);
                startActivity(intent);
                admobShow();
                break;
            case R.id.img_create:
                Intent intent1 = new Intent(MainQrScannerActivity.this, GenerateActivity.class);
                startActivity(intent1);
                admobShow();
                break;
            case R.id.img_flash:
                changeFlashStatus();
                break;
            case R.id.txt_gallery:
                img_gallery.performClick();
                break;
            case R.id.txt_create:
                img_create.performClick();
                break;
            case R.id.txt_history:
                img_history.performClick();
                break;
            case R.id.txt_flash:
                img_flash.performClick();
                break;
            case R.id.privacy_policy:
                startActivity(new Intent(MainQrScannerActivity.this, PrivacyPolicyQRActivity.class));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(MainQrScannerActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    try {
                        camera_source.start(surface_view.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Toast.makeText(MainQrScannerActivity.this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == -1) {

            if (data != null) {
                try {
                    orignalbitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), data.getData());
                    string = data.getData();
                    Intent intent2 = new Intent(MainQrScannerActivity.this, ImageGalScannerActivity.class);
                    startActivity(intent2);
                    admobShow();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {

        }
    }

    public void changeFlashStatus() {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    camera = (Camera) field.get(camera_source);
                    if (camera != null) {
                        params = camera.getParameters();
                        if (!is_flash) {
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            is_flash = true;
                        } else {
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            is_flash = false;
                        }
                        camera.setParameters(params);
                    } else {

                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    public void rename(String data, String data1, String con, String con1) {
        rename_dialog = new Dialog(MainQrScannerActivity.this);
        rename_dialog.setCancelable(false);
        rename_dialog.setContentView(R.layout.dialog_save_code);
        LinearLayout lyCancel = rename_dialog.findViewById(R.id.ly_cancel);
        CardView cvOk = rename_dialog.findViewById(R.id.cv_ok);
        TextView txtFileName = rename_dialog.findViewById(R.id.txt_file_name);
        TextView txtFileNamesec = rename_dialog.findViewById(R.id.txt_file_name_sec);
        TextView txtContent = rename_dialog.findViewById(R.id.txt_content);
        TextView txtContentsec = rename_dialog.findViewById(R.id.txt_content_sec);
        if (data1.trim().equalsIgnoreCase("")) {
            txtFileNamesec.setVisibility(View.GONE);
            txtContentsec.setVisibility(View.GONE);
        } else {
            txtFileNamesec.setVisibility(View.VISIBLE);
            txtContentsec.setVisibility(View.VISIBLE);
        }

        txtFileName.setText(con);
        txtFileNamesec.setText(con1);
        txtContent.setText(data);
        txtContentsec.setText(data1);

        lyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkbarcode = false;
                rename_dialog.dismiss();
            }
        });

        cvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkbarcode = false;
                history_view_model.insert(history);
                startActivity(intent);
                rename_dialog.dismiss();
            }
        });

        Window window = rename_dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rename_dialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainQrScannerActivity.this, ExitQRAppActivity.class);
        intent.putExtra("activityexit", new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                finish();
            }
        });
        startActivity(intent);
    }

    private InterstitialAd mInterstitialAd;

    public void admobInt() {
        if (mInterstitialAd != null)
            return;
        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(
                this,
                getResources().getString(R.string.admob_int),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        mInterstitialAd = null;
                                        admobInt();
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        mInterstitialAd = null;
                                        admobInt();
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        mInterstitialAd = null;
                                        AdsDialog.getInstance().dismissLoader();
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }

    public void admobShow() {

        if (mInterstitialAd != null) {
            AdsDialog.getInstance().showLoader(this);
            mInterstitialAd.show(this);
        }

    }

}