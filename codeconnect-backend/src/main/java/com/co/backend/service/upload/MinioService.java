package com.co.backend.service.upload;

import java.io.File;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-09 16:50
 */

public interface MinioService {
    public boolean addFilesToMinio(String bucket, String objectName, String localFilePath,  String mimeType);
    public String getMimeType(String extension);
    public String getDefaultFolderPath();
    public String getFileMd5(File file);
}
