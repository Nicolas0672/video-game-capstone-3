package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        String query = "SELECT * FROM categories";
        List<Category> categories = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery())
        {
            while(resultSet.next()){
                int categoryId = resultSet.getInt("category_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");

                Category category = new Category(categoryId, name, description);
                categories.add(category);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        String query = "SELECT * FROM category WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, categoryId);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){

                    int category_id = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");

                    return new Category(category_id, name, description);
                }
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        String query = "INSERT INTO category(name, description) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){

                int category_id = resultSet.getInt(1);
                return new Category(category_id, category.getName(), category.getDescription());
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        String query = "UPDATE category SET name = ?, description = ? WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, categoryId);

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(int categoryId)
    {
        String query = "DELETE FROM category WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, categoryId);

            preparedStatement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
