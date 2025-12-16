package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.UserDao;
import org.yearup.models.User;
import org.yearup.service.CheckoutService;

import java.security.Principal;

@RestController
@RequestMapping("checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final UserDao userDao;

    public CheckoutController(CheckoutService checkoutService, UserDao userDao) {
        this.checkoutService = checkoutService;
        this.userDao = userDao;
    }

    @PostMapping
    public ResponseEntity<Void> checkout(Principal principal){
        String username = principal.getName();
        User user = userDao.getByUserName(username);
        int id = user.getId();
        checkoutService.checkout(id);

        return ResponseEntity.ok().build();
    }
}
