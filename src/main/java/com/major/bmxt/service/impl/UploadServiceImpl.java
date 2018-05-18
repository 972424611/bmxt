package com.major.bmxt.service.impl;

import com.google.common.collect.Sets;
import com.major.bmxt.beans.ItemCondition;
import com.major.bmxt.common.RequestHolder;
import com.major.bmxt.exception.UploadException;
import com.major.bmxt.param.UploadFileParam;
import com.major.bmxt.service.ItemService;
import com.major.bmxt.service.MatchService;
import com.major.bmxt.service.UploadService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UploadServiceImpl implements UploadService {

    @Value("${PICTURE_ADDRESS}")
    private String pictureAddress;

    /**  照片最大大小 */
    private static final long MAXSIZE = 5242880;

    private static Set<String> pictureFormatSet = Sets.newHashSet();

    private final ItemService itemService;

    private final MatchService matchService;

    static {
        pictureFormatSet.add(".jpg");
        pictureFormatSet.add(".png");
    }

    @Autowired
    public UploadServiceImpl(ItemService itemService, MatchService matchService) {
        this.itemService = itemService;
        this.matchService = matchService;
    }

    private String getFormat(FileItem item) {
        long size = item.getSize();
        //设定上传的最大值5MB, 5*1024*1024
        if(size > MAXSIZE) {
            throw new UploadException("图片过大, 请使用小于5MB的图片上传");
        }
        String fileName = item.getName();
        String format =  fileName.substring(fileName.lastIndexOf("."));
        if(!pictureFormatSet.contains(format)) {
            throw new UploadException("请使用格式为.jpg或.png的图片");
        }
        return format;
    }

    private File createFile(String photoName, String format) {
        String team = RequestHolder.getCurrentUser().getUsername();
        String fileName = pictureAddress + team + "/" + photoName + format;
        File file = new File(fileName);
        if(!file.getParentFile().exists()) {
            if(!file.getParentFile().mkdirs()) {
                throw new UploadException("创建文件失败");
            }
        }
        if(!file.exists()) {
            try {
                if(file.createNewFile()) {
                    return file;
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new UploadException("创建文件失败");
            }
        }
        return file;
    }

    @Override
    public void uploadPicture(HttpServletRequest request) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            List<FileItem> list = upload.parseRequest(request);
            for(FileItem item : list) {
                if(!item.isFormField()) {
                    String format = getFormat(item);
                    inputStream = item.getInputStream();
                    byte[] buffer = new byte[1024];
                    File file = createFile(request.getParameter("id"), format);
                    fileOutputStream = new FileOutputStream(file);
                    int len;
                    while((len = inputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                }
            }
        } catch (FileUploadException | IOException e) {
            e.printStackTrace();
            throw new UploadException("服务器繁忙请稍后在试");
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                    if(fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getPictureAddress(String photoName) {
        String team = RequestHolder.getCurrentUser().getUsername();
        String fileName = pictureAddress + team + "/" + photoName;
        File file = new File(fileName);
        if(!file.exists()) {
            return null;
        }
        return fileName;
    }

    private void saveData(UploadFileParam uploadFileParam) {
        matchService.saveMatch(uploadFileParam);
        itemService.saveItem(uploadFileParam);
    }

    private void extract(InputStream inputStream) {
        try {
            UploadFileParam uploadFileParam = new UploadFileParam();
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //获取sheet页数 xssfWorkbook.getNumberOfSheets()
            for(int numSheet = 0; numSheet < 1; numSheet++) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if(xssfSheet == null) {
                    continue;
                }
                uploadFileParam.setName(xssfSheet.getRow(0).getCell(1).toString());
                uploadFileParam.setHost(xssfSheet.getRow(0).getCell(2).toString());
                uploadFileParam.setEvent(xssfSheet.getRow(1).getCell(1).toString());
                uploadFileParam.setStartTime(xssfSheet.getRow(2).getCell(1).getDateCellValue());
                uploadFileParam.setEndTime(xssfSheet.getRow(3).getCell(1).getDateCellValue());
                uploadFileParam.setNumber(xssfSheet.getRow(4).getCell(1).toString());
                Map<String, ItemCondition> map = new HashMap<>();
                int rowNum = 5;
                while(true) {
                    if("分项".equals(xssfSheet.getRow(rowNum).getCell(0).toString())) {
                        break;
                    }
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    ItemCondition itemCondition = new ItemCondition();
                    if(StringUtils.isNotBlank(xssfRow.getCell(1).toString())) {
                        if(StringUtils.isNotBlank(xssfRow.getCell(2).toString())) {
                            itemCondition.setStartTime(simpleDateFormat.format(xssfRow.getCell(2).getDateCellValue()));
                        }
                        if(StringUtils.isNotBlank(xssfRow.getCell(3).toString())) {
                            itemCondition.setEndTime(simpleDateFormat.format(xssfRow.getCell(3).getDateCellValue()));
                        }
                    }
                    map.put(xssfRow.getCell(0).toString(), itemCondition);
                    rowNum++;
                }
                uploadFileParam.setMap(map);
                List<UploadFileParam.Item> itemList = new ArrayList<>();
                for(int i = rowNum + 1; i <= rowNum + Double.valueOf(uploadFileParam.getNumber()); i++) {
                    UploadFileParam.Item item = new UploadFileParam().new Item();
                    item.setName(xssfSheet.getRow(i).getCell(1).toString());
                    item.setCondition(xssfSheet.getRow(i).getCell(2).toString());
                    itemList.add(item);
                }
                uploadFileParam.setItemList(itemList);
                System.out.println(uploadFileParam.toString());
                saveData(uploadFileParam);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new UploadException("上传失败, 请根据模板检查格式后再试试!");
        }
    }

    @Override
    public void uploadFile(HttpServletRequest request) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            List<FileItem> list = upload.parseRequest(request);
            for(FileItem item : list) {
                if(!item.isFormField()) {
                    inputStream = item.getInputStream();
                    extract(inputStream);
                }
            }
        } catch (FileUploadException | IOException e) {
            e.printStackTrace();
            throw new UploadException("服务器繁忙请稍后在试");
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                    if(fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
