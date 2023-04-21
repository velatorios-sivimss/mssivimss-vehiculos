package com.imss.sivimss.vehiculos.service;

import com.imss.sivimss.vehiculos.util.Response;
import org.springframework.security.core.Authentication;

import java.io.IOException;

public interface MttoCatalogosService {
    Response<?> getCatMttoUsoVehiculo(Authentication authentication) throws IOException;
    Response<?> getCatMttoModalidad(Authentication authentication) throws IOException;
    Response<?> getCatNivelOficina(Authentication authentication) throws IOException;
    Response<?> getCatMttoTipo(Authentication authentication) throws IOException;
    Response<?> getCatMttoEstado(Authentication authentication) throws IOException;
    Response<?> getCatProveedores(Authentication authentication) throws IOException;
    Response<?> getCatMttoNivel(Authentication authentication) throws IOException;
}
