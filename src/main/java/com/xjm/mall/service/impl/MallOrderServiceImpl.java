package com.xjm.mall.service.impl;

import com.xjm.mall.controller.vo.MallOrderDetailVO;
import com.xjm.mall.controller.vo.MallOrderListVO;
import com.xjm.mall.controller.vo.MallShoppingCartItemVO;
import com.xjm.mall.controller.vo.MallUserVO;
import com.xjm.mall.controller.vo.MallOrderItemVO;
import com.xjm.mall.domain.MallGoodsInfo;
import com.xjm.mall.domain.MallOrder;
import com.xjm.mall.domain.MallOrderItem;
import com.xjm.mall.dto.StockNumDTO;
import com.xjm.mall.enums.MallOrderStatusEnum;
import com.xjm.mall.enums.PayStatusEnum;
import com.xjm.mall.enums.PayTypeEnum;
import com.xjm.mall.enums.ServiceResultEnum;
import com.xjm.mall.exception.MallException;
import com.xjm.mall.mapper.MallGoodsInfoMapper;
import com.xjm.mall.mapper.MallOrderItemMapper;
import com.xjm.mall.mapper.MallShoppingCartItemMapper;
import com.xjm.mall.utils.BeanUtil;
import com.xjm.mall.utils.NumberUtil;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xjm.mall.mapper.MallOrderMapper;
import com.xjm.mall.service.MallOrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
@author Jm
@create 2020-04-02 15:37
*/  
@Service
public class MallOrderServiceImpl implements MallOrderService{

    @Resource
    private MallOrderMapper mallOrderMapper;

    @Resource
    private MallGoodsInfoMapper mallGoodsInfoMapper;

    @Resource
    private MallShoppingCartItemMapper mallShoppingCartItemMapper;

    @Resource
    private MallOrderItemMapper mallOrderItemMapper;

