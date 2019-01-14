package taobao.counpon.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import taobao.core.model.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taobao.counpon.model.UsersCounpon;
import taobao.hbase.data.HbaseRepository;

@RestController
@RequestMapping("/hbase")
public class HBaseController {

    @Autowired
    HbaseRepository<UsersCounpon> hbaseRepository;

    Logger logger = LoggerFactory.getLogger(HBaseController.class);

    @RequestMapping(value = "/put")
    ResponseEntity<?> testHbasePut (@RequestBody UsersCounpon usersCounpon) {
        logger.info("HModel -> {}", usersCounpon);
        Boolean value =  hbaseRepository.upsert(usersCounpon);
        logger.info("value -> {}", value);
        return ResponseEntity.ok(new APIResponse<>(0, value));
    }

    @RequestMapping(value = "/usercounpons/{rowkey}")
    ResponseEntity<?> testHbaseGet (@PathVariable("rowkey") String rowkey) {
        UsersCounpon usersCounpon = hbaseRepository.findOne(rowkey);
        logger.info("usersCounpon -> {}", usersCounpon);
        return ResponseEntity.ok(new APIResponse<>(0, usersCounpon));
    }


}
