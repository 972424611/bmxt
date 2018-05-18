package com.major.bmxt.model;

public class TbMatchItemAthlete {

    private Integer matchId;

    private Integer itemId;

    private String athleteMessage;

    private Integer boatId;

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getAthleteMessage() {
        return athleteMessage;
    }

    public void setAthleteMessage(String athleteMessage) {
        this.athleteMessage = athleteMessage;
    }

    public Integer getBoatId() {
        return boatId;
    }

    public void setBoatId(Integer boatId) {
        this.boatId = boatId;
    }

    @Override
    public String toString() {
        return "TbMatchItemAthlete{" +
                "matchId=" + matchId +
                ", itemId=" + itemId +
                ", athleteMessage='" + athleteMessage + '\'' +
                ", boatId=" + boatId +
                '}';
    }
}
