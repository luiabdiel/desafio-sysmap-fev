package br.com.luiabdiel.ms_customer_v1.shared.exceptions;

public class DataIntegratyViolationException extends RuntimeException {

    public DataIntegratyViolationException(String message) {
        super (message);
    }
}
