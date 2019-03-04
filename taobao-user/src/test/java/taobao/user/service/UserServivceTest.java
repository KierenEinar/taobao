package taobao.user.service;

import taobao.user.App;
import taobao.user.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class UserServivceTest {

    @Autowired
    UserService userService;

    @Test
    public void testInsert () {
        User user = new User();
        user.setCreateTime(new Date());
        user.setLastName("宏斌");
        user.setFirstName("江");
        user.setUpdateTime(new Date());
        user.setGender("男");
        user.setMemberId(1L);
        user.setCreditId(1L);
        user.setId(2L);
        userService.createOne(user);
    }

}
