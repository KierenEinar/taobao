package taobao.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import taobao.App;
import taobao.model.HModel;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class HbaseServiceTest {

    @Autowired
    HbaseService hbaseService;

    Logger logger = LoggerFactory.getLogger(HbaseServiceTest.class);

    @Test
    public void testGet () {
        HModel hModel = new HModel();
        hModel.setRowKey("1");
        hModel.setTableName("users");
        hModel.setFamilyColumn("baseInfo");
        hModel.setQualify("name");
        String value = hbaseService.get(hModel);
        logger.info("value -> {}", value);
    }

}
