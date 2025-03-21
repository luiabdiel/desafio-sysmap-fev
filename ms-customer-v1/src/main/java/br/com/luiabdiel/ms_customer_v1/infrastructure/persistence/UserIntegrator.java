package br.com.luiabdiel.ms_customer_v1.infrastructure.persistence;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.UserEntity;
import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.UserPortOut;
import br.com.luiabdiel.ms_customer_v1.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserIntegrator implements UserPortOut {

    private final UserRepository userRepository;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        log.info("[PORT OUT - UserIntegrator.findByUsername] - Buscando por username: {}", username);
       try {
           return this.userRepository.findByUsername(username);
       } catch (Exception exception) {
           log.error("[PORT OUT - UserIntegrator.findByUsername] - Erro ao buscar por username: {}", username, exception);
           throw exception;
       }
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        log.info("[PORT OUT - UserIntegrator.save] - Salvando usuário: {}", userEntity);
        try {
            return this.userRepository.save(userEntity);
        } catch (Exception exception) {
            log.error("[PORT OUT - UserIntegrator.save] - Erro ao salvar usuário: {}", userEntity, exception);
            throw exception;
        }
    }
}
