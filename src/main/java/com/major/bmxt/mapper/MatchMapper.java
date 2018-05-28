package com.major.bmxt.mapper;

import com.major.bmxt.model.TbMatch;
import com.major.bmxt.model.TbMatchItemAthlete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatchMapper {

    List<TbMatch> selectMatchs();

    TbMatch selectMatchByName(String name);

    void updateMatchById(@Param("id") Integer id, @Param("tbMatch") TbMatch tbMatch);

    void insertMatch(TbMatch tbMatch);

    TbMatch selectMatchById(Integer id);

    void changeMatchStatus(@Param("status") Integer status, @Param("id") Integer id);

    void deleteMatchById(Integer id);

    int countMatchByName(String name);

    List<TbMatchItemAthlete> selectMatchItemAthleteByMatchId(Integer id);
}
