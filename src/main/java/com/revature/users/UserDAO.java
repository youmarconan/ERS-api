package com.revature.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.revature.common.datasource.ConnectionFactory;
import com.revature.common.exceptions.DataSourceException;

@Repository
public class UserDAO {

    private final ConnectionFactory connectionFactory;

    @Autowired 
    public UserDAO (ConnectionFactory connectionFactory){
        this.connectionFactory = connectionFactory;
    }

    private final String baseSelect = "select \"user\".username as username, " +
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
            "on user_role.id = \"user\".role_id ";

    private ArrayList<User> mapResultSet(ResultSet rs) throws SQLException {

        ArrayList<User> users = new ArrayList<>();

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
        return users;
    }

    public List<User> allUsers() {

        String sql = baseSelect + ";";
        ArrayList<User> allUsers = new ArrayList<>();

        try (Connection conn = connectionFactory.getConnection()) {

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            allUsers = mapResultSet(rs);

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
        return allUsers;
    }

    public Optional<User> findUserById(String id) {

        String sql = baseSelect + "where \"user\".id = ?" + " ;";

        try (Connection conn = connectionFactory.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }

    }

    public Optional<User> login(String username, String password) {

        String sql = baseSelect + "where username = ? and \"password\" = ?";

        try (Connection conn = connectionFactory.getConnection()) {

            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setString(1, username);
            pstat.setString(2, password);
            ResultSet rs = pstat.executeQuery();

            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }

    }

    public String register(User user) {

        String sql = "insert into \"user\" (username, email, \"password\", first_name, last_name ,is_active ,role_id) values "
                +
                "(?,?,?,?,?,?,?)";

        try (Connection conn = connectionFactory.getConnection()) {

            PreparedStatement pstat = conn.prepareStatement(sql, new String[] { "id" });
            pstat.setString(1, user.getUsername());
            pstat.setString(2, user.getEmail());
            pstat.setString(3, user.getPassword());
            pstat.setString(4, user.getFirstName());
            pstat.setString(5, user.getLastName());
            pstat.setBoolean(6, user.getIsActive());
            pstat.setInt(7, Integer.parseInt(user.getRole().getId()));

            pstat.executeUpdate();

            ResultSet rs = pstat.getGeneratedKeys();
            rs.next();

            user.setId(rs.getString("id"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Generated ID = " + user.getId();
    }

    public Optional<User> findUserByUsername(String username) {

        String sql = baseSelect + "where \"user\".username = ?";

        try (Connection conn = connectionFactory.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }

    }

    public Optional<User> findUserByEmail(String email) {

        String sql = baseSelect + "where \"user\".email = ?";

        try (Connection conn = connectionFactory.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }

    }

    public String updateUserFristName(String to, String id) {
        String sql = "update \"user\" set  first_name = ? where id = ? ;";

        try (Connection conn = connectionFactory.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, to);
            pstmt.setInt(2, Integer.parseInt(id));

            int rs = pstmt.executeUpdate();

            return "User first name updated to " + to + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateUserLastName(String to, String id) {
        String sql = "update \"user\" set  last_name = ? where id = ? ;";

        try (Connection conn = connectionFactory.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, to);
            pstmt.setInt(2, Integer.parseInt(id));

            int rs = pstmt.executeUpdate();

            return "User last name updated to " + to + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateUserEmail(String to, String id) {
        String sql = "update \"user\" set  email = ? where id = ? ;";

        try (Connection conn = connectionFactory.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, to);
            pstmt.setInt(2, Integer.parseInt(id));

            int rs = pstmt.executeUpdate();

            return "User email updated to " + to + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException("This email is already taken");
        }
    }

    public String updateUserPassword(String to, String id) {
        String sql = "update \"user\" set  \"password\" = ? where id = ? ;";

        try (Connection conn = connectionFactory.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, to);
            pstmt.setInt(2, Integer.parseInt(id));

            int rs = pstmt.executeUpdate();

            return "User password updated to " + to + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateUserIsActive(String to, String id) {
        String sql = "update \"user\" set  is_active = ? where id = ? ;";

        try (Connection conn = connectionFactory.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setBoolean(1, Boolean.parseBoolean(to));
            pstmt.setInt(2, Integer.parseInt(id));

            int rs = pstmt.executeUpdate();

            return "User active status updated to " + to + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateUserRoleId(String to, String id) {
        String sql = "update \"user\" set  role_id = ? where id = ? ;";

        try (Connection conn = connectionFactory.getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, Integer.parseInt(to));
            pstmt.setInt(2, Integer.parseInt(id));

            int rs = pstmt.executeUpdate();

            return "User role ID updated to " + to + ", Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public boolean isUsernameTaken(String username) {
        return findUserByUsername(username).isPresent();
    }

    public boolean isEmailTaken(String email) {
        return findUserByEmail(email).isPresent();
    }

    public boolean isIdValid(String id) {
        return findUserById(id).isPresent();
    }

    public boolean isIdActive(String id) {
        User user = findUserById(id).get();
        return user.getIsActive();
    }

}
