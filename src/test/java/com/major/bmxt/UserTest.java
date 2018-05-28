//package com.major.bmxt;
//
//import com.major.bmxt.mapper.UserMapper;
//import com.major.bmxt.model.TbUser;
//import com.major.bmxt.service.UserService;
//import com.major.bmxt.utils.PropertyUtil;
//import com.major.bmxt.utils.RandomPassword;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.util.DigestUtils;
//
//import java.util.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations={"classpath:spring/applicationContext-*.xml"})
//public class UserTest {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    private final String provinceNames = "安徽 Anhui AH\n" +
//            "北京 Beijing BJ\n" +
//            "福建 Fujian FJ\n" +
//            "甘肃 Gansu GS\n" +
//            "广东 Guangdong GD\n" +
//            "广西 Guangxi GX\n" +
//            "贵州 Guizhou GZ\n" +
//            "海南 Hainan HI\n" +
//            "河北 Hebei HE\n" +
//            "河南 Henan HA\n" +
//            "黑龙江 Heilongjiang HL\n" +
//            "湖北 Hubei HB\n" +
//            "湖南 Hunan HN\n" +
//            "吉林 Jilin JL\n" +
//            "江苏 Jiangsu JS\n" +
//            "江西 Jiangxi JX\n" +
//            "辽宁 Liaoning LN\n" +
//            "内蒙古自治区 Inner Mongoria IM（NM）\n" +
//            "宁夏 Ningxia NX\n" +
//            "青海 Qinghai QH\n" +
//            "山东 Shandong SD\n" +
//            "山西 Shanxi SX\n" +
//            "陕西 Shaanxi SN\n" +
//            "上海 Shanghai SH\n" +
//            "四川 Sichuan SC\n" +
//            "天津 Tianjing TJ\n" +
//            "西藏 Tibet XZ\n" +
//            "新疆 Xinjiang XJ\n" +
//            "云南 Yunnan YN\n" +
//            "浙江 Zhejiang ZJ\n" +
//            "重庆 Chongqing CQ\n" +
//            "澳门 Macao MO\n" +
//            "香港 Hong Kong HK\n" +
//            "台湾 Taiwan TW";
//
//    @Test
//    public void createUser() {
//        String[] s = provinceNames.split("\n");
//        Map<String, String> nameMap = new TreeMap<>(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareTo(o2);
//            }
//        });
//        for(int i = 0; i < s.length; i++) {
//            String[] str = s[i].split(" ");
//            nameMap.put(str[1], str[0]);
//        }
//        List<TbUser> userList = new ArrayList<>();
//        for(Map.Entry<String, String> entry : nameMap.entrySet()) {
//            TbUser user = new TbUser();
//            String password = RandomPassword.getRandomPassword(8);
//            user.setUsername(entry.getKey().toUpperCase());
//            user.setProvince(entry.getValue());
//            user.setPassword(password);
//            user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
//            //userMapper.insertUser(user);
//            user.setPassword(password);
//            userList.add(user);
//        }
//        for(TbUser user : userList) {
//            System.out.println(user.getUsername() + "--" + user.getPassword() + "--" + user.getProvince());
//        }
//    }
//
//    @Test
//    public void test() {
//        System.out.println("asss-ddd-tttt".substring(0, "asss-ddd-tttt".lastIndexOf("-")));
//        System.out.println(DigestUtils.md5DigestAsHex("admin".getBytes()));
//        //System.out.println("asdf".compareTo(null));
//        System.out.println(PropertyUtil.getTeamProperty("北京"));
//    }
//}
