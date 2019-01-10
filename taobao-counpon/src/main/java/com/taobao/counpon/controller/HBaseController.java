package com.taobao.counpon.controller;

import com.taobao.core.model.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.taobao.counpon.model.HModel;
import com.taobao.counpon.service.HbaseService;

@RestController
@RequestMapping("/hbase")
public class HBaseController {

    @Autowired
    HbaseService hbaseService;

    Logger logger = LoggerFactory.getLogger(HBaseController.class);

    @RequestMapping(value = "/query")
    ResponseEntity<?> testHbaseGet (HModel model) {
        logger.info("HModel -> {}", model);
        String value =  hbaseService.get(model);
        logger.info("value -> {}", value);
        return ResponseEntity.ok(new APIResponse<String>(0, value));
    }

}
