package com.revature.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.common.datasource.ConnectionFactory;
import com.revature.roles.UserRole;

public class UserDAO {

    public List<User> allUsers() {

        ArrayList<User> users = new ArrayList<>();

        String sql = "select \"user\".username as username, " +
                "\"user\".id as user_id, " +
                "\"user\".email as email, " +
                "\"user\".\"password\" as \"password\", " +
                "\"user\".first_name as first_name, " +
                "\"user\".last_name as last_name, " +
                "\"user\".is_active as is_active, " +
                "user_role.id as role_id, " +
                "user_role.name as role_name " +
                "from \"user\" " +
                "join user_role " +
                "on user_role.id = \"user\".role_id " + ";";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()) {
                User user = new User();
                UserRole userRole = new UserRole();

                user.setUsername(rs.getString("username"));
                user.setId(rs.getString("user_id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setIsActive(rs.getBoolean("is_active"));
                userRole.setId(rs.getString("role_id"));
                userRole.setName(rs.getString("role_name"));
                user.setRole(userRole);

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public Optional<User> login(String username, String password) {

        String sql = "select \"user\".username as username, " +
                "\"user\".id as user_id, " +
                "\"user\".email as email, " +
                "\"user\".\"password\" as \"password\", " +
                "\"user\".first_name as first_name, " +
                "\"user\".last_name as last_name, " +
                "\"user\".is_active as is_active, " +
                "user_role.id as role_id, " +
                "user_role.name as role_name " +
                "from \"user\" " +
                "join user_role " +
                "on user_role.id = \"user\".role_id " +
                "where username = ? and \"password\" = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setString(1, username);
            pstat.setString(2, password);
            ResultSet rs = pstat.executeQuery();

            while (rs.next()) {
                User user = new User();
                UserRole userRole = new UserRole();

                user.setUsername(rs.getString("username"));
                user.setId(rs.getString("user_id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setIsActive(rs.getBoolean("is_active"));
                userRole.setId(rs.getString("role_id"));
                userRole.setName(rs.getString("role_name"));
                user.setRole(userRole);

                return Optional.of(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
