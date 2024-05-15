package com.co.backend.service.upload.impl;

import com.co.backend.service.upload.MinioService;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-09 16:50
 */

@Slf4j
@Service
public class MinioServiceImpl implements MinioService {
    @Autowired
    MinioClient minioClient;
    /**
     * 文件写入minio
     * @param bucket
     * @param objectName
     * @param localFilePath
     * @param mimeType
     */
    @Override
    public boolean addFilesToMinio(String bucket, String objectName, String localFilePath, String mimeType) {
        try {
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .filename(localFilePath)
                    .contentType(mimeType)
                    .build();
            minioClient.uploadObject(testbucket);
            log.debug("上传文件到minio成功，bucket:{},objectName:{}",bucket,objectName);
            System.out.println("上传成功");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传到文件到minio出错,bucket:{},objectName:{},错误原因:{}",bucket,objectName,e.getMessage(),e);
            return false;
        }
    }

    /**
     * 根据扩展名获取mimeType
     * @param extension
     * @return
     */
    public String getMimeType(String extension) {
        if (extension == null) {
            extension = "";
        }
        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;//通用mimeType，字节流
        if (extensionMatch != null) {
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }

    /**
     * 获取文件默认存储目录路径 年/月/日
     * @return
     */
    @Override
    public String getDefaultFolderPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String folder = sdf.format(new Date()).replace("-", "/") + "/";
        return folder;
    }

    /**
     * 获取文件MD5
     * @param file
     * @return
     */
    @Override
    public String getFileMd5(File file) {
        try(FileInputStream fileInputStream = new FileInputStream(file)) {
            String fileMd5 = DigestUtils.md5DigestAsHex(fileInputStream);
            return fileMd5;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
