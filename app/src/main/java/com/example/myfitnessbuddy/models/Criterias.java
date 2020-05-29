package com.example.myfitnessbuddy.models;

import java.util.List;

public class Criterias {
    private String city;
    private List<String> trainerType;
    private Price price;

    Criterias(String city, List<String> trainerType, Price price) {
        this.city = city;
        this.trainerType = trainerType;
        this.price = price;
    }

    Criterias() {

    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getTrainerType() {
        return trainerType;
    }

    public void setTrainerType(List<String> trainerType) {
        this.trainerType = trainerType;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Criterias{" +
                "city='" + city + '\'' +
                ", trainerType=" + trainerType +
                ", price=" + price +
                '}';
    }
}
