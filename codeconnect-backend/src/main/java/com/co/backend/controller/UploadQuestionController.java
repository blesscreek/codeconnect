package com.co.backend.controller;

import com.co.backend.common.result.ResponseResult;
import com.co.backend.service.upload.UploadQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author co
 * @Version 1.0
 * @Description 上传题目相关，题目描述图片、测试文件
 * @Date 2024-04-09 15:34
 */
@Slf4j
@RestController
@Api(tags = "上传题目接口")
@RequestMapping("/uploadQuestion")
public class UploadQuestionController {
    @Autowired
    private UploadQuestionService uploadQuestionService;
    @ApiOperation("上传图片")
    @PostMapping("/uploadImage")
    @PreAuthorize("hasAuthority('sys:question:add')")
    public ResponseResult uploadImage(@RequestPart("imagedata")MultipartFile image) {
        ResponseResult responseResult = uploadQuestionService.uploadImage(image);
        return responseResult;
    }

    @ApiOperation("上传测试用例文件")
    @PostMapping("/uploadQuestionCase")
    @PreAuthorize("hasAuthority('sys:question:add')")
    public ResponseResult uploadQuestionCase(@RequestParam("qid") Long qid, @RequestPart("zipFileData")MultipartFile zipFile) {
        ResponseResult responseResult = uploadQuestionService.uploadQuestionCase(qid, zipFile);
        return responseResult;
    }
}
