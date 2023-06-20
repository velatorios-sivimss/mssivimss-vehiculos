package com.imss.sivimss.vehiculos.service;

import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.Response;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.text.ParseException;

public interface MttoVehicularService {
    Response<?> insertarMttoVehicular(DatosRequest request, Authentication authentication) throws IOException, ParseException;
    Response<?> modificarMttoVehicular(DatosRequest request, Authentication authentication) throws IOException;
    Response<?> modificarEstatusMttoVehicular(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> detalleVerifInicio(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> detalleSolicitudMtto(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> detalleRegistroMtto(DatosRequest request, Authentication authentication) throws IOException;
}
