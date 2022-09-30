package com.revature.reimbursements;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.users.User;


@Repository
public interface ReimbursementRepo extends JpaRepository<Reimbursement, UUID> {
    

    List<Reimbursement> findReimbursementByStatus(ReimbursementStatus status);

    List<Reimbursement> findReimbursementByType(ReimbursementType type);

    List<Reimbursement> findReimbursementByAuthor(User user);
   
   

}
