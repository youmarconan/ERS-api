package com.revature.reimbursements;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.common.exceptions.InvalidRequestException;
import com.revature.common.exceptions.ResourceNotFoundException;
import com.revature.users.User;
import com.revature.users.UserRepo;

@Service
public class ReimbursementService {

    private final ReimbursementRepo reimbursementRepo;
    private final UserRepo userRepo;
    private final StatusRepo statusRepo;
    private final TypeRepo typeRepo;

    @Autowired
    public ReimbursementService(ReimbursementRepo reimbursementRepo, UserRepo userRepo, StatusRepo statusRepo,
            TypeRepo typeRepo) {
        this.reimbursementRepo = reimbursementRepo;
        this.userRepo = userRepo;
        this.statusRepo = statusRepo;
        this.typeRepo = typeRepo;
    }

    public List<ReimbursementResponse> getAllReimbursements() {

        return reimbursementRepo.findAll().stream().map(ReimbursementResponse::new)
                .collect(Collectors.toList());

    }

    public List<ReimbursementResponse> viewMyReimbursement(String authorId) {

        User author = userRepo.getById(UUID.fromString(authorId));


        return reimbursementRepo.findReimbursementByAuthor(author).stream().map(ReimbursementResponse::new)
                .collect(Collectors.toList());

    }

    public List<ReimbursementResponse> getReimbursementsByStatus(String name) {

        if (name == null || name.length() <= 0) {
            throw new InvalidRequestException("A non-empty status must be provided!");
        }

        if (!name.equals("pending") && !name.equals("approved") && !name.equals("denied")) {
            throw new InvalidRequestException("Status value must be one of (pending, approved, denied)");
        }

        ReimbursementStatus status = statusRepo.findReimbursementStatusByStatusName(name)
                .orElseThrow(ResourceNotFoundException::new);

        return reimbursementRepo.findReimbursementByStatus(status).stream().map(ReimbursementResponse::new)
                .collect(Collectors.toList());
    }

    public List<ReimbursementResponse> getReimbursementsByType(String name) {

        if (name == null || name.length() <= 0) {
            throw new InvalidRequestException("A non-empty type must be provided!");
        }

        if (!name.equals("lodging") && !name.equals("travel") && !name.equals("food")
                && !name.equals("other")) {
            throw new InvalidRequestException("Status value must be one of (lodging,travel, food, other)");
        }

        ReimbursementType type = typeRepo.findReimbursementTypeByTypeName(name)
                .orElseThrow(ResourceNotFoundException::new);

        return reimbursementRepo.findReimbursementByType(type).stream().map(ReimbursementResponse::new)
                .collect(Collectors.toList());
    }

    public ReimbursementResponse getReimbursementById(String id) {

        try {
            return reimbursementRepo.findById(UUID.fromString(id))
                    .map(ReimbursementResponse::new)
                    .orElseThrow(ResourceNotFoundException::new);

        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("An invalid ID string was provided.");
        }
    }

