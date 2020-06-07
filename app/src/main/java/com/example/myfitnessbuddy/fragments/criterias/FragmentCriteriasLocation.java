package com.example.myfitnessbuddy.fragments.criterias;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.myfitnessbuddy.utils.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasLocationFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTraineeCriteriasEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

public class FragmentCriteriasLocation extends BaseFragment<CriteriasLocationFragmentBinding> {

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_location_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        final Spinner citySpinner = binding.traineeCitySpinner;

        final String[] cities = {"Alba Iulia","Arad","Bacau","Baia Mare","Braila", "Brasov", "Bucuresti", "Cluj Napoca","Constanta", "Craiova", "Deva", "Galati",
                "Iasi","Oradea","Pitesti", "Ploiesti","Satu Mare","Sibiu","Suceava", "Targu Mures", "Timisoara"};

        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cities);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(citiesAdapter);

        binding.nextButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PassingTraineeCriteriasEvent(Constants.LOCATION_FRAGMENT, citySpinner.getSelectedItem().toString()));
                EventBus.getDefault().post(new SetNextFragmentEvent());
            }
        });
    }

    public static FragmentCriteriasLocation getInstance(){
        return new FragmentCriteriasLocation();
    }
}
