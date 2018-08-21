package com.major.bmxt.vo;

/**
 * 运动员信息界面
 */
public class AthleteVo {

    private int id;

    private String name;

    private String team;

    private String gender;

    private String event;

    private String birthday;

    private String photoUrl;

    private String number;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "AthleteVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", team='" + team + '\'' +
                ", gender='" + gender + '\'' +
                ", event='" + event + '\'' +
                ", birthday='" + birthday + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
