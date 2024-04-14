package com.co.codeconnectjudge.service.upload.impl;

import com.co.codeconnectjudge.common.result.ResponseResult;
import com.co.codeconnectjudge.common.result.ResultStatus;
import com.co.codeconnectjudge.manager.upload.UploadQuestionManager;
import com.co.codeconnectjudge.model.dto.UploadFileParamsDto;
import com.co.codeconnectjudge.service.upload.UploadQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.co.codeconnectjudge.constant.FileConstans.FILE_TYPE_IMAGE;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-09 16:08
 */

@Service
public class UploadQuestionServiceImpl implements UploadQuestionService {
    @Autowired
    private UploadQuestionManager uploadQuestionManager;
    @Value("${minio.bucketName1}")
    private String bucket_questionimages;
    @Override
    public ResponseResult uploadImage(MultipartFile image) {
        //准备上传文件信息
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFilename(image.getOriginalFilename());
        uploadFileParamsDto.setFileSize(image.getSize());
        uploadFileParamsDto.setFileType(FILE_TYPE_IMAGE);
        //创建一个临时文件
        try {
            File tempFile = File.createTempFile("minio", ".temp");
            image.transferTo(tempFile);
            String localFilePath = tempFile.getAbsolutePath();
            //调用UploadQuestionManager上传图片
            return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "图片上传成功", uploadQuestionManager.uploadFile(uploadFileParamsDto, localFilePath, bucket_questionimages));
        } catch (Exception e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(), e.getMessage());
        }

    }
}
