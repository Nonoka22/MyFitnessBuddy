package com.example.myfitnessbuddy.fragments.criterias;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfitnessbuddy.Constants;
import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.adapters.CriteriaAdapter;
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

        criterias = new ArrayList<>(Arrays.asList(Constants.GOAL_CRITERIA, Constants.PRICE_CRITERIA,Constants.TRAINER_TYPE_CRITERIA,
                Constants.NUTRITIONIST_CRITERIA));

        RecyclerView recyclerView = binding.recyclerViewCriterias;
        CriteriaAdapter criteriaAdapter = new CriteriaAdapter(criterias);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(criteriaAdapter);

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

    public static FragmentCriteriasCriterias getInstance(){
        return new FragmentCriteriasCriterias();
    }
}
