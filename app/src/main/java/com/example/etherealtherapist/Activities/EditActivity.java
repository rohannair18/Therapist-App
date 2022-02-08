package com.example.etherealtherapist.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.etherealtherapist.Model.Therapist;
import com.example.etherealtherapist.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditActivity extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private Button save;

    private FirebaseAuth fUser;
    private MaterialCardView editback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            View decor = getWindow().getDecorView();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.setStatusBarColor(getColor(R.color.dark_lavender2));
                decor.setSystemUiVisibility(0);
            }
        }
        name = findViewById(R.id.nameedit);
        email = findViewById(R.id.emailedit);
        save = findViewById(R.id.savechanges);

        fUser = FirebaseAuth.getInstance();
        editback = findViewById(R.id.editback);

        FirebaseDatabase.getInstance().getReference().child("Therapists").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Therapist therapist = dataSnapshot.getValue(Therapist.class);
                name.setText(therapist.getName());
                email.setText(therapist.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });


    }
    private void updateProfile () {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name.getText().toString());
        map.put("email", email.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("Therapists").child(fUser.getUid()).updateChildren(map);

    }
}