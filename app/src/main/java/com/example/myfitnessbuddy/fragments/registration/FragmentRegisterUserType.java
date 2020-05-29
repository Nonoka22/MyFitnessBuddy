package com.example.myfitnessbuddy.fragments.registration;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.RegisterUserTypeFragmentBinding;
import com.example.myfitnessbuddy.events.PassingUserArgumentsEvent;
import com.example.myfitnessbuddy.events.RegisterEvent;
import com.example.myfitnessbuddy.events.SetPrevFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

public class FragmentRegisterUserType extends BaseFragment<RegisterUserTypeFragmentBinding> {

    private RadioButton radioButtonTrainer;
    private RadioButton radioButtonTrainee;

    @Override
    protected int getFragmentLayout() {
        return R.layout.register_user_type_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        Button createAccountButton = binding.registerButton;
        ImageView prevButton = binding.prevButtonUserType;
        radioButtonTrainee = binding.traineeButton;
        radioButtonTrainer = binding.trainerButton;

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            private String userType;

            @Override
            public void onClick(View v) {
                if(radioButtonTrainee.isChecked()){
                    userType = radioButtonTrainee.getText().toString();
                }
                else{
                    userType = radioButtonTrainer.getText().toString();
                }

                EventBus.getDefault().post(new PassingUserArgumentsEvent("UserType",userType));
                EventBus.getDefault().post(new RegisterEvent());

            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SetPrevFragmentEvent());
            }
        });

    }

    public static FragmentRegisterUserType getInstance(){
        return new FragmentRegisterUserType();
    }
}
