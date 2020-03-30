package com.xjm.mall.service;

import com.xjm.mall.controller.vo.MallUserVO;
import com.xjm.mall.domain.MallUser;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;

import javax.servlet.http.HttpSession;

/**
@author Jm
@create 2020-03-30 15:03
*/  
public interface MallUserService {
        /**
         * 后台分页
         *
         * @param pageUtil
         * @return
         */
        PageResult getNewBeeMallUsersPage(PageQueryUtil pageUtil);

        /**
         * 用户禁用与解除禁用(0-未锁定 1-已锁定)
         *
         * @param ids
         * @param lockStatus
         * @return
         */
        Boolean lockUsers(Integer[] ids, int lockStatus);

}
