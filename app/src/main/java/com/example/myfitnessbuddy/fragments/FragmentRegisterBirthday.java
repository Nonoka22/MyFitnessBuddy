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
import androidx.fragment.app.FragmentManager;

import com.example.myfitnessbuddy.events.DateEvent;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.RegistrationActivity;
import com.example.myfitnessbuddy.events.PassingUserArgumentsEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.events.SetPrevFragmentEvent;
import com.example.myfitnessbuddy.events.ShowTabEvent;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

public class FragmentRegisterBirthday extends Fragment {
    private static final String TAG = "FragmentRegisterBirthday";

    private EditText fieldBirthday;
    private String birthDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_birthday_fragment,container,false);
        Log.i("Noemi","Birthday Fragment");

        fieldBirthday = view.findViewById(R.id.fieldRegisterDateOfBirth);
        ImageView nextButton = view.findViewById(R.id.nextButtonBirthday);
        ImageView prevButton = view.findViewById(R.id.prevButtonBirthday);

        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayout);
        tabLayout.bringToFront();

        fieldBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager dialogFragment = getChildFragmentManager();
                DatePicker datePickerDialog = new DatePicker();
                datePickerDialog.show(dialogFragment, "dialog");
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDate = fieldBirthday.getText().toString();
                if(!birthDate.isEmpty()){
                    Toast.makeText(getActivity(), birthDate, Toast.LENGTH_SHORT).show();

                    EventBus.getDefault().post(new PassingUserArgumentsEvent("BirthDate",birthDate));
                    EventBus.getDefault().post(new ShowTabEvent(3));
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DateEvent event) {
        fieldBirthday.setText(event.getDate());

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
