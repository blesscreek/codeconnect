package com.co.backend.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-09 19:30
 */
@Data
@ToString
public class UploadFileParamsDto {

    /**
     * 文件名称
     */
    private String filename;


    /**
     * 文件类型（文档，音频，视频）
     */
    private String fileType;
    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 标签
     */
    private String tags;
    /**
     * 上传人
     */
    private String username;
    /**
     * 备注
     */
    private String remark;
}
