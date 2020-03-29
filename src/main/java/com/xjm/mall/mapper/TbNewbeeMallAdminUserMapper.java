package com.xjm.mall.mapper;

import com.xjm.mall.domain.TbNewbeeMallAdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.MyMapper;

/**
@author Jm
@create 2020-03-29 21:58
*/
public interface TbNewbeeMallAdminUserMapper extends MyMapper<TbNewbeeMallAdminUser> {
    /**
     * 登陆方法
     *
     * @param userName
     * @param password
     * @return
     */
    TbNewbeeMallAdminUser login(@Param("userName") String userName, @Param("password") String password);
}