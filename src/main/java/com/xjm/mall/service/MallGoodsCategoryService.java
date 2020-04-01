package com.xjm.mall.service;

import com.xjm.mall.controller.vo.MallIndexCategoryVO;
import com.xjm.mall.controller.vo.SearchPageCategoryVO;
import com.xjm.mall.domain.MallGoodsCategory;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;

import java.util.List;

/**
@author Jm
@create 2020-03-30 21:46
*/  
public interface MallGoodsCategoryService{
        /**
         * 后台分页
         *
         * @param pageUtil
         * @return
         */
        PageResult getCategorisPage(PageQueryUtil pageUtil);

        MallGoodsCategory getGoodsCategoryById(Long id);



        /**
         * 添加
         * @param goodsCategory
         * @return
         */
        String saveCategory(MallGoodsCategory goodsCategory);

        /**
         * 修改
         * @param goodsCategory
         * @return
         */
        String updateGoodsCategory(MallGoodsCategory goodsCategory);

        /**
         * 删除
         * @param ids
         * @return
         */
        Boolean deleteBatch(Integer[] ids);

        /**
         * 返回分类数据(首页调用)
         *
         * @return
         */
        List<MallIndexCategoryVO> getCategoriesForIndex();

        /**
         * 返回分类数据(搜索页调用)
         *
         * @param categoryId
         * @return
         */
        SearchPageCategoryVO getCategoriesForSearch(Long categoryId);

        /**
         * 根据parentId和level获取分类列表
         *
         * @param parentIds
         * @param categoryLevel
         * @return
         */
        List<MallGoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);

}
