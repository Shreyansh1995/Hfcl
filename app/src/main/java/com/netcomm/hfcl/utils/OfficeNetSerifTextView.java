package com.netcomm.hfcl.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Netcomm on 2/24/2017.
 */

public class OfficeNetSerifTextView extends TextView {


    public OfficeNetSerifTextView(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/serif.ttf");
        this.setTypeface(face);
    }

    public OfficeNetSerifTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/serif.ttf");
        this.setTypeface(face);
    }

    public OfficeNetSerifTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/serif.ttf");
        this.setTypeface(face);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }

}