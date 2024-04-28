package com.co.codeconnectjudge.manager.upload;

import com.co.codeconnectjudge.common.exception.StatusFailException;
import com.co.codeconnectjudge.config.MinioConfig;
import com.co.codeconnectjudge.model.dto.UploadFileParamsDto;
import com.co.codeconnectjudge.service.upload.MinioService;
import com.co.codeconnectjudge.service.upload.UploadQuestionService;
import com.co.codeconnectjudge.service.upload.impl.UploadQuestionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-09 16:12
 */

@Component
public class UploadQuestionManager {
    @Autowired
    private MinioService minioService;
    @Value("${minio.endpoint}")
    private String endpoint;

    public HashMap<String, String> uploadFile(UploadFileParamsDto uploadFileParamsDto, String localFilePath, String bucket) throws StatusFailException {
        String filename = uploadFileParamsDto.getFilename();
        //扩展名
        String extension = filename.substring(filename.lastIndexOf("."));
        String mimeType = minioService.getMimeType(extension);
        //子目录
        String defaultFolderPath = minioService.getDefaultFolderPath();
        String fileMd5 = minioService.getFileMd5(new File(localFilePath));
        //文件名称
        String objectName = defaultFolderPath + fileMd5 + extension;
        //上传文件到minio
        boolean uploadRes = minioService.addFilesToMinio(bucket, objectName, localFilePath, mimeType);
        if (uploadRes == false) {
            throw new StatusFailException("上传文件失败");
        }
        String url = endpoint + "/" + bucket + "/" + objectName;
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return map;
    }
}
