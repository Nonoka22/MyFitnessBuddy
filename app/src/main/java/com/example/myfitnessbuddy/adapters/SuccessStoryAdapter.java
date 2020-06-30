package com.example.myfitnessbuddy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.SuccessStoryRowBinding;
import com.example.myfitnessbuddy.models.SuccessStory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SuccessStoryAdapter extends RecyclerView.Adapter<SuccessStoryAdapter.ViewHolder> {

    private SuccessStoryRowBinding binding;
    private List<SuccessStory> stories;

    public SuccessStoryAdapter(List<SuccessStory> stories) {
        this.stories = stories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.success_story_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());

        viewHolder.beforeIgmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.afterImgView.setVisibility(View.VISIBLE);
                viewHolder.arrowImgView.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.afterImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.afterImgView.setVisibility(View.GONE);
                viewHolder.arrowImgView.setVisibility(View.GONE);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SuccessStory story = stories.get(position);
        holder.bindViewHolder(story);
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView beforeIgmView, afterImgView, arrowImgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = binding.ssTitleTextView;
            beforeIgmView = binding.beforeImageView;
            afterImgView = binding.afterImageView;
            arrowImgView = binding.arrow;
        }

        void bindViewHolder(SuccessStory story){
            titleTextView.setText(story.getTitle());
            Picasso.get()
                    .load(story.getBeforeImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .into(beforeIgmView);

            Picasso.get()
                    .load(story.getAfterImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .into(afterImgView);
        }
    }
}
