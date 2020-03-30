package com.xjm.mall.mapper;

import com.xjm.mall.domain.MallGoodsCategory;
import com.xjm.mall.utils.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

/**
@author Jm
@create 2020-03-30 21:46
*/  
public interface MallGoodsCategoryMapper extends MyMapper<MallGoodsCategory> {

    List<MallGoodsCategory> findGoodsCategoryList(PageQueryUtil pageUtil);

    int getTotalGoodsCategories(PageQueryUtil pageUtil);

    List<MallGoodsCategory> selectByLevelAndParentIdsAndNumber(@Param("parentIds") List<Long> parentIds, @Param("categoryLevel") int categoryLevel, @Param("number") int number);

    MallGoodsCategory selectByLevelAndName(@Param("categoryLevel") Byte categoryLevel, @Param("categoryName") String categoryName);

    int deleteBatch(Integer[] ids);

}