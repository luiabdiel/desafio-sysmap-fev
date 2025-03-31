package br.com.luiabdiel.ms_customer_v1.core.domain.port.in;

import br.com.luiabdiel.ms_customer_v1.core.domain.port.out.dto.UserResponseDto;

public interface UserPortIn {

    UserResponseDto registerUser(String username, String password);

    UserResponseDto findByUsername(String username);

    String login(String username, String password);
}
