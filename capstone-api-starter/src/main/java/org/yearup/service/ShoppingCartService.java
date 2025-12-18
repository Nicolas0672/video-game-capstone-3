package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.data.ShoppingCartDao;
import org.yearup.exception.InvalidQuantityAmountException;
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

    // Add item to shopping cart
    // If product already exists in cart, increase quantity
    public ShoppingCartItem create(int userId, int productId){
        Product product = productService.getById(productId);

        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        CartRows cartRows = shoppingCartDao.getProductFromCartById(productId, userId);

        if(cartRows != null){
            // Product exists, increment quantity
            int newQuantity = cartRows.getQuantity() + 1;
            shoppingCartDao.updateQuantity(userId, productId, newQuantity);
            shoppingCartItem.setProduct(product);
            shoppingCartItem.setQuantity(newQuantity);
        } else {
            // Product not in cart, create new cart row
            shoppingCartDao.create(userId, productId, shoppingCartItem.getQuantity());
            shoppingCartItem.setProduct(product);
        }

        return shoppingCartItem;
    }

    // Update quantity of a product in cart
    // Throws exceptions if invalid quantity or product not in cart
    public void updateQuantity(int userId, int productId, int newQuantity){
        if(newQuantity < 0){
            throw new InvalidQuantityAmountException("Quantity must be positive: " + newQuantity);
        }

        // Ensure product exists
        productService.getById(productId);

        CartRows cartRows = shoppingCartDao.getProductFromCartById(productId, userId);
        if(cartRows != null){
            shoppingCartDao.updateQuantity(userId, productId, newQuantity);
        } else {
            throw new ProductNotFoundInCartException("Product not found in cart with productId: " + productId);
        }
    }

    // Retrieve shopping cart for a user
    // Throws exception if cart not found
    public ShoppingCart getShoppingCartByUserId(int userId){
        ShoppingCart shoppingCart = shoppingCartDao.getShoppingCartByUserId(userId);
        if(shoppingCart == null){
            throw new ShoppingCartNotFoundException("Cart not found with userId: " + userId);
        }
        return shoppingCart;
    }

    // Delete entire cart for a user
    public void delete(int userId){
        shoppingCartDao.delete(userId);
    }

    // Delete a specific product from cart
    // Ensures product exists before deletion
    public void deleteProductFromCart(int userId, int productId) {
        productService.getById(productId); // validate product exists
        shoppingCartDao.deleteProductFromCart(userId, productId);
    }
}

