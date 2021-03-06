package com.example.myfitnessbuddy.fragments.criterias;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.myfitnessbuddy.utils.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasTrainertypeFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTraineeCriteriasEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;

public class FragmentCriteriasTrainerType extends BaseFragment<CriteriasTrainertypeFragmentBinding> {

    private int count = 0;
    private TextView error;

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_trainertype_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        ImageView nextButton = binding.nextButtonTrainerType;
        ToggleButton persTrainer = binding.personalTrainerTraineeTB;
        ToggleButton groupIns = binding.groupInstructorTraineeTB;
        error = binding.errorMessageCriteria;

        ArrayList<ToggleButton> toggleButtons = new ArrayList<>(Arrays.asList(persTrainer, groupIns));
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

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count == 0){
                    error.setText("You must select at least one type!");
                    error.setVisibility(View.VISIBLE);
                }
                else{
                    EventBus.getDefault().post(new PassingTraineeCriteriasEvent(Constants.TRAINER_TYPE_FRAGMENT,selectedButtons));
                    EventBus.getDefault().post(new SetNextFragmentEvent());
                }
            }
        });
    }

    public static FragmentCriteriasTrainerType getInstance(){
        return new FragmentCriteriasTrainerType();
    }
}
