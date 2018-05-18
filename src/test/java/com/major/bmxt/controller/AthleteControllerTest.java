//package com.major.bmxt.controller;
//
//import com.major.bmxt.param.ItemAthleteParam;
//import com.major.bmxt.utils.JsonUtils;
//import com.major.bmxt.vo.AthleteVo;
//import com.major.bmxt.vo.ItemVo;
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AthleteControllerTest {
//
//        public static void main(String[] args) {
//            int n = 999;
//            int sum = 0;
//            int k = 1;
//            for(int i = 1; i <= n; i++) {
//                System.out.println(i);
//                k = k * i;
//                while(k % 10 == 0) {
//                    sum++;
//                    k = k / 10;
//                }
//            }
//            System.out.println("end: " + sum);
//        }
//
//        public static int search(int[] arr, int key) {
//            int max,min,mid;
//            min=0;
//            max=arr.length-1;
//            mid=(max+min)/2;
//            while(arr[mid] != key)
//            {
//                if (key>arr[mid])
//                    min=mid+1;
//                if (key<arr[mid])
//                    max=mid-1;
//                if (min>max)
//                    return min;
//
//            }
//            return mid;
//        }
//
//
//
//    @Test
//    public void test() {
//        /*String json = "[{\"athleteMessage\":\"小兰-北京-f0b98f\",\"boat\":\"1\"},{\"athleteMessage\":\"小西-北京-164574\",\"boat\":\"1\"}]";
//        ItemAthleteParam.Athlete athlete = new ItemAthleteParam().new Athlete();
//        List<ItemAthleteParam.Athlete> list = new ArrayList<>();
//        athlete.setAthleteMessage("asdf");
//        athlete.setBoat(1);
//        list.add(athlete);
//        athlete.setAthleteMessage("dddddd");
//        athlete.setBoat(1);
//        list.add(athlete);
//        System.out.println(JsonUtils.objectToJson(list));
//        List<AthleteVo> list2 = new ArrayList<>();
//        AthleteVo athleteVo = new AthleteVo();
//        athleteVo.setEvent("asdf");
//        list2.add(athleteVo);
//        athleteVo.setEvent("rrrr");
//        list2.add(athleteVo);
//        System.out.println(JsonUtils.objectToJson(list2));
//        list2 = JsonUtils.jsonToList(JsonUtils.objectToJson(list2), AthleteVo.class);*/
//
//    }
//}
