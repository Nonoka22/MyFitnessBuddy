package com.example.myfitnessbuddy.models;

import java.util.List;

public class TraineeCriterias extends Criterias {

    private String goal;
    private boolean needsNutritionist;
    private List<String> criterias;

    public TraineeCriterias(){
        super();
    }

    public TraineeCriterias(String city, List<String> trainerType, Price price, String goal, boolean needsNutritionist, List<String> criterias) {
        super(city, trainerType, price);
        this.goal = goal;
        this.needsNutritionist = needsNutritionist;
        this.criterias = criterias;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public boolean needsNutritionist() {
        return needsNutritionist;
    }

    public void setNeedsNutritionist(boolean needsNutritionist) {
        this.needsNutritionist = needsNutritionist;
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
                ", needsNutritionist=" + needsNutritionist +
                ", criterias=" + criterias +
                ", city=" + super.getCity() +
                ", trainerType=" + super.getTrainerType() +
                ", priceHour=" + super.getPrice().getHours() +
                ", priceCost=" + super.getPrice().getCost() +
                '}';
    }
}
