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
     * 题目列表分页查询
     * @param pageParams  分页情参数
     * @param getQuestionListDTO  筛选条件
     * @return  题目列表信息
     */
    @ApiOperation("题目列表分页查询")
    @PostMapping("/getQuestionList")
    public ResponseResult getQuestionList(PageParams pageParams, @RequestBody GetQuestionListDTO getQuestionListDTO){
        return adminQuestionService.getQuestionList(pageParams, getQuestionListDTO);
    }

    /**
     * 题目展示
     * @param qid   题目id
     * @return  题目展示
     */
    @ApiOperation("题目展示")
    @GetMapping("/showQuestion")
    public ResponseResult showQuestion(@RequestParam("qid") Long qid){
        return adminQuestionService.showQuestion(qid);
    }

    /**
     * 增加题目
     * @param questionDTO 增加题目信息
     * @return 增加结果
     */
    @ApiOperation("增加题目")
    @PostMapping("/addQuestion")
    @PreAuthorize("hasAuthority('sys:question:add')")
    public ResponseResult addQuestion(@RequestBody QuestionDTO questionDTO) {
        return adminQuestionService.addQuestion(questionDTO);
    }

    @ApiOperation("删除题目")
    @GetMapping("/deleteQuestion")
    @PreAuthorize("hasAuthority('sys:question:delete')")
    public ResponseResult deleteQuestion(@RequestParam("qid") Long qid) {
        return adminQuestionService.deleteQuestion(qid);
    }

    @ApiOperation("获取题目详情")
    @GetMapping("/getQuestion")
    public ResponseResult getQuestion(@RequestParam("qid") Long qid) {
        return adminQuestionService.getQuestion(qid);
    }
    @ApiOperation("更改题目")
    @PostMapping("/updateQuestion")
    @PreAuthorize("hasAuthority('sys:question:update')")
    public ResponseResult updateQuestion(@RequestBody QuestionDTO questionDTO) {
        return adminQuestionService.updateQuestion(questionDTO);
    }


}
