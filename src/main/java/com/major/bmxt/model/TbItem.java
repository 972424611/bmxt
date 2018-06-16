package com.major.bmxt.model;

import java.util.Date;

public class TbItem {

    private Integer id;

    private String name;

    private Integer number;

    private String conditions;

    private Integer maxBoats;

    private String event;

    private Integer matchId;

    private Date createTime;

    public Integer getMaxBoats() {
        return maxBoats;
    }

    public void setMaxBoats(Integer maxBoats) {
        this.maxBoats = maxBoats;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
