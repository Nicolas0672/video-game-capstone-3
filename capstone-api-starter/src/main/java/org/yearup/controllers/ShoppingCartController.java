package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;

import java.security.Principal;

@RestController
@RequestMapping("cart")
@PreAuthorize("hasRole('USER')")
public class ShoppingCartController
{
    // a shopping cart requires
    private final ShoppingCartService shoppingCartService;
    private final UserDao userDao;

    public ShoppingCartController(UserDao userDao, ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
        this.userDao = userDao;
    }

    @GetMapping
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartService.getShoppingCartByUserId(userId);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added

    @PostMapping("products/{productId}")
    public ResponseEntity<ShoppingCartItem> create(@PathVariable int productId, Principal principal){

        String name = principal.getName();
        User user = userDao.getByUserName(name);
        int userId = user.getId();

        ShoppingCartItem shoppingCartItem = shoppingCartService.create(userId, productId);

        return ResponseEntity.ok(shoppingCartItem);
    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated


    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart

}
