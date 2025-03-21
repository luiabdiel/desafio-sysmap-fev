package br.com.luiabdiel.ms_customer_v1.infrastructure.persistence;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.UserEntity;
import br.com.luiabdiel.ms_customer_v1.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserIntegratorTest {

    @InjectMocks
    private UserIntegrator userPortOut;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldFindUserByUsernameSuccessfully() {
        var expectedUsername = "robbor";
        var expectedPassword = "robbor_pass";

        UserEntity userEntity = new UserEntity(expectedUsername, expectedPassword);

        when(this.userRepository.findByUsername(any())).thenReturn(Optional.of(userEntity));
        Optional<UserEntity> userO = this.userPortOut.findByUsername(expectedUsername);

        verify(this.userRepository, times(1)).findByUsername(expectedUsername);
        assert(userO.isPresent());
        assert(userO.get().getUsername().equals(expectedUsername));
    }

    @Test
    void shouldHandleExceptionWhenFindUserByUsernameFails() {
        doThrow(new RuntimeException("Database error")).when(this.userRepository).findByUsername(any());

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            this.userPortOut.findByUsername(any());
        });

        assertEquals("Database error", thrownException.getMessage());
        verify(this.userRepository, times(1)).findByUsername(any());
    }

    @Test
    void shouldSaveUserSuccessfully() {
        var expectedUsername = "robbor";
        var expectedPassword = "robbor_pass";

        UserEntity userEntity = new UserEntity(expectedUsername, expectedPassword);

        when(this.userRepository.save(any())).thenReturn(userEntity);
        UserEntity user = this.userPortOut.save(userEntity);

        verify(this.userRepository, times(1))
                .save(argThat(arg ->
                        Objects.equals(expectedUsername, arg.getUsername()) &&
                        Objects.equals(expectedPassword, arg.getPassword())));
    }

    @Test
    void shouldHandleExceptionWhenSaveUserFails() {
        doThrow(new RuntimeException("Database error")).when(this.userRepository).save(any());

        RuntimeException thrownException = assertThrows(RuntimeException.class, () -> {
            this.userPortOut.save(any());
        });

        assertEquals("Database error", thrownException.getMessage());
        verify(this.userRepository, times(1)).save(any());
    }
}