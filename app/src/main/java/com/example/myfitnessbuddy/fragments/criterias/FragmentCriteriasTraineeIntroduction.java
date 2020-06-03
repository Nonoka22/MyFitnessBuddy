package com.example.myfitnessbuddy.fragments.criterias;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasTraineeIntroductionFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTraineeCriteriasEvent;
import com.example.myfitnessbuddy.events.SaveCriteriasEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

public class FragmentCriteriasTraineeIntroduction extends BaseFragment<CriteriasTraineeIntroductionFragmentBinding> {

    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_trainee_introduction_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        Button saveDataButton = binding.traineeSaveDataButton;
        final EditText introduction = binding.editTextIntroductionTrainer;
        Button uploadImageButton = binding.chooseImageTrainee;
        progressBar = binding.traineeImageProgressBar;
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
                        EventBus.getDefault().post(new PassingTraineeCriteriasEvent("TraineeIntroduction", imageUrl,intro));
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

    public static FragmentCriteriasTraineeIntroduction getInstance(){
        return new FragmentCriteriasTraineeIntroduction();
    }
}
