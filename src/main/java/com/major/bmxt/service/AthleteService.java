package com.major.bmxt.service;

import com.major.bmxt.beans.PageQuery;
import com.major.bmxt.beans.PageQueryCondition;
import com.major.bmxt.beans.PageResult;
import com.major.bmxt.vo.AthleteVo;
import com.major.bmxt.param.AthleteParam;

import java.util.List;

public interface AthleteService {

    /**
     * 保存运动员
     * @param athleteParam AthleteParam
     */
    void saveAthlete(AthleteParam athleteParam);

    /**
     * 获取运动员列表分页
     * @param pageQuery PageQuery
     * @return PageResult<AthleteVo>
     */
    PageResult<AthleteVo> getAthletes(PageQuery pageQuery);

    /**
     * 更新运动员信息
     * @param athleteParam AthleteParam
     */
    void updateAthlete(AthleteParam athleteParam);

    /**
     * 获取运动员列表, 条件查询
     * @param pageQueryCondition PageQueryCondition
     * @return PageResult<AthleteVo>
     */
    PageResult<AthleteVo> getAthletesByCondition(PageQueryCondition pageQueryCondition);

    /**
     * 获取运动员通过小项目id
     * @param itemId 小项目id
     * @return List<String>
     */
    List<String> getAthletesByItem(Integer itemId);

    /**
     * 删除运动员通过运动员id
     * @param athleteId 运动员id
     */
    void deleteByAthleteId(String athleteId);
}
