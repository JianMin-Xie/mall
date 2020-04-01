package com.xjm.mall.service;

import com.xjm.mall.controller.vo.MallShoppingCartItemVO;
import com.xjm.mall.domain.MallShoppingCartItem;

import java.util.List;

/**
@author Jm
@create 2020-04-01 21:47
*/  
public interface MallShoppingCartItemService {

        /**
         * 获取我的购物车中的列表数据
         *
         * @param MallUserId
         * @return
         */
        List<MallShoppingCartItemVO> getMyShoppingCartItems(Long MallUserId);

        /**
         * 保存商品至购物车中
         *
         * @param mallShoppingCartItem
         * @return
         */
        String saveMallCartItem(MallShoppingCartItem mallShoppingCartItem);

        /**
         * 修改购物车中的属性
         *
         * @param mallShoppingCartItem
         * @return
         */
        String updateMallCartItem(MallShoppingCartItem mallShoppingCartItem);

        /**
         * 删除购物车中的商品
         *
         * @param MallShoppingCartItemId
         * @return
         */
        Boolean deleteById(Long MallShoppingCartItemId);
}
