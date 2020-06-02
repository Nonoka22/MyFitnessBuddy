package com.example.myfitnessbuddy.activities;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.myfitnessbuddy.Constants;
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
    private String currentUserId;
    Handler handler = new Handler();

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_criterias;
    }

    @Override
    protected void initActivityImpl() {

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId= firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        currentUser = databaseReference.child(Constants.USERS).child(currentUserId);
        userTypeRef = currentUser.child(Constants.USER_TYPE);

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

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(userType.equals(Constants.TRAINEE)){

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
                    fragmentList = new ArrayList<Fragment>(Arrays.asList(FragmentCriteriasGoals.getInstance(),FragmentCriteriasLocation.getInstance(),FragmentCriteriasTrainerType.getInstance(),
                            FragmentCriteriasTraineePrice.getInstance(), FragmentCriteriasNutritionist.getInstance(), FragmentCriteriasCriterias.getInstance(), FragmentCriteriasTraineeIntroduction.getInstance()));

                }
                else if(userType.equals(Constants.TRAINER)){

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
                    fragmentList = new ArrayList<Fragment>(Arrays.asList( FragmentCriteriasSpecialty.getInstance(), FragmentCriteriasGym.getInstance(), FragmentCriteriasType.getInstance(),  FragmentCriteriasTrainerPrice.getInstance(), FragmentCriteriasTrainerIntroduction.getInstance()));
                }

                setPagerAdapter(viewPager, tabLayout, fragmentList);
            }
        },3000);
    }

    @Subscribe
    public void onSomeActionEventRecieved(PassingTraineeCriteriasEvent event){

        switch (event.getFragmentName()) {
            case Constants.GOAL_FRAGMENT:
                traineeCriterias.setGoal(event.getData());
                break;
            case Constants.LOCATION_FRAGMENT:
                traineeCriterias.setCity(event.getData());
                break;
            case Constants.TRAINER_TYPE_FRAGMENT:
                traineeCriterias.setTrainerType(event.getListData());
                break;
            case Constants.TRAINEE_PRICE_FRAGMENT:
                Price price = new Price();
                price.setHours(event.getData());
                price.setCost(event.getAdditionalData());
                traineeCriterias.setPrice(price);
                break;
            case Constants.NUTRITIONIST_FRAGMENT:
                if(event.getData().equals(Constants.NO)){
                    traineeCriterias.setNutritionistNeeded(false);
                }
                else if(event.getData().equals(Constants.YES)){
                    traineeCriterias.setNutritionistNeeded(true);
                }
                break;
            case Constants.CRITERIAS_FRAGMENT:
                traineeCriterias.setCriterias(event.getListData());
                break;
            case Constants.TRAINEE_INTRODUCTION_FRAGMENT:
                user.setImageURL(event.getData());
                user.setIntroduction(event.getAdditionalData());
                break;
        }

        user.setCriterias(traineeCriterias);
    }

    @Subscribe
    public void onSomeActionEventRecieved(PassingTrainerCriteriasEvent event){

        switch (event.getFragmentName()) {
            case Constants.SPECIALTIES:
                trainerCriterias.setSpecialties(event.getListData());
                break;
            case Constants.GYM_FRAGMENT:
                trainerCriterias.setCity(event.getData());
                trainerCriterias.setGym(event.getAdditionalData());
                break;
            case Constants.TRAINER_TYPE_FRAGMENT:
                trainerCriterias.setTrainerType(event.getListData());
                break;
            case Constants.TRAINER_PRICE_FRAGMENT:
                Price price = new Price();
                price.setHours(event.getData());
                price.setCost(event.getAdditionalData());
                trainerCriterias.setPrice(price);
                break;
            case Constants.TRAINER_INTRODUCTION_FRAGMENT:
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
        currentUser.child(Constants.INTRODUCTION).setValue(user.getIntroduction());
        currentUser.child(Constants.IMAGE_URL).setValue(user.getImageURL());
        currentUser.child(Constants.CRITERIAS).setValue(user.getCriterias());
        if(userType.equals(Constants.TRAINER)){
            //updates the old one every time
            databaseReference.child(Constants.TRAINER_LOCATIONS).child(user.getCriterias().getCity()).push().setValue(currentUserId);
        }
        startActivity(new Intent(CriteriasActivity.this,InteriorActivity.class)); }


}
