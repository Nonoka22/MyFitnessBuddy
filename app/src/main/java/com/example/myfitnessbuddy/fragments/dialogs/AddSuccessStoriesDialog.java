package com.example.myfitnessbuddy.fragments.dialogs;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.example.myfitnessbuddy.OnBuddyClickedListener;
import com.example.myfitnessbuddy.OnSuccessStoryUploaded;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.DialogAddSuccessStoriesBinding;
import com.example.myfitnessbuddy.models.SuccessStory;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class AddSuccessStoriesDialog extends DialogFragment {

    private static final int PICK_FIRST_IMAGE_REQUEST = 1;
    private static final int PICK_SECOND_IMAGE_REQUEST = 2;
    private Button beforeImgButton,afterImgButton,saveButton,dismissButton;
    private ImageView beforeImgView,afterImgView;
    private EditText successStoryEditText;
    private ProgressBar beforeProgressBar, afterProgressBar;
    private TextView error;
    private Uri beforeImageUri,afterImageUri;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String imageUrl;
    private SuccessStory successStory;

    private OnSuccessStoryUploaded listener;

    public AddSuccessStoriesDialog(OnSuccessStoryUploaded listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DialogAddSuccessStoriesBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_success_stories,container,false);
        View view = binding.getRoot();

        setCancelable(false);

        beforeImgButton = binding.chooseBeforeImgButton;
        afterImgButton = binding.chooseAfterImgButton;
        saveButton = binding.saveSuccessStoryButton;
        dismissButton = binding.dismissSuccessStoryDialog;
        beforeImgView = binding.beforeImgView;
        afterImgView = binding.afterImgView;
        beforeProgressBar = binding.beforeProgressBar;
        afterProgressBar = binding.afterProgressBar;
        successStoryEditText = binding.successstoryEditText;
        error = binding.errorMessageAddSuccessStory;

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        beforeImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select new picture and set imageView
                openFileChooser(PICK_FIRST_IMAGE_REQUEST);
            }
        });

        afterImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select new picture and set imageView
                openFileChooser(PICK_SECOND_IMAGE_REQUEST);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successStory = new SuccessStory();

                if(successStoryEditText.getText().toString().isEmpty()){
                    error.setText("You must enter a title!");
                    error.setVisibility(View.VISIBLE);
                }
                else if(beforeImgView.getDrawable() == null){
                    error.setText("You must upload a before image!");
                    error.setVisibility(View.VISIBLE);
                }
                else if(afterImgView.getDrawable() == null){
                    error.setText("You must enter an after image!");
                    error.setVisibility(View.VISIBLE);
                }
                else{
                    Log.i("Noemi",successStory.toString());

                    if(successStory.getBeforeImageUrl().isEmpty()){
                        Log.i("Noemi", "Begore image url is empty");
                    }

                    successStory.setTitle(successStoryEditText.getText().toString());
                    uploadImages(beforeImageUri,beforeProgressBar);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            uploadImages(afterImageUri,afterProgressBar);
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if(successStory.getAfterImageUrl() != null && successStory.getBeforeImageUrl()!= null){
//                                        Log.i("Noemi","Saving "+successStory.getBeforeImageUrl() + successStory.getAfterImageUrl());
//                                        listener.successStoryUploaded(successStory);
//                                    }
//                                }
//                            },5000);
                        }
                    },6000);

                }
            }
        });

        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void openFileChooser(int requestCode){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(intent,requestCode);
    }

    protected String getFileExtension(Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    protected void uploadImages(Uri imageUri, final ProgressBar progressBar){

        Log.i("Noemi",successStory.toString());
        if(imageUri != null){
            final StorageReference storageRef = storageReference.child(System.currentTimeMillis()+ "." + getFileExtension(imageUri));
            StorageTask<UploadTask.TaskSnapshot> uploadTask = storageRef.putFile(imageUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {

                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        Log.i("Noemi", "Uploading image successful: " + downloadUri);
                        imageUrl = downloadUri.toString();
                        if(successStory.getBeforeImageUrl().isEmpty()){
                            successStory.setBeforeImageUrl(imageUrl);
                        }
                        else if(successStory.getAfterImageUrl().isEmpty()){
                            successStory.setAfterImageUrl(imageUrl);
                            Log.i("Noemi","Saving "+successStory.getBeforeImageUrl() + successStory.getAfterImageUrl());
                            listener.successStoryUploaded(successStory);
                            dismiss();
                        }
                    }
                    else {
                        Log.i("Noemi", "Error uploading image: ");

                    }
                }
            });

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Noemi","Here I am. req");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FIRST_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.i("Noemi","First image");
            beforeImageUri = data.getData();
            beforeImgView.setImageURI(beforeImageUri);
            beforeImgView.setVisibility(View.VISIBLE);
        }
        else if(requestCode == PICK_SECOND_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Log.i("Noemi","Second image");

            afterImageUri = data.getData();
            afterImgView.setImageURI(afterImageUri);
            afterImgView.setVisibility(View.VISIBLE);
        }

    }

}
