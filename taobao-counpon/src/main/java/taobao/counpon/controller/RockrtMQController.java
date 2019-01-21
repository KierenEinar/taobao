package taobao.counpon.controller;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(RockrtMQController.class);

    @RequestMapping("/mq/sync/concurrent/test")
    public ResponseEntity<?> send (@RequestParam String body) {
        SendResult sendResult = rocketMQTemplate.syncSend("myTopic","hello world");
        return ResponseEntity.ok(sendResult);
    }

    @RequestMapping("/mq/async/concurrent/test")
    public ResponseEntity<?> sendAsync (@RequestParam String body) {
        rocketMQTemplate.sendAsync("myTopic", "hello world", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("send success -> {}", sendResult);
            }
            @Override
            public void onException(Throwable throwable) {
                logger.error("send failed -> {}", throwable.getMessage());
            }
        });
        return ResponseEntity.ok("success");
    }

    @RequestMapping("/mq/tran/test")
    public ResponseEntity<?> sendTransaction (@RequestParam String body) {
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(Constant.ROCKETMQ_TRANSACTION_DEFAULT_GLOBAL_NAME,
                "tran_topic", "hello world", null);
        return ResponseEntity.ok(transactionSendResult);
    }

    @RequestMapping("/mq/sync/orderly/test")
    public ResponseEntity<?> syncSendOrderly (@RequestParam String body) {
        SendResult sendResult = rocketMQTemplate.syncSendOrderly("myTopic", "hello world", "productId1");
        return ResponseEntity.ok(sendResult);
    }

    @RequestMapping("/mq/async/orderly/test")
    public ResponseEntity<?> asyncSendOrderly (@RequestParam String body) {
        rocketMQTemplate.sendAsyncOrderly("myTopic", "hello world", "productId1", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("send success -> {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("send failed -> {}", throwable.getMessage());
            }
        });
        return ResponseEntity.ok("success");
    }
}
