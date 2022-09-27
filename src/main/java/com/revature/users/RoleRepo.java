package com.revature.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<UserRole, UUID> {

}
