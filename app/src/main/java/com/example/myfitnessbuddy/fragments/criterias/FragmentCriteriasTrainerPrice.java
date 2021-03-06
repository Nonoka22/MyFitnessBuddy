package com.example.myfitnessbuddy.fragments.criterias;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.myfitnessbuddy.utils.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasTrainerPriceFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTrainerCriteriasEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

public class FragmentCriteriasTrainerPrice extends BaseFragment<CriteriasTrainerPriceFragmentBinding> {

    TextView error;

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_trainer_price_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        ImageView nextButton = binding.nextButtonTrainerPrice;
        final NumberPicker numberPicker = binding.numberPickerTrainer;
        final EditText lei = binding.editTextLeiTrainer;
        error = binding.errorMessageCriteria;

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(5);
        final String[] pickerValues = new String[]{"0.5", "1", "1.5", "2", "2.5", "3"};
        numberPicker.setDisplayedValues(pickerValues);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = numberPicker.getValue();
                String selectedHour = pickerValues[selected];
                String cost = lei.getText().toString();

                if(!cost.isEmpty()){
                    EventBus.getDefault().post(new PassingTrainerCriteriasEvent(Constants.TRAINER_PRICE_FRAGMENT,selectedHour,cost));
                    EventBus.getDefault().post(new SetNextFragmentEvent());
                }
                else{
                    error.setText("You must enter a price");
                    error.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public static FragmentCriteriasTrainerPrice getInstance(){
        return new FragmentCriteriasTrainerPrice();
    }
}
