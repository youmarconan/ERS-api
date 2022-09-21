package com.revature.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.revature.common.exceptions.ResourceNotFoundException;

public class UserServiceTest {

    UserService sut;
    UserDAO mockUserDAO;
    UpdateRequestBody mockUpdateRequestBody;

    @BeforeEach
    public void setup() {
        mockUserDAO = Mockito.mock(UserDAO.class);
        sut = new UserService(mockUserDAO);
        mockUpdateRequestBody = new UpdateRequestBody();
    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockUserDAO);
    }

    @Test
    public void test_getAllUsers_returnsSuccessfully_givenNoArgs() {

        // Arrange
        ArrayList<User> usersStub = new ArrayList<>();
        User user1 = new User("id1", "username1", "email1", "password1", "firstName1", "lastName1", true,
                new UserRole());
        User user2 = new User("id2", "username2", "email2", "password2", "firstName2", "lastName2", true,
                new UserRole());
        User user3 = new User("id3", "username3", "email3", "password3", "firstName3", "lastName3", true,
                new UserRole());

        usersStub.add(user1);
        usersStub.add(user2);
        usersStub.add(user3);

        UserResponse x = new UserResponse(user1);
        UserResponse y = new UserResponse(user2);
        UserResponse z = new UserResponse(user3);

        List<UserResponse> userResponses = new ArrayList<>();
        userResponses.add(x);
        userResponses.add(y);
        userResponses.add(z);

        when(mockUserDAO.allUsers()).thenReturn(usersStub);
        List<UserResponse> expected = userResponses;

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
        assertThrows(ResourceNotFoundException.class, () -> {
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
        assertThrows(ResourceNotFoundException.class, () -> {
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
        assertThrows(ResourceNotFoundException.class, () -> {
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

    @Test
    public void test_register_returnsSuccessfully_givenValidNewUserRequest() {

        // Arrange

        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstName", "lastName",
                true, "1");

        when(mockUserDAO.isEmailTaken(newUserRequest.getEmail())).thenReturn(false);
        when(mockUserDAO.isUsernameTaken(newUserRequest.getUsername())).thenReturn(false);
        when(mockUserDAO.register(any(User.class))).thenReturn("totally-real-id");

        ResponseString expected = new ResponseString("totally-real-id");

        // Act
        ResponseString actual = sut.register(newUserRequest);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockUserDAO, times(1)).register(any(User.class));
    }

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
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", null, "lastName", true,
                "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenZeroLengthFirstName() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "", "lastName", true,
                "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullLastName() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", null, true,
                "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenZeroLengthLastName() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", "", true,
                "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullEmail() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", null, "password", "firstname", "lastName", true,
                "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenZeroLengthEmail() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "", "password", "firstname", "lastName", true,
                "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullUsername() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest(null, "email", "password", "firstname", "lastName", true,
                "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenLessThanFourLengthUsername() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("xxx", "email", "password", "firstname", "lastName", true,
                "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenNullPassword() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", null, "firstname", "lastName", true,
                "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsInvalidRequestException_givenLessThanEightLengthPassword() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "xxxxxxx", "firstname", "lastName",
                true, "userRoleId");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_register_throwsIsAlreadyExist_givenTrueisEmailTaken() {

        // Arrange
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", "lastName",
                true, "userRoleId");

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
        NewUserRequest newUserRequest = new NewUserRequest("username", "email", "password", "firstname", "lastName",
                true, "userRoleId");

        when(mockUserDAO.isUsernameTaken(any())).thenReturn(true);
        // Act & Assert
        assertThrows(IsAlreadyExist.class, () -> {
            sut.register(newUserRequest);
        });

        verify(mockUserDAO, times(0)).register(any());
    }

    @Test
    public void test_updateFristNmae_returnsSuccessfully_givenValidUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "userId");
        when(mockUserDAO.isIdValid(anyString())).thenReturn(true);
        when(mockUserDAO.updateUserFristName(anyString(), anyString()))
                .thenReturn("User first name updated to " + "updateTo" + ", Rows affected = 1");
        ResponseString expected = new ResponseString(
                "User first name updated to " + "updateTo" + ", Rows affected = 1");
        // Act
        ResponseString actual = sut.updateFristNmae(mockUpdateRequestBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(mockUserDAO, times(1)).updateUserFristName(anyString(), anyString());
    }

    @Test
    public void test_updateFristNmae_throwsInvalidRequestException_givenNullUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = null;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateFristNmae(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserFristName(anyString(), anyString());
    }

    @Test
    public void test_updateFristNmae_throwsInvalidRequestException_givenNullUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody(null, "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateFristNmae(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserFristName(anyString(), anyString());
    }

    @Test
    public void test_updateFristNmae_throwsInvalidRequestException_givenNullUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", null);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateFristNmae(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserFristName(anyString(), anyString());
    }

    @Test
    public void test_updateFristNmae_throwsInvalidRequestException_givenZeroLengthUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("", "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateFristNmae(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserFristName(anyString(), anyString());
    }

    @Test
    public void test_updateFristNmae_throwsInvalidRequestException_givenZeroLengthUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateFristNmae(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserFristName(anyString(), anyString());
    }

    @Test
    public void test_updateFristNmae_throwsInvalidRequestException_givenInvalidUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "invalidId");
        when(mockUserDAO.isIdValid(anyString())).thenThrow(InvalidRequestException.class);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateFristNmae(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserFristName(anyString(), anyString());
    }

    @Test
    public void test_updateLastNmae_returnsSuccessfully_givenValidUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "userId");
        when(mockUserDAO.isIdValid(anyString())).thenReturn(true);
        when(mockUserDAO.updateUserLastName(anyString(), anyString()))
                .thenReturn("User last name updated to " + "updateTo" + ", Rows affected = 1");
        ResponseString expected = new ResponseString("User last name updated to " + "updateTo" + ", Rows affected = 1");
        // Act
        ResponseString actual = sut.updateLastNmae(mockUpdateRequestBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(mockUserDAO, times(1)).updateUserLastName(anyString(), anyString());
    }

    @Test
    public void test_updateLastNmae_throwsInvalidRequestException_givenNullUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = null;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateLastNmae(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserLastName(anyString(), anyString());
    }

    @Test
    public void test_updateLastNmae_throwsInvalidRequestException_givenNullUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody(null, "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateLastNmae(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserLastName(anyString(), anyString());
    }

    @Test
    public void test_updateLastNmae_throwsInvalidRequestException_givenNullUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", null);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateLastNmae(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserLastName(anyString(), anyString());
    }

    @Test
    public void test_updateLastNmae_throwsInvalidRequestException_givenZeroLengthUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("", "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateLastNmae(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserLastName(anyString(), anyString());
    }

    @Test
    public void test_updateLastNmae_throwsInvalidRequestException_givenZeroLengthUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateLastNmae(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserLastName(anyString(), anyString());
    }

    @Test
    public void test_updateLastNmae_throwsInvalidRequestException_givenInvalidUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "invalidId");
        when(mockUserDAO.isIdValid(anyString())).thenThrow(InvalidRequestException.class);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateLastNmae(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserLastName(anyString(), anyString());
    }

    @Test
    public void test_updateupdateEmail_returnsSuccessfully_givenValidUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "userId");
        when(mockUserDAO.isIdValid(anyString())).thenReturn(true);
        when(mockUserDAO.updateUserEmail(anyString(), anyString()))
                .thenReturn("User email updated to " + "updateTo" + ", Rows affected = 1");
        ResponseString expected = new ResponseString("User email updated to " + "updateTo" + ", Rows affected = 1");
        // Act
        ResponseString actual = sut.updateEmail(mockUpdateRequestBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(mockUserDAO, times(1)).updateUserEmail(anyString(), anyString());
    }

    @Test
    public void test_updateEmail_throwsInvalidRequestException_givenNullUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = null;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateEmail(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserEmail(anyString(), anyString());
    }

    @Test
    public void test_updateEmail_throwsInvalidRequestException_givenNullUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody(null, "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateEmail(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserEmail(anyString(), anyString());
    }

    @Test
    public void test_updateEmail_throwsInvalidRequestException_givenNullUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", null);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateEmail(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserEmail(anyString(), anyString());
    }

    @Test
    public void test_updateEmail_throwsInvalidRequestException_givenZeroLengthUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("", "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateEmail(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserEmail(anyString(), anyString());
    }

    @Test
    public void test_updateEmail_throwsInvalidRequestException_givenZeroLengthUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateEmail(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserEmail(anyString(), anyString());
    }

    @Test
    public void test_updateEmail_throwsInvalidRequestException_givenInvalidUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "invalidId");
        when(mockUserDAO.isIdValid(anyString())).thenThrow(InvalidRequestException.class);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateEmail(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserEmail(anyString(), anyString());
    }

    @Test
    public void test_updateupdatePassword_returnsSuccessfully_givenValidUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "userId");
        when(mockUserDAO.isIdValid(anyString())).thenReturn(true);
        when(mockUserDAO.updateUserPassword(anyString(), anyString()))
                .thenReturn("User password updated to " + "updateTo" + ", Rows affected = 1");
        ResponseString expected = new ResponseString("User password updated to " + "updateTo" + ", Rows affected = 1");
        // Act
        ResponseString actual = sut.updatePassword(mockUpdateRequestBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(mockUserDAO, times(1)).updateUserPassword(anyString(), anyString());
    }

    @Test
    public void test_updatePassword_throwsInvalidRequestException_givenNullUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = null;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updatePassword(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserPassword(anyString(), anyString());
    }

    @Test
    public void test_updatePassword_throwsInvalidRequestException_givenNullUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody(null, "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updatePassword(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserPassword(anyString(), anyString());
    }

    @Test
    public void test_updatePassword_throwsInvalidRequestException_givenNullUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", null);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updatePassword(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserPassword(anyString(), anyString());
    }

    @Test
    public void test_updatePassword_throwsInvalidRequestException_givenZeroLengthUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("", "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updatePassword(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserPassword(anyString(), anyString());
    }

    @Test
    public void test_updatePassword_throwsInvalidRequestException_givenZeroLengthUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updatePassword(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserPassword(anyString(), anyString());
    }

    @Test
    public void test_updatePassword_throwsInvalidRequestException_givenInvalidUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "invalidId");
        when(mockUserDAO.isIdValid(anyString())).thenThrow(InvalidRequestException.class);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updatePassword(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserPassword(anyString(), anyString());
    }

    @Test
    public void test_updatePassword_throwsInvalidRequestException_givenLessThanEightLengthUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("xxxxxxx", "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updatePassword(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserPassword(anyString(), anyString());
    }

    @Test
    public void test_updateIsActive_returnsSuccessfully_givenTrueUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("true", "userId");
        when(mockUserDAO.isIdValid(anyString())).thenReturn(true);
        when(mockUserDAO.updateUserIsActive(anyString(), anyString()))
                .thenReturn("User active status updated to " + "true" + ", Rows affected = 1");
        ResponseString expected = new ResponseString("User active status updated to " + "true" + ", Rows affected = 1");
        // Act
        ResponseString actual = sut.updateIsActive(mockUpdateRequestBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(mockUserDAO, times(1)).updateUserIsActive(anyString(), anyString());
    }

    @Test
    public void test_updateIsActive_returnsSuccessfully_givenFalseUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("false", "userId");
        when(mockUserDAO.isIdValid(anyString())).thenReturn(true);
        when(mockUserDAO.updateUserIsActive(anyString(), anyString()))
                .thenReturn("User active status updated to " + "false" + ", Rows affected = 1");
        ResponseString expected = new ResponseString(
                "User active status updated to " + "false" + ", Rows affected = 1");
        // Act
        ResponseString actual = sut.updateIsActive(mockUpdateRequestBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(mockUserDAO, times(1)).updateUserIsActive(anyString(), anyString());
    }

    @Test
    public void test_updateIsActive_throwsInvalidRequestException_givenNullUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = null;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateIsActive(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserIsActive(anyString(), anyString());
    }

    @Test
    public void test_updateIsActive_throwsInvalidRequestException_givenNullUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody(null, "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateIsActive(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserIsActive(anyString(), anyString());
    }

    @Test
    public void test_updateIsActive_throwsInvalidRequestException_givenNullUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("true", null);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateIsActive(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserIsActive(anyString(), anyString());
    }

    @Test
    public void test_updateIsActive_throwsInvalidRequestException_givenZeroLengthUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("", "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateIsActive(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserIsActive(anyString(), anyString());
    }

    @Test
    public void test_updateIsActive_throwsInvalidRequestException_givenZeroLengthUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateIsActive(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserIsActive(anyString(), anyString());
    }

    @Test
    public void test_updateIsActive_throwsInvalidRequestException_givenInvalidUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("updateTo", "invalidId");
        when(mockUserDAO.isIdValid(anyString())).thenThrow(InvalidRequestException.class);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateIsActive(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserIsActive(anyString(), anyString());
    }

    @Test
    public void test_updateIsActive_throwsInvalidRequestException_givenNonBooleanUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("nonBoolean", "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateIsActive(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserIsActive(anyString(), anyString());
    }

    @Test
    public void test_updateRoleId_returnsSuccessfully_givenValidRoleId1UpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("1", "userId");
        when(mockUserDAO.isIdValid(anyString())).thenReturn(true);
        when(mockUserDAO.updateUserRoleId(anyString(), anyString()))
                .thenReturn("User role ID updated to " + "1" + ", Rows affected = 1");
        ResponseString expected = new ResponseString("User role ID updated to " + "1" + ", Rows affected = 1");
        // Act
        ResponseString actual = sut.updateRoleId(mockUpdateRequestBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(mockUserDAO, times(1)).updateUserRoleId(anyString(), anyString());
    }

    @Test
    public void test_updateRoleId_returnsSuccessfully_givenValidRoleId2UpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("2", "userId");
        when(mockUserDAO.isIdValid(anyString())).thenReturn(true);
        when(mockUserDAO.updateUserRoleId(anyString(), anyString()))
                .thenReturn("User role ID updated to " + "2" + ", Rows affected = 1");
        ResponseString expected = new ResponseString("User role ID updated to " + "2" + ", Rows affected = 1");
        // Act
        ResponseString actual = sut.updateRoleId(mockUpdateRequestBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(mockUserDAO, times(1)).updateUserRoleId(anyString(), anyString());
    }

    @Test
    public void test_updateRoleId_returnsSuccessfully_givenValidRoleId3UpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("3", "userId");
        when(mockUserDAO.isIdValid(anyString())).thenReturn(true);
        when(mockUserDAO.updateUserRoleId(anyString(), anyString()))
                .thenReturn("User role ID updated to " + "3" + ", Rows affected = 1");
        ResponseString expected = new ResponseString("User role ID updated to " + "3" + ", Rows affected = 1");
        // Act
        ResponseString actual = sut.updateRoleId(mockUpdateRequestBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(mockUserDAO, times(1)).updateUserRoleId(anyString(), anyString());
    }

    @Test
    public void test_updateRoleId_throwsInvalidRequestException_givenNullUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = null;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateRoleId(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserRoleId(anyString(), anyString());
    }

    @Test
    public void test_updateRoleId_throwsInvalidRequestException_givenNullUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody(null, "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateRoleId(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserRoleId(anyString(), anyString());
    }

    @Test
    public void test_updateRoleId_throwsInvalidRequestException_givenNullUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("1", null);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateRoleId(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserRoleId(anyString(), anyString());
    }

    @Test
    public void test_updateRoleId_throwsInvalidRequestException_givenZeroLengthUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("", "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateRoleId(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserRoleId(anyString(), anyString());
    }

    @Test
    public void test_updateRoleId_throwsInvalidRequestException_givenZeroLengthUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("1", "");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateRoleId(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserRoleId(anyString(), anyString());
    }

    @Test
    public void test_updateRoleId_throwsInvalidRequestException_givenInvalidUserIdUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("1", "invalidId");
        when(mockUserDAO.isIdValid(anyString())).thenThrow(InvalidRequestException.class);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateRoleId(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserRoleId(anyString(), anyString());
    }

    @Test
    public void test_updateRoleId_throwsInvalidRequestException_givenNonValidRoleIdUpdateToUpdateRequestBody() {

        // Arrange
        mockUpdateRequestBody = new UpdateRequestBody("4", "userId");
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateRoleId(mockUpdateRequestBody);
        });

        verify(mockUserDAO, times(0)).updateUserRoleId(anyString(), anyString());
    }
}
