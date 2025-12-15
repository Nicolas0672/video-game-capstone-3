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
    public ResponseEntity<ShoppingCart> getCart(Principal principal)
    {
        try
        {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();
            ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);
            return ResponseEntity.ok(shoppingCart);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    @PostMapping("products/{productId}")
    public ResponseEntity<ShoppingCartItem> create(@PathVariable int productId, Principal principal){

        String name = principal.getName();
        User user = userDao.getByUserName(name);
        int userId = user.getId();
        ShoppingCartItem shoppingCartItem = shoppingCartService.create(userId, productId);
        return ResponseEntity.ok(shoppingCartItem);
    }

    @PutMapping("products/{productId}")
    public ResponseEntity<Void> updateQuantity(@PathVariable int productId, @RequestBody ShoppingCartItem shoppingCartItem, Principal principal){

        String username = principal.getName();
        User user = userDao.getByUserName(username);
        int id = user.getId();
        shoppingCartService.updateQuantity(id, productId, shoppingCartItem.getQuantity());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(Principal principal){

        String name = principal.getName();
        User user = userDao.getByUserName(name);
        int userId = user.getId();
        shoppingCartService.delete(userId);
        return ResponseEntity.noContent().build();
    }

}
