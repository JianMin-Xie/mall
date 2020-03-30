package com.xjm.mall.service.impl;
import java.util.Date;

import com.xjm.mall.commons.ServiceResultEnum;
import com.xjm.mall.domain.MallCarousel;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xjm.mall.mapper.MallCarouselMapper;
import com.xjm.mall.service.MallCarouselService;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

/**
@author Jm
@create 2020-03-30 16:16
*/  
@Service
public class MallCarouselServiceImpl implements MallCarouselService{

    @Resource
    private MallCarouselMapper mallCarouselMapper;

    @Override
    public PageResult  getCarouselPage(PageQueryUtil pageUtil) {
        List<MallCarousel> mallCarouselList = mallCarouselMapper.findCarouselList(pageUtil);
        int totalCarousels = mallCarouselMapper.getTotalCarousels(pageUtil);
        PageResult pageResult = new PageResult(mallCarouselList, totalCarousels, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveCarousel(MallCarousel carousel) {
        if (mallCarouselMapper.insertSelective(carousel) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
//        return mallCarouselMapper.deleteBatch(ids) > 0;
        Example example = new Example(MallCarousel.class);
        example.createCriteria().andIn("carouselId", Arrays.asList(ids));
        return mallCarouselMapper.deleteByExample(example)>0;
    }



    @Override
    public MallCarousel getCarouselById(Integer id) {
        return mallCarouselMapper.selectByPrimaryKey(id);
    }

    @Override
    public String updateCarousel(MallCarousel carousel) {
        MallCarousel temp = mallCarouselMapper.selectByPrimaryKey(carousel.getCarouselId());

        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }

        temp.setCarouselRank(carousel.getCarouselRank());
        temp.setCarouselUrl(carousel.getCarouselUrl());
        temp.setRedirectUrl(carousel.getRedirectUrl());
        temp.setUpdateTime(new Date());

        if (mallCarouselMapper.updateByPrimaryKeySelective(temp) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }
}
