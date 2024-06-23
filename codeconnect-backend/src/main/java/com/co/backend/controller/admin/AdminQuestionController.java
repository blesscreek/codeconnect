package com.co.backend.controller.admin;


import com.co.backend.model.po.PageParams;
import com.co.backend.common.result.ResponseResult;
import com.co.backend.model.dto.GetQuestionListDTO;
import com.co.backend.model.dto.QuestionDTO;
import com.co.backend.service.admin.AdminQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 * 题目增删改查
 * @author co
 * @since 2024-04-07
 */
@RestController
@RequestMapping("/question")
@Api(tags = "题目管理接口")
public class AdminQuestionController {
    @Autowired
    private AdminQuestionService adminQuestionService;
    /**
     * 增加题目
     * @param questionDTO
     * @return
     */
    @ApiOperation("增加题目")
    @PostMapping("/addQuestion")
    @PreAuthorize("hasAuthority('sys:question:add')")
    public ResponseResult addQuestion(@RequestBody QuestionDTO questionDTO) {
        return adminQuestionService.addQuestion(questionDTO);
    }
    @ApiOperation("题目列表分页查询")
    @PostMapping("/getQuestionList")
    public ResponseResult getQuestionList(PageParams pageParams, @RequestBody GetQuestionListDTO getQuestionListDTO){
        return adminQuestionService.getQuestionList(pageParams, getQuestionListDTO);
    }
    @ApiOperation("题目查询")
    @GetMapping("/getQuestion")
    public ResponseResult getQuestion(@RequestParam("qid") Long qid){
        return adminQuestionService.getQuestion(qid);
    }


}