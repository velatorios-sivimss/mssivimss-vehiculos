package com.imss.sivimss.vehiculos.service;

import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.Response;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface BuscarVehiculosService {
    Response<?> getVehiculos(DatosRequest request, Authentication authentication) throws IOException;
}
