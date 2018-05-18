package com.major.bmxt.mapper;

import com.major.bmxt.beans.PageQuery;
import com.major.bmxt.beans.PageQueryCondition;
import com.major.bmxt.model.TbAthlete;
import com.major.bmxt.model.TbMatchItemAthlete;
import com.major.bmxt.param.AthleteParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AthleteMapper {

    void insertAthlete(TbAthlete athlete);

    List<TbAthlete> selectAthletes(@Param("pageQuery") PageQuery pageQuery, @Param("team") String team);

    int countAthleteByTeam(String team);

    void updateAthleteById(TbAthlete athlete);

    TbAthlete selectAthleteById(int id);

    int countAthleteByCondition(PageQueryCondition pageQueryCondition);

    List<TbAthlete> selectAthletesByCondition(PageQueryCondition pageQueryCondition);

    List<TbAthlete> selectAthletesByTeamAndEvent(@Param("team") String team, @Param("event") String event);

    void deleteByAthleteId(int athleteId);

    Integer countMatchItemAthleteByAthleteNumber(String number);

    int selectAthleteMaxIdByTeam(String team);
}
