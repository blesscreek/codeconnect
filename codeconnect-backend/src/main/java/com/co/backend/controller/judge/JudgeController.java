package com.co.backend.controller.judge;


import com.co.backend.common.result.ResponseResult;
import com.co.backend.model.dto.SubmitJudgeDTO;
import com.co.backend.service.oj.JudgeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "判题接口")
public class JudgeController {
    @Autowired
    private JudgeService judgeService;
    @ApiOperation("提交判题")
    @PreAuthorize("hasAuthority('sys:question:judge')")
    @PostMapping(value = "/submitJudgeQuestion")
    public ResponseResult judgeQuestion(@RequestBody SubmitJudgeDTO submitJudgeDTO) {
        return judgeService.submitJudgeQuestion(submitJudgeDTO);

    }
    @ApiOperation("获取用户判题提交记录列表")
    @GetMapping("/getJudgeList")
    public ResponseResult getJudgeList(@RequestParam("qid") Long qid) {
        return judgeService.getJudgeList(qid);
    }

    @ApiOperation("获取指定判题提交记录")
    @GetMapping("/getJudge")
    public ResponseResult getJudge(@RequestParam("jid") Long jid) {
        return judgeService.getJudge(jid);
    }



}
