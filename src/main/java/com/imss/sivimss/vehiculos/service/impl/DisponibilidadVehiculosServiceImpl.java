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
	private static final String NUMERO_FOLIO_NO_EXISTE = "85";//El número de folio no existe.Verifica tu información.
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO = "64"; // Error en la descarga del documento.Intenta nuevamente.
	private static final String REGISTRO_SALIDA = "83"; // Has registrado la salida/término del servicio correctamente. 
	private static final String REGISTRO_ENTRADA = "84"; // Has registrado la entrada/inicio del servicio correctamente.
	private static final String ERROR_QUERY = "Error al ejecutar el query ";
	private static final String CU060_NOMBRE= "Disponibilida De Vehiculos: ";
	private static final String CONSULTA_VEHICULO_DISP = "disp-vehiculos: ";
	private static final String CONSULTA_VEHICULO_DISP_CALE = "VehiculosDisponibles-Calendario: ";
	private static final String CONSULTA_VEHICULO_DISP_ID = "vehiculo-disponible: " ;
	private static final String CONSULTA_VEHICULO_DISP_ID_DIA = "detalle-vehiculo-dia: " ;
	private static final String CONSULTA_ODS = "consulta-ods: " ;
	private static final String CONSULTA_VEHICULO_OPERADOR = "consulta-operador: " ;
	private static final String REGISTRA_ENTRADA_VEHICULO = "salida-vehiculos: " ;
	private static final String REGISTRA_SALIDA_VEHICULO = "entrada-vehiculos: " ;
	private static final String GENERAR_DOCUMENTO = "gen-doc-vehiculos: " ;
	
	
	private String formatoHora = "%H:%i";
	
	Response<Object> response;
	
	@Override
	public Response<Object> consultaVehiculos(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		VehiculoRequest vehiculoRequest = gson.fromJson(datosJson, VehiculoRequest.class);
		vehiculo = new DisponibilidadVehiculos(vehiculoRequest);
		Map<String, Object> envioDatos = vehiculo.consultarDisponibilidadVehiculos(request, formatoFecha, formatoHora).getDatos();
		try {
			log.info( CU060_NOMBRE + CONSULTA_VEHICULO_DISP + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU060_NOMBRE + CONSULTA_VEHICULO_DISP + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "consultaVehiculos", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos,	urlModCatalogos + CONSULTAR_PAGINADO, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse(response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			log.error( CU060_NOMBRE + CONSULTA_VEHICULO_DISP + ERROR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU060_NOMBRE + CONSULTA_VEHICULO_DISP + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded(envioDatos), CONSULTA, authentication);
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
		Map<String, Object> envioDatos = vehiculo.consultarDisponibilidadVehiculosCalendario(request).getDatos();
		try {
			log.info( CU060_NOMBRE + CONSULTA_VEHICULO_DISP_CALE + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU060_NOMBRE + CONSULTA_VEHICULO_DISP_CALE + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "consultaVehiculosCalendario", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + CONSULTAR, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			log.error( CU060_NOMBRE + CONSULTA_VEHICULO_DISP_CALE + ERROR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU060_NOMBRE + CONSULTA_VEHICULO_DISP_CALE + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded(envioDatos), CONSULTA, authentication);
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
		Map<String, Object> envioDatos = vehiculo.consultaDetalleVehiculo(request).getDatos();
		try {
			log.info( CU060_NOMBRE + CONSULTA_VEHICULO_DISP_ID + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU060_NOMBRE + CONSULTA_VEHICULO_DISP_ID + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "consultaVehiculoDisponible", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + CONSULTAR, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			log.error( CU060_NOMBRE + CONSULTA_VEHICULO_DISP_ID + ERROR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU060_NOMBRE + CONSULTA_VEHICULO_DISP_ID + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded(envioDatos), CONSULTA, authentication);
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
		Map<String, Object> envioDatos = vehiculo.consultaDetalleVehiculoxDia(request, formatoFecha, formatoHora).getDatos();
		try {
			log.info( CU060_NOMBRE + CONSULTA_VEHICULO_DISP_ID_DIA + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU060_NOMBRE + CONSULTA_VEHICULO_DISP_ID_DIA + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "consultaVehiculoDetallexDia", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + CONSULTAR, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			log.error( CU060_NOMBRE + CONSULTA_VEHICULO_DISP_ID_DIA + ERROR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU060_NOMBRE + CONSULTA_VEHICULO_DISP_ID_DIA + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded(envioDatos), CONSULTA, authentication);
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
		Map<String, Object> envioDatos = vehiculo.consultaDetalleODS(request).getDatos();
		try {
			log.info( CU060_NOMBRE + CONSULTA_ODS + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU060_NOMBRE + CONSULTA_ODS + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "consultaODS", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos,
					urlModCatalogos + CONSULTAR_PAGINADO, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse( response, NUMERO_FOLIO_NO_EXISTE);
		} catch (Exception e) {
			log.error( CU060_NOMBRE + CONSULTA_ODS + ERROR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU060_NOMBRE + CONSULTA_ODS + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded(envioDatos), CONSULTA, authentication);
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
		Map<String, Object> envioDatos = vehiculo.consultaOperador(request).getDatos();
		try {
			log.info( CU060_NOMBRE + CONSULTA_VEHICULO_OPERADOR + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU060_NOMBRE + CONSULTA_VEHICULO_OPERADOR + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "consultaOperador", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + CONSULTAR_PAGINADO, authentication);
			
			return MensajeResponseUtil.mensajeConsultaResponse( response, NO_SE_ENCONTRO_INFORMACION);
		} catch (Exception e) {
			log.error( CU060_NOMBRE + CONSULTA_VEHICULO_OPERADOR + ERROR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU060_NOMBRE + CONSULTA_VEHICULO_OPERADOR + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded(envioDatos), CONSULTA, authentication);
			throw new IOException("52", e.getCause());
		}
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
		Map<String, Object> envioDatos = vehiculo.actualizaVehiculosParaSalir(request).getDatos();
		try {
			
			  log.info( CU060_NOMBRE + REGISTRA_SALIDA_VEHICULO + queryDecoded(envioDatos)); 
			  logUtil.crearArchivoLog(Level.INFO.toString(), CU060_NOMBRE + REGISTRA_SALIDA_VEHICULO + this.getClass().getSimpleName(),
			  this.getClass().getPackage().toString(), "registraSalidaVehiculo", ALTA, authentication); 
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + ACTUALIZAR, authentication);
			envioDatos = vehiculo.registrarVehiculoSalida(request).getDatos();
			
			log.info( CU060_NOMBRE + REGISTRA_SALIDA_VEHICULO + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU060_NOMBRE + REGISTRA_SALIDA_VEHICULO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "registraSalidaVehiculo", ALTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos,
					urlModCatalogos + CONSULTAR, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response, REGISTRO_SALIDA);
		} catch (Exception e) {
			log.error( CU060_NOMBRE + REGISTRA_SALIDA_VEHICULO + ERROR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU060_NOMBRE + REGISTRA_SALIDA_VEHICULO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded(envioDatos), ALTA, authentication);
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
				Map<String, Object> envioDatos = vehiculo.actualizaVehiculosEntrada(request).getDatos();
			log.info( CU060_NOMBRE + REGISTRA_ENTRADA_VEHICULO + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU060_NOMBRE + REGISTRA_ENTRADA_VEHICULO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "registraEntradaVehiculo", ALTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos, 	urlModCatalogos + CONSULTAR, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response, REGISTRO_ENTRADA);
		} catch (Exception e) {
			Map<String, Object> envioDatos = vehiculo.actualizaVehiculosEntrada(request).getDatos();
			log.error( CU060_NOMBRE + REGISTRA_ENTRADA_VEHICULO + ERROR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU060_NOMBRE + REGISTRA_ENTRADA_VEHICULO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded(envioDatos), ALTA, authentication);
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
		String queryDecoded = envioDatos.get("condicion").toString();
		try {
			log.info( CU060_NOMBRE + GENERAR_DOCUMENTO + queryDecoded);
			logUtil.crearArchivoLog(Level.INFO.toString(), CU060_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "generarDocumento", GENERA_DOCUMENTO, authentication);
			response = providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response, ERROR_AL_DESCARGAR_DOCUMENTO);
		} catch (Exception e) {
			log.error( CU060_NOMBRE + GENERAR_DOCUMENTO + ERROR_QUERY + queryDecoded);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU060_NOMBRE + GENERAR_DOCUMENTO + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), ERROR_QUERY + queryDecoded, GENERA_DOCUMENTO,
					authentication);
			throw new IOException("52", e.getCause());
		}	
	}
	private String queryDecoded (Map<String, Object> envioDatos ) {
		return new String(DatatypeConverter.parseBase64Binary(envioDatos.get(AppConstantes.QUERY).toString()));
	}
}
