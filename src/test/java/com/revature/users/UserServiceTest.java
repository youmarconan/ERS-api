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
import java.util.UUID;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.revature.common.GeneratedResponse;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.common.exceptions.IsAlreadyExist;
import com.revature.common.exceptions.ResourceNotFoundException;

public class UserServiceTest {

    UserService sut;
    UserRepo mockUserRepo;
    RoleRepo mockRoleRepo;

    @BeforeEach
    public void setup() {
        mockUserRepo = Mockito.mock(UserRepo.class);
        mockRoleRepo = Mockito.mock(RoleRepo.class);
        sut = new UserService(mockUserRepo,mockRoleRepo);
    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockUserRepo);
    }

    @Test
    public void test_getAllUsers_returnsSuccessfully_givenNoArgs() {

        // Arrange
        ArrayList<User> usersStub = new ArrayList<>();
        User user1 = new User(UUID.randomUUID(), "username", "email", "password", "firstName", "lastName", true, new UserRole(UUID.randomUUID(), "name"));
        User user2 = new User(UUID.randomUUID(), "username2", "email2", "password", "firstName", "lastName", true, new UserRole(UUID.randomUUID(), "name"));


        usersStub.add(user1);
        usersStub.add(user2);
        
        UserResponse x = new UserResponse(user1);
        UserResponse y = new UserResponse(user2);
        

        List<UserResponse> userResponses = new ArrayList<>();
        userResponses.add(x);
        userResponses.add(y);
        

        when(mockUserRepo.findAll()).thenReturn(usersStub);
        List<UserResponse> expected = userResponses;

        // Act
        List<UserResponse> actual = sut.getAllUsers();

        // Assert

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockUserRepo, times(1)).findAll();

    }

    @Test
    public void test_getUserById_returnsSuccessfully_givenValidId() {

        // Arrange
        UUID id = UUID.randomUUID();
        User mockUser = new User(UUID.randomUUID(), "username", "email", "password", "firstName", "lastName", true, new UserRole(UUID.randomUUID(), "name"));
        UserResponse expected = new UserResponse(mockUser);

        when(mockUserRepo.findById(id)).thenReturn(Optional.of(mockUser));

        // Act
        UserResponse actual = sut.getUserById(id.toString());
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockUserRepo, times(1)).findById(id);
    }

    @Test
    public void test_getUserById_throwsInvalidRequestException_givenInvalidId() {

        // Arrange
        String id = UUID.randomUUID().toString();

        when(mockUserRepo.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getUserById(id);
        });

        verify(mockUserRepo, times(1)).findById(any());
    }

    @Test
    public void test_getUserById_throwsInvalidRequestException_throwingIllegalArgumentException() {

        // Arrange
        String id = UUID.randomUUID().toString();

        when(mockUserRepo.findById(any())).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserById(id);
        });

        verify(mockUserRepo, times(1)).findById(any());
    }

    @Test
    public void test_getUserByUsername_returnsSuccessfully_givenValidUsername() {

        // Arrange
        String username = "moreThan0";
        User mockUser = new User(UUID.randomUUID(), "username", "email", "password", "firstName", "lastName", true, new UserRole());
        UserResponse expected = new UserResponse(mockUser);

        when(mockUserRepo.findUserByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act
        UserResponse actual = sut.getUserByUsername(username);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockUserRepo, times(1)).findUserByUsername(username);
    }

    @Test
    public void test_getUserByUSername_throwsInvalidRequestException_givenInvalidUsername() {

        // Arrange
        String username = "invalid";

        when(mockUserRepo.findUserByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getUserByUsername(username);
        });

        verify(mockUserRepo, times(1)).findUserByUsername(username);
    }

    @Test
    public void test_getUserByUsername_throwsInvalidRequestException_throwingIllegalArgumentException() {

        // Arrange
        String username = "invalid";

        when(mockUserRepo.findUserByUsername(username)).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByUsername(username);
        });

        verify(mockUserRepo, times(1)).findUserByUsername(username);
    }

    @Test
    public void test_getUserByUsername_throwsInvalidRequestException_givenNullUsername() {

        // Arrange
        String username = null;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByUsername(username);
        });

        verify(mockUserRepo, times(0)).findUserByUsername(username);

    }

    @Test
    public void test_getUserByUsername_throwsInvalidRequestException_givenZeroLengthUsername() {

        // Arrange
        String username = "";
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByUsername(username);
        });

        verify(mockUserRepo, times(0)).findUserByUsername(username);
    }

    @Test
    public void test_getUserByEmail_returnsSuccessfully_givenValidEmail() {

        // Arrange
        String email = "moreThan0";
        User mockUser = new User(UUID.randomUUID(), "username", "email", "password", "firstName", "lastName", true, new UserRole());
        UserResponse expected = new UserResponse(mockUser);

        when(mockUserRepo.findUserByEmail(email)).thenReturn(Optional.of(mockUser));

        // Act
        UserResponse actual = sut.getUserByEmail(email);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockUserRepo, times(1)).findUserByEmail(email);
    }

    @Test
    public void test_getUserByEmail_throwsInvalidRequestException_givenInvalidEmail() {

        // Arrange
        String email = "invalid";

        when(mockUserRepo.findUserByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getUserByEmail(email);
        });

        verify(mockUserRepo, times(1)).findUserByEmail(email);
    }

    @Test
    public void test_getUserByEmail_throwsInvalidRequestException_throwingIllegalArgumentException() {

        // Arrange
        String email = "invalid";

        when(mockUserRepo.findUserByEmail(email)).thenThrow(new IllegalArgumentException());

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByEmail(email);
        });

        verify(mockUserRepo, times(1)).findUserByEmail(email);
    }

    @Test
    public void test_getUserByEmail_throwsInvalidRequestException_givenNullEmail() {

        // Arrange
        String email = null;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByEmail(email);
        });

        verify(mockUserRepo, times(0)).findUserByEmail(email);

    }

    @Test
    public void test_getUserByEmail_throwsInvalidRequestException_givenZeroLengthEmail() {

        // Arrange
        String email = "";
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserByEmail(email);
        });

        verify(mockUserRepo, times(0)).findUserByEmail(email);
    }


    @Test
    public void test_register_throwsInvalidRequestException_givenNullNewUser() {

        // Arrange
        NewUserRequest newUserRequest = null;

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullFirstName() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", null, "lastName", true, "admin" );

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenZeroLengthFirstName() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "", "lastName", true,
        "admin");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullLastName() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", null, true,
        "admin");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenZeroLengthLastName() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", "", true,
        "admin");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullEmail() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", null, "password", "firstname", "lastName", true,
        "admin");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenZeroLengthEmail() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "", "password", "firstname", "lastName", true,
        "admin");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullUsername() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest(null, "email", "password", "firstname", "lastName", true,
        "admin");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenLessThanFourLengthUsername() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("xxx", "email", "password", "firstname", "lastName", true,
        "admin");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullPassword() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", null, "firstname", "lastName", true,
        "admin");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenLessThanEightLengthPassword() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "xxxxxxx", "firstname", "lastName",
                true,"admin");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsIsAlreadyExist_givenTrueisEmailTaken() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", "lastName",
                true, "admin");

        when(mockUserRepo.existsByEmail(any())).thenReturn(true);
        // Act & Assert
        assertThrows(IsAlreadyExist.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsIsAlreadyExist_givenTrueIsUsernameTaken() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", "lastName",
                true, "admin");

        when(mockUserRepo.existsByUsername(any())).thenReturn(true);
        // Act & Assert
        assertThrows(IsAlreadyExist.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullIsActive() {


        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", "lastName", null , "admin");

        when(mockUserRepo.existsByUsername(any())).thenReturn(false);
        when(mockUserRepo.existsByEmail(any())).thenReturn(false);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

   
    @Test
    public void test_register_throwsInvalidRequestException_givenNullRoll() {


        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", "lastName", true , null);

        when(mockUserRepo.existsByUsername(any())).thenReturn(false);
        when(mockUserRepo.existsByEmail(any())).thenReturn(false);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenInvalidRoll() {


        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", "lastName", true , "invalid");

        when(mockUserRepo.existsByUsername(any())).thenReturn(false);
        when(mockUserRepo.existsByEmail(any())).thenReturn(false);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserRepo, times(0)).save(any());
    }

    @Test
    public void test_register_returnsSuccessfully_givenValidNewUserRequest() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("valid", "valid", "password", "valid", "valid", true, "admin");

        User user = newUserRequest.extractEntity();

        UserRole userRole = new UserRole(UUID.randomUUID(), "admin");
        
        when(mockRoleRepo.findById(any())).thenReturn(Optional.of(userRole));
        when(mockUserRepo.save(any())).thenReturn(user);
       
        // Act
        GeneratedResponse actual = sut.register(newUserRequest);
        // Assert
        assertNotNull(actual);
        verify(mockUserRepo, times(1)).save(any());
    }

    @Test
    public void test_updateUser_returnsSuccessfully_givenValidUpdateRequestBody() {

        // Arrange

        UpdateRequestBody updateRequestBody = new UpdateRequestBody(UUID.randomUUID().toString(), "valid", "valid", "password", "valid", "valid", "true", "admin");

        UserRole userRole = new UserRole(UUID.randomUUID(), "admin");
        when(mockRoleRepo.findById(any())).thenReturn(Optional.of(userRole));

        User user = new User();
        
        when(mockUserRepo.findById(any())).thenReturn(Optional.of(user));
        when(mockUserRepo.existsByUsername(any())).thenReturn(false);
        when(mockUserRepo.existsByEmail(any())).thenReturn(false);


       
        // Act
        sut.updateUser(updateRequestBody);
        // Assert
        assertEquals(updateRequestBody.getUsername(), user.getUsername());
        assertEquals(updateRequestBody.getEmail(), user.getEmail());
        assertEquals(updateRequestBody.getPassword(), user.getPassword());
        assertEquals(updateRequestBody.getFirstName(), user.getFirstName());
        assertEquals(updateRequestBody.getLastName(), user.getLastName());
        assertEquals(updateRequestBody.getUserRoleName(), user.getRole().getName());

    }



}
