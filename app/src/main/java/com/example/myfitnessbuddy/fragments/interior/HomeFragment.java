package com.example.myfitnessbuddy.fragments.interior;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.APIService;
import com.example.myfitnessbuddy.MyResponse;
import com.example.myfitnessbuddy.OnNotificationClickedListener;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.CriteriasActivity;
import com.example.myfitnessbuddy.adapters.NotificationAdapter;
import com.example.myfitnessbuddy.databinding.FragmentHomeBinding;
import com.example.myfitnessbuddy.events.NotificationEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.example.myfitnessbuddy.fragments.dialogs.NotificationDetailDialog;
import com.example.myfitnessbuddy.models.Client;
import com.example.myfitnessbuddy.models.NotificationData;
import com.example.myfitnessbuddy.models.NotificationSender;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements OnNotificationClickedListener {


    //create two arrays for the two userTypes to differenciate them
    private ArrayList<NotificationData> notifications = new ArrayList<>();
    private ArrayList<NotificationData> traineeNotifications = new ArrayList<>();
    private ArrayList<NotificationData> trainerNotifications = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;
    private APIService apiService;
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
        List<String> ids = new ArrayList<>();
        List<String> acceptedIds = new ArrayList<>();
        List<String> declinedIds = new ArrayList<>();

        RecyclerView recyclerView = binding.notificationsRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        apiService = Client.getClient(Constants.BASE_URL).create(APIService.class);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userType = dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.USER_TYPE).getValue().toString();

                notifications.clear();
                trainerNotifications.clear();
                traineeNotifications.clear();

                //if there is no introduction saved in the database, then the first notification will appear.
                if(!dataSnapshot.child(Constants.USERS).child(currentUserId).child(Constants.INTRODUCTION).exists()){
                    //the first notification will be default, it will differ according to the userType
                    if(userType.equals(Constants.TRAINEE)){
                        traineeNotifications.add(new NotificationData(Constants.FIRST_NOTIFICATION_TITLE,"Before you may start using the app," +
                                " you must provide some information, so that we can create matches with trainers according to your expectations.",Constants.NOTIFYING_TYPE,""));
                    }
                    else if (userType.equals(Constants.TRAINER)){
                        trainerNotifications.add(new NotificationData(Constants.FIRST_NOTIFICATION_TITLE,"Before you may start using the app," +
                                " you must provide some information, so that we can create matches with trainees.",Constants.NOTIFYING_TYPE,""));
                    }

                }

                ids.clear();
                acceptedIds.clear();
                declinedIds.clear();

                for(DataSnapshot snapshot : dataSnapshot.child(Constants.MATCHES).getChildren()){
                    if(userType.equals(Constants.TRAINEE)){
                        if(snapshot.child(Constants.TRAINEE_ID).getValue().toString().equals(currentUserId)){
                            String relStatus = snapshot.child(Constants.RELATIONSHIP_STATUS).getValue().toString();
                            String trainerId = snapshot.child(Constants.TRAINER_ID).getValue().toString();
                               if(relStatus.equals(Constants.NOT_ACCEPTED_STATUS)){
                                   ids.add(trainerId);
                               }
                               else if(relStatus.equals(Constants.ACCEPTED_STATUS)){
                                   acceptedIds.add(trainerId);
                               }
                               else if (relStatus.equals(Constants.DECLINED)){
                                   declinedIds.add(trainerId);
                               }
                        }
                    }
                    else if(userType.equals(Constants.TRAINER)){
                        if(snapshot.child(Constants.TRAINER_ID).getValue().toString().equals(currentUserId) &&
                                (snapshot.child(Constants.RELATIONSHIP_STATUS).getValue().toString().equals(Constants.NOT_ACCEPTED_STATUS)
                        || snapshot.child(Constants.RELATIONSHIP_STATUS).getValue().toString().equals(Constants.TRAINEE_ACCEPTED_STATUS))){
                            Log.i("Noemi","Traine ids notif: " + snapshot.child(Constants.TRAINEE_ID).getValue().toString());
                            ids.add(snapshot.child(Constants.TRAINEE_ID).getValue().toString());
                        }
                    }

                }

                if(!acceptedIds.isEmpty()){
                    for(String s : acceptedIds){
                        traineeNotifications.add(new NotificationData(Constants.ACCEPTANCE_TITLE,Constants.ACCEPTANCE_MESSAGE + s,Constants.NOTIFYING_TYPE,s));
                        notifications = traineeNotifications;
                    }
                }

                if(!declinedIds.isEmpty()){
                    for(String s : declinedIds){
                        traineeNotifications.add(new NotificationData(Constants.DECLINE_TITLE,Constants.DECLINE_MESSAGE + s,Constants.NOTIFYING_TYPE,s));
                        notifications = traineeNotifications;
                    }
                }

                if(!ids.isEmpty()){
                    for(String s : ids){
                        if(userType.equals(Constants.TRAINEE)){
                            traineeNotifications.add(new NotificationData(Constants.MATCH_NOTIF_TITLE,Constants.MATCH_NOTIF_TRAINEE_MESSAGE + s,Constants.NOTIFYING_TYPE,s));
                            notifications = traineeNotifications;
                        }
                        else if(userType.equals(Constants.TRAINER)){
                            trainerNotifications.add(new NotificationData(Constants.MATCH_NOTIF_TITLE,Constants.MATCH_NOTIF_TRAINER_MESSAGE + s,Constants.MATCHING_TYPE,s));
                            notifications = trainerNotifications;
                        }
                    }
                }

                if(userType.equals(Constants.TRAINEE)){
                    notifications = traineeNotifications;
                }
                else if(userType.equals(Constants.TRAINER)){
                    notifications = trainerNotifications;
                }

                adapter = new NotificationAdapter(notifications,HomeFragment.this);
                recyclerView.setAdapter(adapter);

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
    public void acceptButtonClicked(int position, String matchedId) {
        //this button will only appear if the user is a trainer
        //set the status in Matches node to Accepted where trainer is currentUser and trainee is the one mentioned in the notification...
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               for(DataSnapshot snapshot : dataSnapshot.child(Constants.MATCHES).getChildren()){
                   if(snapshot.child(Constants.TRAINER_ID).getValue().toString().equals(currentUserId) &&
                           snapshot.child(Constants.TRAINEE_ID).getValue().toString().equals(matchedId)){
                       snapshot.child(Constants.RELATIONSHIP_STATUS).getRef().setValue(Constants.ACCEPTED_STATUS);
                   }
               }
                //send notification to the trainee, that the match was accepted
                String userToken = dataSnapshot.child(Constants.TOKENS).child(matchedId).child(Constants.TOKEN_NODE).getValue().toString();
                sendNotifications(userToken,Constants.ACCEPTANCE_TITLE,Constants.ACCEPTANCE_MESSAGE, currentUserId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void declineButtonClicked(int position, String matchedId) {
        //this button will only appear if the user is a trainer
        //set the status in Matches node to Declined
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.child(Constants.MATCHES).getChildren()){
                    if(snapshot.child(Constants.TRAINER_ID).getValue().toString().equals(currentUserId) &&
                            snapshot.child(Constants.TRAINEE_ID).getValue().toString().equals(matchedId)){
                        snapshot.child(Constants.RELATIONSHIP_STATUS).getRef().setValue(Constants.DECLINED);
                    }
                }
                //send notification to trainee, that the match was declined
                String userToken = dataSnapshot.child(Constants.TOKENS).child(matchedId).child(Constants.TOKEN_NODE).getValue().toString();
                sendNotifications(userToken,Constants.ACCEPTANCE_TITLE,Constants.ACCEPTANCE_MESSAGE, currentUserId);

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
            //only the trainee is able to come to else branch
            //it should delete the notification from the list for the trainee
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

                            if(relaStatus.equals(Constants.DECLINED)){
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

    private void sendNotifications(String token, String title, String message, String matchedId) {
        NotificationData data = new NotificationData(title,message,Constants.MATCHING_TYPE, matchedId);
        NotificationSender sender = new NotificationSender(data, token);
        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if(response.code() == 200){
                    if(response.body().succes != 1){
                        Log.i("Noemi", "Sending Notification Failed");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}
