package org.shrek.VacationCalculat.exceptions;

public class BusinessException extends CustomException{

    public BusinessException(int code, String message) {
        super(code, message);
    }
}
