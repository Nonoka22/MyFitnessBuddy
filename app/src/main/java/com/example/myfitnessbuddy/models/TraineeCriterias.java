package com.example.myfitnessbuddy.models;

import java.util.List;

public class TraineeCriterias extends Criterias {

    private String goal;
    private boolean nutritionistNeeded;
    private List<String> criterias;

    public TraineeCriterias(){
        super();
    }

    public TraineeCriterias(String city, List<String> trainerType, Price price, String goal, boolean needsNutritionist, List<String> criterias) {
        super(city, trainerType, price);
        this.goal = goal;
        this.nutritionistNeeded = needsNutritionist;
        this.criterias = criterias;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public boolean isNutritionistNeeded() {
        return nutritionistNeeded;
    }

    public void setNutritionistNeeded(boolean needsNutritionist) {
        this.nutritionistNeeded = needsNutritionist;
    }

    public List<String> getCriterias() {
        return criterias;
    }

    public void setCriterias(List<String> criterias) {
        this.criterias = criterias;
    }


    @Override
    public String toString() {
        return "TraineeCriterias{" +
                "goal='" + goal + '\'' +
                ", needsNutritionist=" + nutritionistNeeded +
                ", criterias=" + criterias +
                ", city=" + super.getCity() +
                ", trainerType=" + super.getTrainerType() +
                ", priceHour=" + super.getPrice().getHours() +
                ", priceCost=" + super.getPrice().getCost() +
                '}';
    }
}
