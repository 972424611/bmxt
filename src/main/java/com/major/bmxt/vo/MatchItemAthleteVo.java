package com.major.bmxt.vo;

/**
 * 小项添加运动员弹出的对话框对应的vo(该小项已经添加的运动员)
 */
public class MatchItemAthleteVo {

    private String athleteMessage;

    private int boatId;

    public String getAthleteMessage() {
        return athleteMessage;
    }

    public void setAthleteMessage(String athleteMessage) {
        this.athleteMessage = athleteMessage;
    }

    public int getBoatId() {
        return boatId;
    }

    public void setBoatId(int boatId) {
        this.boatId = boatId;
    }
}
