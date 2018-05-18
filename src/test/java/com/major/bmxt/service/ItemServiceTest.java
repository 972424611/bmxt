//package com.major.bmxt.service;
//
//import com.google.common.collect.Lists;
//import com.major.bmxt.beans.ItemCondition;
//import com.major.bmxt.vo.ItemVo;
//import com.major.bmxt.param.ItemAthleteParam;
//import com.major.bmxt.utils.JsonUtils;
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
//public class ItemServiceTest {
//
//    @Autowired
//    private ItemService itemService;
//
//    @Test
//    public void getItemsByMatchIdTest() {
//        List<ItemVo> itemVoList = itemService.getItemsByMatchId(1);
//        itemVoList.forEach(System.out::println);
//    }
//
//    @Test
//    public void saveItemTest() {
//        ItemCondition itemCondition = new ItemCondition();
//        itemCondition.setStartTime("1984-01-02");
//        itemCondition.setEndTime("1996-02-13");
//        itemCondition.setGender(1);
//        System.out.println(JsonUtils.objectToJson(itemCondition));
//    }
//
//    @Test
//    public void saveItemAthleteTest() {
//        ItemAthleteParam itemAthleteParam = new ItemAthleteParam();
//        ItemVo itemVo = new ItemVo();
//        itemVo.setNumber(1);
//        itemVo.setId(1);
//        List<ItemAthleteParam.Athlete> athleteList = Lists.newArrayList();
//        ItemAthleteParam.Athlete athlete = new ItemAthleteParam().new Athlete();
//        athlete.setAthleteMessage("张三-ANHUI-123445");
//        athlete.setBoat(1);
//        athleteList.add(athlete);
//        athlete.setAthleteMessage("赵六-ANHUI-ce06e1");
//        athlete.setBoat(2);
//        athleteList.add(athlete);
//        //itemAthleteParam.setAthleteList(athleteList);
//        itemAthleteParam.setItemVo(itemVo);
//        String json = JsonUtils.objectToJson(itemAthleteParam);
//        System.out.println(json);
//        //HttpClientUtils.doPostJson("http://localhost:8085/item/saveAthlete", json);
//        //System.out.println(json);
//        //itemService.saveItemAthlete(itemAthleteParam);
////        itemAthleteParam.setAthleteList();
////        itemAthleteParam.setItemVo();
////        itemService.saveItemAthlete();
//    }
//}
