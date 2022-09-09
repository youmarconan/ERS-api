package com.revature.users;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.common.ResponseString;
import com.revature.common.Error;
import com.revature.common.exceptions.DataSourceException;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.common.exceptions.IsAlreadyExist;

public class UserServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserServlet(UserService userService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        resp.getWriter().write(objectMapper.writeValueAsString(userService.getAllUsers()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {

            NewUserRequest requestBody = objectMapper.readValue(req.getInputStream(), NewUserRequest.class);
            ResponseString generatedId = userService.register(requestBody);
            resp.getWriter().write(objectMapper.writeValueAsString(generatedId));

        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        } catch (IsAlreadyExist e) {

            resp.setStatus(409);

            Error error = new Error(409, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        } catch (DataSourceException e) {

            resp.setStatus(500);

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        String toBeUpdated = req.getParameter("update");

        try {
            UpdateRequestBody requestBody = objectMapper.readValue(req.getInputStream(), UpdateRequestBody.class);

            if (toBeUpdated.equals("firstname")) {
                ResponseString generatedId = userService.updateFristNmae(requestBody);
                resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
            }

            if (toBeUpdated.equals("lastname")) {
                ResponseString generatedId = userService.updateLastNmae(requestBody);
                resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
            }

            if (toBeUpdated.equals("email")) {
                ResponseString generatedId = userService.updateEmail(requestBody);
                resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
            }

            if (toBeUpdated.equals("password")) {
                ResponseString generatedId = userService.updatePassword(requestBody);
                resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
            }

            if (toBeUpdated.equals("isactive")) {
                ResponseString generatedId = userService.updateIsActive(requestBody);
                resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
            }

            if (toBeUpdated.equals("roleid")) {
                ResponseString generatedId = userService.updateRoleId(requestBody);
                resp.getWriter().write(objectMapper.writeValueAsString(generatedId));
            }
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

}
