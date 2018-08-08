package com.major.bmxt.beans;

/**
 * 带有条件的分页查询
 */
public class PageQueryCondition extends PageQuery {

    private String name;

    private String gender;

    private String birthday;

    /** 比赛项目 */
    private String event;

    /** 代表队 */
    private String team;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "PageQueryCondition{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", birthday='" + birthday + '\'' +
                ", event='" + event + '\'' +
                ", team='" + team + '\'' +
                '}';
    }
}
