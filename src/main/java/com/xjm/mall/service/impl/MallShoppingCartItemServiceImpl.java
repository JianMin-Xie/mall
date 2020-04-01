package com.xjm.mall.service.impl;

import com.xjm.mall.common.Constants;
import com.xjm.mall.controller.vo.MallShoppingCartItemVO;
import com.xjm.mall.domain.MallGoodsInfo;
import com.xjm.mall.domain.MallShoppingCartItem;
import com.xjm.mall.enums.ServiceResultEnum;
import com.xjm.mall.mapper.MallGoodsInfoMapper;
import com.xjm.mall.utils.BeanUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xjm.mall.mapper.MallShoppingCartItemMapper;
import com.xjm.mall.service.MallShoppingCartItemService;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
@author Jm
@create 2020-04-01 21:47
*/  
@Service
public class MallShoppingCartItemServiceImpl implements MallShoppingCartItemService {

    @Resource
    private MallShoppingCartItemMapper mallShoppingCartItemMapper;
    
    @Resource
    private MallGoodsInfoMapper mallGoodsInfoMapper;

    @Override
    public List<MallShoppingCartItemVO> getMyShoppingCartItems(Long  MallUserId) {
        List<MallShoppingCartItemVO>  MallShoppingCartItemVOS = new ArrayList<>();
        List<MallShoppingCartItem>  MallShoppingCartItems = mallShoppingCartItemMapper.selectByUserId( MallUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty( MallShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long>  MallGoodsIds =  MallShoppingCartItems.stream().map(MallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<MallGoodsInfo>  MallGoods = mallGoodsInfoMapper.selectByPrimaryKeys( MallGoodsIds);
            Map<Long, MallGoodsInfo>  MallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty( MallGoods)) {
                 MallGoodsMap =  MallGoods.stream().collect(Collectors.toMap(MallGoodsInfo::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (MallShoppingCartItem  MallShoppingCartItem :  MallShoppingCartItems) {
                MallShoppingCartItemVO  MallShoppingCartItemVO = new  MallShoppingCartItemVO();
                BeanUtil.copyProperties( MallShoppingCartItem,  MallShoppingCartItemVO);
                if ( MallGoodsMap.containsKey( MallShoppingCartItem.getGoodsId())) {
                    MallGoodsInfo  MallGoodsTemp =  MallGoodsMap.get( MallShoppingCartItem.getGoodsId());
                     MallShoppingCartItemVO.setGoodsCoverImg( MallGoodsTemp.getGoodsCoverImg());
                    String goodsName =  MallGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                     MallShoppingCartItemVO.setGoodsName(goodsName);
                     MallShoppingCartItemVO.setSellingPrice( MallGoodsTemp.getSellingPrice());
                     MallShoppingCartItemVOS.add( MallShoppingCartItemVO);
                }
            }
        }
        return  MallShoppingCartItemVOS;
    }

    @Override
    public String saveMallCartItem(MallShoppingCartItem mallShoppingCartItem) {
        Example example = new Example(MallShoppingCartItem.class);
        example.createCriteria().andEqualTo("userId", mallShoppingCartItem.getUserId()).andEqualTo("goodsId", mallShoppingCartItem.getGoodsId());
        MallShoppingCartItem temp = mallShoppingCartItemMapper.selectOneByExample(example);
        if (temp != null) {
            //已存在则修改该记录
            //todo count = tempCount + 1
            temp.setGoodsCount(mallShoppingCartItem.getGoodsCount());
            return updateMallCartItem(temp);
        }
        MallGoodsInfo mallGoodsInfo = mallGoodsInfoMapper.selectByPrimaryKey(mallShoppingCartItem.getGoodsId());
        //商品为空
        if (mallGoodsInfo == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        Example example2 = new Example(MallShoppingCartItem.class);
        example2.createCriteria().andEqualTo("userId",mallShoppingCartItem.getUserId());
        int totalItem = mallShoppingCartItemMapper.selectCountByExample(example2);
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //保存记录
        if (mallShoppingCartItemMapper.insertSelective(mallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateMallCartItem(MallShoppingCartItem mallShoppingCartItem) {
        MallShoppingCartItem mallShoppingCartItemUpdate = mallShoppingCartItemMapper.selectByPrimaryKey(mallShoppingCartItem.getCartItemId());
        if (mallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出最大数量
        if (mallShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //todo 数量相同不会进行修改
        //todo userId不同不能修改
        mallShoppingCartItemUpdate.setGoodsCount(mallShoppingCartItem.getGoodsCount());
        mallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (mallShoppingCartItemMapper.updateByPrimaryKeySelective(mallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Boolean deleteById(Long MallShoppingCartItemId) {
        return mallShoppingCartItemMapper.deleteByPrimaryKey(MallShoppingCartItemId) > 0;
    }
}
