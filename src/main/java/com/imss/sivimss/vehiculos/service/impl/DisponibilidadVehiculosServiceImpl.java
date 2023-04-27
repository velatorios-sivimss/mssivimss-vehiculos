package com.imss.sivimss.vehiculos.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.vehiculos.beans.DisponibilidadVehiculos;
import com.imss.sivimss.vehiculos.model.request.VehiculoRequest;
import com.imss.sivimss.vehiculos.model.request.ReporteDto;
import com.imss.sivimss.vehiculos.model.request.UsuarioDto;
import com.imss.sivimss.vehiculos.service.DisponibilidadVehiculosService;
import com.imss.sivimss.vehiculos.util.AppConstantes;
import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.LogUtil;
import com.imss.sivimss.vehiculos.util.MensajeResponseUtil;
import com.imss.sivimss.vehiculos.util.ProviderServiceRestTemplate;
import com.imss.sivimss.vehiculos.util.Response;

@Service
public class DisponibilidadVehiculosServiceImpl implements DisponibilidadVehiculosService {

	@Value("${endpoints.dominio-consulta-paginado}")
	private String urlConsultaGenericoPaginado;

	@Value("${endpoints.dominio-consulta}")
	private String urlConsultaGenerica;


	@Value("${formato_fecha}")
	private String formatoFecha;
	

	@Value("${url_reportes.pdf-DisponibilidadVehiculos}")
	private String nombrePdfReportes;

	@Value("${endpoints.ms-reportes}")
	private String urlReportes;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	@Autowired
	private LogUtil logUtil;
	
	DisponibilidadVehiculos vehiculo;

	private static final String ALTA = "alta";
	private static final String BAJA = "baja";
	private static final String MODIFICACION = "modificacion";
	private static final String CONSULTA = "consulta";
	private static final String GENERA_DOCUMENTO = "Genera_Documento";

	private static final String NO_SE_ENCONTRO_INFORMACION = "45"; // No se encontró información relacionada a tu
																	// búsqueda.
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO = "64"; // Error en la descarga del documento.Intenta nuevamente.
	private static final String AGREGADO_CORRECTAMENTE = "30"; // Agregado correctamente.
	
	@Override
	public Response<?> consultaVehiculos(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".consultaVehiculos", this.getClass().getPackage().toString(), "Resilencia", CONSULTA, authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse(
				providerRestTemplate.consumirServicio(vehiculo.consultarDisponibilidadVehiculo(request).getDatos(),
						urlConsultaGenericoPaginado, authentication),
				NO_SE_ENCONTRO_INFORMACION);
	}

	@Override
	public Response<?> consultaVehiculoDisponible(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName() + ".consultaVehiculoDisponible", this.getClass().getPackage().toString(), "Resilencia", CONSULTA, authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse(
				providerRestTemplate.consumirServicio(vehiculo.consultaDetalleVehiculo(request).getDatos(),
						urlConsultaGenericoPaginado, authentication),
				NO_SE_ENCONTRO_INFORMACION);
	}
	
	@Override
	public Response<?> consultaODS(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName() + ".consultaODS", this.getClass().getPackage().toString(), "Resilencia", CONSULTA, authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse(
				providerRestTemplate.consumirServicio(vehiculo.consultaDetalleODS(request).getDatos(),
						urlConsultaGenericoPaginado, authentication),
				NO_SE_ENCONTRO_INFORMACION);
	}

	@Override
	public Response<?> consultaOperador(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName() + ".consultaOperador", this.getClass().getPackage().toString(), "Resilencia", CONSULTA, authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse(
				providerRestTemplate.consumirServicio(vehiculo.consultaOperador(request).getDatos(),
						urlConsultaGenericoPaginado, authentication),
				NO_SE_ENCONTRO_INFORMACION);
	}
	@Override
	public Response<?> consultarVelatorio(DatosRequest request, Authentication authentication)
			throws IOException {
		vehiculo = new DisponibilidadVehiculos ();
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName() + ".consultaVlatorios", this.getClass().getPackage().toString(), "Resilencia", CONSULTA, authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse(
		providerRestTemplate.consumirServicio(vehiculo.obtenerVelatorio(request).getDatos(),
				urlConsultaGenerica, authentication),
		NO_SE_ENCONTRO_INFORMACION);
	}

	@Override
	public Response<?> registraSalidaVehiculo(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo  = new DisponibilidadVehiculos (vehiculoRequest);
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		vehiculo.setIdUsuarioAlta(usuarioDto.getIdUsuario());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName() + ".registraSalidaVehiculos", this.getClass().getPackage().toString(), "Resilencia", ALTA, authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse(
		providerRestTemplate.consumirServicio(vehiculo.registrarVehiculoSalida(request).getDatos(),
				urlConsultaGenerica, authentication),
		AGREGADO_CORRECTAMENTE);
	}

	@Override
	public Response<?> registraEntradaVehiculo(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo  = new DisponibilidadVehiculos (vehiculoRequest);
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		vehiculo.setIdUsuarioAlta(usuarioDto.getIdUsuario());
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName() + ".registraSalidaVehiculos", this.getClass().getPackage().toString(), "Resilencia", ALTA, authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse(
		providerRestTemplate.consumirServicio(vehiculo.registrarVehiculoEntrada(request).getDatos(),
				urlConsultaGenerica, authentication),
		AGREGADO_CORRECTAMENTE);
	}

	@Override
	public Response<?> generarDocumento(DatosRequest request, Authentication authentication)throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		ReporteDto reporteDto= gson.fromJson(datosJson, ReporteDto.class);
		Map<String, Object> envioDatos = vehiculo.generarReportePDF(reporteDto,nombrePdfReportes);
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName() + ".generaDocumento", this.getClass().getPackage().toString(), "Resilencia", GENERA_DOCUMENTO, authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication)
				, ERROR_AL_DESCARGAR_DOCUMENTO);
		
	}
}
