package com.imss.sivimss.vehiculos.service;

import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.Response;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.text.ParseException;

public interface BuscarVehiculosService {
    Response<?> getVehiculos(DatosRequest request, Authentication authentication) throws IOException, ParseException;
    Response<?> getReporteEncargado(DatosRequest request, Authentication authentication) throws IOException, ParseException;
    Response<?> getReportePredictivo(DatosRequest request, Authentication authentication) throws IOException, ParseException;
	Response<?> reporteProgramarMttoVehicular(DatosRequest request, Authentication authentication) throws IOException;
	Response<?> reporteEncargado(DatosRequest request, Authentication authentication) throws IOException, ParseException;
	Response<?> reportePredictivo(DatosRequest request, Authentication authentication) throws IOException, ParseException;
}
