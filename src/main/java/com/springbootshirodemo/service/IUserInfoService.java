package com.springbootshirodemo.service;

import com.springbootshirodemo.entity.UserInfo;

/**
 * 用户信息接口
 */
public interface IUserInfoService {

    /**
     * 根据用户名获取用户信息
     *
     * @param userName
     * @return
     */
    public UserInfo findByUserName(String userName);
}
