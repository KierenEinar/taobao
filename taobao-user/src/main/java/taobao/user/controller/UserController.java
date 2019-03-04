package taobao.user.controller;

import taobao.core.model.APIResponse;
import taobao.user.model.User;
import taobao.user.service.UserReceiveConfigService;
import taobao.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET,value = "/api/v1/users/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        return ResponseEntity.ok(new APIResponse<>(userService.findOne(id)));
    }

    @RequestMapping(method = RequestMethod.POST,value = "/api/v1/users")
    public ResponseEntity<?> createOne (@RequestBody User user) {
        return ResponseEntity.ok(new APIResponse<>(userService.createOne(user)));
    }

    @RequestMapping(method = RequestMethod.GET,value = "/api/v1/users")
    public ResponseEntity<?> findByPhone(@RequestParam("phone") String phone) {
        return ResponseEntity.ok(new APIResponse<>(userService.findByMobile(phone)));
    }


}
