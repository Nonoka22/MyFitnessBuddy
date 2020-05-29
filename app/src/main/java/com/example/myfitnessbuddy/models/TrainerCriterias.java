package com.example.myfitnessbuddy.models;

import java.util.List;

public class TrainerCriterias extends Criterias {

    private List<String> specialties;
    private String gym;

    public TrainerCriterias() {
        super();
    }

    public TrainerCriterias(String city, List<String> trainerType, Price price, List<String> specialties, String gym) {
        super(city, trainerType, price);
        this.specialties = specialties;
        this.gym = gym;
    }

    public List<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<String> specialties) {
        this.specialties = specialties;
    }

    public String getGym() {
        return gym;
    }

    public void setGym(String gym) {
        this.gym = gym;
    }

    @Override
    public String toString() {
        return "TrainerCriterias{" +
                "specialties=" + specialties +
                ", gym='" + gym + '\'' +
                ", city=" + super.getCity() +
                ", trainerType=" + super.getTrainerType() +
                ", priceHour=" + super.getPrice().getHours() +
                ", priceCost=" + super.getPrice().getCost() +
                '}';
    }
}
