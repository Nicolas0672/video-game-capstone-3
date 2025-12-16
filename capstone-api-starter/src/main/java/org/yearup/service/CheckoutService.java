package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.exception.ShoppingCartNotFoundException;
import org.yearup.models.Order;
import org.yearup.models.ShoppingCart;

@Service
public class CheckoutService {
    private final OrderService orderService;
    private final OrderLineItemService orderLineItemService;
    private final ShoppingCartService shoppingCartService;

    public CheckoutService(OrderService orderService, OrderLineItemService orderLineItemService, ShoppingCartService shoppingCartService) {
        this.orderService = orderService;
        this.orderLineItemService = orderLineItemService;
        this.shoppingCartService = shoppingCartService;
    }

    public void checkout(int userId){
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);
        if(shoppingCart == null){
            throw new ShoppingCartNotFoundException("Cart not found with this userId: " + userId);
        }
        Order order = orderService.create(userId);
        orderLineItemService.create(shoppingCart, order.getOrderId());
        shoppingCartService.delete(userId);
    }
}
