package com.major.bmxt.controller;

import com.major.bmxt.beans.PageQuery;
import com.major.bmxt.beans.PageQueryCondition;
import com.major.bmxt.beans.PageResult;
import com.major.bmxt.beans.ResultData;
import com.major.bmxt.vo.AthleteVo;
import com.major.bmxt.param.AthleteParam;
import com.major.bmxt.service.AthleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.util.List;

@Controller
@RequestMapping(value = "/athlete")
public class AthleteController {

    private final AthleteService athleteService;

    @Autowired
    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    @ResponseBody
    @RequestMapping(value = "/save")
    public ResultData save(AthleteParam athleteParam) {
        athleteService.saveAthlete(athleteParam);
        return ResultData.success();
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public ResultData list(PageQuery pageQuery) {
        PageResult<AthleteVo> pageResult = athleteService.getAthletes(pageQuery);
        return ResultData.success(pageResult);
    }

    @ResponseBody
    @RequestMapping(value = "/update")
    public ResultData update(AthleteParam athleteParam) {
        athleteService.updateAthlete(athleteParam);
        return ResultData.success();
    }

    @ResponseBody
    @RequestMapping(value = "/listCondition")
    public ResultData listCondition(PageQueryCondition pageQueryCondition) {
        PageResult<AthleteVo> pageResult = athleteService.getAthletesByCondition(pageQueryCondition);
        return ResultData.success(pageResult);
    }

    @ResponseBody
    @RequestMapping(value = "/listItem")
    public ResultData listItem(HttpServletRequest request) {
        if(request.getParameter("id") == null) {
            return ResultData.success();
        }
        Integer itemId = Integer.valueOf(request.getParameter("id"));
        if(itemId < 0) {
            return ResultData.success();
        }
        List<String> athleteStrList = athleteService.getAthletesByItem(itemId);
        return ResultData.success(athleteStrList);
    }

    @ResponseBody
    @RequestMapping(value = "/delete")
    public ResultData delete(HttpServletRequest request) {
        if(request.getParameter("id") == null) {
            return ResultData.success();
        }
        Integer id = Integer.valueOf(request.getParameter("id"));
        if(id < 0) {
            return ResultData.success();
        }
        athleteService.deleteByAthleteId(request.getParameter("id"));
        return ResultData.success();
    }
}
