package com.major.bmxt.vo;

import java.util.List;

/**
 * 输出的表格对应的vo
 */
public class MatchInfoTableVo {

    private String matchName;

    private String time;

    private Integer itemTotal;

    private Integer boatTotal;

    private Integer athleteTotal;

    private List<ItemInfo> itemInfoList;

    public class ItemInfo {

        private String itemName;

        private String team;

        private String athleteName;

        private String birthday;

        private String gender;

        private String event;

        private String boatId;

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getTeam() {
            return team;
        }

        public void setTeam(String team) {
            this.team = team;
        }

        public String getAthleteName() {
            return athleteName;
        }

        public void setAthleteName(String athleteName) {
            this.athleteName = athleteName;
        }

        public String getBoatId() {
            return boatId;
        }

        public void setBoatId(String boatId) {
            this.boatId = boatId;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(Integer itemTotal) {
        this.itemTotal = itemTotal;
    }

    public Integer getBoatTotal() {
        return boatTotal;
    }

    public void setBoatTotal(Integer boatTotal) {
        this.boatTotal = boatTotal;
    }

    public Integer getAthleteTotal() {
        return athleteTotal;
    }

    public void setAthleteTotal(Integer athleteTotal) {
        this.athleteTotal = athleteTotal;
    }

    public List<ItemInfo> getItemInfoList() {
        return itemInfoList;
    }

    public void setItemInfoList(List<ItemInfo> itemInfoList) {
        this.itemInfoList = itemInfoList;
    }
}
