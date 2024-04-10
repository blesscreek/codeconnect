package com.co.codeconnectjudge.service.upload;

import com.co.codeconnectjudge.common.result.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author bless
 * @Version 1.0
 * @Description TODO
 * @Date 2024-04-09 16:01
 */
public interface UploadQuestionService {
    ResponseResult uploadImage(MultipartFile image);

}
