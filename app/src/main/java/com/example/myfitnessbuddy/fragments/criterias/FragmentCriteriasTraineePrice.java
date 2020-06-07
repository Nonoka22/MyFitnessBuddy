package com.example.myfitnessbuddy.fragments.criterias;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.example.myfitnessbuddy.utils.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasTraineePriceFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTraineeCriteriasEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

public class FragmentCriteriasTraineePrice extends BaseFragment<CriteriasTraineePriceFragmentBinding> {

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_trainee_price_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        ImageView nextButton = binding.nextButtonTraineePrice;
        final NumberPicker numberPicker = binding.numberPickerTrainee;
        final EditText lei = binding.editTextPriceTrainee;

        numberPicker.setMaxValue(5);
        numberPicker.setMinValue(0);

        final String[] pickerValues = new String[]{"0.5", "1", "1.5", "2", "2.5", "3"};
        numberPicker.setDisplayedValues(pickerValues);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = numberPicker.getValue();
                String selectedHour = pickerValues[selected];
                String cost = lei.getText().toString();

                EventBus.getDefault().post(new PassingTraineeCriteriasEvent(Constants.TRAINEE_PRICE_FRAGMENT,selectedHour,cost));
                EventBus.getDefault().post(new SetNextFragmentEvent());
            }
        });

    }

    public static FragmentCriteriasTraineePrice getInstance(){
        return new FragmentCriteriasTraineePrice();
    }
}
