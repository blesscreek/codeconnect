package com.co.backend.mapper;

import com.co.backend.model.po.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author co
 * @since 2024-04-07
 */
public interface QuestionMapper extends BaseMapper<Question> {
    /**
     * 查询题目列表
     * @param tagNames 标签字符串数组
     * @param titleKeyword 关键字
     * @param difficulty 难度
     * @param pageSize 每页记录数
     * @param offset 从第几条记录开始
     * @return
     */
    List<Question> selectQuestions(@Param("tagNames") String[] tagNames,
                                   @Param("titleKeyword") String titleKeyword,
                                   @Param("difficulty")Integer difficulty,
                                   @Param("pageSize") Long pageSize,
                                   @Param("offset") Long offset);
}
