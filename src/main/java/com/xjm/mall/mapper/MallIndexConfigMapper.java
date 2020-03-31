package com.xjm.mall.mapper;

import com.xjm.mall.domain.MallIndexConfig;
import com.xjm.mall.utils.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

/**
@author Jm
@create 2020-03-31 20:04
*/  
public interface MallIndexConfigMapper extends MyMapper<MallIndexConfig> {
    List<MallIndexConfig> findIndexConfigList(PageQueryUtil pageUtil);

    int getTotalIndexConfigs(PageQueryUtil pageUtil);

    int deleteBatch(Long[] ids);

    List<MallIndexConfig> findIndexConfigsByTypeAndNum(@Param("configType") int configType, @Param("number") int number);
}