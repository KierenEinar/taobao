package taobao.counpon.controller;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import taobao.rocketmq.annotation.Constant;
import taobao.rocketmq.core.RocketMQTemplate;

@RestController
public class RockrtMQController {

    @Autowired
    RocketMQTemplate rocketMQTemplate;

    @RequestMapping("/mq/normal/test")
    public ResponseEntity<?> send (@RequestParam String body) {
        SendResult sendResult = rocketMQTemplate.syncSend("myTopic","hello world");
        return ResponseEntity.ok(sendResult);
    }


    @RequestMapping("/mq/tran/test")
    public ResponseEntity<?> sendTransaction (@RequestParam String body) {
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(Constant.ROCKETMQ_TRANSACTION_DEFAULT_GLOBAL_NAME,
                "tran_topic", "hello world", null);
        return ResponseEntity.ok(transactionSendResult);
    }

}
