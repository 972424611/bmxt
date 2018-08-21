package com.major.bmxt.service.impl;

import com.google.common.collect.Lists;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.major.bmxt.beans.ResultData;
import com.major.bmxt.common.PdfDeal;
import com.major.bmxt.common.RequestHolder;
import com.major.bmxt.exception.MatchException;
import com.major.bmxt.mapper.AthleteMapper;
import com.major.bmxt.mapper.ItemMapper;
import com.major.bmxt.model.*;
import com.major.bmxt.param.UploadFileParam;
import com.major.bmxt.vo.MatchInfoTableVo;
import com.major.bmxt.vo.MatchVo;
import com.major.bmxt.mapper.MatchMapper;
import com.major.bmxt.service.MatchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchMapper matchMapper;

    private final AthleteMapper athleteMapper;

    private final ItemMapper itemMapper;

    @Autowired
    public MatchServiceImpl(MatchMapper matchMapper, ItemMapper itemMapper, AthleteMapper athleteMapper) {
        this.matchMapper = matchMapper;
        this.itemMapper = itemMapper;
        this.athleteMapper = athleteMapper;
    }

    @Override
    public List<MatchVo> getMatch() {
        List<TbMatch> matchList = matchMapper.selectMatchs();
        List<MatchVo> matchVoList = Lists.newLinkedList();
        for(TbMatch tbMatch : matchList) {
            MatchVo matchVo = new MatchVo();
            BeanUtils.copyProperties(tbMatch, matchVo, "createTime");
            matchVoList.add(matchVo);
        }
        //排序
        matchVoList.sort(new Comparator<MatchVo>() {
            @Override
            public int compare(MatchVo o1, MatchVo o2) {
                return o1.getId() - o2.getId();
            }
        });
        return matchVoList;
    }

    @Override
    public void saveMatch(UploadFileParam uploadFileParam) {
        TbMatch tbMatch = new TbMatch();
        tbMatch.setCreateTime(new Date());
        tbMatch.setEvent(uploadFileParam.getEvent());
        tbMatch.setEndTime(uploadFileParam.getEndTime());
        tbMatch.setStartTime(uploadFileParam.getStartTime());
        tbMatch.setName(uploadFileParam.getName());
        tbMatch.setHost(uploadFileParam.getHost());
        tbMatch.setStatus(1);
        TbMatch match = matchMapper.selectMatchByName(uploadFileParam.getName());
        if(match != null) {
            //先删除已经绑定的先
            itemMapper.deleteItemByMatchId(match.getId());
            itemMapper.deleteMatchItemAthleteByMatchId(match.getId());
            matchMapper.updateMatchById(match.getId(), tbMatch);
        } else {
            matchMapper.insertMatch(tbMatch);
        }
    }

    @Override
    public void adjustStatus(Integer matchId, Integer status) {
        if(matchId == null || status == null) {
            throw new MatchException("请求错误, 请重新试试.");
        }
        TbMatch tbMatch = matchMapper.selectMatchById(matchId);
        if(tbMatch == null) {
            throw new MatchException("该大项不存在");
        }
        if(status != 0 && status != 1) {
            throw new MatchException("状态错误!");
        }
        matchMapper.changeMatchStatus(status, matchId);
    }

    @Override
    public void deleteMatch(Integer id) {
        if(matchMapper.selectMatchById(id) == null) {
            throw new MatchException("不存在该比赛");
        }
        //删除大项和小项运动员之间的关系
        itemMapper.deleteMatchItemAthleteByMatchId(id);
        //删除该大项对应的所有小项
        itemMapper.deleteItemByMatchId(id);
        //删除大项
        matchMapper.deleteMatchById(id);
    }

    @Override
    public ResultData verifyMatchName(String name) {
        if(StringUtils.isBlank(name)) {
            throw new MatchException("不存在该比赛");
        }
        int count = matchMapper.countMatchByName(name);
        if(count > 0) {
            return ResultData.success("666");
        }
        return ResultData.success("200");
    }

    @Override
    public MatchInfoTableVo createMatchInfoTable(Integer matchId) {
        TbUser user = RequestHolder.getCurrentUser();
        TbMatch match = matchMapper.selectMatchById(matchId);
        if(match == null) {
            throw new MatchException("不存在该比赛");
        }
        List<TbMatchItemAthlete> list;
        //如果是管理员
        if("admin".equals(user.getUsername()) || "CCA".equals(user.getUsername())) {
            list = matchMapper.selectMatchItemAthleteByMatchId(matchId);
        } else {
            list = matchMapper.selectMatchItemAthleteByMatchIdAndTeam(matchId, user.getProvince());
        }
        List<String> athleteList = new ArrayList<>();
        MatchInfoTableVo matchInfoTableVo = new MatchInfoTableVo();
        List<MatchInfoTableVo.ItemInfo> itemInfoList = new ArrayList<>();
        int k = 0;
        for(int i = 0; i < list.size(); i++) {
            MatchInfoTableVo.ItemInfo itemInfo = new MatchInfoTableVo().new ItemInfo();
            athleteList.add(list.get(i).getAthleteMessage());
            TbMatchItemAthlete matchItemAthlete = list.get(i);
            TbItem item = itemMapper.selectItemById(matchItemAthlete.getItemId());
            String athleteName = matchItemAthlete.getAthleteMessage().split("-")[0];
            String team = matchItemAthlete.getAthleteMessage().split("-")[1];
            TbAthlete athlete = athleteMapper.selectAthleteByNameAndTeam(athleteName, team);
            if(i == 0 || list.get(i - 1).getItemId().intValue() != item.getId().intValue()) {
                itemInfo.setItemName(item.getName());
                k++;
            } else {
                itemInfo.setItemName(null);
            }
            itemInfo.setTeam(matchItemAthlete.getTeam());
            itemInfo.setAthleteName(athleteName);
            itemInfo.setBoatId(String.valueOf(matchItemAthlete.getBoatId()));
            itemInfo.setGender(athlete.getGender() == 1 ? "男" : "女");
            itemInfo.setBirthday(athlete.getBirthday());
            String event = athlete.getEvent();
            if(event.contains("皮艇")) {
                itemInfo.setEvent("皮艇");
            } else if(event.contains("划艇")) {
                itemInfo.setEvent("划艇");
            } else if(event.contains("激流")) {
                itemInfo.setEvent("激流");
            }
            itemInfoList.add(itemInfo);
        }
        Set<String> set = new HashSet<>(athleteList);
        matchInfoTableVo.setItemInfoList(itemInfoList);
        matchInfoTableVo.setAthleteTotal(set.size());
        matchInfoTableVo.setItemTotal(k);
        matchInfoTableVo.setBoatTotal(list.size());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String createTime = simpleDateFormat.format(match.getCreateTime());
        matchInfoTableVo.setTime(createTime);
        matchInfoTableVo.setMatchName(match.getName());
        return matchInfoTableVo;
    }
}
