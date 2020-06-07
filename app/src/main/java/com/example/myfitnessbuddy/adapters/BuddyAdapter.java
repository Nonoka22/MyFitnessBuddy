package com.example.myfitnessbuddy.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.activities.ChatActivity;
import com.example.myfitnessbuddy.databinding.BuddyRowBinding;
import com.example.myfitnessbuddy.models.MatchedBuddy;
import com.example.myfitnessbuddy.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BuddyAdapter extends RecyclerView.Adapter<BuddyAdapter.ViewHolder> {

    private List<MatchedBuddy> matchedBuddies;
    private BuddyRowBinding binding;

    public BuddyAdapter(List<MatchedBuddy> matchedBuddies) {
        this.matchedBuddies = matchedBuddies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.buddy_row,parent,false);

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MatchedBuddy matchedBuddy = matchedBuddies.get(position);
        holder.bindViewHolder(matchedBuddy);
    }

    @Override
    public int getItemCount() {
        return matchedBuddies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        ImageView imageViewPicture;
        ImageView imageViewChat;
        ImageView imageViewTrash;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = binding.buddyTextView;
            imageViewPicture = binding.buddyImageView;
            imageViewChat = binding.chatIcon;
            imageViewTrash = binding.trashIcon;

        }

        void bindViewHolder(MatchedBuddy matchedBuddy){
            textViewName.setText(matchedBuddy.getFirstName() + " " + matchedBuddy.getLastName());
            Picasso.get()
                    .load(matchedBuddy.getImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .into(imageViewPicture);

            imageViewPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Noemi","I have to navigate to the buddy's profile page.");
                }
            });

            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Noemi","Navigate to the buddy's profile screen.");
                }
            });

            imageViewChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Noemi","Navigate into Chat room.");
                    Intent intent = new Intent(imageViewChat.getContext(), ChatActivity.class);
                    intent.putExtra(Constants.BUDDY_NAME_INTENT_EXTRA,matchedBuddy.getFirstName());
                    intent.putExtra(Constants.BUDDY_ID_INTENT_EXTRA,matchedBuddy.getId());
                    // intent.putExtra(Constants.CURRENT_USER_ID_INTENT_EXTRA,);
                     itemView.getContext().startActivity(intent);
                }
            });

            imageViewTrash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Noemi","Remove buddy from list.");
                }
            });
        }
    }
}
