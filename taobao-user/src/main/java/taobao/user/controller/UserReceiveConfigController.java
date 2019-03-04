package taobao.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taobao.core.model.APIResponse;
import taobao.user.model.UserReceiveConfig;
import taobao.user.service.UserReceiveConfigService;

@RestController
@RequestMapping("/api/v1/user-receive-config")
public class UserReceiveConfigController {

    @Autowired
    UserReceiveConfigService userReceiveConfigService;

    @RequestMapping(method = RequestMethod.GET,value = "/{userId}")
    public ResponseEntity<?> findByPhone(@PathVariable Long userId) {
        return ResponseEntity.ok(new APIResponse<>(userReceiveConfigService.findAllByUserId(userId)));
    }

    @RequestMapping(method = RequestMethod.POST,value = "")
    public ResponseEntity<?> findByPhone(@RequestBody UserReceiveConfig userReceiveConfig) {
        return ResponseEntity.ok(new APIResponse<>(userReceiveConfigService.createOne(userReceiveConfig)));
    }

}
