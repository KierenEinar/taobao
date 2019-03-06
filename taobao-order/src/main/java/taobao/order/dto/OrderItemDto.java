package taobao.order.dto;

import taobao.order.model.Order;
import taobao.order.model.OrderDetail;

import java.util.List;

public class OrderItemDto {

    private Order order;
    private List<OrderDetail> details;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }
}
