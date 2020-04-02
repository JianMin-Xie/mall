package com.xjm.mall.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.xjm.mall.mapper.MallOrderItemMapper;
import com.xjm.mall.service.MallOrderItemService;
/**
@author Jm
@create 2020-04-02 15:48
*/  
@Service
public class MallOrderItemServiceImpl implements MallOrderItemService{

    @Resource
    private MallOrderItemMapper mallOrderItemMapper;

}
