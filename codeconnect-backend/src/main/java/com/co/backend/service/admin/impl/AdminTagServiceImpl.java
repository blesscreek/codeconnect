package com.co.backend.service.admin.impl;

import com.co.backend.common.result.ResponseResult;
import com.co.backend.dao.question.TagEntityService;
import com.co.backend.model.po.QuestionTag;
import com.co.backend.model.po.Tag;
import com.co.backend.service.admin.AdminTagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-06-25 16:40
 */
@Service
public class AdminTagServiceImpl implements AdminTagService {
    @Autowired
    private TagEntityService tagEntityService;
    @Override
    public ResponseResult getTag() {
        List<Tag> list = tagEntityService.list();
        LinkedList<Tag> tagLists = new LinkedList<>();
        for (Tag tag : list) {
            tagLists.add(tag);
        }
        return new ResponseResult(200, "获取标签成功", tagLists);
    }
}
