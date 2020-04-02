package com.xjm.mall.service.impl;

import com.xjm.mall.common.Constants;
import com.xjm.mall.controller.vo.MallUserVO;
import com.xjm.mall.domain.MallUser;
import com.xjm.mall.enums.ServiceResultEnum;
import com.xjm.mall.utils.BeanUtil;
import com.xjm.mall.utils.MD5Util;
import com.xjm.mall.utils.PageQueryUtil;
import com.xjm.mall.utils.PageResult;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.xjm.mall.mapper.MallUserMapper;
import com.xjm.mall.service.MallUserService;
import tk.mybatis.mapper.entity.Example;

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

    @Override
    public String register(String loginName, String password) {
        Example example = new Example(MallUser.class);
        example.createCriteria().andEqualTo("loginName",loginName).andEqualTo("isDeleted", 0);
        if (mallUserMapper.selectOneByExample(example) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        MallUser registerUser = new MallUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        if (mallUserMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5, HttpSession httpSession) {
        Example example = new Example(MallUser.class);
        example.createCriteria().andEqualTo("loginName",loginName).andEqualTo("isDeleted", 0).andEqualTo("passwordMd5",passwordMD5);
        MallUser user = mallUserMapper.selectOneByExample(example);

        if (user != null && httpSession != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
            }
            //昵称太长 影响页面展示
            if (user.getNickName() != null && user.getNickName().length() > 7) {
                String tempNickName = user.getNickName().substring(0, 7) + "..";
                user.setNickName(tempNickName);
            }
            MallUserVO mallUserVO = new MallUserVO();
            BeanUtil.copyProperties(user, mallUserVO);
            //设置购物车中的数量
            httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, mallUserVO);
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public MallUserVO updateUserInfo(MallUser mallUser, HttpSession httpSession) {
        MallUser user = mallUserMapper.selectByPrimaryKey(mallUser.getUserId());
        if (user != null) {
            user.setNickName(mallUser.getNickName());
            user.setAddress(mallUser.getAddress());
            user.setIntroduceSign(mallUser.getIntroduceSign());
            if (mallUserMapper.updateByPrimaryKeySelective(user) > 0) {
                MallUserVO mallUserVO = new MallUserVO();
                user = mallUserMapper.selectByPrimaryKey(mallUser.getUserId());
                BeanUtil.copyProperties(user, mallUserVO);
                httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, mallUserVO);
                return mallUserVO;
            }
        }
        return null;
    }
}
