package br.com.luiabdiel.ms_customer_v1.shared.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class ResourceExceptionHandlerTest {

    @InjectMocks
    private ResourceExceptionHandler resourceExceptionHandler;

    @Mock
    private HttpServletRequest request;

    private static final String EXPECTED_ERROR_NOT_FOUND = "Resource not found";
    private static final String EXPECTED_ERROR_DATA_INTEGRITY = "Data integrity violation";

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void shouldHandleObjectNotFoundExceptionSuccessfully() {
        var exception = new ObjectNotFoundException(EXPECTED_ERROR_NOT_FOUND);

        ResponseEntity<StandarError> objectNotFoundException = this.resourceExceptionHandler.objectNotFoundException(exception, request);

        assertNotNull(objectNotFoundException);
        assertNotNull(objectNotFoundException.getBody());

        verify(request, times(1)).getRequestURI();
    }

    @Test
    void shouldHandleDataIntegrityViolationExceptionSuccessfully() {
        var exception = new DataIntegratyViolationException(EXPECTED_ERROR_DATA_INTEGRITY);

        ResponseEntity<StandarError> dataIntegrityViolationException = this.resourceExceptionHandler.dataIntegrityViolationException(exception, request);

        assertNotNull(dataIntegrityViolationException);
        assertNotNull(dataIntegrityViolationException.getBody());

        verify(request, times(1)).getRequestURI();
    }
}