package com.revature.reimbursements;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepo extends JpaRepository<ReimbursementType, UUID>  {
    
    Optional<ReimbursementType> findReimbursementTypeByTypeName(String name);
}
