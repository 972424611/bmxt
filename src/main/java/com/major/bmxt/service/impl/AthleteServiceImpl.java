package com.major.bmxt.service.impl;

import com.google.common.collect.Lists;
import com.major.bmxt.beans.ItemCondition;
import com.major.bmxt.beans.PageQuery;
import com.major.bmxt.beans.PageQueryCondition;
import com.major.bmxt.beans.PageResult;
import com.major.bmxt.common.BeanValidator;
import com.major.bmxt.common.RequestHolder;
import com.major.bmxt.vo.AthleteVo;
import com.major.bmxt.exception.AthleteException;
import com.major.bmxt.mapper.AthleteMapper;
import com.major.bmxt.mapper.ItemMapper;
import com.major.bmxt.model.TbAthlete;
import com.major.bmxt.model.TbItem;
import com.major.bmxt.model.TbUser;
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

    @Autowired
    public AthleteServiceImpl(AthleteMapper athleteMapper, UploadService uploadService,
                              ItemMapper itemMapper) {
        this.athleteMapper = athleteMapper;
        this.uploadService = uploadService;
        this.itemMapper = itemMapper;
    }

    @Override
    public void saveAthlete(AthleteParam athleteParam) {
        BeanValidator.check(athleteParam);
        TbAthlete athlete = new TbAthlete();
        BeanUtils.copyProperties(athleteParam, athlete, "gender");
        String gender = athleteParam.getGender();
        athlete.setGender("男".equals(gender) ? 1 : 2);
        athlete.setCreateTime(new Date());
        athlete.setUpdateTime(new Date());
        TbUser tbUser = RequestHolder.getCurrentUser();
        String unique = UUID.randomUUID().toString().substring(0, 6);
        athlete.setNumber(tbUser.getProvince() + "-" + unique);
        athleteMapper.insertAthlete(athlete);
    }

    @Override
    public PageResult<AthleteVo> getAthletesByCondition(PageQueryCondition pageQueryCondition) {
        BeanValidator.check(pageQueryCondition);
        if(StringUtils.isBlank(pageQueryCondition.getGender())) {
            pageQueryCondition.setGender(null);
        } else {
            pageQueryCondition.setGender("男".equals(pageQueryCondition.getGender()) ? "1" : "2");
        }
        if(StringUtils.isBlank(pageQueryCondition.getEvent())) {
            pageQueryCondition.setEvent(null);
        } else {
            pageQueryCondition.setEvent("%" + pageQueryCondition.getEvent() + "%");
        }
        pageQueryCondition.setTeam(RequestHolder.getCurrentUser().getProvince());
        int count = athleteMapper.countAthleteByCondition(pageQueryCondition);
        if(count < 1) {
            return new PageResult<>();
        }
        List<TbAthlete> athleteList = athleteMapper.selectAthletesByCondition(pageQueryCondition);
        List<AthleteVo> athleteVoList = Lists.newLinkedList();
        for(TbAthlete athlete : athleteList) {
            AthleteVo athleteVo = new AthleteVo();
            BeanUtils.copyProperties(athlete, athleteVo, "gender");
            String photoUrl = uploadService.getPictureAddress(athlete.getPhotoName());
            athleteVo.setGender(athlete.getGender() == 1 ? "男" : "女");
            if(photoUrl != null) {
                //photoUrl = "file://" + photoUrl;
                String username = RequestHolder.getCurrentUser().getUsername();
                photoUrl = this.photoUrl + username + "/" + athlete.getPhotoName();
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
        String itemStr = '%' + item.getEvent() + '%';
        List<TbAthlete> athleteList = athleteMapper.selectAthletesByTeamAndEvent(team, itemStr);
        List<String> athleteStrList = Lists.newLinkedList();
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

            if(startTime != null && endTime != null) {
                String date = tbAthlete.getBirthday();
                if(!(date.compareTo(startTime) > 0 && date.compareTo(endTime) < 0)) {
                    flag = false;
                }
            }
            if(gender != null) {
                if(gender.intValue() != tbAthlete.getGender().intValue()) {
                    flag = false;
                }
            }
            if(flag) {
                String str = tbAthlete.getName();
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
        String number = "%"+ athlete.getNumber() +"%";
        Integer count = athleteMapper.countMatchItemAthleteByAthleteNumber(number);
        if(count > 0) {
            throw new AthleteException("改运动员已经报名参赛，请先删除该运动员的比赛项目");
        }
        athleteMapper.deleteByAthleteId(id);
    }

    @Override
    public PageResult<AthleteVo> getAthletes(PageQuery pageQuery) {
        BeanValidator.check(pageQuery);
        TbUser user = RequestHolder.getCurrentUser();
        String team = user.getProvince();
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
            String photoUrl = uploadService.getPictureAddress(athlete.getPhotoName());
            if(photoUrl != null) {
                //photoUrl = "file://" + photoUrl;
                String username = RequestHolder.getCurrentUser().getUsername();
                photoUrl = this.photoUrl + username + "/" + athlete.getPhotoName();
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
        TbAthlete tbAthlete = athleteMapper.selectAthleteById(athleteParam.getId());
        String number = "%"+ tbAthlete.getNumber() +"%";
        Integer count = athleteMapper.countMatchItemAthleteByAthleteNumber(number);
        if(count > 0) {
            throw new AthleteException("改运动员已经报名参赛，请先删除该运动员的比赛项目");
        }
        TbAthlete athlete = new TbAthlete();
        BeanUtils.copyProperties(athleteParam, athlete, "gender");
        String gender = athleteParam.getGender();
        athlete.setGender("男".equals(gender) ? 1 : 2);
        athlete.setUpdateTime(new Date());
        athleteMapper.updateAthleteById(athlete);
    }

}
