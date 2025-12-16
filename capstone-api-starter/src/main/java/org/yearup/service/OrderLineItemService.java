package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.data.OrderLineItemDao;
import org.yearup.data.mysql.MySqlOrderLineItemDao;
import org.yearup.models.OrderLineItem;
import org.yearup.models.ShoppingCart;

@Service
public class OrderLineItemService {
    private final OrderLineItemDao orderLineItemDao;

    public OrderLineItemService(MySqlOrderLineItemDao orderLineItemDao) {
        this.orderLineItemDao = orderLineItemDao;
    }

    public OrderLineItem create(ShoppingCart shoppingCart, int orderId){
        return orderLineItemDao.create(shoppingCart, orderId);
    }
}
