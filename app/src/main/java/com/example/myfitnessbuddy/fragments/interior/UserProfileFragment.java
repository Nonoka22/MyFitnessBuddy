package com.example.myfitnessbuddy.fragments.interior;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.example.myfitnessbuddy.OnUpdateClickedListener;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.FragmentUserProfileBinding;
import com.example.myfitnessbuddy.databinding.FragmentUserProfileTraineeBinding;
import com.example.myfitnessbuddy.databinding.FragmentUserProfileTrainerBinding;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.example.myfitnessbuddy.fragments.dialogs.EditDataDialog;
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

public class UserProfileFragment extends BaseFragment<FragmentUserProfileBinding> implements OnUpdateClickedListener {

    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private TextView firstName, lastName, birthDate, phoneNumber,gender,introduction,city, goal, nutriNeeded, price, gym;
    private ImageView profPic,editFirstName,editSpecilatyList,editGym,editLastName, editCity, editGoal, editIntroduction, editNutriNeeded, editPrice, editTrainerType, editCriteriasList;
    private ListView trainerTypeListView,criteriaListView,specialtyListView;
    private User user;
    private List<String> trainerType = new ArrayList<>();
    private List<String> criterias = new ArrayList<>();
    private List<String> specialties = new ArrayList<>();
    private List<String> allSpecialties = new ArrayList<>();
    private List<String> gyms = new ArrayList<>();
    private List<String> cities = new ArrayList<>();
    private List<String> goals = new ArrayList<>();

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_user_profile;
    }

    @Override
    protected void initFragmentImpl() {
        //This will be the same for both users

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId= firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                user = dataSnapshot.child(Constants.USERS).child(currentUserId).getValue(User.class);

                DataSnapshot dSnap = dataSnapshot.child(Constants.USERS).child(currentUserId);

                if(user.getUserType().equals(Constants.TRAINEE)){
                    binding.viewStubUserProfile.setOnInflateListener(new ViewStub.OnInflateListener() {
                        @Override
                        public void onInflate(ViewStub stub, View inflated) {
                            FragmentUserProfileTraineeBinding binding = DataBindingUtil.bind(inflated);
                            firstName = binding.uPFirstName;
                            lastName = binding.uPLastName;
                            birthDate = binding.uPBirthDate;
                            phoneNumber = binding.uPPhoneNumber;
                            gender = binding.uPGender;
                            introduction = binding.uPIntroduction;
                            city = binding.uPCity;
                            goal = binding.uPGoal;
                            nutriNeeded = binding.uPNutritionist;
                            price = binding.uPPrice;
                            profPic = binding.uPProfilePic;
                            editFirstName = binding.editFirstName;
                            editLastName = binding.editLastName;
                            editCity = binding.editCity;
                            editGoal = binding.editGoal;
                            editPrice = binding.editPrice;
                            editIntroduction = binding.editIntroduction;
                            editCriteriasList = binding.editCriteriaList;
                            editTrainerType = binding.editTrainerType;
                            editNutriNeeded = binding.editNutritionist;
                            trainerTypeListView = binding.trainerTypeListView;
                            criteriaListView = binding.criteriaListView;
                        }
                    });
                    binding.viewStubUserProfile.getViewStub().setLayoutResource(R.layout.fragment_user_profile_trainee);
                    binding.viewStubUserProfile.getViewStub().inflate();

                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    birthDate.setText(user.getBirthDate());
                    phoneNumber.setText(user.getPhoneNumber());
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
                    ArrayAdapter<String> adapterTT = new ArrayAdapter<>(getContext(), R.layout.small_list_item, trainerType);
                    trainerTypeListView.setAdapter(adapterTT);

                    criterias = traineeCriterias.getCriterias();
                    ArrayAdapter<String> adapterC = new ArrayAdapter<>(getContext(), R.layout.small_list_item, criterias);
                    criteriaListView.setAdapter(adapterC);

                    for(DataSnapshot snapshot: dataSnapshot.child(Constants.GOALS).getChildren()){
                        goals.add(snapshot.getKey());
                    }

                    editGoal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_GOAL, goals,UserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                    editNutriNeeded.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(nutriNeeded.getText().toString(),Constants.UPDATE_NUTRI_NEEDED,UserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                    editCriteriasList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_CRITERIA_LIST,criterias,UserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                }
                else if(user.getUserType().equals(Constants.TRAINER)){
                    binding.viewStubUserProfile.setOnInflateListener(new ViewStub.OnInflateListener() {
                        @Override
                        public void onInflate(ViewStub stub, View inflated) {
                            FragmentUserProfileTrainerBinding binding = DataBindingUtil.bind(inflated);
                            firstName = binding.uPFirstName;
                            lastName = binding.uPLastName;
                            birthDate = binding.uPBirthDate;
                            phoneNumber = binding.uPPhoneNumber;
                            gender = binding.uPGender;
                            introduction = binding.uPIntroduction;
                            city = binding.uPCity;
                            gym = binding.uPGym;
                            price = binding.uPPrice;
                            profPic = binding.uPProfilePic;
                            editFirstName = binding.editFirstName;
                            editLastName = binding.editLastName;
                            editCity = binding.editCity;
                            editGym = binding.editGym;
                            editPrice = binding.editPrice;
                            editIntroduction = binding.editIntroduction;
                            editSpecilatyList = binding.editSpecialtyList;
                            editTrainerType = binding.editTrainerType;
                            trainerTypeListView = binding.trainerTypeListView;
                            specialtyListView = binding.specialtyListView;
                        }
                    });
                    binding.viewStubUserProfile.getViewStub().setLayoutResource(R.layout.fragment_user_profile_trainer);
                    binding.viewStubUserProfile.getViewStub().inflate();

                    firstName.setText(user.getFirstName());
                    lastName.setText(user.getLastName());
                    birthDate.setText(user.getBirthDate());
                    phoneNumber.setText(user.getPhoneNumber());
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
                    ArrayAdapter<String> adapterTT = new ArrayAdapter<>(getContext(), R.layout.small_list_item, trainerType);
                    trainerTypeListView.setAdapter(adapterTT);

                    specialties = trainerCriterias.getSpecialties();
                    ArrayAdapter<String> adapterS = new ArrayAdapter<>(getContext(), R.layout.small_list_item, specialties);
                    specialtyListView.setAdapter(adapterS);

                    //bring this to UserProfileFragment
                    for(DataSnapshot snapshot :dataSnapshot.child(Constants.GYMS).child(trainerCriterias.getCity()).getChildren()){
                        gyms.add(snapshot.getValue().toString());
                    }

                    editGym.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_GYM,gyms,UserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                    for(DataSnapshot snapshot: dataSnapshot.child(Constants.GOALS).getChildren()){
                        for(DataSnapshot snap : snapshot.getChildren()){
                            if(!allSpecialties.contains(snap.getValue().toString())){
                                allSpecialties.add(snap.getValue().toString());
                            }
                        }
                    }

                    editSpecilatyList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_SPECIALTY_LIST,specialties,allSpecialties,UserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                }

                editFirstName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager dialogFragment = getChildFragmentManager();
                        EditDataDialog dialog = new EditDataDialog(user.getFirstName(),Constants.UPDATE_FIRST_NAME,UserProfileFragment.this);
                        dialog.show(dialogFragment, "edit dialog");
                    }
                });

                editLastName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager dialogFragment = getChildFragmentManager();
                        EditDataDialog dialog = new EditDataDialog(user.getLastName(),Constants.UPDATE_LAST_NAME,UserProfileFragment.this);
                        dialog.show(dialogFragment, "edit dialog");
                    }
                });

                profPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager dialogFragment = getChildFragmentManager();
                        EditDataDialog dialog = new EditDataDialog(user.getImageURL(),Constants.UPDATE_PROFILE_PIC,UserProfileFragment.this);
                        dialog.show(dialogFragment, "edit prof pic dialog");
                    }
                });


                for(DataSnapshot snapshot: dataSnapshot.child(Constants.GYMS).getChildren()){
                    cities.add(snapshot.getKey());
                }

                editCity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager dialogFragment = getChildFragmentManager();
                        EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_CITY, cities,UserProfileFragment.this);
                        dialog.show(dialogFragment, "edit dialog");
                    }
                });

                editIntroduction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager dialogFragment = getChildFragmentManager();
                        EditDataDialog dialog = new EditDataDialog(user.getIntroduction(),Constants.UPDATE_INTRODUCTION,UserProfileFragment.this);
                        dialog.show(dialogFragment, "edit dialog");
                    }
                });

                editPrice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager dialogFragment = getChildFragmentManager();
                        EditDataDialog dialog = new EditDataDialog(price.getText().toString(),Constants.UPDATE_PRICE,UserProfileFragment.this);
                        dialog.show(dialogFragment, "edit dialog");
                    }
                });

                editTrainerType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager dialogFragment = getChildFragmentManager();
                        EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_TRAINER_TYPE,trainerType,UserProfileFragment.this);
                        dialog.show(dialogFragment, "edit dialog");
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static UserProfileFragment getInstance(){
        return new UserProfileFragment();
    }

    @Override
    public void firstNameUpdated(String firstName) {

        Log.i("Noemi","First name: "+ firstName);
    }

    @Override
    public void lastNameUpdated(String lastName) {
        Log.i("Noemi","Last name: %s"+ lastName);
    }

    @Override
    public void cityUpdated(String city) {
        Log.i("Noemi","City: %s"+ city);
    }

    @Override
    public void introductionUpdated(String introduction) {
        Log.i("Noemi","Introduction: %s"+ introduction);
    }

    @Override
    public void priceUpdated(String hours, String cost) {
        Log.i("Noemi","price: %s / %s"+ cost+" /"+hours);
    }

    @Override
    public void goalUpdated(String goal) {
        Log.i("Noemi","Goal: %s"+ goal);
    }

    @Override
    public void trainerTypeUpdated(List<String> trainerTypeList) {
        Log.i("Noemi",trainerTypeList.toString());

    }

    @Override
    public void specialtiesUpdated(List<String> specialtyList) {
        Log.i("Noemi","Spec: %s"+ specialtyList.toString());
    }

    @Override
    public void nutriNeededUpdated(boolean nutriNeeded) {
        Log.i("Noemi","Nutri: %s"+ nutriNeeded);
    }

    @Override
    public void gymUpdated(String gym) {
        Log.i("Noemi","Gym: %s"+gym);
    }

    @Override
    public void criteriaUpdated(List<String> criteria) {
        Log.i("Noemi","Spec: %s"+ criteria.toString());
    }

    @Override
    public void profilePicUpdated(String imageUrl) {
        Log.i("Noemi","ImageURL: %s" + imageUrl);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //call super
        Log.i("Noemi","userprof");
        super.onActivityResult(requestCode, resultCode, data);

        EditDataDialog fragment = (EditDataDialog) getChildFragmentManager().getFragments().get(0);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
