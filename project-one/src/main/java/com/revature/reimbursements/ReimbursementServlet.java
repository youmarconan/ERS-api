package com.revature.reimbursements;

import java.io.IOException;
import java.time.LocalDateTime;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReimbursementServlet extends HttpServlet {

    private final ReimbursementService reimbursementService;
    private final ObjectMapper objectMapper;

    private static Logger logger = LogManager.getLogger(ReimbursementServlet.class);

    public ReimbursementServlet(ReimbursementService reimbursementService, ObjectMapper objectMapper) {
        this.reimbursementService = reimbursementService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("A GET request was received by /project1/reimbursement at {}", LocalDateTime.now());

        HttpSession loggedInUserSession = req.getSession(false);

        if (loggedInUserSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(401, "Please log in first!")));

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(),
                    "Non logged in requester is not permitted to communicate with this endpoint");

            return;
        }

        UserResponse loggedInUser = (UserResponse) loggedInUserSession.getAttribute("loggedInUser");

        resp.setContentType("application/json");

        String id = req.getParameter("id");
        String status = req.getParameter("status");
        String type = req.getParameter("type");

        boolean w = loggedInUser.getRoleName().equals("manager");
        boolean x = false;
        if (id != null) {
            try {
                x = loggedInUser.getId().equals(reimbursementService.getReimbursementById(id).getAuthorId());
            } catch (InvalidRequestException e) {

                resp.setStatus(400);

                Error error = new Error(400, e.getMessage());

                resp.getWriter().write(objectMapper.writeValueAsString(error));
                return;
            }
        }
        if (!w && !x) {

            resp.setStatus(403); // FORBIDDEN
            resp.getWriter().write(objectMapper.writeValueAsString(
                    new Error(403, "Requester is not permitted to communicate with this endpoint.")));

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(),
                    "Requester is not permitted to communicate with this endpoint");
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

            logger.info("GET request successfully processed at {}", LocalDateTime.now());

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
        } catch (ResourceNotFoundException e) {

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(404);

            Error error = new Error(404, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("A POST request was received by /project1/reimbursement at {}", LocalDateTime.now());

        resp.setContentType("application/json");

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

        try {

            NewReimbursementRequest newReimbursementRequest = objectMapper.readValue(req.getInputStream(),
                    NewReimbursementRequest.class);
            ResponseString generatedId = reimbursementService.createNewReimbursement(newReimbursementRequest,
                    loggedInUser.getId());
            resp.getWriter().write(objectMapper.writeValueAsString(generatedId));

            logger.info("POST request successfully processed at {}", LocalDateTime.now());

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

        } catch (ResourceNotFoundException e) {

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(), e.getMessage());

            resp.setStatus(404);

            Error error = new Error(404, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        logger.info("A PUT request was received by /project1/reimbursement at {}", LocalDateTime.now());

        HttpSession loggedInUserSession = req.getSession(false);

        if (loggedInUserSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(objectMapper.writeValueAsString(new Error(401, "Please log in first!")));

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(),
                    "Non logged in requester is not permitted to communicate with this endpoint");

            return;
        }

        UserResponse loggedInUser = (UserResponse) loggedInUserSession.getAttribute("loggedInUser");

        boolean w = loggedInUser.getRoleName().equals("manager");

        if ((!w)) {

            resp.setStatus(403); // FORBIDDEN
            resp.getWriter().write(objectMapper.writeValueAsString(
                    new Error(403, "Requester is not permitted to communicate with this endpoint.")));

            logger.warn("Error processing request at {}, error message: {}", LocalDateTime.now(),
                    "Non manager requester is not permitted to communicate with this endpoint");

            return;

        }

        resp.setContentType("application/json");

        try {

            ApproveOrDenyBody approveOrDenyBody = objectMapper.readValue(req.getInputStream(), ApproveOrDenyBody.class);

            ResponseString generatedId = reimbursementService.approveOrDeny(approveOrDenyBody, loggedInUser.getId());
            resp.getWriter().write(objectMapper.writeValueAsString(generatedId));

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
