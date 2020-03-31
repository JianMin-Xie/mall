package com.xjm.mall.service.impl;

import com.xjm.mall.domain.MallGoodsInfo;
import com.xjm.mall.enums.ServiceResultEnum;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xjm.mall.mapper.MallGoodsInfoMapper;
import com.xjm.mall.service.MallGoodsInfoService;

import java.util.Date;
import java.util.List;

/**
@author Jm
@create 2020-03-31 14:23
*/  
@Service
public class MallGoodsInfoServiceImpl implements MallGoodsInfoService{

    @Resource
    private MallGoodsInfoMapper mallGoodsInfoMapper;

    @Override
    public MallGoodsInfo getMallGoodsById(Long id) {
        return mallGoodsInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageResult getMallGoodsPage(PageQueryUtil pageUtil) {
        List<MallGoodsInfo> mallGoodsList = mallGoodsInfoMapper.findMallGoodsList(pageUtil);
        int totalMallGoods = mallGoodsInfoMapper.getTotalMallGoods(pageUtil);
        PageResult pageResult = new PageResult(mallGoodsList, totalMallGoods, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveMallGoods(MallGoodsInfo mallGoodsInfo) {
        if (mallGoodsInfoMapper.insertSelective(mallGoodsInfo)>0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }



    @Override
    public String updateMallGoods(MallGoodsInfo mallGoodsInfo) {
        MallGoodsInfo temp = mallGoodsInfoMapper.selectByPrimaryKey(mallGoodsInfo.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        mallGoodsInfo.setUpdateTime(new Date());
        if (mallGoodsInfoMapper.updateByPrimaryKeySelective(mallGoodsInfo)>0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return mallGoodsInfoMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }



}
