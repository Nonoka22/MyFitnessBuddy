package com.example.myfitnessbuddy.fragments.criterias;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myfitnessbuddy.utils.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasTrainerIntroductionFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTrainerCriteriasEvent;
import com.example.myfitnessbuddy.events.SaveCriteriasEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

public class FragmentCriteriasTrainerIntroduction extends BaseFragment<CriteriasTrainerIntroductionFragmentBinding> {

    private ImageView imageView;
    private ProgressBar progressBar;
    private TextView error;

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_trainer_introduction_fragment;
    }

    @Override
    protected void initFragmentImpl() {

        Button saveDataButton = binding.trainerSaveDataButton;
        error = binding.errorMessageCriteria;
        final EditText introduction = binding.editTextIntroductionTrainer;
        Button uploadImageButton = binding.chooseImageButton;
        progressBar = binding.trainerImageProgressBar;
        imageView = binding.trainerUploadImageView;

        progressBar.setVisibility(View.INVISIBLE);

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String intro = introduction.getText().toString();
                uploadImages(imageUri,progressBar);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(imageView.getDrawable() != null){
                            EventBus.getDefault().post(new PassingTrainerCriteriasEvent(Constants.TRAINER_INTRODUCTION_FRAGMENT, imageUrl ,intro));
                            EventBus.getDefault().post(new SaveCriteriasEvent());
                        }
                        else {
                            error.setText("You must provide a profile picture!");
                            error.setVisibility(View.VISIBLE);
                        }
                    }
                },5000);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        activityResult(requestCode,resultCode,data,imageView);

    }

    public static FragmentCriteriasTrainerIntroduction getInstance(){
        return new FragmentCriteriasTrainerIntroduction();
    }
}

