package taobao.account.service;

import taobao.account.vo.AccountFreezeVo;
import taobao.account.vo.AccountTransferVo;
import taobao.account.vo.AccountWebVo;
import taobao.core.vo.OrderPayVo;

public interface AccountService {
    Boolean pay(AccountWebVo accountWebVo);
    Boolean transfer (AccountTransferVo accountTransferVo);
    Boolean transferFromUnionPay (AccountTransferVo accountTransferVo);
    Boolean createAccount (Long userId);
    Boolean freezeAccount (AccountFreezeVo accountFreezeVo);
    void refund(OrderPayVo orderPayVo);
}
