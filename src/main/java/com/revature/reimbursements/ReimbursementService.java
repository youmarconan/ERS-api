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

@Service
public class ReimbursementService {

    private final ReimbursementRepo reimbursementRepo;

    @Autowired
    public ReimbursementService(ReimbursementRepo reimbursementRepo) {
        this.reimbursementRepo = reimbursementRepo;
    }

    public List<ReimbursementResponse> getAllReimbursements() {

        return reimbursementRepo.findAll().stream().map(ReimbursementResponse::new)
                .collect(Collectors.toList());

    }

    public List<ReimbursementResponse> getReimbursementsByStatus(String status) {

        if (status == null || status.length() <= 0) {
            throw new InvalidRequestException("A non-empty status must be provided!");
        }

        if (!status.equals("pending") && !status.equals("approved") && !status.equals("denied")) {
            throw new InvalidRequestException("Status value must be one of (pending, approved, denied)");
        }

        return reimbursementRepo.findReimbursementbyStatusName(status).stream().map(ReimbursementResponse::new)
                .collect(Collectors.toList());
    }

    public List<ReimbursementResponse> getReimbursementsByType(String type) {

        if (type == null || type.length() <= 0) {
            throw new InvalidRequestException("A non-empty type must be provided!");
        }

        if (!type.equals("lodging") && !type.equals("travel") && !type.equals("food") && !type.equals("other")) {
            throw new InvalidRequestException("Status value must be one of (lodging, travel, food, other)");
        }

        return reimbursementRepo.findReimbursementbyTypeName(type).stream().map(ReimbursementResponse::new)
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
        ReimbursementStatus reimbursementStatus = new ReimbursementStatus();
        reimbursementStatus.setStatusId(UUID.fromString(statusId));

        reimbursement.setStatus(reimbursementStatus);
        reimbursement.setResolved(LocalDateTime.now());
        reimbursement.setResolverId(UUID.fromString(resolverId));

    }

    public String createNewReimbursement(NewReimbursementRequest newReimbursement, String authorId) {

        if (newReimbursement == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (newReimbursement.getAmount() > 9999.99 || newReimbursement.getAmount() <= 0) {
            throw new InvalidRequestException("Provided amount must be between 0.01 and 9999.99");
        }

        if (newReimbursement.getDescription() == null || newReimbursement.getDescription().length() <= 0) {

            throw new InvalidRequestException("Must provide Description");
        }

        if (newReimbursement.getDescription().length() > 65535) {

            throw new InvalidRequestException("Description must not exceed 65,535 characters");
        }

        if (!newReimbursement.getType().equals("lodging") && !newReimbursement.getType().equals("travel")
                && !newReimbursement.getType().equals("food") && !newReimbursement.getType().equals("other")) {

            throw new InvalidRequestException("Reimbursement type must be one of (lodging, travel, food, other)");
        }

        if (newReimbursement.getType().equals("lodging")) {
            newReimbursement.setType("3401d663-0c60-4389-ae8b-34ee3be05e07");
        }
        if (newReimbursement.getType().equals("travel")) {
            newReimbursement.setType("c59565bd-504d-4ec3-a95f-b4714f90174f");
        }
        if (newReimbursement.getType().equals("food")) {
            newReimbursement.setType("ff73b3a8-89ce-4d76-976c-267a35f9e712");
        }
        if (newReimbursement.getType().equals("other") || newReimbursement.getType().equals(null)) {
            newReimbursement.setType("cadb6fcb-06ab-4583-b467-711185626cb7");
        }

        Reimbursement reimbursement = newReimbursement.extractEntity();
        reimbursement.setAuthorId(UUID.fromString(authorId));
        reimbursementRepo.save(reimbursement);

        return "Successfully submitted new reimbursement with ID = " + reimbursement.getId().toString();
    }

    @Transactional
    public void updateReimbursement(UpdateOwnReimbBody updateOwnReimbBody) {

        if (updateOwnReimbBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateOwnReimbBody.getAmount() > 9999.99 || updateOwnReimbBody.getAmount() <= 0) {
            throw new InvalidRequestException("Provided amount must be between 0.01 and 9999.99");
        }

        if (updateOwnReimbBody.getDescription().length() > 65535) {

            throw new InvalidRequestException("Description must not exceed 65,535 characters");
        }

        if (!updateOwnReimbBody.getType().equals("lodging") && !updateOwnReimbBody.getType().equals("travel")
                && !updateOwnReimbBody.getType().equals("food") && !updateOwnReimbBody.getType().equals("other")) {

            throw new InvalidRequestException("Reimbursement type must be one of (lodging, travel, food, other)");
        }

        Reimbursement reimbursement = reimbursementRepo
                .findById(UUID.fromString(updateOwnReimbBody.getReimbursementId()))
                .orElseThrow(ResourceNotFoundException::new);

        if(!String.valueOf(updateOwnReimbBody.getAmount()).equals(null)){
            reimbursement.setAmount(updateOwnReimbBody.getAmount());
        }

        if(!updateOwnReimbBody.getDescription().equals(null)){
            reimbursement.setDescription(updateOwnReimbBody.getDescription());
        }
        
        if(!updateOwnReimbBody.getType().equals(null)){

            ReimbursementType type = new ReimbursementType();

            if (updateOwnReimbBody.getType().equals("lodging")) {
                type.setTypeId(UUID.fromString("3401d663-0c60-4389-ae8b-34ee3be05e07"));
            }
            if (updateOwnReimbBody.getType().equals("travel")) {
                type.setTypeId(UUID.fromString("c59565bd-504d-4ec3-a95f-b4714f90174f"));
            }
            if (updateOwnReimbBody.getType().equals("food")) {
                type.setTypeId(UUID.fromString("ff73b3a8-89ce-4d76-976c-267a35f9e712"));
            }
            if (updateOwnReimbBody.getType().equals("other")) {
                type.setTypeId(UUID.fromString("cadb6fcb-06ab-4583-b467-711185626cb7"));
            }
            
            reimbursement.setType(type);
        }
    }
}
