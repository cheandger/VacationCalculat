package org.shrek.VacationCalculat.controllers;

import org.shrek.VacationCalculat.services.VacationCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Validated
public class VacationCalculationController {


    private final VacationCalculateService vacationService;

    public VacationCalculationController(VacationCalculateService vacationService) {
        this.vacationService = vacationService;
    }

    @GetMapping(value = "/calculacte", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> getVacationPayAmount(@RequestParam Map<String, String> params) {

        Long amount = vacationService.calculate(params);
        return ResponseEntity.ok(amount / 100 +
                "." +
                amount % 100);
    }

}
