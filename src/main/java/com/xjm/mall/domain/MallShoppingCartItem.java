package com.xjm.mall.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
@author Jm
@create 2020-04-01 21:47
*/  
@Data
@Table(name = "tb_newbee_mall_shopping_cart_item")
public class MallShoppingCartItem implements Serializable {
    /**
     * 购物项主键id
     */
    @Id
    @Column(name = "cart_item_id")
    @GeneratedValue(generator = "JDBC")
    private Long cartItemId;

    /**
     * 用户主键id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 关联商品id
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 数量(最大为5)
     */
    @Column(name = "goods_count")
    private Integer goodsCount;

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
     * 最新修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}