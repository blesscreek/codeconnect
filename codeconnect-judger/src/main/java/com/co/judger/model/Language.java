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
@TableName("language")
@ApiModel(value="Language对象", description="")
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "语言类型")
    private String contentType;

    @ApiModelProperty(value = "语言描述")
    private String description;

    @ApiModelProperty(value = "语言名字")
    private String name;

    @ApiModelProperty(value = "编译指令")
    private String compileCommand;

    @ApiModelProperty(value = "模板")
    private String template;

    @ApiModelProperty(value = "语言默认代码模板")
    private String codeTemplate;

    @ApiModelProperty(value = "是否可作为特殊判题的一种语言")
    private Boolean isSpj;

    @ApiModelProperty(value = "语言排序")
    private Integer seq;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
