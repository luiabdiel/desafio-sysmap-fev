package br.com.luiabdiel.ms_customer_v1.api.rest.controller;

import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.UserPortIn;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.dto.UserRequestDto;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthControllerImp implements AuthController {

    private final UserPortIn userPortIn;

    @Override
    public ResponseEntity<UserResponseDto> registerUser(UserRequestDto userRequestDto) {
        log.info("[CONTROLLER - AuthControllerImp.registerUser] - Registrando usuário com nome de usuário: {}", userRequestDto.getUsername());
        UserResponseDto userResponseDto  = this.userPortIn.registerUser(userRequestDto.getUsername(), userRequestDto.getPassword());

        log.info("[CONTROLLER - AuthControllerImp.registerUser] - Usuário registrado com sucesso: {}", userRequestDto.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    @Override
    public ResponseEntity<String> login(UserRequestDto userRequestDto) {
        log.info("[CONTROLLER - AuthControllerImp.login] - Tentando login para o usuário: {}", userRequestDto.getUsername());
        String token = this.userPortIn.login(userRequestDto.getUsername(), userRequestDto.getPassword());

        log.info("[CONTROLLER - AuthControllerImp.login] - Login bem-sucedido para o usuário: {}", userRequestDto.getUsername());
        return ResponseEntity.ok(token);
    }
}
