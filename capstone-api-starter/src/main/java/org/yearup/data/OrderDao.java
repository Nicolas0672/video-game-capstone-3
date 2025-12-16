package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;

import java.sql.Date;

public interface OrderDao {
    Order create(Profile profile, Date date, double shippingAmount);

    interface OrderLineItemDao {
        OrderLineItem create(ShoppingCart shoppingCart, int orderId);
    }
}
