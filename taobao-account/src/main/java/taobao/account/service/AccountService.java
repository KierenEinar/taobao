package taobao.account.service;

import taobao.account.vo.AccountTransferVo;
import taobao.account.vo.AccountWebVo;

public interface AccountService {
    Boolean pay(AccountWebVo accountWebVo);
    Boolean transfer (AccountTransferVo accountTransferVo);
    Boolean transferFromUnionPay (AccountTransferVo accountTransferVo);
    Boolean createAccount (Long userId);
}
