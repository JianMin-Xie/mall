package com.xjm.mall.service;

import com.xjm.mall.domain.MallAdminUser;

/**
@author Jm
@create 2020-03-29 21:58
*/  
public interface MallAdminUserService {
        /**
         * 登录
         * @param userName
         * @param password
         * @return
         */
        public MallAdminUser login(String userName, String password);

        /**
         * 根据ID获取用户
         * @param loginUserId
         * @return
         */
        public MallAdminUser getUserDetailById(Integer loginUserId);

        /**
         * 更新密码
         * @param loginUserId
         * @param originalPassword
         * @param newPassword
         * @return
         */
        public Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword);

        /**
         * 更新名字
         * @param loginUserId
         * @param loginUserName
         * @param nickName
         * @return
         */
        public Boolean updateName(Integer loginUserId, String loginUserName, String nickName);
}
