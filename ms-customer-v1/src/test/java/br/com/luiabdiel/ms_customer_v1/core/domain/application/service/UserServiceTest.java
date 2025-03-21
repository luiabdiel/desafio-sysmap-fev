package br.com.luiabdiel.ms_customer_v1.core.domain.application.service;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.UserEntity;
import br.com.luiabdiel.ms_customer_v1.infrastructure.persistence.UserIntegrator;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.DataIntegratyViolationException;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.ObjectNotFoundException;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.UnauthorizedException;
import br.com.luiabdiel.ms_customer_v1.shared.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserIntegrator userPortOut;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldRegisterUserSuccessfully() {
        var expectedUsername = "robbor";
        var expectedPassword = "robbor_pass";
        var encryptedPassword = "encrypted_pass";

        UserEntity userEntity = new UserEntity(expectedUsername, encryptedPassword);

        when(this.userPortOut.findByUsername(expectedUsername)).thenReturn(Optional.empty());
        when(this.passwordEncoder.encode(expectedPassword)).thenReturn(encryptedPassword);
        when(this.userPortOut.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity savedUser = this.userService.registerUser(expectedUsername, expectedPassword);

        verify(this.userPortOut, times(1)).save(any(UserEntity.class));
        assertNotNull(savedUser);
        assertEquals(expectedUsername, savedUser.getUsername());
        assertEquals(encryptedPassword, savedUser.getPassword());
    }

    @Test
    void shouldThrowDataIntegratyViolationExceptionWhenUserAlreadyRegistered() {
        var expectedUsername = "robbor";
        var expectedPassword = "robbor_pass";

        UserEntity userEntity = new UserEntity(expectedUsername, "encrypted_pass");

        when(this.userPortOut.findByUsername(any())).thenReturn(Optional.of(userEntity));
        DataIntegratyViolationException thrownException = assertThrows(DataIntegratyViolationException.class, () -> {
            this.userService.registerUser(expectedUsername, expectedPassword);
        });

        assertEquals("User already registered", thrownException.getMessage());
        verify(this.userPortOut, times(0)).save(any(UserEntity.class));
    }

    @Test
    void shouldFindUserByUsernamesuccessfully() {
        var expectedUsername = "robbor";
        var expectedPassword = "robbor_pass";

        UserEntity userEntity = new UserEntity(expectedUsername, expectedPassword);

        when(this.userPortOut.findByUsername(any())).thenReturn(Optional.of(userEntity));
        UserEntity user = this.userService.findByUsername(expectedUsername);

        verify(this.userPortOut, times(1)).findByUsername(expectedUsername);
        assertNotNull(user);
        assertEquals(userEntity.getId(), user.getId());
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenUserNotFound() {
        when(this.userPortOut.findByUsername(any())).thenReturn(Optional.empty());

        ObjectNotFoundException thrownException = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.findByUsername(any());
        });

        assertEquals("User not found", thrownException.getMessage());
        verify(this.userPortOut, times(1)).findByUsername(any());
    }

    @Test
    void shouldLoginSuccessfully() {
        var expectedUsername = "robbor";
        var expectedPassword = "robbor_pass";
        var encryptedPassword = "encrypted_pass";
        var expectedToken = "generated_token";

        UserEntity userEntity = new UserEntity(expectedUsername, encryptedPassword);

        when(this.userPortOut.findByUsername(expectedUsername)).thenReturn(Optional.of(userEntity));
        when(this.passwordEncoder.matches(expectedPassword, encryptedPassword)).thenReturn(true);

        try (MockedStatic<JwtUtil> mockedJwtUtil = mockStatic(JwtUtil.class)) {
            mockedJwtUtil.when(() -> JwtUtil.generateToken(expectedUsername)).thenReturn(expectedToken);

            String token = this.userService.login(expectedUsername, expectedPassword);

            verify(this.userPortOut, times(1)).findByUsername(expectedUsername);
            verify(this.passwordEncoder, times(1)).matches(expectedPassword, encryptedPassword);
            assertNotNull(token);
            assertEquals(expectedToken, token);
        }
    }


    @Test
    void shouldThrowUnauthorizedExceptionWhenInvalidCredentials() {
        var expectedUsername = "robbor";
        var expectedPassword = "wrong_pass";

        when(this.userPortOut.findByUsername(expectedUsername)).thenReturn(Optional.of(new UserEntity(expectedUsername, "robbor_pass")));
        when(this.passwordEncoder.matches(expectedPassword, "robbor_pass")).thenReturn(false);

        UnauthorizedException thrownException = assertThrows(UnauthorizedException.class, () -> {
            this.userService.login(expectedUsername, expectedPassword);
        });

        assertEquals("Invalid credentials", thrownException.getMessage());
        verify(this.userPortOut, times(1)).findByUsername(expectedUsername);
        verify(this.passwordEncoder, times(1)).matches(expectedPassword, "robbor_pass");
    }
}