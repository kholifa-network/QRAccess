package com.kholifa.qraccess.activity.CreationCode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.card.MaterialCardView;
import com.kholifa.qraccess.BuildConfig;
import com.kholifa.qraccess.R;
import com.kholifa.qraccess.utillScan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.kholifa.qraccess.utillScan.project_folder_Path;


public class CreateSaveActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialCardView cv_share, cv_save;
    ImageView img_qr_code, img_sv_create_back;
    TextView txt_data_header, txt_data_type_header;
    Dialog rename_dialog;
    public static File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_save);
        admobBanner();
        makeFolder();
        bind();
        txt_data_header.setText(utillScan.string_data);
        txt_data_type_header.setText(utillScan.string_data_type);
        img_qr_code.setImageBitmap(utillScan.generate_bitmap);
    }

    private void bind() {
        img_qr_code = findViewById(R.id.img_qr_code);
        cv_save = findViewById(R.id.cv_save);
        txt_data_type_header = findViewById(R.id.txt_data_type_header);
        txt_data_header = findViewById(R.id.txt_data_header);
        cv_share = findViewById(R.id.cv_share);
        img_sv_create_back = findViewById(R.id.img_sv_create_back);
        cv_save.setOnClickListener(this);
        cv_share.setOnClickListener(this);
        img_sv_create_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_save:
                rename();
                break;
            case R.id.cv_share:
                File fileshare = new File(this.getExternalCacheDir(), "TempQrCode.jpeg");
                saveImage(utillScan.generate_bitmap, fileshare);

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", fileshare));
                startActivity(Intent.createChooser(sharingIntent, "Share Image using"));
                break;
            case R.id.img_sv_create_back:
                onBackPressed();
                break;
        }

    }

    File my_folder_path;

    public void makeFolder() {
        my_folder_path = new File(project_folder_Path);
        if (!my_folder_path.exists()) {
            my_folder_path.mkdirs();
        }

    }

    private void SaveImage() {

        fileSaveTask save_task = new fileSaveTask();
        save_task.execute();
    }

    public class fileSaveTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        protected Void doInBackground(Void... val1) {

            saveImage(utillScan.generate_bitmap, file);
            return null;
        }

        protected void onPostExecute(Void val3) {
        }
    }

    private ProgressDialog pd;

    private void ShowDialog() {
        pd = new ProgressDialog(CreateSaveActivity.this);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
    }

    private void CloseDialog() {
        pd.dismiss();
    }

    private void saveImage(Bitmap bitmap, File file) {
        OutputStream output;


        try {
            output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            output.flush();
            output.close();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void rename() {
        rename_dialog = new Dialog(CreateSaveActivity.this);
        rename_dialog.setCancelable(true);
        rename_dialog.setContentView(R.layout.dialog_rename_code);
        LinearLayout ly_cancel = rename_dialog.findViewById(R.id.ly_cancel);
        CardView cv_ok = rename_dialog.findViewById(R.id.cv_ok);
        ImageView img_clear = rename_dialog.findViewById(R.id.img_clear);
        EditText edt_file_name = rename_dialog.findViewById(R.id.edt_file_name);
        TextView txt_warning = rename_dialog.findViewById(R.id.txt_warning);

        String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String file_name = utillScan.string_data_type + ts;
        edt_file_name.setText(file_name);
        edt_file_name.setCursorVisible(false);

        file = new File(my_folder_path + "/" + edt_file_name.getText().toString().trim() + ".jpeg");

        edt_file_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String custom_file_name = edt_file_name.getText().toString().trim() + ".jpeg";
                file = new File(my_folder_path + "/" + custom_file_name);
                if (file.exists()) {
                    txt_warning.setVisibility(View.VISIBLE);
                } else {
                    txt_warning.setVisibility(View.GONE);
                }
            }
        });
        edt_file_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_file_name.setCursorVisible(true);
                return false;
            }
        });


        ly_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rename_dialog.dismiss();
            }
        });

        cv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_warning.getVisibility() == View.VISIBLE) {
                    Toast.makeText(CreateSaveActivity.this, "The name is already in use..", Toast.LENGTH_SHORT).show();
                } else if (edt_file_name.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(CreateSaveActivity.this, "please, Enter File Name", Toast.LENGTH_SHORT).show();
                } else {

                    String type = Objects.requireNonNull(file_name).endsWith(".mp4") ? "video/*" : "image/*";
                    MediaScannerConnection.scanFile(CreateSaveActivity.this, new String[]{file.getAbsolutePath()}, new String[]{type}, null);

                    SaveImage();
                    rename_dialog.dismiss();
                    Toast.makeText(CreateSaveActivity.this, "Saved successfully...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_file_name.getText().clear();
            }
        });

        Window window = rename_dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rename_dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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