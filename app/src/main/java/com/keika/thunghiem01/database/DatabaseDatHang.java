package com.keika.thunghiem01.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

   /* public void themDonDatHang(DonDatHang ddh, ArrayList<ChiTietDonDH> listChiTietDDH, ArrayList<Integer> listSoLuongTon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PERSON_ID, p.getId());
        values.put(PERSON_NAME, p.getName());

        //phải để true = 1, false = 0, thì ct mới hiểu đúng
        //Male: true - 1, Female: false - 0
        if (p.isGioitinh())
            values.put(PERSON_GIOITINH, 1);
        else
            values.put(PERSON_GIOITINH, 0);

        values.put(PERSON_SALARY, p.getSalary());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }*/

}
