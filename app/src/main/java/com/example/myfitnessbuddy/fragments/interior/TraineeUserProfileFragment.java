package com.example.myfitnessbuddy.fragments.interior;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ViewStubProxy;
import androidx.fragment.app.FragmentManager;

import com.example.myfitnessbuddy.OnUpdateClickedListener;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.FragmentUserProfileTraineeBinding;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.example.myfitnessbuddy.fragments.dialogs.EditDataDialog;
import com.example.myfitnessbuddy.models.TraineeCriterias;
import com.example.myfitnessbuddy.models.User;
import com.example.myfitnessbuddy.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TraineeUserProfileFragment extends BaseFragment<FragmentUserProfileTraineeBinding> implements OnUpdateClickedListener {

    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference currentUserReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private TextView firstName, lastName, birthDate, phoneNumber,gender,introduction,city, goal, nutriNeeded, price;
    private ImageView profPic,editFirstName,editLastName, editCity, editGoal, editIntroduction, editNutriNeeded, editPrice, editTrainerType, editCriteriasList;
    private ListView trainerTypeListView,criteriaListView;
    private User user;
    private List<String> trainerType = new ArrayList<>();
    private List<String> criterias = new ArrayList<>();
    private List<String> cities = new ArrayList<>();
    private List<String> goals = new ArrayList<>();
    private LinearLayout llCity,llGoal,llIntroduction,llNutritionist,llPrice,llCriterias,llTrainerType;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_user_profile_trainee;
    }

    @Override
    protected void initFragmentImpl() {

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId= firebaseAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        currentUserReference = databaseReference.child(Constants.USERS).child(currentUserId);

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
        llCity = binding.llCity;
        llGoal = binding.llGoal;
        llIntroduction = binding.llIntroduction;
        llPrice = binding.llPrice;
        llNutritionist = binding.llNutritionist;
        llTrainerType = binding.llTrainerType;
        llCriterias = binding.llCriteriaList;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.child(Constants.USERS).child(currentUserId).getValue(User.class);

                DataSnapshot dSnap = dataSnapshot.child(Constants.USERS).child(currentUserId);

                firstName.setText(user.getFirstName());
                lastName.setText(user.getLastName());
                birthDate.setText(user.getBirthDate());
                phoneNumber.setText(user.getPhoneNumber());
                gender.setText(user.getGender());

                editFirstName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager dialogFragment = getChildFragmentManager();
                        EditDataDialog dialog = new EditDataDialog(user.getFirstName(),Constants.UPDATE_FIRST_NAME,TraineeUserProfileFragment.this);
                        dialog.show(dialogFragment, "edit dialog");
                    }
                });

                editLastName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager dialogFragment = getChildFragmentManager();
                        EditDataDialog dialog = new EditDataDialog(user.getLastName(),Constants.UPDATE_LAST_NAME,TraineeUserProfileFragment.this);
                        dialog.show(dialogFragment, "edit dialog");
                    }
                });

                if(dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.IMAGE_URL).exists()){
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
                            EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_GOAL, goals,TraineeUserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                    editNutriNeeded.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(nutriNeeded.getText().toString(),Constants.UPDATE_NUTRI_NEEDED,TraineeUserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                    editCriteriasList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_CRITERIA_LIST,criterias,TraineeUserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });


                    profPic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(user.getImageURL(),Constants.UPDATE_PROFILE_PIC,TraineeUserProfileFragment.this);
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
                            EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_CITY, cities,TraineeUserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                    editIntroduction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(user.getIntroduction(),Constants.UPDATE_INTRODUCTION,TraineeUserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                    editPrice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(price.getText().toString(),Constants.UPDATE_PRICE,TraineeUserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                    editTrainerType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_TRAINER_TYPE,trainerType,TraineeUserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });
                }
                else{
                    llCity.setVisibility(View.GONE);
                    llGoal.setVisibility(View.GONE);
                    llTrainerType.setVisibility(View.GONE);
                    llCriterias.setVisibility(View.GONE);
                    llPrice.setVisibility(View.GONE);
                    llIntroduction.setVisibility(View.GONE);
                    llNutritionist.setVisibility(View.GONE);
                    profPic.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static TraineeUserProfileFragment getInstance(){
        return new TraineeUserProfileFragment();
    }


    @Override
    public void firstNameUpdated(String firstName) {
        Log.i("Noemi","First name: "+ firstName);
        currentUserReference.child(Constants.FIRST_NAME).setValue(firstName);
    }

    @Override
    public void lastNameUpdated(String lastName) {
        Log.i("Noemi","Last name: %s"+ lastName);
        currentUserReference.child(Constants.LAST_NAME).setValue(lastName);
    }

    @Override
    public void cityUpdated(String city) {
        Log.i("Noemi","City: %s"+ city);
        currentUserReference.child(Constants.CRITERIAS).child(Constants.CITY).setValue(city);
    }

    @Override
    public void introductionUpdated(String introduction) {
        Log.i("Noemi","Introduction: %s"+ introduction);
        currentUserReference.child(Constants.INTRODUCTION).setValue(introduction);
    }

    @Override
    public void priceUpdated(String hours, String cost) {
        Log.i("Noemi","price: %s / %s"+ cost+" /"+hours);
        currentUserReference.child(Constants.CRITERIAS).child(Constants.PRICE).child(Constants.HOURS).setValue(hours);
        currentUserReference.child(Constants.CRITERIAS).child(Constants.PRICE).child(Constants.COST).setValue(cost);
    }

    @Override
    public void goalUpdated(String goal) {
        Log.i("Noemi","Goal: %s"+ goal);
        currentUserReference.child(Constants.CRITERIAS).child(Constants.GOAL).setValue(goal);

    }

    @Override
    public void trainerTypeUpdated(List<String> trainerTypeList) {
        Log.i("Noemi",trainerTypeList.toString());
        currentUserReference.child(Constants.CRITERIAS).child(Constants.TRAINER_TYPE).setValue(trainerTypeList);
    }

    @Override
    public void specialtiesUpdated(List<String> specialtyList) {
        Log.i("Noemi","Spec: %s"+ specialtyList.toString());
        currentUserReference.child(Constants.CRITERIAS).child(Constants.SPECIALTIES).setValue(specialtyList);
    }

    @Override
    public void nutriNeededUpdated(boolean nutriNeeded) {
        Log.i("Noemi","Nutri: %s"+ nutriNeeded);
        currentUserReference.child(Constants.CRITERIAS).child(Constants.NUTRITIONIST_NEEDED).setValue(nutriNeeded);
    }

    @Override
    public void gymUpdated(String gym) {
        Log.i("Noemi","Gym: %s"+gym);
        currentUserReference.child(Constants.CRITERIAS).child(Constants.GYM).setValue(gym);
    }

    @Override
    public void criteriaUpdated(List<String> criteria) {
        Log.i("Noemi","Spec: %s"+ criteria.toString());
        currentUserReference.child(Constants.CRITERIAS).child(Constants.CRITERIAS).setValue(criteria);
    }

    @Override
    public void profilePicUpdated(String imageUrl, String oldImageUrl) {
        Log.i("Noemi","ImageURL: %s" + imageUrl);
        storageReference = firebaseStorage.getReferenceFromUrl(oldImageUrl);

        //delete the old image from storage...
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.i("Noemi", "onSuccess: deleted file");
                currentUserReference.child(Constants.IMAGE_URL).setValue(imageUrl);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.i("Noemi", "onFailure: did not delete file");
            }
        });
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
