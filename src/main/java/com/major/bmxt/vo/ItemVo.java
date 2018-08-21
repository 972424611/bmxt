package com.major.bmxt.vo;

import com.major.bmxt.beans.ItemCondition;

/**
 * 小项信息界面对应的vo
 */
public class ItemVo {

    private int id;

    private String name;

    private int registeredNumber;

    private int maxBoats;

    private int number;

    private ItemCondition itemCondition;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ItemCondition getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(ItemCondition itemCondition) {
        this.itemCondition = itemCondition;
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

    public int getRegisteredNumber() {
        return registeredNumber;
    }

    public void setRegisteredNumber(int registeredNumber) {
        this.registeredNumber = registeredNumber;
    }

    public int getMaxBoats() {
        return maxBoats;
    }

    public void setMaxBoats(int maxBoats) {
        this.maxBoats = maxBoats;
    }

    @Override
    public String toString() {
        return "ItemVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", registeredNumber=" + registeredNumber +
                ", maxBoats=" + maxBoats +
                ", number=" + number +
                ", itemCondition=" + itemCondition +
                '}';
    }
}
