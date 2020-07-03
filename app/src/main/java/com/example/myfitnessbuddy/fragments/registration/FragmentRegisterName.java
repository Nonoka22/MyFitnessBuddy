package com.example.myfitnessbuddy.fragments.registration;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.RegisterNameFragmentBinding;
import com.example.myfitnessbuddy.events.PassingUserArgumentsEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.events.SetPrevFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;


public class FragmentRegisterName extends BaseFragment<RegisterNameFragmentBinding> {

    private EditText fieldFirstName;
    private  EditText fieldLastName;
    private String firstName;
    private String lastName;
    private TextView error;

    @Override
    protected int getFragmentLayout() {
        return R.layout.register_name_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        fieldFirstName = binding.fieldRegisterFirstName;
        fieldLastName = binding.fieldRegisterLastName;
        ImageView nextButton = binding.nextButtonName;
        ImageView prevButton = binding.prevButtonName;
        error = binding.errorRegisterName;


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = fieldFirstName.getText().toString();
                lastName = fieldLastName.getText().toString();
                if(!firstName.isEmpty()&&!lastName.isEmpty()){

                    EventBus.getDefault().post(new PassingUserArgumentsEvent("Name",firstName,lastName));
                    EventBus.getDefault().post(new SetNextFragmentEvent());

                }
                else{
                    error.setText("You must enter first and last name");
                    error.setVisibility(View.VISIBLE);
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SetPrevFragmentEvent());

            }
        });
    }

    public static FragmentRegisterName getInstance(){
        return new FragmentRegisterName();
    }
}
