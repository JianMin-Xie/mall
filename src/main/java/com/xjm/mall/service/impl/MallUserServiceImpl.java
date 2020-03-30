package com.xjm.mall.service.impl;

import com.xjm.mall.controller.vo.MallUserVO;
import com.xjm.mall.domain.MallUser;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.xjm.mall.mapper.MallUserMapper;
import com.xjm.mall.service.MallUserService;

import java.util.List;

/**
@author Jm
@create 2020-03-30 15:03
*/  
@Service
public class MallUserServiceImpl implements MallUserService {

    @Resource
    private MallUserMapper mallUserMapper;


    @Override
    public PageResult getNewBeeMallUsersPage(PageQueryUtil pageUtil) {
        List<MallUser> mallUserList = mallUserMapper.findMallUserList(pageUtil);
        int totalMallUsers = mallUserMapper.getTotalMallUsers(pageUtil);
        PageResult pageResult = new PageResult(mallUserList, totalMallUsers, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }


    @Override
    public Boolean lockUsers(Integer[] ids, int lockStatus) {
        if (ids.length < 1) {
            return false;
        }
        return mallUserMapper.lockUserBatch(ids, lockStatus) > 0;
    }
}
