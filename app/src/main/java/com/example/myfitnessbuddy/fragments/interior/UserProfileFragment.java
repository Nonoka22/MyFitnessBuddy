package com.example.myfitnessbuddy.fragments.interior;

import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.FragmentUserProfileBinding;
import com.example.myfitnessbuddy.databinding.FragmentUserProfileTraineeBinding;
import com.example.myfitnessbuddy.databinding.FragmentUserProfileTrainerBinding;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.example.myfitnessbuddy.models.User;
import com.example.myfitnessbuddy.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends BaseFragment<FragmentUserProfileBinding> {

    private ImageView profilePicture;
    private TextView introduction, city, gym, price, birthdate,name,goal, nutritionist;
    private ListView trainerType, specialties, criterias;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference currentUser;
    private DatabaseReference userTypeRef;
    private String userType;
    List<String> trainerTypeList = new ArrayList<>();
    List<String> criteriasList = new ArrayList<>();
    List<String> specialtyList = new ArrayList<>();
    User user = new User();

    @Override
    protected int getFragmentLayout() {

        return R.layout.fragment_user_profile;
    }

    @Override
    protected void initFragmentImpl() {

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId= firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        currentUser = databaseReference.child(Constants.USERS).child(currentUserId);
        //userTypeRef = currentUser.child(Constants.USER_TYPE);


        currentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userType = dataSnapshot.child(Constants.USER_TYPE).getValue().toString();

                user = dataSnapshot.getValue(User.class);

                if(userType.equals(Constants.TRAINEE)){

                    for(DataSnapshot dSnap : dataSnapshot.child(Constants.CRITERIAS).child(Constants.CRITERIAS).getChildren()){
                        criteriasList.add(dSnap.getValue().toString());
                    }

                    for(DataSnapshot dSnap : dataSnapshot.child(Constants.CRITERIAS+"/"+Constants.TRAINER_TYPE).getChildren()){
                        trainerTypeList.add(dSnap.getValue().toString());
                    }

                    String needsNutritionist = dataSnapshot.child(Constants.CRITERIAS).child(Constants.NUTRITIONIST_NEEDED).getValue().toString();

                    binding.viewStubUserProfile.setOnInflateListener(new ViewStub.OnInflateListener() {
                        @Override
                        public void onInflate(ViewStub stub, View inflated) {
                            FragmentUserProfileTraineeBinding binding = DataBindingUtil.bind(inflated);

                            name = binding.userProfileName;
                            birthdate = binding.userProfileDate;
                            introduction = binding.userProfileIntroduction;
                            city = binding.userProfileCity;
                            goal = binding.userProfileGoal;
                            price = binding.userProfilePrice;
                            trainerType = binding.userProfileTrainingType;
                            criterias = binding.userProfileCriterias;
                            nutritionist = binding.userProfileNutritionist;
                            profilePicture = binding.userProfileImageView;
                        }
                    });
                    binding.viewStubUserProfile.getViewStub().setLayoutResource(R.layout.fragment_user_profile_trainee);
                    binding.viewStubUserProfile.getViewStub().inflate();

                    name.setText("Name: " + user.getFirstName()+ " " + user.getLastName());
                    birthdate.setText("Birthdate: " + user.getBirthDate());
                    introduction.setText("Introduction: " + user.getIntroduction());
                    city.setText("City: " + dataSnapshot.child(Constants.CRITERIAS).child(Constants.CITY).getValue().toString());
                    goal.setText("Goal: " + dataSnapshot.child(Constants.CRITERIAS).child(Constants.GOAL).getValue().toString());
                    price.setText("Price: " + dataSnapshot.child(Constants.CRITERIAS).child(Constants.PRICE+"/"+Constants.COST).getValue().toString()
                            + "/" + dataSnapshot.child(Constants.CRITERIAS+"/"+Constants.PRICE+"/"+Constants.HOURS).getValue().toString());
                    trainerTypeList = user.getCriterias().getTrainerType();
                    ArrayAdapter trainerTypeAdapter = new ArrayAdapter(getContext(),R.layout.small_list_item,trainerTypeList);
                    trainerType.setAdapter(trainerTypeAdapter);
                    ArrayAdapter criteriasAdapter = new ArrayAdapter(getContext(),R.layout.small_list_item,criteriasList);
                    criterias.setAdapter(criteriasAdapter);
                    nutritionist.setText("Needs nutritionist: " + needsNutritionist);
                    Picasso.get()
                            .load(user.getImageURL())
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .into(profilePicture);

              }
                else if(userType.equals(Constants.TRAINER)){

                    for(DataSnapshot dSnap : dataSnapshot.child(Constants.CRITERIAS).child(Constants.SPECIALTIES).getChildren()){
                        specialtyList.add(dSnap.getValue().toString());
                    }

                    for(DataSnapshot dSnap : dataSnapshot.child(Constants.CRITERIAS+"/"+Constants.TRAINER_TYPE).getChildren()){
                        trainerTypeList.add(dSnap.getValue().toString());
                    }

                    binding.viewStubUserProfile.setOnInflateListener(new ViewStub.OnInflateListener() {
                        @Override
                        public void onInflate(ViewStub stub, View inflated) {
                            FragmentUserProfileTrainerBinding binding = DataBindingUtil.bind(inflated);

                            name = binding.userProfileName;
                            birthdate = binding.userProfileDate;
                            introduction = binding.userProfileIntroduction;
                            city = binding.userProfileCity;
                            gym = binding.userProfileGym;
                            price = binding.userProfilePrice;
                            trainerType = binding.userProfileTrainingType;
                            specialties = binding.userProfileSpecialties;
                            profilePicture = binding.userProfileImageView;

                        }
                    });

                    binding.viewStubUserProfile.getViewStub().setLayoutResource(R.layout.fragment_user_profile_trainer);
                    binding.viewStubUserProfile.getViewStub().inflate();

                    name.setText("Name: " + user.getFirstName()+ " " + user.getLastName());
                    birthdate.setText("Birthdate: " + user.getBirthDate());
                    introduction.setText("Introduction: " + user.getIntroduction());
                    city.setText("City: " + dataSnapshot.child(Constants.CRITERIAS).child(Constants.CITY).getValue().toString());
                    gym.setText("Gym: " + dataSnapshot.child(Constants.CRITERIAS).child(Constants.GYM).getValue().toString());
                    price.setText("Price: " + dataSnapshot.child(Constants.CRITERIAS).child(Constants.PRICE+"/"+Constants.COST).getValue().toString()
                            + "/" + dataSnapshot.child(Constants.CRITERIAS+"/"+Constants.PRICE+"/"+Constants.HOURS).getValue().toString());
                    ArrayAdapter trainerTypeAdapter = new ArrayAdapter(getContext(),R.layout.small_list_item,trainerTypeList);
                    trainerType.setAdapter(trainerTypeAdapter);
                    ArrayAdapter specialtyAdapter = new ArrayAdapter(getContext(),R.layout.small_list_item,specialtyList);
                    specialties.setAdapter(specialtyAdapter);

                    Picasso.get()
                            .load(user.getImageURL())
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .into(profilePicture);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static UserProfileFragment getInstance(){
        return new UserProfileFragment();
    }
}
