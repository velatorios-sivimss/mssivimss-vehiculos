package com.imss.sivimss.vehiculos.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.Response;


public interface DisponibilidadVehiculosService {

	Response<Object> consultaVehiculos(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultaVehiculosCalendario(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultaVehiculoDisponible(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultaVehiculoDetallexDia(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultaOperador(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultaODS(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultarVelatorio(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> registraEntradaVehiculo(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> registraSalidaVehiculo(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> generarDocumento(DatosRequest request, Authentication authentication) throws IOException;
}
