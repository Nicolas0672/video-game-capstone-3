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

    public CheckoutRespondDto checkout(int userId){
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);
        if(shoppingCart.getItems().isEmpty()){
            throw new EmptyCartException("Cart is empty: " + userId);
        }
        Order order = orderService.create(userId);
        List<OrderLineItem> orderLineItemList = orderLineItemService.create(shoppingCart, order.getOrderId());
        shoppingCartService.delete(userId);
        return new CheckoutRespondDto(order, orderLineItemList);
    }
}
