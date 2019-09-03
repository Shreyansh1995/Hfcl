package com.netcomm.hfcl.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.netcomm.hfcl.R;
import com.netcomm.hfcl.model.AttendanceModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Netcomm on 1/31/2017.
 */
public class AttandanceAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<AttendanceModel> data;

    public AttandanceAdapter(Context mContext, ArrayList<AttendanceModel> data) {
        this.mContext = mContext;
        this.data = data;
        Log.e("@@@@@@@@*****", "" + data.size());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public AttendanceModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AttandanceAdapter.ViewHolder holder = null;
        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new AttandanceAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.attandance_detail_item, null);
            holder.tv_date =  (TextView) convertView.findViewById(R.id.txt_date);
            holder.tv_intime = (TextView) convertView.findViewById(R.id.tv_intime);
            holder.tv_outtime = (TextView) convertView.findViewById(R.id.tv_outtime);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            convertView.setTag(holder);
        } else {
            holder = (AttandanceAdapter.ViewHolder) convertView.getTag();
        }
        String status = data.get(position).getStatus();

        String color = "#808080";
        if (data.get(position).getColor().matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")){
            color = data.get(position).getColor();
        }

        //holder.tv_date.setText(getDate(data.get(position).getDateOffice2()));
        holder.tv_date.setText(data.get(position).getDateOffice());
        /*holder.tv_intime.setText(splitDateTime(data.get(position).getInTime()));
        holder.tv_outtime.setText(splitDateTime(data.get(position).getOutTime()));*/
        holder.tv_intime.setText(data.get(position).getInTime());
        holder.tv_outtime.setText(data.get(position).getOutTime());
        holder.tv_status.setText(status);

        if (color.equals("#FFFFFF")){
            holder.tv_date.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
            holder.tv_intime.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
            holder.tv_outtime.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
            holder.tv_status.setTextColor(ContextCompat.getColor(mContext,R.color.grey));
        }
        else {
            holder.tv_date.setTextColor(Color.parseColor(color));
            holder.tv_intime.setTextColor(Color.parseColor(color));
            holder.tv_outtime.setTextColor(Color.parseColor(color));
            holder.tv_status.setTextColor(Color.parseColor(color));
        }

        return convertView;
    }

    class ViewHolder {
        TextView tv_date;
        TextView tv_intime;
        TextView tv_outtime;
        TextView tv_status;
    }

    /*private String splitDateTime(String dateString){
        String[] parts = dateString.split(" ");
        String time = dateString.replace(parts[0] + " ", "");
        return time;
    }*/

    private String splitDateTime(String dateString){
        String[] parts = dateString.split(" ");
        if (parts.length > 0){
            String time = dateString.replace(parts[0] + " ", "");
            String[] time_split = time.split(":");
           /* if (c.length >= 2){
                String get_second = time_split[2].split(" ")[0];
                return time.replace(":" + get_second, "");
            }*/

            try {
                SimpleDateFormat sdf=new SimpleDateFormat("hh:mm:ss a",Locale.getDefault());
                SimpleDateFormat sdf2=new SimpleDateFormat("hh:mm a",Locale.getDefault());
                Date newDate = sdf.parse(time);
                String date2 = sdf2.format(newDate);
                return date2;
            } catch (ParseException e) {
                e.printStackTrace();
            }



        }
        return "NA";
    }

    private String getDate(String dateStr) {
        String date;
        String[] parts = dateStr.split("/");
        date = parts[1];
        return date;
    }
}