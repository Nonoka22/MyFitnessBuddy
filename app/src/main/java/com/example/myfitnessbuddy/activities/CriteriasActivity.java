package com.example.myfitnessbuddy.activities;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.ActivityCriteriasBinding;
import com.example.myfitnessbuddy.databinding.TraineeCriteriasTablayoutBinding;
import com.example.myfitnessbuddy.databinding.TrainerCriteriasTablayoutBinding;
import com.example.myfitnessbuddy.events.PassingTraineeCriteriasEvent;
import com.example.myfitnessbuddy.events.PassingTrainerCriteriasEvent;
import com.example.myfitnessbuddy.events.SaveCriteriasEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.events.SetPrevFragmentEvent;
import com.example.myfitnessbuddy.fragments.criterias.FragmentCriteriasCriterias;
import com.example.myfitnessbuddy.fragments.criterias.FragmentCriteriasGoals;
import com.example.myfitnessbuddy.fragments.criterias.FragmentCriteriasGym;
import com.example.myfitnessbuddy.fragments.criterias.FragmentCriteriasLocation;
import com.example.myfitnessbuddy.fragments.criterias.FragmentCriteriasNutritionist;
import com.example.myfitnessbuddy.fragments.criterias.FragmentCriteriasSpecialty;
import com.example.myfitnessbuddy.fragments.criterias.FragmentCriteriasTraineeIntroduction;
import com.example.myfitnessbuddy.fragments.criterias.FragmentCriteriasTraineePrice;
import com.example.myfitnessbuddy.fragments.criterias.FragmentCriteriasTrainerIntroduction;
import com.example.myfitnessbuddy.fragments.criterias.FragmentCriteriasTrainerPrice;
import com.example.myfitnessbuddy.fragments.criterias.FragmentCriteriasTrainerType;
import com.example.myfitnessbuddy.fragments.criterias.FragmentCriteriasType;
import com.example.myfitnessbuddy.models.CustomViewPager;
import com.example.myfitnessbuddy.models.Price;
import com.example.myfitnessbuddy.models.TraineeCriterias;
import com.example.myfitnessbuddy.models.TrainerCriterias;
import com.example.myfitnessbuddy.models.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

public class CriteriasActivity extends BaseActivity<ActivityCriteriasBinding> {

    CustomViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<Fragment> fragmentList;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference userTypeRef;
    DatabaseReference currentUser;
    String userType;
    private User user = new User();
    TraineeCriterias traineeCriterias = new TraineeCriterias();
    TrainerCriterias trainerCriterias = new TrainerCriterias();

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_criterias;
    }

    @Override
    protected void initActivityImpl() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Users");
        currentUser = databaseReference.child(firebaseAuth.getCurrentUser().getUid());
        userTypeRef = currentUser.child("userType");

        viewPager = binding.viewPagerCriterias;

        userTypeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userType = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(userType.equals("Trainee")){

                    binding.viewStubCriterias.setOnInflateListener(new ViewStub.OnInflateListener() {
                        @Override
                        public void onInflate(ViewStub stub, View inflated) {
                            TraineeCriteriasTablayoutBinding binding = DataBindingUtil.bind(inflated);
                            tabLayout = binding.tabLayoutTraineeCriterias;
                        }
                    });
                    binding.viewStubCriterias.getViewStub().setLayoutResource(R.layout.trainee_criterias_tablayout);
                    binding.viewStubCriterias.getViewStub().inflate();

                    //add fragments
                    fragmentList = new ArrayList<Fragment>(Arrays.asList(new FragmentCriteriasGoals(),new FragmentCriteriasLocation(),new FragmentCriteriasTrainerType(),
                            new FragmentCriteriasTraineePrice(),new FragmentCriteriasNutritionist(),new FragmentCriteriasCriterias(),new FragmentCriteriasTraineeIntroduction()));

                }
                else if(userType.equals("Trainer")){

                    binding.viewStubCriterias.setOnInflateListener(new ViewStub.OnInflateListener() {
                        @Override
                        public void onInflate(ViewStub stub, View inflated) {
                            TrainerCriteriasTablayoutBinding binding = DataBindingUtil.bind(inflated);
                            tabLayout = binding.tabLayoutTrainerCriterias;
                        }
                    });

                    binding.viewStubCriterias.getViewStub().setLayoutResource(R.layout.trainer_criterias_tablayout);
                    binding.viewStubCriterias.getViewStub().inflate();

                    //add fragments
                    fragmentList = new ArrayList<Fragment>(Arrays.asList(new FragmentCriteriasSpecialty(),new FragmentCriteriasGym(),new FragmentCriteriasType(), new FragmentCriteriasTrainerPrice(),new FragmentCriteriasTrainerIntroduction()));
                }

                setPagerAdapter(viewPager, tabLayout, fragmentList);
            }
        },3000);
    }

    @Subscribe
    public void onSomeActionEventRecieved(PassingTraineeCriteriasEvent event){

        switch (event.getFragmentName()) {
            case "Goal":
                traineeCriterias.setGoal(event.getData());
                break;
            case "Location":
                traineeCriterias.setCity(event.getData());
                break;
            case "TrainerType":
                traineeCriterias.setTrainerType(event.getListData());
                break;
            case "TraineePrice":
                Price price = new Price();
                price.setHours(event.getData());
                price.setCost(event.getAdditionalData());
                traineeCriterias.setPrice(price);
                break;
            case "Nutritionist":
                if(event.getData().equals("no")){
                    traineeCriterias.setNeedsNutritionist(false);
                }
                else if(event.getData().equals("yes")){
                    traineeCriterias.setNeedsNutritionist(true);
                }
                break;
            case "Criterias":
                traineeCriterias.setCriterias(event.getListData());
                break;
            case "TraineeIntroduction":
                user.setImageURL(event.getData());
                user.setIntroduction(event.getAdditionalData());
                break;
        }

        user.setCriterias(traineeCriterias);
    }

    @Subscribe
    public void onSomeActionEventRecieved(PassingTrainerCriteriasEvent event){

        switch (event.getFragmentName()) {
            case "Specialties":
                trainerCriterias.setSpecialties(event.getListData());
                break;
            case "Gym":
                trainerCriterias.setCity(event.getData());
                trainerCriterias.setGym(event.getAdditionalData());
                break;
            case "TrainerType":
                trainerCriterias.setTrainerType(event.getListData());
                break;
            case "TrainerPrice":
                Price price = new Price();
                price.setHours(event.getData());
                price.setCost(event.getAdditionalData());
                trainerCriterias.setPrice(price);
                break;
            case "TrainerIntroduction":
                user.setImageURL(event.getData());
                user.setIntroduction(event.getAdditionalData());
                break;
        }

        user.setCriterias(trainerCriterias);
    }

    @Subscribe
    public void onSomeActionEventRecieved(SetNextFragmentEvent showTabEvent){
        setNextFragment(viewPager);
    }

    @Subscribe
    public void onSomeActionEventRecieved(SetPrevFragmentEvent showTabEvent){
        setPreviousFragment(viewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onActionRecieved(SaveCriteriasEvent event){
        currentUser.child("introduction").setValue(user.getIntroduction());
        currentUser.child("imageURL").setValue(user.getImageURL());
        currentUser.child("criterias").setValue(user.getCriterias());
        startActivity(new Intent(CriteriasActivity.this,InteriorActivity.class));
    }
}
