package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartServiceTest {

    @Mock
    private ShoppingCartDao shoppingCartDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void create() {

        // Arrange
        int userId = 1;
        int productId = 10;

        Product product = new Product();
        product.setProductId(productId);
        product.setPrice(new BigDecimal("20.0"));

        ShoppingCart cart = new ShoppingCart();


    }

    @Test
    void updateQuantity() {
    }

    @Test
    void getShoppingCartByUserId() {
    }

    @Test
    void delete() {
    }
}