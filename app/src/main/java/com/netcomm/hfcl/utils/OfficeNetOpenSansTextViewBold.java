package com.netcomm.hfcl.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Netcomm on 4/17/2017.
 */

public class OfficeNetOpenSansTextViewBold extends TextView {


    public OfficeNetOpenSansTextViewBold(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/open-sans_bold.ttf");
        this.setTypeface(face);
    }

    public OfficeNetOpenSansTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/open-sans_bold.ttf");
        this.setTypeface(face);
    }

    public OfficeNetOpenSansTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/open-sans_bold.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

}