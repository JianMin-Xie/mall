package com.xjm.mall.service;

import com.xjm.mall.controller.vo.MallIndexConfigGoodsVO;
import com.xjm.mall.domain.MallIndexConfig;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;

import java.util.List;

/**
@author Jm
@create 2020-03-31 20:04
*/  
public interface MallIndexConfigService{
        /**
         * 后台分页
         *
         * @param pageUtil
         * @return
         */
        PageResult getConfigsPage(PageQueryUtil pageUtil);

        String saveIndexConfig(MallIndexConfig indexConfig);

        String updateIndexConfig(MallIndexConfig indexConfig);

        Boolean deleteBatch(Long[] ids);

        /**
         * 返回固定数量的首页配置商品对象(首页调用)
         *
         * @param number
         * @return
         */
        List<MallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

}
