package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.parameters.P;
import org.yearup.data.ProfileDao;
import org.yearup.dto.CheckoutRespondDto;
import org.yearup.exception.EmptyCartException;
import org.yearup.models.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceTest {

    @Mock
    private OrderService orderService;

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private OrderLineItemService orderLineItemService;

    @Mock
    private ProfileDao profileDao;

    @InjectMocks
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkout_success() {
        int userId = 1;
        int productId = 10;

        // Arrange: create a cart with one item
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setQuantity(2);
        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(new BigDecimal("20.0"));
        shoppingCartItem.setProduct(product);

        Profile profile = new Profile();
        profile.setUserId(userId);

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderId(5);

        ShoppingCart cart = new ShoppingCart();
        cart.getItems().put(productId, shoppingCartItem);

        List<OrderLineItem> lineItems = List.of(
                new OrderLineItem(1, order.getOrderId(), product.getProductId(), product.getPrice(), 2)
        );

        when(shoppingCartService.getShoppingCartByUserId(userId)).thenReturn(cart);
        when(profileDao.findByUserId(userId)).thenReturn(profile);
        when(orderService.create(userId)).thenReturn(order);
        when(orderLineItemService.create(cart, order.getOrderId())).thenReturn(lineItems);

        // Act
        CheckoutRespondDto checkoutRespondDto = checkoutService.checkout(userId);

        // Assert
        assertEquals(order, checkoutRespondDto.getOrder());
        assertEquals(lineItems, checkoutRespondDto.getOrderLineItemList());

        verify(shoppingCartService, times(1)).delete(userId);

    }

    @Test
    void checkout_emptyCart_throwsException() {
        int userId = 1;

        ShoppingCart emptyCart = new ShoppingCart();

        when(shoppingCartService.getShoppingCartByUserId(userId)).thenReturn(emptyCart);

        assertThrows(EmptyCartException.class, () -> checkoutService.checkout(userId));

        verify(orderService, never()).create(anyInt());
        verify(orderLineItemService, never()).create(any(), anyInt());
        verify(shoppingCartService, never()).delete(anyInt());
    }

}