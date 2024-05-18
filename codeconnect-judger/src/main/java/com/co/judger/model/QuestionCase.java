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
@TableName("question_case")
@ApiModel(value="QuestionCase对象", description="")
public class QuestionCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "题目id")
    private Long qid;

    @ApiModelProperty(value = "测试样例的输入")
    private String input;

    @ApiModelProperty(value = "测试样例的输出")
    private String output;

    @ApiModelProperty(value = "是否为展示的测试用例（0否1是）")
    private Boolean isShow;

    @ApiModelProperty(value = "OI赛制分数")
    private Integer score;

    @ApiModelProperty(value = "0可用，1不可用")
    private Boolean status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
