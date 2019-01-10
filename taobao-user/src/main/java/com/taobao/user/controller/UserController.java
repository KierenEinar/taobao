package com.taobao.user.controller;

import com.taobao.user.entity.models.User;
import com.taobao.user.service.UserService;
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
        return ResponseEntity.ok(userService.findOne(id));
    }

    @RequestMapping(method = RequestMethod.POST,value = "/api/v1/users")
    public ResponseEntity<?> createOne (@RequestBody User user) {
        userService.createOne(user);
        return ResponseEntity.ok("success");
    }


}
