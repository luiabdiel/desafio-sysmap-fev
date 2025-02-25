package br.com.luiabdiel.ms_email_v1.core.domain.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {

    private String to;

    private String subject;

    private String body;
}
