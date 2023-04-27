package com.imss.sivimss.vehiculos.service;

import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.Response;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface MttoVehicularService {
    Response<?> insertarMttoVehicular(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> modificarMttoVehicular(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> modificarEstatusMttoVehicular(DatosRequest request, Authentication authentication) throws IOException;
}
