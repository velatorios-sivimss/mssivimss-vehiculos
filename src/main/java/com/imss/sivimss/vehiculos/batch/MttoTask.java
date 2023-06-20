package com.imss.sivimss.vehiculos.batch;

import com.imss.sivimss.vehiculos.service.EstatusMttoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@Component
public class MttoTask {
    @Autowired
    private EstatusMttoService estatusMttoService;

    @Scheduled(cron = "0  0/30 * * * ?")
    public void everyFiveSeconds() throws IOException, ParseException {
        System.out.println("Periodic task: " + new Date());
        estatusMttoService.validarEstatusMtto();
    }
}
