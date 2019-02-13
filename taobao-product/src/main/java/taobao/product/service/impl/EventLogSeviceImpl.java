package taobao.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taobao.product.mapper.EventLogMapper;
import taobao.product.models.EventLog;
import taobao.product.service.EventLogSevice;
@Service
public class EventLogSeviceImpl implements EventLogSevice {

    @Autowired
    EventLogMapper eventLogMapper;

    @Override
    @Transactional
    public void create(EventLog eventLog) {
        eventLogMapper.insertSelective(eventLog);
    }

}
