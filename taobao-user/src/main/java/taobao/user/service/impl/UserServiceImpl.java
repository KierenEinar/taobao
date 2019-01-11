package taobao.user.service.impl;

import taobao.user.entity.models.User;
import taobao.user.mapper.UserMapper;
import taobao.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserMapper userMapper;

    public void createOne(User user) {
        user.setCreateTime(new Date());
        int result = userMapper.insertOne(user);
        logger.info("insert result -> {}", result);
    }

    public User findOne(Long id) {
        return userMapper.selectOne(id);
    }
}
