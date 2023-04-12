package com.imss.sivimss.vehiculos.controller;

import com.imss.sivimss.vehiculos.service.MttoVehicularService;
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
public class MttoVehicularController {
    private static Logger log = LogManager.getLogger(MttoVehicularController.class);

    @Autowired
    private MttoVehicularService mttoVehicularService;

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("mttovehicular/agregar")
    public Response<?> mttovehicularAgregar(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Se agrega un nuevo mtto vehicular");
        return mttoVehicularService.insertarMttoVehicular(request, authentication);
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("mttovehicular/modificar")
    public Response<?> mttovehicularActualizar(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Actualizar un mtto vehicular");
        return mttoVehicularService.modificarMttoVehicular(request, authentication);
    }


    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("mttovehicular/cambiar-estatus")
    public Response<?> mttovehicularCambiarEstatus(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Cambiar estatus de un mtto vehicular");
        return mttoVehicularService.modificarEstatusMttoVehicular(request, authentication);
    }

}
