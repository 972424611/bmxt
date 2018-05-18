package com.major.bmxt.service.impl;

import com.google.common.collect.Lists;
import com.major.bmxt.exception.MatchException;
import com.major.bmxt.mapper.ItemMapper;
import com.major.bmxt.param.UploadFileParam;
import com.major.bmxt.vo.MatchVo;
import com.major.bmxt.mapper.MatchMapper;
import com.major.bmxt.model.TbMatch;
import com.major.bmxt.service.MatchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
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
        if(status != 0 || status != 1) {
            throw new MatchException("状态错误!");
        }
        matchMapper.changeMatchStatus(status, matchId);
    }
}
