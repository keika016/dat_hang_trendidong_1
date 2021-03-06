package com.keika.thunghiem01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.keika.thunghiem01.model.SanPham;

public class DialogAmountActivity extends Activity implements View.OnClickListener {

    private Button btnHuy, btnDongY;
    private TextView tvMaSP, tvTenSP, tvGiaSP, tvSoLuongTon, tvMax, tvSLTitle;
    private EditText edtSoLuong;
    private SanPham sanPham;
    private int soLuongSp;
    private int max;

    private static final String COMMAND_SANPHAM = "sanpham";
    private static final String COMMAND_SOLUONG = "soluong";
    private static final String COMMAND_SOLUONGTON = "soluongton";
    private static final String COMMAND_SOLUONGSP = "soluongsp";
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_amount);

        initView();
        setTextTextView();
    }

    private void initView() {
        this.setFinishOnTouchOutside(false);
        tvMaSP = (TextView) findViewById(R.id.activity_dialog_amount_tvMaSP);
        tvTenSP = (TextView) findViewById(R.id.activity_dialog_amount_tvTenSP);
        tvGiaSP = (TextView) findViewById(R.id.activity_dialog_amount_tvGiaSP);
        tvSoLuongTon = (TextView) findViewById(R.id.activity_dialog_amount_tvSoLuongTon);
        tvSLTitle = (TextView) findViewById(R.id.activity_dialog_amount_tvSLTitle);
        tvMax = (TextView) findViewById(R.id.activity_dialog_amount_Max);
        edtSoLuong = (EditText) findViewById(R.id.activity_dialog_amount_edtSoLuong);
        btnHuy = (Button) findViewById(R.id.activity_dialog_amount_buttonHuy);
        btnDongY = (Button) findViewById(R.id.activity_dialog_amount_buttonDongY);
        sanPham = new SanPham();
        soLuongSp = -1;
        max = -1;
        btnHuy.setOnClickListener(this);
        btnDongY.setOnClickListener(this);
    }

    private void setTextTextView() {
        Bundle b = getIntent().getExtras();
        if (b.get(COMMAND_SANPHAM) != null) {
            sanPham = (SanPham) b.get(COMMAND_SANPHAM);
            tvMaSP.setText(sanPham.getIdSP() + "");
            tvTenSP.setText(sanPham.getTenSP() + "");
            tvGiaSP.setText(sanPham.getGiaSP() + "");
            tvSoLuongTon.setText(sanPham.getSoLuongTon() + "");
        }
        if (b.get(COMMAND_SOLUONGTON) != null) {
            int soluong = (tinhSoLuongKhiCoThanhTien() - (Integer) b.get(COMMAND_SOLUONGTON));
            if (b.get(COMMAND_SOLUONGSP) == null)
            edtSoLuong.setText((soluong+1) + "");
        }
        if (b.get(COMMAND_SOLUONGSP) != null) {
            soLuongSp = (int) b.get(COMMAND_SOLUONGSP);
            Log.e("Dialog Amout: ", soLuongSp + " " + sanPham.getSoLuongTon() + " " + (Integer) b.get(COMMAND_SOLUONGTON));
            int soluong = (tinhSoLuongKhiCoThanhTien() - (Integer) b.get(COMMAND_SOLUONGTON));
            edtSoLuong.setText(soluong + "");
            tvSLTitle.setText("Số lượng đã lấy");
            if ((int) b.get(COMMAND_SOLUONGSP) != 0) {
                //đã có trong db
                this.max = sanPham.getSoLuongTon() + (int) b.get(COMMAND_SOLUONGSP);
                tvMax.setText("Max: " + max);

            } else {
                //chưa có trong db
                max = sanPham.getSoLuongTon();
                tvSoLuongTon.setText(sanPham.getSoLuongTon() + "");
                tvMax.setText("Max: " + max);
            }
        }
    }

    private int tinhSoLuongKhiCoThanhTien() {
        return sanPham.getSoLuongTon() + soLuongSp;
    }

    public static boolean checkLaSoNguyenDuong(String str) {
        return str.matches("[+]?\\d+");
    }

    private void clickDongY(int soluongTonNew, int soLuong) {
        sanPham.setSoLuongTon(soluongTonNew);
        Intent i = new Intent();
        i.putExtra(COMMAND_SANPHAM, sanPham);
        i.putExtra(COMMAND_SOLUONG, soLuong);
        setResult(REQUEST_CODE, i);
        super.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_dialog_amount_buttonHuy:
                finish();
                break;
            case R.id.activity_dialog_amount_buttonDongY:
                String s = edtSoLuong.getText().toString().trim();
                if (s.compareTo("") != 0) {
                    if (checkLaSoNguyenDuong(s) == true) {
                        if (soLuongSp == -1) {
                            if (Integer.parseInt(s) <= sanPham.getSoLuongTon()) {
                                int soLuong = Integer.parseInt(s);
                                int soluongTonNew = 0;
                                soluongTonNew = sanPham.getSoLuongTon() - soLuong;
                                clickDongY(soluongTonNew, soLuong);
                            } else {
                                Toast.makeText(this, "Thêm mới: Số lượng phải nhỏ hơn hoặc bằng Số lượng tồn", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            int soLuong = Integer.parseInt(s);
                            Bundle b = getIntent().getExtras();
                            if (soLuong <= max) {
                                int soluongTonNew = 0;
                                SanPham sp = (SanPham) b.get(COMMAND_SANPHAM);
                                soluongTonNew = sp.getSoLuongTon() + (soLuongSp - soLuong);
                                clickDongY(soluongTonNew, soLuong);
                                Log.e("Dieu chinh: ", "Điều Chỉnh:" + soluongTonNew + " - SLT_DB: " + sp.getSoLuongTon() + " - SLT_Cu" + soLuongSp + " - SL:" + soLuong);
                                Toast.makeText(this, "Điều Chỉnh:" + soluongTonNew + " - SLT_DB: " + sp.getSoLuongTon() + " - SLT_Cu" + soLuongSp + " - SL:" + soLuong, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Điều Chỉnh: Số lượng phải nhỏ hơn hoặc bằng Max", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(this, "Số lượng là số nguyên dương", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Phải nhập số lượng", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
