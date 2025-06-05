package com.kholifa.qraccess.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.card.MaterialCardView;
import com.kholifa.qraccess.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

public class ShareScannerActivity extends AppCompatActivity implements View.OnClickListener{
    TextView txt_data_type, txt_first, txt_second, txt_third, txt_forth, txt_result_sec, txt_result_third, txt_result_forth, txt_result_fifth, txt_result_sixth, txt_result_seven, txt_result_eight, txt_result_nine;
    TextView txt_result_heading, txt_result_sec_heading, txt_result_third_heading, txt_result_forth_heading, txt_result_fifth_heading, txt_result_sixth_heading, txt_result_seven_heading, txt_result_eight_heading, txt_result_nine_heading;
    String result, result_type, contact, email, password, security_type, sub, body, org, add, note, birth, url, title, phone, loc, disc, song, longitude, rawvalue, street, city, state, zip, birthdate, doc_type, issue_date, expiry_date, gender, issuing_country, license_number;
    int s_time_hour, s_time_min, s_date_day, s_date_month, s_date_year, e_time_hour, e_time_min, e_date_day, e_date_month, e_date_year;
    MaterialCardView cv_first, cv_second, cv_third, cv_forth;
    ImageView img_first, img_second, img_third, img_forth, img_down, img_up, img_back;
    int contact_const = 100;
    CardView cv_copy;
    TextView txt_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app);

        result = getIntent().getStringExtra("result");
        result_type = getIntent().getStringExtra("resultType");
        admobBanner();
        bind();

        img_down.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                img_down.setVisibility(View.GONE);
                img_up.setVisibility(View.VISIBLE);
                txt_result.setMaxLines(Integer.MAX_VALUE);

            }
        });

        img_up.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                img_up.setVisibility(View.GONE);
                img_down.setVisibility(View.VISIBLE);
                txt_result.setMaxLines(3);

            }
        });

        if (result.trim().equalsIgnoreCase("")) {
            txt_result.setVisibility(View.GONE);
            txt_result_heading.setVisibility(View.GONE);
            img_down.setVisibility(View.GONE);
        } else {
            if (result_type.equalsIgnoreCase("URL") || result_type.equalsIgnoreCase("Facebook") || result_type.equalsIgnoreCase("Twitter") || result_type.equalsIgnoreCase("Youtube") || result_type.equalsIgnoreCase("Instagram") || result_type.equalsIgnoreCase("Whatsapp") || result_type.equalsIgnoreCase("Viber") || result_type.equalsIgnoreCase("Paypal")) {
                SpannableString content = new SpannableString(result);
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                txt_result.setText(content);

                txt_result.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                txt_result.setTextColor(Color.parseColor("#4fcafe"));
                txt_result.setGravity(Gravity.CENTER);
                txt_result_heading.setVisibility(View.GONE);
                img_down.setVisibility(View.GONE);
            } else {
                txt_result.setText(result.trim());

                txt_result.post(new Runnable() {
                    @Override
                    public void run() {
                        int lineCount = txt_result.getLineCount();
                        if (lineCount <= 3) {
                            img_down.setVisibility(View.GONE);
                        }
                    }
                });

                if (result_type.equalsIgnoreCase("Geo")) {
                    txt_result_heading.setText("Latitude :");
                } else if (result_type.equalsIgnoreCase("Contact")) {
                    txt_result_heading.setText("Name :");
                } else if (result_type.equalsIgnoreCase("Telephone")) {
                    txt_result_heading.setText("Telephone :");
                } else if (result_type.equalsIgnoreCase("Wi-Fi")) {
                    txt_result_heading.setText("Network :");
                } else if (result_type.equalsIgnoreCase("Email")) {
                    txt_result_heading.setText("To :");
                } else if (result_type.equalsIgnoreCase("SMS")) {
                    txt_result_heading.setText("Content :");
                } else if (result_type.equalsIgnoreCase("Calendar")) {
                    txt_result_heading.setText("Title :");
                } else if (result_type.equalsIgnoreCase("Spotify")) {
                    txt_result_heading.setText("Artist :");
                } else if (result_type.equalsIgnoreCase("Driver License")) {
                    txt_result_heading.setText("Driver Name :");
                }

            }
        }
        txt_data_type.setText(result_type);

        if (result_type.equalsIgnoreCase("Text")) {
            txt_first.setText("Send SMS");
            img_first.setImageResource(R.drawable.ic_sms);
            txt_second.setText("Send Email");
            img_second.setImageResource(R.drawable.ic_email);
        } else if (result_type.equalsIgnoreCase("Geo")) {

            txt_first.setText("Show on map");
            img_first.setImageResource(R.drawable.ic_geo);
            txt_second.setText("Navigation");
            img_second.setImageResource(R.drawable.ic_navigation);
            txt_third.setText("Share");
            cv_third.setVisibility(View.VISIBLE);
            img_third.setVisibility(View.VISIBLE);
            img_third.setImageResource(R.drawable.ic_share);

            longitude = getIntent().getStringExtra("longitude");

            if (!longitude.trim().equalsIgnoreCase("")) {
                txt_result_sec.setVisibility(View.VISIBLE);
                txt_result_sec.setText(longitude.trim());
                txt_result_sec_heading.setVisibility(View.VISIBLE);
                txt_result_sec_heading.setText("Longitude :");
            }
        } else if (result_type.equalsIgnoreCase("Book")) {

            txt_first.setText("Google Search");
            img_first.setImageResource(R.drawable.ic_search);
            txt_second.setText("Book Search");
            img_second.setImageResource(R.drawable.ic_book);
            txt_third.setVisibility(View.VISIBLE);
            txt_third.setText("Share");
            cv_third.setVisibility(View.VISIBLE);
            img_third.setVisibility(View.VISIBLE);
            img_third.setImageResource(R.drawable.ic_share);
            txt_forth.setVisibility(View.VISIBLE);
            txt_forth.setText("Product Search");
            cv_forth.setVisibility(View.VISIBLE);
            img_forth.setVisibility(View.VISIBLE);
            img_forth.setImageResource(R.drawable.ic_product);

        } else if (result_type.equalsIgnoreCase("URL") || result_type.equalsIgnoreCase("Facebook") || result_type.equalsIgnoreCase("Youtube") || result_type.equalsIgnoreCase("Instagram") || result_type.equalsIgnoreCase("Whatsapp") || result_type.equalsIgnoreCase("Viber") || result_type.equalsIgnoreCase("Paypal") || result_type.equalsIgnoreCase("Twitter")) {
            txt_first.setText("Open browser");
            img_first.setImageResource(R.drawable.ic_url);
            txt_second.setText("Share");
            img_second.setImageResource(R.drawable.ic_share);

        } else if (result_type.equalsIgnoreCase("Product")) {
            txt_first.setText("Search");
            img_first.setImageResource(R.drawable.ic_search);
            txt_second.setText("Share");
            img_second.setImageResource(R.drawable.ic_share);

        } else if (result_type.equalsIgnoreCase("Contact")) {

            txt_first.setText("Add to Contacts");
            img_first.setImageResource(R.drawable.ic_contact);

            contact = getIntent().getStringExtra("contact");
            email = getIntent().getStringExtra("email");
            org = getIntent().getStringExtra("org");
            add = getIntent().getStringExtra("add");
            note = getIntent().getStringExtra("note");
            birth = getIntent().getStringExtra("birth");
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");
            if (contact.trim().equalsIgnoreCase("") && result.trim().equalsIgnoreCase("") && email.trim().equalsIgnoreCase("")) {
                txt_first.setVisibility(View.GONE);
                img_first.setVisibility(View.GONE);
                cv_first.setVisibility(View.GONE);
            }

            if (!contact.trim().equalsIgnoreCase("")) {
                txt_result_sec_heading.setVisibility(View.VISIBLE);
                txt_result_sec_heading.setText("Telephone :");
                txt_result_sec.setVisibility(View.VISIBLE);
                txt_result_sec.setText(contact.trim());
                img_second.setImageResource(R.drawable.ic_call);
                txt_second.setText("Call");
            } else {
                img_second.setVisibility(View.GONE);
                txt_second.setVisibility(View.GONE);
                cv_second.setVisibility(View.GONE);
            }

            if (!email.trim().equalsIgnoreCase("")) {
                txt_result_third_heading.setVisibility(View.VISIBLE);
                txt_result_third_heading.setText("E-mail :");
                txt_result_third.setVisibility(View.VISIBLE);
                txt_result_third.setText(email.trim());
                cv_third.setVisibility(View.VISIBLE);
                img_third.setVisibility(View.VISIBLE);
                txt_third.setVisibility(View.VISIBLE);
            }
            if (!add.trim().equalsIgnoreCase("")) {
                txt_result_forth_heading.setVisibility(View.VISIBLE);
                txt_result_forth_heading.setText("Address :");
                txt_result_forth.setVisibility(View.VISIBLE);
                txt_result_forth.setText(add.trim());
                cv_forth.setVisibility(View.VISIBLE);
                img_forth.setVisibility(View.VISIBLE);
                txt_forth.setVisibility(View.VISIBLE);
            }
            if (!org.trim().equalsIgnoreCase("")) {
                txt_result_fifth_heading.setVisibility(View.VISIBLE);
                txt_result_fifth_heading.setText("Organization :");
                txt_result_fifth.setVisibility(View.VISIBLE);
                txt_result_fifth.setText(org.trim());
            }
            if (!note.trim().equalsIgnoreCase("")) {
                txt_result_sixth_heading.setVisibility(View.VISIBLE);
                txt_result_sixth_heading.setText("Note :");
                txt_result_sixth.setVisibility(View.VISIBLE);
                txt_result_sixth.setText(note.trim());
            }
            if (!birth.trim().equalsIgnoreCase("")) {
                txt_result_seven_heading.setVisibility(View.VISIBLE);
                txt_result_seven_heading.setText("Birthdate :");
                txt_result_seven.setVisibility(View.VISIBLE);
                txt_result_seven.setText(birth.trim());
            }

            if (!url.trim().equalsIgnoreCase("")) {
                txt_result_eight_heading.setVisibility(View.VISIBLE);
                txt_result_eight_heading.setText("URL :");
                txt_result_eight.setVisibility(View.VISIBLE);
                txt_result_eight.setText(url.trim());
            }
            if (!title.trim().equalsIgnoreCase("")) {
                txt_result_nine_heading.setVisibility(View.VISIBLE);
                txt_result_nine_heading.setText("Title :");
                txt_result_nine.setVisibility(View.VISIBLE);
                txt_result_nine.setText(title.trim());
            }
        } else if (result_type.equalsIgnoreCase("Telephone")) {
            txt_first.setText("Add to Contacts");
            img_first.setImageResource(R.drawable.ic_contact);
            txt_second.setText("Call");
            img_second.setImageResource(R.drawable.ic_call);

        } else if (result_type.equalsIgnoreCase("Wi-Fi")) {
            txt_first.setText("Connect to Network");
            img_first.setImageResource(R.drawable.ic_wifi);
            txt_second.setText("Copy Password");
            img_second.setImageResource(R.drawable.ic_clipboard);
            cv_third.setVisibility(View.VISIBLE);
            img_third.setVisibility(View.VISIBLE);
            txt_third.setVisibility(View.VISIBLE);
            txt_third.setText("Share");
            img_third.setImageResource(R.drawable.ic_share);

            password = getIntent().getStringExtra("password");
            security_type = getIntent().getStringExtra("type");
            txt_result_sec_heading.setVisibility(View.VISIBLE);
            txt_result_sec_heading.setText("Security type :");
            txt_result_sec.setVisibility(View.VISIBLE);
            txt_result_sec.setText(security_type);

            if (!password.equalsIgnoreCase("")) {
                txt_result_third_heading.setVisibility(View.VISIBLE);
                txt_result_third_heading.setText("Password :");
                txt_result_third.setVisibility(View.VISIBLE);
                txt_result_third.setText(password);
            }

        } else if (result_type.equalsIgnoreCase("Email")) {
            txt_first.setText("Send Email");
            img_first.setImageResource(R.drawable.ic_email);
            txt_second.setText("Add to Contacts");
            img_second.setImageResource(R.drawable.ic_contact);
            cv_third.setVisibility(View.VISIBLE);
            txt_third.setVisibility(View.VISIBLE);
            img_third.setVisibility(View.VISIBLE);
            txt_third.setText("Share");
            img_third.setImageResource(R.drawable.ic_share);

            sub = getIntent().getStringExtra("sub");
            body = getIntent().getStringExtra("body");
            if (!sub.equalsIgnoreCase("")) {
                txt_result_sec.setVisibility(View.VISIBLE);
                txt_result_sec.setText(sub);
                txt_result_sec_heading.setVisibility(View.VISIBLE);
                txt_result_sec_heading.setText("Sub :");
            }
            if (!body.equalsIgnoreCase("")) {
                txt_result_third.setVisibility(View.VISIBLE);
                txt_result_third.setText(body);
                txt_result_third_heading.setVisibility(View.VISIBLE);
                txt_result_third_heading.setText("Body :");
            }

        } else if (result_type.equalsIgnoreCase("SMS")) {

            phone = getIntent().getStringExtra("phone");
            if (!phone.trim().equalsIgnoreCase("")) {
                txt_result_sec_heading.setVisibility(View.VISIBLE);
                txt_result_sec_heading.setText("Telephone :");
                txt_result_sec.setVisibility(View.VISIBLE);
                txt_result_sec.setText(phone);
            }
            txt_first.setText("Send SMS");
            img_first.setImageResource(R.drawable.ic_sms);
            txt_second.setVisibility(View.GONE);
            img_second.setVisibility(View.GONE);
            cv_second.setVisibility(View.GONE);

        } else if (result_type.equalsIgnoreCase("Calendar")) {

            txt_first.setText("Add to events");
            img_first.setImageResource(R.drawable.ic_calendar);
            txt_second.setText("Send Email");
            img_second.setImageResource(R.drawable.ic_email);


            loc = getIntent().getStringExtra("loc");
            disc = getIntent().getStringExtra("disc");
            s_time_hour = Integer.parseInt(getIntent().getStringExtra("sTimeHour"));
            s_time_min = Integer.parseInt(getIntent().getStringExtra("sTimeMin"));
            s_date_day = Integer.parseInt(getIntent().getStringExtra("sDateDay"));
            s_date_month = Integer.parseInt(getIntent().getStringExtra("sDateMonth"));
            s_date_year = Integer.parseInt(getIntent().getStringExtra("sDateYear"));
            e_time_hour = Integer.parseInt(getIntent().getStringExtra("eTimeHour"));
            e_time_min = Integer.parseInt(getIntent().getStringExtra("eTimeMin"));
            e_date_day = Integer.parseInt(getIntent().getStringExtra("eDateDay"));
            e_date_month = Integer.parseInt(getIntent().getStringExtra("eDateMonth"));
            e_date_year = Integer.parseInt(getIntent().getStringExtra("eDateYear"));


            if (!loc.trim().equalsIgnoreCase("")) {
                txt_result_sec.setVisibility(View.VISIBLE);
                txt_result_sec.setText(loc);
                txt_result_sec_heading.setVisibility(View.VISIBLE);
                txt_result_sec_heading.setText("Location :");
            }
            if (!disc.trim().equalsIgnoreCase("")) {
                txt_result_third.setVisibility(View.VISIBLE);
                txt_result_third.setText(disc);
                txt_result_third_heading.setVisibility(View.VISIBLE);
                txt_result_third_heading.setText("Discription :");
            }
            txt_result_forth.setVisibility(View.VISIBLE);
            txt_result_fifth.setVisibility(View.VISIBLE);
            txt_result_forth_heading.setVisibility(View.VISIBLE);
            txt_result_forth_heading.setText("StartTime :");
            txt_result_fifth_heading.setVisibility(View.VISIBLE);
            txt_result_fifth_heading.setText("EndTime :");
            if (s_time_min == -1) {
                txt_result_forth.setText(s_date_day + "-" + s_date_month + "-" + s_date_year);
                txt_result_fifth.setText(e_date_day + "-" + e_date_month + "-" + e_date_year);

            } else {
                txt_result_forth.setText(s_date_day + "-" + s_date_month + "-" + s_date_year + "  " + s_time_hour + ":" + s_time_min);
                txt_result_fifth.setText(e_date_day + "-" + e_date_month + "-" + e_date_year + "  " + e_time_hour + ":" + e_time_min);
            }
        } else if (result_type.equalsIgnoreCase("Spotify")) {

            song = getIntent().getStringExtra("song");
            rawvalue = getIntent().getStringExtra("rawvalue");
            if (!song.trim().equalsIgnoreCase("")) {
                txt_result_sec.setVisibility(View.VISIBLE);
                txt_result_sec.setText(song);
                txt_result_sec_heading.setVisibility(View.VISIBLE);
                txt_result_sec_heading.setText("Song :");
            }
            txt_first.setText("Open browser");
            img_first.setImageResource(R.drawable.ic_url);
            txt_second.setText("Share");
            img_second.setImageResource(R.drawable.ic_share);
        } else if (result_type.equalsIgnoreCase("Driver License")) {

            txt_second.setText("Share");
            img_second.setImageResource(R.drawable.ic_share);

            street = getIntent().getStringExtra("street");
            city = getIntent().getStringExtra("city");
            state = getIntent().getStringExtra("state");
            zip = getIntent().getStringExtra("zip");
            birthdate = getIntent().getStringExtra("birthdate");
            doc_type = getIntent().getStringExtra("docType");
            issue_date = getIntent().getStringExtra("issueDate");
            expiry_date = getIntent().getStringExtra("expiryDate");
            gender = getIntent().getStringExtra("gender");
            issuing_country = getIntent().getStringExtra("issuingCountry");
            license_number = getIntent().getStringExtra("licenseNumber");

            if (city.trim().equalsIgnoreCase("") && state.trim().equalsIgnoreCase("") && street.trim().equalsIgnoreCase("") && zip.trim().equalsIgnoreCase("")) {
                txt_first.setVisibility(View.GONE);
                img_first.setVisibility(View.GONE);
                cv_first.setVisibility(View.GONE);
            }

            if (!street.trim().equalsIgnoreCase("")) {
                txt_result_third.setVisibility(View.VISIBLE);
                txt_result_third.setText(street.trim());
                txt_result_third_heading.setVisibility(View.VISIBLE);
                txt_result_third_heading.setText("Street :");
            }
            if (!city.trim().equalsIgnoreCase("")) {
                txt_result_forth.setVisibility(View.VISIBLE);
                txt_result_forth.setText(city.trim());
                txt_result_forth_heading.setVisibility(View.VISIBLE);
                txt_result_forth_heading.setText("City :");
            }
            if (!state.trim().equalsIgnoreCase("")) {
                txt_result_fifth.setVisibility(View.VISIBLE);
                txt_result_fifth.setText(state.trim());
                txt_result_fifth_heading.setVisibility(View.VISIBLE);
                txt_result_fifth_heading.setText("State :");
            }
            if (!zip.trim().equalsIgnoreCase("")) {
                txt_result_sixth.setVisibility(View.VISIBLE);
                txt_result_sixth.setText(zip.trim());
                txt_result_sixth_heading.setVisibility(View.VISIBLE);
                txt_result_sixth_heading.setText("Zip :");
            }
            if (!birthdate.trim().equalsIgnoreCase("")) {
                txt_result_seven.setVisibility(View.VISIBLE);
                txt_result_seven.setText(birthdate.trim());
                txt_result_seven_heading.setVisibility(View.VISIBLE);
                txt_result_seven_heading.setText("Birthdate :");
            }
            if (!doc_type.trim().equalsIgnoreCase("")) {
                txt_result_eight.setVisibility(View.VISIBLE);
                txt_result_eight.setText(doc_type.trim());
                txt_result_eight_heading.setVisibility(View.VISIBLE);
                txt_result_eight_heading.setText("Doc Type :");
            }
            if (!issue_date.trim().equalsIgnoreCase("")) {
                txt_result_nine.setVisibility(View.VISIBLE);
                txt_result_nine.setText(issue_date.trim());
                txt_result_nine_heading.setVisibility(View.VISIBLE);
                txt_result_nine_heading.setText("IssueDate :");
            }
            if (!expiry_date.trim().equalsIgnoreCase("")) {
                txt_result_seven.setVisibility(View.VISIBLE);
                txt_result_seven.setText(expiry_date.trim());
                txt_result_seven_heading.setVisibility(View.VISIBLE);
                txt_result_seven_heading.setText("ExpiryDate :");
            }
            if (!gender.trim().equalsIgnoreCase("")) {
                txt_result_eight.setVisibility(View.VISIBLE);
                txt_result_eight.setText(gender.trim());
                txt_result_eight_heading.setVisibility(View.VISIBLE);
                txt_result_eight_heading.setText("Gender :");
            }
            if (!issuing_country.trim().equalsIgnoreCase("")) {
                txt_result_nine.setVisibility(View.VISIBLE);
                txt_result_nine.setText(issuing_country.trim());
                txt_result_nine_heading.setVisibility(View.VISIBLE);
                txt_result_nine_heading.setText("Issue Country :");
            }
            if (!license_number.trim().equalsIgnoreCase("")) {
                txt_result_sec.setVisibility(View.VISIBLE);
                txt_result_sec.setText(license_number.trim());
                txt_result_sec_heading.setVisibility(View.VISIBLE);
                txt_result_sec_heading.setText("License Number :");
            }

        }

    }

    private void bind() {
        txt_result = findViewById(R.id.txt_result);
        txt_data_type = findViewById(R.id.txt_data_type);
        cv_copy = findViewById(R.id.cv_copy);
        cv_first = findViewById(R.id.cv_first);
        cv_second = findViewById(R.id.cv_second);
        cv_third = findViewById(R.id.cv_third);
        cv_forth = findViewById(R.id.cv_forth);
        img_first = findViewById(R.id.img_first);
        txt_first = findViewById(R.id.txt_first);
        img_second = findViewById(R.id.img_second);
        txt_second = findViewById(R.id.txt_second);
        img_third = findViewById(R.id.img_third);
        txt_third = findViewById(R.id.txt_third);
        img_forth = findViewById(R.id.img_forth);
        txt_forth = findViewById(R.id.txt_forth);
        txt_result_sec = findViewById(R.id.txt_result_sec);
        txt_result_third = findViewById(R.id.txt_result_third);
        txt_result_forth = findViewById(R.id.txt_result_forth);
        txt_result_fifth = findViewById(R.id.txt_result_fifth);
        txt_result_sixth = findViewById(R.id.txt_result_sixth);
        txt_result_seven = findViewById(R.id.txt_result_seven);
        txt_result_eight = findViewById(R.id.txt_result_eight);
        txt_result_nine = findViewById(R.id.txt_result_nine);

        txt_result_heading = findViewById(R.id.txt_result_heading);
        txt_result_sec_heading = findViewById(R.id.txt_result_sec_heading);
        txt_result_third_heading = findViewById(R.id.txt_result_third_heading);
        txt_result_forth_heading = findViewById(R.id.txt_result_forth_heading);
        txt_result_fifth_heading = findViewById(R.id.txt_result_fifth_heading);
        txt_result_sixth_heading = findViewById(R.id.txt_result_sixth_heading);
        txt_result_seven_heading = findViewById(R.id.txt_result_seven_heading);
        txt_result_eight_heading = findViewById(R.id.txt_result_eight_heading);
        txt_result_nine_heading = findViewById(R.id.txt_result_nine_heading);
        img_up = findViewById(R.id.img_up);
        img_down = findViewById(R.id.img_down);
        img_back = findViewById(R.id.img_back);

        cv_copy.setOnClickListener(this);
        txt_result.setOnClickListener(this);

        cv_first.setOnClickListener(this);
        cv_second.setOnClickListener(this);
        cv_third.setOnClickListener(this);
        cv_forth.setOnClickListener(this);

        img_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.txt_result:

                if (result_type.equalsIgnoreCase("URL")) {
                    if (!result.startsWith("http://") && !result.startsWith("https://")) {
                        result = "http://" + result;
                    }
                    Intent browser_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                    startActivity(browser_intent);

                } else if (result_type.equalsIgnoreCase("Facebook")) {

                    if (result.contains("fb://profile/") || result.contains("fb://page/")) {

                    } else if (result.contains("http://www.facebook.com/")) {
                        result = result.replace("http://www.facebook.com/", "");
                        result = "fb://profile/" + result;
                    }
                    SharingToSocialMedia("com.facebook.katana");

                } else if (result_type.equalsIgnoreCase("Youtube")) {
                    SharingToSocialMedia("com.google.android.youtube");

                } else if (result_type.equalsIgnoreCase("Instagram")) {

                    if (result.contains("http://www.instagram.com/")) {
                        result = result.replace("http://www.instagram.com/", "");
                        result = "instagram://user?username=" + result;
                    }
                    SharingToSocialMedia("com.instagram.android");

                } else if (result_type.equalsIgnoreCase("Whatsapp")) {

                    if (result.contains("https://api.whatsapp.com/send?phone=")) {
                        result = result.replace("https://api.whatsapp.com/send?phone=", "");
                        result = "whatsapp://send?phone=" + result;
                    }
                    SharingToSocialMedia("com.whatsapp");

                } else if (result_type.equalsIgnoreCase("Viber")) {

                    SharingToSocialMedia("com.viber.voip");

                } else if (result_type.equalsIgnoreCase("Paypal")) {
                    if (!result.startsWith("http://") && !result.startsWith("https://")) {
                        result = "http://" + result;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                    startActivity(browserIntent);

                } else if (result_type.equalsIgnoreCase("Twitter")) {

                    if (result.contains("https://twitter.com/")) {
                        result = result.replace("https://twitter.com/", "");
                        result = "twitter://user?screen_name=" + result;
                    }
                    SharingToSocialMedia("com.twitter.android");
                }
                break;
            case R.id.cv_first:
                if (result_type.equalsIgnoreCase("Text")) {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("sms_body", result);
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    if (sendIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(sendIntent);
                    }
                } else if (result_type.equalsIgnoreCase("SMS")) {

                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("sms_body", result);
                    sendIntent.putExtra("address", phone);
                    sendIntent.setType("vnd.android-dir/mms-sms");
                    if (sendIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(sendIntent);
                    }
                } else if (result_type.equalsIgnoreCase("URL") || result_type.equalsIgnoreCase("Facebook") || result_type.equalsIgnoreCase("Youtube") || result_type.equalsIgnoreCase("Instagram") || result_type.equalsIgnoreCase("Whatsapp") || result_type.equalsIgnoreCase("Paypal") || result_type.equalsIgnoreCase("Twitter")) {
                    if (result.contains("fb://page/")) {
                        result = result.replace("fb://page/", "");
                        result = "http://www.facebook.com/" + result;
                    } else if (result.contains("fb://profile/")) {
                        result = result.replace("fb://profile/", "");
                        result = "http://www.facebook.com/" + result;
                    } else if (result.contains("instagram://user?username=")) {
                        result = result.replace("instagram://user?username=", "");
                        result = "http://www.instagram.com/" + result;
                    } else if (result.contains("whatsapp://send?phone=")) {
                        result = result.replace("whatsapp://send?phone=", "");
                        result = "https://api.whatsapp.com/send?phone=" + result;
                    } else if (result.contains("twitter://user?screen_name=")) {
                        result = result.replace("twitter://user?screen_name=", "");
                        result = "https://twitter.com/" + result;
                    }
                    if (!result.startsWith("http://") && !result.startsWith("https://")) {
                        result = "http://" + result;
                    }
                    Intent browser_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                    startActivity(browser_intent);

                } else if (result_type.equalsIgnoreCase("Spotify")) {
                    Intent launcher = new Intent(Intent.ACTION_VIEW, Uri.parse(rawvalue));
                    launcher.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + getPackageName()));
                    if (launcher.resolveActivity(getPackageManager()) != null) {
                        startActivity(launcher);
                    } else {
                        if (rawvalue.contains("spotify:search:")) {
                            rawvalue = rawvalue.replace("spotify:search:", "");
                        }
                        result = "https://open.spotify.com/search/" + rawvalue;
                        Intent browser_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                        startActivity(browser_intent);
                    }
                } else if (result_type.equalsIgnoreCase("Viber")) {

                    if (appInstalledOrNot("com.viber.voip")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                        startActivity(intent);
                    } else {
                        if (result.contains("viber://add?number=")) {
                            result = "https://www.viber.com/en/";
                        }
                        Intent browser_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                        startActivity(browser_intent);
                    }

                } else if (result_type.equalsIgnoreCase("Contact")) {

                    Intent i = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                    i.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                    i.putExtra(ContactsContract.Intents.Insert.NAME, result);
                    i.putExtra(ContactsContract.Intents.Insert.PHONE, contact);
                    i.putExtra(ContactsContract.Intents.Insert.EMAIL, email);

                    if (Integer.valueOf(Build.VERSION.SDK) > 14)
                        i.putExtra("finishActivityOnSaveCompleted", true);
                    startActivityForResult(i, contact_const);

                } else if (result_type.equalsIgnoreCase("Telephone")) {

                    Intent i = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                    i.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                    i.putExtra(ContactsContract.Intents.Insert.PHONE, result);

                    if (Integer.valueOf(Build.VERSION.SDK) > 14)
                        i.putExtra("finishActivityOnSaveCompleted", true);
                    startActivityForResult(i, contact_const);

                } else if (result_type.equalsIgnoreCase("Email")) {

                    Intent email_intent = new Intent(Intent.ACTION_SENDTO);
                    email_intent.setData(Uri.parse("mailto:" + result));
                    email_intent.putExtra(android.content.Intent.EXTRA_SUBJECT, sub);
                    email_intent.putExtra(Intent.EXTRA_TEXT, body);
                    startActivity(email_intent);

                } else if (result_type.equalsIgnoreCase("Geo")) {
                    String uri = "http://maps.google.com/maps?q=loc:" + result + "," + longitude;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);

                } else if (result_type.equalsIgnoreCase("Calendar")) {
                    Calendar scal_time = Calendar.getInstance();
                    scal_time.set(s_date_year, s_date_month - 1, s_date_day + 1, s_time_hour, s_time_min);
                    long start_time = scal_time.getTimeInMillis();

                    Calendar ecal_time = Calendar.getInstance();
                    ecal_time.set(e_date_year, e_date_month - 1, e_date_day + 1, e_time_hour, e_time_min);
                    long end_time = ecal_time.getTimeInMillis();

                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setType("vnd.android.cursor.item/event");

                    if (s_time_hour == -1) {
                        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                    } else {
                        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
                    }
                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start_time);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end_time);

                    intent.putExtra(CalendarContract.Events.TITLE, result);
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, disc);
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, loc);

                    startActivity(intent);

                } else if (result_type.equalsIgnoreCase("Wi-Fi")) {

                    String networkSSID = result;
                    String networkPass = password;
                    connectWiFi(networkSSID, networkPass);


                } else if (result_type.equalsIgnoreCase("Driver License")) {

                    if (isGoogleMapsInstalled()) {
                        if (!zip.trim().equalsIgnoreCase("")) {
                            showMap(zip);

                        } else if (!city.trim().equalsIgnoreCase("")) {
                            showMap(city);

                        } else if (!street.trim().equalsIgnoreCase("")) {
                            showMap(street);

                        } else if (!state.trim().equalsIgnoreCase("")) {
                            showMap(state);
                        }
                    } else {
                        Toast.makeText(this, "Application not installed", Toast.LENGTH_SHORT).show();
                    }

                } else if (result_type.equalsIgnoreCase("Product") || result_type.equalsIgnoreCase("Book")) {
                    String escaped_query = null;
                    try {
                        escaped_query = URLEncoder.encode(result, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Uri uri = Uri.parse("http://www.google.com/search?q=" + escaped_query);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    String escaped_query = null;
                    try {
                        escaped_query = URLEncoder.encode(result, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Uri uri = Uri.parse("http://www.google.com/search?q=" + escaped_query);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;
            case R.id.cv_copy:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String copy = "";
                if (result_type.equalsIgnoreCase("Contact")) {
                    copy = "Name : " + result + "\n" + "Telephone : " + contact + "\n" + "E-mail : " + email + "\n" + "Organization : " + org + "\n" + "Address : "
                            + add + "\n" + "Note : " + note + "\n" + "Birthdate : " + birth + "\n" + "URL : " + url + "\n" + "Title : " + title;
                } else if (result_type.equalsIgnoreCase("Email")) {
                    copy = "To : " + result + "\n" + "Sub : " + sub + "\n" + "Body : " + body;
                } else if (result_type.equalsIgnoreCase("SMS")) {
                    copy = "Content : " + result + "\n " + "Telephone : " + phone;
                } else if (result_type.equalsIgnoreCase("Spotify")) {
                    copy = "Artist : " + result + "\n" + "Song : " + song;
                } else if (result_type.equalsIgnoreCase("Calendar")) {
                    copy = "Title : " + result + "\n" + "Location : " + loc + "\n" + "Discription : " + disc + "\n" + "StartTime : "
                            + txt_result_forth.getText().toString() + "\n" + "EndTime : " + txt_result_fifth.getText().toString();
                } else if (result_type.equalsIgnoreCase("Geo")) {
                    copy = "Latitude : " + result + "\n" + "Longitude : " + longitude;
                } else if (result_type.equalsIgnoreCase("Driver License")) {
                    copy = "Driver Name : " + result + "\n" + "Street : " + street + "\n" + "City : " + city + "\n" + "State : " + state +
                            "\n" + "Zip : " + zip + "\n" + "Birthdate : " + birthdate +
                            "\n" + "Doctype : " + doc_type + "\n" + "IssueDate : " + issue_date + "\n" +
                            "ExpiryDate : " + expiry_date + "\n" + "Gender : " + gender + "\n" + "IssuingCountry : " + issuing_country
                            + "\n" + "LicenseNumber : " + license_number;
                } else if (result_type.equalsIgnoreCase("Telephone")) {
                    copy = "Telephone : " + result;
                } else if (result_type.equalsIgnoreCase("Wi-Fi")) {
                    copy = "Network name : " + result + "\n" + "SecurityType : " + security_type + "\n" + "Password : " + password;
                } else {
                    copy = result;
                }

                copy = copy.replaceAll("(?m)^[ \t]*\r?\n", "");
                ClipData clip = ClipData.newPlainText("label", copy);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Text copied...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cv_second:
                if (result_type.equalsIgnoreCase("Text")) {

                    Intent email_intent = new Intent(Intent.ACTION_SENDTO);
                    email_intent.setData(Uri.parse("mailto:"));
                    email_intent.putExtra(Intent.EXTRA_TEXT, result);
                    startActivity(email_intent);
                } else if (result_type.equalsIgnoreCase("URL") || result_type.equalsIgnoreCase("Product") || result_type.equalsIgnoreCase("Facebook") || result_type.equalsIgnoreCase("Paypal") || result_type.equalsIgnoreCase("Youtube") || result_type.equalsIgnoreCase("Instagram") || result_type.equalsIgnoreCase("Whatsapp") || result_type.equalsIgnoreCase("Viber") || result_type.equalsIgnoreCase("Twitter") || result_type.equalsIgnoreCase("Spotify")) {
                    String share = "";
                    if (result_type.equalsIgnoreCase("Spotify")) {
                        share = "Artist : " + result + "\n" + "Song : " + song;
                    } else {
                        share = result;
                    }
                    try {
                        Intent sharing_intent = new Intent(Intent.ACTION_SEND);
                        sharing_intent.setType("text/plain");
                        sharing_intent.putExtra(Intent.EXTRA_TEXT, share);
                        startActivity(Intent.createChooser(sharing_intent, "Share with"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result_type.equalsIgnoreCase("Geo")) {
                    String s = "http://maps.google.com/maps?daddr=" + result + "," + longitude;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(s));
                    startActivity(intent);

                } else if (result_type.equalsIgnoreCase("Book")) {
                    String url = "https://books.google.com/books?vid=isbn" + result + "&redir_esc=y";
                    Intent browser_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browser_intent);

                } else if (result_type.equalsIgnoreCase("Contact")) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + contact));
                    startActivity(intent);

                } else if (result_type.equalsIgnoreCase("Telephone")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + result));
                    startActivity(intent);
                } else if (result_type.equalsIgnoreCase("Wi-Fi")) {
                    ClipboardManager cbpwd = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip_data = ClipData.newPlainText("label", password);
                    cbpwd.setPrimaryClip(clip_data);
                } else if (result_type.equalsIgnoreCase("Email")) {
                    Intent i = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                    i.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                    i.putExtra(ContactsContract.Intents.Insert.EMAIL, result);

                    if (Integer.valueOf(Build.VERSION.SDK) > 14)
                        i.putExtra("finishActivityOnSaveCompleted", true);
                    startActivityForResult(i, contact_const);
                } else if (result_type.equalsIgnoreCase("Calendar")) {
                    String share = "";
                    Intent send_intent = new Intent();
                    send_intent.setAction(Intent.ACTION_SENDTO);
                    send_intent.setData(Uri.parse("mailto:"));
                    send_intent.putExtra(android.content.Intent.EXTRA_SUBJECT, result);
                    share = "Title : " + result + "\n" + "Location : " + loc + "\n" + "Discription : " + disc + "\n" + "StartTime : " +
                            txt_result_forth.getText().toString() + "\n" + "EndTime : " + txt_result_fifth.getText().toString();
                    send_intent.putExtra(Intent.EXTRA_TEXT, share);
                    startActivity(send_intent);
                } else if (result_type.equalsIgnoreCase("Driver License")) {
                    String share = "Driver Name : " + result + "\n" + "Street : " + street + "\n" + "City : " + city +
                            "\n" + "State : " + state + "\n" + "Zip : " + zip + "\n" + "Birthdate : " + birthdate +
                            "\n" + "Doctype : " + doc_type + "\n" + "IssueDate : " + issue_date + "\n" + "ExpiryDate : "
                            + expiry_date + "\n" + "Gender : " + gender + "\n" + "IssuingCountry : " + issuing_country + "\n" +
                            "LicenseNumber : " + license_number;
                    try {
                        Intent sharing_intent = new Intent(Intent.ACTION_SEND);
                        sharing_intent.setType("text/plain");
                        sharing_intent.putExtra(Intent.EXTRA_TEXT, share);
                        startActivity(Intent.createChooser(sharing_intent, "Share with"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
                break;
            case R.id.cv_third:
                if (result_type.equalsIgnoreCase("Contact")) {
                    Intent email_intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                    if (email_intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(email_intent);
                    }
                } else if (result_type.equalsIgnoreCase("Wi-Fi") || result_type.equalsIgnoreCase("Book") || result_type.equalsIgnoreCase("Email") || result_type.equalsIgnoreCase("Geo")) {

                    String share = "";
                    if (result_type.equalsIgnoreCase("Email")) {
                        share = "To : " + result + "\n" + "Sub : " + sub + "\n" + "Body : " + body;
                    } else if (result_type.equalsIgnoreCase("Wi-Fi")) {
                        share = "Network name : " + result + "\n" + "SecurityType : " + security_type + "\n" + "Password : " + password;
                    } else if (result_type.equalsIgnoreCase("Geo")) {
                        share = "Latitude : " + result + "\n" + "Longitude : " + longitude;
                    } else {
                        share = "ISBN : " + result;
                    }
                    try {
                        Intent sharing_intent = new Intent(Intent.ACTION_SEND);
                        sharing_intent.setType("text/plain");
                        sharing_intent.putExtra(Intent.EXTRA_TEXT, share);
                        startActivity(Intent.createChooser(sharing_intent, "Share with"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
                break;
            case R.id.cv_forth:
                if (result_type.equalsIgnoreCase("Contact")) {
                    if (isGoogleMapsInstalled()) {
                        showMap(add);
                    } else {
                        Toast.makeText(this, "Application not installed", Toast.LENGTH_SHORT).show();
                    }

                } else if (result_type.equalsIgnoreCase("Book")) {
                    String s = "https://www.google.com/m/search?tbm=shop&q=" + result;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
                    startActivity(browserIntent);

                } else {
                }
                break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == contact_const) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Added Contact", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled Added Contact", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void showMap(String geoLocation) {

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.co.in/maps?q=" + geoLocation));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void SharingToSocialMedia(String application) {
        if (appInstalledOrNot(application)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
            startActivity(intent);
        } else {
            Toast.makeText(ShareScannerActivity.this,
                    "Application is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean appInstalledOrNot(String packagename) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

    public void connectWiFi(String result, String pass) {
        try {


            String network_ssid = result;
            String network_pass = pass;

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + network_ssid + "\"";
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.priority = 40;


            if (security_type.equals("WEP")) {
                Log.v("rht", "Configuring WEP");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

                if (network_pass.matches("^[0-9a-fA-F]+$")) {
                    conf.wepKeys[0] = network_pass;
                } else {
                    conf.wepKeys[0] = "\"".concat(network_pass).concat("\"");
                }

                conf.wepTxKeyIndex = 0;

            } else if (security_type.equals("WPA")) {
                Log.v("rht", "Configuring WPA");

                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                conf.preSharedKey = "\"" + network_pass + "\"";

            } else if (security_type.equals("OPEN")) {
                Log.v("rht", "Configuring OPEN network");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.clear();
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            } else {
                Toast.makeText(this, "Wi-Fi protocol error", Toast.LENGTH_SHORT).show();
            }
            WifiManager wifi_manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int network_id = wifi_manager.addNetwork(conf);

            Log.v("rht", "Add result " + network_id);
            if (network_id == -1) {
                wifi_manager.setWifiEnabled(true);
                wifi_manager.enableNetwork(network_id, true);
            }
            startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
        } catch (Exception e) {
            Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
        }


    }

    private AdView m_ad_view;
    ImageView img_banner;

    public void admobBanner() {

        m_ad_view = findViewById(R.id.ad_view);
        img_banner = findViewById(R.id.img_banner);
        AdRequest ad_request = new AdRequest.Builder().build();
        m_ad_view.loadAd(ad_request);
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