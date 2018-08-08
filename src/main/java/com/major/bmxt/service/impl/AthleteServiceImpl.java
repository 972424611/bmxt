package com.major.bmxt.service.impl;

import com.google.common.collect.Lists;
import com.major.bmxt.beans.ItemCondition;
import com.major.bmxt.beans.PageQuery;
import com.major.bmxt.beans.PageQueryCondition;
import com.major.bmxt.beans.PageResult;
import com.major.bmxt.common.BeanValidator;
import com.major.bmxt.common.RequestHolder;
import com.major.bmxt.mapper.MatchMapper;
import com.major.bmxt.mapper.UserMapper;
import com.major.bmxt.model.*;
import com.major.bmxt.utils.PropertyUtil;
import com.major.bmxt.vo.AthleteVo;
import com.major.bmxt.exception.AthleteException;
import com.major.bmxt.mapper.AthleteMapper;
import com.major.bmxt.mapper.ItemMapper;
import com.major.bmxt.param.AthleteParam;
import com.major.bmxt.service.AthleteService;
import com.major.bmxt.service.UploadService;
import com.major.bmxt.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AthleteServiceImpl implements AthleteService {

    @Value("${PHOTO_URL}")
    private String photoUrl;

    private final UploadService uploadService;

    private final AthleteMapper athleteMapper;

    private final ItemMapper itemMapper;

    private final MatchMapper matchMapper;

    @Autowired
    public AthleteServiceImpl(AthleteMapper athleteMapper, UploadService uploadService,
                              ItemMapper itemMapper, MatchMapper matchMapper) {
        this.athleteMapper = athleteMapper;
        this.uploadService = uploadService;
        this.itemMapper = itemMapper;
        this.matchMapper = matchMapper;
    }

    @Override
    public void saveAthlete(AthleteParam athleteParam) {
        BeanValidator.check(athleteParam);
        if(PropertyUtil.getTeamProperty(athleteParam.getTeam()) == null) {
            throw new AthleteException("代表队错误");
        }
        if(StringUtils.isBlank(athleteParam.getPhotoName())) {
            athleteParam.setPhotoName(null);
        }
        TbAthlete athlete = new TbAthlete();
        BeanUtils.copyProperties(athleteParam, athlete, "gender");
        String gender = athleteParam.getGender();
        athlete.setGender("男".equals(gender) ? 1 : 2);
        athlete.setCreateTime(new Date());
        athlete.setUpdateTime(new Date());
        athleteMapper.insertAthlete(athlete);
    }

    @Override
    public PageResult<AthleteVo> getAthletesByCondition(PageQueryCondition pageQueryCondition) {
        BeanValidator.check(pageQueryCondition);
        //判断条件查询中性别是否为空
        if(StringUtils.isBlank(pageQueryCondition.getGender())) {
            pageQueryCondition.setGender(null);
        } else {
            pageQueryCondition.setGender("男".equals(pageQueryCondition.getGender()) ? "1" : "2");
        }
        //判断条件查询小项是否为空
        if(StringUtils.isBlank(pageQueryCondition.getEvent())) {
            pageQueryCondition.setEvent(null);
        } else {
            pageQueryCondition.setEvent("%" + pageQueryCondition.getEvent() + "%");
        }
        int count = athleteMapper.countAthleteByCondition(pageQueryCondition);
        if(count < 1) {
            return new PageResult<>();
        }
        List<TbAthlete> athleteList = athleteMapper.selectAthletesByCondition(pageQueryCondition);
        List<AthleteVo> athleteVoList = Lists.newLinkedList();
        //把实体 tb -> vo
        for(TbAthlete athlete : athleteList) {
            AthleteVo athleteVo = new AthleteVo();
            BeanUtils.copyProperties(athlete, athleteVo, "gender");
            String team = PropertyUtil.getTeamProperty(athlete.getTeam());
            String photoUrl = uploadService.getPictureAddress(team, athlete.getPhotoName());
            athleteVo.setGender(athlete.getGender() == 1 ? "男" : "女");
            if(photoUrl != null) {
                photoUrl = this.photoUrl + team + "/" + athlete.getPhotoName();
                athleteVo.setPhotoUrl(photoUrl);
            }
            athleteVoList.add(athleteVo);
        }
        PageResult<AthleteVo> pageResult = new PageResult<>();
        pageResult.setData(athleteVoList);
        pageResult.setTotal(count);
        return pageResult;
    }

    @Override
    public List<String> getAthletesByItem(Integer itemId) {
        TbItem item = itemMapper.getItemById(itemId);
        ItemCondition itemCondition = JsonUtils.jsonToPojo(item.getConditions(), ItemCondition.class);
        String team = RequestHolder.getCurrentUser().getProvince();
        if(team == null) {
            throw new AthleteException("请先登录");
        }
        //权限判断
        String username = RequestHolder.getCurrentUser().getUsername();
        if("CCA".equals(username) || "admin".equals(username)) {
            team = null;
        }
        String itemStr = '%' + item.getEvent() + '%';
        List<TbAthlete> athleteList = athleteMapper.selectAthletesByTeamAndEvent(team, itemStr);
        List<String> athleteStrList = Lists.newLinkedList();
        //如果该小项没有条件
        if(itemCondition == null) {
            for(TbAthlete tbAthlete : athleteList) {
                String str = tbAthlete.getName();
                athleteStrList.add(str);
            }
            return athleteStrList;
        }
        for(TbAthlete tbAthlete : athleteList) {
            boolean flag = true;
            String startTime = itemCondition.getStartTime();
            String endTime = itemCondition.getEndTime();
            Integer gender = itemCondition.getGender();
            String date = tbAthlete.getBirthday();
            //如果有出生日期起始时间限制
            if(startTime != null && date.compareTo(startTime) < 0) {
                flag = false;
            }
            //如果有出生日期截止时间限制
            if(endTime != null && date.compareTo(endTime) > 0) {
                flag = false;
            }
            //如果有性别限制
            if(gender != null) {
                if(gender.intValue() != tbAthlete.getGender().intValue()) {
                    flag = false;
                }
            }
            //以上条件需要同时满足，才能添加
            if(flag) {
                String str = tbAthlete.getName() + "-" + tbAthlete.getTeam();
                athleteStrList.add(str);
            }
        }
        return athleteStrList;
    }

    @Override
    public void deleteByAthleteId(String athleteId) {
        if(athleteId == null) {
            throw new AthleteException("未找到该运动员，删除失败");
        }
        Integer id = Integer.valueOf(athleteId);
        TbAthlete athlete = athleteMapper.selectAthleteById(id);
        if(athlete == null) {
            throw new AthleteException("未能找到该运动员");
        }
        String number = "%"+ athlete.getName() + "-" + athlete.getTeam() +"%";
        List<TbMatchItemAthlete> list = athleteMapper.selectMatchItemAthleteByAthleteNumber(number);
        if(list.size() > 0) {
            for(TbMatchItemAthlete matchItemAthlete : list) {
                //判断该运动员是否报名项目
                TbMatch tbMatch = matchMapper.selectMatchById(matchItemAthlete.getMatchId());
                //判断该大项是否正在进行中
                if(tbMatch.getStatus() == 1) {
                    throw new AthleteException("该运动员已经报名[" + tbMatch.getName() + "]比赛，请先删除该运动员的比赛项目");
                }
            }
        }
        athleteMapper.deleteByAthleteId(id);
    }

    @Override
    public PageResult<AthleteVo> getAthletes(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        //权限判断
        TbUser user = RequestHolder.getCurrentUser();
        String team;
        if("admin".equals(user.getUsername()) || "CCA".equals(user.getUsername())) {
            team = null;
        } else {
            team = user.getProvince();
        }
        int count = athleteMapper.countAthleteByTeam(team);
        if(count < 1) {
            return new PageResult<>();
        }
        List<TbAthlete> athleteList = athleteMapper.selectAthletes(pageQuery, team);
        List<AthleteVo> athleteVoList = Lists.newLinkedList();
        for(TbAthlete athlete : athleteList) {
            AthleteVo athleteVo = new AthleteVo();
            BeanUtils.copyProperties(athlete, athleteVo, "gender");
            athleteVo.setGender(athlete.getGender() == 1 ? "男" : "女");
            team = PropertyUtil.getTeamProperty(athlete.getTeam());
            String photoUrl = uploadService.getPictureAddress(team, athlete.getPhotoName());
            if(photoUrl != null) {
                //photoUrl = "file://" + photoUrl;
                photoUrl = this.photoUrl + team + "/" + athlete.getPhotoName();
                athleteVo.setPhotoUrl(photoUrl);
            }
            athleteVoList.add(athleteVo);
        }
        PageResult<AthleteVo> pageResult = new PageResult<>();
        pageResult.setData(athleteVoList);
        pageResult.setTotal(count);
        return pageResult;
    }

    @Override
    public void updateAthlete(AthleteParam athleteParam) {
        BeanValidator.check(athleteParam);
        if(PropertyUtil.getTeamProperty(athleteParam.getTeam()) == null) {
            throw new AthleteException("代表队错误");
        }
        TbAthlete tbAthlete = athleteMapper.selectAthleteById(athleteParam.getId());
        boolean flag = true;
        //内容如果有变化的
        if(!tbAthlete.getTeam().equals(athleteParam.getTeam())
                || !tbAthlete.getName().equals(athleteParam.getName())
                || !tbAthlete.getBirthday().equals(athleteParam.getBirthday())
                || !tbAthlete.getEvent().equals(athleteParam.getEvent())
                || !tbAthlete.getGender().equals("男".equals(athleteParam.getGender()) ? 1 : 2)) {
            flag = false;
        }
        String number = "%"+ tbAthlete.getName() + "-" + tbAthlete.getTeam() +"%";
        List<TbMatchItemAthlete> list = athleteMapper.selectMatchItemAthleteByAthleteNumber(number);
        if(list.size() > 0 && !flag) {
            for(TbMatchItemAthlete matchItemAthlete : list) {
                int matchId = matchItemAthlete.getMatchId();
                TbMatch tbMatch = matchMapper.selectMatchById(matchId);
                if(tbMatch.getStatus() == 1) {
                    throw new AthleteException("该运动员已经报名[" + tbMatch.getName() + "]比赛，请先删除该运动员的比赛项目");
                }
            }
        }
        TbAthlete athlete = new TbAthlete();
        BeanUtils.copyProperties(athleteParam, athlete, "gender");
        String gender = athleteParam.getGender();
        athlete.setGender("男".equals(gender) ? 1 : 2);
        athlete.setUpdateTime(new Date());
        //TODO 这里以后要改，加入身份证
        athlete.setNumber(null);
        athleteMapper.updateAthleteById(athlete);
    }
}
