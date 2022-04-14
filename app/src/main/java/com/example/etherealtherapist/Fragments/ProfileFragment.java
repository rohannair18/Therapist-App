package com.example.etherealtherapist.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;


import com.example.etherealtherapist.Activities.EditActivity;
import com.example.etherealtherapist.Activities.MainActivity;
import com.example.etherealtherapist.Activities.uploadprofilepic;
import com.example.etherealtherapist.HelperClasses.ViewPagerAdapter;
import com.example.etherealtherapist.Model.Therapist;
import com.example.etherealtherapist.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.CollationElementIterator;



public class ProfileFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseAuth firebaseAuth;
    private ImageView edit;
    private ImageView logout;
    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String profileId;
    private TextView profilename;
    private ImageView profilepic;
    private StorageReference storageReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        profileId = fUser.getUid();
        storageReference= FirebaseStorage.getInstance().getReference("Therapist Display Pics/"+profileId+".jpg");
        profilepic=view.findViewById(R.id.Profilepic);
        profilename = view.findViewById(R.id.profilename);
        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
        logout = view.findViewById(R.id.logout);
        edit=view.findViewById(R.id.edit);
        //Uri uri= fUser.getPhotoUrl();
        //Picasso.with(getContext()).load(uri).into(profilepic);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(getContext(), uploadprofilepic.class));

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditActivity.class));
            }
        });
        profileName();
        ProfilePic();
        return view;

    }

    private void ProfilePic() {

        try {
            final File localfile=File.createTempFile("Tempfile",".jpg");
            storageReference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap=BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            Matrix matrix=new Matrix();

                            matrix.postRotate(90);

                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,100 ,100 , true);

                            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                            profilepic.setImageBitmap(rotatedBitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Error occurred while retrieving/Profile Pic has not been set!", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new NotificationFragment(), "Notifications");
        adapter.addFragment(new JournalFragment(), "Journal");
        viewPager.setAdapter(adapter);

    }
    private void profileName() {

        FirebaseDatabase.getInstance().getReference().child("Therapists").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Therapist therapist = dataSnapshot.getValue(Therapist.class);
                profilename.setText(therapist.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}