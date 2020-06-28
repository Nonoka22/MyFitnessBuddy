package com.example.myfitnessbuddy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.OnDeleteListItemClicked;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.EditListItemRowBinding;

import java.util.List;

public class EditListAdapter extends RecyclerView.Adapter<EditListAdapter.ViewHolder> {

    private List<String> dataList;
    private String listType;
    private EditListItemRowBinding binding;
    private OnDeleteListItemClicked listener;

    public EditListAdapter(List<String> dataList, String listType, OnDeleteListItemClicked listener) {
        this.dataList = dataList;
        this.listType = listType;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.edit_list_item_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(binding.getRoot());

        viewHolder.imageViewTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editItemTrashClicked(viewHolder.getAdapterPosition(),listType);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String listItem = dataList.get(position);
        holder.bindViewHolder(listItem);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewListItem;
        ImageView imageViewTrash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewListItem = binding.editListViewItem;
            imageViewTrash = binding.editTrashIcon;

            //when clicking on trash the item is removed from list
        }

        void bindViewHolder(String listItem){
            textViewListItem.setText(listItem);
        }
    }
}
