package com.revature.reimbursements;

import java.io.IOException;
import java.time.LocalDateTime;

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
import com.revature.common.exceptions.ResourceNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateOwnReimbursementServlet extends HttpServlet {

    private final ReimbursementService reimbursementService;
    private final ObjectMapper objectMapper;

    private static Logger logger = LogManager.getLogger(UpdateOwnReimbursementServlet.class);

    public UpdateOwnReimbursementServlet(ReimbursementService reimbursementService, ObjectMapper objectMapper) {
        this.reimbursementService = reimbursementService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("A PUT request was received by /project1/updateOwnReimbursement at {}", LocalDateTime.now());

        HttpSession loggedInUserSession = req.getSession(false);

        if (loggedInUserSession == null) {

            resp.setStatus(401);
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(401, "Please log in first!")));

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(),
                    "Non logged in requester is not permitted to communicate with this endpoint");

            return;
        }

        UserResponse loggedInUser = (UserResponse) loggedInUserSession.getAttribute("loggedInUser");

        boolean w = loggedInUser.getRoleName().equals("employee");

        if ((!w)) {

            resp.setStatus(403); // FORBIDDEN
            resp.getWriter().write(objectMapper.writeValueAsString(
                    new Error(403, "Requester is not permitted to communicate with this endpoint.")));

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(),
                    "Non employee requester is not permitted to communicate with this endpoint");

            return;

        }

        resp.setContentType("application/json");
        String toBeUpdated = req.getParameter("update");

        try {
            UpdateOwnReimbBody updateOwnReimbBody = objectMapper.readValue(req.getInputStream(),
                    UpdateOwnReimbBody.class);

            ReimbursementResponse reimbursement = reimbursementService
                    .getReimbursementById(updateOwnReimbBody.getReimbursementId());
            boolean x = reimbursement.getStatusName().equals("pending");
            boolean y = reimbursement.getAuthorId().equals(loggedInUser.getId());

            if (!y) {
                resp.setStatus(403); // FORBIDDEN
                resp.getWriter().write(objectMapper
                        .writeValueAsString(new Error(403, "Reimbuesement's author ID DOSE NOT match your ID")));
                return;
            }

            if (!x) {
                resp.setStatus(403); // FORBIDDEN
                resp.getWriter().write(objectMapper
                        .writeValueAsString(new Error(403, "Reimbuesement status is not pending anymore!")));
                return;
            }

            if (toBeUpdated.equals("amount")) {
                ResponseString responseString = reimbursementService.updateAmount(updateOwnReimbBody);
                resp.getWriter().write(objectMapper.writeValueAsString(responseString));
            }

            if (toBeUpdated.equals("type")) {
                ResponseString responseString = reimbursementService.updateTypeId(updateOwnReimbBody);
                resp.getWriter().write(objectMapper.writeValueAsString(responseString));
            }

            if (toBeUpdated.equals("description")) {
                ResponseString responseString = reimbursementService.updateDescription(updateOwnReimbBody);
                resp.getWriter().write(objectMapper.writeValueAsString(responseString));
            }

            logger.info("PUT request successfully processed at {}", LocalDateTime.now());

        } catch (InvalidRequestException | JsonMappingException e) {

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        } catch (DataSourceException e) {

            logger.error("A data source error occurred at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(500);

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }catch (ResourceNotFoundException e) {

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(404);

            Error error = new Error(404, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }
}
