package com.keika.thunghiem01.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.keika.thunghiem01.model.ChiTietDonDH;
import com.keika.thunghiem01.model.CuaHang;
import com.keika.thunghiem01.model.DonDatHang;
import com.keika.thunghiem01.model.SanPham;

import java.util.ArrayList;

/**
 * Created by Son-Auto on 4/6/2018.
 */

public class DatabaseDatHang extends SQLiteOpenHelper {
    public static final String DBNAME = "thunghiem_03.db";
    public static final String DBLOCATION = "/data/data/com.keika.thunghiem01/databases/";

    //Table
    public static final String CUAHANG_TB = "CuaHang";
    public static final String SANPHAM_TB = "SanPham";
    public static final String DONDATHANG_TB = "DonDatHang";
    public static final String CHITIETDONDH_TB = "ChiTietDonDH";

    //Thuoc tinh table
    public static final String CUAHANG_ATTRI_IDCH = "idCH";
    public static final String CUAHANG_ATTRI_TENCH = "tenCH";
    public static final String CUAHANG_ATTRI_DIACHICH = "diaChi";
    public static final String CUAHANG_ATTRI_NGUOILHCH = "nguoiLH";
    public static final String CUAHANG_ATTRI_SODTCH = "soDT";

    public static final String SANPHAM_ATTRI_IDSP = "idSP";
    public static final String SANPHAM_ATTRI_TENSP = "tenSP";
    public static final String SANPHAM_ATTRI_GIASP = "giaSP";
    public static final String SANPHAM_ATTRI_SOLUONGTON = "soLuongTon";

    public static final String DONDATHANG_ATTRI_IDDDH = "idDDH";
    public static final String DONDATHANG_ATTRI_GIATRIDDH = "giaTriDDH";
    public static final String DONDATHANG_ATTRI_NGAYVIENG = "ngayVieng";
    public static final String DONDATHANG_ATTRI_IDCUAHANG = "idCH";

    public static final String CHITIETDONDH_ATTRI_IDDDH = "idDDH";
    public static final String CHITIETDONDH_ATTRI_IDSANPHAM = "idSP";
    public static final String CHITIETDONDH_ATTRI_SOLUONG = "soLuong";


    private Context context;
    private SQLiteDatabase mDatabase;

    public DatabaseDatHang(Context context) {
        super(context, DBNAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = context.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public ArrayList<DonDatHang> getListDonDatHang() {
        DonDatHang dDH = null;
        ArrayList<DonDatHang> listDonDH = new ArrayList<DonDatHang>();
        openDatabase();
        String query = "SELECT ddh.idCH, ddh.idDDH, ddh.giaTriDDH FROM DonDatHang AS ddh;";
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dDH = new DonDatHang(cursor.getInt(0), cursor.getInt(1), cursor.getString(2));
            listDonDH.add(dDH);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return listDonDH;
    }

    public ArrayList<String> getListTenCuaHangByDonDH() {
        String s = "";
        ArrayList<String> listTenCuaHang = new ArrayList<String>();
        openDatabase();
        String query = "SELECT ch.tenCH FROM DonDatHang AS ddh, CuaHang AS ch\n" +
                "WHERE ddh.idCH = ch.idCH;";
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            s = cursor.getString(0);
            listTenCuaHang.add(s);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return listTenCuaHang;
    }

    public ArrayList<SanPham> getListSanPhamConHang() {
        SanPham sp = null;
        ArrayList<SanPham> listSP = new ArrayList<SanPham>();
        openDatabase();
        String query = "select * from SanPham as sp\n" +
                "where sp.soLuongTon >=1;";
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            sp = new SanPham(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            listSP.add(sp);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return listSP;
    }

    public CuaHang checkTenCuaHangExist(String tenCH, String diaChiCH) {
        String query = "SELECT * FROM CuaHang AS ch\n" +
                "WHERE ch.tenCH = '" + tenCH + "' AND ch.diaChi = '" + diaChiCH + "';";
        CuaHang ch = null;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.isBeforeFirst()) {
            //cửa hàng không tồn tại
        } else {
            ch = new CuaHang(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        }
        cursor.close();
        closeDatabase();
        return ch;
    }

    public CuaHang checkCuaHangExist2(int idCH) {
        String query = "SELECT * FROM CuaHang AS ch WHERE ch.idCH = " + idCH + ";";
        CuaHang ch = null;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ch = new CuaHang(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return ch;
    }

    public DonDatHang getDonHang(int idDDH) {
        String query = "SELECT * FROM DonDatHang AS ddh\n" +
                "WHERE ddh.idDDH = " + idDDH + ";";
        DonDatHang dDH = null;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            dDH = new DonDatHang(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return dDH;
    }

    public boolean checkIdCuaHangExist(String s) {
        String query = "SELECT ddh.idDDH FROM DonDatHang AS ddh\n" +
                "WHERE ddh.idDDH = '" + s + "';";
        boolean b = false;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.isBeforeFirst()) {
            //đơn hàng không tồn tại
        } else {
            b = true;
        }
        cursor.close();
        closeDatabase();
        return b;
    }

    public SanPham getSanPham(int idSP) {
        String query = "select * from SanPham as sp\n" +
                "where sp.idSP=" + idSP + ";";
        SanPham sp = null;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        if (cursor.isBeforeFirst()) {
            //đơn hàng không tồn tại
        } else {
            sp = new SanPham(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
        }
        cursor.close();
        closeDatabase();
        return sp;
    }

    public ArrayList<ChiTietDonDH> getListChiTietDDHByIdDDH(int idDonDatHang) {
        String query = "SELECT * FROM ChiTietDonDH\n" +
                "where idDDH =" + idDonDatHang + ";";
        ChiTietDonDH ct = null;
        ArrayList<ChiTietDonDH> listCTDDH = new ArrayList<ChiTietDonDH>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ct = new ChiTietDonDH(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));
            listCTDDH.add(ct);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return listCTDDH;
    }

    public ChiTietDonDH getChiTietDDHByIdDDH(int idDonDatHang, int idSP) {
        String query = "SELECT * FROM ChiTietDonDH\n" +
                "where idDDH =" + idDonDatHang + " and idSP =" + idSP + ";";
        ChiTietDonDH ct = null;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ct = new ChiTietDonDH(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2));
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return ct;
    }

    public void themDonDatHang(DonDatHang ddh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DONDATHANG_ATTRI_IDDDH, ddh.getIdDDH());
        values.put(DONDATHANG_ATTRI_GIATRIDDH, ddh.getGiaTriDDH());
        values.put(DONDATHANG_ATTRI_NGAYVIENG, ddh.getNgayVieng());
        values.put(DONDATHANG_ATTRI_IDCUAHANG, ddh.getIdCH());
        db.insert(DONDATHANG_TB, null, values);
        db.close();
    }

    public void themChiTietDDH(ArrayList<ChiTietDonDH> listChiTietDDH) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ChiTietDonDH item : listChiTietDDH) {
            ContentValues values = new ContentValues();
            values.put(CHITIETDONDH_ATTRI_IDDDH, item.getIdDDH());
            values.put(CHITIETDONDH_ATTRI_IDSANPHAM, item.getIdSP());
            values.put(CHITIETDONDH_ATTRI_SOLUONG, item.getSoLuong());
            db.insert(CHITIETDONDH_TB, null, values);
        }
        db.close();
    }

