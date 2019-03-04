package taobao.user.service;

import taobao.user.model.UserReceiveConfig;

import java.util.List;

public interface UserReceiveConfigService {
    List<UserReceiveConfig> findAllByUserId(Long userId);
    Boolean createOne(UserReceiveConfig userReceiveConfig);
}
