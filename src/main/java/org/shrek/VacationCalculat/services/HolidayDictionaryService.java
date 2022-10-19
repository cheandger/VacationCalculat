package org.shrek.VacationCalculat.services;

import java.time.LocalDate;

public interface HolidayDictionaryService {
    Boolean isHoliday(LocalDate date);
    Integer getWorkDays(int month,int year);
}
