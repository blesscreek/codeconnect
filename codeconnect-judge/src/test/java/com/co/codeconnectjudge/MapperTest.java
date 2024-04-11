package com.co.codeconnectjudge;

import com.co.codeconnectjudge.dao.question.QuestionService;
import com.co.codeconnectjudge.mapper.MenuMapper;
import com.co.codeconnectjudge.mapper.QuestionMapper;
import com.co.codeconnectjudge.mapper.QuestionTagMapper;
import com.co.codeconnectjudge.model.po.Question;
import com.google.common.collect.PeekingIterator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-03-25 20:21
 *
 */
@SpringBootTest
public class MapperTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void TestBCryptPasswordEncoder() {
        String encode = passwordEncoder.encode("1234567");
        System.out.println(encode);
//        System.out.println(passwordEncoder.matches("123","$2a$10$hkH8KN.m/210sorJY/KgFeNr1fvP0KBOSITHTa9VDFT09ytMDDLoe"));

    }

    @Autowired
    private MenuMapper menuMapper;
    @Test
    public void TestselectPermsByUserId() {
        List<String> list = menuMapper.selectPermsByUserId(3L);
        System.out.println(list);
    }

    @Autowired
    private QuestionService questionService;
    @Test
    public void TestselectQuestions() {
        String[] tagNames = {"顺序结构", "分支结构"}; // 标签名称数组
        String titleKeyword = "4";
        Integer difficulty = 1;
        List<Question> questions = questionService.selectQuestions(null, titleKeyword, null,2L,2L);
        System.out.println(questions);
//        System.out.println(questions.size());
    }

    @Autowired
    private QuestionTagMapper questionTagMapper;
    @Test
    public void TestgetTagNamesByQuestionId() {
        List<String> tagNamesByQuestionId = questionTagMapper.getTagNamesByQuestionId(10L);
        System.out.println(tagNamesByQuestionId);
    }
}
