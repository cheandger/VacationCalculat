package org.shrek.VacationCalculat.services.impl;

import org.shrek.VacationCalculat.exceptions.IncorrectDateRangeException;
import org.shrek.VacationCalculat.exceptions.ParametersValidationException;
import org.shrek.VacationCalculat.services.HolidayDictionaryService;
import org.shrek.VacationCalculat.services.VacationCalculateService;
import org.shrek.VacationCalculat.validators.ParamsValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class VacationCalculateServiceImpl implements VacationCalculateService {

    private final HolidayDictionaryService holidayService;

    public VacationCalculateServiceImpl(HolidayDictionaryService holidayService) {
        this.holidayService = holidayService;
    }

    @Override
    public Long calculate(Map<String, String> params) {
        //проверяем входные параметры
        DataBinder dataBinder = new DataBinder(params);
        dataBinder.addValidators(new ParamsValidator());
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()) {
            ObjectError objectError = dataBinder.getBindingResult().getAllErrors().get(0);
            throw new ParametersValidationException(objectError.getDefaultMessage());
        }
        Optional<Long> avgPay = Optional.of(new BigDecimal(params.get("avgPay")).movePointRight(2).unscaledValue().longValue());
        Optional<LocalDate> dateFrom = Optional.ofNullable(params.get("from"))
                .map(from -> LocalDate.parse(from, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())));
        Optional<LocalDate> dateTo = Optional.ofNullable(params.get("to"))
                .map(to -> LocalDate.parse(to, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())));
        //если указаны даты отпуска то это приорететнее. считаем с учетом праздников
        if (dateFrom.isPresent() && dateTo.isPresent()) {
            return calculate(avgPay.get(),
                    dateFrom.get(),
                    dateTo.get());
        }
        //если указано количество дней считаем по среднему в текущем месяце без учета праздников
        Optional<Integer> days = Optional.ofNullable(params.get("days")).map(Integer::valueOf);
        if (days.isPresent()) {
            return calculate(avgPay.get(), days.get());
        }

        //TODO на всякий пожарный оставил. Такая ситуация невозможна тк параметры провалидированы
        throw new IncorrectDateRangeException();
    }

    @Override
    public Long calculate(Long avgPay, Integer daysCount) {
        return avgPay / 31 * daysCount;
    }

    @Override
    public Long calculate(Long avgPay, LocalDate from, LocalDate to) {
        if (!from.isBefore(to)) {
            throw new IncorrectDateRangeException();
        }
        LocalDate localDate = LocalDate.of(from.getYear(), from.getMonth(), from.getDayOfMonth());
        while (localDate.isBefore(to)) {
            //если празничный день попал на отпуск добавляем 1 день к отпуску
            if (holidayService.isHoliday(localDate)) {
                to = to.plusDays(1);
            }
            localDate = localDate.plusDays(1);
        }
        Long retVal = 0L;
        Calendar calendar = Calendar.getInstance();
        Integer workDays = holidayService.getWorkDays(from.getMonth().getValue(), from.getYear());
        //Если отпуск весь в одном месяце
        if (from.getMonth().equals(to.getMonth())){
            return avgPay / workDays * (to.getDayOfMonth()-from.getDayOfMonth());
        }

        //высчитываем отпускные отдельно за начальный месяц
        calendar.set(Calendar.MONTH, from.getMonthValue()-1);
        int vacationDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - from.getDayOfMonth();
        retVal += avgPay / workDays * vacationDays;
        //высчитываем отпускные за второй месяц
        workDays = holidayService.getWorkDays(to.getMonth().getValue(), to.getYear());
        retVal += avgPay / workDays * to.getDayOfMonth();

        return retVal;
    }
}