    public void themChiTietDDH_withCheckExist(ArrayList<ChiTietDonDH> listChiTietDDH) {
        ArrayList<ChiTietDonDH> listCTInsert = new ArrayList<ChiTietDonDH>();
        for (ChiTietDonDH item : listChiTietDDH) {
            String query = "select * from ChiTietDonDH where  idDDH=" + item.getIdDDH() + " and idSP=" + item.getIdSP() + ";";
            openDatabase();
            Cursor cursor = mDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.isBeforeFirst()) {
                //đơn hàng không tồn tại
                listCTInsert.add(item);
            } else {
            }
            cursor.close();
            closeDatabase();
        }
        themChiTietDDH(listCTInsert);
    }

    public void updateSanPham(ArrayList<SanPham> listSpUpdate) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (SanPham item : listSpUpdate) {
            ContentValues values = new ContentValues();
            values.put(SANPHAM_ATTRI_SOLUONGTON, item.getSoLuongTon());
            //update thành công sẽ trả về giá trị là tổng số dòng của table mà update được
            int i = db.update(SANPHAM_TB, values, SANPHAM_ATTRI_IDSP + "=?", new String[]{item.getIdSP() + ""});
        }
        db.close();
    }

    public void updateDonDatHang(DonDatHang ddh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DONDATHANG_ATTRI_GIATRIDDH, ddh.getGiaTriDDH());
        values.put(DONDATHANG_ATTRI_IDCUAHANG, ddh.getIdCH());
        //update thành công sẽ trả về giá trị là tổng số dòng của table mà update được
        int i = db.update(DONDATHANG_TB, values, DONDATHANG_ATTRI_IDDDH + "=?", new String[]{ddh.getIdDDH() + ""});
        db.close();
    }

    public void updateChitiet(ArrayList<ChiTietDonDH> listCt) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ChiTietDonDH item : listCt) {
            ContentValues values = new ContentValues();
            values.put(CHITIETDONDH_ATTRI_SOLUONG, item.getSoLuong());
            //update thành công sẽ trả về giá trị là tổng số dòng của table mà update được
            int i = db.update(CHITIETDONDH_TB, values, CHITIETDONDH_ATTRI_IDDDH + "=? and " + CHITIETDONDH_ATTRI_IDSANPHAM + "=?", new String[]{item.getIdDDH() + "", item.getIdSP() + ""});
        }
        db.close();
    }


    public void xoaDonHang(DonDatHang ddh) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DONDATHANG_TB, DONDATHANG_ATTRI_IDDDH + " =? ", new String[]{ddh.getIdDDH() + ""});
        db.close();
    }

    public void xoaChiTietDDH(ChiTietDonDH ct) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CHITIETDONDH_TB, CHITIETDONDH_ATTRI_IDDDH + " =? ", new String[]{ct.getIdDDH() + ""});
        db.close();
    }

    /*public void xoaChiTietDDHbyIdSP(ChiTietDonDH ct) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CHITIETDONDH_TB, CHITIETDONDH_ATTRI_IDDDH + " =? and " + CHITIETDONDH_ATTRI_IDSANPHAM + "=?", new String[]{ct.getIdDDH() + "", ct.getIdSP() + ""});
        db.close();
    }*/

}
