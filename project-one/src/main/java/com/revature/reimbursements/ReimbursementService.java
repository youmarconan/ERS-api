package com.revature.reimbursements;

import java.util.List;
import java.util.stream.Collectors;

import com.revature.common.ResponseString;
import com.revature.common.exceptions.InvalidRequestException;
public class ReimbursementService {

    private final ReimbursementDAO reimbursementDAO;

    public ReimbursementService ( ReimbursementDAO reimbursementDAO){
        this.reimbursementDAO = reimbursementDAO;
    }

    public List <ReimbursementResponse> getAllReimbursements(){

        return reimbursementDAO.getAllReimbursements().stream().map(ReimbursementResponse::new).collect(Collectors.toList());

    }

    public List <ReimbursementResponse> getReimbursementsByStatus(String status){

        if (status==null || status.length() <= 0){
            throw new InvalidRequestException("A non-empty status must be provided!");
        }

        if ( !status.equals("pending") && !status.equals("approved") && !status.equals("denied")){
            throw new InvalidRequestException("Status value must be one of (pending/approved/denied)");
        }

        return reimbursementDAO.findReimbursementbyStatus(status).stream().map(ReimbursementResponse::new).collect(Collectors.toList());
    }

    public List <ReimbursementResponse> getReimbursementsByType(String type){

        if (type==null || type.length() <= 0){
            throw new InvalidRequestException("A non-empty type must be provided!");
        }

        if ( !type.equals("lodging") && !type.equals("travel") && !type.equals("food") && !type.equals("other")){
            throw new InvalidRequestException("Status value must be one of (lodging/travel/food/other)");
        }

        return reimbursementDAO.findReimbursementbyType(type).stream().map(ReimbursementResponse::new).collect(Collectors.toList());
    }

    public ReimbursementResponse getReimbursementById(String id) {

        if (id == null || id.length() <= 0) {
            throw new InvalidRequestException("A non-empty ID must be provided!");
        }

        try {
            return reimbursementDAO.findReimbursementById(id)
                          .map(ReimbursementResponse::new)
                          .orElseThrow(InvalidRequestException::new);

        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("An invalid ID string was provided.");
        }
    }

