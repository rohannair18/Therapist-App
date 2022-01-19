package com.example.etherealtherapist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class JoinActivity extends AppCompatActivity {
    Button btn1;
    TextView btn2;
    EditText Name,email,password;
    FirebaseAuth auth;
    DatabaseReference Reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        btn1=(Button)findViewById(R.id.submitjoin);
        btn2=(TextView)findViewById(R.id.alreadyjoin);
        Name=findViewById(R.id.namenickname);
        email=findViewById(R.id.emailjoin);
        password=findViewById(R.id.passwordjoin);
        auth= FirebaseAuth.getInstance();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name= Name.getText().toString();
                String txt_email= email.getText().toString();
                String txt_password=password.getText().toString();
                if (TextUtils.isEmpty(txt_name) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password))
                {
                    Toast.makeText(JoinActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else if (txt_password.length() < 6)
                {
                    Toast.makeText(JoinActivity.this, "Password must be atleast 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    signup(txt_name, txt_email, txt_password);
                }
                //Intent K= new Intent(getApplicationContext(),Login.class);
                //startActivity(K);
                
            }
            private void signup (String Name, String email, String password)
            {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    FirebaseUser firebaseUser =  auth.getCurrentUser();
                                    assert firebaseUser != null;
                                    String userid = firebaseUser.getUid();

                                    Reference = FirebaseDatabase.getInstance().getReference("Therapists").child(userid);

                                    HashMap<String, String> hashMap =  new HashMap<>();
                                    hashMap.put("Name", Name);
                                    hashMap.put("email", email);
                                    hashMap.put("password", password);

                                    Reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Intent intent = new Intent(JoinActivity.this, Login.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(JoinActivity.this, "The email or password entered is incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
