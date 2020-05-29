package com.example.myfitnessbuddy.fragments.registration;

import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.RegisterGenderFragmentBinding;
import com.example.myfitnessbuddy.events.PassingUserArgumentsEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.events.SetPrevFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

public class FragmentRegisterGender extends BaseFragment<RegisterGenderFragmentBinding> {

    private RadioButton radioButtonFemale;
    private RadioButton radioButtonMale;
    private String gender;

    @Override
    protected int getFragmentLayout() {
        return R.layout.register_gender_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        ImageView nextButton = binding.nextButtonGender;
        ImageView prevButton = binding.prevButtonGender;
        radioButtonFemale = binding.femaleButton;
        radioButtonMale = binding.maleButton;


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButtonFemale.isChecked()){
                    gender = radioButtonFemale.getText().toString();
                }
                else {
                    gender = radioButtonMale.getText().toString();
                }

                EventBus.getDefault().post(new PassingUserArgumentsEvent("Gender",gender));
                EventBus.getDefault().post(new SetNextFragmentEvent());


            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SetPrevFragmentEvent());
            }
        });
    }

    public static FragmentRegisterGender getInstance(){
        return new FragmentRegisterGender();
    }

}
