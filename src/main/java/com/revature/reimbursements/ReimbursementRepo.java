package com.revature.reimbursements;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ReimbursementRepo extends JpaRepository<Reimbursement, UUID> {
    


    @Query(value = "select r from Reimbursement where r.status.name = 1 ", nativeQuery = true)
    List<Reimbursement> findReimbursementByStatus(String statusName);

    // List<Reimbursement> findReimbursementByTypeId_Name(String typeName);

}
