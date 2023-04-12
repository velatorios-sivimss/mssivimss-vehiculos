package com.imss.sivimss.vehiculos.controller;

import com.imss.sivimss.vehiculos.service.MttoCatalogosService;
import com.imss.sivimss.vehiculos.util.Response;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class MttoCatalogosController {
    private static Logger log = LogManager.getLogger(MttoCatalogosController.class);

    private MttoCatalogosService mttoCatalogosService;

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catMttoUsoVehiculo")
    public Response<?> getCatMttoUsoVehiculo(Authentication authentication) throws IOException {
        log.info("Obtiene lista  de uso vehicular");
        return mttoCatalogosService.getCatMttoUsoVehiculo(authentication);
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catMttoModalidad")
    public Response<?> getCatMttoModalidad(Authentication authentication) throws IOException {
        log.info("Obtiene lista de modalidad de mantenimiento");
        return mttoCatalogosService.getCatMttoModalidad(authentication);
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catNivelOficina")
    public Response<?> getCatNivelOficina(Authentication authentication) throws IOException {
        log.info("Obtiene lista de nivel de oficina");
        return mttoCatalogosService.getCatNivelOficina(authentication);
    }


    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catMttoTipo")
    public Response<?> getCatMttoTipo(Authentication authentication) throws IOException {
        log.info("Obtiene lista de tipo de mtto");
        return mttoCatalogosService.getCatMttoTipo(authentication);
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catMttoEstado")
    public Response<?> getCatMttoEstado(Authentication authentication) throws IOException {
        log.info("Obtiene lista de estado de mtto");
        return mttoCatalogosService.getCatMttoEstado(authentication);
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catProveedores")
    public Response<?> getCatProveedores(Authentication authentication) throws IOException {
        log.info("Obtiene lista de proveedores");
        return mttoCatalogosService.getCatProveedores(authentication);
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catMttoNivel")
    public Response<?> getCatMttoNivel(Authentication authentication) throws IOException {
        log.info("Obtiene lista de niveles");
        return mttoCatalogosService.getCatMttoNivel(authentication);
    }
}
