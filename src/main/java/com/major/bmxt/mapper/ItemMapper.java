package com.major.bmxt.mapper;

import com.major.bmxt.model.TbItem;
import com.major.bmxt.model.TbMatchItemAthlete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemMapper {
    List<TbItem> selectItemsByMatchId(Integer matchId);

    TbItem getItemById(Integer itemId);

    void insertItemAthlete(TbMatchItemAthlete matchItemAthlete);

    int countRegisteredNumber(int itemId);

    TbItem selectItemById(Integer itemId);

    List<TbMatchItemAthlete> selectItemAthleteByItemId(Integer itemId);

    void deleteItemAthleteByItemId(int id);

    TbItem selectItemByName(String name);

    void updateItemById(@Param("id") Integer id, @Param("tbItem") TbItem tbItem);

    void insertItem(TbItem tbItem);

    void deleteItemByMatchId(Integer id);

    void deleteMatchItemAthleteByMatchId(Integer id);
}
