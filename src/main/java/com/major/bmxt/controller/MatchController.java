package com.major.bmxt.controller;

import com.major.bmxt.beans.ResultData;
import com.major.bmxt.vo.MatchInfoTableVo;
import com.major.bmxt.vo.MatchVo;
import com.major.bmxt.service.MatchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/match")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public ResultData list() {
        List<MatchVo> matchVoList = matchService.getMatch();
        return ResultData.success(matchVoList);
    }

    @ResponseBody
    @RequestMapping(value = "/status")
    public ResultData status(@RequestParam(value = "status", defaultValue = "1") Integer status,
                             @RequestParam(value = "id") Integer id) {
        matchService.adjustStatus(id, status);
        return ResultData.success();
    }

    @ResponseBody
    @RequestMapping(value = "/delete")
    public ResultData delete(HttpServletRequest request) {
        if(StringUtils.isBlank(request.getParameter("id"))) {
            return ResultData.success();
        }
        Integer id = Integer.valueOf(request.getParameter("id"));
        matchService.deleteMatch(id);
        return ResultData.success();
    }

    @ResponseBody
    @RequestMapping(value = "/verify")
    public ResultData query(HttpServletRequest request) {
        if(StringUtils.isBlank(request.getParameter("name"))) {
            return ResultData.success("404");
        }
        String name = String.valueOf(request.getParameter("name"));
        return matchService.verifyMatchName(name);
    }

    @ResponseBody
    @RequestMapping(value = "/table")
    public ResultData table(@RequestParam("id") Integer id) {
        MatchInfoTableVo matchInfoTableVo = matchService.createMatchInfoTable(id);
        return ResultData.success(matchInfoTableVo);
    }
}
