package com.xjm.mall.service;

import com.xjm.mall.controller.vo.MallOrderDetailVO;
import com.xjm.mall.controller.vo.MallOrderItemVO;
import com.xjm.mall.controller.vo.MallShoppingCartItemVO;
import com.xjm.mall.controller.vo.MallUserVO;
import com.xjm.mall.domain.MallOrder;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;

import java.util.List;

/**
@author Jm
@create 2020-04-02 15:37
*/  
public interface MallOrderService{

        /**
         * 获取订单详情
         *
         * @param orderNo
         * @param userId
         * @return
         */
        MallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

        /**
         * 保存订单
         *
         * @param user
         * @param myShoppingCartItems
         * @return
         */
        String saveOrder(MallUserVO user, List<MallShoppingCartItemVO> myShoppingCartItems);

        /**
         * 我的订单列表
         *
         * @param pageUtil
         * @return
         */
        PageResult getMyOrders(PageQueryUtil pageUtil);

        String paySuccess(String orderNo, int payType);

        /**
         * 获取订单详情
         *
         * @param orderNo
         * @return
         */
        MallOrder getMallOrderByOrderNo(String orderNo);

        /**
         * 后台分页
         *
         * @param pageUtil
         * @return
         */
        PageResult getMallOrdersPage(PageQueryUtil pageUtil);

        /**
         * 订单信息修改
         *
         * @param mallOrder
         * @return
         */
        String updateOrderInfo(MallOrder mallOrder);

        List<MallOrderItemVO> getOrderItems(Long id);

        /**
         * 配货
         *
         * @param ids
         * @return
         */
        String checkDone(Long[] ids);

        /**
         * 出库
         *
         * @param ids
         * @return
         */
        String checkOut(Long[] ids);


        /**
         * 关闭订单
         *
         * @param ids
         * @return
         */
        String closeOrder(Long[] ids);

        /**
         * 手动取消订单
         *
         * @param orderNo
         * @param userId
         * @return
         */
        String cancelOrder(String orderNo, Long userId);

        /**
         * 确认收货
         *
         * @param orderNo
         * @param userId
         * @return
         */
        String finishOrder(String orderNo, Long userId);

}

