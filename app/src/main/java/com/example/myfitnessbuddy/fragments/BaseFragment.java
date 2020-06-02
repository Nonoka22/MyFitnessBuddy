package com.example.myfitnessbuddy.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myfitnessbuddy.Constants;
import com.example.myfitnessbuddy.models.Criterias;
import com.example.myfitnessbuddy.models.CustomViewPager;
import com.example.myfitnessbuddy.models.TraineeCriterias;
import com.example.myfitnessbuddy.models.TrainerCriterias;
import com.example.myfitnessbuddy.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.CountDownLatch;

import static android.app.Activity.RESULT_OK;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {

    protected T binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("Images");
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    //private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    protected String imageUrl;
    protected Uri imageUri;
    protected User user = new User();
    protected Criterias criterias;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(binding == null){
            binding = DataBindingUtil.inflate(inflater, getFragmentLayout(), container, false);
        }
        View view = binding.getRoot();

        initFragmentImpl();

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected abstract int getFragmentLayout();
    protected abstract void initFragmentImpl();

    protected void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }


    protected void activityResult(int requestCode, int resultCode, Intent data, ImageView imageView) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            imageView.setImageURI(imageUri);
        }
    }

    protected String getFileExtension(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    protected void uploadImage(Uri imageUri, final ProgressBar progressBar){
        if(imageUri != null){
            StorageReference storageRef = storageReference.child(System.currentTimeMillis()+ "." + getFileExtension(imageUri));
            storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("Noemi","Uploading image successful");
                    imageUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("Noemi","Error uploading image: "+e.getMessage());
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }
    }

    protected void setUser(final String userId){
        databaseReference.child(Constants.USERS).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if(user.getUserType().equals(Constants.TRAINEE)){
                    criterias = dataSnapshot.child(Constants.CRITERIAS).getValue(TraineeCriterias.class);

                }
                else if(user.getUserType().equals(Constants.TRAINER)){
                    criterias = dataSnapshot.child(Constants.CRITERIAS).getValue(TrainerCriterias.class);

                }
                user.setCriterias(criterias);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected User getUser(){
        return user;
    }

}
