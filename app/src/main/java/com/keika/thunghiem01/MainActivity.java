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
import com.keika.thunghiem01.model.CuaHang;
import com.keika.thunghiem01.model.DonDatHang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnThemmoi;
    private ListView listViewDSDDH;
    private ArrayList<DonDatHang> listDonDatHang;
    private ArrayList<String> listTenCuaHang;
    private AdapterLV_MainActivity adapterLV_mainActivity;
    private DatabaseDatHang dbTuAssets;

    private static final String COMMAND = "command";
    private static final String COMMAND_THEMMOI = "themmoi";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDatabase();

        listViewDSDDH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Đã click " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        btnThemmoi = (Button) findViewById(R.id.activity_main_buttonaddnew);
        listViewDSDDH = (ListView) findViewById(R.id.activity_main_listview);
        listDonDatHang = new ArrayList<DonDatHang>();
        listTenCuaHang = new ArrayList<String>();
        adapterLV_mainActivity = new AdapterLV_MainActivity(MainActivity.this, listDonDatHang, listTenCuaHang);
        listViewDSDDH.setAdapter(adapterLV_mainActivity);
        btnThemmoi.setOnClickListener(this);
        listViewDSDDH.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewDSDDH.setSelector(R.color.colorAccent);
    }

    private void initDatabase() {
        readDatabase();
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
}
