package com.major.bmxt.param;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class AthleteParam {

    private int id;

    @NotBlank(message = "运动员名称不能为空")
    private String name;

    private String team;

    @NotBlank(message = "性别不能为空")
    private String gender;

    @NotBlank(message = "出生日期不能为空")
    @Length(min = 5, max = 18, message = "请输入正确格式的身份证")
    private String birthday;

    @NotBlank(message = "请选择要参加的比赛")
    private String event;

    private String photoName;

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

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }
}
