package br.com.luiabdiel.ms_customer_v1.core.domain.application.service;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.UserEntity;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.in.UserPortIn;
import br.com.luiabdiel.ms_customer_v1.infrastructure.persistence.UserIntegrator;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.DataIntegratyViolationException;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.ObjectNotFoundException;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.UnauthorizedException;
import br.com.luiabdiel.ms_customer_v1.shared.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserPortIn {

    private final UserIntegrator userPortOut;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity registerUser(String username, String password) {
        log.info("[PORT IN - UserService.registerUser] - Registrando usuário: {}", username);
        this.userPortOut.
                findByUsername(username)
                .ifPresent(x -> {
                    log.error("[PORT IN - UserService.registerUser] - Usuário já registrado: {}", username);
                    throw new DataIntegratyViolationException("User already registered");
                });

        String encryptedPassword = this.passwordEncoder.encode(password);
        UserEntity user = new UserEntity(username, encryptedPassword);

        UserEntity savedUser = this.userPortOut.save(user);

        log.info("[PORT IN - UserService.registerUser] - Usuário registrado com sucesso: {}", savedUser.getUsername());
        return savedUser;
    }

    @Override
    public UserEntity findByUsername(String username) {
        log.info("[PORT IN - UserService.findByUsername] - Buscando usuário: {}", username);
        return this.userPortOut.findByUsername(username)
                                .orElseThrow(() -> {
                                    log.error("[PORT IN - UserService.findByUsername] - Usuário não encontrado: {}", username);
                                    return new ObjectNotFoundException("User not found");
                                });
    }

    @Override
    public String login(String username, String password) {
        log.info("[PORT IN - UserService.login] - Tentando login para o usuário: {}", username);
        UserEntity userEntity = this.userPortOut.findByUsername(username)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> {
                    log.error("[PORT IN - UserService.login] - Credenciais inválidas para o usuário: {}", username);
                    return new UnauthorizedException("Invalid credentials");
                });

        log.info("[PORT IN - UserService.login] - Login bem-sucedido para o usuário: {}", username);
        return JwtUtil.generateToken(userEntity.getUsername());
    }
}
