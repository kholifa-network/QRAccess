package com.kholifa.qraccess;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class MySpannableScan extends ClickableSpan {

    private boolean is_underline = true;


    public MySpannableScan(boolean is_underline) {
        this.is_underline = is_underline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(is_underline);
        ds.setColor(Color.parseColor("#1b76d3"));
    }

    @Override
    public void onClick(View widget) {


    }
}