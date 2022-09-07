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

public class UserDAO {


    private final String base = "select \"user\".username as username, " +
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


    private ArrayList<User> mapResultSet(ResultSet rs) throws SQLException{

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

        String sql = base + ";";
        ArrayList <User> allUsers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            allUsers = mapResultSet(rs);
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUsers;
    }




    public Optional<User> login(String username, String password) {

        String sql = base + "where username = ? and \"password\" = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstat = conn.prepareStatement(sql);
            pstat.setString(1, username);
            pstat.setString(2, password);
            ResultSet rs = pstat.executeQuery();

            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    public void register(User user, Integer userRoleId){

        String sql = 
        "insert into \"user\" (username, email, \"password\", first_name, last_name ,is_active ,role_id) values " +
        "(?,?,?,?,?,?,?)";

        try(Connection conn = ConnectionFactory.getInstance().getConnection()){

            PreparedStatement pstat = conn.prepareStatement(sql, new String[] {"id"});
            pstat.setString(1, user.getUsername());
            pstat.setString(2, user.getEmail());
            pstat.setString(3, user.getPassword());
            pstat.setString(4, user.getFirstName());
            pstat.setString(5, user.getLastName());
            pstat.setBoolean(6, user.getIsActive());
            pstat.setInt(7, userRoleId);

            pstat.executeUpdate();

            ResultSet rs = pstat.getGeneratedKeys();
            rs.next();

            user.setId(rs.getString("id"));

        }catch(SQLException e){
            e.printStackTrace();
        }

        System.out.println("Successfully registered new user with ID : " +user.getId());
    }
}
