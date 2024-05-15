package com.co.backend.dao.judge.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.co.backend.dao.judge.JudgeEntityService;
import com.co.backend.model.po.Judge;
import com.co.backend.mapper.JudgeMapper;
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