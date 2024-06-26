package com.co.backend.service.upload;

import com.co.backend.common.result.ResponseResult;
import com.co.common.exception.StatusFailException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author bless
 * @Version 1.0
 * @Description TODO
 * @Date 2024-04-09 16:01
 */
public interface UploadQuestionService {
    ResponseResult uploadImage(MultipartFile image);

    ResponseResult uploadQuestionCase(Long qid, MultipartFile zipFile);
}
