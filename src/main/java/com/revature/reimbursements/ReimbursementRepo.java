package com.revature.reimbursements;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReimbursementRepo extends JpaRepository<Reimbursement, UUID> {
    
    List <Reimbursement> findReimbursementbyStatusName(String statusName);

    List <Reimbursement> findReimbursementbyTypeName(String typeName);

}
