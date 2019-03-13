package taobao.account.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import taobao.account.service.AccountService;
import taobao.account.vo.AccountFreezeVo;
import taobao.core.model.APIResponse;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/freeze", method = RequestMethod.POST)
    APIResponse<Boolean> freezeAccount (@RequestBody AccountFreezeVo accountFreezeVo) {
        return new APIResponse<>(accountService.freezeAccount(accountFreezeVo));
    }

}
