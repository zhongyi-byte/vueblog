package com.zhongyi.shiro;

import java.io.Serializable;

import lombok.Data;

@Data
public class AccountProfile implements Serializable{
    private Long id;
    private String username;
    private String avater;
    private String email;
}
