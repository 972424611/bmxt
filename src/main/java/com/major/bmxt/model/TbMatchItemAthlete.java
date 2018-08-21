package com.major.bmxt.model;

public class TbMatchItemAthlete {

    private Integer matchId;

    private Integer itemId;

    //TODO 后期这个属性要改，要用身份证来替代
    private String athleteMessage;

    private Integer boatId;

    private String team;

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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "TbMatchItemAthlete{" +
                "matchId=" + matchId +
                ", itemId=" + itemId +
                ", athleteMessage='" + athleteMessage + '\'' +
                ", boatId=" + boatId +
                ", team='" + team + '\'' +
                '}';
    }
}
