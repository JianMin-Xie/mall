package com.xjm.mall.service.impl;

import com.xjm.mall.domain.MallGoodsCategory;
import com.xjm.mall.enums.ServiceResultEnum;
import com.xjm.mall.mapper.MallGoodsCategoryMapper;
import com.xjm.mall.service.MallGoodsCategoryService;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
@author Jm
@create 2020-03-30 21:46
*/  
@Service
public class MallGoodsCategoryServiceImpl implements MallGoodsCategoryService{

    @Resource
    private MallGoodsCategoryMapper mallGoodsCategoryMapper;

    @Override
    public PageResult getCategorisPage(PageQueryUtil pageUtil) {
        List<MallGoodsCategory> goodsCategoryList = mallGoodsCategoryMapper.findGoodsCategoryList(pageUtil);
        int totalGoodsCategories = mallGoodsCategoryMapper.getTotalGoodsCategories(pageUtil);
        PageResult pageResult = new PageResult(goodsCategoryList, totalGoodsCategories, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public MallGoodsCategory getGoodsCategoryById(Long id) {
        return mallGoodsCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<MallGoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel) {
        //0代表查询所有
        return mallGoodsCategoryMapper.selectByLevelAndParentIdsAndNumber(parentIds, categoryLevel, 0);
    }

    @Override
    public String saveCategory(MallGoodsCategory goodsCategory) {
        MallGoodsCategory temp = mallGoodsCategoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(), goodsCategory.getCategoryName());
        if (temp != null) {
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        if (mallGoodsCategoryMapper.insertSelective(goodsCategory) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateGoodsCategory(MallGoodsCategory goodsCategory) {
        MallGoodsCategory temp = mallGoodsCategoryMapper.selectByPrimaryKey(goodsCategory.getCategoryId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        MallGoodsCategory temp2 = mallGoodsCategoryMapper.selectByLevelAndName(goodsCategory.getCategoryLevel(), goodsCategory.getCategoryName());
        if (temp2 != null && !temp2.getCategoryId().equals(goodsCategory.getCategoryId())) {
            //同名且不同id 不能继续修改
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        goodsCategory.setUpdateTime(new Date());
        if (mallGoodsCategoryMapper.updateByPrimaryKeySelective(goodsCategory) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除分类数据
        return mallGoodsCategoryMapper.deleteBatch(ids) > 0;
    }
}
