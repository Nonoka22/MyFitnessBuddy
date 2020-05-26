package com.example.myfitnessbuddy.fragments.registration;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.myfitnessbuddy.databinding.RegisterBirthdayFragmentBinding;
import com.example.myfitnessbuddy.events.DateEvent;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.events.PassingUserArgumentsEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.events.SetPrevFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.example.myfitnessbuddy.fragments.dialogs.DatePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FragmentRegisterBirthday extends BaseFragment<RegisterBirthdayFragmentBinding> {

    private EditText fieldBirthday;
    private String birthDate;

    @Override
    protected int getFragmentLayout() {
        return R.layout.register_birthday_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        fieldBirthday = binding.fieldRegisterDateOfBirth;
        ImageView nextButton = binding.nextButtonBirthday;
        ImageView prevButton = binding.prevButtonBirthday;

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

    public static FragmentRegisterBirthday getInstance(){
        return new FragmentRegisterBirthday();
    }
}
