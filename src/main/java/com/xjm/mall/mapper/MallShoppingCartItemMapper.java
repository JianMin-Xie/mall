package com.xjm.mall.mapper;

import com.xjm.mall.domain.MallShoppingCartItem;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

/**
@author Jm
@create 2020-04-01 21:47
*/  
public interface MallShoppingCartItemMapper extends MyMapper<MallShoppingCartItem> {

    List<MallShoppingCartItem> selectByUserId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("number") int number);


}