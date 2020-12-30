package com.zkw.springboot.dao;

import com.zkw.springboot.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    /**
     * 根据账号删除用户信息
     * @param account
     * @return
     */
    int deleteByPrimaryKey(String account);

    /**
     * 插入用户
     * @param record
     * @return
     */
    int insert(User record);

    /**
     * 插入用户，已经存在则更新
     * @param record
     * @return
     */
    int insertSelective(User record);

    /**
     * 根据用户账号查找用户
     * @param account
     * @return
     */
    User selectByPrimaryKey(String account);

    /**
     * 根据用户账号更新用户，如果字段是null则不更新
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * 根据用户账号更新用户
     * @param record
     * @return
     */
    int updateByPrimaryKey(User record);
}