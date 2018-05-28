package com.major.bmxt.service.impl;

import com.google.common.collect.Lists;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.major.bmxt.beans.ResultData;
import com.major.bmxt.common.PdfDeal;
import com.major.bmxt.exception.MatchException;
import com.major.bmxt.mapper.ItemMapper;
import com.major.bmxt.model.TbItem;
import com.major.bmxt.model.TbMatchItemAthlete;
import com.major.bmxt.param.UploadFileParam;
import com.major.bmxt.vo.MatchInfoTableVo;
import com.major.bmxt.vo.MatchVo;
import com.major.bmxt.mapper.MatchMapper;
import com.major.bmxt.model.TbMatch;
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

    private final ItemMapper itemMapper;

    @Autowired
    public MatchServiceImpl(MatchMapper matchMapper, ItemMapper itemMapper) {
        this.matchMapper = matchMapper;
        this.itemMapper = itemMapper;
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
        itemMapper.deleteMatchItemAthleteByMatchId(id);
        itemMapper.deleteItemByMatchId(id);
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
        TbMatch match = matchMapper.selectMatchById(matchId);
        if(match == null) {
            throw new MatchException("不存在该比赛");
        }
        List<TbMatchItemAthlete> list = matchMapper.selectMatchItemAthleteByMatchId(matchId);
        List<String> athleteList = new ArrayList<>();
        MatchInfoTableVo matchInfoTableVo = new MatchInfoTableVo();
        List<MatchInfoTableVo.ItemInfo> itemInfoList = new ArrayList<>();
        int k = 0;
        for(int i = 0; i < list.size(); i++) {
            MatchInfoTableVo.ItemInfo itemInfo = new MatchInfoTableVo().new ItemInfo();
            athleteList.add(list.get(i).getAthleteMessage());
            TbMatchItemAthlete matchItemAthlete = list.get(i);
            TbItem item = itemMapper.selectItemById(matchItemAthlete.getItemId());
            if(i==0 || list.get(i - 1).getItemId().intValue() != item.getId().intValue()) {
                itemInfo.setItemName(item.getName());
                k++;
            } else {
                itemInfo.setItemName(null);
            }
            itemInfo.setTeam(matchItemAthlete.getTeam());
            itemInfo.setAthleteName(matchItemAthlete.getAthleteMessage());
            itemInfo.setBoatId(String.valueOf(matchItemAthlete.getBoatId()));
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
