package br.com.luiabdiel.ms_customer_v1.core.domain.port.in.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDto {

    @NotBlank(message = "Nome não pode ser vazio")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String name;

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Email inválido")
    private String email;
}
