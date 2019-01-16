package taobao.counpon.controller;

import org.springframework.web.bind.annotation.*;
import taobao.core.model.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import taobao.counpon.model.UsersCounpon;
import taobao.hbase.data.HbaseRepository;

@RestController
@RequestMapping("/hbase")
public class HBaseController {

    @Autowired
    HbaseRepository<UsersCounpon> hbaseRepository;

    Logger logger = LoggerFactory.getLogger(HBaseController.class);

    @RequestMapping(value = "/usercounpons", method = RequestMethod.POST)
    ResponseEntity<?> testHbasePut (@RequestBody UsersCounpon usersCounpon) {
        logger.info("HModel -> {}", usersCounpon);
        Boolean value =  hbaseRepository.insert(usersCounpon);
        logger.info("value -> {}", value);
        return ResponseEntity.ok(new APIResponse<>(0, value));
    }

    @RequestMapping(value = "/usercounpons/{rowkey}", method = RequestMethod.GET)
    ResponseEntity<?> testHbaseGet (@PathVariable("rowkey") String rowkey) {
        UsersCounpon usersCounpon = hbaseRepository.findOne(rowkey, UsersCounpon.class);
        logger.info("usersCounpon -> {}", usersCounpon);
        return ResponseEntity.ok(new APIResponse<>(0, usersCounpon));
    }


}
