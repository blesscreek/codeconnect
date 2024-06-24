package com.co.backend.model.po;

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
 * @since 2024-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("judge")
@ApiModel(value="Judge对象", description="")
public class Judge implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "题目id")
    private Long qid;

    @ApiModelProperty(value = "用户id")
    private Long uid;

    @ApiModelProperty(value = "提交的时间")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "结果码具体参考文档")
    private Integer status;

    @ApiModelProperty(value = "0为仅自己可见，1为全部人可见。")
    private Boolean share;

    @ApiModelProperty(value = "错误提醒（编译错误）")
    private String errorMessage;

    @ApiModelProperty(value = "运行时间(ms)")
    private Integer time;

    @ApiModelProperty(value = "运行内存（kb）")
    private Integer memory;

    @ApiModelProperty(value = "IO判题则不为空")
    private Integer score;

    @ApiModelProperty(value = "代码长度")
    private Integer length;

    @ApiModelProperty(value = "代码")
    private String code;

    @ApiModelProperty(value = "代码语言")
    private String language;

    @ApiModelProperty(value = "团队id，不为团队内提交则为null")
    private Long gid;

    @ApiModelProperty(value = "比赛id，非比赛题目默认为0")
    private Long cid;

    @ApiModelProperty(value = "比赛中题目排序id，非比赛题目默认为0")
    private Long cpid;

    @ApiModelProperty(value = "提交者所在ip")
    private String ip;

    @ApiModelProperty(value = "乐观锁")
    private Integer version;

    @ApiModelProperty(value = "oi排行榜得分")
    private Integer oiRankScore;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @ApiModelProperty(value = "是否删除（0否，1删）")
    private Boolean isDelete;


}