    @Transactional
    public void approveOrDeny(ApproveOrDenyBody approveOrDenyBody, String resolverId) {

        if (approveOrDenyBody == null) {

            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (approveOrDenyBody.getReimbursementId() == null || approveOrDenyBody.getReimbursementId().length() <= 0) {

            throw new InvalidRequestException("Must provide reimbursement ID!");
        }

        if (approveOrDenyBody.getStatusName() == null || approveOrDenyBody.getStatusName().length() <= 0) {

            throw new InvalidRequestException("Must provid status Name");
        }

        if (!reimbursementRepo.existsById(UUID.fromString(approveOrDenyBody.getReimbursementId()))) {

            throw new InvalidRequestException("Must provid a valid reimbursement ID");
        }

        if (!approveOrDenyBody.getStatusName().equals("approve") && !approveOrDenyBody.getStatusName().equals("deny")) {

            throw new InvalidRequestException("Status value must be (approve or deny)");
        }

        String statusId = null;
        if (approveOrDenyBody.getStatusName().equals("approve")) {
            statusId = "ecff108b-af77-4c32-83b2-06ebbb3e7d90";
        }
        if (approveOrDenyBody.getStatusName().equals("deny")) {
            statusId = "c7de8d84-80e3-49b9-931e-e9099c9ccd73";
        }

        Reimbursement reimbursement = reimbursementRepo
                .findById(UUID.fromString(approveOrDenyBody.getReimbursementId()))
                .orElseThrow(ResourceNotFoundException::new);

        ReimbursementStatus reimbursementStatus = statusRepo.findById(UUID.fromString(statusId))
                .orElseThrow(ResourceNotFoundException::new);
        User resolver = userRepo.findById(UUID.fromString(resolverId)).orElseThrow(ResourceNotFoundException::new);

        reimbursement.setStatus(reimbursementStatus);
        reimbursement.setResolved(LocalDateTime.now());
        reimbursement.setResolver(resolver);

    }

    public String createNewReimbursement(NewReimbursementRequest newReimbursement, String authorId) {

        if (newReimbursement == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (newReimbursement.getAmount() >= 9999.99 || newReimbursement.getAmount() <= 0) {
            throw new InvalidRequestException("Provided amount must be between 0.01 and 9999.99");
        }

        if (newReimbursement.getDescription() == null || newReimbursement.getDescription().length() <= 0) {

            throw new InvalidRequestException("Must provide Description");
        }

        if (newReimbursement.getDescription().length() > 65535) {

            throw new InvalidRequestException("Description must not exceed 65,535 characters");
        }

        ReimbursementType reimbursementType = typeRepo.findById(UUID.fromString("cadb6fcb-06ab-4583-b467-711185626cb7"))
                .orElseThrow(ResourceNotFoundException::new);

        if (newReimbursement.getType() != null) {

            if (newReimbursement.getType().equals("lodging")) {

                reimbursementType = typeRepo.findById(UUID.fromString("3401d663-0c60-4389-ae8b-34ee3be05e07"))
                        .orElseThrow(ResourceNotFoundException::new);

            }
            if (newReimbursement.getType().equals("travel")) {

                reimbursementType = typeRepo.findById(UUID.fromString("c59565bd-504d-4ec3-a95f-b4714f90174f"))
                        .orElseThrow(ResourceNotFoundException::new);

            }
            if (newReimbursement.getType().equals("food")) {

                reimbursementType = typeRepo.findById(UUID.fromString("ff73b3a8-89ce-4d76-976c-267a35f9e712"))
                        .orElseThrow(ResourceNotFoundException::new);

            }

        }

        ReimbursementStatus reimbursementStatus = statusRepo
                .findById(UUID.fromString("41301461-daf4-41d1-91e1-767eb87c398d"))
                .orElseThrow(ResourceNotFoundException::new);

        Reimbursement reimbursement = newReimbursement.extractEntity();

        reimbursement.setType(reimbursementType);
        reimbursement.setStatus(reimbursementStatus);
        reimbursement.setSubmitted(LocalDateTime.now());

        User author = userRepo.findById(UUID.fromString(authorId)).orElseThrow(ResourceNotFoundException::new);

        reimbursement.setAuthor(author);

        reimbursementRepo.save(reimbursement);

        return "Successfully submitted new reimbursement with ID = " + reimbursement.getId().toString();
    }

    @Transactional
    public void updateReimbursement(UpdateOwnReimbBody updateOwnReimbBody) {

        if (updateOwnReimbBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        Reimbursement reimbursement = reimbursementRepo
                .findById(UUID.fromString(updateOwnReimbBody.getReimbursementId()))
                .orElseThrow(ResourceNotFoundException::new);
        

        if (String.valueOf(updateOwnReimbBody.getAmount()) != null) {

            if (updateOwnReimbBody.getAmount() > 9999.99 || updateOwnReimbBody.getAmount() <= 0) {
                throw new InvalidRequestException("Provided amount must be between 0.01 and 9999.99");
            } else {
                reimbursement.setAmount(updateOwnReimbBody.getAmount());
            }
        }

        if (updateOwnReimbBody.getDescription() != null) {
            if (updateOwnReimbBody.getDescription().length() > 65535) {

                throw new InvalidRequestException("Description must not exceed 65,535 characters");
            } else {
                reimbursement.setDescription(updateOwnReimbBody.getDescription());
            }
        }

        if (updateOwnReimbBody.getType() != null) {

            if (!updateOwnReimbBody.getType().equals("lodging") && !updateOwnReimbBody.getType().equals("travel")
                    && !updateOwnReimbBody.getType().equals("food") && !updateOwnReimbBody.getType().equals("other")) {

                throw new InvalidRequestException("Reimbursement type must be one of (lodging, travel, food, other)");

            } else {

                UUID id = null;
                if (updateOwnReimbBody.getType().equals("lodging")) {
                    id = UUID.fromString("3401d663-0c60-4389-ae8b-34ee3be05e07");
                }
                if (updateOwnReimbBody.getType().equals("travel")) {
                    id = UUID.fromString("c59565bd-504d-4ec3-a95f-b4714f90174f");
                }
                if (updateOwnReimbBody.getType().equals("food")) {
                    id = UUID.fromString("ff73b3a8-89ce-4d76-976c-267a35f9e712");
                }
                if (updateOwnReimbBody.getType().equals("other")) {
                    id = UUID.fromString("cadb6fcb-06ab-4583-b467-711185626cb7");
                }

                ReimbursementType type = typeRepo.findById(id).orElseThrow(ResourceNotFoundException::new);
                reimbursement.setType(type);
            }
        }

    }
}
