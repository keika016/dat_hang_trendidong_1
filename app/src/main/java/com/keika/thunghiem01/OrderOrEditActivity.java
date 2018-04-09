package com.keika.thunghiem01;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.keika.thunghiem01.adapter.AdapterLV_OrderOrEdit;
import com.keika.thunghiem01.database.DatabaseDatHang;
import com.keika.thunghiem01.model.ChiTietDonDH;
import com.keika.thunghiem01.model.CuaHang;
import com.keika.thunghiem01.model.DonDatHang;
import com.keika.thunghiem01.model.SanPham;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class OrderOrEditActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Button btnBack, btnCheck, btnLuu, btnRedo;
    private EditText edtTenCuaHang, edtDiaChi, edtNguoiLienHe, edtSoDT, edtNgayVieng, edtSoDH;
    private TextView tvTongGiaTriHD;
    private ImageView imageView;

    private DatabaseDatHang dbTuAssets;
    private String trangThai;
    private int getItemPosition;
    private ListView listViewOrderOrEdit;
    private AdapterLV_OrderOrEdit adapterLV_orderOrEdit;

    private ArrayList<SanPham> listSanPham;
    private ArrayList<String> listThanhTien;
    private DonDatHang mDonDatHang;
    private ArrayList<ChiTietDonDH> listChiTietDonDH;
    //Từ Main qua
    private static final String COMMAND = "command";
    private static final String COMMAND_THEMMOI = "themmoi";
    private static final String COMMAND_DIEUCHINH = "dieuchinh";
    private static final String COMMAND_DIEUCHINH_DONDH = "dieuchinh_dondathang";
    //Chuyển qua cho Dialog Amount
    private static final String COMMAND_SANPHAM = "sanpham";
    private static final String COMMAND_SOLUONG = "soluong";
    private static final String COMMAND_SOLUONGTON = "soluongton";
    private static final String COMMAND_SOLUONGSP = "soluongsp";
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_or_edit);

        initView();
        setTrangThai();
        forButtonLuu();
        forDateAndTime();
        readDatabase();
        themMoi_LoadListSanPham();
        dieuChinh_LoadInfoCuaHang();
        Log.e("OrderOrEdit", "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("OrderOrEdit", "onResume");
        if (listViewOrderOrEdit.isEnabled() == false)
            listViewOrderOrEdit.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("OrderOrEdit", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("OrderOrEdit", "onStop");
    }

    private void initView() {
        edtTenCuaHang = (EditText) findViewById(R.id.activity_order_or_edit_tencuahang);
        edtDiaChi = (EditText) findViewById(R.id.activity_order_or_edit_diachi);
        edtNguoiLienHe = (EditText) findViewById(R.id.activity_order_or_edit_nguoilienhe);
        edtSoDT = (EditText) findViewById(R.id.activity_order_or_edit_sodienthoai);
        edtNgayVieng = (EditText) findViewById(R.id.activity_order_or_edit_ngayvieng);
        edtSoDH = (EditText) findViewById(R.id.activity_order_or_edit_sodonhang);
        tvTongGiaTriHD = (TextView) findViewById(R.id.activity_order_or_edit_tvTonggGiaTriHD);
        btnBack = (Button) findViewById(R.id.activity_order_or_edit_buttonBack);
        btnCheck = (Button) findViewById(R.id.activity_order_or_edit_buttoncheck);
        btnLuu = (Button) findViewById(R.id.activity_order_or_edit_buttonsave);
        btnRedo = (Button) findViewById(R.id.activity_order_or_edit_buttonredo);
        trangThai = "";

        listSanPham = new ArrayList<SanPham>();
        listThanhTien = new ArrayList<String>();
        listChiTietDonDH = new ArrayList<ChiTietDonDH>();
        mDonDatHang = new DonDatHang();

        listViewOrderOrEdit = (ListView) findViewById(R.id.activity_order_or_edit_listview);
        imageView = (ImageView) findViewById(R.id.activity_order_or_edit_imageListView);
        adapterLV_orderOrEdit = new AdapterLV_OrderOrEdit(OrderOrEditActivity.this, listSanPham, listThanhTien);
        listViewOrderOrEdit.setAdapter(adapterLV_orderOrEdit);
        listViewOrderOrEdit.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewOrderOrEdit.setSelector(R.color.colorAccent);

        btnBack.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        btnLuu.setOnClickListener(this);
        btnRedo.setOnClickListener(this);
        listViewOrderOrEdit.setOnItemClickListener(this);
        listViewOrderOrEdit.setVisibility(View.INVISIBLE);
    }

    private String getCurrentDateAndTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }

    private boolean checkEditTextNotEmpty() {
        if (edtTenCuaHang.getText().toString().trim().compareTo("") == 0
                || edtSoDH.getText().toString().trim().compareTo("") == 0
                || edtDiaChi.getText().toString().trim().compareTo("") == 0)
            return false;
        return true;
    }

    public static boolean checkLaSoNguyenDuong(String str) {
        return str.matches("[+]?\\d+");
    }

    private void setTrangThai() {
        Bundle b = getIntent().getExtras();
        if (b.get(COMMAND).toString().compareTo(COMMAND_THEMMOI) == 0) {
            trangThai = COMMAND_THEMMOI;
        } else {
            trangThai = COMMAND_DIEUCHINH;
        }
    }

    private void dieuChinh_LoadInfoCuaHang() {
        if (trangThai.compareTo(COMMAND_DIEUCHINH) == 0) {
            Bundle b = getIntent().getExtras();
            mDonDatHang = (DonDatHang) b.get(COMMAND_DIEUCHINH_DONDH);
            CuaHang a = dbTuAssets.checkCuaHangExist2(mDonDatHang.getIdCH());
            Log.e("Order or Edit", "" + mDonDatHang);
            edtTenCuaHang.setText(a.getTenCH() + "");
            edtDiaChi.setText(a.getDiaChi() + "");
            edtNguoiLienHe.setText(a.getNguoiLH() + "");
            edtSoDT.setText(a.getSoDT() + "");
            edtSoDH.setText(mDonDatHang.getIdDDH() + "");
            edtNgayVieng.setText(mDonDatHang.getNgayVieng() + "");
            setEditTextUnTouch(edtSoDH);
            setEditTextUnTouch(edtTenCuaHang);
            setEditTextUnTouch(edtDiaChi);
            dieuChinh_LoadListSanPham();
            setButtonEnable(btnRedo);
            setButtonDisable(btnCheck);
        }
    }

    private void dieuChinh_LoadListSanPham() {
        if (trangThai.compareTo(COMMAND_DIEUCHINH) == 0) {
            listSanPham.clear();
            listChiTietDonDH.clear();
            listThanhTien.clear();
            listSanPham.addAll(dbTuAssets.getListSanPham());
            listChiTietDonDH.addAll(dbTuAssets.getListChiTietDDHByIdDDH(mDonDatHang.getIdDDH()));
            Log.e("OrderOrEdit", "onStop" + listChiTietDonDH.toString());

            ArrayList<ChiTietDonDH> listCTDemo = listChiTietDonDH;
            boolean flag = false;
            for (SanPham sp : listSanPham) {
                for (ChiTietDonDH ct : listCTDemo) {
                    if (ct.getIdSP() == sp.getIdSP()) {
                        long thanhtien = ct.getSoLuong() * Long.parseLong(sp.getGiaSP());
                        listThanhTien.add(thanhtien + "");
                        flag = true;
                        listCTDemo.remove(ct);
                        break;
                    }
                }
                if (flag == true) {
                    flag = false;
                    continue;
                } else {
                    listThanhTien.add("0");
                }
            }
            adapterLV_orderOrEdit.notifyDataSetChanged();
            tvTongGiaTriHD.setText(mDonDatHang.getGiaTriDDH() + "");
            imageView.setVisibility(View.INVISIBLE);
            listViewOrderOrEdit.setVisibility(View.VISIBLE);
        }
    }

    private void forButtonLuu() {
        setButtonLuuDisable();
    }

    private void setButtonLuuEnable() {
        btnLuu.setBackgroundResource(R.drawable.shape_2);
        btnLuu.setEnabled(true);
    }

    private void setButtonLuuDisable() {
        btnLuu.setBackgroundResource(R.drawable.shape_4);
        btnLuu.setEnabled(false);
    }

    private void forDateAndTime() {
        if (trangThai.compareTo(COMMAND_THEMMOI) == 0)
            edtNgayVieng.setText(getCurrentDateAndTime());
    }

    private void themMoi_LoadInfoCuaHang(CuaHang cuaHang) {
        setEditTextUnTouch(edtTenCuaHang);
        setEditTextUnTouch(edtDiaChi);
        if (trangThai.compareTo(COMMAND_THEMMOI) == 0)
            setEditTextUnTouch(edtSoDH);
        edtNguoiLienHe.setText(cuaHang.getNguoiLH() + "");
        edtSoDT.setText(cuaHang.getSoDT());

        setButtonDisable(btnCheck);
        setButtonEnable(btnRedo);
        imageView.setVisibility(View.INVISIBLE);
        listViewOrderOrEdit.setVisibility(View.VISIBLE);

        if (trangThai.compareTo(COMMAND_DIEUCHINH) == 0) {
            Bundle b = getIntent().getExtras();
            DonDatHang a = (DonDatHang) b.get(COMMAND_DIEUCHINH_DONDH);
            if (a.getIdCH() != cuaHang.getIdCH()) {
                mDonDatHang.setIdCH(cuaHang.getIdCH());
                setButtonLuuEnable();
            }
        }
        if (trangThai.compareTo(COMMAND_THEMMOI) == 0)
            mDonDatHang.setIdCH(cuaHang.getIdCH());


        mDonDatHang.setIdDDH(Integer.parseInt(edtSoDH.getText().toString().trim()));
    }

    private void themMoi_LoadListSanPham() {
        if (trangThai.compareTo(COMMAND_THEMMOI) == 0) {
            listSanPham.clear();
            listThanhTien.clear();
            listSanPham.addAll(dbTuAssets.getListSanPhamConHang());
            for (int i = 0; i < listSanPham.size(); i++) {
                listThanhTien.add("0");
            }
            adapterLV_orderOrEdit.notifyDataSetChanged();

            mDonDatHang.setNgayVieng(edtNgayVieng.getText().toString().trim() + "");
            mDonDatHang.setGiaTriDDH("0");
        }
    }

    private void setButtonDisable(Button b) {
        b.setEnabled(false);
        b.setVisibility(View.INVISIBLE);
    }

    private void setButtonEnable(Button b) {
        b.setEnabled(true);
        b.setVisibility(View.VISIBLE);
    }

    private void setEditTextUnTouch(EditText e) {
        e.setClickable(false);
        e.setCursorVisible(false);
        e.setFocusable(false);
        e.setFocusableInTouchMode(false);
    }

    private void setEditTextTouch(EditText e) {
        e.setClickable(true);
        e.setCursorVisible(true);
        e.setFocusable(true);
        e.setFocusableInTouchMode(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_order_or_edit_buttonBack:
                Intent i = new Intent(OrderOrEditActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.activity_order_or_edit_buttoncheck:
                if (checkEditTextNotEmpty() == true) {
                    if (trangThai.compareTo(COMMAND_THEMMOI) == 0) {
                        if (checkLaSoNguyenDuong(edtSoDH.getText().toString().trim())) {
                            CuaHang a = new CuaHang();
                            a = dbTuAssets.checkTenCuaHangExist(edtTenCuaHang.getText().toString().trim(), edtDiaChi.getText().toString().trim());
                            boolean b = dbTuAssets.checkIdCuaHangExist(edtSoDH.getText().toString().trim());
                            if (a != null && b == false) {
                                Toast.makeText(this, "Check Đúng Hết", Toast.LENGTH_SHORT).show();
                                themMoi_LoadInfoCuaHang(a);
                            } else {
                                String s = "";
                                if (a == null)
                                    s += "\n-Cửa hàng hoặc Địa Chỉ không tồn tại ";
                                if (b == true)
                                    s += "\n-Số DH này đã tồn tại";
                                Toast.makeText(this, "Sai: " + s, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Số DH phải là số nguyên dương", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        CuaHang a = new CuaHang();
                        a = dbTuAssets.checkTenCuaHangExist(edtTenCuaHang.getText().toString().trim(), edtDiaChi.getText().toString().trim());
                        if (a != null) {
                            Toast.makeText(this, "Check Đúng Hết", Toast.LENGTH_SHORT).show();
                            themMoi_LoadInfoCuaHang(a);
                        } else {
                            String s = "-Cửa hàng hoặc Địa Chỉ không tồn tại ";
                            Toast.makeText(this, "Sai: " + s, Toast.LENGTH_SHORT).show();
                        }

                    }

                } else {
                    Toast.makeText(this, "Phải nhập đủ Tên CH; Địa Chỉ CH; Số DH", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.activity_order_or_edit_buttonredo:
                setEditTextTouch(edtTenCuaHang);
                setEditTextTouch(edtDiaChi);
                if (trangThai.compareTo(COMMAND_THEMMOI) == 0)
                    setEditTextTouch(edtSoDH);
                setButtonEnable(btnCheck);
                setButtonDisable(btnRedo);
                imageView.setVisibility(View.VISIBLE);
                listViewOrderOrEdit.setVisibility(View.INVISIBLE);
                break;
            case R.id.activity_order_or_edit_buttonsave:
                if (btnCheck.isEnabled()) {
                    Toast.makeText(this, "Button Lưu: Hãy nhấn Check trước khi Lưu", Toast.LENGTH_SHORT).show();
                } else {
                    themMoi_themDonDatHang();
                    Toast.makeText(this, "Đã Lưu Vào DB", Toast.LENGTH_SHORT).show();
                    i = new Intent(OrderOrEditActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                break;
        }
    }

    private void themMoi_themDonDatHang() {
        mDonDatHang.setGiaTriDDH(tvTongGiaTriHD.getText().toString() + "");
        for (ChiTietDonDH chitiet : listChiTietDonDH) {
            chitiet.setIdDDH(Integer.parseInt(edtSoDH.getText().toString().trim()));
        }
        // (donDatHang, listChiTietDonDH, listSanPham);
        ArrayList<SanPham> listSPUpdate = new ArrayList<SanPham>();
        for (SanPham sp : listSanPham) {
            for (ChiTietDonDH ct : listChiTietDonDH) {
                if (sp.getIdSP() == ct.getIdSP()) {
                    SanPham s = new SanPham();
                    s.setSoLuongTon(sp.getSoLuongTon());
                    s.setIdSP(sp.getIdSP());
                    listSPUpdate.add(s);
                }
            }
        }
       /* Log.e("OrderOrEdit Dieu Chỉnh:", "" + listSPUpdate.toString() + "\n");
        Log.e("OrderOrEdit Dieu Chỉnh:", "" + mDonDatHang.toString() + "\n");
        Log.e("OrderOrEdit Dieu Chỉnh:", "" + listChiTietDonDH.toString() + "\n");
        Log.e("OrderOrEdit Dieu Chỉnh:", "" + listSanPham.toString() + "\n");
        Log.e("OrderOrEdit Dieu Chỉnh:", "" + listThanhTien.toString() + "\n");*/
        if (trangThai.compareTo(COMMAND_THEMMOI) == 0)
            themMoi_themDonDatHangVaoDB(mDonDatHang, listChiTietDonDH, listSPUpdate);
        else
            dieuChinh_dieuChinhDonHanhVaoDB(mDonDatHang, listChiTietDonDH, listSPUpdate);

    }

    private void themMoi_themDonDatHangVaoDB(DonDatHang ddh, ArrayList<ChiTietDonDH> listChiTiet, ArrayList<SanPham> listSanPham) {
        dbTuAssets.themDonDatHang(ddh);
        dbTuAssets.themChiTietDDH(listChiTiet);
        dbTuAssets.updateSanPham(listSanPham);
    }

    private void dieuChinh_dieuChinhDonHanhVaoDB(DonDatHang ddh, ArrayList<ChiTietDonDH> listChiTiet, ArrayList<SanPham> listSanPham) {
        dbTuAssets.updateDonDatHang(ddh);
        dbTuAssets.updateChitiet(listChiTiet);
        dbTuAssets.themChiTietDDH_withCheckExist(listChiTiet);
        dbTuAssets.updateSanPham(listSanPham);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.activity_order_or_edit_listview:
                getItemPosition = position;
                listViewOrderOrEdit.setEnabled(false);
                Intent i = new Intent(OrderOrEditActivity.this, DialogAmountActivity.class);
                i.putExtra(COMMAND_SANPHAM, dbTuAssets.getSanPham(listSanPham.get(position).getIdSP()));
                i.putExtra(COMMAND_SOLUONGTON, listSanPham.get(position).getSoLuongTon());
                if (trangThai.compareTo(COMMAND_DIEUCHINH) == 0) {
                    ChiTietDonDH ct = dbTuAssets.getChiTietDDHByIdDDH(mDonDatHang.getIdDDH(), listSanPham.get(position).getIdSP());
                    int soLuongSP = 0;
                    if (ct != null) {
                        soLuongSP = ct.getSoLuong();
                    }
                    i.putExtra(COMMAND_SOLUONGSP, soLuongSP);
                }

                startActivityForResult(i, REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(OrderOrEditActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == REQUEST_CODE && requestCode == 1) {
            SanPham sp = new SanPham();
            int soluong = 0;
            if (data.hasExtra(COMMAND_SANPHAM) && data.hasExtra(COMMAND_SOLUONG)) {
                sp = (SanPham) data.getExtras().get(COMMAND_SANPHAM);
                soluong = (Integer) data.getExtras().get(COMMAND_SOLUONG);

                if (checkEditTextNotEmpty() == true) {
                    updateDonDatHang(sp, soluong);
                    if (checkLaSoNguyenDuong(tvTongGiaTriHD.getText().toString()) == true) {
                        if (Long.parseLong(tvTongGiaTriHD.getText().toString()) != 0)
                            setButtonLuuEnable();
                        else
                            setButtonLuuDisable();
                    }
                }
            }
        }
    }

    private void updateDonDatHang(SanPham sp, int soLuong) {
        long giaTriCTDH = soLuong * Long.parseLong(sp.getGiaSP());
        ChiTietDonDH chitiet = new ChiTietDonDH(Integer.parseInt(edtSoDH.getText().toString().trim()), sp.getIdSP(), soLuong);
        for (ChiTietDonDH ct : listChiTietDonDH) {
            if (ct.getIdSP() == chitiet.getIdSP()) {
                listChiTietDonDH.remove(ct);
                break;
            }
        }
        listChiTietDonDH.add(chitiet);
        listSanPham.set(getItemPosition, sp);
        listThanhTien.set(getItemPosition, giaTriCTDH + "");
        long giaTriDDH = 0;
        for (String thanhtien : listThanhTien) {
            long tien = Long.parseLong(thanhtien.toString());
            if (tien != 0)
                giaTriDDH += tien;
        }
        mDonDatHang.setGiaTriDDH(giaTriDDH + "");
        tvTongGiaTriHD.setText(mDonDatHang.getGiaTriDDH() + "");
        adapterLV_orderOrEdit.notifyDataSetChanged();
    }

    private void readDatabase() {
        dbTuAssets = new DatabaseDatHang(OrderOrEditActivity.this);
        //Check exist database
        File database = getApplicationContext().getDatabasePath(dbTuAssets.DBNAME);
        if (false == database.exists()) {
            dbTuAssets.getReadableDatabase();
            //Copy db
            if (copyDatabase(OrderOrEditActivity.this)) {
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


}
