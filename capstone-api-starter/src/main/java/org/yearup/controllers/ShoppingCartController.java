package org.yearup.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Shopping Cart", description = "API for managing shopping cart")
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
    @Operation(summary = "Get shopping cart by userID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shopping cart found"),
            @ApiResponse(responseCode = "404", description = "Shopping cart not found")
    })
    public ResponseEntity<ShoppingCart> getCart(Principal principal) {
        int userId = getUserId(principal);
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUserId(userId);
        return ResponseEntity.ok(shoppingCart);
    }

    @PostMapping("products/{productId}")
    @Operation(summary = "Add product to shopping cart", description = "If product already exists, increment the quantity otherwise add to cart")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product added to cart"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<ShoppingCartItem> create(@PathVariable int productId, Principal principal){

        int userId = getUserId(principal);
        ShoppingCartItem shoppingCartItem = shoppingCartService.create(userId, productId);
        return ResponseEntity.status(201).body(shoppingCartItem);
    }
    @Operation(summary = "Update product quantity")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart updated"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "404", description = "Product not found in cart to update"),
            @ApiResponse(responseCode = "400", description = "Quantity must be positive")
    })
    @PutMapping("products/{productId}")
    public ResponseEntity<Void> updateQuantity(@PathVariable int productId, @RequestBody ShoppingCartItem shoppingCartItem, Principal principal){

        int userId = getUserId(principal);
        shoppingCartService.updateQuantity(userId, productId, shoppingCartItem.getQuantity());
        return ResponseEntity.ok().build();

    }

    @DeleteMapping
    @Operation(summary = "Delete cart by userID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cart deleted"),
            @ApiResponse(responseCode = "404", description = "Cart not found to delete")
    })
    public ResponseEntity<Void> delete(Principal principal){

        int userId = getUserId(principal);
        shoppingCartService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("products/{productId}")
    @Operation(summary = "Delete product from cart")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted from cart"),
            @ApiResponse(responseCode = "404", description = "Product not found in cart to delete")
    })
    public ResponseEntity<Void> deleteProductFromCart(Principal principal, @PathVariable int productId){

        int userId = getUserId(principal);
        shoppingCartService.deleteProductFromCart(userId, productId);
        return ResponseEntity.noContent().build();

    }

    // Helper
    private int getUserId(Principal principal){
        String name = principal.getName();
        User user = userDao.getByUserName(name);
        return user.getId();
    }

}
