package com.xjm.mall.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

/**
@author Jm
@create 2020-03-29 21:58
*/  
@Data
@Table(name = "tb_newbee_mall_admin_user")
public class MallAdminUser implements Serializable {
    /**
     * 管理员id
     */
    @Id
    @Column(name = "admin_user_id")
    @GeneratedValue(generator = "JDBC")
    private Integer adminUserId;

    /**
     * 管理员登陆名称
     */
    @Column(name = "login_user_name")
    private String loginUserName;

    /**
     * 管理员登陆密码
     */
    @Column(name = "login_password")
    private String loginPassword;

    /**
     * 管理员显示昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 是否锁定 0未锁定 1已锁定无法登陆
     */
    @Column(name = "locked")
    private Byte locked;

    private static final long serialVersionUID = 1L;
}