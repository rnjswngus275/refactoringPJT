package com.example.yoony.opensourceandroidproject.db.model;


public class GoalSub {
    private int indexNumber;
    private String addedByUser;
    private String subTitle;
    private int disable;

    public GoalSub() {
    }

    public GoalSub(String addedByUser, String subTitle, int disable) {
        this.addedByUser = addedByUser;
        this.subTitle = subTitle;
        this.disable = disable;
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getAddedByUser() {
        return addedByUser;
    }

    public void setAddedByUser(String addedByUser) {
        this.addedByUser = addedByUser;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getDisable() {
        return disable;
    }

    public void setDisable(int disable) {
        this.disable = disable;
    }

    @Override
    public String toString() {
        return "SubGoal{" +
                "indexNumber=" + indexNumber +
                ", addedByUser='" + addedByUser + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", disable=" + disable +
                '}';
    }
}
