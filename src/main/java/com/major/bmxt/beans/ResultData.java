package com.major.bmxt.beans;

import java.util.HashMap;
import java.util.Map;

public class ResultData {

    private boolean ret;

    private String msg;

    private Object data;

    public Object getData() {
        return data;
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResultData(boolean ret) {
        this.ret = ret;
    }

    public static ResultData success(Object object, String msg) {
        ResultData resultData = new ResultData(true);
        resultData.data = object;
        resultData.msg = msg;
        return resultData;
    }

    public static ResultData success(Object object) {
        ResultData resultData = new ResultData(true);
        resultData.data = object;
        return resultData;
    }

    public static ResultData success() {
        return new ResultData(true);
    }

    public static ResultData fail(String msg) {
        ResultData resultData = new ResultData(false);
        resultData.msg = msg;
        return resultData;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ret", ret);
        result.put("msg", msg);
        result.put("data", data);
        return result;
    }

}
