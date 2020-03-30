package com.xjm.mall.mapper;

import com.xjm.mall.domain.MallAdminUser;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

/**
@author Jm
@create 2020-03-29 21:58
*/
public interface MallAdminUserMapper extends MyMapper<MallAdminUser> {
    /**
     * 登陆方法
     *
     * @param userName
     * @param password
     * @return
     */
    MallAdminUser login(@Param("userName") String userName, @Param("password") String password);
}