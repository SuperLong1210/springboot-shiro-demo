package com.springbootshirodemo.service.impl;

import com.springbootshirodemo.dao.UserInfoDao;
import com.springbootshirodemo.entity.UserInfo;
import com.springbootshirodemo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现类
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    public UserInfoDao userInfoDao;

    @Override
    public UserInfo findByUserName(String userName) {
        return userInfoDao.findByUsername(userName);
    }
}
