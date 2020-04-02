package com.xjm.mall.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
@author Jm
@create 2020-04-02 15:37
*/  
@Data
@Table(name = "tb_newbee_mall_order")
public class MallOrder implements Serializable {
    /**
     * 订单表主键id
     */
    @Id
    @Column(name = "order_id")
    @GeneratedValue(generator = "JDBC")
    private Long orderId;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 用户主键id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 订单总价
     */
    @Column(name = "total_price")
    private Integer totalPrice;

    /**
     * 支付状态:0.未支付,1.支付成功,-1:支付失败
     */
    @Column(name = "pay_status")
    private Byte payStatus;

    /**
     * 0.无 1.支付宝支付 2.微信支付
     */
    @Column(name = "pay_type")
    private Byte payType;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private Date payTime;

    /**
     * 订单状态:0.待支付 1.已支付 2.配货完成 3:出库成功 4.交易成功 -1.手动关闭 -2.超时关闭 -3.商家关闭
     */
    @Column(name = "order_status")
    private Byte orderStatus;

    /**
     * 订单body
     */
    @Column(name = "extra_info")
    private String extraInfo;

    /**
     * 收货人姓名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 收货人手机号
     */
    @Column(name = "user_phone")
    private String userPhone;

    /**
     * 收货人收货地址
     */
    @Column(name = "user_address")
    private String userAddress;

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