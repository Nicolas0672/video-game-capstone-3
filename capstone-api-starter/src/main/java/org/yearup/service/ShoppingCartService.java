package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.CartRows;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

@Service
public class ShoppingCartService {
    private final ShoppingCartDao shoppingCartDao;
    private final ProductDao productDao;

    public ShoppingCartService(ShoppingCartDao shoppingCartDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.productDao = productDao;
    }

    // add item to shopping cart
    public ShoppingCartItem create(int userId, int productId){

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        CartRows cartRows = shoppingCartDao.getProductById(productId, userId);

        if(cartRows != null){
            int newQuantity = cartRows.getQuantity() + 1;
            shoppingCartDao.updateQuantity(userId, productId, newQuantity);
            Product product = productDao.getById(productId);
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(newQuantity);
        } else {
            shoppingCartDao.create(userId, productId, shoppingCartItem.getQuantity());
            Product product = productDao.getById(productId);
            shoppingCartItem.setProduct(product);
        }

        return shoppingCartItem;
    }

    public ShoppingCart getShoppingCartByUserId(int userId){
        return shoppingCartDao.getShoppingCartByUserId(userId);
    }
}
