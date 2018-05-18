package com.major.bmxt.utils;

import java.util.Random;

public class RandomPassword {
    public static void main(String[] args) {

        String password = getRandomPassword(8);
        System.out.println(password);
    }

    //获取验证过的随机密码
    public static String getRandomPassword(int len) {
        String result = null;

        /*if(len >= 6) {
            for(result = makeRandomPassword(len);len >= 6;result = makeRandomPassword(len)){
                if (result.matches(".*[a-z]{1,}.*") && result.matches(".*[A-Z]{1,}.*") && result.matches(".*\\d{1,}.*") && result.matches(".*[~!@#$%^&*\\.?]{1,}.*")) {
                    return result;
                }
            }
        }*/
        while(len>=6){
            result = makeRandomPassword(len);
            if (result.matches(".*[a-z]{1,}.*") && result.matches(".*[A-Z]{1,}.*") && result.matches(".*\\d{1,}.*") && result.matches(".*[~!@#$%^&*\\.?]{1,}.*")) {
                return result;
            }
            result = makeRandomPassword(len);
        }
        return "长度不得少于6位!";
    }
    //随机密码生成
    public static String makeRandomPassword(int len){
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*.?".toCharArray();
        //System.out.println("字符数组长度:" + charr.length); //可以看到调用此方法多少次
        StringBuilder sb = new StringBuilder();
        Random r = new Random();

        for (int x = 0; x < len; ++x) {
            sb.append(charr[r.nextInt(charr.length)]);
        }
        return sb.toString();
    }

}
