package com.xjm.mall.mapper;

import com.xjm.mall.domain.MallOrderItem;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

/**
@author Jm
@create 2020-04-02 15:48
*/  
public interface MallOrderItemMapper extends MyMapper<MallOrderItem> {

    /**
     * 批量insert订单项数据
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<MallOrderItem> orderItems);
}