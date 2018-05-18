package com.major.bmxt.service.impl;

import com.google.common.collect.Lists;
import com.major.bmxt.beans.ItemCondition;
import com.major.bmxt.common.BeanValidator;
import com.major.bmxt.common.RequestHolder;
import com.major.bmxt.mapper.MatchMapper;
import com.major.bmxt.model.TbMatch;
import com.major.bmxt.param.UploadFileParam;
import com.major.bmxt.vo.ItemVo;
import com.major.bmxt.vo.MatchItemAthleteVo;
import com.major.bmxt.exception.ItemException;
import com.major.bmxt.mapper.ItemMapper;
import com.major.bmxt.model.TbItem;
import com.major.bmxt.model.TbMatchItemAthlete;
import com.major.bmxt.param.ItemAthleteParam;
import com.major.bmxt.service.ItemService;
import com.major.bmxt.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;

    private final MatchMapper matchMapper;

    @Autowired
    public ItemServiceImpl(ItemMapper itemMapper, MatchMapper matchMapper) {
        this.itemMapper = itemMapper;
        this.matchMapper = matchMapper;
    }

    @Override
    public List<ItemVo> getItemsByMatchId(Integer matchId) {
        List<TbItem> itemList = itemMapper.selectItemsByMatchId(matchId);
        if(itemList == null || itemList.size() < 1) {
            return Lists.newArrayList();
        }
        List<ItemVo> itemVoList = Lists.newLinkedList();
        for(TbItem tbItem : itemList) {
            ItemVo itemVo = new ItemVo();
            BeanUtils.copyProperties(tbItem, itemVo);
            String condition = tbItem.getConditions();
            if(StringUtils.isNotBlank(condition)) {
                ItemCondition itemCondition = JsonUtils.jsonToPojo(condition, ItemCondition.class);
                itemVo.setItemCondition(itemCondition);
            }
            int count = itemMapper.countRegisteredNumber(itemVo.getId());
            count = count / itemVo.getNumber();
            itemVo.setRegisteredNumber(count);
            itemVoList.add(itemVo);
        }
        return itemVoList;
    }

    @Override
    public void saveItemAthlete(ItemAthleteParam itemAthleteParam) {
        ItemVo itemVo = itemAthleteParam.getItemVo();
        List<ItemAthleteParam.Athlete> athleteList = itemAthleteParam.getAthleteList();
        Set<ItemAthleteParam.Athlete> athleteSet = new HashSet<>(athleteList);
        if(athleteSet.size() != athleteList.size()) {
            throw new ItemException("同一场比赛一个人只能报一个赛艇");
        }
        //先把以前的全部删除
        itemMapper.deleteItemAthleteByItemId(itemVo.getId());
        TbItem item = itemMapper.selectItemById(itemVo.getId());
        for(ItemAthleteParam.Athlete athlete : athleteList) {
            TbMatchItemAthlete matchItemAthlete = new TbMatchItemAthlete();
            matchItemAthlete.setMatchId(item.getMatchId());
            matchItemAthlete.setAthleteMessage(athlete.getAthleteMessage());
            matchItemAthlete.setBoatId(athlete.getBoat());
            matchItemAthlete.setItemId(itemVo.getId());
            System.out.println(matchItemAthlete.toString());
            itemMapper.insertItemAthlete(matchItemAthlete);
        }
    }

    @Override
    public List<MatchItemAthleteVo> getItemAthlete(Integer itemId) {
        TbItem item = itemMapper.selectItemById(itemId);
        if(item == null) {
            throw new ItemException("不存在该小项目");
        }
        List<TbMatchItemAthlete> matchItemAthleteList = itemMapper.selectItemAthleteByItemId(itemId);
        List<MatchItemAthleteVo> matchItemAthleteVoList = Lists.newArrayList();
        for(TbMatchItemAthlete matchItemAthlete : matchItemAthleteList) {
            MatchItemAthleteVo matchItemAthleteVo = new MatchItemAthleteVo();
            //int index = matchItemAthlete.getAthleteMessage().lastIndexOf("-");
            //String athleteMessage = matchItemAthlete.getAthleteMessage().substring(0, index);
            matchItemAthleteVo.setAthleteMessage(matchItemAthlete.getAthleteMessage());
            matchItemAthleteVo.setBoatId(matchItemAthlete.getBoatId());
            matchItemAthleteVoList.add(matchItemAthleteVo);
        }
        return matchItemAthleteVoList;
    }

    @Override
    public void saveItem(UploadFileParam uploadFileParam) {
        Map<String, ItemCondition> map = uploadFileParam.getMap();
        List<UploadFileParam.Item> itemList = uploadFileParam.getItemList();
        for(UploadFileParam.Item item : itemList) {
            TbItem tbItem = new TbItem();
            ItemCondition itemCondition = map.get("条件" + item.getCondition());
            if(itemCondition == null) {
                itemCondition = new ItemCondition();
            }
            if(item.getName().startsWith("M")) {
                itemCondition.setGender(1);
            } else if(item.getName().startsWith("W")) {
                itemCondition.setGender(2);
            }
            tbItem.setConditions(JsonUtils.objectToJson(itemCondition));
            tbItem.setCreateTime(new Date());
            tbItem.setEvent(uploadFileParam.getEvent());
            TbMatch tbMatch = matchMapper.selectMatchByName(uploadFileParam.getName());
            tbItem.setMatchId(tbMatch.getId());
            tbItem.setName(item.getName());
            tbItem.setNumber(Integer.valueOf(item.getName().charAt(2) + ""));
            itemMapper.insertItem(tbItem);
        }
    }
}
