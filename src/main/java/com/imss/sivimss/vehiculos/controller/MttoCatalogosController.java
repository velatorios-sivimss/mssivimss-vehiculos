package com.imss.sivimss.vehiculos.controller;

import com.imss.sivimss.vehiculos.service.MttoCatalogosService;
import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.ProviderServiceRestTemplate;
import com.imss.sivimss.vehiculos.util.Response;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class MttoCatalogosController {
    private static Logger log = LogManager.getLogger(MttoCatalogosController.class);

    @Autowired
    private ProviderServiceRestTemplate restTemplate;

    @Autowired
    private MttoCatalogosService mttoCatalogosService;

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catMttoUsoVehiculo")
    public CompletableFuture<?> getCatMttoUsoVehiculo(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene lista  de uso vehicular");
        Response<?> response = mttoCatalogosService.getCatMttoUsoVehiculo(authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catMttoModalidad")
    public CompletableFuture<?> getCatMttoModalidad(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene lista de modalidad de mantenimiento");
        Response<?> response = mttoCatalogosService.getCatMttoModalidad(authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catNivelOficina")
    public CompletableFuture<?> getCatNivelOficina(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene lista de nivel de oficina");
        Response<?> response =  mttoCatalogosService.getCatNivelOficina(authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
    }


    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catMttoTipo")
    public CompletableFuture<?> getCatMttoTipo(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene lista de tipo de mtto");
        Response<?> response =  mttoCatalogosService.getCatMttoTipo(authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catMttoEstado")
    public CompletableFuture<?> getCatMttoEstado(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene lista de estado de mtto");
        Response<?> response =  mttoCatalogosService.getCatMttoEstado(authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catProveedores")
    public CompletableFuture<?> getCatProveedores(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene lista de proveedores");
        Response<?> response = mttoCatalogosService.getCatProveedores(authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catMttoNivel")
    public CompletableFuture<?> getCatMttoNivel(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene lista de niveles");
        Response<?> response= mttoCatalogosService.getCatMttoNivel(authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("catalogo/catMttoReporteTipo")
    public CompletableFuture<?> getCatMttoReporteTipo(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene lista tipo de reporte");
        Response<?> response= mttoCatalogosService.getCatMttoReporteTipo(authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
    }

    /**
     * Crea el responseEntity para contestar la petici&oacute;n.
     *
     * @param response
     * @return
     */
    private static ResponseEntity<? extends Response<?>> getResponseEntity(Response<?> response) {
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo()));
    }

    /**
     * fallbacks generico
     *
     * @return respuestas
     */
    private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
                                                  CallNotPermittedException e) {
        Response<?> response = restTemplate.respuestaProvider(e.getMessage());
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
                                                  RuntimeException e) {
        Response<?> response = restTemplate.respuestaProvider(e.getMessage());
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }

    private CompletableFuture<?> fallbackGenerico(@RequestBody DatosRequest request, Authentication authentication,
                                                  NumberFormatException e) {
        Response<?> response = restTemplate.respuestaProvider(e.getMessage());
        return CompletableFuture
                .supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
    }
}
