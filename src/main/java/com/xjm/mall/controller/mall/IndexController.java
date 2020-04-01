package com.xjm.mall.controller.mall;

import com.xjm.mall.common.Constants;
import com.xjm.mall.controller.vo.MallIndexCarouselVO;
import com.xjm.mall.controller.vo.MallIndexCategoryVO;
import com.xjm.mall.controller.vo.MallIndexConfigGoodsVO;
import com.xjm.mall.enums.IndexConfigTypeEnum;
import com.xjm.mall.service.MallCarouselService;
import com.xjm.mall.service.MallGoodsCategoryService;
import com.xjm.mall.service.MallIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Jm
 * @create 2020-03-31 16:16
 */
@Controller
public class IndexController {

    @Resource
    private  MallCarouselService mallCarouselService;

    @Resource
    private MallGoodsCategoryService mallGoodsCategoryService;

    @Resource
    private MallIndexConfigService mallIndexConfigService;

    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<MallIndexCategoryVO> categories = mallGoodsCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error_5xx";
        }

        List<MallIndexCarouselVO> carousels = mallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<MallIndexConfigGoodsVO> hotGoodses = mallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<MallIndexConfigGoodsVO> newGoodses = mallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<MallIndexConfigGoodsVO> recommendGoodses = mallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        //分类数据
        request.setAttribute("categories", categories);
        //轮播图
        request.setAttribute("carousels", carousels);
        //热销商品
        request.setAttribute("hotGoodses", hotGoodses);
        //新品
        request.setAttribute("newGoodses", newGoodses);
        //推荐商品
        request.setAttribute("recommendGoodses", recommendGoodses);
        return "mall/index";
    }

}
