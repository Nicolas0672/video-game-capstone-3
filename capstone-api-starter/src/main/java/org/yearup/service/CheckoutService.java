package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.dto.CheckoutRespondDto;
import org.yearup.exception.EmptyCartException;
import org.yearup.exception.ShoppingCartNotFoundException;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;
import org.yearup.models.ShoppingCart;

import java.util.List;

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

    // Process checkout for a user
    // 1. Retrieve shopping cart
    // 2. Ensure cart is not empty
    // 3. Create order and line items
    // 4. Clear the shopping cart
    public CheckoutRespondDto checkout(int userId){
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);

        if(shoppingCart.getItems().isEmpty()){
            throw new EmptyCartException("Cart is empty: " + userId);
        }

        Order order = orderService.create(userId); // create new order
        List<OrderLineItem> orderLineItemList = orderLineItemService.create(shoppingCart, order.getOrderId()); // add line items

        shoppingCartService.delete(userId); // clear cart after checkout

        return new CheckoutRespondDto(order, orderLineItemList);
    }
}

