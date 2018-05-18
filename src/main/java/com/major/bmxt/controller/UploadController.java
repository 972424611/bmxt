package com.major.bmxt.controller;

import com.major.bmxt.beans.ResultData;
import com.major.bmxt.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/upload")
public class UploadController {

    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @ResponseBody
    @RequestMapping(value = "/picture")
    public ResultData pictureUpload(HttpServletRequest request) {
        uploadService.uploadPicture(request);
        return ResultData.success();
    }

    @ResponseBody
    @RequestMapping(value = "/get")
    public String get(@RequestParam("photoName") String photoName) {
        return uploadService.getPictureAddress(photoName);
    }

    @ResponseBody
    @RequestMapping(value = "/file")
    public ResultData fileUpload(HttpServletRequest request) {
        uploadService.uploadFile(request);
        return ResultData.success();
    }
}
