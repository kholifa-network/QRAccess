package com.kholifa.qraccess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import androidx.room.TypeConverter;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;

public class utillScan {

    public static Bitmap generate_bitmap;
    public static String string_data, string_data_type;
    public static String edit_folder_name = "QR Code Scanner";
    public static String project_folder_Path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + edit_folder_name + File.separator;
    static final String[] empty_str_array = new String[0];


    @TypeConverter
    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        if (temp == null) {
            return null;
        } else
            return temp;
    }

    @TypeConverter
    public static Bitmap StringToBitMap(String encoded_string) {
        try {
            byte[] encode_byte = Base64.decode(encoded_string, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encode_byte, 0, encode_byte.length);
            if (bitmap == null) {
                return null;
            } else {
                return bitmap;
            }

        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static Bitmap TextToImageEncode(String value) throws WriterException {
        try {
            BitMatrix encode = new MultiFormatWriter().encode(value, BarcodeFormat.QR_CODE, 1080, 1080, null);
            int width = encode.getWidth();
            int height = encode.getHeight();
            int[] iArr = new int[(width * height)];
            for (int i = 0; i < height; i++) {
                int i2 = i * width;
                for (int i3 = 0; i3 < width; i3++) {
                    iArr[i2 + i3] = encode.get(i3, i) ? BLACK : -1;
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            createBitmap.setPixels(iArr, 0, 1080, 0, 0, width, height);
            return createBitmap;
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    public static String matchSinglePrefixedField(String prefix, String raw_text, char end_char, boolean trim) {
        String[] matches = matchPrefixedField(prefix, raw_text, end_char, trim);
        return matches == null ? null : matches[0];
    }

    public static String[] matchPrefixedField(String prefix, String raw_text, char end_char, boolean trim) {
        List<String> matches = null;
        int i = 0;
        int max = raw_text.length();
        while (i < max) {
            i = raw_text.indexOf(prefix, i);
            if (i < 0) {
                break;
            }
            i += prefix.length();
            int start = i;
            boolean more = true;
            while (more) {
                i = raw_text.indexOf(end_char, i);
                if (i < 0) {

                    i = raw_text.length();
                    more = false;
                } else if (countPrecedingBackslashes(raw_text, i) % 2 != 0) {

                    i++;
                } else {

                    if (matches == null) {
                        matches = new ArrayList<>(3);
                    }
                    String element = unescapeBackslash(raw_text.substring(start, i));
                    if (trim) {
                        element = element.trim();
                    }
                    if (!element.isEmpty()) {
                        matches.add(element);
                    }
                    i++;
                    more = false;
                }
            }
        }
        if (matches == null || matches.isEmpty()) {
            return null;
        }
        return matches.toArray(empty_str_array);
    }

    protected static String unescapeBackslash(String escaped) {
        int backslash = escaped.indexOf('\\');
        if (backslash < 0) {
            return escaped;
        }
        int max = escaped.length();
        StringBuilder unescaped = new StringBuilder(max - 1);
        unescaped.append(escaped.toCharArray(), 0, backslash);
        boolean nextIsEscaped = false;
        for (int i = backslash; i < max; i++) {
            char c = escaped.charAt(i);
            if (nextIsEscaped || c != '\\') {
                unescaped.append(c);
                nextIsEscaped = false;
            } else {
                nextIsEscaped = true;
            }
        }
        return unescaped.toString();
    }

    private static int countPrecedingBackslashes(CharSequence s, int pos) {
        int count = 0;
        for (int i = pos - 1; i >= 0; i--) {
            if (s.charAt(i) == '\\') {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}
