package com.example.myfitnessbuddy.activities;

import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewStubProxy;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.ActivityBuddyProfileBinding;
import com.example.myfitnessbuddy.databinding.TraineeBuddyProfileBinding;
import com.example.myfitnessbuddy.databinding.TrainerBuddyProfileBinding;
import com.example.myfitnessbuddy.models.TraineeCriterias;
import com.example.myfitnessbuddy.models.TrainerCriterias;
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

import static com.example.myfitnessbuddy.R.layout.small_list_item;

public class BuddyProfileActivity extends BaseActivity<ActivityBuddyProfileBinding> {

    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView firstName, lastName,gender,introduction,city, goal, nutriNeeded, price, gym;
    private ImageView profPic;
    private ListView trainerTypeListView,specialtyListView;
    private ViewStubProxy viewStub;
    private User user;
    private List<String> trainerType = new ArrayList<>();
    private List<String> specialties = new ArrayList<>();
    String buddyId;
    String userType;

    public static BuddyProfileActivity getInstance(){
        return new BuddyProfileActivity();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_buddy_profile;
    }

    @Override
    protected void initActivityImpl() {

        buddyId = getIntent().getStringExtra(Constants.BUDDY_ID);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId= firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        viewStub = binding.viewStubBuddyProfile;

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userType = dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.USER_TYPE).getValue(String.class);

                user = dataSnapshot.child(Constants.USERS).child(buddyId).getValue(User.class);
                DataSnapshot dSnap = dataSnapshot.child(Constants.USERS).child(buddyId);

                if(userType.equals(Constants.TRAINER)){
                    binding.viewStubBuddyProfile.setOnInflateListener(new ViewStub.OnInflateListener() {
                        @Override
                        public void onInflate(ViewStub stub, View inflated) {
                            TraineeBuddyProfileBinding binding = DataBindingUtil.bind(inflated);
                            firstName = binding.bPFirstName;
                            lastName = binding.bPLastName;
                            gender = binding.bPGender;
                            introduction = binding.bPIntroduction;
                            city = binding.bPCity;
                            goal = binding.bPGoal;
                            nutriNeeded = binding.bPNutritionist;
                            price = binding.bPPrice;
                            profPic = binding.bPProfilePic;
                            trainerTypeListView = binding.trainerTypeListView;
                        }
                    });
                    viewStub.getViewStub().setLayoutResource(R.layout.trainee_buddy_profile);
                    viewStub.getViewStub().inflate();

                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    gender.setText(user.getGender());
                    introduction.setText(user.getIntroduction());
                    String imageURL = user.getImageURL();
                    Picasso.get()
                            .load(imageURL)
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .into(profPic);

                    TraineeCriterias traineeCriterias = dSnap.child(Constants.CRITERIAS).getValue(TraineeCriterias.class);
                    city.setText(traineeCriterias.getCity());
                    goal.setText(traineeCriterias.getGoal());
                    price.setText(traineeCriterias.getPrice().getCost() + "/" + traineeCriterias.getPrice().getHours() + "lej/hour");
                    nutriNeeded.setText(String.valueOf(traineeCriterias.isNutritionistNeeded()));

                    trainerType = traineeCriterias.getTrainerType();
                    ArrayAdapter adapterTT = new ArrayAdapter<>(BuddyProfileActivity.this, small_list_item, trainerType);
                    trainerTypeListView.setAdapter(adapterTT);

                }
                else if(userType.equals(Constants.TRAINEE)){
                    viewStub.setOnInflateListener(new ViewStub.OnInflateListener() {
                        @Override
                        public void onInflate(ViewStub stub, View inflated) {
                            TrainerBuddyProfileBinding binding = DataBindingUtil.bind(inflated);
                            firstName = binding.bPFirstName;
                            lastName = binding.bPLastName;
                            gender = binding.bPGender;
                            introduction = binding.bPIntroduction;
                            city = binding.bPCity;
                            gym = binding.bPGym;
                            price = binding.bPPrice;
                            profPic = binding.bPProfilePic;
                            trainerTypeListView = binding.trainerTypeListView;
                            specialtyListView = binding.specialtyListView;
                        }
                    });
                    viewStub.getViewStub().setLayoutResource(R.layout.trainer_buddy_profile);
                    viewStub.getViewStub().inflate();

                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    gender.setText(user.getGender());
                    introduction.setText(user.getIntroduction());
                    String imageURL = user.getImageURL();
                    Picasso.get()
                            .load(imageURL)
                            .placeholder(R.mipmap.ic_launcher)
                            .fit()
                            .into(profPic);

                    TrainerCriterias trainerCriterias = dSnap.child(Constants.CRITERIAS).getValue(TrainerCriterias.class);
                    city.setText(trainerCriterias.getCity());

                    gym.setText(trainerCriterias.getGym());

                    price.setText(trainerCriterias.getPrice().getCost() + "/" + trainerCriterias.getPrice().getHours() + "lej/hour");

                    trainerType = trainerCriterias.getTrainerType();
                    Log.i("Noemi","TrainerType: " + trainerType.toString());
                    ArrayAdapter<String> adapterTT = new ArrayAdapter<>(BuddyProfileActivity.this, small_list_item, trainerType);
                    trainerTypeListView.setAdapter(adapterTT);

                    specialties = trainerCriterias.getSpecialties();
                    ArrayAdapter<String> adapterS = new ArrayAdapter<>(BuddyProfileActivity.this, small_list_item, specialties);
                    specialtyListView.setAdapter(adapterS);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}