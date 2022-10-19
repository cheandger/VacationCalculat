package org.shrek.VacationCalculat.services;

import java.time.LocalDate;
import java.util.Map;

public interface VacationCalculateService {

    Long calculate(Map<String, String> params);

    Long calculate(Long avgPay, Integer daysCount);

    Long calculate(Long avgPay, LocalDate from, LocalDate to);
}
