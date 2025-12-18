package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.data.OrderDao;
import org.yearup.data.ProfileDao;
import org.yearup.models.Order;
import org.yearup.models.Profile;

import java.time.LocalDate;
import java.sql.Date;

@Service
public class OrderService {
    private final OrderDao orderDao;
    private final ProfileDao profileDao;

    public OrderService(OrderDao orderDao, ProfileDao profileDao) {
        this.orderDao = orderDao;
        this.profileDao = profileDao;
    }

    // Create a new order for a user
    // Retrieves the user's profile, sets order date and shipping amount
    public Order create(int userId){
        Profile profile = profileDao.findByUserId(userId); // get user's profile

        // Use current date as order date
        LocalDate localDate = LocalDate.now();
        Date sqlDate = Date.valueOf(localDate);

        int shippingAmount = 10; // default shipping cost
        return orderDao.create(profile, sqlDate, shippingAmount);
    }
}
