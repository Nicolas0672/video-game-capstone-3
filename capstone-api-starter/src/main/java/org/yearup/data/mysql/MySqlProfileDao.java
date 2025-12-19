package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao
{
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile)
    {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    // Ensure dynamic body request
    @Override
    public Profile update(Profile profile, int userId) {
        List<String> updates = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (profile.getFirstName() != null) {
            updates.add("first_name = ?");
            params.add(profile.getFirstName());
        }

        if (profile.getLastName() != null) {
            updates.add("last_name = ?");
            params.add(profile.getLastName());
        }

        if (profile.getPhone() != null) {
            updates.add("phone = ?");
            params.add(profile.getPhone());
        }

        if (profile.getEmail() != null) {
            updates.add("email = ?");
            params.add(profile.getEmail());
        }

        if (profile.getAddress() != null) {
            updates.add("address = ?");
            params.add(profile.getAddress());
        }

        if (profile.getCity() != null) {
            updates.add("city = ?");
            params.add(profile.getCity());
        }

        if (profile.getState() != null) {
            updates.add("state = ?");
            params.add(profile.getState());
        }

        if (profile.getZip() != null) {
            updates.add("zip = ?");
            params.add(profile.getZip());
        }

        if (updates.isEmpty()) {
            return profile;
        }

        String query = "UPDATE profiles SET " + String.join(", ", updates) + " WHERE user_id = ?";
        params.add(userId);

        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            for(int i = 0; i < params.size(); i++){
                preparedStatement.setObject(i + 1, params.get(i));
            }

            preparedStatement.executeUpdate();

            return findByUserId(userId);

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile findByUserId(int userId) {
        String query = "SELECT * FROM profiles WHERE user_id = ?";

        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    String email = resultSet.getString("email");
                    String city = resultSet.getString("city");
                    String state = resultSet.getString("state");
                    String zip = resultSet.getString("zip");

                    return new Profile(userId, firstName, lastName, phone, email, address, city, state, zip);
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, int userId) {
        String query = "SELECT 1 FROM profiles WHERE email = ? AND user_id != ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query))
        {
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if(resultSet.next()){
                    return true;
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        return false;
    }

}
