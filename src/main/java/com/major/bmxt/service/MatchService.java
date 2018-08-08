package com.major.bmxt.service;

import com.major.bmxt.beans.ResultData;
import com.major.bmxt.param.UploadFileParam;
import com.major.bmxt.vo.MatchInfoTableVo;
import com.major.bmxt.vo.MatchVo;

import java.util.List;

public interface MatchService {

    /**
     * 获取全部大项目
     * @return List<MatchVo>
     */
    List<MatchVo> getMatch();

    /**
     * 保存大项目
     * @param uploadFileParam UploadFileParam
     */
    void saveMatch(UploadFileParam uploadFileParam);

    /**
     * 调整大项比赛状态
     * @param matchId 大项id
     * @param status 比赛状态0-关闭, 1-开启
     */
    void adjustStatus(Integer matchId, Integer status);

    /**
     * 删除比赛
     * @param id 比赛id
     */
    void deleteMatch(Integer id);

    /**
     * 验证比赛的名称, 是否已经存在
     * @param name 比赛名称
     * @return ResultData
     */
    ResultData verifyMatchName(String name);

    /**
     * 生成报名信息表
     * @param matchId 比赛id
     * @return MatchInfoTableVo
     */
    MatchInfoTableVo createMatchInfoTable(Integer matchId);
}
