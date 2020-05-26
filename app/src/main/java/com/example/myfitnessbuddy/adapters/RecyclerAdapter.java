package com.example.myfitnessbuddy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<String> criterias;

    public RecyclerAdapter(List<String> criterias) {
        this.criterias = criterias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.criteria_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
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

      public ViewHolder(@NonNull View itemView) {
          super(itemView);

          textView = itemView.findViewById(R.id.criteria_item);
      }
  }
}
