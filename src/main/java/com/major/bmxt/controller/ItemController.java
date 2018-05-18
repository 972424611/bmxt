package com.major.bmxt.controller;

import com.major.bmxt.beans.ResultData;
import com.major.bmxt.utils.JsonUtils;
import com.major.bmxt.vo.ItemVo;
import com.major.bmxt.vo.MatchItemAthleteVo;
import com.major.bmxt.param.ItemAthleteParam;
import com.major.bmxt.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/item")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public ResultData list(HttpServletRequest request) {
        if(StringUtils.isBlank(request.getParameter("id"))) {
            return ResultData.success();
        }
        Integer id = Integer.valueOf(request.getParameter("id"));
        List<ItemVo> itemVoList = itemService.getItemsByMatchId(id);
        return ResultData.success(itemVoList);
    }

    @ResponseBody
    @RequestMapping(value = "/saveAthlete")
    public ResultData saveAthlete(@RequestParam("athleteList") String athleteList,
                                  @RequestParam("itemVo") String itemVoStr) {
        if(StringUtils.isBlank(athleteList) || StringUtils.isBlank(itemVoStr)) {
            return ResultData.success();
        }
        List<ItemAthleteParam.Athlete> list = JsonUtils.jsonToList(athleteList, ItemAthleteParam.Athlete.class);
        ItemAthleteParam itemAthleteParam = new ItemAthleteParam();
        itemAthleteParam.setItemVo(JsonUtils.jsonToPojo(itemVoStr, ItemVo.class));
        itemAthleteParam.setAthleteList(list);
        itemService.saveItemAthlete(itemAthleteParam);
        return ResultData.success();
    }

    @ResponseBody
    @RequestMapping(value = "/getAthletes")
    public ResultData getAthletes(HttpServletRequest request) {
        Integer itemId = Integer.valueOf(request.getParameter("id"));
        List<MatchItemAthleteVo> matchItemAthleteVoList = itemService.getItemAthlete(itemId);
        return ResultData.success(matchItemAthleteVoList);
    }
}
