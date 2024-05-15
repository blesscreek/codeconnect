package com.co.backend.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-07 11:47
 */

@TableName(value = "user_role")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;


}
