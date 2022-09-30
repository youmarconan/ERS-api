package com.revature.reimbursements;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepo extends JpaRepository<ReimbursementStatus, UUID>  {
    
   Optional<ReimbursementStatus> findReimbursementStatusByStatusName(String name);
}
