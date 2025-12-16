package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.Profile;
import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {
    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public Order create(Profile profile, Date localDate, double shippingAmount) {
        String query = "INSERT INTO orders(user_id, date, address, city, state, zip, shipping_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            preparedStatement.setInt(1, profile.getUserId());
            preparedStatement.setDate(2, localDate);
            preparedStatement.setString(3, profile.getAddress());
            preparedStatement.setString(4, profile.getCity());
            preparedStatement.setString(5, profile.getState());
            preparedStatement.setString(6, profile.getZip());
            preparedStatement.setDouble(7, shippingAmount);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int id = resultSet.getInt(1);
                    return new Order(id, profile.getUserId(), localDate, profile.getAddress(), profile.getCity(), profile.getZip(), shippingAmount);
                }
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
}

