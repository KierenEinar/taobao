package taobao.rocketmq.config;

import taobao.rocketmq.core.RocketMQTemplate;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionHandlerRegistry {

    private RocketMQTemplate rocketMQTemplate;

    private Set<String> listenerContainers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public TransactionHandlerRegistry (RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    public void register(TransactionHandler transactionHandler) {
        if (listenerContainers.contains(transactionHandler.getName()))
            throw new IllegalArgumentException(String.format("The transaction name [%s] has been defined in TransactionListener [%s]",
                    transactionHandler.getName(), transactionHandler.getBeanName()));
        listenerContainers.add(transactionHandler.getName());
        rocketMQTemplate.createAndStartMQTransactionProducer(transactionHandler.getName(),
                transactionHandler.getBean(), transactionHandler.getThreadPoolExecutor());
    }


}
