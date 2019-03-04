package taobao.user.service;

import taobao.user.model.User;

public interface UserService {
    Boolean createOne(User user);
    User findOne(Long id);
    User findByMobile (String mobile);
}
