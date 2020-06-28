package com.example.myfitnessbuddy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.OnBuddyClickedListener;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.BuddyRowBinding;
import com.example.myfitnessbuddy.models.MatchedBuddy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BuddyAdapter extends RecyclerView.Adapter<BuddyAdapter.ViewHolder> {

    private List<MatchedBuddy> matchedBuddies;
    private BuddyRowBinding binding;
    private OnBuddyClickedListener listener;

    public BuddyAdapter(List<MatchedBuddy> matchedBuddies, OnBuddyClickedListener listener) {
        this.matchedBuddies = matchedBuddies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.buddy_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());

        viewHolder.imageViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.buddyChatClicked(viewHolder.getAdapterPosition(),matchedBuddies.get(viewHolder.getAdapterPosition()).getStatus());
            }
        });

        viewHolder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.buddyNameClicked(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.imageViewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.buddyPictureClicked(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.imageViewTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.buddyTrashClicked(viewHolder.getAdapterPosition());
            }
        });

        return viewHolder;
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

        }
    }


}
