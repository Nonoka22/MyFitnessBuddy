package com.example.myfitnessbuddy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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


public class FragmentRegisterName extends Fragment {
    private static final String TAG = "FragmentRegisterName";

    private EditText fieldFirstName;
    private  EditText fieldLastName;
    private String firstName;
    private String lastName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_name_fragment,container,false);
        Log.i("Noemi","Name fragment ");

        fieldFirstName = view.findViewById(R.id.fieldRegisterFirstName);
        fieldLastName = view.findViewById(R.id.fieldRegisterLastName);
        ImageView nextButton = view.findViewById(R.id.nextButtonName);
        ImageView prevButton = view.findViewById(R.id.prevButtonName);

        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayout);
        tabLayout.bringToFront();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = fieldFirstName.getText().toString();
                lastName = fieldLastName.getText().toString();
                if(!firstName.isEmpty()&&!lastName.isEmpty()){
                    Toast.makeText(getActivity(), firstName, Toast.LENGTH_SHORT).show();

                    EventBus.getDefault().post(new PassingUserArgumentsEvent("Name",firstName,lastName));
                    EventBus.getDefault().post(new ShowTabEvent(2));
                    EventBus.getDefault().post(new SetNextFragmentEvent());

                }
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
