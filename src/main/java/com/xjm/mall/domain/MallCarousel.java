package com.xjm.mall.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
@author Jm
@create 2020-03-30 16:16
*/  
@Data
@Table(name = "tb_newbee_mall_carousel")
public class MallCarousel implements Serializable {
    /**
     * 首页轮播图主键id
     */
    @Id
    @Column(name = "carousel_id")
    @GeneratedValue(generator = "JDBC")
    private Integer carouselId;

    /**
     * 轮播图
     */
    @Column(name = "carousel_url")
    private String carouselUrl;

    /**
     * 点击后的跳转地址(默认不跳转)
     */
    @Column(name = "redirect_url")
    private String redirectUrl;

    /**
     * 排序值(字段越大越靠前)
     */
    @Column(name = "carousel_rank")
    private Integer carouselRank;

    /**
     * 删除标识字段(0-未删除 1-已删除)
     */
    @Column(name = "is_deleted")
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建者id
     */
    @Column(name = "create_user")
    private Integer createUser;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 修改者id
     */
    @Column(name = "update_user")
    private Integer updateUser;

    private static final long serialVersionUID = -5009870603425274600L;
}