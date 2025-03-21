package br.com.luiabdiel.ms_customer_v1.core.domain.application.service;

import br.com.luiabdiel.ms_customer_v1.core.domain.entity.UserEntity;
import br.com.luiabdiel.ms_customer_v1.infrastructure.persistence.UserIntegrator;
import br.com.luiabdiel.ms_customer_v1.shared.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthUserDetailsService implements UserDetailsService {

    private final UserIntegrator userPortOut;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[USER DETAILS SERVICE - AuthUserDetailsService.loadUserByUsername] - Buscando usuário: {}", username);
        UserEntity user = this.userPortOut
                .findByUsername(username).orElseThrow(() -> {
                    log.error("[USER DETAILS SERVICE - AuthUserDetailsService.loadUserByUsername] - Usuário não encontrado: {}", username);
                    return new ObjectNotFoundException("User not found");
                });

        log.info("[USER DETAILS SERVICE - AuthUserDetailsService.loadUserByUsername] - Usuário encontrado: {}", username);
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("ADMIN" )
                .build();
    }
}
