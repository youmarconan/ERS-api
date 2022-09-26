package com.revature.reimbursements;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.revature.common.SecurityUtils;
import com.revature.common.exceptions.AuthorizationException;
import com.revature.users.UserResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reimbursement")
public class ReimbursementController {

    private final ReimbursementService reimbursementService;

    private static Logger logger = LogManager.getLogger(ReimbursementController.class);

    @Autowired
    public ReimbursementController(ReimbursementService reimbursementService) {
        this.reimbursementService = reimbursementService;
    }

    @GetMapping(produces = "application/json")
    public List<ReimbursementResponse> getAllReimbursements(HttpServletRequest req) {

        logger.info("A GET request was received by /reimbursement at {}", LocalDateTime.now());

        HttpSession userSession = req.getSession(false);

        SecurityUtils.enforceAuthentication(userSession);
        SecurityUtils.enforcePermissions(userSession, "manager");

        return reimbursementService.getAllReimbursements();
    }

    @GetMapping(value = "/{status}", produces = "application/json")
    public List<ReimbursementResponse> getAllReimbursementsByStatus(@PathVariable String status,
            HttpServletRequest req) {

        logger.info("A GET request was received by /reimbursement at {}", LocalDateTime.now());

        HttpSession userSession = req.getSession(false);

        SecurityUtils.enforceAuthentication(userSession);
        SecurityUtils.enforcePermissions(userSession, "manager");

        return reimbursementService.getReimbursementsByStatus(status);
    }

    // @GetMapping(value = "/{type}", produces = "application/json")
    // public List<ReimbursementResponse> getAllReimbursementsByType(@PathVariable String type, HttpServletRequest req) {

    //     logger.info("A GET request was received by /reimbursement at {}", LocalDateTime.now());

    //     HttpSession userSession = req.getSession(false);

    //     SecurityUtils.enforceAuthentication(userSession);
    //     SecurityUtils.enforcePermissions(userSession, "manager");

    //     return reimbursementService.getReimbursementsByType(type);
    // }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ReimbursementResponse getAllReimbursementsById(@PathVariable String id, HttpServletRequest req) {

        logger.info("A GET request was received by /reimbursement at {}", LocalDateTime.now());

        HttpSession userSession = req.getSession(false);

        SecurityUtils.enforceAuthentication(userSession);

        if (SecurityUtils.validateRole(userSession, "manager") || SecurityUtils.validateUserId(userSession, id)) {
            return reimbursementService.getReimbursementById(id);
        } else {
            throw new AuthorizationException();
        }

    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewReimbursement(@RequestBody NewReimbursementRequest newReimbursement, HttpServletRequest req) {

        logger.info("A POST request was received by /reimbursement at {}", LocalDateTime.now());

        HttpSession userSession = req.getSession(false);

        SecurityUtils.enforceAuthentication(userSession);
        SecurityUtils.enforcePermissions(userSession, "employee");

        return reimbursementService.createNewReimbursement(newReimbursement,
                ((UserResponse) userSession.getAttribute("authUser")).getId());
    }

    @PutMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReimbursement(@RequestBody UpdateOwnReimbBody updateOwnReimbBody, HttpServletRequest req) {

        logger.info("A PUT request was received by /reimbursement at {}", LocalDateTime.now());

        HttpSession userSession = req.getSession(false);

        SecurityUtils.enforceAuthentication(userSession);
        SecurityUtils.enforcePermissions(userSession, "employee");

        ReimbursementResponse reimbursement = reimbursementService
                .getReimbursementById(updateOwnReimbBody.getReimbursementId());

        String autherId = reimbursement.getAuthorId();
        
        String currentStatus = reimbursement.getStatusName();

        if (((UserResponse) userSession.getAttribute("authUser")).getId().equals(autherId) && !currentStatus.equals("pending")) {
            reimbursementService.updateReimbursement(updateOwnReimbBody);
        } else {
            throw new AuthorizationException();
        }

    }

    @PutMapping(value = "/{approveOrDenyBody}",consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approveOrDeny(@RequestBody @PathVariable ApproveOrDenyBody approveOrDenyBody, HttpServletRequest req) {

        logger.info("A PUT request was received by /reimbursement at {}", LocalDateTime.now());

        HttpSession userSession = req.getSession(false);

        SecurityUtils.enforceAuthentication(userSession);
        SecurityUtils.enforcePermissions(userSession, "manager");

        reimbursementService.approveOrDeny(approveOrDenyBody,((UserResponse) userSession.getAttribute("authUser")).getId()); 

    }
}
