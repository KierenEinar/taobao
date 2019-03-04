package taobao.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taobao.user.mapper.UserReceiveConfigMapper;
import taobao.user.model.UserReceiveConfig;
import taobao.user.service.UserReceiveConfigService;

import java.util.List;

@Service
public class UserReceiveConfigServiceImpl implements UserReceiveConfigService {

    @Autowired
    UserReceiveConfigMapper userReceiveConfigMapper;


    @Override
    public List<UserReceiveConfig> findAllByUserId(Long userId) {
        return userReceiveConfigMapper.findByUserId(userId);
    }

    @Override
    public Boolean createOne(UserReceiveConfig userReceiveConfig) {
        return userReceiveConfigMapper.insertSelective(userReceiveConfig) > 0;
    }
}
