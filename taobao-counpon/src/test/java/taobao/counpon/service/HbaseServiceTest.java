package taobao.counpon.service;

import com.google.common.collect.Lists;
import taobao.counpon.model.Counpon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import taobao.counpon.App;
import taobao.counpon.model.UsersCounpon;
import taobao.hbase.data.HbaseRepository;
import taobao.hbase.data.impl.SimpleHbaseRepositoryProxy;

import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class HbaseServiceTest {

    @Autowired
    HbaseRepository hbaseRepository;

    Logger logger = LoggerFactory.getLogger(HbaseServiceTest.class);

    @Test
    public void test () throws IOException {

        logger.info("bean -> {}", hbaseRepository);
        UsersCounpon usersCounpon = new UsersCounpon();
        usersCounpon.setUserId(1L);
        usersCounpon.setCouponId(1L);
        usersCounpon.setRowkey("1");
        usersCounpon.setProductIds(Lists.newArrayList("1","2"));

        hbaseRepository.upsert(usersCounpon);
    }


}
