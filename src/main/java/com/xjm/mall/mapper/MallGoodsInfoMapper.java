package com.xjm.mall.mapper;

import com.xjm.mall.domain.MallGoodsInfo;
import com.xjm.mall.utils.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

/**
@author Jm
@create 2020-03-31 14:23
*/  
public interface MallGoodsInfoMapper extends MyMapper<MallGoodsInfo> {
    List<MallGoodsInfo> findMallGoodsList(PageQueryUtil pageUtil);

    int getTotalMallGoods(PageQueryUtil pageUtil);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds, @Param("sellStatus") int sellStatus);
}