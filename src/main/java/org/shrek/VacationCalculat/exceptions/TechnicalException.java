package org.shrek.VacationCalculat.exceptions;

public class TechnicalException extends CustomException{
    protected TechnicalException(int code, String message) {
        super(code, message);
    }
}
