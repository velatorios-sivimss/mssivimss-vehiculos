package com.imss.sivimss.vehiculos.service;

import com.imss.sivimss.vehiculos.util.DatosRequest;
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
    Response<?> getCatMttoReporteTipo(Authentication authentication) throws IOException;
    Response<?> getCatMttoPeriodo(Authentication authentication) throws IOException;
    Response<?> getCatPlacasVehiculos(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> getCatContratosProveedores(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> getRegistroMtto(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> getMttoTipoModalidad(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> getMttoTipoModalidadDetalle(DatosRequest request, Authentication authentication) throws IOException;
}
