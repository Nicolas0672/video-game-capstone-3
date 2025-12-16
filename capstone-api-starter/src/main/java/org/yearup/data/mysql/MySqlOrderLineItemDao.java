package org.yearup.data.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.data.OrderLineItemDao;
import org.yearup.models.OrderLineItem;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MySqlOrderLineItemDao extends MySqlDaoBase implements OrderLineItemDao {

    private static final Logger log = LoggerFactory.getLogger(MySqlOrderLineItemDao.class);

    public MySqlOrderLineItemDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<OrderLineItem> create(ShoppingCart shoppingCart, int orderId) {
        String query = "INSERT INTO order_line_items(order_id, product_id, sales_price, quantity, discount) VALUES " +
                "(?, ?, ?, ?, ?)";
        List<OrderLineItem> orderLineItemList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            for(Map.Entry<Integer, ShoppingCartItem> entry : shoppingCart.getItems().entrySet()){
                ShoppingCartItem shoppingCartItem = entry.getValue();
                int productId = shoppingCartItem.getProductId();
                log.info("ProductId: {}", productId);
                int quantity = shoppingCartItem.getQuantity();
                log.info("Quantity: {}", quantity);
                BigDecimal salesPrice = shoppingCartItem.getLineTotal();
                log.info("salesPrice: {}", salesPrice);
                BigDecimal discount = shoppingCartItem.getDiscountPercent();
                log.info("discount: {}", discount);

                preparedStatement.setInt(1, orderId);
                preparedStatement.setInt(2, productId);
                preparedStatement.setBigDecimal(3, salesPrice);
                preparedStatement.setInt(4, quantity);
                preparedStatement.setBigDecimal(5, discount);

                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    int id = resultSet.getInt(1);
                    OrderLineItem orderLineItem = new OrderLineItem(id, orderId, productId, salesPrice, quantity);
                    orderLineItemList.add(orderLineItem);
                }

            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return orderLineItemList;
    }
}

