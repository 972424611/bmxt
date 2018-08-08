package com.major.bmxt.mapper;

import com.major.bmxt.model.TbItem;
import com.major.bmxt.model.TbMatchItemAthlete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemMapper {

    /**
     * 获取比赛小项目通过大项id
     * @param matchId 大项id
     * @return List<TbItem>
     */
    List<TbItem> selectItemsByMatchId(Integer matchId);

    /**
     * 获取小项通过id
     * @param itemId 比赛小项id
     * @return TbItem
     */
    TbItem getItemById(Integer itemId);

    /**
     * 保存参加该比赛小项的运动员
     * @param matchItemAthlete  matchItemAthlete
     */
    void insertItemAthlete(TbMatchItemAthlete matchItemAthlete);

    /**
     * 获取已经报名该小项的队伍数目
     * @param itemId 小项id
     * @param team 代表队
     * @return int
     */
    int countRegisteredNumber(@Param("itemId") int itemId, @Param("team") String team);

    /**
     * 获取比赛小项通过小项id
     * @param itemId 小项id
     * @return TbItem
     */
    TbItem selectItemById(Integer itemId);

    /**
     * 获取已经报名小项的运动员通过小项和代表队
     * @param itemId 小项id
     * @param team 代表队
     * @return List<TbMatchItemAthlete>
     */
    List<TbMatchItemAthlete> selectItemAthleteByItemIdAndTeam(@Param("itemId") Integer itemId, @Param("team") String team);

    /**
     * 删除大项和小项之间的关系通过小项id
     * @param id 小项id
     * @param team 代表队
     */
    void deleteMatchItemAthleteByItemId(@Param("id") int id, @Param("team") String team);

    /**
     * 更新小项
     * @param id 小项id
     * @param tbItem 代替换小项
     */
    void updateItemById(@Param("id") Integer id, @Param("tbItem") TbItem tbItem);

    /**
     * 插入小项
     * @param tbItem 小项
     */
    void insertItem(TbItem tbItem);

    /**
     * 删除小项通过大项id
     * @param id 大项id
     */
    void deleteItemByMatchId(Integer id);

    /**
     * 删除大项和小项之间的关系通过大项id
     * @param id 大项id
     */
    void deleteMatchItemAthleteByMatchId(Integer id);
}
