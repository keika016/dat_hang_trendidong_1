package com.keika.thunghiem01.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.keika.thunghiem01.R;
import com.keika.thunghiem01.model.DonDatHang;

import java.util.ArrayList;

/**
 * Created by Son-Auto on 4/6/2018.
 */

public class AdapterLV_MainActivity extends BaseAdapter {
    private Activity activity;
    private ArrayList<DonDatHang> listDonDatHang;
    private ArrayList<String> listTenCuaHang;

    public AdapterLV_MainActivity(Activity activity, ArrayList<DonDatHang> listDonDatHang, ArrayList<String> listTenCuaHnag) {
        this.activity = activity;
        this.listDonDatHang = listDonDatHang;
        this.listTenCuaHang = listTenCuaHnag;
    }

    @Override
    public int getCount() {
        return listDonDatHang.size();
    }

    @Override
    public Object getItem(int position) {
        return listDonDatHang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView ==null){
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_lv_activity_main,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_TenCuaHang = (TextView)convertView.findViewById(R.id.item_lv_activity_main_cuaHang);
            viewHolder.tv_idDonDatHang = (TextView)convertView.findViewById(R.id.item_lv_activity_main_idDDH);
            viewHolder.tv_giaTriDDH = (TextView)convertView.findViewById(R.id.item_lv_activity_main_giaTriDDH);
            viewHolder.layout_item = (LinearLayout)convertView.findViewById(R.id.item_lv_activity_main_layout);

            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder)convertView.getTag();

        viewHolder.tv_TenCuaHang.setText(listTenCuaHang.get(position).toString()+"");
        viewHolder.tv_idDonDatHang.setText(listDonDatHang.get(position).getIdDDH()+"");
        viewHolder.tv_giaTriDDH.setText(listDonDatHang.get(position).getGiaTriDDH()+"");
       /* viewHolder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Da nhan ok ok ok", Toast.LENGTH_SHORT).show();
            }
        });*/

        return convertView;
    }
    public static class ViewHolder{
        private TextView tv_TenCuaHang;
        private TextView tv_idDonDatHang;
        private TextView tv_giaTriDDH;
        private LinearLayout layout_item;
    }
}
