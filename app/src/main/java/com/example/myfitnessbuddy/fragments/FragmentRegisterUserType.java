package com.example.myfitnessbuddy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.RegistrationActivity;
import com.example.myfitnessbuddy.events.PassingUserArgumentsEvent;
import com.example.myfitnessbuddy.events.RegisterEvent;
import com.example.myfitnessbuddy.events.SetPrevFragmentEvent;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

public class FragmentRegisterUserType extends Fragment {
    private static final String TAG = "FragmentRegisterUserType";

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.register_user_type_fragment,container,false);
        Log.i("Noemi","User Type Fargment");

        radioGroup = view.findViewById(R.id.radioGroupUserType);
        Button createAccountButton = view.findViewById(R.id.registerButton);
        ImageView prevButton = view.findViewById(R.id.prevButtonUserType);


        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayout);
        tabLayout.bringToFront();

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            private String userType;

            @Override
            public void onClick(View v) {

                int selectedUserTypeId = radioGroup.getCheckedRadioButtonId();
                radioButton = view.findViewById(selectedUserTypeId);
                userType = radioButton.getText().toString();

                Toast.makeText(getActivity(), "CreateAccount", Toast.LENGTH_SHORT).show();

                EventBus.getDefault().post(new PassingUserArgumentsEvent("UserType",userType));

                //meghiv adatbazisba mento fgvnyt
                EventBus.getDefault().post(new RegisterEvent());

            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SetPrevFragmentEvent());

            }
        });

        return view;
    }
}
