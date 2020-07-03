package com.example.myfitnessbuddy.models;

public class SuccessStory {

    private String title;
    private String beforeImageUrl;
    private String afterImageUrl;

    public SuccessStory() {
        this.title = "";
        this.beforeImageUrl = "";
        this.afterImageUrl = "";
    }

    public SuccessStory(String title, String beforeImageUrl, String afterImageUrl) {
        this.title = title;
        this.beforeImageUrl = beforeImageUrl;
        this.afterImageUrl = afterImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBeforeImageUrl() {
        return beforeImageUrl;
    }

    public void setBeforeImageUrl(String beforeImageUrl) {
        this.beforeImageUrl = beforeImageUrl;
    }

    public String getAfterImageUrl() {
        return afterImageUrl;
    }

    public void setAfterImageUrl(String afterImageUrl) {
        this.afterImageUrl = afterImageUrl;
    }

    @Override
    public String toString() {
        return "SuccessStory{" +
                "title='" + title + '\'' +
                ", beforeImageUrl='" + beforeImageUrl + '\'' +
                ", afterImageUrl='" + afterImageUrl + '\'' +
                '}';
    }
}
