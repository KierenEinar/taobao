package taobao.user.service;

import taobao.user.entity.models.User;

public interface UserService {
    void createOne(User user);
    User findOne(Long id);
}
