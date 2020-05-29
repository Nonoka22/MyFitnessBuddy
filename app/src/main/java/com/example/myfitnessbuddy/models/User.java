package com.example.myfitnessbuddy.models;

public class User {
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String gender;
    private String userType;
    private String introduction;
    private String imageURL;
    private Criterias criterias;

    public User() {
    }

    public User(String phoneNumber, String firstName, String lastName, String birthDate, String gender, String userType, String introduction, String imageURL, Criterias criterias) {
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.userType = userType;
        this.introduction = introduction;
        this.imageURL = imageURL;
        this.criterias = criterias;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Criterias getCriterias() {
        return criterias;
    }

    public void setCriterias(Criterias criterias) {
        this.criterias = criterias;
    }

    @Override
    public String toString() {
        return "User{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender='" + gender + '\'' +
                ", userType='" + userType + '\'' +
                ", introduction='" + introduction + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", criterias=" + criterias +
                '}';
    }
}
