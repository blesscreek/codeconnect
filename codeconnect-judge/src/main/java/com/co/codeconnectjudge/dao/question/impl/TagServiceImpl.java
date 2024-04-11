package com.co.codeconnectjudge.dao.question.impl;

import com.co.codeconnectjudge.dao.question.TagService;
import com.co.codeconnectjudge.model.po.Tag;
import com.co.codeconnectjudge.mapper.TagMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author co
 * @since 2024-04-10
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

}
