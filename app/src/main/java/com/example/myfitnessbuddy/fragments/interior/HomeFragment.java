package com.example.myfitnessbuddy.fragments.interior;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.interfaces.OnNotificationClickedListener;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.CriteriasActivity;
import com.example.myfitnessbuddy.adapters.NotificationAdapter;
import com.example.myfitnessbuddy.databinding.FragmentHomeBinding;
import com.example.myfitnessbuddy.events.NotificationEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.example.myfitnessbuddy.fragments.dialogs.NotificationDetailDialog;
import com.example.myfitnessbuddy.models.NotificationBuddy;
import com.example.myfitnessbuddy.models.NotificationData;
import com.example.myfitnessbuddy.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements OnNotificationClickedListener {


    //create two arrays for the two userTypes to differenciate them
    private ArrayList<NotificationData> notifications = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private NotificationAdapter adapter;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initFragmentImpl() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
        List<NotificationBuddy> ids = new ArrayList<>();
        List<NotificationBuddy> acceptedIds = new ArrayList<>();
        List<NotificationBuddy> declinedIds = new ArrayList<>();
        List<NotificationBuddy> removedIds = new ArrayList<>();

        TextView emptyHome = binding.emptyHomeTV;
        RecyclerView recyclerView = binding.notificationsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userType = dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.USER_TYPE).getValue().toString();

                notifications.clear();
                //if there is no introduction saved in the database, then the first notification will appear.
                if(!dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.IMAGE_URL).exists()){
                    //the first notification will be default, it will differ according to the userType
                    if(userType.equals(Constants.TRAINEE)){
                        notifications.add(new NotificationData(Constants.FIRST_NOTIFICATION_TITLE,"Before you may start using the app," +
                                " you must provide some information, so that we can create matches with trainers according to your expectations.",Constants.NOTIFYING_TYPE));
                    }
                    else if (userType.equals(Constants.TRAINER)){
                        notifications.add(new NotificationData(Constants.FIRST_NOTIFICATION_TITLE,"Before you may start using the app," +
                                " you must provide some information, so that we can create matches with trainees.",Constants.NOTIFYING_TYPE));
                    }

                }

                ids.clear();
                acceptedIds.clear();
                declinedIds.clear();
                removedIds.clear();

                for(DataSnapshot snapshot : dataSnapshot.child(Constants.MATCHES).getChildren()){
                    if(userType.equals(Constants.TRAINEE)){
                        if(snapshot.child(Constants.TRAINEE_ID).getValue().toString().equals(currentUserId)){
                            String relStatus = snapshot.child(Constants.RELATIONSHIP_STATUS).getValue().toString();
                            NotificationBuddy notificationBuddy = new NotificationBuddy();
                            notificationBuddy.setId(snapshot.child(Constants.TRAINER_ID).getValue().toString());
                            notificationBuddy.setName(dataSnapshot.child(Constants.USERS).child(snapshot.child(Constants.TRAINER_ID).getValue().toString()).child(Constants.FIRST_NAME).getValue().toString());

                               if(relStatus.equals(Constants.NOT_ACCEPTED_STATUS)){
                                   ids.add(notificationBuddy);
                               }
                               else if(relStatus.equals(Constants.ACCEPTED_STATUS)){
                                   acceptedIds.add(notificationBuddy);
                               }
                               else if (relStatus.equals(Constants.DECLINED)){
                                   declinedIds.add(notificationBuddy);
                               }
                               else if (relStatus.equals(Constants.REMOVED_BY_TRAINER_STATUS)){
                                   removedIds.add(notificationBuddy);
                               }
                        }
                    }
                    else if(userType.equals(Constants.TRAINER)){
                        if(snapshot.child(Constants.TRAINER_ID).getValue().toString().equals(currentUserId)){
                            String relStatus = snapshot.child(Constants.RELATIONSHIP_STATUS).getValue().toString();
                            NotificationBuddy nB = new NotificationBuddy();
                            nB.setId(snapshot.child(Constants.TRAINEE_ID).getValue().toString());
                            nB.setName(dataSnapshot.child(Constants.USERS).child(snapshot.child(Constants.TRAINEE_ID).getValue().toString()).child(Constants.FIRST_NAME).getValue().toString());

                            if(relStatus.equals(Constants.NOT_ACCEPTED_STATUS)
                                    || relStatus.equals(Constants.TRAINEE_ACCEPTED_STATUS)){
                                Log.i("Noemi","Traine ids notif: " + snapshot.child(Constants.TRAINEE_ID).getValue().toString());
                                ids.add(nB);
                            }
                            else if (relStatus.equals(Constants.REMOVED_BY_TRAINEE_STATUS)){
                                removedIds.add(nB);
                            }

                        }
                    }

                }

                if(!acceptedIds.isEmpty()){
                    for(NotificationBuddy s : acceptedIds){
                        notifications.add(new NotificationData(Constants.ACCEPTANCE_TITLE,Constants.ACCEPTANCE_MESSAGE + s.getName(),Constants.NOTIFYING_TYPE,s.getId()));
                    }
                }

                if(!declinedIds.isEmpty()){
                    for(NotificationBuddy s : declinedIds){
                        notifications.add(new NotificationData(Constants.DECLINE_TITLE,Constants.DECLINE_MESSAGE + s.getName(),Constants.NOTIFYING_TYPE,s.getId()));
                    }
                }

                if(!removedIds.isEmpty()){
                    for(NotificationBuddy s : removedIds){
                        if(userType.equals(Constants.TRAINEE)){
                            notifications.add(new NotificationData(Constants.DECLINE_TITLE,Constants.REMOVED_MESSAGE + s.getName(),Constants.NOTIFYING_TYPE,s.getId()));
                        }
                        else if(userType.equals(Constants.TRAINER)){
                            notifications.add(new NotificationData(Constants.DECLINE_TITLE,Constants.REMOVED_MESSAGE + s.getName(),Constants.NOTIFYING_TYPE,s.getId()));
                        }
                    }
                }

                if(!ids.isEmpty()){
                    for(NotificationBuddy s : ids){
                        if(userType.equals(Constants.TRAINEE)){
                            notifications.add(new NotificationData(Constants.MATCH_NOTIF_TITLE,Constants.MATCH_NOTIF_TRAINEE_MESSAGE + s.getName(),Constants.NOTIFYING_TYPE,s.getId()));
                        }
                        else if(userType.equals(Constants.TRAINER)){
                            notifications.add(new NotificationData(Constants.MATCH_NOTIF_TITLE,Constants.MATCH_NOTIF_TRAINER_MESSAGE + s.getName(),Constants.MATCHING_TYPE,s.getId()));
                        }
                    }
                }


                adapter = new NotificationAdapter(notifications,HomeFragment.this);
                if(adapter.getItemCount() == 0){
                    emptyHome.setVisibility(View.VISIBLE);
                }
                else{
                    recyclerView.setAdapter(adapter);
                    emptyHome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static HomeFragment getInstance(){
        return new HomeFragment();
    }

    @Override
    public void acceptButtonClicked(int position, String matchedId) { //no idea, why i needed position
        //this button will only appear if the user is a trainer
        //set the status in Matches node to Accepted where trainer is currentUser and trainee is the one mentioned in the notification...
        setStatusByTrainer(matchedId,Constants.ACCEPTED_STATUS,Constants.ACCEPTANCE_TITLE,Constants.ACCEPTANCE_MESSAGE);
    }

    @Override
    public void declineButtonClicked(int position, String matchedId) {
        //this button will only appear if the user is a trainer
        //set the status in Matches node to Declined
        setStatusByTrainer(matchedId,Constants.DECLINED,Constants.DECLINE_TITLE,Constants.DECLINE_MESSAGE);
    }

    public void setStatusByTrainer(String matchedId,String status,String notificationTitle, String notificationMessage){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.child(Constants.MATCHES).getChildren()){
                    if(snapshot.child(Constants.TRAINER_ID).getValue().toString().equals(currentUserId) &&
                            snapshot.child(Constants.TRAINEE_ID).getValue().toString().equals(matchedId)){
                        snapshot.child(Constants.RELATIONSHIP_STATUS).getRef().setValue(status);
                    }
                }
                //send notification to the trainee, that the match was accepted
                String userToken = dataSnapshot.child(Constants.TOKENS).child(matchedId).child(Constants.TOKEN_NODE).getValue().toString();
                sendNotifications(userToken,notificationTitle,notificationMessage + currentUserId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void okButtonClicked(int position, String matchedId) {
        if(notifications.get(position).getTitle().equals(Constants.FIRST_NOTIFICATION_TITLE)) {
            Intent intent = new Intent(getContext(), CriteriasActivity.class);
            startActivity(intent);
        }
        else{
            //it should delete the notification from the list for the user
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.child(Constants.MATCHES).getChildren()){
                        if(snapshot.child(Constants.TRAINEE_ID).getValue().toString().equals(currentUserId) &&
                                snapshot.child(Constants.TRAINER_ID).getValue().toString().equals(matchedId)){
                            String relaStatus = snapshot.child(Constants.RELATIONSHIP_STATUS).getValue().toString();
                            if(relaStatus.equals(Constants.NOT_ACCEPTED_STATUS)){
                                snapshot.child(Constants.RELATIONSHIP_STATUS).getRef().setValue(Constants.TRAINEE_ACCEPTED_STATUS);
                            }
                            if(relaStatus.equals(Constants.ACCEPTED_STATUS)){
                                snapshot.child(Constants.RELATIONSHIP_STATUS).getRef().setValue(Constants.DONE_STATUS);
                            }

                            if(relaStatus.equals(Constants.DECLINED) || relaStatus.equals(Constants.REMOVED_BY_TRAINER_STATUS)){
                                snapshot.child(Constants.RELATIONSHIP_STATUS).getRef().setValue(Constants.DELETED_STATUS);
                            }
                        }
                        else if(snapshot.child(Constants.TRAINER_ID).getValue().toString().equals(currentUserId) &&
                                snapshot.child(Constants.TRAINEE_ID).getValue().toString().equals(matchedId)){
                            String relaStatus = snapshot.child(Constants.RELATIONSHIP_STATUS).getValue().toString();
                            if(relaStatus.equals(Constants.REMOVED_BY_TRAINEE_STATUS)){
                                snapshot.child(Constants.RELATIONSHIP_STATUS).getRef().setValue(Constants.DELETED_STATUS);
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void notificationClicked(int position) {

        //Pass information
        EventBus.getDefault().postSticky(new NotificationEvent(notifications.get(position),position));

        //opens notification detail screen...
        FragmentManager dialogFragment = getChildFragmentManager();
        NotificationDetailDialog notificationDialog= new NotificationDetailDialog(position,HomeFragment.this,notifications.get(position).getNotificationType());

        notificationDialog.show(dialogFragment, "notification dialog");

    }

}
