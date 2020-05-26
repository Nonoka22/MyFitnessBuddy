package com.example.myfitnessbuddy.fragments.criterias;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasTrainertypeFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTraineeCriteriasEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentCriteriasTrainerType extends BaseFragment<CriteriasTrainertypeFragmentBinding> {

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_trainertype_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        ImageView nextButton = binding.nextButtonTrainerType;
        ToggleButton persTrainer = binding.personalTrainerTraineeTB;
        ToggleButton groupIns = binding.groupInstructorTraineeTB;

        ArrayList<ToggleButton> toggleButtons = new ArrayList<>(Arrays.asList(persTrainer, groupIns));
        final ArrayList<String> selectedButtons = new ArrayList<>();

        for(final ToggleButton tb : toggleButtons){
            tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        tb.setAlpha((float) 0.5);
                        selectedButtons.add(tb.getText().toString());
                    }
                    else {
                        tb.setAlpha((float) 1);
                        selectedButtons.remove(tb.getText().toString());
                    }
                }
            });
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PassingTraineeCriteriasEvent("TrainerType",selectedButtons));
                EventBus.getDefault().post(new SetNextFragmentEvent());
            }
        });
    }
}
