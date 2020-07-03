package com.example.myfitnessbuddy.fragments.interior;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.interfaces.OnBuddyClickedListener;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.BuddyProfileActivity;
import com.example.myfitnessbuddy.activities.ChatActivity;
import com.example.myfitnessbuddy.adapters.BuddyAdapter;
import com.example.myfitnessbuddy.databinding.FragmentBuddiesBinding;
import com.example.myfitnessbuddy.events.DeleteBuddyEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.example.myfitnessbuddy.fragments.dialogs.DeleteBuddyDialog;
import com.example.myfitnessbuddy.fragments.dialogs.MessageDialog;
import com.example.myfitnessbuddy.models.BuddyRelationshipStatus;
import com.example.myfitnessbuddy.models.Match;
import com.example.myfitnessbuddy.models.MatchedBuddy;
import com.example.myfitnessbuddy.models.Matcher;
import com.example.myfitnessbuddy.models.Price;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class BuddiesFragment extends BaseFragment<FragmentBuddiesBinding> implements OnBuddyClickedListener {

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
    private TextView emptyBuddies;
    private List<MatchedBuddy> matchedBuddyList;
   // private List<String> matchedBuddyIds;
    private BuddyAdapter adapter;
    private User user = new User();
    private User trainer = new User();
    private TraineeCriterias traineeCriterias = new TraineeCriterias();
    private TrainerCriterias trainerCriterias = new TrainerCriterias();
    private MatchedBuddy matchedBuddy;
    private User matchedUser;
    private List<BuddyRelationshipStatus> buddies;
    private List<BuddyRelationshipStatus> activeBuddies;
    private MatchedBuddy buddy;
    private String currentUserId;
    private int matchCounter;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_buddies;
    }

    @Override
    protected void initFragmentImpl() {

        recyclerView = binding.recyclerViewBuddies;
        emptyBuddies = binding.emptyBuddiesTV;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        matchedBuddyList = new ArrayList<>();
      //  matchedBuddyIds = new ArrayList<>();
        findBuddies = binding.findBuddiesButton;
        buddies = new ArrayList<>();
        activeBuddies = new ArrayList<>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userType = dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.USER_TYPE).getValue().toString();

               // matchedBuddyIds.clear();
                buddies.clear();
                activeBuddies.clear();

                if(userType.equals(Constants.TRAINEE)){

                    findBuddies.setVisibility(View.VISIBLE);
                    if(!dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.INTRODUCTION).exists()){
                        findBuddies.setEnabled(false);
                    }

                   // Log.i("Noemi","Trainee ");

                    //get the trainerIds, which the user already matched with for the listing...
                    for(DataSnapshot dataSnap : dataSnapshot.child(Constants.MATCHES).getChildren()) {
                        String relStatus = dataSnap.child(Constants.RELATIONSHIP_STATUS).getValue().toString();
                        if (dataSnap.child(Constants.TRAINEE_ID).getValue().toString().equals(currentUserId)) { //this makes it match multiple time, must change

                            //adding id and status as well:
                            BuddyRelationshipStatus brs = new BuddyRelationshipStatus();
                            brs.setBuddyId(dataSnap.child(Constants.TRAINER_ID).getValue().toString());
                            brs.setStatus(dataSnap.child(Constants.RELATIONSHIP_STATUS).getValue().toString());

                           // Log.i("Noemi","buddy: " + brs.getBuddyId());
                            buddies.add(brs);

                            if(!relStatus.equals(Constants.DECLINED) && !relStatus.equals(Constants.DELETED_STATUS)
                            && !relStatus.equals(Constants.REMOVED_BY_TRAINEE_STATUS) && !relStatus.equals(Constants.REMOVED_BY_TRAINER_STATUS)){
                                activeBuddies.add(brs);
                            }

                        }
                        else if(dataSnap.child(Constants.RELATIONSHIP_STATUS).equals(Constants.DECLINED)){

                        }
                    }

                    findBuddies.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            matchCounter = 0;
                           // Log.i("Noemi","Find buddies button clicked");
                            //make sure, the criteriasList is empty
                            criteriasList.clear();

                            //get the criteriasList which shows the user's needs
                            for(DataSnapshot snap0 : dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.CRITERIAS).getChildren()){
                                criteriasList.add(snap0.getValue().toString());
                            }

                          //  Log.i("Noemi",criteriasList.toString());

                            //get the location of the user, where he/she wants to train...
                            location = dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.CITY).getValue().toString();
                           // Log.i("Noemi",location);


                            //check in TrainerLocations if, the there are trainers and if they match the user's criteria
                            for(DataSnapshot snapshot : dataSnapshot.child(Constants.TRAINER_LOCATIONS).child(location).getChildren()){//location

                                //initialize matcher and boolean match checker
                                matcher = new Matcher();
                                matchedWithTrainer = false;

                                //get the current trainer's id
                                trainerId = snapshot.getValue().toString();
                                Log.i("Noemi",trainerId);

                                //check if the user already matched with the current trainer
                                for(BuddyRelationshipStatus s : buddies){
                                    if(s.getBuddyId().equals(trainerId)){
                                        matchedWithTrainer = true;
                                        break;
                                    }
                                }

                                //if the user hasn't matched with the trainer
                                if(!matchedWithTrainer){

                                    //for each each criteria type check if there is a match
                                    for(int i = 0; i < 4; i++ ){
                                        switch (criteriasList.get(i)){//criteriasList.get(i)
                                            case Constants.GOAL_CRITERIA:
                                                Log.i("Noemi","Goal crit.");
                                                //the goal the user selected..
                                                goal = dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.GOAL).getValue().toString();

                                                //check if any specialty that the trainer has, exists in the selected goal's node
                                                specialtyloop:
                                                for(DataSnapshot snap1 : dataSnapshot.child(Constants.USERS).child(trainerId).child(Constants.CRITERIAS).child(Constants.SPECIALTIES).getChildren()){
                                                    for(DataSnapshot snap2 : dataSnapshot.child(Constants.GOALS).child(goal).getChildren()){
                                                        //if it finds one specialty, that matches, than it is a match
                                                        if(snap2.getValue().toString().equals(snap1.getValue().toString())){
                                                            Log.i("Noemi", "Goal crit match");
                                                            setMatch(i);
                                                            break specialtyloop;
                                                        }
                                                    }
                                                }

                                                break;

                                            case Constants.PRICE_CRITERIA:
                                                Log.i("Noemi","Price crit.");
                                                //the hour and cost the user selected
                                                float traineeHour = Float.parseFloat(dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.PRICE).child(Constants.HOURS).getValue().toString());
                                                float traineeX = (float) 1/traineeHour;
                                                float traineeCost = traineeX * Float.parseFloat(dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.PRICE).child(Constants.COST).getValue().toString());

                                                //the hour and cost the trainer selected
                                                Log.i("Noemi",dataSnapshot.child(Constants.USERS).child(trainerId).getValue(String.class));
                                                float trainerHour = Float.parseFloat(dataSnapshot.child(Constants.USERS).child(trainerId).child(Constants.CRITERIAS).child(Constants.PRICE).child(Constants.HOURS).getValue().toString());
                                                float trainerX = (float) 1/trainerHour;
                                                float trainerCost = trainerX * Float.parseFloat(dataSnapshot.child(Constants.USERS).child(trainerId).child(Constants.CRITERIAS).child(Constants.PRICE).child(Constants.COST).getValue().toString());

//
                                                //if the trainer is cheaper or equal or max 10 leis more expensive than the user's price it is a match
                                                if(trainerCost <= (traineeCost + 10)){
                                                    Log.i("Noemi","Price crit match");
                                                    setMatch(i);
                                                }
                                                break;
                                            case Constants.NUTRITIONIST_CRITERIA:
                                                Log.i("Noemi","Nutritionist crit.");
                                                //check if user needs nutritionist or not..
                                                Log.i("Noemi",dataSnapshot.toString());
                                                needsNutritionist = (boolean) dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.NUTRITIONIST_NEEDED).getValue();

                                                if(needsNutritionist){//needsNutritionist

                                                    //check if Nutritionist is in the trainer's specialty list...
                                                    for(DataSnapshot snap3 : dataSnapshot.child(Constants.USERS).child(trainerId).child(Constants.CRITERIAS).child(Constants.SPECIALTIES).getChildren()){
                                                        if(snap3.getValue().toString().equals(Constants.NUTRITIONIST)){
                                                            Log.i("Noemi","Nutri crit match");
                                                            setMatch(i);
                                                            break;
                                                        }
                                                    }

                                                }
                                                else{
                                                    Log.i("Noemi","Nutri not needed crit match");
                                                    setMatch(i); // probably will be changed
                                                }
                                                break;

                                            case Constants.TRAINER_TYPE_CRITERIA:
                                                Log.i("Noemi","TrainerType crit.");

                                                trainerType.clear();
                                                //get what the user selected..
                                                for(DataSnapshot snap4 : dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.CRITERIAS).child(Constants.TRAINER_TYPE).getChildren()){
                                                    trainerType.add(snap4.getValue().toString());
                                                }

                                                //if user selected both types, than it is already a match...
                                                if(trainerType.size() == 2){//trainerType.size() == 2
                                                    Log.i("Noemi","TrainerType default crit match");
                                                    setMatch(i);
                                                }
                                                else { //check if the trainer is the specific type
                                                    for(DataSnapshot snap5 : dataSnapshot.child(Constants.USERS).child(trainerId).child(Constants.CRITERIAS).child(Constants.TRAINER_TYPE).getChildren()){
                                                        if(snap5.getValue().toString().equals(trainerType.get(0))){
                                                            Log.i("Noemi","TrainerType crit match");
                                                            setMatch(i);
                                                        }
                                                    }
                                                }
                                                break;
                                        }
                                    }

                                }
                                else{
                                    Log.i("Noemi","They already matched...");
                                }

                                if(matcher != null){
                                    if(matcher.isFirstCriteria() && matcher.isSecondCriteria()){
                                        ++matchCounter;

                                        //save the match into the Matches table
                                        Match match = new Match(currentUserId,trainerId,matcher.getMatchCounter(),Constants.NOT_ACCEPTED_STATUS);
                                        databaseReference.child(Constants.MATCHES).push().setValue(match);
                                        Log.i("Noemi","SAved to database: " + trainerId);

                                        //send notification to Trainer:
                                        String trainerToken = dataSnapshot.child(Constants.TOKENS).child(trainerId).child(Constants.TOKEN_NODE).getValue().toString();
                                        String userToken = dataSnapshot.child(Constants.TOKENS).child(currentUserId).child(Constants.TOKEN_NODE).getValue().toString();
                                        String trainerName = dataSnapshot.child(Constants.USERS).child(trainerId).child(Constants.FIRST_NAME).getValue().toString();
                                        String userName = dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.FIRST_NAME).getValue().toString();
                                        sendNotifications(trainerToken,Constants.MATCH_NOTIF_TITLE,Constants.MATCH_NOTIF_TRAINER_MESSAGE + userName);
                                        //this one is not sent
                                        sendNotifications(userToken,Constants.MATCH_NOTIF_TITLE,Constants.MATCH_NOTIF_TRAINEE_MESSAGE + trainerName);
                                    }
                                    else {
                                        Log.i("Noemi","Matcher is not set...");
                                    }
                                }
                            }
                            if(matchCounter == 0){
                                //there were no matches
                                FragmentManager dialogFragment = getFragmentManager();
                                MessageDialog messageDialog = new MessageDialog("Sorry..","We have not found new buddies.");
                                messageDialog.show(dialogFragment,"dialog");
                            }
                        }
                    });
                }
                else {
                    //The user is a trainer..
                    for(DataSnapshot dataSnap : dataSnapshot.child(Constants.MATCHES).getChildren()) {
                        String relStatus = dataSnap.child(Constants.RELATIONSHIP_STATUS).getValue().toString();
                        if (dataSnap.child(Constants.TRAINER_ID).getValue().toString().equals(currentUserId)
                                && !relStatus.equals(Constants.DECLINED) && !relStatus.equals(Constants.DELETED_STATUS) &&
                                !relStatus.equals(Constants.REMOVED_BY_TRAINEE_STATUS) && !relStatus.equals(Constants.REMOVED_BY_TRAINER_STATUS)){

                            //adding id and status as well:
                            BuddyRelationshipStatus brs = new BuddyRelationshipStatus();
                            brs.setBuddyId(dataSnap.child(Constants.TRAINEE_ID).getValue().toString());
                            brs.setStatus(dataSnap.child(Constants.RELATIONSHIP_STATUS).getValue().toString());

                            activeBuddies.add(brs);

                        }
                    }
                }

                Log.i("Noemi","buddy list : " + activeBuddies.toString());

                matchedBuddyList.clear();

                for(BuddyRelationshipStatus buds : activeBuddies){
                    matchedBuddy = new MatchedBuddy();
                    matchedUser = new User();
                    matchedUser = dataSnapshot.child(Constants.USERS).child(buds.getBuddyId()).getValue(User.class);

                    matchedBuddy.setFirstName(matchedUser.getFirstName());
                    matchedBuddy.setLastName(matchedUser.getLastName());
                    matchedBuddy.setId(buds.getBuddyId());
                    matchedBuddy.setImageUrl(matchedUser.getImageURL());
                    matchedBuddy.setStatus(buds.getStatus());
                    matchedBuddyList.add(matchedBuddy);
                }

                Log.i("Noemi",matchedBuddyList.toString());

                adapter = new BuddyAdapter(matchedBuddyList,BuddiesFragment.this);
                if(adapter.getItemCount() == 0){
                    Log.i("Noemi","Adapter is empty...");
                    emptyBuddies.setVisibility(View.VISIBLE);
                }
                else{
                    Log.i("Noemi","Adapter not empty");
                    recyclerView.setAdapter(adapter);
                }
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

    @Override
    public void buddyChatClicked(int position, String status) {
        buddy = matchedBuddyList.get(position);
        //Log.i("Noemi","Navigate into Chat room.");
        if(status.equals(Constants.NOT_ACCEPTED_STATUS)
                || status.equals(Constants.TRAINEE_ACCEPTED_STATUS)){
            FragmentManager dialogFragment = getFragmentManager();
            MessageDialog dialog;
            if(userType.equals(Constants.TRAINEE)){
                dialog = new MessageDialog("Sorry! ","You cannot chat with the trainer, until he accepts the match.");
            }
            else {
                dialog = new MessageDialog(Constants.TRAINER_CANNOT_CHAT_TITLE,"First you have to accept the match.");
            }
            dialog.show(dialogFragment, "dialog");
        }else{
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra(Constants.BUDDY_NAME_INTENT_EXTRA, buddy.getFirstName());
            intent.putExtra(Constants.BUDDY_ID_INTENT_EXTRA, buddy.getId());
            startActivity(intent);
        }
    }

    @Override
    public void buddyNameClicked(int position) {
        buddy = matchedBuddyList.get(position);
        Log.i("Noemi","Navigate to the buddy's profile screen.");
        openBuddyProfile();
    }

    @Override
    public void buddyPictureClicked(int position) {
        buddy = matchedBuddyList.get(position);
        Log.i("Noemi","I have to navigate to the buddy's profile page.");
        openBuddyProfile();
    }

    @Override
    public void buddyTrashClicked(int position) {
        buddy = matchedBuddyList.get(position);
        Log.i("Noemi","Remove buddy from list.");

        FragmentManager dialogFragment = getFragmentManager();
        DeleteBuddyDialog dialog = new DeleteBuddyDialog(buddy.getFirstName());
        dialog.show(dialogFragment, "dialog");

    }

    private void openBuddyProfile(){
        Intent intent = new Intent(getContext(), BuddyProfileActivity.class);
        intent.putExtra(Constants.BUDDY_ID,buddy.getId());
        startActivity(intent);
    }

    private void deleteBuddy(){
        if(userType.equals(Constants.TRAINEE)){
            delete(currentUserId,buddy.getId(),currentUserId,buddy.getId(),Constants.REMOVED_BY_TRAINEE_STATUS);
        }
        else {
            delete(currentUserId,buddy.getId(),buddy.getId(),currentUserId,Constants.REMOVED_BY_TRAINER_STATUS);
        }
    }

    public void delete(String currentUID, String buddyId, String traineeId, String trainerId, String removedByStatus){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.child(Constants.MATCHES).getChildren()){
                    if(snapshot.child(Constants.TRAINEE_ID).getValue().toString().equals(traineeId) &&
                            snapshot.child(Constants.TRAINER_ID).getValue().toString().equals(trainerId)){
                        snapshot.child(Constants.RELATIONSHIP_STATUS).getRef().setValue(removedByStatus);
                        //send notification informing user, that he/she is removed
                        String token = dataSnapshot.child(Constants.TOKENS).child(buddyId).child(Constants.TOKEN_NODE).getValue().toString();
                        sendNotifications(token,Constants.DECLINE_TITLE,Constants.REMOVED_MESSAGE + currentUID);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Subscribe
    public void onMessageEvent(DeleteBuddyEvent event) {
        deleteBuddy();
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


}
