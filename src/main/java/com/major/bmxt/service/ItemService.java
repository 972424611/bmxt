package com.major.bmxt.service;

import com.major.bmxt.param.UploadFileParam;
import com.major.bmxt.vo.ItemVo;
import com.major.bmxt.vo.MatchItemAthleteVo;
import com.major.bmxt.param.ItemAthleteParam;

import java.util.List;

public interface ItemService {

    /**
     * 获取小项目根据大项目id
     * @param matchId 大项目id
     * @return List<ItemVo>
     */
    List<ItemVo> getItemsByMatchId(Integer matchId);

    /**
     * 保存选择该小项目的运动员
     * @param itemAthleteParam ItemAthleteParam
     */
    void saveItemAthlete(ItemAthleteParam itemAthleteParam);

    /**
     * 获取已经报名该小项目的运动员
     * @param itemId 小项目id
     * @return List<MatchItemAthleteVo>
     */
    List<MatchItemAthleteVo> getItemAthlete(Integer itemId);

    /**
     * 保存小项目
     * @param uploadFileParam UploadFileParam
     */
    void saveItem(UploadFileParam uploadFileParam);
}
