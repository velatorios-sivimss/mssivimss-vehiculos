package com.imss.sivimss.vehiculos.controller;

import com.imss.sivimss.vehiculos.service.BuscarVehiculosService;
import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class BuscarVehiculosController {
    private static Logger log = LogManager.getLogger(BuscarVehiculosController.class);

    @Autowired
    private BuscarVehiculosService buscarVehiculosService;

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("vehiculos/buscar")
    public Response<?> getVehiculos(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene lista de vehiculos");
        return buscarVehiculosService.getVehiculos(request, authentication);
    }
}
