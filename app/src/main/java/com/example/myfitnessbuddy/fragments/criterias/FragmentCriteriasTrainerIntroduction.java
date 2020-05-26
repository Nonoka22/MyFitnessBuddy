package com.example.myfitnessbuddy.fragments.criterias;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasTrainerIntroductionFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTrainerCriteriasEvent;
import com.example.myfitnessbuddy.events.SaveCriteriasEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

public class FragmentCriteriasTrainerIntroduction extends BaseFragment<CriteriasTrainerIntroductionFragmentBinding> {

    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_trainer_introduction_fragment;
    }

    @Override
    protected void initFragmentImpl() {

        Button saveDataButton = binding.trainerSaveDataButton;
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
                uploadImage(imageUri,progressBar);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new PassingTrainerCriteriasEvent("TrainerIntroduction", imageUrl ,intro));
                        EventBus.getDefault().post(new SaveCriteriasEvent());
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
}

