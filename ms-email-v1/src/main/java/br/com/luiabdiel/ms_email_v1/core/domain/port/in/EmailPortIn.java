package br.com.luiabdiel.ms_email_v1.core.domain.port.in;

import br.com.luiabdiel.ms_email_v1.core.domain.port.in.dto.EmailDto;

public interface EmailPortIn {

    public void sendEmail(EmailDto emailDto);
}
