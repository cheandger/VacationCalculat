package org.shrek.VacationCalculat.services.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.shrek.VacationCalculat.exceptions.IncorrectDateRangeException;
import org.shrek.VacationCalculat.services.HolidayDictionaryService;
import org.shrek.VacationCalculat.services.VacationCalculateService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class VacationCalculateServiceImplTest {

    final HolidayDictionaryService service = new HolidayDictionaryServiceImpl();
    final VacationCalculateService instance = new VacationCalculateServiceImpl(service);

    @Test
    @DisplayName("Test calculation by days count")
    void calculate() {
        Assertions.assertEquals(100000 / 31 * 10, this.instance.calculate(100000L, 10));
    }

    @Test
    @DisplayName("Test calculation by date from, to ")
    void calculateFromTo() {
        LocalDate from = LocalDate.parse("01.01.2022", DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()));
        LocalDate to = LocalDate.parse("10.01.2022", DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()));
        Long amount = this.instance.calculate(100000L, from, to);
        //13 рабочих дней в январе
        //17 дней отпуска
        //10000000 / 13 * 17=130764
        Assertions.assertEquals(130764, amount);
    }

    @Test
    @DisplayName("Test  calculation from Map")
    void calculateMap() {
        Map<String,String> params=new HashMap<>();
        params.put("avgPay","10000.00");
        params.put("from","15.01.2022");
        params.put("to","04.02.2022");
        //13 рабочих дней в январе
        //19 рабочих дней в феврале
        //1000000/13*16+1000000/19*4=1441292
        Long amount = this.instance.calculate(params);
        Assertions.assertEquals(1441292, amount);
    }

    @Test
    @DisplayName("Test  calculation by date from, to with incorrect range")
    void calculateFromToWrongRange() {
        LocalDate from = LocalDate.parse("10.01.2022", DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()));
        LocalDate to = LocalDate.parse("01.01.2022", DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()));
        Assertions.assertThrows(IncorrectDateRangeException.class, () -> instance.calculate(100000L, from, to));
    }

}
