package taobao.account.producer;

import taobao.core.vo.OrderPayVo;

public interface ProducerService {
    void notifyPaySuccess(OrderPayVo orderPayVo);
}
