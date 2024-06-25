package com.co.backend.controller.admin;

import com.co.backend.common.result.ResponseResult;
import com.co.backend.service.admin.AdminTagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-06-25 16:38
 */
@RestController
@RequestMapping("/tag")
@Api(tags = "题目标签管理接口")
public class AdminTagController {
    @Autowired
    private AdminTagService adminTagService;
    @ApiOperation("获取标签")
    @GetMapping("/getTag")
    public ResponseResult getTag(){
        return adminTagService.getTag();
    }


}
