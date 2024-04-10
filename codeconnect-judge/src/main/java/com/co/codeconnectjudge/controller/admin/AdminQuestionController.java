package com.co.codeconnectjudge.controller.admin;


import com.co.codeconnectjudge.common.result.ResponseResult;
import com.co.codeconnectjudge.model.dto.QuestionDTO;
import com.co.codeconnectjudge.service.admin.AdminQuestionService;
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
 * 题目增删改查
 * @author co
 * @since 2024-04-07
 */
@RestController
@RequestMapping("/question")
public class AdminQuestionController {
    @Autowired
    private AdminQuestionService adminQuestionService;
    /**
     * 增加题目
     * @param questionDTO
     * @return
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('sys:question:add')")
    public ResponseResult addQuestion(@RequestBody QuestionDTO questionDTO) {
        return adminQuestionService.addQuestion(questionDTO);
    }
}
