package com.major.bmxt.beans;

public class ItemCondition {

    private String startTime;

    private String endTime;

    private Integer gender;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "ItemCondition{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", gender=" + gender +
                '}';
    }
}
