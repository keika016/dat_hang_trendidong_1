package com.keika.thunghiem01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OrderOrEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_or_edit);
        Button btnLuu = (Button)findViewById(R.id.activity_order_or_edit_buttonsave);
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderOrEditActivity.this, DialogAmountActivity.class);
                startActivity(i);
            }
        });
    }
}
