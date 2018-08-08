package com.major.bmxt.beans;

/**
 * 比赛小项目的条件限制
 */
public class ItemCondition {

    /** 出生年月的显示起始时间 */
    private String startTime;

    /** 出生年月的显示终止时间 */
    private String endTime;

    /** 性别限制 */
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
