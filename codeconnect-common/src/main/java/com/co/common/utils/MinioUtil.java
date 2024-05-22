package com.co.common.utils;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;


import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-18 15:11
 */
@Slf4j
@Component
public class MinioUtil {
    @Autowired
    MinioClient minioClient;
    /**
     * 文件写入minio
     * @param bucket
     * @param objectName
     * @param localFilePath
     * @param mimeType
     */
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
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传到文件到minio出错,bucket:{},objectName:{},错误原因:{}",bucket,objectName,e.getMessage(),e);
            return false;
        }
    }
    public File downloadFile(String bucket,String objectName,String suffix) {
        File minioFile = null;
        FileOutputStream outputStream = null;
        try {
            InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
            if (suffix != null ){
                minioFile = File.createTempFile("minio", suffix);
            } else {
                minioFile = File.createTempFile("minio", ".merge");
            }
            outputStream = new FileOutputStream(minioFile);
            IOUtils.copy(stream, outputStream);
            return minioFile;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

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
