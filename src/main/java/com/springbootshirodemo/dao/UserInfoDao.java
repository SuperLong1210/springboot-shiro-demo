package com.springbootshirodemo.dao;

import com.springbootshirodemo.entity.UserInfo;
import org.springframework.data.repository.CrudRepository;

public interface UserInfoDao extends CrudRepository<UserInfo, Long> {

    /**
     * 通过 userName 查找用户信息
     *
     * @param userName
     * @return
     */
    public UserInfo findByUsername(String userName);
}
