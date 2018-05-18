package com.major.bmxt.param;

import com.major.bmxt.beans.ItemCondition;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class UploadFileParam {

    /** 赛事名称 */
    private String name;

    /** 举办地点 */
    private String host;

    /** 大项 */
    private String event;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;

    /** 分项数目 */
    private String number;

    private Map<String, ItemCondition> map;

    private List<Item> itemList;

    public class Item {

        /** 分项名称 */
        private String name;

        /** 条件约束 */
        private String condition;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", condition='" + condition + '\'' +
                    '}';
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Map<String, ItemCondition> getMap() {
        return map;
    }

    public void setMap(Map<String, ItemCondition> map) {
        this.map = map;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "UploadFileParam{" +
                "name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", event='" + event + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", number='" + number + '\'' +
                ", map=" + map +
                ", itemList=" + itemList +
                '}';
    }
}
