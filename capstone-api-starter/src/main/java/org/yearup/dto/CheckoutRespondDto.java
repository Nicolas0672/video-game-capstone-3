package org.yearup.dto;

import org.springframework.stereotype.Component;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;

import java.util.List;

public class CheckoutRespondDto {
    private Order order;
    List<OrderLineItem> orderLineItemList;

    public CheckoutRespondDto(Order order, List<OrderLineItem> orderLineItemList) {
        this.order = order;
        this.orderLineItemList = orderLineItemList;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderLineItem> getOrderLineItemList() {
        return orderLineItemList;
    }

    public void setOrderLineItemList(List<OrderLineItem> orderLineItemList) {
        this.orderLineItemList = orderLineItemList;
    }
}
