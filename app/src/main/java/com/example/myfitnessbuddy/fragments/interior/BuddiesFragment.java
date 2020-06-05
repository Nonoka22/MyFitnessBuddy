package com.example.myfitnessbuddy.fragments.interior;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.Constants;
import com.example.myfitnessbuddy.PubNubUtils;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.adapters.BuddyAdapter;
import com.example.myfitnessbuddy.databinding.FragmentBuddiesBinding;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.example.myfitnessbuddy.models.Match;
import com.example.myfitnessbuddy.models.MatchedBuddy;
import com.example.myfitnessbuddy.models.Matcher;
import com.example.myfitnessbuddy.models.Price;
import com.example.myfitnessbuddy.models.TraineeCriterias;
import com.example.myfitnessbuddy.models.TrainerCriterias;
import com.example.myfitnessbuddy.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuddiesFragment extends BaseFragment<FragmentBuddiesBinding> {

    private String userType;
    private Button findBuddies;
    private String location;
    private String trainerId;
    private ArrayList<String> trainerType = new ArrayList<>();
    private Price price = new Price();
    private String goal;
    private boolean needsNutritionist;
    private DatabaseReference databaseReference;
    private ArrayList<String> criteriasList = new ArrayList<>();
    private Matcher matcher;
    private boolean matchedWithTrainer;
    private RecyclerView recyclerView;
    private List<MatchedBuddy> matchedBuddyList;
    private List<String> matchedBuddyIds;
    private BuddyAdapter adapter;
    private User user = new User();
    private User trainer = new User();
    private TraineeCriterias traineeCriterias = new TraineeCriterias();
    private TrainerCriterias trainerCriterias = new TrainerCriterias();

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_buddies;
    }

    @Override
    protected void initFragmentImpl() {
        recyclerView = binding.recyclerViewBuddies;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        matchedBuddyList = new ArrayList<>();
        matchedBuddyIds = new ArrayList<>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                userType = dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.USER_TYPE).getValue().toString();

                if(userType.equals(Constants.TRAINEE)){

                    Log.i("Noemi","Trainee ");

                    //get the trainerIds, which the user already matched with for the listing...
                    for(DataSnapshot dataSnap : dataSnapshot.child(Constants.MATCHES).getChildren()) {
                        if (dataSnap.child(Constants.TRAINEE_ID).getValue().toString().equals(currentUserId)) {
                            matchedBuddyIds.add(dataSnap.child(Constants.TRAINER_ID).getValue().toString());
                            Log.i("Noemi",matchedBuddyIds.toString());
                        }
                    }

                    findBuddies.setVisibility(View.VISIBLE);

                    findBuddies.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //make sure, the criteriasList is empty
                            criteriasList.clear();

                            //get the criteriasList which shows the user's needs
                            for(DataSnapshot snap0 : dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.CRITERIAS).getChildren()){
                                criteriasList.add(snap0.getValue().toString());
                            }

                            //get the location of the user, where he/she wants to train...
                             location = dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.CITY).getValue().toString();

                            //check in TrainerLocations if, the there are trainers and if they match the user's criterias
                            for(DataSnapshot snapshot : dataSnapshot.child(Constants.TRAINER_LOCATIONS).child(location).getChildren()){//location

                                //initialize matcher and boolean match checker
                                matcher = new Matcher();
                                matchedWithTrainer = false;

                                //get the current trainer's id
                                trainerId = snapshot.getValue().toString();

                                //check if the user already matched with the current trainer
                                for(DataSnapshot dSnap : dataSnapshot.child(Constants.MATCHES).getChildren()){
                                    if(dSnap.child(Constants.TRAINEE_ID).getValue().toString().equals(currentUserId) &&
                                            dSnap.child(Constants.TRAINER_ID).getValue().toString().equals(trainerId)){
                                        matchedWithTrainer = true;
                                    }
                                }

                                //if the user hasn't matched with the trainer
                                if(!matchedWithTrainer){

                                    //for each each criteria type check if there is a match
                                    for(int i = 0; i < 4; i++ ){
                                        switch (criteriasList.get(i)){//criteriasList.get(i)
                                            case Constants.GOAL_CRITERIA:
                                                //Log.i("Noemi","Goal crit.");
                                                //the goal the user selected..
                                                goal = dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.GOAL).getValue().toString();

                                                //check if any specialty that the trainer has, exists in the selected goal's node
                                                specialtyloop:
                                                for(DataSnapshot snap1 : dataSnapshot.child(Constants.USERS).child(trainerId).child(Constants.CRITERIAS).child(Constants.SPECIALTIES).getChildren()){
                                                    for(DataSnapshot snap2 : dataSnapshot.child(Constants.GOALS).child(goal).getChildren()){
                                                        //if it finds one specialty, that matches, than it is a match
                                                        if(snap2.getValue().toString().equals(snap1.getValue().toString())){
                                                            setMatch(i);
                                                            break specialtyloop;
                                                        }
                                                    }
                                                }


                                                break;

                                            case Constants.PRICE_CRITERIA:
                                                //Log.i("Noemi","Price crit.");
                                                //the hour and cost the user selected
                                                float traineeHour = Float.parseFloat(dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.PRICE).child(Constants.HOURS).getValue().toString());
                                                float traineeX = (float) 1/traineeHour;
                                                float traineeCost = traineeX * Float.parseFloat(dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.PRICE).child(Constants.COST).getValue().toString());

                                                //the hour and cost the trainer selected
                                                float trainerHour = Float.parseFloat(dataSnapshot.child(Constants.USERS).child(trainerId).child(Constants.CRITERIAS).child(Constants.PRICE).child(Constants.HOURS).getValue().toString());
                                                float trainerX = (float) 1/traineeHour;
                                                float trainerCost = traineeX * Float.parseFloat(dataSnapshot.child(Constants.USERS).child(trainerId).child(Constants.CRITERIAS).child(Constants.PRICE).child(Constants.COST).getValue().toString());

//
                                                //if the trainer is cheaper or equal or max 10 leis more expensive than the user's price it is a match
                                                if(trainerCost <= (traineeCost + 10)){
                                                   // Log.i("Noemi","Price crit match");
                                                    setMatch(i);
                                                }
                                                break;
                                            case Constants.NUTRITIONIST_CRITERIA:
                                                //Log.i("Noemi","Nutritionist crit.");
                                                //check if user needs nutritionist or not..
                                                needsNutritionist = (boolean) dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.NUTRITIONIST_NEEDED).getValue();

                                                if(needsNutritionist){//needsNutritionist

                                                    //check if Nutritionist is in the trainer's specialty list...
                                                    for(DataSnapshot snap3 : dataSnapshot.child(Constants.USERS).child(trainerId).child(Constants.CRITERIAS).child(Constants.SPECIALTIES).getChildren()){
                                                        if(snap3.getValue().toString().equals(Constants.NUTRITIONIST)){
                                                            setMatch(i);
                                                            break;
                                                        }
                                                    }

                                                }
                                                else{
                                                    setMatch(i); // probably will be changed
                                                }
                                                break;

                                            case Constants.TRAINER_TYPE_CRITERIA:
                                               // Log.i("Noemi","TrainerType crit.");

                                                 trainerType.clear();
                                                //get what the user selected..
                                                for(DataSnapshot snap4 : dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.TRAINER_TYPE).getChildren()){
                                                    trainerType.add(snap4.getValue().toString());
                                                }

                                                //if user selected both types, than it is already a match...
                                                if(trainerType.size() == 2){//trainerType.size() == 2
                                                    setMatch(i);
                                                }
                                                else { //check if the trainer is the specific type
                                                    for(DataSnapshot snap5 : dataSnapshot.child(Constants.USERS).child(trainerId).child(Constants.CRITERIAS).child(Constants.TRAINER_TYPE).getChildren()){
                                                        if(snap5.getValue().toString().equals(trainerType.get(0))){
                                                            setMatch(i);
                                                        }
                                                    }
                                                }
                                                break;
                                        }
                                    }

                                }
                                else{
                                    //Log.i("Noemi","They already matched...");
                                }

                                if(matcher.isFirstCriteria() && matcher.isSecondCriteria()){
                                    //save the match into the Matches table
                                    Match match = new Match(currentUserId,trainerId,matcher.getMatchCounter());
                                    databaseReference.child(Constants.MATCHES).push().setValue(match);
                                    adapter.notifyDataSetChanged();

                                    //create PubNub channel between the two users
                                    PubNubUtils.setPubNubChannel(currentUserId + trainerId,"Chatroom");
                                    //add message listener first!
                                    PubNubUtils.addMessageListener();
                                    //subscribe the user to the channel
                                    PubNubUtils.subscribeToChannel(currentUserId + trainerId);
                                }
                                else {
                                   // Log.i("Noemi","Matcher is not set...");
                                }
                            }
                        }
                    });
                }
                else {
                    //The user is a trainer..
                    for(DataSnapshot dataSnap : dataSnapshot.child(Constants.MATCHES).getChildren()) {
                        if (dataSnap.child(Constants.TRAINER_ID).getValue().toString().equals(currentUserId)) {
                            matchedBuddyIds.add(dataSnap.child(Constants.TRAINEE_ID).getValue().toString());
                        }
                    }
                }

               // Log.i("Noemi","buddy list : " + matchedBuddyIds.toString());

                for(String s : matchedBuddyIds){
                    //for each one of them, check if the user has subscribed yet to their channel


                    MatchedBuddy matchedBuddy = new MatchedBuddy();
                    //User matchedUser = new User();
                    User matchedUser = dataSnapshot.child(Constants.USERS).child(s).getValue(User.class);
                   // Log.i("Noemi","MatchedUser: " + matchedUser.getFirstName());
                    matchedBuddy.setFirstName(matchedUser.getFirstName());
                    matchedBuddy.setLastName(matchedUser.getLastName());
                    matchedBuddy.setId(s);
                    matchedBuddy.setImageUrl(matchedUser.getImageURL());
                    matchedBuddyList.add(matchedBuddy);
                   // Log.i("Noemi","URL: " + matchedUser.getImageURL());
                }

                //Log.i("Noemi",matchedBuddyList.toString());
                adapter = new BuddyAdapter(matchedBuddyList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static BuddiesFragment getInstance(){
        return new BuddiesFragment();
    }

    private void setMatch(int i){
        if(i == 0){
            //first match completed
            matcher.setFirstCriteria(true);
        }
        else if (i == 1){
            matcher.setSecondCriteria(true);
        }
        else {
            matcher.setMatchCounter(matcher.getMatchCounter()+1);
        }
    }
}
