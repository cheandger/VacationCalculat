package org.shrek.VacationCalculat.exceptions;

public class ParametersValidationException extends BusinessException{
    public ParametersValidationException(String message) {
        super(MessageCode.VALIDATION_EXCEPTION, "Параметры не прошли проверку :"+message);
    }
}
