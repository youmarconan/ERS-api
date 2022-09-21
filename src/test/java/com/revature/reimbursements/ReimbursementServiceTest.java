package com.revature.reimbursements;

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
import com.revature.common.exceptions.ResourceNotFoundException;

public class ReimbursementServiceTest {

    ReimbursementService sut;
    ReimbursementDAO mockReimbursementDAO;

    @BeforeEach
    public void setup() {
        mockReimbursementDAO = Mockito.mock(ReimbursementDAO.class);
        sut = new ReimbursementService(mockReimbursementDAO);

    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockReimbursementDAO);
    }

    @Test
    public void test_getAllReimbursements_returnsSuccessfully_givenNoArgs() {

        // Arrange

        ReimbursementStatus reimbursementStatus = new ReimbursementStatus();
        ReimbursementType reimbursementType = new ReimbursementType();

        Reimbursement reimbursement = new Reimbursement("id", 1111.11, "submitted", "resolved", "description",
                "authorId", "resolverId", reimbursementStatus, reimbursementType);
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(reimbursement);

        ReimbursementResponse reimbursementResponse = new ReimbursementResponse(reimbursement);
        List<ReimbursementResponse> reimbursementResponses = new ArrayList<>();
        reimbursementResponses.add(reimbursementResponse);

        when(mockReimbursementDAO.getAllReimbursements()).thenReturn(reimbursements);

        List<ReimbursementResponse> expected = reimbursementResponses;

        // Act

        List<ReimbursementResponse> actual = sut.getAllReimbursements();

        // Assert

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).getAllReimbursements();

    }

    @Test
    public void test_getReimbursementsByStatus_returnsSuccessfully_givenStringStatus() {

        // Arrange

        String status = "pending";

        ReimbursementStatus reimbursementStatus = new ReimbursementStatus();
        reimbursementStatus.setStatusName(status);
        ReimbursementType reimbursementType = new ReimbursementType();

        Reimbursement reimbursement = new Reimbursement("id", 1111.11, "submitted", "resolved", "description",
                "authorId", "resolverId", reimbursementStatus, reimbursementType);
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(reimbursement);

        ReimbursementResponse reimbursementResponse = new ReimbursementResponse(reimbursement);
        List<ReimbursementResponse> reimbursementResponses = new ArrayList<>();
        reimbursementResponses.add(reimbursementResponse);

        when(mockReimbursementDAO.findReimbursementbyStatus(status)).thenReturn(reimbursements);

        List<ReimbursementResponse> expected = reimbursementResponses;

        // Act

        List<ReimbursementResponse> actual = sut.getReimbursementsByStatus(status);

        // Assert

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).findReimbursementbyStatus(status);

    }

    @Test
    public void test_getReimbursementsByStatus_throwsInvalidRequestException_givenNullStatus() {

        // Arrange
        String status = null;

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbursementsByStatus(status);
        });

        verify(mockReimbursementDAO, times(0)).findReimbursementbyStatus(status);
    }

    @Test
    public void test_getReimbursementsByStatus_throwsInvalidRequestException_givenZeroLengthStatus() {

        // Arrange
        String status = "";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbursementsByStatus(status);
        });

        verify(mockReimbursementDAO, times(0)).findReimbursementbyStatus(status);
    }

    @Test
    public void test_getReimbursementsByStatus_throwsInvalidRequestException_givenInvalidStatusName() {

        // Arrange
        String status = "invalid";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbursementsByStatus(status);
        });

        verify(mockReimbursementDAO, times(0)).findReimbursementbyStatus(status);
    }

    @Test
    public void test_getReimbursementsByType_returnsSuccessfully_givenStringType() {

        // Arrange

        String type = "other";

        ReimbursementStatus reimbursementStatus = new ReimbursementStatus();
        ReimbursementType reimbursementType = new ReimbursementType();
        reimbursementType.setTypeName(type);

        Reimbursement reimbursement = new Reimbursement("id", 1111.11, "submitted", "resolved", "description",
                "authorId", "resolverId", reimbursementStatus, reimbursementType);
        List<Reimbursement> reimbursements = new ArrayList<>();
        reimbursements.add(reimbursement);

        ReimbursementResponse reimbursementResponse = new ReimbursementResponse(reimbursement);
        List<ReimbursementResponse> reimbursementResponses = new ArrayList<>();
        reimbursementResponses.add(reimbursementResponse);

        when(mockReimbursementDAO.findReimbursementbyType(type)).thenReturn(reimbursements);

        List<ReimbursementResponse> expected = reimbursementResponses;

        // Act

        List<ReimbursementResponse> actual = sut.getReimbursementsByType(type);

        // Assert

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).findReimbursementbyType(type);

    }

    @Test
    public void test_getReimbursementsByType_throwsInvalidRequestException_givenNullType() {

        // Arrange
        String type = null;

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbursementsByType(type);
        });

        verify(mockReimbursementDAO, times(0)).findReimbursementbyType(type);
    }

    @Test
    public void test_getReimbursementsByType_throwsInvalidRequestException_givenZeroLengthType() {

        // Arrange
        String type = "";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbursementsByType(type);
        });

        verify(mockReimbursementDAO, times(0)).findReimbursementbyType(type);
    }

    @Test
    public void test_getReimbursementsByType_throwsInvalidRequestException_givenInvalidTypeName() {

        // Arrange
        String type = "invalid";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbursementsByType(type);
        });

        verify(mockReimbursementDAO, times(0)).findReimbursementbyType(type);
    }

    @Test
    public void test_getReimbursementById_returnsSuccessfully_givenStringId() {

        // Arrange
        String id = "id";
        ReimbursementStatus reimbursementStatus = new ReimbursementStatus();
        ReimbursementType reimbursementType = new ReimbursementType();
        Reimbursement reimbursement = new Reimbursement(id, 1111.11, "submitted", "resolved", "description", "authorId",
                "resolverId", reimbursementStatus, reimbursementType);

        ReimbursementResponse reimbursementResponse = new ReimbursementResponse(reimbursement);
        when(mockReimbursementDAO.findReimbursementById(anyString())).thenReturn(Optional.of(reimbursement));

        ReimbursementResponse expected = reimbursementResponse;
        // Act
        ReimbursementResponse actual = sut.getReimbursementById(id);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);

        verify(mockReimbursementDAO, times(1)).findReimbursementById(id);
    }

    @Test
    public void test_getReimbursementsById_throwsInvalidRequestException_givenNullId() {

        // Arrange
        String id = null;

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbursementById(id);
        });

        verify(mockReimbursementDAO, times(0)).findReimbursementById(id);
    }

    @Test
    public void test_getReimbursementsById_throwsInvalidRequestException_givenZeroLengthId() {

        // Arrange
        String id = "";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbursementById(id);
        });

        verify(mockReimbursementDAO, times(0)).findReimbursementById(id);
    }

    @Test
    public void test_getReimbursementsById_throwsResourceNotFoundException_givenInvalidId() {

        // Arrange
        String id = "invalid";

        when(mockReimbursementDAO.findReimbursementById(id)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getReimbursementById(id);
        });

        verify(mockReimbursementDAO, times(1)).findReimbursementById(id);
    }

    @Test
    public void test_getReimbursementsById_throwsResourceNotFoundException_givenBadRequest() {

        // Arrange
        String id = "invalid";

        when(mockReimbursementDAO.findReimbursementById(id)).thenThrow(IllegalArgumentException.class);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getReimbursementById(id);
        });

        verify(mockReimbursementDAO, times(1)).findReimbursementById(id);
    }

    @Test
    public void test_approveOrDeny_returnsSuccessfully_givenApproveAndResolverId() {

        // Arrange
        ApproveOrDenyBody approveOrDenyBody = new ApproveOrDenyBody("approve", "reimbursementId");
        String resolverId = "id";
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);
        when(mockReimbursementDAO.approveOrDenyReimbursement(anyString(), anyString(), anyString()))
                .thenReturn("expected-value");
        ResponseString expected = new ResponseString("expected-value");

        // Act
        ResponseString actual = sut.approveOrDeny(approveOrDenyBody, resolverId);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).approveOrDenyReimbursement(anyString(), anyString(), anyString());
    }

    @Test
    public void test_approveOrDeny_returnsSuccessfully_givenDenyAndResolverId() {

        // Arrange
        ApproveOrDenyBody approveOrDenyBody = new ApproveOrDenyBody("deny", "reimbursementId");
        String resolverId = "id";
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);
        when(mockReimbursementDAO.approveOrDenyReimbursement(anyString(), anyString(), anyString()))
                .thenReturn("expected-value");
        ResponseString expected = new ResponseString("expected-value");

        // Act
        ResponseString actual = sut.approveOrDeny(approveOrDenyBody, resolverId);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).approveOrDenyReimbursement(anyString(), anyString(), anyString());
    }

    @Test
    public void test_approveOrDeny_throwsInvalidRequestException_givenNullApproveOrDenyBody() {

        // Arrange
        ApproveOrDenyBody approveOrDenyBody = null;
        String resolverId = "id";
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.approveOrDeny(approveOrDenyBody, resolverId);
        });

        verify(mockReimbursementDAO, times(0)).approveOrDenyReimbursement(anyString(), anyString(), anyString());
    }

    @Test
    public void test_approveOrDeny_throwsInvalidRequestException_givenNullResplverId() {

        // Arrange
        ApproveOrDenyBody approveOrDenyBody = new ApproveOrDenyBody("approve", "reimbursementId");
        String resolverId = null;
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.approveOrDeny(approveOrDenyBody, resolverId);
        });

        verify(mockReimbursementDAO, times(0)).approveOrDenyReimbursement(anyString(), anyString(), anyString());
    }

    @Test
    public void test_approveOrDeny_throwsInvalidRequestException_givenZeroLengthResplverId() {

        // Arrange
        ApproveOrDenyBody approveOrDenyBody = new ApproveOrDenyBody("approve", "reimbursementId");
        String resolverId = "";
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.approveOrDeny(approveOrDenyBody, resolverId);
        });

        verify(mockReimbursementDAO, times(0)).approveOrDenyReimbursement(anyString(), anyString(), anyString());
    }

    @Test
    public void test_approveOrDeny_throwsInvalidRequestException_givenNullStatusName() {

        // Arrange
        ApproveOrDenyBody approveOrDenyBody = new ApproveOrDenyBody(null, "reimbursementId");
        String resolverId = "valid";
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.approveOrDeny(approveOrDenyBody, resolverId);
        });

        verify(mockReimbursementDAO, times(0)).approveOrDenyReimbursement(anyString(), anyString(), anyString());
    }

    @Test
    public void test_approveOrDeny_throwsInvalidRequestException_givenZeroLengthStatusName() {

        // Arrange
        ApproveOrDenyBody approveOrDenyBody = new ApproveOrDenyBody("", "reimbursementId");
        String resolverId = "valid";
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.approveOrDeny(approveOrDenyBody, resolverId);
        });

        verify(mockReimbursementDAO, times(0)).approveOrDenyReimbursement(anyString(), anyString(), anyString());
    }

    @Test
    public void test_approveOrDeny_throwsInvalidRequestException_givenNullReimbursementId() {

        // Arrange
        ApproveOrDenyBody approveOrDenyBody = new ApproveOrDenyBody("approve", null);
        String resolverId = "valid";
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.approveOrDeny(approveOrDenyBody, resolverId);
        });

        verify(mockReimbursementDAO, times(0)).approveOrDenyReimbursement(anyString(), anyString(), anyString());
    }

    @Test
    public void test_approveOrDeny_throwsInvalidRequestException_givenZeroLengthReimbursementId() {

        // Arrange
        ApproveOrDenyBody approveOrDenyBody = new ApproveOrDenyBody("approve", "");
        String resolverId = "valid";
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.approveOrDeny(approveOrDenyBody, resolverId);
        });

        verify(mockReimbursementDAO, times(0)).approveOrDenyReimbursement(anyString(), anyString(), anyString());
    }

    @Test
    public void test_approveOrDeny_throwsInvalidRequestException_givenInvalidReimbursementId() {

        // Arrange
        ApproveOrDenyBody approveOrDenyBody = new ApproveOrDenyBody("approve", "valid");
        String resolverId = "valid";
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(false);
        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.approveOrDeny(approveOrDenyBody, resolverId);
        });

        verify(mockReimbursementDAO, times(0)).approveOrDenyReimbursement(anyString(), anyString(), anyString());
    }

    @Test
    public void test_approveOrDeny_throwsInvalidRequestException_givenInvalidStatusName() {

        // Arrange
        ApproveOrDenyBody approveOrDenyBody = new ApproveOrDenyBody("invalid", "reimbursementId");
        String resolverId = "id";
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.approveOrDeny(approveOrDenyBody, resolverId);
        });
        verify(mockReimbursementDAO, times(0)).approveOrDenyReimbursement(anyString(), anyString(), anyString());
    }

    @Test
    public void test_createNewReimbursement_returnsSuccessfully_givenNewReimbursementRequestWithTypeLodgingAndAuthorId() {

        // Arrange
        NewReimbursementRequest newReimbursementRequest = new NewReimbursementRequest(1111.11, "description",
                "lodging");
        String authorId = "id";

        when(mockReimbursementDAO.createNewReimbursement(any(Reimbursement.class))).thenReturn("true-value");
        ResponseString expected = new ResponseString("true-value");

        // Act
        ResponseString actual = sut.createNewReimbursement(newReimbursementRequest, authorId);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).createNewReimbursement(any(Reimbursement.class));

    }

    @Test
    public void test_createNewReimbursement_returnsSuccessfully_givenNewReimbursementRequestWithTypeTravelAndAuthorId() {

        // Arrange
        NewReimbursementRequest newReimbursementRequest = new NewReimbursementRequest(1111.11, "description", "travel");
        String authorId = "id";

        when(mockReimbursementDAO.createNewReimbursement(any(Reimbursement.class))).thenReturn("true-value");
        ResponseString expected = new ResponseString("true-value");

        // Act
        ResponseString actual = sut.createNewReimbursement(newReimbursementRequest, authorId);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).createNewReimbursement(any(Reimbursement.class));

    }

    @Test
    public void test_createNewReimbursement_returnsSuccessfully_givenNewReimbursementRequestWithTypeOtherAndAuthorId() {

        // Arrange
        NewReimbursementRequest newReimbursementRequest = new NewReimbursementRequest(1111.11, "description", "other");
        String authorId = "id";

        when(mockReimbursementDAO.createNewReimbursement(any(Reimbursement.class))).thenReturn("true-value");
        ResponseString expected = new ResponseString("true-value");

        // Act
        ResponseString actual = sut.createNewReimbursement(newReimbursementRequest, authorId);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).createNewReimbursement(any(Reimbursement.class));

    }

    @Test
    public void test_createNewReimbursement_returnsSuccessfully_givenNewReimbursementRequestWithTypeFoodAndAuthorId() {

        // Arrange
        NewReimbursementRequest newReimbursementRequest = new NewReimbursementRequest(1111.11, "description", "food");
        String authorId = "id";

        when(mockReimbursementDAO.createNewReimbursement(any(Reimbursement.class))).thenReturn("true-value");
        ResponseString expected = new ResponseString("true-value");

        // Act
        ResponseString actual = sut.createNewReimbursement(newReimbursementRequest, authorId);

        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).createNewReimbursement(any(Reimbursement.class));

    }

    @Test
    public void test_createNewReimbursement_throwsInvalidRequestException_givenNewReimbursementRequestWithTypeNullAndAuthorId() {

        // Arrange
        NewReimbursementRequest newReimbursementRequest = new NewReimbursementRequest(1111.11, "description", null);
        String authorId = "id";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.createNewReimbursement(newReimbursementRequest, authorId);
        });

        verify(mockReimbursementDAO, times(0)).createNewReimbursement(any(Reimbursement.class));

    }

    @Test
    public void test_createNewReimbursement_throwsInvalidRequestException_givenNewReimbursementRequestWithZeroLengthTypeAndAuthorId() {

        // Arrange
        NewReimbursementRequest newReimbursementRequest = new NewReimbursementRequest(1111.11, "description", "");
        String authorId = "id";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.createNewReimbursement(newReimbursementRequest, authorId);
        });

        verify(mockReimbursementDAO, times(0)).createNewReimbursement(any(Reimbursement.class));

    }

    @Test
    public void test_createNewReimbursement_throwsInvalidRequestException_givenNewReimbursementRequestWithInvalidTypeAndAuthorId() {

        // Arrange
        NewReimbursementRequest newReimbursementRequest = new NewReimbursementRequest(1111.11, "description",
                "invalid");
        String authorId = "id";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.createNewReimbursement(newReimbursementRequest, authorId);
        });

        verify(mockReimbursementDAO, times(0)).createNewReimbursement(any(Reimbursement.class));

    }

    @Test
    public void test_createNewReimbursement_throwsInvalidRequestException_givenNullNewReimbursementRequestAndAuthorId() {

        // Arrange
        NewReimbursementRequest newReimbursementRequest = null;
        String authorId = "id";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.createNewReimbursement(newReimbursementRequest, authorId);
        });

        verify(mockReimbursementDAO, times(0)).createNewReimbursement(any(Reimbursement.class));

    }

    @Test
    public void test_createNewReimbursement_throwsInvalidRequestException_givenNewReimbursementRequestWithNullDescriptionAndAuthorId() {

        // Arrange
        NewReimbursementRequest newReimbursementRequest = new NewReimbursementRequest(1111.11, null, "other");
        String authorId = "valid";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.createNewReimbursement(newReimbursementRequest, authorId);
        });

        verify(mockReimbursementDAO, times(0)).createNewReimbursement(any(Reimbursement.class));

    }

    @Test
    public void test_createNewReimbursement_throwsInvalidRequestException_givenNewReimbursementRequestWithZeroLengthDescriptionAndAuthorId() {

        // Arrange
        NewReimbursementRequest newReimbursementRequest = new NewReimbursementRequest(1111.11, "", "other");
        String authorId = "valid";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.createNewReimbursement(newReimbursementRequest, authorId);
        });

        verify(mockReimbursementDAO, times(0)).createNewReimbursement(any(Reimbursement.class));

    }

    @Test
    public void test_createNewReimbursement_throwsInvalidRequestException_givenNewReimbursementRequestWithNegativeAmountAndAuthorId() {

        // Arrange
        NewReimbursementRequest newReimbursementRequest = new NewReimbursementRequest(-1111.11, "description", "other");
        String authorId = "valid";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.createNewReimbursement(newReimbursementRequest, authorId);
        });

        verify(mockReimbursementDAO, times(0)).createNewReimbursement(any(Reimbursement.class));

    }

    @Test
    public void test_createNewReimbursement_throwsInvalidRequestException_givenNewReimbursementRequestWithAmountMoreThan9999Point99AmountAndAuthorId() {

        // Arrange
        NewReimbursementRequest newReimbursementRequest = new NewReimbursementRequest(10000.00, "description", "other");
        String authorId = "valid";

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.createNewReimbursement(newReimbursementRequest, authorId);
        });

        verify(mockReimbursementDAO, times(0)).createNewReimbursement(any(Reimbursement.class));

    }

    @Test
    public void test_updateAmount_returnsSuccessfully_givenValidUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("1111.11", "reimbursementId");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);
        when(mockReimbursementDAO.updateOwnreimbursementAmount(updateOwnReimbBody)).thenReturn("value");
        ResponseString expected = new ResponseString("value");
        // Act
        ResponseString actual = sut.updateAmount(updateOwnReimbBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).updateOwnreimbursementAmount(updateOwnReimbBody);
    }

    @Test
    public void test_updateAmount_throwsInvalidRequestException_givenNullUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = null;
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateAmount(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementAmount(updateOwnReimbBody);

    }

    @Test
    public void test_updateAmount_throwsInvalidRequestException_givenNullUpdateToInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody(null, "reimbursementId");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateAmount(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementAmount(updateOwnReimbBody);

    }

    @Test
    public void test_updateAmount_throwsInvalidRequestException_givenZeroLengthUpdateToInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("", "reimbursementId");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateAmount(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementAmount(updateOwnReimbBody);

    }

    @Test
    public void test_updateAmount_throwsInvalidRequestException_givenZeroLengthReimbursementIdInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("1111.11", "");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateAmount(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementAmount(updateOwnReimbBody);

    }

    @Test
    public void test_updateAmount_throwsInvalidRequestException_givenNullReimbursementIdInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("1111.11", null);
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateAmount(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementAmount(updateOwnReimbBody);

    }

    @Test
    public void test_updateAmount_throwsInvalidRequestException_givenUpdateToMoreThan9999Point99InUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("10000.00", "valid");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateAmount(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementAmount(updateOwnReimbBody);

    }

    @Test
    public void test_updateAmount_throwsInvalidRequestException_givenNonValidReimbursementIdInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("1111.11", "invalid");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sut.updateAmount(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementAmount(updateOwnReimbBody);

    }

    @Test
    public void test_updateTypeId_returnsSuccessfully_givenLodgingTypeInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("lodging", "reimbursementId");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);
        when(mockReimbursementDAO.updateOwnreimbursementTypeId(updateOwnReimbBody)).thenReturn("value");
        ResponseString expected = new ResponseString("value");
        // Act
        ResponseString actual = sut.updateTypeId(updateOwnReimbBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).updateOwnreimbursementTypeId(updateOwnReimbBody);
    }

    @Test
    public void test_updateTypeId_returnsSuccessfully_givenTravelTypeInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("travel", "reimbursementId");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);
        when(mockReimbursementDAO.updateOwnreimbursementTypeId(updateOwnReimbBody)).thenReturn("value");
        ResponseString expected = new ResponseString("value");
        // Act
        ResponseString actual = sut.updateTypeId(updateOwnReimbBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).updateOwnreimbursementTypeId(updateOwnReimbBody);
    }

    @Test
    public void test_updateTypeId_returnsSuccessfully_givenFoodTypeInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("food", "reimbursementId");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);
        when(mockReimbursementDAO.updateOwnreimbursementTypeId(updateOwnReimbBody)).thenReturn("value");
        ResponseString expected = new ResponseString("value");
        // Act
        ResponseString actual = sut.updateTypeId(updateOwnReimbBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).updateOwnreimbursementTypeId(updateOwnReimbBody);
    }

    @Test
    public void test_updateTypeId_returnsSuccessfully_givenOtherTypeInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("other", "reimbursementId");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);
        when(mockReimbursementDAO.updateOwnreimbursementTypeId(updateOwnReimbBody)).thenReturn("value");
        ResponseString expected = new ResponseString("value");
        // Act
        ResponseString actual = sut.updateTypeId(updateOwnReimbBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).updateOwnreimbursementTypeId(updateOwnReimbBody);
    }

    @Test
    public void test_updateTypeId_throwsInvalidRequestException_givenNullUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = null;
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateTypeId(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementTypeId(updateOwnReimbBody);

    }

    @Test
    public void test_updateTypeId_throwsInvalidRequestException_givenNullUpdateToInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody(null, "reimbursementId");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateTypeId(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementTypeId(updateOwnReimbBody);

    }

    @Test
    public void test_updateTypeId_throwsInvalidRequestException_givenZeroLengthUpdateToInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("", "reimbursementId");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateTypeId(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementTypeId(updateOwnReimbBody);

    }

    @Test
    public void test_updateTypeId_throwsInvalidRequestException_givenZeroLengthReimbursementIdInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("food", "");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateTypeId(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementTypeId(updateOwnReimbBody);

    }

    @Test
    public void test_updateTypeId_throwsInvalidRequestException_givenNullReimbursementIdInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("food", null);
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateTypeId(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementTypeId(updateOwnReimbBody);

    }

    @Test
    public void test_updateTypeId_throwsInvalidRequestException_givenNonValidReimbursementIdInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("food", "invalid");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sut.updateTypeId(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementTypeId(updateOwnReimbBody);

    }

    @Test
    public void test_updateTypeId_throwsInvalidRequestException_givenNonValidTypeNameInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("invalid", "valid");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateTypeId(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementTypeId(updateOwnReimbBody);

    }

    @Test
    public void test_updateDescription_returnsSuccessfully_givenValidInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("Description", "reimbursementId");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);
        when(mockReimbursementDAO.updateOwnreimbursementDescription(updateOwnReimbBody)).thenReturn("value");
        ResponseString expected = new ResponseString("value");
        // Act
        ResponseString actual = sut.updateDescription(updateOwnReimbBody);
        // Assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(mockReimbursementDAO, times(1)).updateOwnreimbursementDescription(updateOwnReimbBody);
    }

    @Test
    public void test_updateDescription_throwsInvalidRequestException_givenNullUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = null;
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateDescription(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementDescription(updateOwnReimbBody);

    }

    @Test
    public void test_updateDescription_throwsInvalidRequestException_givenNullUpdateToInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody(null, "reimbursementId");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateDescription(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementDescription(updateOwnReimbBody);

    }

    @Test
    public void test_updateDescription_throwsInvalidRequestException_givenZeroLengthUpdateToInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("", "reimbursementId");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateDescription(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementDescription(updateOwnReimbBody);

    }

    @Test
    public void test_updateDescription_throwsInvalidRequestException_givenZeroLengthReimbursementIdInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("Description", "");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateDescription(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementDescription(updateOwnReimbBody);

    }

    @Test
    public void test_updateDescription_throwsInvalidRequestException_givenNullReimbursementIdInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("food", null);
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.updateDescription(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementDescription(updateOwnReimbBody);

    }

    @Test
    public void test_updateDescription_throwsInvalidRequestException_givenNonValidReimbursementIdInUpdateOwnReimbBody() {

        // Arrange
        UpdateOwnReimbBody updateOwnReimbBody = new UpdateOwnReimbBody("food", "invalid");
        when(mockReimbursementDAO.isIdValid(anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sut.updateDescription(updateOwnReimbBody);
        });

        verify(mockReimbursementDAO, times(0)).updateOwnreimbursementDescription(updateOwnReimbBody);

    }
}
