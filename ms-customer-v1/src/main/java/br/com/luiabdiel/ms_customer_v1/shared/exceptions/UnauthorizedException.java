package br.com.luiabdiel.ms_customer_v1.shared.exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
