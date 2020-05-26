package com.example.myfitnessbuddy.fragments.criterias;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.adapters.RecyclerAdapter;
import com.example.myfitnessbuddy.databinding.CriteriasCriteriasFragmentBinding;
import com.example.myfitnessbuddy.events.PassingTraineeCriteriasEvent;
import com.example.myfitnessbuddy.events.SetNextFragmentEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FragmentCriteriasCriterias extends BaseFragment<CriteriasCriteriasFragmentBinding> {

    ArrayList<String> criterias;

    @Override
    protected int getFragmentLayout() {
        return R.layout.criterias_criterias_fragment;
    }

    @Override
    protected void initFragmentImpl() {
        ImageView nextButton = binding.nextButtonCriterias;

        criterias = new ArrayList<>(Arrays.asList("Your Goal", "The Training Price","Group Instructor or Personal Trainer",
                "Nutritionist or Not"));

        RecyclerView recyclerView = binding.recyclerViewCriterias;
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(criterias);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(recyclerAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PassingTraineeCriteriasEvent("Criterias", criterias));
                EventBus.getDefault().post(new SetNextFragmentEvent());
            }
        });
    }

    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START
    | ItemTouchHelper.END,0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(criterias,fromPosition,toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);

            //Log.i("Noemi",criterias.toString());
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

}
