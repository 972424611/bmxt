package com.major.bmxt.mapper;

import com.major.bmxt.beans.PageQuery;
import com.major.bmxt.beans.PageQueryCondition;
import com.major.bmxt.model.TbAthlete;
import com.major.bmxt.model.TbMatchItemAthlete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AthleteMapper {

    /**
     * 新增运动员
     * @param athlete 运动员
     */
    void insertAthlete(TbAthlete athlete);

    /**
     * 查找运动员
     * @param pageQuery pageQuery
     * @param team 代表队
     * @return List<TbAthlete>
     */
    List<TbAthlete> selectAthletes(@Param("pageQuery") PageQuery pageQuery, @Param("team") String team);

    /**
     * 根据代表队获取运动员数量
     * @param team 代表队
     * @return int
     */
    int countAthleteByTeam(@Param("team") String team);

    /**
     * 更新运动员信息通过id
     * @param athlete 运动员
     */
    void updateAthleteById(TbAthlete athlete);

    /**
     * 查找运动员通过id
     * @param id 运动员id
     * @return TbAthlete
     */
    TbAthlete selectAthleteById(int id);

    /**
     * 根据条件获取运动员数量
     * @param pageQueryCondition 详细条件
     * @return int
     */
    int countAthleteByCondition(PageQueryCondition pageQueryCondition);

    /**
     * 获取运动员通过条件
     * @param pageQueryCondition 详细条件
     * @return List<TbAthlete>
     */
    List<TbAthlete> selectAthletesByCondition(PageQueryCondition pageQueryCondition);

    /**
     * 获取运动员通过代表队和比赛小项
     * @param team 代表队
     * @param event 小项
     * @return List<TbAthlete>
     */
    List<TbAthlete> selectAthletesByTeamAndEvent(@Param("team") String team, @Param("event") String event);

    /**
     * 删除运动员通过id
     * @param athleteId 运动员id
     */
    void deleteByAthleteId(int athleteId);

    /**
     *
     * @param number 运动员编号
     * @return List<TbMatchItemAthlete>
     */
    List<TbMatchItemAthlete> selectMatchItemAthleteByAthleteNumber(String number);
}
