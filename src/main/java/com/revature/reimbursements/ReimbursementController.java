package com.revature.reimbursements;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.revature.auth.AuthController;
import com.revature.common.SecurityUtils;
import com.revature.common.exceptions.AuthorizationException;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.users.UserResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reimbursement")
@CrossOrigin(origins="http://localhost:4200/", allowCredentials="true")
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

        SecurityUtils.enforceAuthentication(AuthController.userSession);
        SecurityUtils.enforcePermissions(AuthController.userSession, "manager");

        return reimbursementService.getAllReimbursements();
    }

    @GetMapping(value = "/myReimbs",produces = "application/json")
    public List<ReimbursementResponse> viewMyReimbursements(HttpServletRequest req) {

        logger.info("A GET request was received by /reimbursement/myReimbs at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(AuthController.userSession);
        SecurityUtils.enforcePermissions(AuthController.userSession, "employee");

        String id = ((UserResponse) (AuthController.userSession).getAttribute("loggedInUser")).getId();

        return reimbursementService.viewMyReimbursement(id);
    }

    @GetMapping(value = "/byStatus/{status}", produces = "application/json")
    public List<ReimbursementResponse> getAllReimbursementsByStatus(@PathVariable String status,
            HttpServletRequest req) {

        logger.info("A GET request was received by /reimbursement/byStatus at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(AuthController.userSession);
        SecurityUtils.enforcePermissions(AuthController.userSession, "manager");

        return reimbursementService.getReimbursementsByStatus(status);
    }

    @GetMapping(value = "/byType/{type}", produces = "application/json")
    public List<ReimbursementResponse> getAllReimbursementsByType(@PathVariable String type, HttpServletRequest req) {

        logger.info("A GET request was received by /reimbursement/byType at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(AuthController.userSession);
        SecurityUtils.enforcePermissions(AuthController.userSession, "manager");

        return reimbursementService.getReimbursementsByType(type);
    }

    @GetMapping(value = "/byId/{id}", produces = "application/json")
    public ReimbursementResponse getReimbursementsById(@PathVariable String id, HttpServletRequest req) {

        logger.info("A GET request was received by /reimbursement/byId at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(AuthController.userSession);

        if (SecurityUtils.validateRole(AuthController.userSession, "manager") || SecurityUtils.validateUserId(AuthController.userSession, id)) {
            return reimbursementService.getReimbursementById(id);
        } else {
            throw new AuthorizationException();
        }

    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public String createNewReimbursement(@RequestBody NewReimbursementRequest newReimbursement,
            HttpServletRequest req) {

        logger.info("A POST request was received by /reimbursement at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(AuthController.userSession);
        SecurityUtils.enforcePermissions(AuthController.userSession, "employee");

        String id = ((UserResponse) (AuthController.userSession).getAttribute("loggedInUser")).getId();

        return reimbursementService.createNewReimbursement(newReimbursement, id);
    }

    @PutMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReimbursement(@RequestBody UpdateOwnReimbBody updateOwnReimbBody, HttpServletRequest req) {

        logger.info("A PUT request was received by /reimbursement at {}", LocalDateTime.now());

        SecurityUtils.enforceAuthentication(AuthController.userSession);
        SecurityUtils.enforcePermissions(AuthController.userSession, "employee");

        ReimbursementResponse reimbursement = null;
        if (updateOwnReimbBody.getReimbursementId() != null) {

            reimbursement = reimbursementService
                    .getReimbursementById(updateOwnReimbBody.getReimbursementId());
        } else {
            throw new InvalidRequestException();
        }

        System.out.println("\n" + reimbursement +"\n" );

        String autherId = reimbursement.getAuthorId();

        String currentStatus = reimbursement.getStatusName();

        String loggedInUserId = ((UserResponse) (AuthController.userSession).getAttribute("loggedInUser")).getId();

        if (loggedInUserId.equals(autherId) && currentStatus.equals("pending")) {
            reimbursementService.updateReimbursement(updateOwnReimbBody);
        } else {
            throw new AuthorizationException();
        }

    }

    @PutMapping(value = "/manager", consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approveOrDeny(@RequestBody ApproveOrDenyBody approveOrDenyBody, HttpServletRequest req) {

        logger.info("A PUT request was received by /reimbursement at {}",
                LocalDateTime.now());

        SecurityUtils.enforceAuthentication(AuthController.userSession);
        SecurityUtils.enforcePermissions(AuthController.userSession, "manager");

        String resolverId = ((UserResponse) (AuthController.userSession).getAttribute("loggedInUser")).getId();

        reimbursementService.approveOrDeny(approveOrDenyBody,resolverId);

    }
}
