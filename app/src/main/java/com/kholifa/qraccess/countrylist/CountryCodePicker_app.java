package com.kholifa.qraccess.countrylist;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kholifa.qraccess.BuildConfig;
import com.kholifa.qraccess.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;


public class CountryCodePicker_app extends RelativeLayout {
    private static String tag = CountryCodePicker_app.class.getSimpleName();

    private final String default_country = Locale.getDefault().getCountry();
    private static final String default_iso_country = "ID";
    private static final int default_text_color = 0;
    private static final int default_background_color = Color.TRANSPARENT;

    private int m_background_color = default_background_color;

    private int m_default_country_code;
    private String m_default_country_name_code;

    private PhoneNumberUtil m_phone_util;
    private PhoneNumberWatcher m_phone_number_watcher;
    PhoneNumberInputValidityListener m_phone_number_input_validity_listener;

    private TextView m_tv_selected_country;
    private TextView m_registered_phone_number_textview;
    private RelativeLayout m_rly_holder;
    private ImageView m_imv_arrow;
    private ImageView m_imv_flag;
    private LinearLayout m_lly_flag_holder;
    private Country_app m_selected_country;
    private Country_app m_default_country;
    private RelativeLayout m_rly_click_consumer;
    private OnClickListener m_country_code_holder_clicklistener;

    private boolean m_hide_name_code = false;
    private boolean m_show_flag = true;
    private boolean m_show_full_name = false;
    private boolean m_use_full_name = false;
    private boolean m_selection_dialog_show_search = true;

    private List<Country_app> m_preferred_countries;

    private String m_country_preference;
    private List<Country_app> m_custom_master_countries_list;

    private String m_custom_master_countries;
    private boolean m_keyboard_auto_pop_on_search = true;
    private boolean m_is_clickable = true;


    private boolean m_hide_phone_code = false;

    private int m_text_color = default_text_color;

    private int m_dialog_text_color = default_text_color;


    private Typeface m_typeFace;

    private boolean m_is_hint_enabled = true;
    private boolean m_is_enable_phone_number_watcher = true;

    private boolean m_set_country_by_time_zone = true;

    private OnCountryChangeListener m_on_country_change_listener;


    public interface OnCountryChangeListener {
        void onCountrySelected(Country_app selectedCountry);
    }


    public interface PhoneNumberInputValidityListener {
        void onFinish(CountryCodePicker_app ccp, boolean isValid);
    }

    public CountryCodePicker_app(Context context) {
        super(context);

        init(null);
    }

