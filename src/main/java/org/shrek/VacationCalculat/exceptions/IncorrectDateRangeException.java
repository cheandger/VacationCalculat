package org.shrek.VacationCalculat.exceptions;

public class IncorrectDateRangeException extends BusinessException{

    public IncorrectDateRangeException() {
        super(MessageCode.INCORRECT_DATA_RANGE,
                "Дата начала отпуска должна быть меньше даты окончания");
    }
}
