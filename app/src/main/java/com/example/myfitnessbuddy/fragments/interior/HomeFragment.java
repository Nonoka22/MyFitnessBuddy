package com.example.myfitnessbuddy.fragments.interior;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.FragmentManager;

import com.example.myfitnessbuddy.R;
import com.example.myfitnessbuddy.adapters.NotificationAdapter;
import com.example.myfitnessbuddy.databinding.FragmentHomeBinding;
import com.example.myfitnessbuddy.events.NotificationEvent;
import com.example.myfitnessbuddy.fragments.BaseFragment;
import com.example.myfitnessbuddy.fragments.dialogs.NotificationDetailDialog;
import com.example.myfitnessbuddy.models.Notifications;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private ArrayList<Notifications> notifications = new ArrayList<>();

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initFragmentImpl() {

        ListView listView = binding.notificationsListView;

        //the first notification will be default, it will differ according to the userType
        notifications.add(new Notifications("Let's get started!","Tell us what you need...","Before you may start using the app," +
                " you must provide some information, so that we can create matches according to your expectations."));

        NotificationAdapter adapter = new NotificationAdapter(getContext(), R.layout.notification_row, notifications);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Pass information
                EventBus.getDefault().postSticky(new NotificationEvent(notifications.get(position)));

                //opens notification detail screen...
                FragmentManager dialogFragment = getChildFragmentManager();
                NotificationDetailDialog notificationDialog= new NotificationDetailDialog();
                notificationDialog.show(dialogFragment, "notification dialog");
            }
        });
    }


}
