package com.revature.reimbursements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.common.datasource.ConnectionFactory;
import com.revature.common.exceptions.DataSourceException;

public class ReimbursementDAO {

    private final String baseSelect = "select reimbursement.id as reimbursement_id, " +
            "reimbursement.amount as amount, " +
            "reimbursement.submitted as submitted, " +
            "reimbursement.resolved as resolved, " +
            "reimbursement.description as description, " +
            "reimbursement.author_id as author_id, " +
            "reimbursement.resolver_id as resolver_id, " +
            "reimbursement_type.\"name\" as type_name, " +
            "reimbursement_type.id as type_id, " +
            "reimbursement_status.\"name\" as status_name, " +
            "reimbursement_status.id as status_id " +
            "from reimbursement " +
            "join reimbursement_type " +
            "on reimbursement_type.id = reimbursement.type_id " +
            "join reimbursement_status " +
            "on reimbursement_status.id = reimbursement.status_id ";

    private ArrayList<Reimbursement> mapResultSet(ResultSet rs) throws SQLException {

        ArrayList<Reimbursement> reimbursements = new ArrayList<>();

        while (rs.next()) {

            Reimbursement reimbursement = new Reimbursement();
            ReimbursementStatus reimbursementStatus = new ReimbursementStatus();
            ReimbursementType reimbursementType = new ReimbursementType();

            reimbursementStatus.setStatusName(rs.getString("status_name"));
            reimbursementStatus.setStatusId(rs.getString("status_id"));
            reimbursementType.setTypeName(rs.getString("type_name"));
            reimbursementType.setTypeId(rs.getString("type_id"));

            reimbursement.setId(rs.getString("reimbursement_id"));
            reimbursement.setAmount(rs.getDouble("amount"));
            reimbursement.setSubmitted(rs.getString("submitted"));
            reimbursement.setResolved(rs.getString("resolved"));
            reimbursement.setDescription(rs.getString("description"));
            reimbursement.setAuthorId(rs.getString("author_id"));
            reimbursement.setResolverId(rs.getString("resolver_id"));
            reimbursement.setType(reimbursementType);
            reimbursement.setStatus(reimbursementStatus);

            reimbursements.add(reimbursement);

        }

        return reimbursements;
    }

    public List<Reimbursement> getAllReimbursements() {

        String sql = baseSelect + ";";
        ArrayList<Reimbursement> allReimbursements = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);

            allReimbursements = mapResultSet(rs);

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
        return allReimbursements;
    }

    public List<Reimbursement> findReimbursementbyStatus(String status) {

        String sql = baseSelect + "where reimbursement_status.\"name\" = ?" + " ;";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs);

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }

    }

    public List<Reimbursement> findReimbursementbyType(String type) {

        String sql = baseSelect + "where reimbursement_type.\"name\" = ?" + " ;";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs);

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }

    }

    public Optional<Reimbursement> findReimbursementById(String id) {

        String sql = baseSelect + "where reimbursement.id = ?" + " ;";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }

    }

    
    public String approveOrDenyReimbursement(String statusId, String reimbursementId, String resolverId) {
        String sql = "update reimbursement " +
        "set status_id  = ?, " +
        "resolved = CURRENT_TIMESTAMP, " +
        "resolver_id = ? " +
        "where reimbursement.id = ? ;" ;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, Integer.parseInt(statusId));
            pstmt.setInt(2, Integer.parseInt(resolverId));
            pstmt.setInt(3, Integer.parseInt(reimbursementId));

            int rs = pstmt.executeUpdate();

            return "Reimbursement status has been updated, Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String createNewReimbursement(Reimbursement reimbursement) {

        String sql = "insert into reimbursement ( amount , description , author_id ,type_id ) values " + " ( ?, ?, ?, ?) ;";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstat = conn.prepareStatement(sql, new String[] { "id" });

            pstat.setDouble(1, reimbursement.getAmount());
            pstat.setString(2, reimbursement.getDescription());
            pstat.setInt(3, Integer.parseInt(reimbursement.getAuthorId()));
            pstat.setInt(4, Integer.parseInt(reimbursement.getType().getTypeId()));

            pstat.executeUpdate();

            ResultSet rs = pstat.getGeneratedKeys();
            rs.next();

            reimbursement.setId(rs.getString("id"));

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }

        return "Generated reimbursement ID = " + reimbursement.getId();
    }


    public String updateOwnreimbursementAmount( UpdateOwnReimbBody updateOwnReimbBody) {
        String sql = "update reimbursement " +
        "set amount = ?, " +
        "where reimbursement.id = ? ;" ;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setDouble(1, Double.parseDouble(updateOwnReimbBody.getUpdateTo()));
            pstmt.setInt(2, Integer.parseInt(updateOwnReimbBody.getReimbursementId()));

            int rs = pstmt.executeUpdate();

            return "Reimbursement's amount has been updated, Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateOwnreimbursementTypeId( UpdateOwnReimbBody updateOwnReimbBody) {
        String sql = "update reimbursement " +
        "set type_id = ?, " +
        "where reimbursement.id = ? ;" ;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, Integer.parseInt(updateOwnReimbBody.getUpdateTo()));
            pstmt.setInt(2, Integer.parseInt(updateOwnReimbBody.getReimbursementId()));

            int rs = pstmt.executeUpdate();

            return "Reimbursement's type ID has been updated, Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public String updateOwnreimbursementDescription( UpdateOwnReimbBody updateOwnReimbBody) {
        String sql = "update reimbursement " +
        "set description = ?, " +
        "where reimbursement.id = ? ;" ;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, updateOwnReimbBody.getUpdateTo());
            pstmt.setInt(2, Integer.parseInt(updateOwnReimbBody.getReimbursementId()));

            int rs = pstmt.executeUpdate();

            return "Reimbursement's description has been updated, Rows affected = " + rs;

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public boolean isIdValid(String id) {
        return findReimbursementById(id).isPresent();
    }

}
