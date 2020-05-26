package com.example.myfitnessbuddy.fragments.criterias;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasNutritionistFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTraineeCriteriasEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

public class FragmentCriteriasNutritionist extends BaseFragment<CriteriasNutritionistFragmentBinding> {

    private RadioButton yesButton;
    private RadioButton noButton;
    private String nutritionistNeeded;

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_nutritionist_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        RadioGroup radioGroup = binding.radioGroupNutritionist;
        yesButton = binding.yesButton;
        noButton = binding.noButton;
        ImageView nextButton = binding.nextButtonNutritionist;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noButton.isChecked()){
                    nutritionistNeeded = "no";
                }
                else if (yesButton.isChecked()){
                    nutritionistNeeded = "yes";
                }

                EventBus.getDefault().post(new PassingTraineeCriteriasEvent("Nutritionist", nutritionistNeeded));
                EventBus.getDefault().post(new SetNextFragmentEvent());
            }
        });
    }
}
