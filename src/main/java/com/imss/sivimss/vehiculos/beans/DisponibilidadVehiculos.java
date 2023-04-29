package com.imss.sivimss.vehiculos.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.vehiculos.model.request.VehiculoRequest;
import com.imss.sivimss.vehiculos.model.request.ReporteDto;
import com.imss.sivimss.vehiculos.util.AppConstantes;
import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.SelectQueryUtil;
import com.imss.sivimss.vehiculos.util.QueryHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class DisponibilidadVehiculos {

	private Integer idVelatorio;
	private Integer idVehiculo;
	private String idODS;
	private String fecSalida;
	private String horaSalida;
	private String gasolinaInicial;
	private String kmInicial;
	private String fecEntrada;
	private String horaEntrada;
	private String gasolinaFinal;
	private String kmFinal;
	private Integer idResponsable;
	private Integer idUsuarioAlta;
	private String fecIniRepo;
	private String fecFinRepo;
	
	
	private static final String TABLA_SVC_VELATORIO_SV = "SVC_VELATORIO sv";
	private static final String TABLA_SVT_VEHICULO_SV =  "SVT_VEHICULOS sv";  
	private static final String TABLA_SVT_DISPONIBILIDAD_VEHICULO_SDV = "SVT_DISPONIBILIDAD_VEHICULO sdv";  
	private static final String TABLA_SVC_ORDEN_SERVICIO_SOS = "SVC_ORDEN_SERVICIO sos"; 
	private static final String TABLA_SVC_CONTRATANTE_SC = "SVC_CONTRATANTE sc";  
	private static final String TABLA_SVC_PERSONA_SP = "SVC_PERSONA sp";
	private static final String TABLA_SVC_FINADO_SF = "SVC_FINADO sf"; 
	private static final String TABLA_SVC_PERSONA_SP2 ="SVC_PERSONA sp2"; 
	private static final String TABLA_SVC_INFORMACION_SERVICIO_SIS = "SVC_INFORMACION_SERVICIO sis"; 
	private static final String TABLA_SVC_INFORMACION_SERVICIO_VELACION_SISV = "SVC_INFORMACION_SERVICIO_VELACION sisv";  
	private static final String TABLA_SVC_CP_SC2 = "SVC_CP sc2";
	
	private static final String NOW = "CURRENT_TIMESTAMP()";
	
	private static final String PARAM_IDVELATORIO = "idVel";
	private static final String PARAM_IDVEHICULO = "idVehi";

	
	public DisponibilidadVehiculos(VehiculoRequest vehiculoRequest) {
		this.idVelatorio = vehiculoRequest.getIdVelatorio();
		this.idVehiculo = vehiculoRequest.getIdVehiculo();
		this.idODS = vehiculoRequest.getIdODS();
		this.fecSalida = vehiculoRequest.getFecSalida();
		this.horaSalida = vehiculoRequest.getHoraSalida();
		this.gasolinaInicial = vehiculoRequest.getGasolinaInicial();
		this.kmInicial = vehiculoRequest.getKmInicial();
		this.idResponsable = vehiculoRequest.getIdResponsable();
		this.fecEntrada = vehiculoRequest.getFecEntrada();
		this.horaEntrada = vehiculoRequest.getHoraEntrada();
		this.gasolinaFinal = vehiculoRequest.getGasolinaFinal();
		this.kmFinal = vehiculoRequest.getKmFinal();
		this.fecIniRepo = vehiculoRequest.getFecIniRepo();
		this.fecFinRepo = vehiculoRequest.getFecFinRepo();
	}

	public DatosRequest consultarDisponibilidadVehiculo(DatosRequest request) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil
				.select("sv.ID_VEHICULO AS idVehiculo","sv.DESCRIPCION AS descripcion","IFNULL(sdv.DISPONIBLE,1) AS disponible","IFNULL(sdv.FEC_ENTRADA,sdv.FEC_SALIDA) AS fecha")
				.from(TABLA_SVT_VEHICULO_SV)
				.leftJoin(TABLA_SVT_DISPONIBILIDAD_VEHICULO_SDV, "sdv.ID_VEHICULO  = sv.ID_VEHICULO")
				.where("sv.ID_VELATORIO = :idVel")
				.setParameter(PARAM_IDVELATORIO, this.idVelatorio);
		if(this.fecIniRepo != null || this.fecFinRepo != null){
			queryUtil = queryUtil.and("DATE_FORMAT(sdv.FEC_ENTRADA,'%Y-%m-%d') >= :fecIni").setParameter("fecIni", this.fecIniRepo)
					.and("DATE_FORMAT(sdv.FEC_ENTRADA,'%Y-%m-%d') <= :fecFin").setParameter("fecFin", this.fecFinRepo)
					.or(" DATE_FORMAT(sdv.FEC_SALIDA,'%Y-%m-%d') >= :fecIni").setParameter("fecIni", this.fecIniRepo)
					.and("DATE_FORMAT(sdv.FEC_SALIDA,'%Y-%m-%d') <= :fecFin").setParameter("fecFin", this.fecFinRepo);
		}
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest consultaDetalleVehiculo(DatosRequest request) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil
				.select("sv.ID_VEHICULO AS idVehiculo","sv.DES_MARCA AS marca","sv.DES_MODELO AS modelo"
						,"sv.DES_PLACAS AS placas","sv.TARJETA_CIRCULACION AS   tarjetaCirculacion"
						,"sos.CVE_FOLIO AS folioODS","CONCAT(sp.NOM_PERSONA, ' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO ) AS nombreContratante"
						,"CONCAT(sp2.NOM_PERSONA, ' ' , sp2.NOM_PRIMER_APELLIDO, ' ', sp2.NOM_SEGUNDO_APELLIDO ) as nombreFinado"
						,"sc2.DES_MNPIO AS nombreDestino")
				.from(TABLA_SVT_VEHICULO_SV)
				.leftJoin(TABLA_SVT_DISPONIBILIDAD_VEHICULO_SDV, "sdv.ID_VEHICULO  = sv.ID_VEHICULO")
				.join(TABLA_SVC_ORDEN_SERVICIO_SOS, "sos.ID_ORDEN_SERVICIO = sdv.ID_ODS")
				.join(TABLA_SVC_CONTRATANTE_SC, "sc.ID_CONTRATANTE = sos.ID_CONTRATANTE")
				.join(TABLA_SVC_PERSONA_SP, "sp.ID_PERSONA = sc.ID_PERSONA")
				.leftJoin(TABLA_SVC_FINADO_SF, "sf.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO")
				.leftJoin(TABLA_SVC_PERSONA_SP2," sp2.ID_PERSONA = sf.ID_PERSONA")
				.join(TABLA_SVC_INFORMACION_SERVICIO_SIS, "sis.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO")
				.join(TABLA_SVC_INFORMACION_SERVICIO_VELACION_SISV, "sisv.ID_INFORMACION_SERVICIO  = sis.ID_INFORMACION_SERVICIO")
				.join(TABLA_SVC_CP_SC2,"sc2.ID_CODIGO_POSTAL = sisv.ID_CP")
				.where("sv.ID_VEHICULO = :" + PARAM_IDVEHICULO)
				.setParameter(PARAM_IDVEHICULO, this.idVehiculo);
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}
	public DatosRequest consultaDetalleODS(DatosRequest request) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil
				.select("concat(sp.NOM_PERSONA, ' ' , sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO ) as nombreContratante"
						,"concat(sp2.NOM_PERSONA, ' '  , sp2.NOM_PRIMER_APELLIDO, ' ', sp2.NOM_SEGUNDO_APELLIDO ) as nombreFinado"
						,"sc2.DES_MNPIO AS nombreDestino")
				.from("svc_orden_servicio sos")
				.innerJoin("svc_contratante sc", "sos.ID_CONTRATANTE = sc.ID_CONTRATANTE")
				.innerJoin("svc_persona sp", "sc.ID_PERSONA = sp.ID_PERSONA")
				.leftJoin("svc_finado sf", "sos.ID_ORDEN_SERVICIO = sf.ID_ORDEN_SERVICIO")
				.leftJoin("svc_persona sp2", "sp2.ID_PERSONA = sf.ID_PERSONA")
				.innerJoin("svc_informacion_servicio sis", "sis.ID_ORDEN_SERVICIO = sos.ID_ORDEN_SERVICIO")
				.innerJoin("svc_informacion_servicio_velacion sisv", "sisv.ID_INFORMACION_SERVICIO = sis.ID_INFORMACION_SERVICIO")
				.innerJoin("svc_cp sc2", "sc2.ID_CODIGO_POSTAL = sisv.ID_CP ")
				.where("sos.CVE_FOLIO = :idODS" )
				.setParameter("idODS", this.idODS);
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest consultaOperador(DatosRequest request) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil
				.select("CONCAT(su.NOM_USUARIO,' ', su.NOM_APELLIDO_PATERNO,' ', su.NOM_APELLIDO_MATERNO ) AS nombreResponsable")
				.from("svt_operadores so")
				.innerJoin("svt_usuarios su", "su.ID_USUARIO = so.ID_USUARIO")
				.where("so.ID_VEHICULO = :idVehi")
				.setParameter(PARAM_IDVEHICULO, this.idVehiculo);
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}
	public DatosRequest obtenerVelatorio(DatosRequest request) {

		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("sv.ID_VELATORIO", "sv.NOM_VELATORIO").from(TABLA_SVC_VELATORIO_SV);
		final String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest registrarVehiculoSalida(DatosRequest request) {

		final QueryHelper q = new QueryHelper("INSERT INTO svt_disponibilidad_vehiculo");
		q.agregarParametroValues("ID_VEHICULO", "'" + this.idVehiculo + "'");
		q.agregarParametroValues("ID_ODS", "'" + this.idODS + "'");
		q.agregarParametroValues("FEC_SALIDA", "'" + this.fecSalida + "'");
		q.agregarParametroValues("HORA_SALIDA", "'" + this.horaSalida + "'");
		q.agregarParametroValues("NIVEL_GASOLINA_INICIAL", "'" + this.gasolinaInicial + "'");
		q.agregarParametroValues("KM_INICIAL", "'" + this.kmInicial + "'");
		q.agregarParametroValues("DISPONIBLE", "0");
		q.agregarParametroValues("ID_RESPONSABLE", "'" + this.idResponsable + "'");
		q.agregarParametroValues("ID_BITACORA", "1");
		q.agregarParametroValues("ID_USUARIO_ALTA", "'" + this.idUsuarioAlta + "'");
		q.agregarParametroValues("FEC_ALTA", NOW);
		String query = q.obtenerQueryInsertar();
		
		  String encoded =DatatypeConverter.printBase64Binary(query.getBytes());
		  request.getDatos().put(AppConstantes.QUERY, encoded);
		  
		  return request; 
	  }

	public DatosRequest registrarVehiculoEntrada(DatosRequest request) {

		final QueryHelper q = new QueryHelper("INSERT INTO svt_disponibilidad_vehiculo");
		q.agregarParametroValues("ID_VEHICULO", "'" + this.idVehiculo + "'");
		q.agregarParametroValues("ID_ODS", "'" + this.idODS + "'");
		q.agregarParametroValues("FEC_ENTRADA", "'" + this.fecEntrada + "'");
		q.agregarParametroValues("HORA_ENTRADA", "'" + this.horaEntrada + "'");
		q.agregarParametroValues("NIVEL_GASOLINA_FINAL", "'" + this.gasolinaFinal + "'");
		q.agregarParametroValues("KM_FINAL", "'" + this.kmFinal + "'");
		q.agregarParametroValues("DISPONIBLE", "1");
		q.agregarParametroValues("ID_BITACORA", "1");
		q.agregarParametroValues("ID_USUARIO_ALTA", "'" + this.idUsuarioAlta + "'");
		q.agregarParametroValues("FEC_ALTA", NOW);
		String query = q.obtenerQueryInsertar();
		
		  String encoded =DatatypeConverter.printBase64Binary(query.getBytes());
		  request.getDatos().put(AppConstantes.QUERY, encoded);
		  
		  return request; 
	  }
	public Map<String, Object> generarReportePDF(ReporteDto reporteDto, String nombrePdfReportes) {
		Map<String, Object> envioDatos = new HashMap<>();
		String condicion = " ";
		if (this.fecIniRepo != null && this.fecFinRepo != null) {
		condicion = " AND (DATE_FORMAT(sdv.FEC_ENTRADA,'%Y-%m-%d') >= '" + this.fecIniRepo + "' "
				+ " AND DATE_FORMAT(sdv.FEC_ENTRADA,'%Y-%m-%d') <= '" + this.fecFinRepo + "')"
				+ " OR (DATE_FORMAT(sdv.FEC_SALIDA ,'%Y-%m-%d') >= '" + this.fecIniRepo + "' "
				+ " AND DATE_FORMAT(sdv.FEC_SALIDA ,'%Y-%m-%d') <= '" + this.fecFinRepo + "') ";
		}
		envioDatos.put("condicion", condicion);
		envioDatos.put("tipoReporte", reporteDto.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfReportes);

		return envioDatos;
	}

}
