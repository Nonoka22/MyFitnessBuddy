package com.example.myfitnessbuddy.fragments.criterias;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.myfitnessbuddy.utils.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasGoalsFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTraineeCriteriasEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentCriteriasGoals extends BaseFragment<CriteriasGoalsFragmentBinding> {

    private String selectedGoal;
    private int count = 0;
    private TextView error;

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_goals_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        List<ToggleButton> toggleButtons = new ArrayList<>(Arrays.asList(binding.agilityTB, binding.athleticTB, binding.composotionTB, binding.enduranceTB, binding.fizioTB
                , binding.flexibilityTB, binding.muscleTB, binding.painfreeTB, binding.staminaTB, binding.weightTB));

        error = binding.errorMessageCriteria;

        for (final ToggleButton tb : toggleButtons){
            tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        ++count;
                        tb.setAlpha((float) 0.5);
                        selectedGoal = tb.getText().toString();
                        if(count > 1){
                            error.setText("Only 1 goal can be selected.");
                            error.setVisibility(View.VISIBLE);
                            --count;
                            tb.setChecked(false);
                            tb.setAlpha((float) 1);
                        }
                    }
                    else{
                        --count;
                        tb.setAlpha((float)1);
                    }
                }
            });
        }

        binding.nextButtonGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count < 1){
                    error.setText("You must select at least one goal.");
                    error.setVisibility(View.VISIBLE);
                }
                else{
                    EventBus.getDefault().post(new PassingTraineeCriteriasEvent(Constants.GOAL_FRAGMENT, selectedGoal));
                    EventBus.getDefault().post(new SetNextFragmentEvent());
                }
            }
        });
    }

    public static FragmentCriteriasGoals getInstance(){
        return new FragmentCriteriasGoals();
    }
}

