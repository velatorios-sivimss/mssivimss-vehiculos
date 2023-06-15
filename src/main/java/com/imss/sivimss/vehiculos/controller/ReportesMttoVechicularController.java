package com.imss.sivimss.vehiculos.controller;

import com.imss.sivimss.vehiculos.service.BuscarVehiculosService;
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
import java.text.ParseException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/")
public class ReportesMttoVechicularController {
    private static Logger log = LogManager.getLogger(ReportesMttoVechicularController.class);

    @Autowired
    private ProviderServiceRestTemplate restTemplate;

    @Autowired
    private BuscarVehiculosService buscarVehiculosService;

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("reporte/mtto/encargado")
    public CompletableFuture<?> getReporteEncargado(@RequestBody DatosRequest request, Authentication authentication) throws IOException, ParseException {
        log.info("Obtiene reporte de encargado");
        Response<?> response =  buscarVehiculosService.getReporteEncargado(request, authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
    }

    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
    @TimeLimiter(name = "msflujo")
    @PostMapping("reporte/mtto/predictivo")
    public CompletableFuture<?> getReportePredictivo(@RequestBody DatosRequest request, Authentication authentication) throws IOException, ParseException {
        log.info("Obtiene reporte predictivo");
        Response<?> response =  buscarVehiculosService.getReportePredictivo(request, authentication);
        return CompletableFuture.supplyAsync(
                () -> getResponseEntity(response)
        );
    }
    
    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
  	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
  	@TimeLimiter(name = "msflujo")
  	@PostMapping("reporte/mtto/vehicular")
  	public CompletableFuture<?> descargarReporteProgramarMttoVehicular(@RequestBody DatosRequest request,Authentication authentication) throws IOException{
  		Response<?> response = buscarVehiculosService.reporteProgramarMttoVehicular(request,authentication);
  		return CompletableFuture
  				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
  	}
    
    @CircuitBreaker(name = "msflujo", fallbackMethod = "fallbackGenerico")
  	@Retry(name = "msflujo", fallbackMethod = "fallbackGenerico")
  	@TimeLimiter(name = "msflujo")
  	@PostMapping("reporte/mtto/reporte-encargado")
  	public CompletableFuture<?> descargarReporteEncargado(@RequestBody DatosRequest request,Authentication authentication) throws IOException, ParseException{
  		Response<?> response = buscarVehiculosService.reporteEncargado(request,authentication);
  		return CompletableFuture
  				.supplyAsync(() -> new ResponseEntity<>(response, HttpStatus.valueOf(response.getCodigo())));
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
