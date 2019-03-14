package taobao.account.service.impl;

import io.shardingsphere.core.keygen.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taobao.account.exception.AccountException;
import taobao.account.mapper.AccountMapper;
import taobao.account.mapper.AccountTradeLogMapper;
import taobao.account.model.Account;
import taobao.account.model.AccountTradeLog;
import taobao.account.producer.ProducerService;
import taobao.account.service.AccountService;
import taobao.account.vo.AccountFreezeVo;
import taobao.account.vo.AccountTransferVo;
import taobao.account.vo.AccountWebVo;
import taobao.core.vo.OrderPayVo;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    AccountTradeLogMapper accountTradeLogMapper;

    @Autowired
    ProducerService producerService;

    @Autowired
    KeyGenerator keyGenerator;

    Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Override
    @Transactional
    public Boolean pay(AccountWebVo accountWebVo) {
        return Boolean.FALSE;
    }

    @Override
    @Transactional
    public Boolean transfer(AccountTransferVo accountTransferVo) {
        return null;
    }

    @Override
    @Transactional
    public Boolean transferFromUnionPay(AccountTransferVo accountTransferVo) {
        //模拟调接口
        //分布式消息最终一致性
        return Boolean.TRUE;
    }

    @Override
    public Boolean createAccount(Long userId) {
        Account account = new Account();
        account.setUserId(userId);
        int result = accountMapper.insertSelective(account);
        return result > 0;
    }

    @Override
    @Transactional
    public Boolean freezeAccount(AccountFreezeVo accountFreezeVo) {
        int result = accountMapper.updateDecrBalanceAndIncrLockBalance(accountFreezeVo.getUserId(), accountFreezeVo.getAmount());
        if (result == 0) throw new AccountException("账户余额不足");
        AccountTradeLog accountTradeLog = accountFreezeVo.buildAccountTradeLog();
        accountTradeLog.setId(keyGenerator.generateKey().longValue());
        //幂等性校验(order_id + user_id + status)
        result = accountTradeLogMapper.insertSelective(accountTradeLog);
        if (result == 0) throw new AccountException("系统繁忙, 稍后重试");

        OrderPayVo orderPayVo = new OrderPayVo();
        orderPayVo.setUserId(accountFreezeVo.getUserId());
        orderPayVo.setOrderId(accountFreezeVo.getOrderId()+"");
        orderPayVo.setTradeNo(accountTradeLog.getId());

        producerService.notifyPaySuccess(orderPayVo);
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public void refund(OrderPayVo orderPayVo) {
        AccountTradeLog accountTradeLog = copyToRefund(orderPayVo);
        if (null != accountTradeLogMapper.selectByOrderIdAndUserIdAndStatus(orderPayVo.getOrderId(), orderPayVo.getUserId(), AccountTradeLog.Status.refund)) {
            logger.info("重复调用退款接口, orderPayVo -> {}", orderPayVo);
            return;
        }
        int result = accountTradeLogMapper.insertSelective(accountTradeLog);
        if (result == 0) throw new AccountException("系统繁忙, 稍后重试");
        result = accountMapper.updateDecrBalanceAndIncrLockBalance(orderPayVo.getUserId(), accountTradeLog.getBalance());
        if (result == 0) throw new AccountException("账户余额不足");
    }

    private AccountTradeLog copyToRefund(OrderPayVo orderPayVo ) {
        AccountTradeLog accountTradeLog = accountTradeLogMapper.selectByIdAndUserId(orderPayVo);
        BigDecimal amount = new BigDecimal(0).subtract(accountTradeLog.getBalance());
        accountTradeLog.setId(keyGenerator.generateKey().longValue());
        Long from = accountTradeLog.getFromAccountId();
        Long to = accountTradeLog.getToAccountId();
        accountTradeLog.setRemark("refund");
        accountTradeLog.setStatus(AccountTradeLog.Status.refund);
        accountTradeLog.setToAccountId(from);
        accountTradeLog.setFromAccountId(to);
        accountTradeLog.setBalance(amount);
        accountTradeLog.setCreateTime(new Date());
        return accountTradeLog;
    }


}
