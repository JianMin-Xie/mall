package com.xjm.mall.service.impl;

import com.xjm.mall.domain.TbNewbeeMallAdminUser;
import com.xjm.mall.utils.MD5Util;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xjm.mall.mapper.TbNewbeeMallAdminUserMapper;
import com.xjm.mall.service.TbNewbeeMallAdminUserService;
/**
@author Jm
@create 2020-03-29 21:58
*/  
@Service
public class TbNewbeeMallAdminUserServiceImpl implements TbNewbeeMallAdminUserService{

    @Resource
    private TbNewbeeMallAdminUserMapper tbNewbeeMallAdminUserMapper;

    @Override
    public TbNewbeeMallAdminUser login(String userName, String password) {
        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        return tbNewbeeMallAdminUserMapper.login(userName,passwordMd5);
    }
}
