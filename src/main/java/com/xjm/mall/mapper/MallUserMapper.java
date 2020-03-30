package com.xjm.mall.mapper;

import com.xjm.mall.domain.MallUser;
import com.xjm.mall.utils.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.MyMapper;

import java.util.List;

/**
@author Jm
@create 2020-03-30 15:03
*/  
public interface MallUserMapper extends MyMapper<MallUser> {
    /**
     * 根据分页参数查找会员集合
     * @param pageUtil
     * @return
     */
    List<MallUser> findMallUserList(PageQueryUtil pageUtil);

    /**
     * 根据分页参数查找会员总数
     * @param pageUtil
     * @return
     */
    int getTotalMallUsers(PageQueryUtil pageUtil);

    /**
     * 锁定用户批次
     * @param ids
     * @param lockStatus
     * @return
     */
    int lockUserBatch(@Param("ids") Integer[] ids, @Param("lockStatus") int lockStatus);
}