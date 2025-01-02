package com.co.backend.dao.judge.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.co.backend.dao.judge.JudgeEntityService;
import com.co.backend.dao.judge.JudgeServerEntityService;
import com.co.backend.mapper.JudgeMapper;
import com.co.backend.mapper.JudgeServerMapper;
import com.co.backend.model.po.Judge;
import com.co.common.model.JudgeServer;
import org.springframework.stereotype.Service;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-10-13 16:56
 */
@Service
public class JudgeServerEntityServiceImpl extends ServiceImpl<JudgeServerMapper, JudgeServer> implements JudgeServerEntityService {
}
