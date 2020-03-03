package com.example.myfitnessbuddy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.events.PassingUserArgumentsEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.events.ShowTabEvent;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;


public class FragmentRegisterPhoneNumber extends Fragment {
    private static final String TAG = "FragmentRegisterPhoneNumber";

    private EditText fieldPhoneNumber;
    private String phoneNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_phone_number_fragment,container,false);

        fieldPhoneNumber = view.findViewById(R.id.fieldRegisterPhone);
        Log.i("Noemi","Phone Number Fragment is shown");
        ImageView nextButton = view.findViewById(R.id.nextButtonPhoneNumber);

        //hasznalj erre is EventBus-t
        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayout);
        tabLayout.bringToFront();

        EventBus.getDefault().post(new ShowTabEvent(0));

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = fieldPhoneNumber.getText().toString();

                if(!phoneNumber.isEmpty()){

                    EventBus.getDefault().post(new PassingUserArgumentsEvent("PhoneNumber", phoneNumber));
                    EventBus.getDefault().post(new ShowTabEvent(1));
                    EventBus.getDefault().post(new SetNextFragmentEvent());
                }
                else{
                    Log.i("Noemi",phoneNumber+"else...");
                }

            }
        });

        return view;
    }

}
