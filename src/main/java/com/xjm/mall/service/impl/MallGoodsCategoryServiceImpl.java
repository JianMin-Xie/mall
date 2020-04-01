package com.xjm.mall.service.impl;

import com.xjm.mall.common.Constants;
import com.xjm.mall.controller.vo.MallIndexCategoryVO;
import com.xjm.mall.controller.vo.SearchPageCategoryVO;
import com.xjm.mall.controller.vo.SecondLevelCategoryVO;
import com.xjm.mall.controller.vo.ThirdLevelCategoryVO;
import com.xjm.mall.domain.MallGoodsCategory;
import com.xjm.mall.enums.MallCategoryLevelEnum;
import com.xjm.mall.enums.ServiceResultEnum;
import com.xjm.mall.mapper.MallGoodsCategoryMapper;
import com.xjm.mall.service.MallGoodsCategoryService;
import com.xjm.mall.utils.BeanUtil;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

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

    @Override
    public List<MallIndexCategoryVO> getCategoriesForIndex() {
        List<MallIndexCategoryVO> mallIndexCategoryVOS = new ArrayList<>();
        //获取一级分类的固定数量的数据
        List<MallGoodsCategory> firstLevelCategories = mallGoodsCategoryMapper.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), MallCategoryLevelEnum.LEVEL_ONE.getLevel(), Constants.INDEX_CATEGORY_NUMBER);
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            List<Long> firstLevelCategoryIds = firstLevelCategories.stream().map(MallGoodsCategory::getCategoryId).collect(Collectors.toList());
            //获取二级分类的数据
            List<MallGoodsCategory> secondLevelCategories = mallGoodsCategoryMapper.selectByLevelAndParentIdsAndNumber(firstLevelCategoryIds, MallCategoryLevelEnum.LEVEL_TWO.getLevel(), 0);
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                List<Long> secondLevelCategoryIds = secondLevelCategories.stream().map(MallGoodsCategory::getCategoryId).collect(Collectors.toList());
                //获取三级分类的数据
                List<MallGoodsCategory> thirdLevelCategories = mallGoodsCategoryMapper.selectByLevelAndParentIdsAndNumber(secondLevelCategoryIds, MallCategoryLevelEnum.LEVEL_THREE.getLevel(), 0);
                if (!CollectionUtils.isEmpty(thirdLevelCategories)) {
                    //根据 parentId 将 thirdLevelCategories 分组
                    Map<Long, List<MallGoodsCategory>> thirdLevelCategoryMap = thirdLevelCategories.stream().collect(groupingBy(MallGoodsCategory::getParentId));
                    List<SecondLevelCategoryVO> secondLevelCategoryVOS = new ArrayList<>();
                    //处理二级分类
                    for (MallGoodsCategory secondLevelCategory : secondLevelCategories) {
                        SecondLevelCategoryVO secondLevelCategoryVO = new SecondLevelCategoryVO();
                        BeanUtil.copyProperties(secondLevelCategory, secondLevelCategoryVO);
                        //如果该二级分类下有数据则放入 secondLevelCategoryVOS 对象中
                        if (thirdLevelCategoryMap.containsKey(secondLevelCategory.getCategoryId())) {
                            //根据二级分类的id取出thirdLevelCategoryMap分组中的三级分类list
                            List<MallGoodsCategory> tempGoodsCategories = thirdLevelCategoryMap.get(secondLevelCategory.getCategoryId());
                            secondLevelCategoryVO.setThirdLevelCategoryVOS((BeanUtil.copyList(tempGoodsCategories, ThirdLevelCategoryVO.class)));
                            secondLevelCategoryVOS.add(secondLevelCategoryVO);
                        }
                    }
                    //处理一级分类
                    if (!CollectionUtils.isEmpty(secondLevelCategoryVOS)) {
                        //根据 parentId 将 thirdLevelCategories 分组
                        Map<Long, List<SecondLevelCategoryVO>> secondLevelCategoryVOMap = secondLevelCategoryVOS.stream().collect(groupingBy(SecondLevelCategoryVO::getParentId));
                        for (MallGoodsCategory firstCategory : firstLevelCategories) {
                            MallIndexCategoryVO newBeeMallIndexCategoryVO = new MallIndexCategoryVO();
                            BeanUtil.copyProperties(firstCategory, newBeeMallIndexCategoryVO);
                            //如果该一级分类下有数据则放入 mallIndexCategoryVOS 对象中
                            if (secondLevelCategoryVOMap.containsKey(firstCategory.getCategoryId())) {
                                //根据一级分类的id取出secondLevelCategoryVOMap分组中的二级级分类list
                                List<SecondLevelCategoryVO> tempGoodsCategories = secondLevelCategoryVOMap.get(firstCategory.getCategoryId());
                                newBeeMallIndexCategoryVO.setSecondLevelCategoryVOS(tempGoodsCategories);
                                mallIndexCategoryVOS.add(newBeeMallIndexCategoryVO);
                            }
                        }
                    }
                }
            }
            return mallIndexCategoryVOS;
        } else {
            return null;
        }
    }

    @Override
    public SearchPageCategoryVO getCategoriesForSearch(Long categoryId) {
        SearchPageCategoryVO searchPageCategoryVO = new SearchPageCategoryVO();
        MallGoodsCategory thirdLevelGoodsCategory = mallGoodsCategoryMapper.selectByPrimaryKey(categoryId);
        if (thirdLevelGoodsCategory != null && thirdLevelGoodsCategory.getCategoryLevel() == MallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
            //获取当前三级分类的二级分类
            MallGoodsCategory secondLevelGoodsCategory = mallGoodsCategoryMapper.selectByPrimaryKey(thirdLevelGoodsCategory.getParentId());
            if (secondLevelGoodsCategory != null && secondLevelGoodsCategory.getCategoryLevel() == MallCategoryLevelEnum.LEVEL_TWO.getLevel()) {
                //获取当前二级分类下的三级分类List
                List<MallGoodsCategory> thirdLevelCategories = mallGoodsCategoryMapper.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelGoodsCategory.getCategoryId()), MallCategoryLevelEnum.LEVEL_THREE.getLevel(), Constants.SEARCH_CATEGORY_NUMBER);
                searchPageCategoryVO.setCurrentCategoryName(thirdLevelGoodsCategory.getCategoryName());
                searchPageCategoryVO.setSecondLevelCategoryName(secondLevelGoodsCategory.getCategoryName());
                searchPageCategoryVO.setThirdLevelCategoryList(thirdLevelCategories);
                return searchPageCategoryVO;
            }
        }
        return null;
    }
}
