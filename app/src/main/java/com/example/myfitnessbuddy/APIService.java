package com.example.myfitnessbuddy;

import com.example.myfitnessbuddy.models.NotificationSender;

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
}
