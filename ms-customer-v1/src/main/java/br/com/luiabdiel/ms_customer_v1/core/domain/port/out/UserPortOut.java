package br.com.luiabdiel.ms_customer_v1.core.domain.port.out;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.UserEntity;

import java.util.Optional;

public interface UserPortOut {

    Optional<UserEntity> findByUsername(String username);

    UserEntity save(UserEntity userEntity);
}
