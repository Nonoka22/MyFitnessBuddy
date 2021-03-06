package com.example.myfitnessbuddy.fragments.criterias;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.myfitnessbuddy.utils.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasTypeFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTrainerCriteriasEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentCriteriasType extends BaseFragment<CriteriasTypeFragmentBinding> {

    private int count = 0;
    private TextView error;

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_type_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        ImageView nexrButton = binding.nextButtonTypeTrainer;
        final ToggleButton groupIns = binding.groupInstructorTrainerTB;
        final ToggleButton persTrainer =  binding.personalTrainerTrainerTB;
        error = binding.errorMessageCriteria;

        List<ToggleButton> toggleButtons = new ArrayList<>(Arrays.asList(persTrainer,groupIns));
        final ArrayList<String> selectedButtons = new ArrayList<>();

        for(final ToggleButton tb : toggleButtons){
            tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        ++count;
                        tb.setAlpha((float) 0.5);
                        selectedButtons.add(tb.getText().toString());
                    }
                    else {
                        --count;
                        tb.setAlpha((float) 1);
                        selectedButtons.remove(tb.getText().toString());
                    }
                }
            });
        }


        nexrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == 0){
                    error.setText("You must select at least one type!");
                    error.setVisibility(View.VISIBLE);
                }
                else {
                    EventBus.getDefault().post(new PassingTrainerCriteriasEvent(Constants.TRAINER_TYPE_FRAGMENT,selectedButtons));
                    EventBus.getDefault().post(new SetNextFragmentEvent());
                }
            }
        });
    }

    public static FragmentCriteriasType getInstance(){
        return new FragmentCriteriasType();
    }
}
