package com.example.myfitnessbuddy.utils;

import android.content.Context;
import android.util.Log;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.example.myfitnessbuddy.BuildConfig;
import com.example.myfitnessbuddy.models.MessageWrapper;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class CometChatUtil {

    public static void initCometChat(Context context){
        String appID = BuildConfig.APP_ID;
        String region = Constants.REGION;

        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(region).build();

        CometChat.init(context, appID, appSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                Timber.i("Initialization completed successfully");
            }
            @Override
            public void onError(CometChatException e) {
                Timber.i("Initialization failed with exception: %s", e.getMessage());
            }
        });
    }

    public static void createCometChatUser(String userId,String username){
        String authKey = BuildConfig.AUTH_KEY;
        User user = new User();
        user.setUid(userId);
        user.setName(username);

        CometChat.createUser(user, authKey, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                Log.i("Noemi","createUser"+ user.toString());
                //login CometChatUser
                loginCometChatUser(userId);
            }

            @Override
            public void onError(CometChatException e) {
                Log.i("Noemi","createUser fail :"+ e.getMessage());
            }
        });
    }

    public static void loginCometChatUser(String UID){
        String authKey = BuildConfig.AUTH_KEY;

        if (CometChat.getLoggedInUser() == null) {
            CometChat.login(UID, authKey, new CometChat.CallbackListener<User>() {

                @Override
                public void onSuccess(User user) {
                    Log.i("Noemi", "Login Successful : " + user.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.i("Noemi", "Login failed with exception: " + e.getMessage());
                }
            });
        }
        else {
            // User already logged in
            Log.i("Noemi", "User is already logged in");
        }
    }

    public static void logoutCometChat(){
        CometChat.logout(new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String successMessage) {
                Log.i("Noemi", "Logout completed successfully");
            }
            @Override
            public void onError(CometChatException e) {
                Log.i("Noemi", "Logout failed with exception: " + e.getMessage());
            }
        });
    }

    public static void sendCometChatMessage(String receiverID, String messageText, MessagesListAdapter<MessageWrapper> adapter){

        String receiverType = CometChatConstants.RECEIVER_TYPE_USER;

        TextMessage textMessage = new TextMessage(receiverID, messageText, receiverType);

        CometChat.sendMessage(textMessage, new CometChat.CallbackListener <TextMessage> () {
            @Override
            public void onSuccess(TextMessage textMessage) {
                Log.i("Noemi", "Message sent successfully: " + textMessage.toString());
                addCometChatMessage(textMessage,adapter);
            }
            @Override
            public void onError(CometChatException e) {
                Log.i("Noemi", "Message sending failed with exception: " + e.getMessage());
            }
        });
    }


    public static void listenerForRealTimeMessages(MessagesListAdapter<MessageWrapper> adapter){
        String listenerID = "CHAT_ACTIVITY";

        CometChat.addMessageListener(listenerID, new CometChat.MessageListener() {
            @Override
            public void onTextMessageReceived(TextMessage textMessage) {
                Log.i("Noemi", "Text message received successfully: " + textMessage.toString());
                addCometChatMessage(textMessage,adapter);
            }
            @Override
            public void onMediaMessageReceived(MediaMessage mediaMessage) {
                Log.i("Noemi", "Media message received successfully: " + mediaMessage.toString());
            }
            @Override
            public void onCustomMessageReceived(CustomMessage customMessage) {
                Log.i("Noemi", "Custom message received successfully: " +customMessage.toString());
            }
        });
    }

    public static void removeCometChatMessageListener(){
        String listenerID = "CHAT_ACTIVITY";

        CometChat.removeMessageListener(listenerID);
    }

    private static void addCometChatMessage(TextMessage textMessage, MessagesListAdapter<MessageWrapper> adapter){
        adapter.addToStart(new MessageWrapper(textMessage),true);
    }

    public static void fetchMissedMessages(String UID,MessagesListAdapter<MessageWrapper> adapter){
        MessagesRequest messagesRequest = new MessagesRequest.MessagesRequestBuilder().setUID(UID).build();

        messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
            @Override
            public void onSuccess(List <BaseMessage> list) {
                for (BaseMessage message: list) {
                    if (message instanceof TextMessage) {
                        addCometChatMessage((TextMessage) message , adapter);
                    } else if (message instanceof MediaMessage) {}
                }
            }
            @Override
            public void onError(CometChatException e) {
                Log.i("Noemi", "Message fetching failed with exception: " + e.getMessage());
            }
        });
    }
}
