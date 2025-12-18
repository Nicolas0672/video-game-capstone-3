package org.yearup.data;

import org.yearup.models.CartRows;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartDao
{
    ShoppingCart getShoppingCartByUserId(int userId);
    void create(int userId, int productId, int quantity);
    CartRows getProductFromCartById(int productId, int userId);
    void delete(int userId);
    void updateQuantity(int userId, int productId, int quantity);
    void deleteProductFromCart(int userId, int productId);
}
