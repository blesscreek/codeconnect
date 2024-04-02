package com.co.codeconnectjudge.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author co
 * @Version 1.0
 * @Description
 * @Date 2024-04-02 19:34
 */

@Data
public class RegisterUser implements Serializable {
    private static final long serialVersionUID = 3191241716373120793L;

    private String account;

    private String password;

    private String checkPassword;
}
