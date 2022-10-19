package org.shrek.VacationCalculat.services.impl;

import org.shrek.VacationCalculat.services.HolidayDictionaryService;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class HolidayDictionaryServiceImpl implements HolidayDictionaryService {
    private final Set<Integer> holidays;

    public HolidayDictionaryServiceImpl() {
//        1, 2, 3, 4, 5, 6 и 8 января — Новогодние каникулы;
//        7 января — Рождество Христово;
//        23 февраля — День защитника Отечества;
//        8 марта — Международный женский день;
//        1 мая — Праздник Весны и Труда;
//        9 мая — День Победы;
//        12 июня — День России;
//        4 ноября — День народного единства.
        holidays = new HashSet<>();
        Calendar calendar = Calendar.getInstance();
        Stream.of(new String[]{
                        "01.01",
                        "01.02",
                        "01.03",
                        "01.04",
                        "01.05",
                        "01.06",
                        "01.07",
                        "01.08",
                        "02.23",
                        "03.08",
                        "05.01",
                        "05.09",
                        "06.12",
                        "11.04"
                })
                .forEach(str -> {
                    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                            .appendPattern("MM.dd")
                            .parseDefaulting(ChronoField.YEAR, calendar.get(Calendar.YEAR))
                            .toFormatter(Locale.getDefault());
                    LocalDate localDate = LocalDate.parse(str, formatter);
                    //Если праздничный день выпадает на выходной то переносим на первый рабочий
                    while (localDate.getDayOfWeek() == DayOfWeek.SUNDAY
                            || localDate.getDayOfWeek() == DayOfWeek.SATURDAY
                            || holidays.contains(localDate.getDayOfYear())) {
                        localDate = localDate.plusDays(1);
                    }
                    holidays.add(localDate.getDayOfYear());
                });
    }

    @Override
    public Boolean isHoliday(LocalDate date) {
        return holidays.contains(date.getDayOfYear());
    }

    @Override
    public Integer getWorkDays(int month,int year) {
        LocalDate date = LocalDate.of(year, month, 1);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,month-1);
        calendar.set(Calendar.YEAR,year);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Integer retVal=0;
        while(date.getDayOfMonth()<daysInMonth){
            if(!date.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                    && !date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    && ! holidays.contains(date.getDayOfYear())){
                retVal++;
            }
            date=date.plusDays(1);
        }
        //последний день месяца не попадет в цикл
        if(!date.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                && !date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                && ! holidays.contains(date.getDayOfYear())){
            retVal++;
        }

        return retVal;
    }
}
