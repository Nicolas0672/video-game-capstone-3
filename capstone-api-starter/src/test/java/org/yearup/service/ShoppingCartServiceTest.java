package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.yearup.data.ShoppingCartDao;
import org.yearup.exception.ShoppingCartNotFoundException;
import org.yearup.models.CartRows;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartDao shoppingCartDao;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void create_productNotInCart_callsCreate() {

        // Arrange
        int userId = 1;
        int productId = 10;

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(new BigDecimal("20.0"));

        when(productService.getById(productId)).thenReturn(product);
        when(shoppingCartDao.getProductFromCartById(productId, userId)).thenReturn(null);

        ShoppingCartItem result = shoppingCartService.create(userId, productId);

        assertEquals(productId, result.getProduct().getProductId());
        assertEquals(1, result.getQuantity());

        verify(shoppingCartDao, times(1)).create(userId, productId, 1);
    }

    @Test
    void create_productAlreadyInCart_callsUpdateQuantity(){

        // Arrange
        int userId = 1;
        int productId = 10;

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(new BigDecimal("20"));

        CartRows existing = new CartRows(productId, userId, 3);

        when(productService.getById(productId)).thenReturn(product);
        when(shoppingCartDao.getProductFromCartById(productId, userId)).thenReturn(existing);

        ShoppingCartItem result = shoppingCartService.create(userId, productId);
        assertEquals(4, result.getQuantity());

        verify(shoppingCartDao, times(1)).updateQuantity(userId, productId, 4);
    }

    @Test
    void updateQuantity() {

        // Arrange
        int userId = 1;
        int productId = 5;

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(new BigDecimal("20.0"));

        CartRows existing = new CartRows(productId, userId, 5);

        when(productService.getById(productId)).thenReturn(product);
        when(shoppingCartDao.getProductFromCartById(productId, userId)).thenReturn(existing);

        shoppingCartService.updateQuantity(userId, productId, 6);

        verify(shoppingCartDao, times(1)).updateQuantity(userId, productId, 6);
    }

    @Test
    void getShoppingCartByUserId_cartExists() {
        int userId = 1;
        ShoppingCart cart = new ShoppingCart();

        when(shoppingCartDao.getShoppingCartByUserId(userId)).thenReturn(cart);

        ShoppingCart result = shoppingCartService.getShoppingCartByUserId(userId);

        assertEquals(cart, result);
        verify(shoppingCartDao, times(1)).getShoppingCartByUserId(userId);
    }

    @Test
    void getShoppingCartByUserId_cartNotFound() {
        int userId = 1;

        when(shoppingCartDao.getShoppingCartByUserId(userId)).thenReturn(null);

        assertThrows(ShoppingCartNotFoundException.class,
                () -> shoppingCartService.getShoppingCartByUserId(userId));

        verify(shoppingCartDao, times(1)).getShoppingCartByUserId(userId);
    }



    @Test
    void delete_callsDao() {
        int userId = 1;

        shoppingCartService.delete(userId);

        verify(shoppingCartDao, times(1)).delete(userId);
    }

}