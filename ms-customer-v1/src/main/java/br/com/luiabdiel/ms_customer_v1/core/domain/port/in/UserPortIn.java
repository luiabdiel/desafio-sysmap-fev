package br.com.luiabdiel.ms_customer_v1.core.domain.port.in;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.UserEntity;

public interface UserPortIn {

    UserEntity registerUser(String username, String password);

    UserEntity findByUsername(String username);

    String login(String username, String password);
}
