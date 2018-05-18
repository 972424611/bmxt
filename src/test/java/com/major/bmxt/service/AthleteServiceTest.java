//package com.major.bmxt.service;
//
//import com.major.bmxt.beans.PageQuery;
//import com.major.bmxt.beans.PageQueryCondition;
//import com.major.bmxt.beans.PageResult;
//import com.major.bmxt.vo.AthleteVo;
//import com.major.bmxt.param.AthleteParam;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.List;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring/applicationContext-*.xml"})
//public class AthleteServiceTest {
//
//    @Autowired
//    private AthleteService athleteService;
//
//    @Test
//    public void saveAthleteTest() {
//        AthleteParam athleteParam = new AthleteParam();
//        athleteParam.setName("李四3rr");
//        athleteParam.setBirthday("1989-05-24");
//        athleteParam.setEvent("皮划艇静水-皮划艇激流-赛艇");
//        athleteParam.setGender("男");
//        athleteParam.setTeam("北京");
//        athleteService.saveAthlete(athleteParam);
//    }
//
//    @Test
//    public void getAthletesTest() {
//        PageQuery pageQuery = new PageQuery();
//        pageQuery.setPageNo(1);
//        pageQuery.setPageSize(10);
//        PageResult<AthleteVo> pageResult = athleteService.getAthletes(pageQuery);
//        List<AthleteVo> athleteVoList = pageResult.getData();
//        System.out.println("total: " + pageResult.getTotal());
//        for(AthleteVo athleteVo : athleteVoList) {
//            System.out.println(athleteVo.toString());
//        }
//    }
//
//    @Test
//    public void getAthletesByConditionTest() {
//        PageQueryCondition pageQueryCondition = new PageQueryCondition();
//        pageQueryCondition.setPageNo(1);
//        pageQueryCondition.setPageSize(10);
//        pageQueryCondition.setBirthday(null);
//        pageQueryCondition.setEvent("皮划艇静水");
//        pageQueryCondition.setGender(null);
//        pageQueryCondition.setName(null);
//        pageQueryCondition.setTeam("安徽");
//        PageResult<AthleteVo> pageResult = athleteService.getAthletesByCondition(pageQueryCondition);
//        List<AthleteVo> athleteVoList = pageResult.getData();
//        System.out.println("total: " + pageResult.getTotal());
//        for(AthleteVo athleteVo : athleteVoList) {
//            System.out.println(athleteVo.toString());
//        }
//    }
//
//    @Test
//    public void getAthletesByItemTest() {
////        ItemCondition itemCondition = new ItemCondition();
////        itemCondition.setStartTime("1980-02-02");
////        itemCondition.setEndTime("1999-03-03");
////        itemCondition.setGender(1);
////        String json = JsonUtils.objectToJson(itemCondition);
//        //System.out.println(json);
//        List<String> strList = athleteService.getAthletesByItem(3);
////        for(String str : strList) {
////            System.out.println(str);
////        }
//        strList.forEach(System.out::println);
//    }
//
//}
