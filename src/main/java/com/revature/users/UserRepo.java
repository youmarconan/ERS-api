package com.revature.users;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


    Optional<User> findUserByUsernameAndPassword(String username, String password);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

}
