package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.exception.ProductNotFoundInCartException;
import org.yearup.exception.ShoppingCartNotFoundException;
import org.yearup.models.CartRows;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

@Service
public class ShoppingCartService {
    private final ShoppingCartDao shoppingCartDao;
    private final ProductService productService;

    public ShoppingCartService(ShoppingCartDao shoppingCartDao, ProductService productService) {
        this.shoppingCartDao = shoppingCartDao;
        this.productService = productService;
    }

    // add item to shopping cart
    // check if product exists
    public ShoppingCartItem create(int userId, int productId){

        Product product = productService.getById(productId);

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        CartRows cartRows = shoppingCartDao.getProductFromCartById(productId, userId);

        if(cartRows != null){
            int newQuantity = cartRows.getQuantity() + 1;
            shoppingCartDao.updateQuantity(userId, productId, newQuantity);
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(newQuantity);
        } else {
            shoppingCartDao.create(userId, productId, shoppingCartItem.getQuantity());
            shoppingCartItem.setProduct(product);
        }

        return shoppingCartItem;
    }

    // Add exception if productId is not found, throw a 404
    public void updateQuantity(int userId, int productId, int newQuantity){
        productService.getById(productId);
        CartRows cartRows = shoppingCartDao.getProductFromCartById(productId, userId);
        if(cartRows != null){
            shoppingCartDao.updateQuantity(userId, productId, newQuantity);
        } else {
            throw new ProductNotFoundInCartException("Product not found in cart with productId: " + productId);
        }
    }

    public ShoppingCart getShoppingCartByUserId(int userId){
        ShoppingCart shoppingCart = shoppingCartDao.getShoppingCartByUserId(userId);
        if(shoppingCart == null){
            throw new ShoppingCartNotFoundException("Cart not found with userId: " + userId);
        }
        return shoppingCart;
    }

    public void delete(int userId){
        shoppingCartDao.delete(userId);
    }
}
