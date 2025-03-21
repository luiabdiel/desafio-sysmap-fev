package br.com.luiabdiel.ms_customer_v1.api.rest.controller;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.UserEntity;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.UserPortIn;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.dto.UserRequestDto;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerImpTest {

    @InjectMocks
    private AuthControllerImp authController;

    @Mock
    private UserPortIn userPortIn;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void shouldRegisterUserSuccessfully() {
        var expectedUsername = "roooob";
        var expectedPassword = "roooobroooobroooob";

        UserRequestDto requestDto = new UserRequestDto(expectedUsername, expectedPassword);
        UserEntity userEntity = new UserEntity(expectedUsername, expectedPassword);
        UserResponseDto responseDto = new UserResponseDto(1L, expectedUsername);

        when(userPortIn.registerUser(expectedUsername, expectedPassword)).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserResponseDto.class)).thenReturn(responseDto);

        ResponseEntity<UserResponseDto> response = authController.registerUser(requestDto);

        verify(userPortIn, times(1)).registerUser(expectedUsername, expectedPassword);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(expectedUsername, response.getBody().getUsername());
    }

    @Test
    void shouldLoginUserSuccessfully() {
        var expectedUsername = "roooob";
        var expectedPassword = "roooobroooobroooob";

        var token = "valid_token";
        UserRequestDto requestDto = new UserRequestDto(expectedUsername, expectedPassword);

        when(userPortIn.login(expectedUsername, expectedPassword)).thenReturn(token);

        ResponseEntity<String> response = authController.login(requestDto);

        verify(userPortIn, times(1)).login(expectedUsername, expectedPassword);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody());
    }
}