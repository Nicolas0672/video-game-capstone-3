package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.data.OrderLineItemDao;
import org.yearup.models.OrderLineItem;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Map;

@Component
public class MySqlOrderLineItemDao extends MySqlDaoBase implements OrderLineItemDao {

    public MySqlOrderLineItemDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public OrderLineItem create(ShoppingCart shoppingCart, int orderId) {
        String query = "INSERT INTO order_line_items(order_id, product_id, sales_price, quantity, discount) VALUES " +
                "(?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            for(Map.Entry<Integer, ShoppingCartItem> entry : shoppingCart.getItems().entrySet()){
                ShoppingCartItem shoppingCartItem = entry.getValue();
                int productId = shoppingCartItem.getProductId();
                int quantity = shoppingCartItem.getQuantity();
                BigDecimal salesPrice = shoppingCartItem.getLineTotal();
                BigDecimal discount = shoppingCartItem.getDiscountPercent();

                preparedStatement.setInt(1, orderId);
                preparedStatement.setInt(2, productId);
                preparedStatement.setBigDecimal(3, salesPrice);
                preparedStatement.setInt(4, quantity);
                preparedStatement.setBigDecimal(5, discount);

                try (ResultSet resultSet = preparedStatement.executeQuery()){
                    if(resultSet.next()){
                        int id = resultSet.getInt(1);
                        return new OrderLineItem(id, orderId, productId, salesPrice, quantity);
                    }
                }
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }
}

