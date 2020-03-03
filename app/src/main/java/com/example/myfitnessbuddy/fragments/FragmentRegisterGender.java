package com.example.myfitnessbuddy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.events.SetPrevFragmentEvent;
import com.example.myfitnessbuddy.events.ShowTabEvent;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

public class FragmentRegisterGender extends Fragment {

    private static final String TAG = "FragmentRegisterGender";

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String gender;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.register_gender_fragment,container,false);
        Log.i("Noemi","Gender Fargment");

        radioGroup = view.findViewById(R.id.radioGroupGender);
        ImageView nextButton = view.findViewById(R.id.nextButtonGender);
        ImageView prevButton = view.findViewById(R.id.prevButtonGender);

        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayout);
        tabLayout.bringToFront();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGenderId = radioGroup.getCheckedRadioButtonId();
                radioButton = view.findViewById(selectedGenderId);
                gender = radioButton.getText().toString();
                Toast.makeText(getActivity(), gender, Toast.LENGTH_SHORT).show();

                EventBus.getDefault().post(new PassingUserArgumentsEvent("Gender",gender));
                EventBus.getDefault().post(new ShowTabEvent(4));
                EventBus.getDefault().post(new SetNextFragmentEvent());

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
