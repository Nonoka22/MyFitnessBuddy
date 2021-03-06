package com.example.myfitnessbuddy.fragments.interior;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.adapters.SuccessStoryAdapter;
import com.example.myfitnessbuddy.databinding.FragmentUserProfileTrainerBinding;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.example.myfitnessbuddy.fragments.dialogs.AddSuccessStoriesDialog;
import com.example.myfitnessbuddy.fragments.dialogs.EditDataDialog;
import com.example.myfitnessbuddy.interfaces.APIService;
import com.example.myfitnessbuddy.models.SuccessStory;
import com.example.myfitnessbuddy.models.TrainerCriterias;
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

public class TrainerUserProfileFragment extends BaseFragment<FragmentUserProfileTrainerBinding> implements APIService.OnUpdateClickedListener, APIService.OnSuccessStoryUploaded {

    private Context mContext;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference currentUserReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private TextView firstName, lastName, birthDate, phoneNumber,gender,introduction,city, price, gym, ssTitle, ssHint;
    private ImageView profPic,editFirstName,editSpecilatyList,editGym,editLastName, editCity, editIntroduction, editPrice, editTrainerType;
    private ListView trainerTypeListView,specialtyListView;
    private Button addSuccessStoryButton;
    private RecyclerView successStoryRecyclerView;
    private SuccessStoryAdapter sSAdapter;
    private LinearLayout llCity,llGym,llIntroduction,llPrice,llSpecialtes,llTrainerType;

    private User user;
    private List<String> trainerType = new ArrayList<>();
    private List<SuccessStory> successStories = new ArrayList<>();
    private SuccessStory successStory = new SuccessStory();
    private List<String> specialties = new ArrayList<>();
    private List<String> allSpecialties = new ArrayList<>();
    private List<String> gyms = new ArrayList<>();
    private List<String> cities = new ArrayList<>();

    public TrainerUserProfileFragment() {
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_user_profile_trainer;
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
        gym = binding.uPGym;
        price = binding.uPPrice;
        profPic = binding.uPProfilePic;
        ssTitle = binding.uPssStoriesTitle;
        ssHint = binding.uPssStoriesHint;
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
        successStoryRecyclerView = binding.successStoryRecyclerView;
        successStoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addSuccessStoryButton = binding.addSuccessStoryButton;
        llCity =binding.llCity;
        llGym = binding.llGym;
        llIntroduction = binding.llIntroduction;
        llPrice = binding.llPrice;
        llSpecialtes = binding.llSpecialtyList;
        llTrainerType = binding.llTrainerType;

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
                        EditDataDialog dialog = new EditDataDialog(user.getFirstName(),Constants.UPDATE_FIRST_NAME,TrainerUserProfileFragment.this);
                        dialog.show(dialogFragment, "edit dialog");
                    }
                });

                editLastName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager dialogFragment = getChildFragmentManager();
                        EditDataDialog dialog = new EditDataDialog(user.getLastName(),Constants.UPDATE_LAST_NAME,TrainerUserProfileFragment.this);
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

                    TrainerCriterias trainerCriterias = dSnap.child(Constants.CRITERIAS).getValue(TrainerCriterias.class);
                    city.setText(trainerCriterias.getCity());

                    gym.setText(trainerCriterias.getGym());

                    price.setText(trainerCriterias.getPrice().getCost() + "/" + trainerCriterias.getPrice().getHours() + "lej/hour");

                    trainerType = trainerCriterias.getTrainerType();
                    Log.i("Noemi","TrainerType: " + trainerType.toString());
                    ArrayAdapter<String> adapterTT = new ArrayAdapter<>(mContext, R.layout.small_list_item, trainerType);
                    trainerTypeListView.setAdapter(adapterTT);

                    specialties = trainerCriterias.getSpecialties();
                    ArrayAdapter<String> adapterS = new ArrayAdapter<>(mContext, R.layout.small_list_item, specialties);
                    specialtyListView.setAdapter(adapterS);


                    successStories.clear();
                    for(DataSnapshot snapshot : dataSnapshot.child(Constants.STORIES).child(currentUserId).getChildren()){
                        //Log.i("Noemi", String.valueOf(snapshot.getValue()));
                        successStory = snapshot.getValue(SuccessStory.class);
                        successStories.add(successStory);
                    }

                    if(successStories.isEmpty()){
                        ssHint.setVisibility(View.GONE);
                        ssTitle.setVisibility(View.GONE);
                    }

                    sSAdapter = new SuccessStoryAdapter(successStories);
                    successStoryRecyclerView.setAdapter(sSAdapter);

                    for(DataSnapshot snapshot :dataSnapshot.child(Constants.GYMS).child(trainerCriterias.getCity()).getChildren()){
                        gyms.add(snapshot.getValue().toString());
                    }

                    editGym.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_GYM,gyms,TrainerUserProfileFragment.this);
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
                            EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_SPECIALTY_LIST,specialties,allSpecialties,TrainerUserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });


                    profPic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(user.getImageURL(),Constants.UPDATE_PROFILE_PIC,TrainerUserProfileFragment.this);
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
                            EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_CITY, cities,TrainerUserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                    editIntroduction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(user.getIntroduction(),Constants.UPDATE_INTRODUCTION,TrainerUserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                    editPrice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(price.getText().toString(),Constants.UPDATE_PRICE,TrainerUserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });

                    editTrainerType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragmentManager dialogFragment = getChildFragmentManager();
                            EditDataDialog dialog = new EditDataDialog(Constants.UPDATE_TRAINER_TYPE,trainerType,TrainerUserProfileFragment.this);
                            dialog.show(dialogFragment, "edit dialog");
                        }
                    });
                }
                else {
                    llCity.setVisibility(View.GONE);
                    llGym.setVisibility(View.GONE);
                    llIntroduction.setVisibility(View.GONE);
                    llPrice.setVisibility(View.GONE);
                    llSpecialtes.setVisibility(View.GONE);
                    llTrainerType.setVisibility(View.GONE);
                    profPic.setVisibility(View.GONE);
                    addSuccessStoryButton.setVisibility(View.GONE);
                    ssTitle.setVisibility(View.GONE);
                    ssHint.setVisibility(View.GONE);
                }

        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addSuccessStoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager dialogFragment = getChildFragmentManager();
                AddSuccessStoriesDialog dialog = new AddSuccessStoriesDialog(TrainerUserProfileFragment.this);
                dialog.show(dialogFragment, "success dialog");
            }
        });
    }

    public static TrainerUserProfileFragment getInstance(){
        return new TrainerUserProfileFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
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

        getChildFragmentManager().getFragments().get(0).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void successStoryUploaded(SuccessStory successStory) {
        databaseReference.child(Constants.STORIES).child(currentUserId).push().setValue(successStory);
    }

}
