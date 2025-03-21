package br.com.luiabdiel.ms_customer_v1.core.domain.application.service;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.UserEntity;
import br.com.luiabdiel.ms_customer_v1.infrastructure.persistence.UserIntegrator;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthUserDetailsServiceTest {

    @InjectMocks
    private AuthUserDetailsService authUserDetailsService;

    @Mock
    private UserIntegrator userPortOut;

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        var username = "robbor";
        var password = "robbor_pass";

        UserEntity userEntity = new UserEntity(username, password);

        when(this.userPortOut.findByUsername(any())).thenReturn(Optional.of(userEntity));
        UserDetails userDetails = this.authUserDetailsService.loadUserByUsername(username);

        verify(this.userPortOut, times(1)).findByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenUserNotFound() {
        when(this.userPortOut.findByUsername(any())).thenReturn(Optional.empty());
        ObjectNotFoundException thrownException = assertThrows(ObjectNotFoundException.class, () -> {
            this.authUserDetailsService.loadUserByUsername(any());
        });

        assertEquals("User not found", thrownException.getMessage());
        verify(this.userPortOut, times(1)).findByUsername(any());
    }
}