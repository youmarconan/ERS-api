package com.revature.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.revature.common.exceptions.AuthenticationException;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.users.User;
import com.revature.users.UserDAO;
import com.revature.users.UserResponse;
import com.revature.users.UserRole;

public class AuthServiceTest {

    AuthService sut;
    UserDAO mockUserDAO;


    @BeforeEach
    public void setup(){
        mockUserDAO = Mockito.mock(UserDAO.class);
        sut = new AuthService(mockUserDAO);
    }
    
    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockUserDAO); 
    }

    @Test
    public void test_authenticate_returnsSuccessfully_givenValidAndKnownCredentialsAndActiveUser() {

        // Arrange
        Credentials credentialsStub = new Credentials("valid", "credentials");

        User userStub = new User("id", "username", "email", "password", "first", "last", true, new UserRole("role id", "role name"));

        when(mockUserDAO.login(anyString(), anyString())).thenReturn(Optional.of(userStub));
        UserResponse expectedResult = new UserResponse(userStub);

        
        // Act
        UserResponse actualResult = sut.authenticate(credentialsStub);

        // Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult); // PLEASE NOTE: the objects you are comparing need to have a .equals method
        verify(mockUserDAO, times(1)).login(anyString(), anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenShortPassword() {

        // Arrange
        Credentials credentialsStub = new Credentials("username", "x");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        verify(mockUserDAO, times(0)).login(anyString(), anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenShortUsername() {

        // Arrange
        Credentials credentialsStub = new Credentials("x", "Password");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        verify(mockUserDAO, times(0)).login(anyString(), anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenNullCredentials() {

        // Arrange
        Credentials credentialsStub =null;

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        verify(mockUserDAO, times(0)).login(anyString(), anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenNonActiveUser() {

        // Arrange
        Credentials credentialsStub = new Credentials("username", "password");
        User userStub = new User("id", "username", "email", "password", "first", "last", false, new UserRole("role id", "role name"));
        

        when(mockUserDAO.login(anyString(), anyString())).thenReturn(Optional.of(userStub));

        
        // Act & Assert
        assertThrows(AuthenticationException.class, () -> {
            sut.authenticate(credentialsStub);
        });

        verify(mockUserDAO, times(1)).login(anyString(), anyString());

    }



}
