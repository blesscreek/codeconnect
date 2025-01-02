package com.co.judger.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.co.common.model.JudgeServer;
import com.co.judger.dao.JudgeServerEntityService;
import com.co.judger.mapper.JudgeServerMapper;
import org.springframework.stereotype.Service;

@Service
public class JudgeServerEntityServiceImpl extends ServiceImpl<JudgeServerMapper, JudgeServer> implements JudgeServerEntityService {
}
