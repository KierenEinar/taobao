package taobao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import taobao.account.service.AccountService;

@SpringBootTest(classes = App.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    Logger logger = LoggerFactory.getLogger(AccountServiceTest.class);

    @Test
    public void testCreateAccount () {
        boolean result = accountService.createAccount(309379499147919365L);
        logger.info("result -> {}", result);
    }


}
