package com.keika.thunghiem01.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keika.thunghiem01.R;
import com.keika.thunghiem01.model.DonDatHang;
import com.keika.thunghiem01.model.SanPham;

import java.util.ArrayList;

/**
 * Created by Son-Auto on 4/7/2018.
 */

public class AdapterLV_OrderOrEdit extends BaseAdapter {
    private Activity activity;
    private ArrayList<SanPham> listSanPham;
    private ArrayList<String> listThanhTien;

    public AdapterLV_OrderOrEdit(Activity activity, ArrayList<SanPham> listSanPham, ArrayList<String> listThanhTien) {
        this.activity = activity;
        this.listSanPham = listSanPham;
        this.listThanhTien = listThanhTien;
    }

    @Override
    public int getCount() {
        return listSanPham.size();
    }

    @Override
    public Object getItem(int position) {
        return listSanPham.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView ==null){
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_lv_order_or_edit,null);
            viewHolder = new ViewHolder();
            viewHolder.tv_TenSanPham = (TextView)convertView.findViewById(R.id.item_lv_activity_order_or_edit_item_lv_SanPham);
            viewHolder.tv_ThanhTien = (TextView)convertView.findViewById(R.id.item_lv_activity_order_or_edit_item_lv_ThanhTien);
            viewHolder.layout_item = (LinearLayout)convertView.findViewById(R.id.item_lv_activity_main_layout);

            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder)convertView.getTag();

        viewHolder.tv_TenSanPham.setText(listSanPham.get(position).getTenSP().toString()+"");
        viewHolder.tv_ThanhTien.setText(listThanhTien.get(position).toString()+"");
       /* viewHolder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Da nhan ok ok ok", Toast.LENGTH_SHORT).show();
            }
        });*/

        return convertView;
    }
    public static class ViewHolder{
        private TextView tv_TenSanPham;
        private TextView tv_ThanhTien;
        private LinearLayout layout_item;
    }
}
