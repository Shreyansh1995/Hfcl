package com.netcomm.hfcl.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Netcomm on 2/4/2017.
 */
public class MyCalenderUtil {
    public static Calendar createCalenderWith6monthDaysBehind(String day, String month, String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, Integer.parseInt(month));
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        calendar.add(Calendar.DATE, -180);
        return calendar;
    } public static Calendar createCalenderWith1monthDaysBehind(String day, String month, String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, Integer.parseInt(month));
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        calendar.add(Calendar.DATE, -30);
        return calendar;
    }

    public static Calendar getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        return c;
    }

    public static Calendar createCalender(String day, String month, String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, Integer.parseInt(month));
        calendar.set(Calendar.YEAR, Integer.parseInt(year));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
        return calendar;
    }


    public static boolean validDate(Context mContext, String from, String to, TextView error_text) {
        Date fromDate = getDateFromString(from);
        Date toDate = getDateFromString(to);
        long diff = 0;
        try {
            diff = toDate.getTime() - fromDate.getTime();
            diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            Log.e("Days: ", "" + diff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (diff < 0) {
            error_text.setVisibility(View.VISIBLE);
            error_text.setText("From Date Must be Less Than To Date");
            return false;
        } else if (diff > 360) {
            error_text.setVisibility(View.VISIBLE);
            error_text.setText("From Date & To Date Different Only  1 year");
            return false;
        }
        return true;
    } public static boolean validDate1(Context mContext, String from, String to, TextView error_text) {
        Date fromDate = getDateFromString(from);
        Date toDate = getDateFromString(to);
        long diff = 0;
        try {
            diff = toDate.getTime() - fromDate.getTime();
            diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            Log.e("Days: ", "" + diff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (diff < 0) {
            error_text.setVisibility(View.VISIBLE);
            error_text.setText("From Date Must be Less Than To Date");
            return false;
        } else if (diff > 360) {
            error_text.setVisibility(View.VISIBLE);
            error_text.setText("From Date & To Date Different Only 1 year");
            return false;
        }
        return true;
    }


    private static Date getDateFromString(String dateString) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = format.parse(dateString);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDateInServerSendFormat(String date) {
        String arr[] = date.split("-");
        //return arr[1] + "/" + arr[0] + "/" + arr[2];
        return arr[0] + "/" + arr[1] + "/" + arr[2];
    }

    public static String getDateInServerSendFormatMM(String date) {
        String arr[] = date.split("-");
        return arr[1] + "/" + arr[0] + "/" + arr[2];
    }

    public static boolean validateFromTimeAndToTimeBasedOnAMPM(String fromTime, String toTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Calendar c = getCurrentDateTime();
        String fromDateFromat = "" + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + fromTime;
        String toDateFromat = "" + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + toTime;
        try {
            Date fromD = df.parse(fromDateFromat);
            Date toD = df.parse(toDateFromat);
            int hoursDifference = (int) ((toD.getTime() - fromD.getTime()) / 3600000L);
            // if remove the condition like fromTime == toTime
            if (hoursDifference == 0) {
                return true;
            }
            // then remove this if condition
            return toD.after(fromD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean validateFromTimeAndToTime(String fromTime, String toTime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Calendar c = getCurrentDateTime();
        String fromDateFromat = "" + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + fromTime;
        String toDateFromat = "" + c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH) + " " + toTime;
        try {
            Date fromD = df.parse(fromDateFromat);
            Date toD = df.parse(toDateFromat);
            int hoursDifference = (int) ((toD.getTime() - fromD.getTime()) / 3600000L);
            // if remove the condition like fromTime == toTime
            if (hoursDifference == 0) {
                return true;
            }
            // then remove this if condition
            return toD.after(fromD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean validateFromDateAndToDate(String fromDate, String fromTime, String toDate, String toTime) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        boolean b = false;
        String d1 = fromDate + " " + fromTime;
        String d2 = toDate + " " + toTime;


        try {
            Log.d("@@@@", "validateFromDateAndToDate: "+ df.parse(d1).toString());
            Log.d("@@@@", "validateFromDateAndToDate: "+ df.parse(d2).toString());
            if(df.parse(d1).before(df.parse(d2)))
            {
                b = true;//If start date is before end date
            }
            else if(df.parse(d1).equals(df.parse(d2)))
                {
                b = false;//If two dates are equal
            }
            else
            {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }
}
