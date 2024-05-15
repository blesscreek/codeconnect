package com.co.backend.service.upload.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.co.backend.constant.FileConstans;
import com.co.backend.dao.question.QuestionCaseEntityService;
import com.co.backend.dao.question.QuestionEntityService;
import com.co.backend.model.po.Question;
import com.co.backend.model.po.QuestionCase;
import com.co.backend.service.upload.UploadQuestionService;
import com.co.backend.common.result.ResponseResult;
import com.co.backend.common.result.ResultStatus;
import com.co.backend.manager.upload.UploadQuestionManager;
import com.co.backend.model.dto.UploadFileParamsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
    @Autowired
    private QuestionEntityService questionEntityService;
    @Autowired
    private QuestionCaseEntityService questionCaseEntityService;
    @Value("${minio.bucketName1}")
    private String bucket_questionimages;
    @Value("${minio.bucketName2}")
    private String  bucket_questioncases;
    @Override
    public ResponseResult uploadImage(MultipartFile image) {
        //准备上传文件信息
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFilename(image.getOriginalFilename());
        uploadFileParamsDto.setFileSize(image.getSize());
        uploadFileParamsDto.setFileType(FileConstans.FILE_TYPE_IMAGE);
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

    @Override
    public ResponseResult uploadQuestionCase(String questionName, MultipartFile zipFile) {
        try(ZipInputStream zipIn = new ZipInputStream(zipFile.getInputStream(),Charset.forName("UTF-8"))) {
            QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
            questionQueryWrapper.eq("title", questionName);
            Question question = questionEntityService.getOne(questionQueryWrapper);

            QueryWrapper<QuestionCase> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("qid", question.getId()).eq("is_show", 0);
            questionCaseEntityService.remove(queryWrapper);

            Path tempPath = Files.createTempDirectory("tempDir");
            File tempDir = tempPath.toFile();

            ZipEntry entry = zipIn.getNextEntry();
            int cnt = 0;
            String inputUrl = null, outputUrl = null;
            String pairPrefix = null;
            while(entry != null) {
                cnt++;
                String fileName = entry.getName();
                String[] parts = fileName.split("\\.");
                String suffix = parts[parts.length - 1];
                String prefix = parts[0];
                if (cnt % 2 == 1 &&!suffix.equals("in")) {
                    return new ResponseResult(ResultStatus.FAIL.getStatus(), "文件格式错误");
                }
                if (cnt % 2 == 0 &&!suffix.equals("out")) {
                    return new ResponseResult(ResultStatus.FAIL.getStatus(), "文件格式错误");
                }
                UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
                uploadFileParamsDto.setFilename(fileName);
                uploadFileParamsDto.setFileSize(entry.getSize());
                uploadFileParamsDto.setFileType(FileConstans.FILE_TYPE_INOUT);
                HashMap<String, String> map;
                try {
                    File tempFile = File.createTempFile("minio", ".temp");
                   try (OutputStream os = new FileOutputStream(tempFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipIn.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                    }
                    String localFilePath = tempFile.getAbsolutePath();

                    //调用UploadQuestionManager上传文件
                    map = uploadQuestionManager.uploadFile(uploadFileParamsDto, localFilePath, bucket_questioncases);
                    String url = map.get("url");

                    if (cnt % 2 == 1) {
                        inputUrl = url;
                        pairPrefix = prefix;
                    } else {
                        if (!prefix.equals(pairPrefix)) {
                            return new ResponseResult(ResultStatus.FAIL.getStatus(),"上传文件命名不正确，上传失败");
                        }
                        outputUrl = url;
                        QuestionCase questionCase = new QuestionCase();
                        questionCase.setQid(question.getId());
                        questionCase.setInput(inputUrl);
                        questionCase.setOutput(outputUrl);
                        questionCase.setIsShow(false);
                        questionCase.setCreateTime(LocalDateTime.now());
                        questionCase.setUpdateTime(LocalDateTime.now());
                        questionCaseEntityService.save(questionCase);
                    }
                    zipIn.closeEntry();
                    tempFile.delete();

                } catch (Exception e) {
                    return new ResponseResult(ResultStatus.FAIL.getStatus(), e.getMessage());
                }
                entry = zipIn.getNextEntry();
                deleteTempDir(tempDir);

            }

        } catch (IOException e) {
            return new ResponseResult(ResultStatus.FAIL.getStatus(),e.getMessage());
        }
        return new ResponseResult(ResultStatus.SUCCESS.getStatus(), "上传成功");
    }

    private void deleteTempDir(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteTempDir(file);
                }
            }
        }
        dir.delete();
    }
}
