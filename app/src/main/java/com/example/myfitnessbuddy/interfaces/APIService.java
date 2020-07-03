package com.example.myfitnessbuddy.interfaces;

import com.example.myfitnessbuddy.utils.MyResponse;
import com.example.myfitnessbuddy.models.NotificationSender;
import com.example.myfitnessbuddy.models.SuccessStory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA7NRXEKw:APA91bGR-7tYewQYvJaDkcdT9iKZI7FU4ZP_-2Kwrbk9C-JQHVOoV4XtUQRYNrqj3msd2i43hFy_xtqSl5uT59hEJlZiyfYk7X7EgcXUKxuv7GrfztqMzm6WvMCi5KkAsBdxlHV44sHa"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);

    interface OnSuccessStoryUploaded {
        void successStoryUploaded(SuccessStory successStory);
    }

    interface OnUpdateClickedListener {
        void firstNameUpdated(String firstName);
        void lastNameUpdated(String lastName);
        void cityUpdated(String city);
        void introductionUpdated(String introduction);
        void priceUpdated(String hours, String cost);
        void goalUpdated(String goal);
        void trainerTypeUpdated(List<String> trainerTypeList);
        void specialtiesUpdated(List<String> specialtyList);
        void nutriNeededUpdated(boolean nutriNeeded);
        void gymUpdated(String gym);
        void criteriaUpdated(List<String> criteria);
        void profilePicUpdated(String imageUrl, String oldImageUrl);
    }
}
