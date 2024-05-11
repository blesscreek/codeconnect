package com.co.codeconnectjudge.dao.judge.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.co.codeconnectjudge.dao.judge.JudgeEntityService;
import com.co.codeconnectjudge.mapper.JudgeMapper;
import com.co.codeconnectjudge.model.po.Judge;
import org.springframework.stereotype.Service;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-05-08 18:13
 */

@Service
public class JudgeEntityServiceImpl extends ServiceImpl<JudgeMapper, Judge> implements JudgeEntityService {

}