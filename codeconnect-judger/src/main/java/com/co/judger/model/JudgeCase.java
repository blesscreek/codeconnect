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
 * @since 2024-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("judge_case")
@ApiModel(value="JudgeCase对象", description="")
public class JudgeCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "提交判题id")
    private Long judgeId;

    @ApiModelProperty(value = "用户id")
    private Long uid;

    @ApiModelProperty(value = "题目id")
    private Long qid;

    @ApiModelProperty(value = "测试样例id")
    private Long caseId;

    @ApiModelProperty(value = "具体看结果码")
    private Integer status;

    @ApiModelProperty(value = "测试该样例所用时间ms")
    private Long time;

    @ApiModelProperty(value = "测试该样例所用空间KB")
    private Long memory;

    @ApiModelProperty(value = "IO得分")
    private Integer score;

    @ApiModelProperty(value = "subtask分组的组号")
    private Integer groupNum;

    @ApiModelProperty(value = "排序")
    private Integer seq;

    @ApiModelProperty(value = "default,subtask_lowest,subtask_average")
    private String mode;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
