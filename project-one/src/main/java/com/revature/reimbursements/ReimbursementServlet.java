package com.revature.reimbursements;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.revature.common.Error;
import com.revature.common.ResponseString;
import com.revature.common.exceptions.DataSourceException;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.common.exceptions.ResourceNotFoundException;
import com.revature.users.UserResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReimbursementServlet extends HttpServlet {

    private final ReimbursementService reimbursementService;
    private final ObjectMapper objectMapper;

    public ReimbursementServlet(ReimbursementService reimbursementService, ObjectMapper objectMapper) {
        this.reimbursementService = reimbursementService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession loggedInUserSession = req.getSession(false);

        if (loggedInUserSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(401, "Please log in first!")));
            return;
        }

        UserResponse loggedInUser = (UserResponse) loggedInUserSession.getAttribute("loggedInUser");

        resp.setContentType("application/json");

        String id = req.getParameter("id");
        String status = req.getParameter("status");
        String type = req.getParameter("type");

        boolean w = loggedInUser.getRoleName().equals("manager");
        boolean x = loggedInUser.getId().equals(id);

        if ((!w && !x)) {

            resp.setStatus(403); // FORBIDDEN
            resp.getWriter().write(objectMapper.writeValueAsString(
                    new Error(403, "Requester is not permitted to communicate with this endpoint.")));
            return;

        }

        try {
            if (id == null && status == null && type == null) {
                List<ReimbursementResponse> reimbursements = reimbursementService.getAllReimbursements();
                resp.getWriter().write(objectMapper.writeValueAsString(reimbursements));
            }
            if (id != null) {
                ReimbursementResponse reimbursementResponse = reimbursementService.getReimbursementById(id);
                resp.getWriter().write(objectMapper.writeValueAsString(reimbursementResponse));
            }
            if (status != null) {
                List<ReimbursementResponse> reimbursements = reimbursementService.getReimbursementsByStatus(status);
                resp.getWriter().write(objectMapper.writeValueAsString(reimbursements));
            }
            if (type != null) {
                List<ReimbursementResponse> reimbursements = reimbursementService.getReimbursementsByType(type);
                resp.getWriter().write(objectMapper.writeValueAsString(reimbursements));
            }
        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        } catch (DataSourceException e) {

            resp.setStatus(500);

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        } catch (ResourceNotFoundException e) {

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        HttpSession loggedInUserSession = req.getSession(false);

        if (loggedInUserSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(401, "Please log in first!")));
            return;
        }

        UserResponse loggedInUser = (UserResponse) loggedInUserSession.getAttribute("loggedInUser");

        boolean w = loggedInUser.getRoleName().equals("employee");

        if( (!w) ){

            resp.setStatus(403); // FORBIDDEN
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(403, "Requester is not permitted to communicate with this endpoint.")));
            return;

        }

        try {

            NewReimbursementRequest newReimbursementRequest = objectMapper.readValue(req.getInputStream(), NewReimbursementRequest.class);
            ResponseString generatedId = reimbursementService.createNewReimbursement(newReimbursementRequest, loggedInUser.getId());
            resp.getWriter().write(objectMapper.writeValueAsString(generatedId));

        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        } catch (DataSourceException e) {

            resp.setStatus(500);

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession loggedInUserSession = req.getSession(false);

        if (loggedInUserSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(401, "Please log in first!")));
            return;
        }

        UserResponse loggedInUser = (UserResponse) loggedInUserSession.getAttribute("loggedInUser");

        boolean w = loggedInUser.getRoleName().equals("manager");

        if( (!w) ){

            resp.setStatus(403); // FORBIDDEN
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(403, "Requester is not permitted to communicate with this endpoint.")));
            return;

        }

        resp.setContentType("application/json");


        try {
            ApproveOrDenyBody approveOrDenyBody = objectMapper.readValue(req.getInputStream(), ApproveOrDenyBody.class);

            ResponseString generatedId = reimbursementService.approveOrDeny(approveOrDenyBody, loggedInUser.getId());
            resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
        }catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        } catch (DataSourceException e) {

            resp.setStatus(500);

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }
}
