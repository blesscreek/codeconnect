package com.co.backend.manager.upload;

import com.co.common.exception.StatusFailException;
import com.co.backend.model.dto.UploadFileParamsDto;
import com.co.common.utils.MinioUtil;
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
    private MinioUtil minioUtil;
    @Value("${minio.endpoint}")
    private String endpoint;

    public HashMap<String, String> uploadFile(UploadFileParamsDto uploadFileParamsDto, String localFilePath, String bucket) throws StatusFailException {
        String filename = uploadFileParamsDto.getFilename();
        //扩展名
        String extension = filename.substring(filename.lastIndexOf("."));
        String mimeType = minioUtil.getMimeType(extension);
        //子目录
        String defaultFolderPath = minioUtil.getDefaultFolderPath();
        String fileMd5 = minioUtil.getFileMd5(new File(localFilePath));
        //文件名称
        String objectName = defaultFolderPath + fileMd5 + extension;
        //上传文件到minio
        boolean uploadRes = minioUtil.addFilesToMinio(bucket, objectName, localFilePath, mimeType);
        if (uploadRes == false) {
            throw new StatusFailException("上传文件失败");
        }
        String url = endpoint + "/" + bucket + "/" + objectName;
        HashMap<String, String> map = new HashMap<>();
        map.put("url", url);
        return map;
    }
}
