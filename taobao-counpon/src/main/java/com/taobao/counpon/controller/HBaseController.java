package com.taobao.counpon.controller;

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

    @RequestMapping(value = "/query")
    ResponseEntity<?> testHbaseGet (HModel model) {
        String value =  hbaseService.get(model);
        return ResponseEntity.ok(value);
    }

}
