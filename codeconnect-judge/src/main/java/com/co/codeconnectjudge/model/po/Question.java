package com.co.codeconnectjudge.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2024-04-07
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

    @ApiModelProperty(value = "作者")
    private String author;

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

    @ApiModelProperty(value = "题目难度(0简单，1中等，2困难)")
    private Integer difficulty;

    @ApiModelProperty(value = "备注、提醒")
    private String hint;

    @ApiModelProperty(value = "权限（0公开、1私有）")
    private Integer auth;

    @ApiModelProperty(value = "判题模式（普通default，特判spj）")
    private String judgeMode;

    @ApiModelProperty(value = "题目提交数")
    private Long submitNum;

    @ApiModelProperty(value = "题目通过数")
    private Long acceptNum;

    @ApiModelProperty(value = "时间限制（单位ms）")
    private Integer timeLimit;

    @ApiModelProperty(value = "空间限制（单位kb）")
    private Integer memoryLimit;

    @ApiModelProperty(value = "堆栈限制（单位mb）")
    private Integer stackLimit;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除(0否，1删)")
    private Integer isDelete;


}
