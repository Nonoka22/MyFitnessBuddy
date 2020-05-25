package com.example.myfitnessbuddy.fragments.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myfitnessbuddy.events.DateEvent;
import com.example.myfitnessbuddy.R;

import org.greenrobot.eventbus.EventBus;

public class DatePicker extends DialogFragment implements View.OnClickListener{

    private String date;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_dialog,null);

        setCancelable(false);
        Button selectDate = view.findViewById(R.id.selectDateButton);
        selectDate.setOnClickListener(this);
        CalendarView calendarView = view.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month = month +1;
                date = year + "/" + month + "/" + dayOfMonth;
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new DateEvent(date));
        dismiss();
    }

}