    public ResponseString approveOrDeny (ApproveOrDenyBody approveOrDenyBody, String resolverId){

        if (approveOrDenyBody == null) {

            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (approveOrDenyBody.getReimbursementId() == null || approveOrDenyBody.getReimbursementId().length() <= 0){

            throw new InvalidRequestException("Must provide reimbursement ID!");
        }

        if (resolverId == null || resolverId.length() <= 0){

            throw new InvalidRequestException("Must provide resolver ID!");
        }

        if(approveOrDenyBody.getStatusName() == null || approveOrDenyBody.getStatusName().length() <= 0) {

                throw new InvalidRequestException("Must provid status Name");
        }

        if (!reimbursementDAO.isIdValid(approveOrDenyBody.getReimbursementId())){

            throw new InvalidRequestException("Must provid a valid reimbursement ID");
        }

        if ( !approveOrDenyBody.getStatusName().equals("approve") && !approveOrDenyBody.getStatusName().equals("deny")){

            throw new InvalidRequestException("Status value must be (approve/deny)");
        }

        String statusId = null ;
        if (approveOrDenyBody.getStatusName().equals("approve")){
            statusId = "2";
        }
        if (approveOrDenyBody.getStatusName().equals("deny")){
            statusId = "3";
        }

        String updateSuccessfullMessage = reimbursementDAO.approveOrDenyReimbursement(statusId, approveOrDenyBody.getReimbursementId(), resolverId);
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString createNewReimbursement (NewReimbursementRequest newReimbursement, String authorId){

        if (newReimbursement == null){
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if ( newReimbursement.getAmount() > 9999.99 || newReimbursement.getAmount() <= 0){
            throw new InvalidRequestException("Provided amount must be between 0.01 and 9999.99");
        }


        if( newReimbursement.getDescription() == null || newReimbursement.getDescription().length()<=0){

            throw new InvalidRequestException("Must provide Description");
        }

        if(newReimbursement.getDescription().length() > 65535 ){

            throw new InvalidRequestException("Description must not exceed 65,535 characters");
        }

        if( newReimbursement.getType() == null || newReimbursement.getType().length() <= 0){

            throw new InvalidRequestException("Must provide type ID");
        }
        
        if(!newReimbursement.getType().equals("lodging") && !newReimbursement.getType().equals("travel") && !newReimbursement.getType().equals("food") && !newReimbursement.getType().equals("other") ){

            throw new InvalidRequestException("Reimbursement type must be one of (lodging/travel/food/other)");
        }

        if(newReimbursement.getType().equals("lodging")){
            newReimbursement.setType("1");
        }
        if(newReimbursement.getType().equals("travel")){
            newReimbursement.setType("2");
        }
        if(newReimbursement.getType().equals("food")){
            newReimbursement.setType("3");
        }
        if(newReimbursement.getType().equals("other")){
            newReimbursement.setType("4");
        }

        Reimbursement reimbursement = newReimbursement.extractEntity();
        reimbursement.setAuthorId(authorId);
        String reimbursementId = reimbursementDAO.createNewReimbursement(reimbursement);
        return new ResponseString(reimbursementId);
    }

    public ResponseString updateAmount (UpdateOwnReimbBody updateOwnReimbBody){

        if (updateOwnReimbBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateOwnReimbBody.getUpdateTo() == null || updateOwnReimbBody.getUpdateTo().length() <= 0 ||
        updateOwnReimbBody.getReimbursementId() == null || updateOwnReimbBody.getReimbursementId().length() <= 0) {

                throw new InvalidRequestException("Must provid amount and reimbursement ID");
            }

        if (!reimbursementDAO.isIdValid(updateOwnReimbBody.getReimbursementId())){

            throw new InvalidRequestException("Must provid a valid reimbursement ID");
        }

        if (Double.parseDouble(updateOwnReimbBody.getUpdateTo()) > 9999.99 || Double.parseDouble(updateOwnReimbBody.getUpdateTo()) <= 0){
            throw new InvalidRequestException("Provided amount must be between 0.01 and 9999.99");
        }

        String updateSuccessfullMessage = reimbursementDAO.updateOwnreimbursementAmount(updateOwnReimbBody);
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString updateTypeId (UpdateOwnReimbBody updateOwnReimbBody){

        if (updateOwnReimbBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateOwnReimbBody.getUpdateTo() == null || updateOwnReimbBody.getUpdateTo().length() <= 0 ||
        updateOwnReimbBody.getReimbursementId() == null || updateOwnReimbBody.getReimbursementId().length() <= 0) {

                throw new InvalidRequestException("Must provid amount and reimbursement ID");
            }

        if (!reimbursementDAO.isIdValid(updateOwnReimbBody.getReimbursementId())){

            throw new InvalidRequestException("Must provid a valid reimbursement ID");
        }

        if(!updateOwnReimbBody.getUpdateTo().equals("lodging") && !updateOwnReimbBody.getUpdateTo().equals("travel") && !updateOwnReimbBody.getUpdateTo().equals("food") && !updateOwnReimbBody.getUpdateTo().equals("other") ){

            throw new InvalidRequestException("Reimbursement type must be one of (lodging/travel/food/other)");
        }

        if(updateOwnReimbBody.getUpdateTo().equals("lodging")){
            updateOwnReimbBody.setUpdateTo("1");
        }
        if(updateOwnReimbBody.getUpdateTo().equals("travel")){
            updateOwnReimbBody.setUpdateTo("2");
        }
        if(updateOwnReimbBody.getUpdateTo().equals("food")){
            updateOwnReimbBody.setUpdateTo("3");
        }
        if(updateOwnReimbBody.getUpdateTo().equals("other")){
            updateOwnReimbBody.setUpdateTo("4");
        }


        String updateSuccessfullMessage = reimbursementDAO.updateOwnreimbursementTypeId(updateOwnReimbBody);
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString updateDescription (UpdateOwnReimbBody updateOwnReimbBody){

        if (updateOwnReimbBody == null) {
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (updateOwnReimbBody.getUpdateTo() == null || updateOwnReimbBody.getUpdateTo().length() <= 0 ||
        updateOwnReimbBody.getReimbursementId() == null || updateOwnReimbBody.getReimbursementId().length() <= 0) {

                throw new InvalidRequestException("Must provid amount and reimbursement ID");
            }

        if (!reimbursementDAO.isIdValid(updateOwnReimbBody.getReimbursementId())){

            throw new InvalidRequestException("Must provid a valid reimbursement ID");
        }

        if(updateOwnReimbBody.getUpdateTo().length() > 65535 ){

            throw new InvalidRequestException("Description must not exceed 65,535 characters");
        }
      

        String updateSuccessfullMessage = reimbursementDAO.updateOwnreimbursementDescription(updateOwnReimbBody);
        return new ResponseString(updateSuccessfullMessage);
    }
}
