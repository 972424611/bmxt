package com.major.bmxt.service;


import javax.servlet.http.HttpServletRequest;

public interface UploadService {

    /**
     * 上传图片
     * @param request HttpServletRequest
     */
    void uploadPicture(HttpServletRequest request);

    /**
     * 获取图片地址
     * @param photoName 图片名称
     * @return 图片地址
     */
    String getPictureAddress(String photoName);

    /**
     * 上传文件
     * @param request HttpServletRequest
     */
    void uploadFile(HttpServletRequest request);
}
