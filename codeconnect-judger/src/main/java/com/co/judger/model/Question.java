package com.co.judger.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author co
 * @since 2024-05-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("question")
@ApiModel(value="Question对象", description="")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "题目名称")
    private String title;

    @ApiModelProperty(value = "用户id")
    private Long uid;

    @ApiModelProperty(value = "题目背景")
    private String background;

    @ApiModelProperty(value = "题目描述")
    private String description;

    @ApiModelProperty(value = "输入描述")
    private String input;

    @ApiModelProperty(value = "输出描述")
    private String output;

    @ApiModelProperty(value = "题面样例")
    private String examples;

    @ApiModelProperty(value = "题目难度(0EASY，1MEDIUM，2HARD)")
    private Integer difficulty;

    @ApiModelProperty(value = "备注、提醒")
    private String hint;

    @ApiModelProperty(value = "题目模式(0是OI，1是ACM)")
    private Integer type;

    @ApiModelProperty(value = "权限（0PUBLIC、1PRIVATE）")
    private Integer auth;

    @ApiModelProperty(value = "判题模式（普通default、特判spj）")
    private String judgeMode;

    @ApiModelProperty(value = "是否是组内私密题目")
    private Boolean isGroup;

    @ApiModelProperty(value = "组队id")
    private Long gid;

    @ApiModelProperty(value = "题目提交数")
    private Long submitNum;

    @ApiModelProperty(value = "题目通过数")
    private Long acceptNum;

    @ApiModelProperty(value = "时间限制（单位ms）")
    private Long timeLimit;

    @ApiModelProperty(value = "空间限制（单位kb）")
    private Long memoryLimit;

    @ApiModelProperty(value = "堆栈限制（单位mb）")
    private Long stackLimit;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除(0否，1删)")
    private Boolean isDelete;


}
