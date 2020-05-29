package com.example.myfitnessbuddy.fragments.registration;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.RegisterPhoneNumberFragmentBinding;
import com.example.myfitnessbuddy.events.PassingUserArgumentsEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;


public class FragmentRegisterPhoneNumber extends BaseFragment<RegisterPhoneNumberFragmentBinding> {
    private EditText fieldPhoneNumber;
    private String phoneNumber;

    @Override
    protected int getFragmentLayout() {
        return R.layout.register_phone_number_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        fieldPhoneNumber = binding.fieldRegisterPhone;
        ImageView nextButton = binding.nextButtonPhoneNumber;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = fieldPhoneNumber.getText().toString();
                if(!phoneNumber.isEmpty()){

                    EventBus.getDefault().post(new PassingUserArgumentsEvent("PhoneNumber", phoneNumber));
                    EventBus.getDefault().post(new SetNextFragmentEvent());
                }
                else{
                    Log.i("Noemi",phoneNumber+"else...");
                }
            }
        });
    }

    public static FragmentRegisterPhoneNumber getInstance(){
        return new FragmentRegisterPhoneNumber();
    }
}
