package com.example.myfitnessbuddy;

import java.util.List;

public interface OnUpdateClickedListener {
    void firstNameUpdated(String firstName);
    void lastNameUpdated(String lastName);
    void cityUpdated(String city);
    void introductionUpdated(String introduction);
    void priceUpdated(String hours,String cost);
    void goalUpdated(String goal);
    void trainerTypeUpdated(List<String> trainerTypeList);
    void specialtiesUpdated(List<String> specialtyList);
    void nutriNeededUpdated(boolean nutriNeeded);
    void gymUpdated(String gym);
    void criteriaUpdated(List<String> criteria);
    void profilePicUpdated(String imageUrl);
}
