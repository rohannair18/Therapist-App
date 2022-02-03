package com.example.etherealtherapist.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.etherealtherapist.R;

public class MainActivity extends AppCompatActivity {
    Button btn1;
    TextView btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(getColor(R.color.dark_lavender2));
                decor.setSystemUiVisibility(0);
            }
        }

        btn1=(Button)findViewById(R.id.getstarted);
        btn2=(TextView)findViewById(R.id.ialready);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent K= new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(K);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent J= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(J);
            }
        });

    }
}