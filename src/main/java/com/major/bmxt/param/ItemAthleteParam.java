package com.major.bmxt.param;

import com.major.bmxt.vo.ItemVo;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;
import java.util.Objects;

public class ItemAthleteParam {

    private List<Athlete> athleteList;

    private ItemVo itemVo;

    public static class Athlete {

        private String athleteMessage;

        private int boat;

        public String getAthleteMessage() {
            return athleteMessage;
        }

        public void setAthleteMessage(String athleteMessage) {
            this.athleteMessage = athleteMessage;
        }

        public int getBoat() {
            return boat;
        }

        public void setBoat(int boat) {
            this.boat = boat;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Athlete athlete = (Athlete) o;
            return Objects.equals(athleteMessage, athlete.athleteMessage);
        }

        @Override
        public int hashCode() {
            return Objects.hash(athleteMessage);
        }
    }

    public List<Athlete> getAthleteList() {
        return athleteList;
    }

    public void setAthleteList(List<Athlete> athleteList) {
        this.athleteList = athleteList;
    }

    public ItemVo getItemVo() {
        return itemVo;
    }

    public void setItemVo(ItemVo itemVo) {
        this.itemVo = itemVo;
    }
}
