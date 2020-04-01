package com.xjm.mall.controller.vo;


import com.xjm.mall.domain.MallGoodsCategory;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索页面分类数据VO
 * @author Jm
 */
public class SearchPageCategoryVO implements Serializable {

    private String firstLevelCategoryName;

    private List<MallGoodsCategory> secondLevelCategoryList;

    private String secondLevelCategoryName;

    private List<MallGoodsCategory> thirdLevelCategoryList;

    private String currentCategoryName;

    public String getFirstLevelCategoryName() {
        return firstLevelCategoryName;
    }

    public void setFirstLevelCategoryName(String firstLevelCategoryName) {
        this.firstLevelCategoryName = firstLevelCategoryName;
    }

    public List<MallGoodsCategory> getSecondLevelCategoryList() {
        return secondLevelCategoryList;
    }

    public void setSecondLevelCategoryList(List<MallGoodsCategory> secondLevelCategoryList) {
        this.secondLevelCategoryList = secondLevelCategoryList;
    }

    public String getSecondLevelCategoryName() {
        return secondLevelCategoryName;
    }

    public void setSecondLevelCategoryName(String secondLevelCategoryName) {
        this.secondLevelCategoryName = secondLevelCategoryName;
    }

    public List<MallGoodsCategory> getThirdLevelCategoryList() {
        return thirdLevelCategoryList;
    }

    public void setThirdLevelCategoryList(List<MallGoodsCategory> thirdLevelCategoryList) {
        this.thirdLevelCategoryList = thirdLevelCategoryList;
    }

    public String getCurrentCategoryName() {
        return currentCategoryName;
    }

    public void setCurrentCategoryName(String currentCategoryName) {
        this.currentCategoryName = currentCategoryName;
    }
}
