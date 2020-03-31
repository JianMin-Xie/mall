package com.xjm.mall.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
@author Jm
@create 2020-03-31 20:04
*/  
@Data
@Table(name = "tb_newbee_mall_index_config")
public class MallIndexConfig implements Serializable {
    /**
     * 首页配置项主键id
     */
    @Id
    @Column(name = "config_id")
    @GeneratedValue(generator = "JDBC")
    private Long configId;

    /**
     * 显示字符(配置搜索时不可为空，其他可为空)
     */
    @Column(name = "config_name")
    private String configName;

    /**
     * 1-搜索框热搜 2-搜索下拉框热搜 3-(首页)热销商品 4-(首页)新品上线 5-(首页)为你推荐
     */
    @Column(name = "config_type")
    private Byte configType;

    /**
     * 商品id 默认为0
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 点击后的跳转地址(默认不跳转)
     */
    @Column(name = "redirect_url")
    private String redirectUrl;

    /**
     * 排序值(字段越大越靠前)
     */
    @Column(name = "config_rank")
    private Integer configRank;

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
     * 最新修改时间
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