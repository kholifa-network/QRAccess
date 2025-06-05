package com.kholifa.qraccess.activity.CreationCode;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.card.MaterialCardView;
import com.google.zxing.WriterException;
import com.kholifa.qraccess.AdsDialog;
import com.kholifa.qraccess.R;
import com.kholifa.qraccess.RoomDatabaseStore.CreationHistory;
import com.kholifa.qraccess.RoomDatabaseStore.HistoryListViewModel;
import com.kholifa.qraccess.utillScan;
import com.kholifa.qraccess.countrylist.CountryCodePicker_app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.kholifa.qraccess.utillScan.TextToImageEncode;


public class CreateActivity extends AppCompatActivity implements View.OnClickListener {
    private static SimpleDateFormat df_date;
    EditText edt_first, edt_second, edt_third, edt_pwd, edt_note, edt_org, edt_add, edt_cal_note;
    TextView txt_edt_right, txt_logo_name, txt_type_wifi, txt_birthday, txt_birthday_text, txt_all_day, txt_start, txt_start_date, txt_end, txt_end_date, txt_start_time, txt_end_time;
    ImageView img_edt_right, img_logo, img_back_create;
    MaterialCardView cv_id, cv_url, cv_channel, cv_none, cv_wep, cv_wpa;
    Switch toggle;
    HistoryListViewModel history_view_model;
    CountryCodePicker_app ccp;
    String flag;
    CardView cv_create;
    private int m_year, m_month, m_day, hour, minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);
        admobInt();
        bind();
        df_date = new SimpleDateFormat("yyyy-MM-dd");

        final Calendar c = Calendar.getInstance();
        m_year = c.get(Calendar.YEAR);
        m_month = c.get(Calendar.MONTH);
        m_day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        m_month = m_month + 1;


        flag = getIntent().getStringExtra("flag");

        if (flag.equalsIgnoreCase("Text")) {
            img_logo.setImageResource(R.drawable.ic_text);
            txt_logo_name.setText("Text");
            edt_first.setHint("Enter Text Here");
            img_edt_right.setVisibility(View.VISIBLE);
            txt_edt_right.setVisibility(View.VISIBLE);
            img_edt_right.setImageResource(R.drawable.ic_clear);
            txt_edt_right.setText("Clear");
        } else if (flag.equalsIgnoreCase("Clipboard")) {
            txt_logo_name.setText("ClipBoard");
            img_logo.setImageResource(R.drawable.ic_clipboard);
            ClipboardManager clipboard = (ClipboardManager)
                    getSystemService(Context.CLIPBOARD_SERVICE);
            String pasteData = "";
            if (!(clipboard.hasPrimaryClip())) {
                edt_first.setHint("Enter Text Here");
            } else {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                pasteData = String.valueOf(item.getText());
                edt_first.setText(pasteData);
            }

            img_edt_right.setVisibility(View.VISIBLE);
            txt_edt_right.setVisibility(View.VISIBLE);
            img_edt_right.setImageResource(R.drawable.ic_clear);
            txt_edt_right.setText("Clear");
        } else if (flag.equalsIgnoreCase("Contact")) {
            txt_logo_name.setText("Contact");
            img_logo.setImageResource(R.drawable.ic_contact);
            edt_third.setVisibility(View.VISIBLE);
            edt_second.setVisibility(View.VISIBLE);

            edt_first.setHint("Name");
            edt_second.setHint("Phone Number");
            edt_third.setHint("Email");
            img_edt_right.setVisibility(View.VISIBLE);
            txt_edt_right.setVisibility(View.VISIBLE);
            img_edt_right.setImageResource(R.drawable.ic_person);
            txt_edt_right.setText("Contact");
        } else if (flag.equalsIgnoreCase("URL")) {
            txt_logo_name.setText("Website");
            img_logo.setImageResource(R.drawable.ic_url);
            edt_first.setHint("Enter URL Here");
            img_edt_right.setVisibility(View.VISIBLE);
            txt_edt_right.setVisibility(View.VISIBLE);
            img_edt_right.setImageResource(R.drawable.ic_clear);
            txt_edt_right.setText("Clear");
        } else if (flag.equalsIgnoreCase("Facebook")) {
            txt_logo_name.setText("Facebook");
            img_logo.setImageResource(R.drawable.ic_facebook);
            edt_first.setHint("Enter Facebook ID");
            img_edt_right.setVisibility(View.VISIBLE);
            txt_edt_right.setVisibility(View.VISIBLE);
            img_edt_right.setImageResource(R.drawable.ic_clear);
            txt_edt_right.setText("Clear");
            cv_id.setVisibility(View.VISIBLE);
            cv_url.setVisibility(View.VISIBLE);
        } else if (flag.equalsIgnoreCase("Wi-Fi")) {
            txt_logo_name.setText("Wi-Fi");
            img_logo.setImageResource(R.drawable.ic_wifi);
            edt_first.setHint("Enter Nextwork name(SSID)");
            cv_wpa.setVisibility(View.VISIBLE);
            cv_wep.setVisibility(View.VISIBLE);
            cv_none.setVisibility(View.VISIBLE);
            edt_pwd.setVisibility(View.VISIBLE);
            txt_type_wifi.setVisibility(View.VISIBLE);
        } else if (flag.equalsIgnoreCase("Youtube")) {
            txt_logo_name.setText("Youtube");
            img_logo.setImageResource(R.drawable.ic_youtube);
            edt_first.setHint("Enter Youtube video ID");
            cv_id.setVisibility(View.VISIBLE);
            cv_channel.setVisibility(View.VISIBLE);
            cv_url.setVisibility(View.VISIBLE);
        } else if (flag.equalsIgnoreCase("Telephone")) {
            txt_logo_name.setText("Telephone");
            img_logo.setImageResource(R.drawable.ic_call);
            edt_first.setHint("Enter Phone Number");
            img_edt_right.setVisibility(View.VISIBLE);
            txt_edt_right.setVisibility(View.VISIBLE);
            img_edt_right.setImageResource(R.drawable.ic_person);
            txt_edt_right.setText("Contact");
        } else if (flag.equalsIgnoreCase("Email")) {
            txt_logo_name.setText("E-mail");
            img_logo.setImageResource(R.drawable.ic_email);
            img_edt_right.setVisibility(View.VISIBLE);
            txt_edt_right.setVisibility(View.VISIBLE);
            img_edt_right.setImageResource(R.drawable.ic_down);
            txt_edt_right.setText("More");
            edt_first.setHint("Enter Email Address");
        } else if (flag.equalsIgnoreCase("Instagram") || flag.equalsIgnoreCase("Twitter")) {
            if (flag.equalsIgnoreCase("Twitter")) {
                txt_logo_name.setText("Twitter");
                img_logo.setImageResource(R.drawable.ic_twitter);
                edt_first.setHint("@Username");
            } else {
                txt_logo_name.setText("Instagram");
                img_logo.setImageResource(R.drawable.ic_instagram);
                edt_first.setHint("Enter Instagram ID");
            }
            cv_id.setVisibility(View.VISIBLE);
            cv_url.setVisibility(View.VISIBLE);

        } else if (flag.equalsIgnoreCase("My Card")) {

            txt_logo_name.setText("My Card");
            img_logo.setImageResource(R.drawable.ic_my_card);
            img_edt_right.setVisibility(View.VISIBLE);
            txt_edt_right.setVisibility(View.VISIBLE);
            img_edt_right.setImageResource(R.drawable.ic_person);
            txt_edt_right.setText("Contact");
            edt_second.setVisibility(View.VISIBLE);
            edt_third.setVisibility(View.VISIBLE);
            edt_add.setVisibility(View.VISIBLE);
            txt_birthday.setVisibility(View.VISIBLE);
            txt_birthday_text.setVisibility(View.VISIBLE);
            edt_org.setVisibility(View.VISIBLE);
            edt_note.setVisibility(View.VISIBLE);
            edt_first.setHint("Name");
            edt_second.setHint("Phone Number");
            edt_third.setHint("Email");
            txt_birthday.setText(m_year + "-" + m_month + "-" + m_day);

        } else if (flag.equalsIgnoreCase("Whatsapp") || flag.equalsIgnoreCase("Viber")) {

            if (flag.equalsIgnoreCase("Viber")) {
                txt_logo_name.setText("Viber");
                img_logo.setImageResource(R.drawable.ic_viber);
            } else {
                txt_logo_name.setText("Whatsapp");
                img_logo.setImageResource(R.drawable.ic_whatsapp);
            }
            ccp.setVisibility(View.VISIBLE);
            edt_first.setHint("Phone Number");

        } else if (flag.equalsIgnoreCase("SMS")) {
            txt_logo_name.setText("SMS");
            img_logo.setImageResource(R.drawable.ic_sms);
            edt_first.setHint("Phone Number");
            img_edt_right.setVisibility(View.VISIBLE);
            txt_edt_right.setVisibility(View.VISIBLE);
            img_edt_right.setImageResource(R.drawable.ic_person);
            txt_edt_right.setText("Contact");
            edt_second.setVisibility(View.VISIBLE);
            edt_second.setHint("please Enter Content");
        } else if (flag.equalsIgnoreCase("Paypal")) {
            txt_logo_name.setText("Paypal");
            img_logo.setImageResource(R.drawable.ic_paypal);
            edt_first.setHint("Paypal.Me Link");
            cv_id.setVisibility(View.VISIBLE);
            cv_url.setVisibility(View.VISIBLE);
        } else if (flag.equalsIgnoreCase("Calendar")) {
            txt_logo_name.setText("Calendar");
            img_logo.setImageResource(R.drawable.ic_calendar);
            edt_first.setHint("Title");
            img_edt_right.setVisibility(View.VISIBLE);
            txt_edt_right.setVisibility(View.VISIBLE);
            img_edt_right.setImageResource(R.drawable.ic_down);
            txt_edt_right.setText("More");
            txt_all_day.setVisibility(View.VISIBLE);
            toggle.setVisibility(View.VISIBLE);
            txt_start.setVisibility(View.VISIBLE);
            txt_start_date.setVisibility(View.VISIBLE);
            txt_start_time.setVisibility(View.VISIBLE);
            txt_end.setVisibility(View.VISIBLE);
            txt_end_date.setVisibility(View.VISIBLE);
            txt_end_time.setVisibility(View.VISIBLE);
            edt_cal_note.setVisibility(View.VISIBLE);

            txt_start_date.setText(m_year + "-" + m_month + "-" + m_day);
            txt_end_date.setText(m_year + "-" + m_month + "-" + m_day);
            txt_start_time.setText(hour + ":" + minute);
            txt_end_time.setText(hour + ":" + minute);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date date = sdf.parse(txt_start_date.getText().toString());
                Calendar ca = Calendar.getInstance();
                ca.setTime(date);
                ca.set(Calendar.HOUR_OF_DAY, 0);
                ca.set(Calendar.MINUTE, 0);
                ca.set(Calendar.SECOND, 0);
                ca.set(Calendar.MILLISECOND, 0);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (flag.equalsIgnoreCase("Spotify")) {
            txt_logo_name.setText("Spotify");
            img_logo.setImageResource(R.drawable.ic_spotify);
            edt_first.setHint("Artist Name");
            edt_second.setVisibility(View.VISIBLE);
            edt_second.setHint("Song Name");

        } else {

        }
        history_view_model = new ViewModelProvider(this).get(HistoryListViewModel.class);
        img_edt_right.setOnClickListener(this);
        txt_edt_right.setOnClickListener(this);
        cv_create.setOnClickListener(this);
        cv_id.setOnClickListener(this);
        cv_url.setOnClickListener(this);
        cv_channel.setOnClickListener(this);
        cv_wpa.setOnClickListener(this);
        cv_wep.setOnClickListener(this);
        cv_none.setOnClickListener(this);
        txt_birthday.setOnClickListener(this);
        txt_start_time.setOnClickListener(this);
        txt_start_date.setOnClickListener(this);
        txt_end_time.setOnClickListener(this);
        txt_end_date.setOnClickListener(this);
        img_back_create.setOnClickListener(this);

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txt_start_time.setVisibility(View.GONE);
                    txt_end_time.setVisibility(View.GONE);

                } else {
                    txt_start_time.setVisibility(View.VISIBLE);
                    txt_end_time.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void bind() {

        cv_create = findViewById(R.id.cv_create);
        img_logo = findViewById(R.id.img_logo);
        txt_logo_name = findViewById(R.id.txt_logo_name);
        edt_first = findViewById(R.id.edt_first);
        img_edt_right = findViewById(R.id.img_edt_right);
        txt_edt_right = findViewById(R.id.txtEdtRight);
        edt_second = findViewById(R.id.edt_second);
        edt_third = findViewById(R.id.edt_third);
        cv_id = findViewById(R.id.cv_id);
        cv_url = findViewById(R.id.cv_url);
        cv_channel = findViewById(R.id.cv_channel);
        cv_wpa = findViewById(R.id.cv_wpa);
        cv_wep = findViewById(R.id.cv_wep);
        cv_none = findViewById(R.id.cv_none);
        txt_type_wifi = findViewById(R.id.txt_type_wifi);
        edt_pwd = findViewById(R.id.edt_pwd);
        edt_note = findViewById(R.id.edt_note);
        edt_org = findViewById(R.id.edt_org);
        txt_birthday = findViewById(R.id.txt_birthday);
        txt_birthday_text = findViewById(R.id.txt_birthday_text);
        edt_add = findViewById(R.id.edt_add);
        ccp = (CountryCodePicker_app) findViewById(R.id.ccp);
        txt_all_day = findViewById(R.id.txt_all_day);
        txt_start = findViewById(R.id.txt_start);
        txt_start_date = findViewById(R.id.txt_start_date);
        txt_start_time = findViewById(R.id.txt_start_time);
        txt_end = findViewById(R.id.txt_end);
        txt_end_date = findViewById(R.id.txt_end_date);
        txt_end_time = findViewById(R.id.txt_end_time);
        toggle = findViewById(R.id.toggle);
        edt_cal_note = findViewById(R.id.edt_cal_note);
        img_back_create = findViewById(R.id.img_back_create);


    }

    private boolean isValidMail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1 && i2 == -1) {
            Cursor query = this.getContentResolver().query(intent.getData(), null, null, null, null);
            if (query.moveToFirst()) {
                String name = query.getString(query.getColumnIndex("display_name"));
                String id = query.getString(query.getColumnIndex(ContactsContract.Contacts._ID));
                Integer hasPhone = query.getInt(query.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));


                String email = null;
                Cursor ce = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
                if (ce != null && ce.moveToFirst()) {
                    email = ce.getString(ce.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    ce.close();
                }


                String phone = null;
                if (hasPhone > 0) {
                    Cursor cp = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (cp != null && cp.moveToFirst()) {
                        phone = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        cp.close();
                    }
                }
                if (flag.equalsIgnoreCase("Contact") || flag.equalsIgnoreCase("My Card")) {
                    edt_first.setText(name);
                    edt_second.setText(phone);
                    edt_third.setText(email);
                } else if (flag.equalsIgnoreCase("Telephone") || flag.equalsIgnoreCase("SMS")) {
                    edt_first.setText(phone);
                }

            }
        }
    }

    public static String formatDate(String date, String init_date_format, String end_date_format) throws ParseException {
        Date init_date = new SimpleDateFormat(init_date_format).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(end_date_format);
        String parsed_date = formatter.format(init_date);
        return parsed_date;
    }

    public static String formatTime(String date, String init_date_format, String end_date_format) throws ParseException {
        Date init_date = new SimpleDateFormat(init_date_format).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(end_date_format);
        String parsed_date = formatter.format(init_date);
        return parsed_date;
    }

    public static Date convertDate(int year, int month, int day) {
        String _year = String.format(Locale.ENGLISH, "%04d", year);
        String _month = String.format(Locale.ENGLISH, "%02d", month);
        String _day = String.format(Locale.ENGLISH, "%02d", day);
        String string_date = _year.concat("-").concat(_month).concat("-").concat(_day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            Date date = sdf.parse(string_date);

            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            return c.getTime();



        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean compareDate(Date start, Date end) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String _start = df.format(start);
        String _end = df.format(end);
        return _start.equalsIgnoreCase(_end);
    }

    public static String getLongtime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis() + "";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_create:
                onBackPressed();
                break;
            case R.id.cv_id:
                if (flag.equalsIgnoreCase("Facebook")) {
                    edt_first.setHint("Enter Facebook ID");
                } else if (flag.equalsIgnoreCase("Youtube")) {
                    edt_first.setHint("Enter Youtube video ID");
                } else if (flag.equalsIgnoreCase("Instagram")) {
                    edt_first.setHint("Enter Username");
                } else if (flag.equalsIgnoreCase("Paypal")) {
                    edt_first.setHint("Paypal.Me Link");
                } else if (flag.equalsIgnoreCase("Twitter")) {
                    edt_first.setHint("@Username");
                }

                break;
            case R.id.cv_url:
                if (flag.equalsIgnoreCase("Facebook")) {
                    edt_first.setHint("Enter Facebook Profile URL");
                } else if (flag.equalsIgnoreCase("Youtube")) {
                    edt_first.setHint("Enter Youtube URL");
                } else if (flag.equalsIgnoreCase("Instagram")) {
                    edt_first.setHint("Enter Instagram URL");
                } else if (flag.equalsIgnoreCase("Paypal")) {
                    edt_first.setHint("Paypal.Me Username");
                } else if (flag.equalsIgnoreCase("Twitter")) {
                    edt_first.setHint("Enter URL");
                }
                break;
            case R.id.cv_channel:
                edt_first.setHint("Enter Youtube Channel ID");
                break;
            case R.id.cv_wpa:
                txt_type_wifi.setText("WPA");
                edt_pwd.setVisibility(View.VISIBLE);
                break;
            case R.id.cv_wep:
                txt_type_wifi.setText("WEP");
                edt_pwd.setVisibility(View.VISIBLE);
                break;
            case R.id.cv_none:
                txt_type_wifi.setText("OPEN");
                edt_pwd.setVisibility(View.GONE);
                break;
            case R.id.txt_birthday:

                DatePickerDialog date_picker_dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txt_birthday.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, m_year, m_month - 1, m_day);
                date_picker_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                date_picker_dialog.getDatePicker().setSpinnersShown(true);
                date_picker_dialog.getDatePicker().setCalendarViewShown(false);
                date_picker_dialog.show();
                break;
            case R.id.txt_start_date:
                DatePickerDialog sdate_picker_dialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        txt_start_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        String d1 = txt_start_date.getText().toString();
                        String d2 = txt_end_date.getText().toString();

                        try {
                            if (df_date.parse(d1).before(df_date.parse(d2))) {
                            } else if (df_date.parse(d1).equals(df_date.parse(d2))) {
                                txt_end_time.setText(txt_start_time.getText().toString());
                            } else {
                                txt_end_date.setText(txt_start_date.getText().toString());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, m_year, m_month - 1, m_day);
                sdate_picker_dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                sdate_picker_dialog.show();
                break;
            case R.id.txt_end_date:
                DatePickerDialog edate_picker_dialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                txt_end_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                String d1 = txt_start_date.getText().toString();
                                String d2 = txt_end_date.getText().toString();
                                try {
                                    if (df_date.parse(d1).equals(df_date.parse(d2))) {
                                        txt_end_time.setText(txt_start_time.getText().toString());
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, m_year, m_month - 1, m_day);
                Date date;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                try {
                    date = sdf.parse(txt_start_date.getText().toString());
                    edate_picker_dialog.getDatePicker().setMinDate(Long.parseLong(getLongtime(date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                edate_picker_dialog.show();
                break;
            case R.id.txt_start_time:

                TimePickerDialog start_time_picker;
                start_time_picker = new TimePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        try {
                            Date start_date = sdf2.parse(txt_start_date.getText().toString());
                            Date end_date = sdf2.parse(txt_end_date.getText().toString());
                            if (compareDate(start_date, end_date)) {

                                Date time2 = new SimpleDateFormat("HH:mm").parse(txt_end_time.getText().toString());
                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.setTime(time2);
                                calendar2.add(Calendar.DATE, 1);

                                txt_start_time.setText(selectedHour + ":" + selectedMinute);
                                Date d = new SimpleDateFormat("HH:mm").parse(txt_start_time.getText().toString());
                                Calendar calendar3 = Calendar.getInstance();
                                calendar3.setTime(d);
                                calendar3.add(Calendar.DATE, 1);

                                Date x = calendar3.getTime();

                                if (calendar2.getTime().before(x)) {
                                    txt_end_time.setText(selectedHour + 1 + ":" + selectedMinute);
                                }
                            } else {
                                txt_start_time.setText(selectedHour + ":" + selectedMinute);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }, hour, minute, true);

                start_time_picker.show();
                break;

            case R.id.txt_end_time:
                TimePickerDialog end_time_picker;
                end_time_picker = new TimePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        try {
                            Date start_date = sdf2.parse(txt_start_date.getText().toString());
                            Date end_date = sdf2.parse(txt_end_date.getText().toString());
                            if (compareDate(start_date, end_date)) {
                                CharSequence pri = txt_end_time.getText();
                                txt_end_time.setText(selectedHour + ":" + selectedMinute);
                                Date time2 = new SimpleDateFormat("HH:mm").parse(txt_end_time.getText().toString());
                                Calendar calendar2 = Calendar.getInstance();
                                calendar2.setTime(time2);
                                calendar2.add(Calendar.DATE, 1);

                                Date d = new SimpleDateFormat("HH:mm").parse(txt_start_time.getText().toString());
                                Calendar calendar3 = Calendar.getInstance();
                                calendar3.setTime(d);
                                calendar3.add(Calendar.DATE, 1);

                                Date x = calendar3.getTime();
                                if (calendar2.getTime().before(x)) {
                                    txt_end_time.setText(pri);
                                }
                            } else {
                                txt_end_time.setText(selectedHour + ":" + selectedMinute);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, hour, minute, true);
                end_time_picker.show();
                break;
            case R.id.img_edt_right:
                if (flag.equalsIgnoreCase("Text") || flag.equalsIgnoreCase("Clipboard") || flag.equalsIgnoreCase("URL")) {
                    edt_first.getText().clear();
                } else if (flag.equalsIgnoreCase("Contact") || flag.equalsIgnoreCase("Telephone") || flag.equalsIgnoreCase("My Card") || flag.equalsIgnoreCase("SMS")) {
                    startActivityForResult(new Intent("android.intent.action.PICK", ContactsContract.Contacts.CONTENT_URI), 1);
                } else if (flag.equalsIgnoreCase("Email")) {

                    if (edt_second.getVisibility() == View.GONE && edt_third.getVisibility() == View.GONE) {
                        edt_second.setVisibility(View.VISIBLE);
                        edt_third.setVisibility(View.VISIBLE);
                        edt_second.setHint("Subject");
                        edt_third.setHint("Enter Content");
                        img_edt_right.setImageResource(R.drawable.ic_up);
                        txt_edt_right.setText("Close");
                    } else {
                        edt_second.setVisibility(View.GONE);
                        edt_third.setVisibility(View.GONE);
                        img_edt_right.setImageResource(R.drawable.ic_down);
                        txt_edt_right.setText("More");
                    }

                } else if (flag.equalsIgnoreCase("Calendar")) {
                    if (edt_second.getVisibility() == View.GONE) {
                        edt_second.setVisibility(View.VISIBLE);
                        edt_second.setHint("location");
                        img_edt_right.setImageResource(R.drawable.ic_up);
                        txt_edt_right.setText("Close");
                    } else {
                        edt_second.setVisibility(View.GONE);
                        img_edt_right.setImageResource(R.drawable.ic_down);
                        txt_edt_right.setText("More");
                    }

                }
                break;
            case R.id.txtEdtRight:
                img_edt_right.performClick();
                break;
            case R.id.cv_create:
                if (flag.equalsIgnoreCase("Text") || flag.equalsIgnoreCase("Clipboard")) {
                    if (edt_first.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter Text", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            utillScan.generate_bitmap = TextToImageEncode(edt_first.getText().toString());
                            utillScan.string_data = edt_first.getText().toString();
                            if (flag.equalsIgnoreCase("Text")) {
                                utillScan.string_data_type = "Text";
                            } else if (flag.equalsIgnoreCase("Clipboard")) {
                                utillScan.string_data_type = "Clipboard";
                            }
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                    }
                } else if (flag.equalsIgnoreCase("Contact")) {

                    String name = edt_first.getText().toString();
                    String phonenumber = edt_second.getText().toString();
                    String email = edt_third.getText().toString();
                    String append = "MECARD:N:" + name + ";TEL:" + phonenumber + ";EMAIL:" + email + ";;";

                    if (edt_first.getText().toString().trim().equalsIgnoreCase("") || edt_second.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter Name & Phone number", Toast.LENGTH_SHORT).show();
                    } else if (!isValidMail(edt_third.getText().toString()) && !edt_third.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter valid Email", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            utillScan.generate_bitmap = TextToImageEncode(append);
                            utillScan.string_data_type = "Contact";
                            utillScan.string_data = edt_first.getText().toString();
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                    }
                } else if (flag.equalsIgnoreCase("URL")) {
                    if (edt_first.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter URL", Toast.LENGTH_SHORT).show();
                    } else if (!Patterns.WEB_URL.matcher(edt_first.getText().toString()).matches()) {
                        Toast.makeText(CreateActivity.this, "Please,Enter Valid URL", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            utillScan.generate_bitmap = TextToImageEncode(edt_first.getText().toString());
                            utillScan.string_data_type = "URL";
                            utillScan.string_data = edt_first.getText().toString();
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (flag.equalsIgnoreCase("Facebook")) {
                    if (edt_first.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please, Enter the Content", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String fb_making = "";
                            if (edt_first.getText().toString().trim().contains("facebook.com")) {
                                fb_making = edt_first.getText().toString().trim();
                            } else {
                                fb_making = "http://www.facebook.com/" + edt_first.getText().toString().trim();
                            }
                            utillScan.generate_bitmap = TextToImageEncode(fb_making);
                            utillScan.string_data_type = "Facebook";
                            utillScan.string_data = edt_first.getText().toString();
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (flag.equalsIgnoreCase("Wi-Fi")) {
                    String ssid = edt_first.getText().toString();
                    String pwd = edt_pwd.getText().toString();
                    String security_type = txt_type_wifi.getText().toString();
                    String append = "WIFI:S:" + ssid + ";T:" + security_type + ";P:" + pwd + ";;";

                    if (edt_first.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter Network name", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            utillScan.generate_bitmap = TextToImageEncode(append);
                            utillScan.string_data_type = "Wi-Fi";
                            utillScan.string_data = ssid;
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (flag.equalsIgnoreCase("Youtube")) {

                    if (edt_first.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please, Enter the Content", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String fb_making = "";
                            if (edt_first.getText().toString().trim().contains("youtube.com")) {
                                fb_making = edt_first.getText().toString().trim();
                            } else {
                                fb_making = "http://www.youtube.com/watch?v=" + edt_first.getText().toString().trim();
                            }
                            utillScan.generate_bitmap = TextToImageEncode(fb_making);
                            utillScan.string_data_type = "Youtube";
                            utillScan.string_data = edt_first.getText().toString();
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (flag.equalsIgnoreCase("Telephone")) {

                    if (edt_first.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter Number", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            utillScan.generate_bitmap = TextToImageEncode("tel:" + edt_first.getText().toString());
                            utillScan.string_data = edt_first.getText().toString();
                            utillScan.string_data_type = "Telephone";
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (flag.equalsIgnoreCase("Email")) {
                    if (edt_first.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter Email", Toast.LENGTH_SHORT).show();
                    } else if (!isValidMail(edt_first.getText().toString())) {
                        Toast.makeText(CreateActivity.this, "Please,Enter valid Email", Toast.LENGTH_SHORT).show();
                    } else {
                        String sub, content;
                        try {
                            if (edt_second.getVisibility() == View.GONE && edt_third.getVisibility() == View.GONE) {
                                sub = "";
                                content = "";
                            } else {
                                sub = edt_second.getText().toString();
                                content = edt_third.getText().toString();
                            }
                            String email = edt_first.getText().toString();

                            String append = "MATMSG:TO:" + email + ";SUB:" + sub + ";BODY:" + content + ";;";
                            utillScan.generate_bitmap = TextToImageEncode(append);
                            utillScan.string_data = edt_first.getText().toString();
                            utillScan.string_data_type = "E-mail";
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (flag.equalsIgnoreCase("Instagram") || flag.equalsIgnoreCase("Twitter")) {
                    if (edt_first.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter Content", Toast.LENGTH_SHORT).show();
                    } else {

                        try {

                            if (flag.equalsIgnoreCase("Twitter")) {

                                String TwitterMaking = "";
                                if (edt_first.getText().toString().trim().contains("twitter.com")) {
                                    TwitterMaking = edt_first.getText().toString();
                                } else {
                                    TwitterMaking = "https://twitter.com/" + edt_first.getText().toString();
                                }
                                utillScan.generate_bitmap = TextToImageEncode(TwitterMaking);
                                utillScan.string_data_type = "Twitter";

                            } else {
                                String InstaMaking = "";
                                if (edt_first.getText().toString().trim().contains("instagram.com")) {
                                    InstaMaking = edt_first.getText().toString();
                                } else {
                                    InstaMaking = "http://www.instagram.com/" + edt_first.getText().toString();
                                }
                                utillScan.generate_bitmap = TextToImageEncode(InstaMaking);
                                utillScan.string_data_type = "Instagram";
                            }
                            utillScan.string_data = edt_first.getText().toString();
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (flag.equalsIgnoreCase("My Card")) {

                    String name = edt_first.getText().toString();
                    String phone = edt_second.getText().toString();
                    String email = edt_third.getText().toString();
                    String add = edt_add.getText().toString();
                    String org = edt_org.getText().toString();
                    String note = edt_note.getText().toString();
                    String birth = "";
                    try {
                        birth = formatDate(txt_birthday.getText().toString(), "yyyy-MM-dd", "yyyyMMdd");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String append = "MECARD:N:" + name + ";ORG:" + org + ";TEL:" + phone + ";EMAIL:" + email + ";BDAY:" + birth + ";ADR:" + add + ";NOTE:" + note + ";;";


                    if (edt_first.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter Name", Toast.LENGTH_SHORT).show();
                    } else if (!isValidMail(edt_third.getText().toString()) && !edt_third.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter valid Email", Toast.LENGTH_SHORT).show();
                    } else {

                        try {
                            utillScan.generate_bitmap = TextToImageEncode(append);
                            utillScan.string_data_type = "My Card";
                            utillScan.string_data = name;
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (flag.equalsIgnoreCase("Whatsapp") || flag.equalsIgnoreCase("Viber")) {

                    ccp.registerPhoneNumberTextView(edt_first);

                    if (edt_first.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter Number", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            if (flag.equalsIgnoreCase("Viber")) {
                                utillScan.generate_bitmap = TextToImageEncode("viber://add?number=" + ccp.getFullNumberWithPlus());
                                utillScan.string_data_type = "Viber";
                            } else {
                                utillScan.generate_bitmap = TextToImageEncode("whatsapp://send?phone=" + ccp.getFullNumberWithPlus());
                                utillScan.string_data_type = "Whatsapp";
                            }
                            utillScan.string_data = edt_first.getText().toString();
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (flag.equalsIgnoreCase("SMS")) {

                    if (edt_first.getText().toString().trim().equalsIgnoreCase("") && edt_second.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter content First", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String append = "smsto:" + edt_first.getText().toString().trim() + ":" + edt_second.getText().toString().trim();
                            utillScan.generate_bitmap = TextToImageEncode(append);
                            utillScan.string_data = edt_first.getText().toString();
                            utillScan.string_data_type = "SMS";
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (flag.equalsIgnoreCase("Paypal")) {
                    String append;
                    if (edt_first.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter content", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            if (edt_first.getText().toString().trim().contains("paypal.me/")) {
                                append = edt_first.getText().toString().trim();
                            } else {
                                append = "https://www.paypal.me/" + edt_first.getText().toString().trim();
                            }

                            utillScan.generate_bitmap = TextToImageEncode(append);
                            utillScan.string_data = edt_first.getText().toString();
                            utillScan.string_data_type = "Paypal";
                            callNext();
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (flag.equalsIgnoreCase("Calendar")) {

                    if (edt_first.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter Title", Toast.LENGTH_SHORT).show();
                    } else {
                        String append, startDate = "", endDate = "", startTime = "", endTime = "";
                        try {
                            startDate = formatDate(txt_start_date.getText().toString(), "yyyy-MM-dd", "yyyyMMdd");
                            endDate = formatDate(txt_end_date.getText().toString(), "yyyy-MM-dd", "yyyyMMdd");
                            startTime = formatTime(txt_start_time.getText().toString(), "HH:MM", "HHMMSS");
                            endTime = formatTime(txt_end_time.getText().toString(), "HH:MM", "HHMMSS");
                            Log.d("startTime", "++++++++++++++++++" + startTime);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            if (toggle.isChecked()) {
                                append = "BEGIN:VEVENT\nSUMMARY:" + edt_first.getText().toString().trim() + "\nDTSTART;VALUE=DATE:" + startDate + "\nDTEND;VALUE=DATE:" + endDate + "\nLOCATION:" + edt_second.getText().toString() + "\nDESCRIPTION:" + edt_cal_note.getText().toString() + "\nEND:VEVENT";
                            } else {



                                append = "BEGIN:VEVENT\nSUMMARY:" + edt_first.getText().toString().trim() + "\nDTSTART:" + startDate + "T" + startTime + "\nDTEND:" + endDate + "T" + endTime +
                                        "\nLOCATION:" + edt_second.getText().toString() + "\nDESCRIPTION:" + edt_cal_note.getText().toString() + "\nEND:VEVENT";
                            }
                            utillScan.generate_bitmap = TextToImageEncode(append);
                            utillScan.string_data = edt_first.getText().toString();
                            utillScan.string_data_type = "Calendar";
                            callNext();
                        } catch (WriterException e1) {
                            e1.printStackTrace();
                        }
                    }
                } else if (flag.equalsIgnoreCase("Spotify")) {

                    if (edt_first.getText().toString().trim().equalsIgnoreCase("") || edt_second.getText().toString().trim().equalsIgnoreCase("")) {
                        Toast.makeText(CreateActivity.this, "Please,Enter content", Toast.LENGTH_SHORT).show();
                    } else {
                        String append = "spotify:search:" + edt_first.getText().toString() + ";" + edt_second.getText().toString();
                        try {
                            utillScan.generate_bitmap = TextToImageEncode(append);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                        utillScan.string_data = edt_first.getText().toString();
                        utillScan.string_data_type = "Spotify";
                        callNext();
                    }
                }
                break;

        }
    }

    public void callNext() {
        String s = utillScan.BitMapToString(utillScan.generate_bitmap);
        CreationHistory create_history = new CreationHistory(utillScan.string_data, utillScan.string_data_type, s);
        history_view_model.insert(create_history);
        Intent intent = new Intent(CreateActivity.this, CreateSaveActivity.class);
        intent.putExtra("CreateActivity", new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                finish();
            }
        });
        startActivity(intent);
        admobShow();
    }

    public static boolean checkDates(String d1, String d2) {
        boolean b = false;
        try {
            if (df_date.parse(d1).before(df_date.parse(d2))) {
                b = true;
            } else if (df_date.parse(d1).equals(df_date.parse(d2))) {
                b = true;
            } else {
                b = false;
            }
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return b;
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
}