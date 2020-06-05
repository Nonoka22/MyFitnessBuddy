package com.example.myfitnessbuddy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.databinding.CriteriaRowBinding;

import java.util.List;

public class CriteriaAdapter extends RecyclerView.Adapter<CriteriaAdapter.ViewHolder> {

    private List<String> criterias;
    private CriteriaRowBinding binding;

    public CriteriaAdapter(List<String> criterias) {
        this.criterias = criterias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.criteria_row, parent,false);

        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(criterias.get(position));
    }

    @Override
    public int getItemCount() {
        return criterias.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

      ViewHolder(@NonNull View itemView) {
          super(itemView);

          textView = binding.criteriaItem;
      }
  }
}
