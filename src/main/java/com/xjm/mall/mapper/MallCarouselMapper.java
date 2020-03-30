package com.xjm.mall.mapper;

import com.xjm.mall.domain.MallCarousel;
import com.xjm.mall.utils.PageQueryUtil;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

/**
@author Jm
@create 2020-03-30 16:16
*/  
public interface MallCarouselMapper extends MyMapper<MallCarousel> {

    List<MallCarousel> findCarouselList(PageQueryUtil pageUtil);

    int getTotalCarousels(PageQueryUtil pageUtil);
}