    public CountryCodePicker_app(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CountryCodePicker_app(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    @SuppressWarnings("unused")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CountryCodePicker_app(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.layout_country_code_picker, this);

        m_tv_selected_country = findViewById(R.id.selected_country_tv);
        m_rly_holder = findViewById(R.id.country_code_holder_rly);
        m_imv_arrow = findViewById(R.id.arrow_imv);
        m_imv_flag = findViewById(R.id.flag_imv);
        m_lly_flag_holder = findViewById(R.id.flag_holder_lly);
        m_rly_click_consumer = findViewById(R.id.click_consumer_rly);

        applyCustomProperty(attrs);

        m_country_code_holder_clicklistener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickable()) {
                    showBottomSheetDialog();

                }
            }
        };

        m_rly_click_consumer.setOnClickListener(m_country_code_holder_clicklistener);
    }

    private void applyCustomProperty(AttributeSet attrs) {
        m_phone_util = PhoneNumberUtil.createInstance(getContext());
        Resources.Theme theme = getContext().getTheme();
        TypedArray a = theme.obtainStyledAttributes(attrs, R.styleable.CountryCodePicker, 0, 0);

        try {
            m_hide_phone_code = a.getBoolean(R.styleable.CountryCodePicker_ccp_hidePhoneCode, false);
            m_show_full_name = a.getBoolean(R.styleable.CountryCodePicker_ccp_showFullName, false);
            m_hide_name_code = a.getBoolean(R.styleable.CountryCodePicker_ccp_hideNameCode, false);

            m_is_hint_enabled = a.getBoolean(R.styleable.CountryCodePicker_ccp_enableHint, true);


            m_is_enable_phone_number_watcher =
                    a.getBoolean(R.styleable.CountryCodePicker_ccp_enablePhoneAutoFormatter, true);

            setKeyboardAutoPopOnSearch(
                    a.getBoolean(R.styleable.CountryCodePicker_ccp_keyboardAutoPopOnSearch, false));

            m_custom_master_countries = a.getString(R.styleable.CountryCodePicker_ccp_customMasterCountries);
            refreshCustomMasterList();

            m_country_preference = a.getString(R.styleable.CountryCodePicker_ccp_countryPreference);
            refreshPreferredCountries();

            applyCustomPropertyOfDefaultCountryNameCode(a);

            showFlag(a.getBoolean(R.styleable.CountryCodePicker_ccp_showFlag, true));

            applyCustomPropertyOfColor(a);


            String font_path = a.getString(R.styleable.CountryCodePicker_ccp_textFont);
            if (font_path != null && !font_path.isEmpty()) setTypeFace(font_path);


            int text_size = a.getDimensionPixelSize(R.styleable.CountryCodePicker_ccp_textSize, 0);
            if (text_size > 0) {
                m_tv_selected_country.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);
                setFlagSize(text_size);
                setArrowSize(text_size);
            } else {
                DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
                int default_size = Math.round(18 * (dm.xdpi / DisplayMetrics.DENSITY_DEFAULT));
                setTextSize(default_size);
            }


            int arrowSize = a.getDimensionPixelSize(R.styleable.CountryCodePicker_ccp_arrowSize, 0);
            if (arrowSize > 0) setArrowSize(arrowSize);

            m_selection_dialog_show_search =
                    a.getBoolean(R.styleable.CountryCodePicker_ccp_selectionDialogShowSearch, true);
            setClickable(a.getBoolean(R.styleable.CountryCodePicker_ccp_clickable, true));

            m_set_country_by_time_zone =
                    a.getBoolean(R.styleable.CountryCodePicker_ccp_setCountryByTimeZone, true);


            if (m_default_country_name_code == null || m_default_country_name_code.isEmpty()) {
                setDefaultCountryFlagAndCode();
            }
        } catch (Exception e) {
            Log.d(tag, "exception = " + e.toString());
            if (isInEditMode()) {
                m_tv_selected_country.setText(
                        getContext().getString(R.string.phone_code,
                                getContext().getString(R.string.country_indonesia_number)));
            } else {
                m_tv_selected_country.setText(e.getMessage());
            }
        } finally {
            a.recycle();
        }
    }

    private void applyCustomPropertyOfDefaultCountryNameCode(TypedArray tar) {

        m_default_country_name_code = tar.getString(R.styleable.CountryCodePicker_ccp_defaultNameCode);
        if (BuildConfig.DEBUG) {
            Log.d(tag, "mDefaultCountryNameCode from attribute = " + m_default_country_name_code);
        }

        if (m_default_country_name_code == null || m_default_country_name_code.isEmpty()) return;

        if (m_default_country_name_code.trim().isEmpty()) {
            m_default_country_name_code = null;
            return;
        }

        setDefaultCountryUsingNameCode(m_default_country_name_code);
        setSelectedCountry(m_default_country);
    }

    private void applyCustomPropertyOfColor(TypedArray arr) {

        int text_color;
        if (isInEditMode()) {
            text_color = arr.getColor(R.styleable.CountryCodePicker_ccp_textColor, default_text_color);
        } else {
            text_color = arr.getColor(R.styleable.CountryCodePicker_ccp_textColor,
                    getColor(getContext(), R.color.defaultTextColor));
        }
        if (text_color != 0) setTextColor(text_color);

        m_dialog_text_color =
                arr.getColor(R.styleable.CountryCodePicker_ccp_dialogTextColor, default_text_color);


        m_background_color =
                arr.getColor(R.styleable.CountryCodePicker_ccp_backgroundColor, Color.TRANSPARENT);

        if (m_background_color != Color.TRANSPARENT) m_rly_holder.setBackgroundColor(m_background_color);
    }

    private Country_app getDefaultCountry() {
        return m_default_country;
    }

    private void setDefaultCountry(Country_app defaultCountry) {
        m_default_country = defaultCountry;
    }

    @SuppressWarnings("unused")
    private Country_app getSelectedCountry() {
        return m_selected_country;
    }

    protected void setSelectedCountry(Country_app selected_country) {
        m_selected_country = selected_country;

        Context ctx = getContext();


        if (selected_country == null) {
            selected_country = CountryUtils_app.getByCode(ctx, m_preferred_countries, m_default_country_code);
        }

        if (m_registered_phone_number_textview != null) {
            String iso = selected_country.getIso().toUpperCase();
            setPhoneNumberWatcherToTextView(m_registered_phone_number_textview, iso);
        }

        if (m_on_country_change_listener != null) {
            m_on_country_change_listener.onCountrySelected(selected_country);
        }

        m_imv_flag.setImageResource(CountryUtils_app.getFlagDrawableResId(selected_country));

        if (m_is_hint_enabled) setPhoneNumberHint();

        setSelectedCountryText(ctx, selected_country);
    }

    private void setSelectedCountryText(Context ctx, Country_app selected_country) {
        if (m_hide_name_code && m_hide_phone_code && !m_show_full_name) {
            m_tv_selected_country.setText("");
            return;
        }

        String phone_code = selected_country.getPhone_code();
        if (m_show_full_name) {
            String country_name = selected_country.getName().toUpperCase();

            if (m_hide_phone_code && m_hide_name_code) {
                m_tv_selected_country.setText(country_name);
                return;
            }

            if (m_hide_name_code) {
                m_tv_selected_country.setText(ctx.getString(R.string.country_full_name_and_phone_code,
                        country_name, phone_code));
                return;
            }

            String iso = selected_country.getIso().toUpperCase();
            if (m_hide_phone_code) {
                m_tv_selected_country.setText(ctx.getString(R.string.country_full_name_and_name_code,
                        country_name, iso));
                return;
            }

            m_tv_selected_country.setText(ctx.getString(R.string.country_full_name_name_code_and_phone_code,
                    country_name, iso, phone_code));

            return;
        }

        if (m_hide_name_code && m_hide_phone_code) {
            String country_name = selected_country.getName().toUpperCase();
            m_tv_selected_country.setText(country_name);
            return;
        }

        if (m_hide_name_code) {
            m_tv_selected_country.setText(ctx.getString(R.string.phone_code, phone_code));
            return;
        }

        if (m_hide_phone_code) {
            String iso = selected_country.getIso().toUpperCase();
            m_tv_selected_country.setText(iso);
            return;
        }

        String iso = selected_country.getIso().toUpperCase();
        m_tv_selected_country.setText(ctx.getString(R.string.country_code_and_phone_code, iso, phone_code));
    }

    boolean isKeyboardAutoPopOnSearch() {
        return m_keyboard_auto_pop_on_search;
    }


    public void setKeyboardAutoPopOnSearch(boolean keyboardAutoPopOnSearch) {
        m_keyboard_auto_pop_on_search = keyboardAutoPopOnSearch;
    }


    @SuppressWarnings("unused")
    public boolean isPhoneAutoFormatterEnabled() {
        return m_is_enable_phone_number_watcher;
    }


    @SuppressWarnings("unused")
    public void enablePhoneAutoFormatter(boolean isEnable) {
        m_is_enable_phone_number_watcher = isEnable;
        if (isEnable) {
            if (m_phone_number_watcher == null) {
                m_phone_number_watcher = new PhoneNumberWatcher(getSelectedCountryNameCode());
            }
        } else {
            m_phone_number_watcher = null;
        }
    }

    @SuppressWarnings("unused")
    private OnClickListener getCountryCodeHolderClickListener() {
        return m_country_code_holder_clicklistener;
    }


    void refreshPreferredCountries() {
        if (m_country_preference == null || m_country_preference.length() == 0) {
            m_preferred_countries = null;
            return;
        }

        List<Country_app> localCountryList = new ArrayList<>();
        for (String name_code : m_country_preference.split(",")) {
            Country_app country =
                    CountryUtils_app.getByNameCodeFromCustomCountries(getContext(), m_custom_master_countries_list,
                            name_code);
            if (country == null) continue;

            if (isAlreadyInList(country, localCountryList)) continue;
            localCountryList.add(country);
        }

        if (localCountryList.size() == 0) {
            m_preferred_countries = null;
        } else {
            m_preferred_countries = localCountryList;
        }
    }


    void refreshCustomMasterList() {
        if (m_custom_master_countries == null || m_custom_master_countries.length() == 0) {
            m_custom_master_countries_list = null;
            return;
        }

        List<Country_app> localCountries = new ArrayList<>();
        String[] split = m_custom_master_countries.split(",");
        for (int i = 0; i < split.length; i++) {
            String nameCode = split[i];
            Country_app country = CountryUtils_app.getByNameCodeFromAllCountries(getContext(), nameCode);
            if (country == null) continue;

            if (isAlreadyInList(country, localCountries)) continue;
            localCountries.add(country);
        }

        if (localCountries.size() == 0) {
            m_custom_master_countries_list = null;
        } else {
            m_custom_master_countries_list = localCountries;
        }
    }

    List<Country_app> getCustomCountries() {
        return m_custom_master_countries_list;
    }


    List<Country_app> getCustomCountries(@NonNull CountryCodePicker_app codePicker) {
        codePicker.refreshCustomMasterList();
        if (codePicker.getCustomCountries() == null || codePicker.getCustomCountries().size() <= 0) {
            return CountryUtils_app.getAllCountries(codePicker.getContext());
        } else {
            return codePicker.getCustomCountries();
        }
    }

    @SuppressWarnings("unused")
    public void setCustomMasterCountriesList(@Nullable List<Country_app> customMasterCountriesList) {
        m_custom_master_countries_list = customMasterCountriesList;
    }

    @SuppressWarnings("unused")
    public String getCustomMasterCountries() {
        return m_custom_master_countries;
    }

    public List<Country_app> getPreferredCountries() {
        return m_preferred_countries;
    }


    @SuppressWarnings("unused")
    public void setCustomMasterCountries(@Nullable String customMasterCountries) {
        m_custom_master_countries = customMasterCountries;
    }


    private boolean isAlreadyInList(Country_app country, List<Country_app> countries) {
        if (country == null || countries == null) return false;

        for (int i = 0; i < countries.size(); i++) {
            if (countries.get(i).getIso().equalsIgnoreCase(country.getIso())) {
                return true;
            }
        }

        return false;
    }


    private String detectCarrierNumber(String fullNumber, Country_app country) {
        String carrier_number;
        if (country == null || fullNumber == null) {
            carrier_number = fullNumber;
        } else {
            int indexOfCode = fullNumber.indexOf(country.getPhone_code());
            if (indexOfCode == -1) {
                carrier_number = fullNumber;
            } else {
                carrier_number = fullNumber.substring(indexOfCode + country.getPhone_code().length());
            }
        }
        return carrier_number;
    }


    @Deprecated
    public void setDefaultCountryUsingPhoneCode(int defaultCountryCode) {
        Country_app default_country =
                CountryUtils_app.getByCode(getContext(), m_preferred_countries, defaultCountryCode);

        if (default_country == null) return;


        m_default_country_code = defaultCountryCode;
        setDefaultCountry(default_country);
    }

    public void setDefaultCountryUsingPhoneCodeAndApply(int defaultCountryCode) {
        Country_app default_country =
                CountryUtils_app.getByCode(getContext(), m_preferred_countries, defaultCountryCode);

        if (default_country == null) return;


        m_default_country_code = defaultCountryCode;
        setDefaultCountry(default_country);

        resetToDefaultCountry();
    }


    public void setDefaultCountryUsingNameCode(@NonNull String countryIso) {
        Country_app default_country = CountryUtils_app.getByNameCodeFromAllCountries(getContext(), countryIso);

        if (default_country == null) return;


        m_default_country_name_code = default_country.getIso();
        setDefaultCountry(default_country);
    }


    public void setDefaultCountryUsingNameCodeAndApply(@NonNull String countryIso) {
        Country_app default_country = CountryUtils_app.getByNameCodeFromAllCountries(getContext(), countryIso);

        if (default_country == null) return;


        m_default_country_name_code = default_country.getIso();
        setDefaultCountry(default_country);



        setEmptyDefault(null);
    }


    public String getDefaultCountryCode() {
        return m_default_country.getPhone_code();
    }


    @SuppressWarnings("unused")
    public int getDefaultCountryCodeAsInt() {
        int code = 0;
        try {
            code = Integer.parseInt(getDefaultCountryCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }


    @SuppressWarnings("unused")
    public String getDefaultCountryCodeWithPlus() {
        return getContext().getString(R.string.phone_code, getDefaultCountryCode());
    }


    @SuppressWarnings("unused")
    public String getDefaultCountryName() {
        return m_default_country.getName();
    }


    public String getDefaultCountryNameCode() {
        return m_default_country.getIso().toUpperCase();
    }


    @SuppressWarnings("unused")
    public void resetToDefaultCountry() {
        setEmptyDefault();
    }


    public String getSelectedCountryCode() {
        return m_selected_country.getPhone_code();
    }


    @SuppressWarnings("unused")
    public String getSelectedCountryCodeWithPlus() {
        return getContext().getString(R.string.phone_code, getSelectedCountryCode());
    }


    @SuppressWarnings("unused")
    public int getSelectedCountryCodeAsInt() {
        int code = 0;
        try {
            code = Integer.parseInt(getSelectedCountryCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }


    @SuppressWarnings("unused")
    public String getSelectedCountryName() {
        return m_selected_country.getName();
    }


    public String getSelectedCountryNameCode() {
        return m_selected_country.getIso().toUpperCase();
    }


    @SuppressWarnings("unused")
    public void setCountryForPhoneCode(int countryCode) {
        Context ctx = getContext();
        Country_app country = CountryUtils_app.getByCode(ctx, m_preferred_countries, countryCode);
        if (country == null) {
            if (m_default_country == null) {
                m_default_country = CountryUtils_app.getByCode(ctx, m_preferred_countries, m_default_country_code);
            }
            setSelectedCountry(m_default_country);
        } else {
            setSelectedCountry(country);
        }
    }


    @SuppressWarnings("unused")
    public void setCountryForNameCode(@NonNull String countryNameCode) {
        Context ctx = getContext();
        Country_app country = CountryUtils_app.getByNameCodeFromAllCountries(ctx, countryNameCode);
        if (country == null) {
            if (m_default_country == null) {
                m_default_country = CountryUtils_app.getByCode(ctx, m_preferred_countries, m_default_country_code);
            }
            setSelectedCountry(m_default_country);
        } else {
            setSelectedCountry(country);
        }
    }


    @SuppressWarnings("unused")
    public void registerPhoneNumberTextView(@NonNull TextView textView) {
        setRegisteredPhoneNumberTextView(textView);
        if (m_is_hint_enabled) setPhoneNumberHint();
    }

    @SuppressWarnings("unused")
    public TextView getRegisteredPhoneNumberTextView() {
        return m_registered_phone_number_textview;
    }

    void setRegisteredPhoneNumberTextView(@NonNull TextView phoneNumberTextView) {
        m_registered_phone_number_textview = phoneNumberTextView;
        if (m_is_enable_phone_number_watcher) {
            if (m_phone_number_watcher == null) {
                m_phone_number_watcher = new PhoneNumberWatcher(getDefaultCountryNameCode());
            }
            m_registered_phone_number_textview.addTextChangedListener(m_phone_number_watcher);
        }
    }

    private void setPhoneNumberWatcherToTextView(TextView textView, String countryNameCode) {
        if (!m_is_enable_phone_number_watcher) return;

        if (m_phone_number_watcher == null) {
            m_phone_number_watcher = new PhoneNumberWatcher(countryNameCode);
            textView.addTextChangedListener(m_phone_number_watcher);
        } else {
            if (!m_phone_number_watcher.getPreviousCountryCode().equalsIgnoreCase(countryNameCode)) {
                textView.removeTextChangedListener(m_phone_number_watcher);
                m_phone_number_watcher = new PhoneNumberWatcher(countryNameCode);
                textView.addTextChangedListener(m_phone_number_watcher);
            }
        }
    }


    public String getFullNumber() {
        String full_number = m_selected_country.getPhone_code();
        if (m_registered_phone_number_textview == null) {
            Log.w(tag, getContext().getString(R.string.error_unregister_carrier_number));
        } else {
            full_number += m_registered_phone_number_textview.getText().toString();
        }
        return full_number;
    }


    @SuppressWarnings("unused")
    public void setFullNumber(@NonNull String fullNumber) {
        Country_app country = CountryUtils_app.getByNumber(getContext(), m_preferred_countries, fullNumber);
        setSelectedCountry(country);
        String carrier_number = detectCarrierNumber(fullNumber, country);
        if (m_registered_phone_number_textview == null) {
            Log.w(tag, getContext().getString(R.string.error_unregister_carrier_number));
        } else {
            m_registered_phone_number_textview.setText(carrier_number);
        }
    }


    @SuppressWarnings("unused")
    public String getFullNumberWithPlus() {
        return getContext().getString(R.string.phone_code, getFullNumber());
    }


    @SuppressWarnings("unused")
    public int getTextColor() {
        return m_text_color;
    }

    public int getDefaultContentColor() {
        return default_text_color;
    }


    public void setTextColor(int contentColor) {
        m_text_color = contentColor;
        m_tv_selected_country.setTextColor(contentColor);
        m_imv_arrow.setColorFilter(contentColor, PorterDuff.Mode.SRC_IN);
    }

    public int getBackgroundColor() {
        return m_background_color;
    }

    public void setBackgroundColor(int background_color) {
        m_background_color = background_color;
        m_rly_holder.setBackgroundColor(background_color);
    }

    public int getDefaultBackgroundColor() {
        return default_background_color;
    }


    public void setTextSize(int text_size) {
        if (text_size > 0) {
            m_tv_selected_country.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);
            setArrowSize(text_size);
            setFlagSize(text_size);
        }
    }


    public void setArrowSize(int arrow_size_in_dp) {
        if (arrow_size_in_dp > 0) {
            LayoutParams params = (LayoutParams) m_imv_arrow.getLayoutParams();
            params.width = arrow_size_in_dp;
            params.height = arrow_size_in_dp;
            m_imv_arrow.setLayoutParams(params);
        }
    }


    @SuppressWarnings("unused")
    public void hideNameCode(boolean hide) {
        m_hide_name_code = hide;
        setSelectedCountry(m_selected_country);
    }


    @SuppressWarnings("unused")
    public void setCountryPreference(@NonNull String country_preference) {
        m_country_preference = country_preference;
    }


    @SuppressWarnings("unused")
    public void setTypeFace(@NonNull Typeface type_face) {
        m_typeFace = type_face;
        try {
            m_tv_selected_country.setTypeface(type_face);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setTypeFace(@NonNull String font_asset_path) {
        try {
            Typeface type_face = Typeface.createFromAsset(getContext().getAssets(), font_asset_path);
            m_typeFace = type_face;
            m_tv_selected_country.setTypeface(type_face);
        } catch (Exception e) {
            Log.d(tag, "Invalid fontPath. " + e.toString());
        }
    }


    @SuppressWarnings("unused")
    public void setTypeFace(@NonNull Typeface type_face, int style) {
        try {
            m_tv_selected_country.setTypeface(type_face, style);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Typeface getTypeFace() {
        return m_typeFace;
    }


    @SuppressWarnings("unused")
    public void setOnCountryChangeListener(@NonNull OnCountryChangeListener on_country_change_listener) {
        m_on_country_change_listener = on_country_change_listener;
    }


    public void setFlagSize(int flag_size) {
        m_imv_flag.getLayoutParams().height = flag_size;
        m_imv_flag.requestLayout();
    }

    public void showFlag(boolean show_flag) {
        m_show_flag = show_flag;
        m_lly_flag_holder.setVisibility(show_flag ? VISIBLE : GONE);
    }


    @SuppressWarnings("unused")
    public void showFullName(boolean show) {
        m_show_full_name = show;
        setSelectedCountry(m_selected_country);
    }


    public boolean isSelectionDialogShowSearch() {
        return m_selection_dialog_show_search;
    }


    @SuppressWarnings("unused")
    public void setSelectionDialogShowSearch(boolean selection_dialog_show_search) {
        m_selection_dialog_show_search = selection_dialog_show_search;
    }

    @Override
    public boolean isClickable() {
        return m_is_clickable;
    }


    public void setClickable(boolean isClickable) {
        m_is_clickable = isClickable;
        m_rly_click_consumer.setOnClickListener(isClickable ? m_country_code_holder_clicklistener : null);
        m_rly_click_consumer.setClickable(isClickable);
        m_rly_click_consumer.setEnabled(isClickable);
    }

    public boolean isHidePhoneCode() {
        return m_hide_phone_code;
    }

    public boolean isHideNameCode() {
        return m_hide_name_code;
    }


    @SuppressWarnings("unused")
    public boolean isHintEnabled() {
        return m_is_hint_enabled;
    }


    @SuppressWarnings("unused")
    public void enableHint(boolean hintEnabled) {
        m_is_hint_enabled = hintEnabled;
        if (m_is_hint_enabled) setPhoneNumberHint();
    }


    @SuppressWarnings("unused")
    public void hidePhoneCode(boolean hide) {
        m_hide_phone_code = hide;
        setSelectedCountry(m_selected_country);
    }

    private void setPhoneNumberHint() {

        if (m_registered_phone_number_textview == null
                || m_selected_country == null
                || m_selected_country.getIso() == null) {
            return;
        }

        String iso = m_selected_country.getIso().toUpperCase();
        PhoneNumberUtil.PhoneNumberType mobile = PhoneNumberUtil.PhoneNumberType.MOBILE;
        Phonenumber.PhoneNumber phoneNumber = m_phone_util.getExampleNumberForType(iso, mobile);
        if (phoneNumber == null) {
            m_registered_phone_number_textview.setHint("");
            return;
        }

        if (BuildConfig.DEBUG) {
            Log.d(tag, "setPhoneNumberHint called");
            Log.d(tag, "mSelectedCountry.getIso() = " + m_selected_country.getIso());
            Log.d(tag,
                    "hint = " + m_phone_util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
        }

        String hint = m_phone_util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);



        m_registered_phone_number_textview.setHint(hint);
    }

    private class PhoneNumberWatcher extends PhoneNumberFormattingTextWatcher {
        private boolean lastValidity;
        private String previousCountryCode = "";

        String getPreviousCountryCode() {
            return previousCountryCode;
        }

        @SuppressWarnings("unused")
        public PhoneNumberWatcher() {
            super();
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public PhoneNumberWatcher(String countryCode) {
            super(countryCode);
            previousCountryCode = countryCode;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            super.onTextChanged(s, start, before, count);
            try {
                String iso = null;
                if (m_selected_country != null) iso = m_selected_country.getPhone_code().toUpperCase();
                Phonenumber.PhoneNumber phoneNumber = m_phone_util.parse(s.toString(), iso);
                iso = m_phone_util.getRegionCodeForNumber(phoneNumber);
                if (iso != null) {


                }
            } catch (NumberParseException ignored) {
            }

            if (m_phone_number_input_validity_listener != null) {
                boolean validity = isValid();
                if (validity != lastValidity) {
                    m_phone_number_input_validity_listener.onFinish(CountryCodePicker_app.this, validity);
                }
                lastValidity = validity;
            }
        }
    }


    @SuppressWarnings("unused")
    public String getNumber() {
        Phonenumber.PhoneNumber phoneNumber = getPhoneNumber();

        if (phoneNumber == null) return null;

        if (m_registered_phone_number_textview == null) {
            Log.w(tag, getContext().getString(R.string.error_unregister_carrier_number));
            return null;
        }

        return m_phone_util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
    }


    @SuppressWarnings("unused")
    public Phonenumber.PhoneNumber getPhoneNumber() {
        try {
            String iso = null;
            if (m_selected_country != null) iso = m_selected_country.getIso().toUpperCase();
            if (m_registered_phone_number_textview == null) {
                Log.w(tag, getContext().getString(R.string.error_unregister_carrier_number));
                return null;
            }
            return m_phone_util.parse(m_registered_phone_number_textview.getText().toString(), iso);
        } catch (NumberParseException ignored) {
            return null;
        }
    }

    @SuppressWarnings("unused")
    public boolean isValid() {
        Phonenumber.PhoneNumber phoneNumber = getPhoneNumber();
        return phoneNumber != null && m_phone_util.isValidNumber(phoneNumber);
    }

    @SuppressWarnings("unused")
    public void setPhoneNumberInputValidityListener(PhoneNumberInputValidityListener listener) {
        m_phone_number_input_validity_listener = listener;
    }


    private void setDefaultCountryFlagAndCode() {
        Context ctx = getContext();
        TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager == null) {
            Log.e(tag, "Can't access TelephonyManager. Using default county code");
            setEmptyDefault(getDefaultCountryCode());
            return;
        }

        try {
            String sim_country_iso = manager.getSimCountryIso();
            if (sim_country_iso == null || sim_country_iso.isEmpty()) {
                String iso = manager.getNetworkCountryIso();
                if (iso == null || iso.isEmpty()) {
                    enableSetCountryByTimeZone(true);
                } else {
                    setEmptyDefault(iso);
                    if (BuildConfig.DEBUG) Log.d(tag, "isoNetwork = " + iso);
                }
            } else {
                setEmptyDefault(sim_country_iso);
                if (BuildConfig.DEBUG) Log.d(tag, "simCountryIso = " + sim_country_iso);
            }
        } catch (Exception e) {
            Log.e(tag, "Error when getting sim country, error = " + e.toString());
            setEmptyDefault(getDefaultCountryCode());
        }
    }

    private void setEmptyDefault() {
        setEmptyDefault(null);
    }

    private void setEmptyDefault(String country_code) {
        if (country_code == null || country_code.isEmpty()) {
            if (m_default_country_name_code == null || m_default_country_name_code.isEmpty()) {
                if (default_country == null || default_country.isEmpty()) {
                    country_code = default_iso_country;
                } else {
                    country_code = default_country;
                }
            } else {
                country_code = m_default_country_name_code;
            }
        }

        if (m_is_enable_phone_number_watcher && m_phone_number_watcher == null) {
            m_phone_number_watcher = new PhoneNumberWatcher(country_code);
        }

        setDefaultCountryUsingNameCode(country_code);
        setSelectedCountry(getDefaultCountry());
    }


    public void enableSetCountryByTimeZone(boolean isEnabled) {
        if (isEnabled) {
            if (m_default_country_name_code != null && !m_default_country_name_code.isEmpty()) return;
            if (m_registered_phone_number_textview != null) return;
            if (m_set_country_by_time_zone) {
                TimeZone tz = TimeZone.getDefault();

                if (BuildConfig.DEBUG) Log.d(tag, "tz.getID() = " + tz.getID());
                List<String> country_isos = CountryUtils_app.getCountryIsoByTimeZone(getContext(), tz.getID());

                if (country_isos == null) {

                    setEmptyDefault();
                } else {
                    setDefaultCountryUsingNameCode(country_isos.get(0));
                    setSelectedCountry(getDefaultCountry());
                }
            }
        }
        m_set_country_by_time_zone = isEnabled;
    }

    public int getDialogTextColor() {
        return m_dialog_text_color;
    }

    @SuppressWarnings("unused")
    public void setDialogTextColor(int dialogTextColor) {
        m_dialog_text_color = dialogTextColor;
    }

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }
    private List<Country_app> master_countries;
    private List<Country_app> m_filtered_countries;
    private InputMethodManager m_input_method_manager;
    private CountryCodeArray_app_Adapter m_array_adapter;
    private List<Country_app> m_temp_countries;
    EditText m_edt_search;
    RelativeLayout m_rly_dialog;
    ListView mlv_country_dialog;
    TextView m_tv_title;
    TextView m_tv_no_result;
    private CountryCodePicker_app m_country_code_picker = this;
    BottomSheetDialog bottom_sheet_dialog;

    private void showBottomSheetDialog() {
        bottom_sheet_dialog = new BottomSheetDialog(getContext(), R.style.ThemeOverlay_Demo_BottomSheetDialog);
        bottom_sheet_dialog.setContentView(R.layout.dialog_country_code_picker);

        m_rly_dialog = bottom_sheet_dialog.findViewById(R.id.dialog_rly);
        mlv_country_dialog = bottom_sheet_dialog.findViewById(R.id.country_dialog_lv);
        m_tv_title = bottom_sheet_dialog.findViewById(R.id.title_tv);
        m_edt_search = bottom_sheet_dialog.findViewById(R.id.search_edt);
        m_tv_no_result = bottom_sheet_dialog.findViewById(R.id.no_result_tv);

        bottom_sheet_dialog.show();
        setupData();
    }

    private void setupData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mlv_country_dialog.setLayoutDirection(m_country_code_picker.getLayoutDirection());
        }
        if (m_country_code_picker.getTypeFace() != null) {
            Typeface typeface = m_country_code_picker.getTypeFace();
            m_tv_title.setTypeface(typeface);
            m_edt_search.setTypeface(typeface);
            m_tv_no_result.setTypeface(typeface);
        }
        if (m_country_code_picker.getBackgroundColor() != m_country_code_picker.getDefaultBackgroundColor()) {
            m_rly_dialog.setBackgroundColor(m_country_code_picker.getBackgroundColor());
        }
        if (m_country_code_picker.getDialogTextColor() != m_country_code_picker.getDefaultContentColor()) {
            int color = m_country_code_picker.getDialogTextColor();
            m_tv_title.setTextColor(color);
            m_tv_no_result.setTextColor(color);
            m_edt_search.setTextColor(color);
            m_edt_search.setHintTextColor(adjustAlpha(color, 0.7f));
        }

        m_country_code_picker.refreshCustomMasterList();
        m_country_code_picker.refreshPreferredCountries();
        master_countries = m_country_code_picker.getCustomCountries(m_country_code_picker);

        m_filtered_countries = getFilteredCountries();
        setupListView(mlv_country_dialog);

        Context ctx = m_country_code_picker.getContext();
        m_input_method_manager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        setSearchBar();
    }

    private void setupListView(ListView listView) {
        m_array_adapter = new CountryCodeArray_app_Adapter(getContext(), m_filtered_countries, m_country_code_picker);

        if (!m_country_code_picker.isSelectionDialogShowSearch()) {
            LayoutParams params = (LayoutParams) listView.getLayoutParams();
            params.height = ListView.LayoutParams.WRAP_CONTENT;
            listView.setLayoutParams(params);
        }

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (m_filtered_countries == null) {
                    Log.e(tag, "no filtered countries found! This should not be happened, Please report!");
                    return;
                }

                if (m_filtered_countries.size() < position || position < 0) {
                    Log.e(tag, "Something wrong with the ListView. Please report this!");
                    return;
                }

                Country_app country = m_filtered_countries.get(position);

                if (country == null) return;

                m_country_code_picker.setSelectedCountry(country);
                m_input_method_manager.hideSoftInputFromWindow(m_edt_search.getWindowToken(), 0);
                bottom_sheet_dialog.dismiss();
            }
        };
        listView.setOnItemClickListener(listener);
        listView.setAdapter(m_array_adapter);
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private void setSearchBar() {
        if (m_country_code_picker.isSelectionDialogShowSearch()) {
            setTextWatcher();
        } else {
            m_edt_search.setVisibility(View.GONE);
        }
    }


    private void setTextWatcher() {
        if (m_edt_search == null) return;

        m_edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyQuery(s.toString());
            }
        });

        if (m_country_code_picker.isKeyboardAutoPopOnSearch()) {
            if (m_input_method_manager != null) {
                m_input_method_manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }


    private void applyQuery(String query) {
        m_tv_no_result.setVisibility(View.GONE);
        query = query.toLowerCase();


        if (query.length() > 0 && query.charAt(0) == '+') {
            query = query.substring(1);
        }

        m_filtered_countries = getFilteredCountries(query);

        if (m_filtered_countries.size() == 0) {
            m_tv_no_result.setVisibility(View.VISIBLE);
        }

        m_array_adapter.notifyDataSetChanged();
    }

    private List<Country_app> getFilteredCountries() {
        return getFilteredCountries("");
    }

    private List<Country_app> getFilteredCountries(String query) {
        if (m_temp_countries == null) {
            m_temp_countries = new ArrayList<>();
        } else {
            m_temp_countries.clear();
        }

        List<Country_app> preferred_countries = m_country_code_picker.getPreferredCountries();
        if (preferred_countries != null && preferred_countries.size() > 0) {
            for (Country_app country : preferred_countries) {
                if (country.isEligibleForQuery(query)) {
                    m_temp_countries.add(country);
                }
            }

            if (m_temp_countries.size() > 0) {
                m_temp_countries.add(null);
            }
        }

        for (Country_app country : master_countries) {
            if (country.isEligibleForQuery(query)) {
                m_temp_countries.add(country);
            }
        }
        return m_temp_countries;
    }
}