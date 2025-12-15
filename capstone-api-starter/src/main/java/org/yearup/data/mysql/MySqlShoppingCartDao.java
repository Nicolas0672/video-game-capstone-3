package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.CartRows;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getShoppingCartByUserId(int userId) {
        String query = "SELECT * FROM products as p JOIN shopping_cart as cart ON cart.product_id = p.product_id " +
                "WHERE cart.user_id = ?" ;
        ShoppingCart shoppingCart = new ShoppingCart();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int quantity = resultSet.getInt("quantity");
                    ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                    shoppingCartItem.setQuantity(quantity);
                    Product product = mapRow(resultSet);
                    shoppingCartItem.setProduct(product);
                    shoppingCart.add(shoppingCartItem);
                }
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return shoppingCart;
    }

    @Override
    public void create(int userId, int productId, int quantity) {
        String query = "INSERT INTO shopping_cart(user_id, product_id, quantity) VALUES " +
                "(?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, productId);
            preparedStatement.setInt(3, quantity);

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public CartRows getProductFromCartById(int productId, int userId) {
        String query = "SELECT * FROM shopping_cart WHERE product_id = ? AND user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, productId);
            preparedStatement.setInt(2, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    int id = resultSet.getInt("product_id");
                    int user = resultSet.getInt("user_id");
                    int quantity = resultSet.getInt("quantity");
                    return new CartRows(id, user, quantity);
                }
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void delete(int userId) {
        String query = "DELETE FROM shopping_cart where user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateQuantity(int userId, int productId, int quantity) {
        String query = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, productId);

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    // Helper
    protected static Product mapRow(ResultSet row) throws SQLException
    {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String subCategory = row.getString("subcategory");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        return new Product(productId, name, price, categoryId, description, subCategory, stock, isFeatured, imageUrl);
    }
}
