package taobao.account.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taobao.account.mapper.AccountMapper;
import taobao.account.mapper.AccountTradeLogMapper;
import taobao.account.model.Account;
import taobao.account.service.AccountService;
import taobao.account.vo.AccountTransferVo;
import taobao.account.vo.AccountWebVo;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    AccountTradeLogMapper accountTradeLogMapper;

    @Override
    @Transactional
    public Boolean pay(AccountWebVo accountWebVo) {




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
}
