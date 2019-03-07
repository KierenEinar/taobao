package taobao.user.service.impl;

import io.shardingsphere.core.keygen.KeyGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import taobao.user.model.User;
import taobao.user.mapper.UserMapper;
import taobao.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    UserMapper userMapper;

    @Autowired
    KeyGenerator keyGenerator;

    @Value("${sharding.jdbc.datasource.names}")
    String dataSources;


    @Override
    @Transactional
    public Boolean createOne(User user) {
        Long userId = keyGenerator.generateKey().longValue();
        int length = dataSources.split(",").length;
        int index = user.getPhone().hashCode() % length;
        index = index > 0 ? index : (0-index);
        userId = (index - userId % length) + userId ;
        user.setId(userId);
        user.setUserId(userId);
        user.setCreateTime(new Date());
        int result = userMapper.insertOne(user);
        logger.info("insert result -> {}", result);
        return result > 0;
    }

    public User findOne(Long id) {
        return userMapper.selectOne(id);
    }

    @Override
    public User findByMobile(String mobile) {
        User user = userMapper.selectByPhone(mobile);
        return user;
    }
}
