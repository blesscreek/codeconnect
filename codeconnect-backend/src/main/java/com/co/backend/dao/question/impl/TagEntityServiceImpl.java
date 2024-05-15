package com.co.backend.dao.question.impl;

import com.co.backend.model.po.Tag;
import com.co.backend.dao.question.TagEntityService;
import com.co.backend.mapper.TagMapper;
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
public class TagEntityServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagEntityService {

}
