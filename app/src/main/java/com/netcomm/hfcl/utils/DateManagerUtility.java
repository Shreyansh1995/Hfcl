package com.netcomm.hfcl.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import com.netcomm.hfcl.sharedpreference.MySharedPreference;

import java.util.Calendar;

/**
 * Created by Netcomm on 2/16/2017.
 */

public class DateManagerUtility {
    private Context mContext;
    private int curYear;
    private int curMonth;
    private int curDay;
    private int fromYear;
    private int fromMonth;
    private int fromDay;
    private int toYear;
    private int toMonth;
    private int toDay;
    private MySharedPreference pref;
    private TextView tv_FromDate;
    private TextView tv_ToDate;


    // for every screen this method should call only once :)
    public void setCurrentDate(Context mContext, TextView tvFrom, TextView tvTo) {
        Calendar c = MyCalenderUtil.getCurrentDateTime();
        curYear = c.get(Calendar.YEAR);
        curMonth = c.get(Calendar.MONTH);
        curDay = c.get(Calendar.DAY_OF_MONTH);
        toYear = curYear;
        toMonth = curMonth;
        toDay = curDay;
        pref = MySharedPreference.getInstance(mContext);
        tv_FromDate = tvFrom;
        tv_ToDate = tvTo;
        this.mContext = mContext;
        tv_ToDate.setText("" + curDay + "-" + (curMonth + 1) + "-" + curYear);
        Calendar cal = MyCalenderUtil.createCalenderWith6monthDaysBehind("" + curDay, "" + curMonth, "" + curYear);
        fromYear = cal.get(Calendar.YEAR);
        fromMonth = cal.get(Calendar.MONTH);
        fromDay = cal.get(Calendar.DAY_OF_MONTH);
        tv_FromDate.setText("" + cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR));


    }
    public void setCurrentDate1(Context mContext, TextView tvFrom, TextView tvTo) {
        Calendar c = MyCalenderUtil.getCurrentDateTime();
        curYear = c.get(Calendar.YEAR);
        curMonth = c.get(Calendar.MONTH);
        curDay = c.get(Calendar.DAY_OF_MONTH);
        toYear = curYear;
        toMonth = curMonth;
        toDay = curDay;
        pref = MySharedPreference.getInstance(mContext);
        tv_FromDate = tvFrom;
        tv_ToDate = tvTo;
        this.mContext = mContext;
        tv_ToDate.setText("" + curDay + "-" + (curMonth + 1) + "-" + curYear);
        Calendar cal = MyCalenderUtil.createCalenderWith1monthDaysBehind("" + curDay, "" + curMonth, "" + curYear);
        fromYear = cal.get(Calendar.YEAR);
        fromMonth = cal.get(Calendar.MONTH);
        fromDay = cal.get(Calendar.DAY_OF_MONTH);
        tv_FromDate.setText("" + cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.YEAR));


    }

    public void FromDatePicker(final TextView tv) {
        DatePickerDialog expdatePickerDialog = null;
        expdatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv.setText("" + dayOfMonth + "-" + (month + 1) + "-" + year);
                fromDay = dayOfMonth;
                fromMonth = month;
                fromYear = year;

            }
        }, fromYear, fromMonth, fromDay);
        //Calendar cal = MyCalenderUtil.createCalender("" + curDay, "" + (curMonth), "" + curYear);
        //expdatePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        expdatePickerDialog.show();
    }

    public void ToDatePicker(final TextView tv) {
        DatePickerDialog expdatePickerDialog = null;
        expdatePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                tv.setText("" + dayOfMonth + "-" + (month + 1) + "-" + year);
                toDay = dayOfMonth;
                toMonth = month;
                toYear = year;
            }
        }, toYear, toMonth, toDay);
      //  Calendar cal = MyCalenderUtil.createCalender("" + curDay, "" + (curMonth), "" + curYear);
        //expdatePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        expdatePickerDialog.show();
    }


}
