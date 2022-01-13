package com.example.etherealtherapist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class JoinActivity extends AppCompatActivity {
    Button btn1;
    TextView btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        btn1=(Button)findViewById(R.id.submitjoin);
        btn2=(TextView)findViewById(R.id.alreadyjoin);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent K= new Intent(getApplicationContext(),Login.class);
                startActivity(K);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent J= new Intent(getApplicationContext(),Login.class);
                startActivity(J);
            }
        });
    }
}