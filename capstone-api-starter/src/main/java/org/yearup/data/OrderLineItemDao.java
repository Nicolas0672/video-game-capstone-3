package org.yearup.data;

import org.yearup.models.OrderLineItem;
import org.yearup.models.ShoppingCart;

public interface OrderLineItemDao {
    OrderLineItem create(ShoppingCart shoppingCart, int orderId);
}
