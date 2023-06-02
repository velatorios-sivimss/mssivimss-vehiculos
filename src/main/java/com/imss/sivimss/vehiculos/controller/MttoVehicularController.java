package com.imss.sivimss.vehiculos.controller;

import com.imss.sivimss.vehiculos.service.MttoVehicularService;
import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.ProviderServiceRestTemplate;
import com.imss.sivimss.vehiculos.util.Response;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
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
public class MttoVehicularController {
    private static Logger log = LogManager.getLogger(MttoVehicularController.class);

    @Autowired
    private MttoVehicularService mttoVehicularService;

    @Autowired
    private ProviderServiceRestTemplate restTemplate;

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("mttovehicular/agregar")
    public CompletableFuture<?> mttovehicularAgregar(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Se agrega un nuevo mtto vehicular");
        Response<?> response =   mttoVehicularService.insertarMttoVehicular(request, authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("mttovehicular/modificar")
    public CompletableFuture<?> mttovehicularActualizar(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Actualizar un mtto vehicular");
        Response<?> response =  mttoVehicularService.modificarMttoVehicular(request, authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
    }


    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("mttovehicular/cambiar-estatus")
    public CompletableFuture<?> mttovehicularCambiarEstatus(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
        log.info("Cambiar estatus de un mtto vehicular");
        Response<?> response = mttoVehicularService.modificarEstatusMttoVehicular(request, authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
        }
        
        @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
        @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
        @TimeLimiter(name = "msflujo")
        @PostMapping("mttovehicular/detalle/verificacion-inicio")
        public CompletableFuture<?> detalleVerificacionInicio(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
            log.info("Detalle verificar al inicio de la jornada");
            Response<?> response = mttoVehicularService.detalleVerifInicio(request, authentication);
            return CompletableFuture.supplyAsync(() -> getResponseEntity(response));
        }
        
        @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
        @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
        @TimeLimiter(name = "msflujo")
        @PostMapping("mttovehicular/detalle/solicitud-mtto")
        public CompletableFuture<?> detalleSolicitudMantenimiento(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
            log.info("Detalle solicitud de mantenimiento");
            Response<?> response = mttoVehicularService.detalleSolicitudMtto(request, authentication);
            return CompletableFuture.supplyAsync(() -> getResponseEntity(response));
        }
        
        @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
        @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
        @TimeLimiter(name = "msflujo")
        @PostMapping("mttovehicular/detalle/registro-mtto")
        public CompletableFuture<?> detalleRegistroMantenimiento(@RequestBody DatosRequest request, Authentication authentication) throws IOException {
            log.info("Detalle registro de mantenimiento");
            Response<?> response = mttoVehicularService.detalleRegistroMtto(request, authentication);
            return CompletableFuture.supplyAsync(() -> getResponseEntity(response));
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
