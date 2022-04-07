package com.example.etherealtherapist.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.etherealtherapist.Fragments.HomeFragment;
import com.example.etherealtherapist.Model.Profile;
import com.example.etherealtherapist.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class CreateProfile extends AppCompatActivity {
    ImageView imageView;
    Button enter;
    EditText EnterPrice,EnterExperience,EnterQualification,URL;
    ProgressBar progressBar;
    Uri uri;
    StorageReference storageReference;
    UploadTask uploadTask;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    DocumentReference documentReference;
    private static final int PICK_IMAGE=1;
    Profile member;
    String currentuserID;

    String prevStarted = "yes";
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
        } else {
            moveToSecondary();
        }
    }

    private void moveToSecondary() {

        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        member= new Profile();
        imageView =findViewById(R.id.Profilepic);
        EnterPrice=findViewById(R.id.EnterPrice);
        enter = findViewById(R.id.Enter);
        EnterExperience=findViewById(R.id.EnterExperience);
        EnterQualification=findViewById(R.id.EnterQualification);
        URL=findViewById(R.id.EnterURL);
        progressBar=findViewById(R.id.progressBar);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        currentuserID=user.getUid();
         firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection("Therapists").document(currentuserID);
        storageReference= FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference=database.getInstance().getReference("Therapists");
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }


        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode==PICK_IMAGE|| resultCode ==RESULT_OK || data !=null ||data.getData() !=null){
                uri=data.getData();
                //Picasso.get().load(uri).into(imageView);
            }
        }catch (Exception e){
            Toast.makeText(this,"Error"+e,Toast.LENGTH_SHORT).show();
        }

    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver= getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }
    private void uploadData() {
        String price=EnterPrice.getText().toString();
        String experience=EnterExperience.getText().toString();
        String qualification=EnterQualification.getText().toString();
        String url=URL.getText().toString();
        if (!TextUtils.isEmpty(price) || !TextUtils.isEmpty(experience)|| !TextUtils.isEmpty(qualification)|| !TextUtils.isEmpty(url)|| uri !=null){
            final StorageReference reference= storageReference.child(System.currentTimeMillis()+"."+getFileExt(uri));
            uploadTask=reference.putFile(uri);
            Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }
                return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        Map<String,String> profile =new HashMap<>();
                        profile.put("price",price);
                        profile.put("Experience",experience);
                        profile.put("qualification",qualification);
                        profile.put("url",url);

                        member.setEnterPrice(price);
                        member.setEnterExperience(experience);
                        member.setEnterQualification(qualification);
                        member.setURL(url);
                        databaseReference.child(currentuserID).setValue(member);
                        documentReference.set(profile)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(CreateProfile.this,"Profile Created",Toast.LENGTH_SHORT).show();
                                        Handler handler=new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                    Intent intent= new Intent(CreateProfile.this,LoginActivity.class);
                                                    startActivity(intent);
                                            }
                                        },2000);
                                    }
                                });

                    }

                }
            });
        }else{
            Toast.makeText(this,"Please fill all fields",Toast.LENGTH_SHORT).show();
        }

    }
}