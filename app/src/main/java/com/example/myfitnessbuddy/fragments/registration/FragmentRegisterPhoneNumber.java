package com.example.myfitnessbuddy.fragments.registration;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.RegisterPhoneNumberFragmentBinding;
import com.example.myfitnessbuddy.events.PassingUserArgumentsEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.example.myfitnessbuddy.fragments.dialogs.MessageDialog;
import com.example.myfitnessbuddy.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;


public class FragmentRegisterPhoneNumber extends BaseFragment<RegisterPhoneNumberFragmentBinding> {
    private EditText fieldPhoneNumber;
    private String phoneNumber;
    private TextView errorMessage;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private boolean alreadyExists = false;

    @Override
    protected int getFragmentLayout() {
        return R.layout.register_phone_number_fragment;
    }

    @Override
    protected void initFragmentImpl() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        fieldPhoneNumber = binding.fieldRegisterPhone;
        ImageView nextButton = binding.nextButtonPhoneNumber;
        errorMessage = binding.errorRegisterPhonNum;

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = fieldPhoneNumber.getText().toString();

                if(!phoneNumber.isEmpty()){
                    errorMessage.setVisibility(View.GONE);

                    if(phoneNumber.length() < 9 || phoneNumber.length() > 10){
                        errorMessage.setText("You entered an invalid phone number.");
                        errorMessage.setVisibility(View.VISIBLE);
                    }
                    else{
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot : dataSnapshot.child(Constants.USERS).getChildren()){
                                    if(snapshot.child(Constants.PHONE_NUMBER).getValue().toString().equals(phoneNumber)){
                                        alreadyExists = true;
                                        break;
                                    }
                                }

                                if(alreadyExists){
                                    FragmentManager dialogFragment = getChildFragmentManager();
                                    MessageDialog dialog = new MessageDialog(Constants.KNOWN_PHONE_NUMBER_TITLE,"We already know you! Log in, if you want to...");
                                    dialog.show(dialogFragment, "dialog");
                                }
                                else{
                                    EventBus.getDefault().post(new PassingUserArgumentsEvent("PhoneNumber", phoneNumber));
                                    EventBus.getDefault().post(new SetNextFragmentEvent());
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
                else{
                    errorMessage.setText("You must provide a phone number.");
                    errorMessage.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    public static FragmentRegisterPhoneNumber getInstance(){
        return new FragmentRegisterPhoneNumber();
    }
}
