package com.kholifa.qraccess.countrylist;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.kholifa.qraccess.R;

import java.util.List;
import java.util.Locale;

public class CountryCodeArray_app_Adapter extends ArrayAdapter<Country_app> {
    private final CountryCodePicker_app m_country_code_picker;
    private String m_default_locale_language;

    CountryCodeArray_app_Adapter(Context ctx, List<Country_app> countries, CountryCodePicker_app picker) {
        super(ctx, 0, countries);
        m_country_code_picker = picker;
        m_default_locale_language = Locale.getDefault().getLanguage();
    }

    private static class ViewHolder {
        RelativeLayout rly_main;
        TextView tv_name, tv_code;
        ImageView imv_flag;
        LinearLayout lly_flag_holder;
        View view_divider;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Country_app country = getItem(position);

        ViewHolder view_holder;
        if (convertView == null) {
            view_holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_country_code_picker, parent, false);

            view_holder.rly_main = convertView.findViewById(R.id.item_country_rly);
            view_holder.tv_name = convertView.findViewById(R.id.country_name_tv);
            view_holder.tv_code = convertView.findViewById(R.id.code_tv);
            view_holder.imv_flag = convertView.findViewById(R.id.flag_imv);
            view_holder.lly_flag_holder = convertView.findViewById(R.id.flag_holder_lly);
            view_holder.view_divider = convertView.findViewById(R.id.preference_divider_view);
            convertView.setTag(view_holder);
        } else {
            view_holder = (ViewHolder) convertView.getTag();
        }
        setData(country, view_holder);
        return convertView;
    }

    private void setData(Country_app country, ViewHolder viewholder) {
        if (country == null) {
            viewholder.view_divider.setVisibility(View.VISIBLE);
            viewholder.tv_name.setVisibility(View.GONE);
            viewholder.tv_code.setVisibility(View.GONE);
            viewholder.lly_flag_holder.setVisibility(View.GONE);
            return;
        }

        viewholder.view_divider.setVisibility(View.GONE);
        viewholder.tv_name.setVisibility(View.VISIBLE);
        viewholder.tv_code.setVisibility(View.VISIBLE);
        viewholder.lly_flag_holder.setVisibility(View.VISIBLE);
        Context ctx = viewholder.tv_name.getContext();
        String name = country.getName();
        String iso = country.getIso().toUpperCase();
        String country_name;
        String country_name_and_code;
        try {
            country_name = getLocale(iso).getDisplayCountry();
        } catch (NullPointerException exception) {
            country_name = name;
        }
        if (m_country_code_picker.isHideNameCode()) {
            country_name_and_code = country_name;
        } else {
            country_name_and_code = ctx.getString(R.string.country_name_and_code, country_name, iso);
        }
        viewholder.tv_name.setText(country_name_and_code);

        if (m_country_code_picker.isHidePhoneCode()) {
            viewholder.tv_code.setVisibility(View.GONE);
        } else {
            viewholder.tv_code.setText(ctx.getString(R.string.phone_code, country.getPhone_code()));
        }

        Typeface typeface = m_country_code_picker.getTypeFace();
        if (typeface != null) {
            viewholder.tv_code.setTypeface(typeface);
            viewholder.tv_name.setTypeface(typeface);
        }
        viewholder.imv_flag.setImageResource(CountryUtils_app.getFlagDrawableResId(country));
        int color = m_country_code_picker.getDialogTextColor();
        if (color != m_country_code_picker.getDefaultContentColor()) {
            viewholder.tv_code.setTextColor(color);
            viewholder.tv_name.setTextColor(color);
        }
    }

    private Locale getLocale(String iso) throws NullPointerException {
        return new Locale(m_default_locale_language, iso);
    }
}