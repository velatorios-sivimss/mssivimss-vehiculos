package com.imss.sivimss.vehiculos.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.Response;


public interface DisponibilidadVehiculosService {

	Response<?> consultaVehiculos(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> consultaVehiculosCalendario(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> consultaVehiculoDisponible(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> consultaVehiculoDetallexDia(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> consultaOperador(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> consultaODS(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> consultarVelatorio(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> registraEntradaVehiculo(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> registraSalidaVehiculo(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> generarDocumento(DatosRequest request, Authentication authentication) throws IOException;
}
