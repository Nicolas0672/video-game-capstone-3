package org.yearup.data;

import org.yearup.models.OrderLineItem;
import org.yearup.models.ShoppingCart;

import java.util.List;

public interface OrderLineItemDao {
    List<OrderLineItem> create(ShoppingCart shoppingCart, int orderId);
}
