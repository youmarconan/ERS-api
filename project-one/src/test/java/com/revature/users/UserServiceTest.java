package com.revature.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.revature.common.ResponseString;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.common.exceptions.IsAlreadyExist;


public class UserServiceTest {
    
    UserService sut;
    UserDAO mockUserDAO;
    NewUserRequest mockNewUserRequest;

    @BeforeEach
    public void setup(){
        mockUserDAO = Mockito.mock(UserDAO.class);
        mockNewUserRequest = Mockito.mock(NewUserRequest.class);
        sut = new UserService(mockUserDAO);
    }
    
    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockUserDAO); 
    }

    @Test
    public void test_getAllUsers_returnsSuccessfully_givenNoArgs() {

        // Arrange
        ArrayList<User> usersStub = new ArrayList<>();
        User user1 = new User("id1", "username1", "email1", "password1", "firstName1", "lastName1", true, new UserRole());
        User user2 = new User("id2", "username2", "email2", "password2", "firstName2", "lastName2", true, new UserRole());
        User user3 = new User("id3", "username3", "email3", "password3", "firstName3", "lastName3", true, new UserRole());

        usersStub.add(user1);
        usersStub.add(user2);
        usersStub.add(user3);

        UserResponse x = new UserResponse(user1);
        UserResponse y = new UserResponse(user2);
        UserResponse z = new UserResponse(user3);

        List <UserResponse> userResponses = new ArrayList<>();
        userResponses.add(x);
        userResponses.add(y);
        userResponses.add(z);

        when(mockUserDAO.allUsers()).thenReturn(usersStub);
        List<UserResponse> expected =  userResponses;

        // Act
        List<UserResponse> actual = sut.getAllUsers();

        // Assert

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockUserDAO, times(1)).allUsers();

    }


    @Test
    public void test_getUserById_returnsSuccessfully_givenValidId() {

        // Arrange
        String id = "moreThan0";
        User mockUser = new User("id", "username", "email", "password", "firstName", "lastName", true, new UserRole());
        UserResponse expected = new UserResponse(mockUser);

        when(mockUserDAO.findUserById(id)).thenReturn(Optional.of(mockUser));


        // Act
        UserResponse actual = sut.getUserById(id);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockUserDAO, times(1)).findUserById(id);
    }
    
    @Test
    public void test_getUserById_throwsInvalidRequestException_givenInvalidId() {

        // Arrange
        String id = "invalid";
       
        when(mockUserDAO.findUserById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserById(id);
        });

        verify(mockUserDAO, times(1)).findUserById(id);
    }

    @Test
    public void test_getUserById_throwsInvalidRequestException_throwingIllegalArgumentException() {

        // Arrange
        String id = "invalid";
       
        when(mockUserDAO.findUserById(id)).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserById(id);
        });

        verify(mockUserDAO, times(1)).findUserById(id);
    }

    @Test
    public void test_getUserById_throwsInvalidRequestException_givenNullId() {

        // Arrange
        String id = null;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserById(id);
        });

        verify(mockUserDAO, times(0)).findUserById(id);

    }
    
    @Test
    public void test_getUserById_throwsInvalidRequestException_givenZeroLengthId() {

        // Arrange
        String id = "";
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserById(id);
        });

        verify(mockUserDAO, times(0)).findUserById(id);

    }

    @Test
    public void test_getUserByUsername_returnsSuccessfully_givenValidUsername() {

        // Arrange
        String username = "moreThan0";
        User mockUser = new User("id", "username", "email", "password", "firstName", "lastName", true, new UserRole());
        UserResponse expected = new UserResponse(mockUser);

        when(mockUserDAO.findUserByUsername(username)).thenReturn(Optional.of(mockUser));


        // Act
        UserResponse actual = sut.getUserByUsername(username);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockUserDAO, times(1)).findUserByUsername(username);
    }
    
    @Test
    public void test_getUserByUSername_throwsInvalidRequestException_givenInvalidUsername() {

        // Arrange
        String username = "invalid";
       
        when(mockUserDAO.findUserByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByUsername(username);
        });

        verify(mockUserDAO, times(1)).findUserByUsername(username);
    }

    @Test
    public void test_getUserByUsername_throwsInvalidRequestException_throwingIllegalArgumentException() {

        // Arrange
        String username = "invalid";
       
        when(mockUserDAO.findUserByUsername(username)).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByUsername(username);
        });

        verify(mockUserDAO, times(1)).findUserByUsername(username);
    }

    @Test
    public void test_getUserByUsername_throwsInvalidRequestException_givenNullUsername() {

        // Arrange
        String username = null;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByUsername(username);
        });

        verify(mockUserDAO, times(0)).findUserByUsername(username);

    }
    
    @Test
    public void test_getUserByUsername_throwsInvalidRequestException_givenZeroLengthUsername() {

        // Arrange
        String username = "";
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByUsername(username);
        });
    
        verify(mockUserDAO, times(0)).findUserByUsername(username);
    }

    @Test
    public void test_getUserByEmail_returnsSuccessfully_givenValidEmail() {

        // Arrange
        String email = "moreThan0";
        User mockUser = new User("id", "username", "email", "password", "firstName", "lastName", true, new UserRole());
        UserResponse expected = new UserResponse(mockUser);

        when(mockUserDAO.findUserByEmail(email)).thenReturn(Optional.of(mockUser));


        // Act
        UserResponse actual = sut.getUserByEmail(email);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockUserDAO, times(1)).findUserByEmail(email);
    }
    
    @Test
    public void test_getUserByEmail_throwsInvalidRequestException_givenInvalidEmail() {

        // Arrange
        String email = "invalid";
       
        when(mockUserDAO.findUserByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByEmail(email);
        });

        verify(mockUserDAO, times(1)).findUserByEmail(email);
    }

    @Test
    public void test_getUserByEmail_throwsInvalidRequestException_throwingIllegalArgumentException() {

        // Arrange
        String email = "invalid";
       
        when(mockUserDAO.findUserByEmail(email)).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByEmail(email);
        });

        verify(mockUserDAO, times(1)).findUserByEmail(email);
    }

    @Test
    public void test_getUserByEmail_throwsInvalidRequestException_givenNullEmail() {

        // Arrange
        String email = null;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByEmail(email);
        });

        verify(mockUserDAO, times(0)).findUserByEmail(email);

    }
    
    @Test
    public void test_getUserByEmail_throwsInvalidRequestException_givenZeroLengthEmail() {

        // Arrange
        String email = "";
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByEmail(email);
        });
    
        verify(mockUserDAO, times(0)).findUserByEmail(email);
    }

    //TODO ask wezley tomorrow
    // @Test
    // public void test_register_returnsSuccessfully_givenValidNewUserRequest() {

    //     // Arrange
       
    //     UserRole role = new UserRole();
    //     role.setId("userRoleId");
    //     User user = new User("id", "username", "email", "password", "firstName", "lastName", true, role);
    
    //     mockNewUserRequest.setFirstName("firstName");
    //     mockNewUserRequest.setLastName("lastName");
    //     mockNewUserRequest.setEmail("email");
    //     mockNewUserRequest.setUsername("username");
    //     mockNewUserRequest.setPassword("password");
    //     mockNewUserRequest.setIsActive(true);
    //     mockNewUserRequest.setUserRoleId("id");

    //     when(mockUserDAO.isEmailTaken(mockNewUserRequest.getEmail())).thenReturn(false);
    //     when(mockUserDAO.isUsernameTaken(mockNewUserRequest.getUsername())).thenReturn(false);
    //     when(mockNewUserRequest.extractEntity()).thenReturn(user);
    //     when(mockUserDAO.register(user)).thenReturn("Generated ID = " + user.getId());

        
    //     ResponseString expected = new ResponseString(mockUserDAO.register(user));
    //     // Act
    //     ResponseString actual = sut.register(mockNewUserRequest);
    //     // Assert
    //     assertNotNull(actual);
    //     assertEquals(expected, actual);
    //     verify(mockUserDAO, times(1)).register(user);
    // }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullNewUser() {

        // Arrange
        NewUserRequest newUserRequest = null;

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullFirstName() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", null, "lastName", true, "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenZeroLengthFirstName() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "", "lastName", true, "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullLastName() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", null, true, "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenZeroLengthLastName() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", "", true, "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullEmail() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", null, "password", "firstname", "lastName", true, "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenZeroLengthEmail() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "", "password", "firstname", "lastName", true, "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullUsername() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest(null, "email", "password", "firstname", "lastName", true, "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }


    @Test
    public void test_register_throwsInvalidRequestException_givenLessThanFourLengthUsername() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("xxx", "email", "password", "firstname", "lastName", true, "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullPassword() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", null, "firstname", "lastName", true, "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }


    @Test
    public void test_register_throwsInvalidRequestException_givenLessThanEightLengthPassword() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "xxxxxxx", "firstname", "lastName", true, "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsIsAlreadyExist_givenTrueisEmailTaken() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", "lastName", true, "userRoleId");

        when(mockUserDAO.isEmailTaken(any())).thenReturn(true);
        // Act & Assert
        assertThrows(IsAlreadyExist.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsIsAlreadyExist_givenTrueisUsernameTaken() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", "lastName", true, "userRoleId");

        when(mockUserDAO.isUsernameTaken(any())).thenReturn(true);
        // Act & Assert
        assertThrows(IsAlreadyExist.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }
}
