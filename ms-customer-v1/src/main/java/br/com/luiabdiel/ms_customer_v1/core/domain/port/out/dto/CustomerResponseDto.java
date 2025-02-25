package br.com.luiabdiel.ms_customer_v1.core.domain.port.out.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {

    private Long id;

    private String name;

    private String email;
}
