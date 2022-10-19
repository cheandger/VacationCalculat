package org.shrek.VacationCalculat.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class ParamsValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Map<String, String> params = (Map<String, String>) target;
        Optional<String> avgPay = Optional.ofNullable(params.get("avgPay"));
        Optional<String> days = Optional.ofNullable(params.get("days"));
        Optional<String> from = Optional.ofNullable(params.get("from"));
        Optional<String> to = Optional.ofNullable(params.get("to"));
        if (avgPay.isEmpty()) {
            errors.reject("avgPay", "Отсутсвует обязательный параметр avgPay (средняя зарплата за 12 месяцев)");
            return;
        }
        if (!avgPay.get().matches("^\\d*\\.?\\d{0,2}$")) {
            errors.reject("avgPay", "avgPay не валиден (только чмсло с 2 знаками после запятой)" + avgPay);
            return;
        }
        if (days.isEmpty() && (from.isEmpty() || to.isEmpty())) {
            errors.reject("avgPay", "Отсутсвует обязательные параметры days\from\to количество дней отпуска");
            return;
        }
        if ((from.isEmpty() || to.isEmpty()) && !days.get().matches("^\\d+$")) {
            errors.reject("days", "количество дней отпуска days должно быть целым числом " + days);
            return;
        }

        if (from.isPresent() && !from.get().matches("^\\d{2}\\.\\d{2}\\.\\d{4}$")) {
            errors.reject("days", "from дожно быть датой dd.MM.yyyy " + from);
            return;
        }
        if (to.isPresent() && !to.get().matches("^\\d{2}\\.\\d{2}\\.\\d{4}$")) {
            errors.reject("days", "to дожно быть датой dd.MM.yyyy " + to);
            return;
        }

        Optional<LocalDate> dateFrom = Optional.ofNullable(params.get("from"))
                .map(f -> LocalDate.parse(f, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())));
        Optional<LocalDate> dateTo = Optional.ofNullable(params.get("to"))
                .map(t -> LocalDate.parse(t, DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())));

        if (dateFrom.isPresent() && dateTo.isPresent() && dateFrom.get().isAfter(dateTo.get())) {
            errors.reject("days", "from  дожно быть раньше to  " + from + "->" + to);
        }

    }
}
