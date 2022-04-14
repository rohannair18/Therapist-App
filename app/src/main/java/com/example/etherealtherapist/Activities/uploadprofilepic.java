package com.example.etherealtherapist.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.etherealtherapist.Fragments.ProfileFragment;
import com.example.etherealtherapist.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class uploadprofilepic extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView imageView;
    private FirebaseAuth authProfile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private Uri uriimage;
    private static final int PICK_IMAGE_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadprofilepic);
       // getSupportActionBar().setTitle("Upload profile Picture");
        Button choosepic=findViewById(R.id.choose_pic);
        Button buttonupload=findViewById(R.id.upload_pic);
        progressBar=findViewById(R.id.progressbar);
        imageView=findViewById(R.id.dp);
        authProfile= FirebaseAuth.getInstance();
        firebaseUser=authProfile.getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference("Therapist Display Pics");
        Uri uri= firebaseUser.getPhotoUrl();
        Picasso.with(uploadprofilepic.this).load(uri).into(imageView);
        choosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        buttonupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                UploadPic();
            }
        });

    }

    private void UploadPic() {
        if (uriimage != null){
            StorageReference filereference=storageReference.child(authProfile.getCurrentUser().getUid() +"." +getFilesExtension(uriimage));
            filereference.putFile(uriimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloaduri=uri;
                            firebaseUser= authProfile.getCurrentUser();
                            UserProfileChangeRequest profileUpdates= new UserProfileChangeRequest.Builder().setPhotoUri(downloaduri).build();
                            firebaseUser.updateProfile(profileUpdates);

                        }
                    });
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(uploadprofilepic.this, "Upload Sucessfull", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(uploadprofilepic.this, ProfileFragment.class);
                    //startActivity(intent);
                    setIntent(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(uploadprofilepic.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFilesExtension(Uri uri) {
        ContentResolver contentResolver= getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data !=null && data.getData() !=null) {
             uriimage = data.getData();
            imageView.setImageURI(uriimage);
        }
        }
    }
