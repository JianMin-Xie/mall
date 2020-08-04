package com.xjm.mall.service;

import com.xjm.mall.domain.MallGoodsInfo;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;

/**
@author Jm
@create 2020-03-31 14:23
*/  
public interface MallGoodsInfoService{


    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    MallGoodsInfo getMallGoodsById(Long id);

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param mallGoodsInfo
     * @return
     */
    String saveMallGoods(MallGoodsInfo mallGoodsInfo);

    /**
     * 修改商品信息
     *
     * @param mallGoodsInfo
     * @return
     */
    String updateMallGoods(MallGoodsInfo mallGoodsInfo);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchMallGoods(PageQueryUtil pageUtil);

}
