package com.example.myfitnessbuddy.fragments.criterias;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasSpecialtyFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTrainerCriteriasEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentCriteriasSpecialty extends BaseFragment<CriteriasSpecialtyFragmentBinding> {

    private int count = 0;

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_specialty_fragment;
    }

    @Override
    protected void initFragmentImpl() {

        List<ToggleButton> toggleButtons = new ArrayList<>(Arrays.asList(binding.nutritionistTB, binding.aerobicsTB, binding.sportsTB, binding.yogaTB, binding.pilatesTB, binding.kettlebellsTB,
                binding.cardioTB, binding.bodybuilderTB, binding.functionalTB, binding.fiziotherapistTB, binding.weightmanagementTB, binding.recoveryTB,
                binding.seniorTB, binding.youthTB, binding.performanceTB, binding.zumbaTB, binding.flexibilityspecialistTB));

        final ArrayList<String> selectedButtons = new ArrayList<>();

        for (final ToggleButton tb : toggleButtons){
            tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled
                        tb.setAlpha((float) 0.5);
                        ++count;
                        selectedButtons.add(tb.getText().toString());
                        if(count > 5){
                            Toast.makeText(getActivity(), "Only 5 can be selected.", Toast.LENGTH_SHORT).show();
                            --count;
                            tb.setChecked(false);
                            tb.setAlpha((float) 1);
                            selectedButtons.remove(tb.getText().toString());
                        }
                    } else {
                        // The toggle is disabled
                        --count;
                        tb.setAlpha((float) 1);
                        selectedButtons.remove(tb.getText().toString());
                    }
                }
            });
        }

        binding.nextButtonSpecialty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count < 1){
                    Toast.makeText(getActivity(), "At least 1 has to be selected.", Toast.LENGTH_SHORT).show();
                }
                else{
                    EventBus.getDefault().post(new PassingTrainerCriteriasEvent("Specialties",selectedButtons));
                    EventBus.getDefault().post(new SetNextFragmentEvent());
                }
            }
        });
    }
}
