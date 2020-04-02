package com.xjm.mall.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
@author Jm
@create 2020-04-02 15:48
*/  
@Data
@Table(name = "tb_newbee_mall_order_item")
public class MallOrderItem implements Serializable {
    /**
     * 订单关联购物项主键id
     */
    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(generator = "JDBC")
    private Long orderItemId;

    /**
     * 订单主键id
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 关联商品id
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 下单时商品的名称(订单快照)
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 下单时商品的主图(订单快照)
     */
    @Column(name = "goods_cover_img")
    private String goodsCoverImg;

    /**
     * 下单时商品的价格(订单快照)
     */
    @Column(name = "selling_price")
    private Integer sellingPrice;

    /**
     * 数量(订单快照)
     */
    @Column(name = "goods_count")
    private Integer goodsCount;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}