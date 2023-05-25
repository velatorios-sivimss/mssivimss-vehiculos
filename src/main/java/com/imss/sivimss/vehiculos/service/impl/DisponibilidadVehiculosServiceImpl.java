package com.imss.sivimss.vehiculos.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DisponibilidadVehiculosServiceImpl implements DisponibilidadVehiculosService {

	@Value("${endpoints.mod-catalogos}")
	private String urlModCatalogos;

	private static final String CONSULTAR = "/consulta";
	private static final String CONSULTAR_PAGINADO = "/paginado";
	private static final String ACTUALIZAR = "/actualizar";


	@Value("${formato_fecha}")
	private String formatoFecha;
	

	@Value("${plantilla.pdf-DisponibilidadVehiculos}")
	private String nombrePdfReportes;

	@Value("${url_reportes.ms-reportes}")
	private String urlReportes;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	@Autowired
	private LogUtil logUtil;
	
	DisponibilidadVehiculos vehiculo;

	private static final String ALTA = "alta";
	private static final String CONSULTA = "consulta";
	private static final String GENERA_DOCUMENTO = "Genera_Documento";

	private static final String NO_SE_ENCONTRO_INFORMACION = "45"; // No se encontró información relacionada a tu
																	// búsqueda.
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO = "64"; // Error en la descarga del documento.Intenta nuevamente.
	private static final String AGREGADO_CORRECTAMENTE = "30"; // Agregado correctamente.
	private static final String ERROR_QUERY = "Error al ejecutar el query ";
	private static final String FALLO_EJECUTAR_QUERY = "Fallo al ejecutar el query: ";
	
	private String formatoHora = "%H:%i";
	
	Response<Object> response;
	
	@Override
	public Response<Object> consultaVehiculos(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".consultaVehiculos", this.getClass().getPackage().toString(), "consultaVehiculos", CONSULTA, authentication, null);
			response = providerRestTemplate.consumirServicio(vehiculo.consultarDisponibilidadVehiculos(request, formatoFecha, formatoHora).getDatos(),	urlModCatalogos + CONSULTAR_PAGINADO, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse(response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			String consulta = vehiculo.consultarDisponibilidadVehiculos(request, formatoFecha, formatoHora).getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error( ERROR_QUERY+ decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_EJECUTAR_QUERY + decoded, CONSULTA,
					authentication, null);
			throw new IOException("52", e.getCause());
		}
	}


	@Override
	public Response<Object> consultaVehiculosCalendario(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".consultaVehiculosCalendario", this.getClass().getPackage().toString(), "consultaVehiculosCalendario", CONSULTA, authentication, null);
				response = providerRestTemplate.consumirServicio(vehiculo.consultarDisponibilidadVehiculosCalendario(request, formatoFecha).getDatos(),	urlModCatalogos + CONSULTAR, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			String consulta = vehiculo.consultarDisponibilidadVehiculosCalendario(request, formatoFecha).getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error( ERROR_QUERY+ decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_EJECUTAR_QUERY + decoded, CONSULTA,
					authentication, null);
			throw new IOException("52", e.getCause());
		}
	}
	
	@Override
	public Response<Object> consultaVehiculoDisponible(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".consultaVehiculoDisponible", this.getClass().getPackage().toString(), "consultaVehiculoDisponible", CONSULTA, authentication, null);
			response = providerRestTemplate.consumirServicio(vehiculo.consultaDetalleVehiculo(request).getDatos(),
					urlModCatalogos + CONSULTAR, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			String consulta = vehiculo.consultaDetalleVehiculo(request).getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error(ERROR_QUERY + decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_EJECUTAR_QUERY + decoded, CONSULTA,
					authentication, null);
			throw new IOException("52", e.getCause());
		}
	}
	

	@Override
	public Response<Object> consultaVehiculoDetallexDia(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".consultaVehiculoDetalleDia", this.getClass().getPackage().toString(), "consultaVehiculoDetalleDia", CONSULTA, authentication, null);
			response = providerRestTemplate.consumirServicio(vehiculo.consultaDetalleVehiculoxDia(request, formatoFecha, formatoHora).getDatos(),
					urlModCatalogos + CONSULTAR, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			String consulta = vehiculo.consultaDetalleVehiculoxDia(request, formatoFecha, formatoHora).getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error(ERROR_QUERY + decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_EJECUTAR_QUERY + decoded, CONSULTA,
					authentication, null);
			throw new IOException("52", e.getCause());
		}
	}
	
	@Override
	public Response<Object> consultaODS(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".consultaODS", this.getClass().getPackage().toString(), "consultaODS", CONSULTA, authentication, null);
			response = providerRestTemplate.consumirServicio(vehiculo.consultaDetalleODS(request).getDatos(),
					urlModCatalogos + CONSULTAR_PAGINADO, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse( response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			String consulta = vehiculo.consultaDetalleODS(request).getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error(ERROR_QUERY + decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_EJECUTAR_QUERY + decoded, CONSULTA,
					authentication, null);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<Object> consultaOperador(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".consultaOperador", this.getClass().getPackage().toString(), "consultaOperador", CONSULTA, authentication, null);
			response = providerRestTemplate.consumirServicio(vehiculo.consultaOperador(request).getDatos(),
					urlModCatalogos + CONSULTAR_PAGINADO, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse( response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			String consulta = vehiculo.consultaDetalleODS(request).getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error(ERROR_QUERY + decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_EJECUTAR_QUERY + decoded, CONSULTA,
					authentication, null);
			throw new IOException("52", e.getCause());
		}
	}
	@Override
	public Response<Object> consultarVelatorio(DatosRequest request, Authentication authentication)
			throws IOException {
		vehiculo = new DisponibilidadVehiculos ();
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName() + ".consultaVlatorios", this.getClass().getPackage().toString(), "Resilencia", CONSULTA, authentication, null);
		
		return MensajeResponseUtil.mensajeConsultaResponse(
		providerRestTemplate.consumirServicio(vehiculo.obtenerVelatorio(request).getDatos(),
				urlModCatalogos + CONSULTAR, authentication),
		NO_SE_ENCONTRO_INFORMACION);
	}

	@Override
	public Response<Object> registraSalidaVehiculo(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo  = new DisponibilidadVehiculos (vehiculoRequest);
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		vehiculo.setIdUsuarioAlta(usuarioDto.getIdUsuario());
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".registraSalidaVehiculo", this.getClass().getPackage().toString(), "registraSalidaVehiculo", ALTA, authentication, null);
			response = providerRestTemplate.consumirServicio(vehiculo.actualizaVehiculosParaSalir(request).getDatos(),
					urlModCatalogos + ACTUALIZAR, authentication);
			response = providerRestTemplate.consumirServicio(vehiculo.registrarVehiculoSalida(request).getDatos(),
					urlModCatalogos + CONSULTAR, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response, AGREGADO_CORRECTAMENTE);
		} catch (Exception e) {
			String consulta = vehiculo.registrarVehiculoSalida(request).getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error(ERROR_QUERY + decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_EJECUTAR_QUERY + decoded, ALTA,
					authentication, null);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<Object> registraEntradaVehiculo(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo  = new DisponibilidadVehiculos (vehiculoRequest);
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		vehiculo.setIdUsuarioAlta(usuarioDto.getIdUsuario());
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".registraEntradaVehiculo", this.getClass().getPackage().toString(), "registraEntradaVehiculo", ALTA, authentication, null);
			response = providerRestTemplate.consumirServicio(vehiculo.actualizaVehiculosEntrada(request).getDatos(),
					urlModCatalogos + ACTUALIZAR, authentication);
			response = providerRestTemplate.consumirServicio(vehiculo.registrarVehiculoEntrada(request).getDatos(), 	urlModCatalogos + CONSULTAR, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response, AGREGADO_CORRECTAMENTE);
		} catch (Exception e) {
			String consulta = vehiculo.registrarVehiculoSalida(request).getDatos().get(AppConstantes.QUERY).toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error(ERROR_QUERY + decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_EJECUTAR_QUERY + decoded, ALTA,
					authentication, null);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<Object> generarDocumento(DatosRequest request, Authentication authentication)throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		ReporteDto reporteDto= gson.fromJson(datosJson, ReporteDto.class);
		Map<String, Object> envioDatos = vehiculo.generarReportePDF(reporteDto,nombrePdfReportes);
		try {
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName()+ ".generarDocumento", this.getClass().getPackage().toString(), "generarDocumento", GENERA_DOCUMENTO, authentication, null);
			response = providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response, ERROR_AL_DESCARGAR_DOCUMENTO);
		} catch (Exception e) {
			String consulta = vehiculo.generarReportePDF(reporteDto, nombrePdfReportes).get("condicion").toString();
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error(ERROR_QUERY + decoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), FALLO_EJECUTAR_QUERY + consulta, GENERA_DOCUMENTO,
					authentication, null);
			throw new IOException("52", e.getCause());
		}
		
	}
}
