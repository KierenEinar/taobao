package com.taobao.user.service;

import com.taobao.user.entity.models.User;

public interface UserService {
    void createOne(User user);
    User findOne(Long id);
}
