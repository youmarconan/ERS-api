package com.revature.reimbursements;




import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;



public class ReimbursementServiceTest {

    ReimbursementService sut;
    ReimbursementRepo mockReimbursementRepo;

    @BeforeEach
    public void setup() {
        mockReimbursementRepo = Mockito.mock(ReimbursementRepo.class);
        sut = new ReimbursementService(mockReimbursementRepo);

    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockReimbursementRepo);
    }


}
