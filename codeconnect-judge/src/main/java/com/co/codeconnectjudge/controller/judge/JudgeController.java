package com.co.codeconnectjudge.controller.judge;


import com.co.codeconnectjudge.common.result.ResponseResult;
import com.co.codeconnectjudge.model.dto.SubmitJudgeDTO;
import com.co.codeconnectjudge.service.oj.JudgeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author co
 * @since 2024-04-11
 */
@RestController
@RequestMapping("/judge")
public class JudgeController {
    @Autowired
    private JudgeService judgeService;
    @ApiOperation("提交判题")
    @PreAuthorize("hasAuthority('sys:question:judge')")
    @PostMapping(value = "/submitJudgeQuestion")
    public ResponseResult judgeQuestion(@RequestBody SubmitJudgeDTO submitJudgeDTO) {
        return judgeService.submitJudgeQuestion(submitJudgeDTO);

    }
}