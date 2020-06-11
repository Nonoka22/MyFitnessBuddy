package com.example.myfitnessbuddy.activities;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.cometchat.pro.core.CometChat;
import com.example.myfitnessbuddy.models.MessageWrapper;
import com.example.myfitnessbuddy.utils.CometChatUtil;
import com.example.myfitnessbuddy.utils.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.ActivityChatBinding;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

public class ChatActivity extends BaseActivity<ActivityChatBinding> {

    String buddyName;
    String buddyId;
    MessagesListAdapter<MessageWrapper> adapter;
    String senderId;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initActivityImpl() {

        Intent intent = getIntent();

        if(intent != null){
            buddyName = intent.getStringExtra(Constants.BUDDY_NAME_INTENT_EXTRA);
            buddyId = intent.getStringExtra(Constants.BUDDY_ID_INTENT_EXTRA);
        }

        TextView buddyNameTextView = binding.buddyChatName;
        buddyNameTextView.setText(buddyName);
        MessagesList messagesList = binding.messagesList;

        MessageInput inputView = binding.input;

        senderId = CometChat.getLoggedInUser().getUid();
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Picasso.get().load(url).into(imageView);
            }
        };

        adapter = new MessagesListAdapter<>(senderId, imageLoader);
        messagesList.setAdapter(adapter);

        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                CometChatUtil.sendCometChatMessage(buddyId,input.toString(),adapter);
                return true;
            }
        });

        CometChatUtil.fetchMissedMessages(buddyId, adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        CometChatUtil.listenerForRealTimeMessages(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        CometChatUtil.removeCometChatMessageListener();
    }
}
