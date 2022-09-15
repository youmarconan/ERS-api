package com.revature.reimbursements;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.users.UserResponse;
import com.revature.common.Error;
import com.revature.common.ResponseString;
import com.revature.common.exceptions.DataSourceException;
import com.revature.common.exceptions.InvalidRequestException;


public class UpdateOwnReimbursementServlet extends HttpServlet{

    private final ReimbursementService reimbursementService;
    private final ObjectMapper objectMapper;

    public UpdateOwnReimbursementServlet(ReimbursementService reimbursementService, ObjectMapper objectMapper) {
        this.reimbursementService = reimbursementService;
        this.objectMapper = objectMapper;
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

        boolean w = loggedInUser.getRoleName().equals("employee");

        if( (!w) ){

            resp.setStatus(403); // FORBIDDEN
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(403, "Requester is not permitted to communicate with this endpoint.")));
            return;

        }

        
        resp.setContentType("application/json");
        String toBeUpdated = req.getParameter("update");
        try {
            UpdateOwnReimbBody updateOwnReimbBody = objectMapper.readValue(req.getInputStream(), UpdateOwnReimbBody.class);

            ReimbursementResponse reimbursement = reimbursementService.getReimbursementById(updateOwnReimbBody.getReimbursementId());
            boolean x = reimbursement.getStatusName().equals("pending");
            boolean y = reimbursement.getAuthorId().equals(loggedInUser.getId());

            if(!y){
                resp.setStatus(403); // FORBIDDEN
                resp.getWriter().write(objectMapper.writeValueAsString(new Error(403, "Reimbuesement's auther ID DOSE NOT match your ID")));
                return;
            }

            if(!x){
                resp.setStatus(403); // FORBIDDEN
                resp.getWriter().write(objectMapper.writeValueAsString(new Error(403, "Reimbuesement status is not pending anymore!")));
                return;
            }

            if (toBeUpdated.equals("amount")){
                ResponseString responseString = reimbursementService.updateAmount(updateOwnReimbBody);
                resp.getWriter().write(objectMapper.writeValueAsString(responseString));
            }

            if (toBeUpdated.equals("type")){
                ResponseString responseString = reimbursementService.updateTypeId(updateOwnReimbBody);
                resp.getWriter().write(objectMapper.writeValueAsString(responseString));
            }

            if (toBeUpdated.equals("description")){
                ResponseString responseString = reimbursementService.updateDescription(updateOwnReimbBody);
                resp.getWriter().write(objectMapper.writeValueAsString(responseString));
            }        }catch (InvalidRequestException | JsonMappingException e) {

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