    @Override
    public MallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        Example example = new Example(MallOrder.class);
        example.createCriteria().andEqualTo("orderNo",orderNo).andEqualTo("isDeleted",0);
        MallOrder mallOrder = mallOrderMapper.selectOneByExample(example);
        if (mallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            Example example2 = new Example(MallOrderItem.class);
            example2.createCriteria().andEqualTo("orderId",mallOrder.getOrderId());
            List<MallOrderItem> orderItems = mallOrderItemMapper.selectByExample(example2);
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<MallOrderItemVO> mallOrderItemVOS = BeanUtil.copyList(orderItems, MallOrderItemVO.class);
                MallOrderDetailVO mallOrderDetailVO = new MallOrderDetailVO();
                BeanUtil.copyProperties(mallOrder, mallOrderDetailVO);
                mallOrderDetailVO.setOrderStatusString(MallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(mallOrderDetailVO.getOrderStatus()).getName());
                mallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(mallOrderDetailVO.getPayType()).getName());
                mallOrderDetailVO.setNewBeeMallOrderItemVOS(mallOrderItemVOS);
                return mallOrderDetailVO;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public String saveOrder(MallUserVO user, List<MallShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(MallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(MallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<MallGoodsInfo> mallGoodsInfos = mallGoodsInfoMapper.selectByPrimaryKeys(goodsIds);
        Map<Long, MallGoodsInfo> mallGoodsInfoMap = mallGoodsInfos.stream().collect(Collectors.toMap(MallGoodsInfo::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (MallShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!mallGoodsInfoMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                MallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > mallGoodsInfoMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                MallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(mallGoodsInfos)) {
            if (mallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = mallGoodsInfoMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    MallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                MallOrder mallOrder = new MallOrder();
                mallOrder.setOrderNo(orderNo);
                mallOrder.setUserId(user.getUserId());
                mallOrder.setUserAddress(user.getAddress());
                //总价
                for (MallShoppingCartItemVO mallShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += mallShoppingCartItemVO.getGoodsCount() * mallShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    MallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                mallOrder.setTotalPrice(priceTotal);
                //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
                String extraInfo = "";
                mallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (mallOrderMapper.insertSelective(mallOrder) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<MallOrderItem> mallOrderItems = new ArrayList<>();
                    for (MallShoppingCartItemVO mallShoppingCartItemVO : myShoppingCartItems) {
                        MallOrderItem mallOrderItem = new MallOrderItem();
                        //使用BeanUtil工具类将MallShoppingCartItemVO中的属性复制到MallOrderItem对象中
                        BeanUtil.copyProperties(mallShoppingCartItemVO, mallOrderItem);
                        //NewBeeMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        mallOrderItem.setOrderId(mallOrder.getOrderId());
                        mallOrderItems.add(mallOrderItem);
                    }
                    //保存至数据库
                    if (mallOrderItemMapper.insertBatch(mallOrderItems) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    MallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            MallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        MallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }


    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = mallOrderMapper.getTotalMallOrders(pageUtil);
        List<MallOrder> mallOrderList = mallOrderMapper.findMallOrderList(pageUtil);
        List<MallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(mallOrderList, MallOrderListVO.class);
            //设置订单状态中文显示值
            for (MallOrderListVO mallOrderListVO : orderListVOS) {
                mallOrderListVO.setOrderStatusString(MallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(mallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = mallOrderList.stream().map(MallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {

                Example example = new Example(MallOrderItem.class);
                example.createCriteria().andIn("orderId",orderIds);
                List<MallOrderItem> orderItems = mallOrderItemMapper.selectByExample(example);

                Map<Long, List<MallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(MallOrderItem::getOrderId));
                for (MallOrderListVO newBeeMallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(newBeeMallOrderListVO.getOrderId())) {
                        List<MallOrderItem> orderItemListTemp = itemByOrderIdMap.get(newBeeMallOrderListVO.getOrderId());
                        //将MallOrderItem对象列表转换成MallOrderItemVO对象列表
                        List<MallOrderItemVO> mallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, MallOrderItemVO.class);
                        newBeeMallOrderListVO.setNewBeeMallOrderItemVOS(mallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }


    @Override
    public String paySuccess(String orderNo, int payType) {
        Example example = new Example(MallOrder.class);
        example.createCriteria().andEqualTo("orderNo",orderNo).andEqualTo("isDeleted", 0);
        MallOrder mallOrder = mallOrderMapper.selectOneByExample(example);
        if (mallOrder != null) {
            //todo 订单状态判断 非待支付状态下不进行修改操作
            mallOrder.setOrderStatus((byte) MallOrderStatusEnum.OREDER_PAID.getOrderStatus());
            mallOrder.setPayType((byte) payType);
            mallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            mallOrder.setPayTime(new Date());
            mallOrder.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKeySelective(mallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }


    @Override
    public MallOrder getMallOrderByOrderNo(String orderNo) {
        Example example = new Example(MallOrder.class);
        example.createCriteria().andEqualTo("orderNo",orderNo);
        MallOrder mallOrder = mallOrderMapper.selectOneByExample(example);
        return mallOrder;
    }

    @Override
    public PageResult getMallOrdersPage(PageQueryUtil pageUtil) {
        List<MallOrder> mallOrderList = mallOrderMapper.findMallOrderList(pageUtil);
        int total = mallOrderMapper.getTotalMallOrders(pageUtil);
        PageResult pageResult = new PageResult(mallOrderList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(MallOrder mallOrder) {
        MallOrder temp = mallOrderMapper.selectByPrimaryKey(mallOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(mallOrder.getTotalPrice());
            temp.setUserAddress(mallOrder.getUserAddress());
            temp.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    public List<MallOrderItemVO> getOrderItems(Long id) {
        MallOrder mallOrder = mallOrderMapper.selectByPrimaryKey(id);
        if (mallOrder != null) {
            Example example = new Example(MallOrderItem.class);
            example.createCriteria().andEqualTo("orderId", mallOrder.getOrderId());
            List<MallOrderItem> orderItems = mallOrderItemMapper.selectByExample(example);
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<MallOrderItemVO> newBeeMallOrderItemVOS = BeanUtil.copyList(orderItems, MallOrderItemVO.class);
                return newBeeMallOrderItemVOS;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        Example example = new Example(MallOrder.class);
        example.createCriteria().andIn("orderId", Arrays.asList(ids));
        List<MallOrder> orders = mallOrderMapper.selectByExample(example);
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (MallOrder mallOrder : orders) {
                if (mallOrder.getIsDeleted() == 1) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                    continue;
                }
                if (mallOrder.getOrderStatus() != 1) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (mallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        Example example = new Example(MallOrder.class);
        example.createCriteria().andIn("orderId", Arrays.asList(ids));
        List<MallOrder> orders = mallOrderMapper.selectByExample(example);
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (MallOrder newBeeMallOrder : orders) {
                if (newBeeMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += newBeeMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (newBeeMallOrder.getOrderStatus() != 1 && newBeeMallOrder.getOrderStatus() != 2) {
                    errorOrderNos += newBeeMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (mallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        Example example = new Example(MallOrder.class);
        example.createCriteria().andIn("orderId", Arrays.asList(ids));
        List<MallOrder> orders = mallOrderMapper.selectByExample(example);
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (MallOrder mallOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (mallOrder.getIsDeleted() == 1) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (mallOrder.getOrderStatus() == 4 || mallOrder.getOrderStatus() < 0) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (mallOrderMapper.closeOrder(Arrays.asList(ids), MallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        Example example = new Example(MallOrder.class);
        example.createCriteria().andEqualTo("orderNo",orderNo).andEqualTo("isDeleted",0);
        MallOrder mallOrder = mallOrderMapper.selectOneByExample(example);
        if (mallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            if (mallOrderMapper.closeOrder(Collections.singletonList(mallOrder.getOrderId()), MallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        Example example = new Example(MallOrder.class);
        example.createCriteria().andEqualTo("orderNo",orderNo).andEqualTo("isDeleted",0);
        MallOrder mallOrder = mallOrderMapper.selectOneByExample(example);
        if (mallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            mallOrder.setOrderStatus((byte) MallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            mallOrder.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKeySelective(mallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }
}
