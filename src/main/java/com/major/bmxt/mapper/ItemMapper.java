package com.major.bmxt.mapper;

import com.major.bmxt.model.TbItem;
import com.major.bmxt.model.TbMatchItemAthlete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemMapper {
    List<TbItem> selectItemsByMatchId(Integer matchId);

    TbItem getItemById(Integer itemId);

    void insertItemAthlete(TbMatchItemAthlete matchItemAthlete);

    int countRegisteredNumber(@Param("itemId") int itemId, @Param("team") String team);

    TbItem selectItemById(Integer itemId);

    List<TbMatchItemAthlete> selectItemAthleteByItemIdAndTeam(@Param("itemId") Integer itemId, @Param("team") String team);

    void deleteMatchItemAthleteByItemId(@Param("id") int id, @Param("team") String team);

    TbItem selectItemByName(String name);

    void updateItemById(@Param("id") Integer id, @Param("tbItem") TbItem tbItem);

    void insertItem(TbItem tbItem);

    void deleteItemByMatchId(Integer id);

    void deleteMatchItemAthleteByMatchId(Integer id);
}
