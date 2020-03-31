package com.xjm.mall.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

/**
@author Jm
@create 2020-03-31 14:23
*/  
@Data
@Table(name = "tb_newbee_mall_goods_info")
public class MallGoodsInfo implements Serializable {
    /**
     * 商品表主键id
     */
    @Id
    @Column(name = "goods_id")
    @GeneratedValue(generator = "JDBC")
    private Long goodsId;

    /**
     * 商品名
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * 商品简介
     */
    @Column(name = "goods_intro")
    private String goodsIntro;

    /**
     * 关联分类id
     */
    @Column(name = "goods_category_id")
    private Long goodsCategoryId;

    /**
     * 商品主图
     */
    @Column(name = "goods_cover_img")
    private String goodsCoverImg;

    /**
     * 商品轮播图
     */
    @Column(name = "goods_carousel")
    private String goodsCarousel;

    /**
     * 商品详情
     */
    @Column(name = "goods_detail_content")
    private String goodsDetailContent;

    /**
     * 商品价格
     */
    @Column(name = "original_price")
    private Integer originalPrice;

    /**
     * 商品实际售价
     */
    @Column(name = "selling_price")
    private Integer sellingPrice;

    /**
     * 商品库存数量
     */
    @Column(name = "stock_num")
    private Integer stockNum;

    /**
     * 商品标签
     */
    @Column(name = "tag")
    private String tag;

    /**
     * 商品上架状态 0-下架 1-上架
     */
    @Column(name = "goods_sell_status")
    private Byte goodsSellStatus;

    /**
     * 添加者主键id
     */
    @Column(name = "create_user")
    private Integer createUser;

    /**
     * 商品添加时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改者主键id
     */
    @Column(name = "update_user")
    private Integer updateUser;

    /**
     * 商品修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}