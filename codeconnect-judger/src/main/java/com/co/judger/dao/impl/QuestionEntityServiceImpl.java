package com.co.judger.dao.impl;

import com.co.judger.dao.QuestionEntityService;
import com.co.judger.model.Question;
import com.co.judger.mapper.QuestionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author co
 * @since 2024-05-16
 */
@Service
public class QuestionEntityServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionEntityService {

}
