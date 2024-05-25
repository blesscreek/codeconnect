package com.co.backend.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author co
 * @since 2024-05-10
 */
@Data
@TableName("question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 题目名称
     */
    private String title;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 题目背景
     */
    private String background;

    /**
     * 题目描述
     */
    private String description;

    /**
     * 输入描述
     */
    private String input;

    /**
     * 输出描述
     */
    private String output;

    /**
     * 题面样例
     */
    private String examples;

    /**
     * 题目难度(0EASY，1MEDIUM，2HARD)
     */
    private Integer difficulty;

    /**
     * 备注、提醒
     */
    private String hint;

    /**
     * 题目模式(0是OI，1是ACM)
     */
    private Integer type;

    /**
     * 权限（0PUBLIC、1PRIVATE）
     */
    private Integer auth;

    /**
     * 判题模式（普通default、特判spj）
     */
    private String judgeMode;

    /**
     * 是否是组内私密题目
     */
    private Boolean isGroup;

    /**
     * 组队id
     */
    private Long gid;

    /**
     * 题目提交数
     */
    private Long submitNum;

    /**
     * 题目通过数
     */
    private Long acceptNum;

    /**
     * 时间限制（单位ms）
     */
    private Long timeLimit;

    /**
     * 空间限制（单位kb）
     */
    private Long memoryLimit;

    /**
     * 堆栈限制（单位mb）
     */
    private Long stackLimit;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更改时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除(0否，1删)
     */
    private Boolean isDelete;


}
