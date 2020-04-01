package com.xjm.mall.interceptor;

import com.xjm.mall.common.Constants;
import com.xjm.mall.controller.vo.MallUserVO;
import com.xjm.mall.domain.MallShoppingCartItem;
import com.xjm.mall.mapper.MallShoppingCartItemMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * mall购物车数量处理
 *
 * @author Jm
 * @create 2020-04-01 23:08
 */
@Component
public class MallCartNumberInterceptor implements HandlerInterceptor {

    @Resource
    private MallShoppingCartItemMapper mallShoppingCartItemMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //购物车中的数量会更改，但是在这些接口中并没有对session中的数据做修改，这里统一处理一下
        if (null != request.getSession() && null != request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY)) {
            //如果当前为登陆状态，就查询数据库并设置购物车中的数量值
            MallUserVO mallUserVO = (MallUserVO) request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY);
            //设置购物车中的数量
            Example example = new Example(MallShoppingCartItem.class);
            example.createCriteria().andEqualTo("userId",mallUserVO.getUserId()).andEqualTo("isDeleted",0);
            int i = mallShoppingCartItemMapper.selectCountByExample(example);
            mallUserVO.setShopCartItemCount(i);
            request.getSession().setAttribute(Constants.MALL_USER_SESSION_KEY, mallUserVO);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
