package com.xjm.mall.service.impl;

import com.xjm.mall.controller.vo.MallIndexConfigGoodsVO;
import com.xjm.mall.domain.MallGoodsInfo;
import com.xjm.mall.domain.MallIndexConfig;
import com.xjm.mall.enums.ServiceResultEnum;
import com.xjm.mall.mapper.MallGoodsInfoMapper;
import com.xjm.mall.mapper.MallIndexConfigMapper;
import com.xjm.mall.service.MallIndexConfigService;
import com.xjm.mall.utils.BeanUtil;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
@author Jm
@create 2020-03-31 20:04
*/  
@Service
public class MallIndexConfigServiceImpl implements MallIndexConfigService{

    @Resource
    private MallIndexConfigMapper mallIndexConfigMapper;

    @Resource
    private MallGoodsInfoMapper mallGoodsInfoMapper;

    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<MallIndexConfig> indexConfigs = mallIndexConfigMapper.findIndexConfigList(pageUtil);
        int total = mallIndexConfigMapper.getTotalIndexConfigs(pageUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }



    @Override
    public String saveIndexConfig(MallIndexConfig indexConfig) {
        //todo 判断是否存在该商品
        if (mallIndexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }



    @Override
    public String updateIndexConfig(MallIndexConfig indexConfig) {
        //todo 判断是否存在该商品
        MallIndexConfig temp = mallIndexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (mallIndexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }


    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
        return mallIndexConfigMapper.deleteBatch(ids) > 0;
    }

    @Override
    public List<MallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<MallIndexConfigGoodsVO> mallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<MallIndexConfig> indexConfigs = mallIndexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(MallIndexConfig::getGoodsId).collect(Collectors.toList());
            List<MallGoodsInfo> newBeeMallGoods = mallGoodsInfoMapper.selectByPrimaryKeys(goodsIds);
            mallIndexConfigGoodsVOS = BeanUtil.copyList(newBeeMallGoods, MallIndexConfigGoodsVO.class);
            for (MallIndexConfigGoodsVO newBeeMallIndexConfigGoodsVO : mallIndexConfigGoodsVOS) {
                String goodsName = newBeeMallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = newBeeMallIndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    newBeeMallIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    newBeeMallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return mallIndexConfigGoodsVOS;
    }

}
