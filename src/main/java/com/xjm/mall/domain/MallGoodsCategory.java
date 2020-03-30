package com.xjm.mall.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
@author Jm
@create 2020-03-30 21:46
*/  
@Data
@Table(name = "tb_newbee_mall_goods_category")
public class MallGoodsCategory implements Serializable {
    /**
     * 分类id
     */
    @Id
    @Column(name = "category_id")
    @GeneratedValue(generator = "JDBC")
    private Long categoryId;

    /**
     * 分类级别(1-一级分类 2-二级分类 3-三级分类)
     */
    @Column(name = "category_level")
    private Byte categoryLevel;

    /**
     * 父分类id
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 分类名称
     */
    @Column(name = "category_name")
    private String categoryName;

    /**
     * 排序值(字段越大越靠前)
     */
    @Column(name = "category_rank")
    private Integer categoryRank;

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

    private static final long serialVersionUID = 1L;
}