package com.xjm.mall.controller.admin;

import com.xjm.mall.common.Constants;
import com.xjm.mall.domain.MallGoodsCategory;
import com.xjm.mall.domain.MallGoodsInfo;
import com.xjm.mall.enums.MallCategoryLevelEnum;
import com.xjm.mall.enums.ServiceResultEnum;
import com.xjm.mall.service.MallGoodsCategoryService;
import com.xjm.mall.service.MallGoodsInfoService;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.Result;
import com.xjm.mall.utils.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Jm
 * @create 2020-03-31 14:20
 */
@Controller
@RequestMapping("/admin")
public class MallGoodsController {

    @Resource
    private MallGoodsCategoryService mallGoodsCategoryService;

    @Resource
    private MallGoodsInfoService mallGoodsInfoService;

    @GetMapping("/goods")
    public String goodsPage(HttpServletRequest request) {
        request.setAttribute("path", "mall_goods");
        return "admin/mall_goods";
    }

    @GetMapping("/goods/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        //查询所有的一级分类
        List<MallGoodsCategory> firstLevelCategories = mallGoodsCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), MallCategoryLevelEnum.LEVEL_ONE.getLevel());
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            //查询一级分类列表中第一个实体的所有二级分类
            List<MallGoodsCategory> secondLevelCategories = mallGoodsCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), MallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<MallGoodsCategory> thirdLevelCategories = mallGoodsCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), MallCategoryLevelEnum.LEVEL_THREE.getLevel());
                request.setAttribute("firstLevelCategories", firstLevelCategories);
                request.setAttribute("secondLevelCategories", secondLevelCategories);
                request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                request.setAttribute("path", "goods-edit");
                return "admin/mall_goods_edit";
            }
        }
        return "error/error_5xx";
    }

    @GetMapping("/goods/edit/{goodsId}")
    public String edit(HttpServletRequest request, @PathVariable("goodsId") Long goodsId) {
        request.setAttribute("path", "edit");
        MallGoodsInfo newBeeMallGoods = mallGoodsInfoService.getMallGoodsById(goodsId);
        if (newBeeMallGoods == null) {
            return "error/error_400";
        }
        if (newBeeMallGoods.getGoodsCategoryId() > 0) {
            if (newBeeMallGoods.getGoodsCategoryId() != null || newBeeMallGoods.getGoodsCategoryId() > 0) {
                //有分类字段则查询相关分类数据返回给前端以供分类的三级联动显示
                MallGoodsCategory currentGoodsCategory = mallGoodsCategoryService.getGoodsCategoryById(newBeeMallGoods.getGoodsCategoryId());
                //商品表中存储的分类id字段为三级分类的id，不为三级分类则是错误数据
                if (currentGoodsCategory != null && currentGoodsCategory.getCategoryLevel() == MallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
                    //查询所有的一级分类
                    List<MallGoodsCategory> firstLevelCategories = mallGoodsCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), MallCategoryLevelEnum.LEVEL_ONE.getLevel());
                    //根据parentId查询当前parentId下所有的三级分类
                    List<MallGoodsCategory> thirdLevelCategories = mallGoodsCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(currentGoodsCategory.getParentId()), MallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    //查询当前三级分类的父级二级分类
                    MallGoodsCategory secondCategory = mallGoodsCategoryService.getGoodsCategoryById(currentGoodsCategory.getParentId());
                    if (secondCategory != null) {
                        //根据parentId查询当前parentId下所有的二级分类
                        List<MallGoodsCategory> secondLevelCategories = mallGoodsCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondCategory.getParentId()), MallCategoryLevelEnum.LEVEL_TWO.getLevel());
                        //查询当前二级分类的父级一级分类
                        MallGoodsCategory firestCategory = mallGoodsCategoryService.getGoodsCategoryById(secondCategory.getParentId());
                        if (firestCategory != null) {
                            //所有分类数据都得到之后放到request对象中供前端读取
                            request.setAttribute("firstLevelCategories", firstLevelCategories);
                            request.setAttribute("secondLevelCategories", secondLevelCategories);
                            request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                            request.setAttribute("firstLevelCategoryId", firestCategory.getCategoryId());
                            request.setAttribute("secondLevelCategoryId", secondCategory.getCategoryId());
                            request.setAttribute("thirdLevelCategoryId", currentGoodsCategory.getCategoryId());
                        }
                    }
                }
            }
        }
        if (newBeeMallGoods.getGoodsCategoryId() == 0) {
            //查询所有的一级分类
            List<MallGoodsCategory> firstLevelCategories = mallGoodsCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), MallCategoryLevelEnum.LEVEL_ONE.getLevel());
            if (!CollectionUtils.isEmpty(firstLevelCategories)) {
                //查询一级分类列表中第一个实体的所有二级分类
                List<MallGoodsCategory> secondLevelCategories = mallGoodsCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), MallCategoryLevelEnum.LEVEL_TWO.getLevel());
                if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                    //查询二级分类列表中第一个实体的所有三级分类
                    List<MallGoodsCategory> thirdLevelCategories = mallGoodsCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), MallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    request.setAttribute("firstLevelCategories", firstLevelCategories);
                    request.setAttribute("secondLevelCategories", secondLevelCategories);
                    request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                }
            }
        }
        request.setAttribute("goods", newBeeMallGoods);
        request.setAttribute("path", "goods-edit");
        return "admin/mall_goods_edit";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(mallGoodsInfoService.getMallGoodsPage(pageUtil));
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/goods/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody MallGoodsInfo mallGoodsInfo) {
        if (StringUtils.isEmpty(mallGoodsInfo.getGoodsName())
                || StringUtils.isEmpty(mallGoodsInfo.getGoodsIntro())
                || StringUtils.isEmpty(mallGoodsInfo.getTag())
                || Objects.isNull(mallGoodsInfo.getOriginalPrice())
                || Objects.isNull(mallGoodsInfo.getGoodsCategoryId())
                || Objects.isNull(mallGoodsInfo.getSellingPrice())
                || Objects.isNull(mallGoodsInfo.getStockNum())
                || Objects.isNull(mallGoodsInfo.getGoodsSellStatus())
                || StringUtils.isEmpty(mallGoodsInfo.getGoodsCoverImg())
                || StringUtils.isEmpty(mallGoodsInfo.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = mallGoodsInfoService.saveMallGoods(mallGoodsInfo);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/goods/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody MallGoodsInfo mallGoodsInfo) {
        if (Objects.isNull(mallGoodsInfo.getGoodsId())
                || StringUtils.isEmpty(mallGoodsInfo.getGoodsName())
                || StringUtils.isEmpty(mallGoodsInfo.getGoodsIntro())
                || StringUtils.isEmpty(mallGoodsInfo.getTag())
                || Objects.isNull(mallGoodsInfo.getOriginalPrice())
                || Objects.isNull(mallGoodsInfo.getSellingPrice())
                || Objects.isNull(mallGoodsInfo.getGoodsCategoryId())
                || Objects.isNull(mallGoodsInfo.getStockNum())
                || Objects.isNull(mallGoodsInfo.getGoodsSellStatus())
                || StringUtils.isEmpty(mallGoodsInfo.getGoodsCoverImg())
                || StringUtils.isEmpty(mallGoodsInfo.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = mallGoodsInfoService.updateMallGoods(mallGoodsInfo);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/goods/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        MallGoodsInfo goods = mallGoodsInfoService.getMallGoodsById(id);
        if (goods == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(goods);
    }

    /**
     * 批量修改销售状态
     */
    @RequestMapping(value = "/goods/status/{sellStatus}", method = RequestMethod.PUT)
    @ResponseBody
    public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") int sellStatus) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
            return ResultGenerator.genFailResult("状态异常！");
        }
        if (mallGoodsInfoService.batchUpdateSellStatus(ids, sellStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }
}
