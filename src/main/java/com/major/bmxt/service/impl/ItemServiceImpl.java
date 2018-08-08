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
import org.omg.CORBA.Request;
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
        String username = RequestHolder.getCurrentUser().getUsername();
        String team = RequestHolder.getCurrentUser().getProvince();
        for(TbItem tbItem : itemList) {
            ItemVo itemVo = new ItemVo();
            BeanUtils.copyProperties(tbItem, itemVo);
            String condition = tbItem.getConditions();
            if(StringUtils.isNotBlank(condition)) {
                ItemCondition itemCondition = JsonUtils.jsonToPojo(condition, ItemCondition.class);
                itemVo.setItemCondition(itemCondition);
            }
            int count;
            if("CCA".equals(username) || "admin".equals(username)) {
                count = itemMapper.countRegisteredNumber(itemVo.getId(), null);
            } else {
                count = itemMapper.countRegisteredNumber(itemVo.getId(), team);
            }
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
            throw new ItemException("同一项比赛中选手重复报名");
        }
        String username = RequestHolder.getCurrentUser().getUsername();
        String team = RequestHolder.getCurrentUser().getProvince();
        if("CCA".equals(username) || "admin".equals(username)) {
            team = null;
        }
        //先把以前的全部删除
        itemMapper.deleteMatchItemAthleteByItemId(itemVo.getId(), team);
        TbItem item = itemMapper.selectItemById(itemVo.getId());
        for(ItemAthleteParam.Athlete athlete : athleteList) {
            TbMatchItemAthlete matchItemAthlete = new TbMatchItemAthlete();
            matchItemAthlete.setMatchId(item.getMatchId());
            matchItemAthlete.setAthleteMessage(athlete.getAthleteMessage());
            matchItemAthlete.setBoatId(athlete.getBoat());
            matchItemAthlete.setItemId(itemVo.getId());
            team = athlete.getAthleteMessage().split("-")[1];
            matchItemAthlete.setTeam(team);
            itemMapper.insertItemAthlete(matchItemAthlete);
        }
    }

    @Override
    public List<MatchItemAthleteVo> getItemAthlete(Integer itemId) {
        TbItem item = itemMapper.selectItemById(itemId);
        if(item == null) {
            throw new ItemException("不存在该小项目");
        }
        String username = RequestHolder.getCurrentUser().getUsername();
        String team;
        if("CCA".equals(username) || "admin".equals(username)) {
            team = null;
        } else {
            team = RequestHolder.getCurrentUser().getProvince();
        }
        List<TbMatchItemAthlete> matchItemAthleteList = itemMapper.selectItemAthleteByItemIdAndTeam(itemId, team);
        List<MatchItemAthleteVo> matchItemAthleteVoList = Lists.newArrayList();
        int i = 1;
        Iterator iterator = matchItemAthleteList.iterator();
        //下面的代码逻辑可能有些复杂，主要就是tb -> vo
        if("CCA".equals(username) || "admin".equals(username)) {
            while(iterator.hasNext()) {
                TbMatchItemAthlete matchItemAthlete = (TbMatchItemAthlete) iterator.next();
                if(matchItemAthlete == null) {
                    iterator.remove();
                    continue;
                }
                MatchItemAthleteVo matchItemAthleteVo = new MatchItemAthleteVo();
                matchItemAthleteVo.setAthleteMessage(matchItemAthlete.getAthleteMessage());
                matchItemAthleteVo.setBoatId(i);
                matchItemAthleteVoList.add(matchItemAthleteVo);
                for(TbMatchItemAthlete tbMatchItemAthlete : matchItemAthleteList) {
                    if(tbMatchItemAthlete == null) {
                        break;
                    }
                    if(tbMatchItemAthlete.getAthleteMessage().equals(matchItemAthlete.getAthleteMessage())) {
                        continue;
                    }
                    //看看是否是同一个代表队和同一个船
                    if(matchItemAthlete.getBoatId().intValue() == tbMatchItemAthlete.getBoatId().intValue()
                            && matchItemAthlete.getTeam().equals(tbMatchItemAthlete.getTeam())) {
                        MatchItemAthleteVo matchItemAthleteVo2 = new MatchItemAthleteVo();
                        matchItemAthleteVo2.setAthleteMessage(tbMatchItemAthlete.getAthleteMessage());
                        matchItemAthleteVo2.setBoatId(i);
                        matchItemAthleteVoList.add(matchItemAthleteVo2);
                        matchItemAthleteList.set(matchItemAthleteList.indexOf(tbMatchItemAthlete), null);
                    }
                }
                //查找过的就去除(优化)
                iterator.remove();
                i++;
            }
        } else {
            for(TbMatchItemAthlete matchItemAthlete : matchItemAthleteList) {
                MatchItemAthleteVo matchItemAthleteVo = new MatchItemAthleteVo();
                matchItemAthleteVo.setAthleteMessage(matchItemAthlete.getAthleteMessage());
                matchItemAthleteVo.setBoatId(matchItemAthlete.getBoatId());
                matchItemAthleteVoList.add(matchItemAthleteVo);
            }
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
            String[] sign = item.getName().split(" ");
            if(sign[0].contains("K")) {
                tbItem.setEvent("皮划艇静水(皮艇)");
            } else if(sign[0].contains("C")) {
                tbItem.setEvent("皮划艇静水(划艇)");
            }
            if(item.getName().contains("跨界")) {
                tbItem.setEvent("皮划艇静水");
            }
            TbMatch tbMatch = matchMapper.selectMatchByName(uploadFileParam.getName());
            tbItem.setMatchId(tbMatch.getId());
            tbItem.setName(item.getName());
            tbItem.setNumber(Integer.valueOf(item.getName().charAt(2) + ""));
            tbItem.setMaxBoats(item.getMaxBoats());
            itemMapper.insertItem(tbItem);
        }
    }
}
