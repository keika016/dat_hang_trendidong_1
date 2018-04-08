package com.keika.thunghiem01;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.keika.thunghiem01.adapter.AdapterLV_MainActivity;
import com.keika.thunghiem01.database.DatabaseDatHang;
import com.keika.thunghiem01.model.ChiTietDonDH;
import com.keika.thunghiem01.model.CuaHang;
import com.keika.thunghiem01.model.DonDatHang;
import com.keika.thunghiem01.model.SanPham;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button btnThemmoi, btnXoa, btnDieuChinh;
    private ListView listViewDSDDH;
    private ArrayList<DonDatHang> listDonDatHang;
    private ArrayList<String> listTenCuaHang;
    private AdapterLV_MainActivity adapterLV_mainActivity;
    private DatabaseDatHang dbTuAssets;
    private int getItemSelected;
    private static final String COMMAND = "command";
    private static final String COMMAND_THEMMOI = "themmoi";
    private static final String COMMAND_DIEUCHINH = "dieuchinh";
    private static final String COMMAND_DIEUCHINH_DONDH = "dieuchinh_dondathang";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDatabase();
        listViewDSDDH.setOnItemClickListener(this);
    }

    private void initView() {
        btnThemmoi = (Button) findViewById(R.id.activity_main_buttonaddnew);
        btnXoa = (Button) findViewById(R.id.activity_main_buttonXoa);
        btnDieuChinh = (Button)findViewById(R.id.activity_main_buttonDieuChinh);
        listViewDSDDH = (ListView) findViewById(R.id.activity_main_listview);
        listDonDatHang = new ArrayList<DonDatHang>();
        listTenCuaHang = new ArrayList<String>();
        adapterLV_mainActivity = new AdapterLV_MainActivity(MainActivity.this, listDonDatHang, listTenCuaHang);
        listViewDSDDH.setAdapter(adapterLV_mainActivity);
        btnThemmoi.setOnClickListener(this);
        btnXoa.setOnClickListener(this);
        btnDieuChinh.setOnClickListener(this);
        listViewDSDDH.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewDSDDH.setSelector(R.color.colorAccent);
        getItemSelected = -1;
    }

    private void initDatabase() {
        readDatabase();
        reLoadDatabase();
    }

    private void reLoadDatabase() {
        listDonDatHang.clear();
        listTenCuaHang.clear();
        listDonDatHang.addAll(dbTuAssets.getListDonDatHang());
        listTenCuaHang.addAll(dbTuAssets.getListTenCuaHangByDonDH());
        adapterLV_mainActivity.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_buttonaddnew:
                Intent i = new Intent(MainActivity.this, OrderOrEditActivity.class);
                i.putExtra(COMMAND, COMMAND_THEMMOI);
                startActivity(i);
                finish();
                break;
            case R.id.activity_main_buttonXoa:
                if (getItemSelected != -1) {
                    DonDatHang ddh = listDonDatHang.get(getItemSelected);
                    ArrayList<ChiTietDonDH> listCTDDH = dbTuAssets.getListChiTietDDHByIdDDH(ddh.getIdDDH());
                    ArrayList<SanPham> listSP = new ArrayList<SanPham>();
                    for (ChiTietDonDH item : listCTDDH) {
                        SanPham sp = dbTuAssets.getSanPham(item.getIdSP());
                        int soLuongTonNew = sp.getSoLuongTon() + item.getSoLuong();
                        sp.setSoLuongTon(soLuongTonNew);
                        listSP.add(sp);
                        dbTuAssets.xoaChiTietDDH(item);
                    }
                    dbTuAssets.xoaDonHang(ddh);
                    dbTuAssets.updateSanPham(listSP);
                    reLoadDatabase();
                    getItemSelected = -1;
                    Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Phải chọn 1 Đơn Hàng nào đó", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.activity_main_buttonDieuChinh:
                if (getItemSelected != -1) {
                    DonDatHang ddh = dbTuAssets.getDonHang(listDonDatHang.get(getItemSelected).getIdDDH());
                    i = new Intent(MainActivity.this, OrderOrEditActivity.class);
                    i.putExtra(COMMAND, COMMAND_DIEUCHINH);
                    i.putExtra(COMMAND_DIEUCHINH_DONDH, ddh);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(this, "Phải chọn 1 Đơn Hàng nào đó", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void readDatabase() {
        dbTuAssets = new DatabaseDatHang(MainActivity.this);
        //Check exist database
        File database = getApplicationContext().getDatabasePath(dbTuAssets.DBNAME);
        if (false == database.exists()) {
            dbTuAssets.getReadableDatabase();
            //Copy db
            if (copyDatabase(MainActivity.this)) {
                Toast.makeText(this, "Copy dabase success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(dbTuAssets.DBNAME);
            String outFileName = dbTuAssets.DBLOCATION + dbTuAssets.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[] buff = new byte[1024];
            int lenght = 0;
            while ((lenght = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, lenght);
            }
            outputStream.flush();
            outputStream.close();
            Log.e("Main Activity Thong bao", "Da copy database");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.activity_main_listview:
                Toast.makeText(MainActivity.this, "Đã click " + position, Toast.LENGTH_SHORT).show();
                getItemSelected = position;
                break;
        }
    }

}
