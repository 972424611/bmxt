package com.major.bmxt.mapper;

import com.major.bmxt.model.TbMatch;
import com.major.bmxt.model.TbMatchItemAthlete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatchMapper {

    /**
     * 获取大项
     * @return List<TbMatch>
     */
    List<TbMatch> selectMatchs();

    /**
     * 获取大项通过名称
     * @param name 大项名称
     * @return TbMatch
     */
    TbMatch selectMatchByName(String name);

    /**
     * 更新大项通过id
     * @param id 大项id
     * @param tbMatch 待用来替换的大项
     */
    void updateMatchById(@Param("id") Integer id, @Param("tbMatch") TbMatch tbMatch);

    /**
     * 插入大项
     * @param tbMatch tbMatch
     */
    void insertMatch(TbMatch tbMatch);

    /**
     * 获取大项通过大项id
     * @param id 大项id
     * @return TbMatch
     */
    TbMatch selectMatchById(Integer id);

    /**
     * 更改大项的状态
     * @param status 1-正在进行、0-关闭
     * @param id 大项id
     */
    void changeMatchStatus(@Param("status") Integer status, @Param("id") Integer id);

    /**
     * 删除大项通过id
     * @param id 大项id
     */
    void deleteMatchById(Integer id);

    /**
     * 获取大项的数量用过名称(用来判断大项是否唯一)
     * @param name 大项名称
     * @return int
     */
    int countMatchByName(String name);

    /**
     * 获取大项、小项、运动员通过大项id(管理员调用)
     * @param id 大项id
     * @return List<TbMatchItemAthlete>
     */
    List<TbMatchItemAthlete> selectMatchItemAthleteByMatchId(Integer id);

    /**
     * 获取大项、小项、运动员通过大项id和省份(非管理员调用)
     * @param id 大项id
     * @param team 省份(像广东这样的中文名称)
     * @return
     */
    List<TbMatchItemAthlete> selectMatchItemAthleteByMatchIdAndTeam(@Param("id") Integer id,@Param("team") String team);
}
