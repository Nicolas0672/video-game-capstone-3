package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.UserDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;

import java.security.Principal;

@RestController
@RequestMapping("cart")
@PreAuthorize("hasRole('USER')")
@CrossOrigin
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
    public ResponseEntity<ShoppingCart> getCart(Principal principal) {
        int userId = getUserId(principal);
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);
        return ResponseEntity.ok(shoppingCart);
    }

    @PostMapping("products/{productId}")
    public ResponseEntity<ShoppingCartItem> create(@PathVariable int productId, Principal principal){

        int userId = getUserId(principal);
        ShoppingCartItem shoppingCartItem = shoppingCartService.create(userId, productId);
        return ResponseEntity.ok(shoppingCartItem);
    }

    @PutMapping("products/{productId}")
    public ResponseEntity<Void> updateQuantity(@PathVariable int productId, @RequestBody ShoppingCartItem shoppingCartItem, Principal principal){

        int userId = getUserId(principal);
        shoppingCartService.updateQuantity(userId, productId, shoppingCartItem.getQuantity());
        return ResponseEntity.ok().build();

    }

    @DeleteMapping
    public ResponseEntity<Void> delete(Principal principal){

        int userId = getUserId(principal);
        shoppingCartService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    // Helper
    private int getUserId(Principal principal){
        String name = principal.getName();
        User user = userDao.getByUserName(name);
        return user.getId();
    }

}
