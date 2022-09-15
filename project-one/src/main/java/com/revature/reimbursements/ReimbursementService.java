package com.revature.reimbursements;

import java.util.List;
import java.util.stream.Collectors;

import com.revature.common.ResponseString;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.users.UserDAO;

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

        return reimbursementDAO.findReimbursementbyStatus(type).stream().map(ReimbursementResponse::new).collect(Collectors.toList());
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

    public ResponseString approveOrDeny (ApproveOrDenyBody approveOrDenyBody){

        if (approveOrDenyBody == null) {

            throw new InvalidRequestException("Provided request must not be null!");
        }

        if (approveOrDenyBody.getReimbursementId() == null || approveOrDenyBody.getReimbursementId().length() <= 0){

            throw new InvalidRequestException("Must provide reimbursement ID!");
        }

        if (approveOrDenyBody.getResolverId() == null || approveOrDenyBody.getResolverId().length() <= 0){

            throw new InvalidRequestException("Must provide resolver ID!");
        }

        if(approveOrDenyBody.getStatusId() == null || approveOrDenyBody.getStatusId().length() <= 0) {

                throw new InvalidRequestException("Must provid status ID");
        }

        if (!reimbursementDAO.isIdValid(approveOrDenyBody.getReimbursementId())){

            throw new InvalidRequestException("Must provid a valid reimbursement ID");
        }

        if ( !approveOrDenyBody.getStatusId().equals("2") && !approveOrDenyBody.getStatusId().equals("3")){

            throw new InvalidRequestException("Status value must be (\"2\" or \"3\")  -hint(\"2\" to approve, \"3\" to deny)");
        }

        String updateSuccessfullMessage = reimbursementDAO.approveOrDenyReimbursement(approveOrDenyBody.getStatusId(), approveOrDenyBody.getReimbursementId(), approveOrDenyBody.getResolverId());
        return new ResponseString(updateSuccessfullMessage);
    }

    public ResponseString createNewReimbursement (NewReimbursementRequest newReimbursement){

        UserDAO userDAO = new UserDAO();

        if (newReimbursement == null){
            throw new InvalidRequestException("Provided request must not be null!");
        }

        if ( newReimbursement.getAmount() > 9999.99 || newReimbursement.getAmount() <= 0){
            throw new InvalidRequestException("Provided amount must be between 0.01 and 9999.99");
        }

        if( newReimbursement.getAuthorId() == null || newReimbursement.getAuthorId().length() <= 0 ){
            throw new InvalidRequestException("Must provide author ID");
        }
        if( !userDAO.isIdValid(newReimbursement.getAuthorId()) ){

            throw new InvalidRequestException("Must provide VALID author ID");
        }

        if( !userDAO.isIdActive(newReimbursement.getAuthorId()) ){

            throw new InvalidRequestException("Must provide ACTIVE author ID");
        }

        if( newReimbursement.getDescription() == null || newReimbursement.getDescription().length()<=0){

            throw new InvalidRequestException("Must provide Description");
        }

        if(newReimbursement.getDescription().length() > 65535 ){

            throw new InvalidRequestException("Description must not exceed 65,535 characters");
        }

        if( newReimbursement.getTypeId() == null || newReimbursement.getTypeId().length() <= 0){

            throw new InvalidRequestException("Must provide type ID");
        }
        
        if( !newReimbursement.getTypeId().equals("1") && !newReimbursement.getTypeId().equals("2") && !newReimbursement.getTypeId().equals("3") && !newReimbursement.getTypeId().equals("4") ){

            throw new InvalidRequestException("Type ID must be one of (\"1\" \"2\" \"3\" \"4\") -hint(\"1\" for lodging, \"2\" for travel, \"3\" for food, \"4\" for other");
        }

        Reimbursement reimbursement = newReimbursement.extractEntity();
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

        if(!updateOwnReimbBody.getUpdateTo().equals("1") && !updateOwnReimbBody.getUpdateTo().equals("2") && !updateOwnReimbBody.getUpdateTo().equals("3") && !updateOwnReimbBody.getUpdateTo().equals("4") ){

            throw new InvalidRequestException("Type ID must be one of (\"1\" \"2\" \"3\" \"4\") -hint(\"1\" for lodging, \"2\" for travel, \"3\" for food, \"4\" for other");
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
