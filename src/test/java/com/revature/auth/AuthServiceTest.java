package com.revature.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.revature.common.exceptions.AuthenticationException;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.users.User;
import com.revature.users.UserRepo;
import com.revature.users.UserResponse;
import com.revature.users.UserRole;

public class AuthServiceTest {

    AuthService sut;
    UserRepo userRepo;


    @BeforeEach
    public void setup(){
        userRepo = Mockito.mock(UserRepo.class);
        sut = new AuthService(userRepo);
    }
    
    @AfterEach
    public void cleanUp() {
        Mockito.reset(userRepo); 
    }

    @Test
    public void test_authenticate_returnsSuccessfully_givenValidAndKnownCredentialsAndActiveUser() {

        // Arrange
        Credentials credentialsStub = new Credentials("valid", "credentials");

        User userStub = new User(UUID.randomUUID(), "username", "email", "password", "firstName", "lastName", true, new UserRole(UUID.randomUUID(), "name"));

        when(userRepo.findUserByUsernameAndPassword(credentialsStub.getUsername(),credentialsStub.getPassword())).thenReturn(Optional.of(userStub));
        UserResponse expectedResult = new UserResponse(userStub);

        
        // Act
        UserResponse actualResult = sut.authenticate(credentialsStub);

        // Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult); // PLEASE NOTE: the objects you are comparing need to have a .equals method
        verify(userRepo, times(1)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenShortPassword() {

        // Arrange
        Credentials credentialsStub = new Credentials("username", "x");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        verify(userRepo, times(0)).findUserByUsernameAndPassword(anyString(),anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenShortUsername() {

        // Arrange
        Credentials credentialsStub = new Credentials("x", "Password");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        verify(userRepo, times(0)).findUserByUsernameAndPassword(anyString(),anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenNullCredentials() {

        // Arrange
        Credentials credentialsStub =null;

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        verify(userRepo, times(0)).findUserByUsernameAndPassword(anyString(),anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenNonActiveUser() {

        // Arrange
        Credentials credentialsStub = new Credentials("username", "password");
        User userStub = new User(UUID.randomUUID(), "username", "email", "password", "first", "last", false, new UserRole(UUID.randomUUID(), "role name"));
        

        when(userRepo.findUserByUsernameAndPassword(anyString(),anyString())).thenReturn(Optional.of(userStub));

        
        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        verify(userRepo, times(1)).findUserByUsernameAndPassword(anyString(),anyString());


    }

    @Test
    public void test_authenticate_throwsAuthenticationException_givenValidUnknownCredentials() {

        // Arrange
        Credentials credentialsStub = new Credentials("unknown", "credentials");
        when(userRepo.findUserByUsernameAndPassword(anyString(),anyString())).thenReturn(Optional.empty());

        // Act
        assertThrows(AuthenticationException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        // Assert
        verify(userRepo, times(1)).findUserByUsernameAndPassword(anyString(),anyString());

    }
}
