package com.xjm.mall.service;

import com.xjm.mall.domain.MallCarousel;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;

/**
@author Jm
@create 2020-03-30 16:16
*/  
public interface MallCarouselService{

        /**
         * 后台分页
         *
         * @param pageUtil
         * @return
         */
        PageResult  getCarouselPage(PageQueryUtil pageUtil);

        /**
         * 添加轮播图
         * @param carousel
         * @return
         */
        String saveCarousel(MallCarousel carousel);

        /**
         * 删除轮播图
         * @param ids
         * @return
         */
        Boolean deleteBatch(Integer[] ids);

        /**
         * 根据Id获得详情
         * @param id
         * @return
         */
        MallCarousel getCarouselById(Integer id);

        /**
         * 更新轮播图
         * @param carousel
         * @return
         */
        String updateCarousel(MallCarousel carousel);
}
