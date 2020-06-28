package com.example.myfitnessbuddy.fragments.registration;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.example.myfitnessbuddy.databinding.RegisterBirthdayFragmentBinding;
import com.example.myfitnessbuddy.events.DateEvent;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.events.PassingUserArgumentsEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.events.SetPrevFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.example.myfitnessbuddy.fragments.dialogs.MessageDialog;
import com.example.myfitnessbuddy.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;

public class FragmentRegisterBirthday extends BaseFragment<RegisterBirthdayFragmentBinding> {

    private TextView fieldBirthday;
    private String birthDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TextView error;
    private boolean isMinor = false;

    @Override
    protected int getFragmentLayout() {
        return R.layout.register_birthday_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        fieldBirthday = binding.fieldRegisterDateOfBirth;
        ImageView nextButton = binding.nextButtonBirthday;
        ImageView prevButton = binding.prevButtonBirthday;
        error = binding.errorRegisterDate;

        fieldBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = 2002;
                int month = 0;
                int day = 1;

                DatePickerDialog dialog = new DatePickerDialog(getContext(),R.style.Theme_AppCompat_DayNight_DarkActionBar,
                        dateSetListener,year,month,day);
                dialog.show();

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDate = fieldBirthday.getText().toString();
                if(!birthDate.isEmpty() && !isMinor){
                    error.setVisibility(View.GONE);

                    EventBus.getDefault().post(new PassingUserArgumentsEvent("BirthDate",birthDate));
                    EventBus.getDefault().post(new SetNextFragmentEvent());

                }

                else if(isMinor){
                    FragmentManager dialogFragment = getChildFragmentManager();
                    MessageDialog dialog = new MessageDialog(Constants.MINOR_TITLE,"Sorry! You cannot join as a minor. :(");
                    dialog.show(dialogFragment, "dialog");
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SetPrevFragmentEvent());
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "/" + month + "/" + dayOfMonth;

                if(Calendar.getInstance().get(Calendar.YEAR) - year < 18){
                    isMinor = true;
                }
                fieldBirthday.setText(date);
            }
        };
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
