package com.xjm.mall.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
@author Jm
@create 2020-03-30 15:03
*/  
@Data
@Table(name = "tb_newbee_mall_user")
public class MallUser implements Serializable {
    /**
     * 用户主键id
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(generator = "JDBC")
    private Long userId;

    /**
     * 用户昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 登陆名称(默认为手机号)
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * MD5加密后的密码
     */
    @Column(name = "password_md5")
    private String passwordMd5;

    /**
     * 个性签名
     */
    @Column(name = "introduce_sign")
    private String introduceSign;

    /**
     * 收货地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 注销标识字段(0-正常 1-已注销)
     */
    @Column(name = "is_deleted")
    private Byte isDeleted;

    /**
     * 锁定标识字段(0-未锁定 1-已锁定)
     */
    @Column(name = "locked_flag")
    private Byte lockedFlag;

    /**
     * 注册时间
     */
    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}