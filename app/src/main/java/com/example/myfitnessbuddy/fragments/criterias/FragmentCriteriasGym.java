package com.example.myfitnessbuddy.fragments.criterias;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriasGymFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTrainerCriteriasEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class FragmentCriteriasGym extends BaseFragment<CriteriasGymFragmentBinding> {

    private Spinner citySpinner;
    private Spinner gymSpinner;
    private List<String> gyms = new ArrayList<>();

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_gym_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference =  mFirebaseDatabase.getReference().child("Gyms");

        citySpinner = binding.spinnerLocationTrainer;
        gymSpinner = binding.spinnerGym;
        ImageView nextButton = binding.nextButtonGym;

        final String[] cities = {"Alba Iulia","Arad","Bacau","Baia Mare","Braila", "Brasov", "Bucuresti", "Cluj Napoca","Constanta", "Craiova", "Deva", "Galati",
                "Iasi","Oradea","Pitesti", "Ploiesti","Satu Mare","Sibiu","Suceava", "Targu Mures", "Timisoara"};

        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cities);
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(citiesAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                databaseReference.child(citySpinner.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        gyms.clear();
                        for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                            //Log.i("Noemi",""+ childDataSnapshot.getValue());
                            gyms.add(childDataSnapshot.getValue().toString());
                        }
                        ArrayAdapter<String> gymsAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, gyms);
                        gymsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        gymSpinner.setAdapter(gymsAdapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = citySpinner.getSelectedItem().toString();
                String gym = gymSpinner.getSelectedItem().toString();
                EventBus.getDefault().post(new PassingTrainerCriteriasEvent("Gym",city,gym));
                EventBus.getDefault().post(new SetNextFragmentEvent());
            }
        });
    }

    public static FragmentCriteriasGym getInstance(){
        return new FragmentCriteriasGym();
    }
}
