package org.yearup.data;

import org.yearup.models.CartRows;
import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getShoppingCartByUserId(int userId);
    void create(int userId, int productId, int quantity);
    CartRows getProductById(int productId, int userId);

    void updateQuantity(int userId, int productId, int quantity);
}
