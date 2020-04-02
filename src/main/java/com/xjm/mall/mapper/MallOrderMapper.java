package com.xjm.mall.mapper;

import com.xjm.mall.domain.MallOrder;
import com.xjm.mall.utils.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

/**
@author Jm
@create 2020-04-02 15:37
*/  
public interface MallOrderMapper extends MyMapper<MallOrder> {

    int getTotalMallOrders(PageQueryUtil pageUtil);

    List<MallOrder> findMallOrderList(PageQueryUtil pageUtil);

    int checkDone(@Param("orderIds") List<Long> asList);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

}