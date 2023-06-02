package com.imss.sivimss.vehiculos.beans;

import java.nio.charset.StandardCharsets;
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
	private String idODS;
	private String horaSalida;
	private String kmInicial;
	private String horaEntrada;
	private String kmFinal;
	private Integer idResponsable;
	private Integer idVehiculo;
	private Integer idUsuarioAlta;
	private String fecSalida;
	private String fecIniRepo;
	private String gasolinaInicial;
	private String fecFinRepo;
	private String fecEntrada;
	private String fecDia;
	private String gasolinaFinal;
	private Integer idDelegacion;
	private Integer idDispVehiculo;
	
	
	private static final String TABLA_SVC_VELATORIO_SV = " SVC_VELATORIO sv";
	private static final String TABLA_SVT_VEHICULO_SV =  " SVT_VEHICULOS sv";  
	private static final String TABLA_SVT_DISPONIBILIDAD_VEHICULO_SDV = " SVT_DISPONIBILIDAD_VEHICULO sdv";  
	private static final String TABLA_SVC_ORDEN_SERVICIO_SOS = " SVC_ORDEN_SERVICIO sos"; 
	private static final String TABLA_SVC_CONTRATANTE_SC = " SVC_CONTRATANTE sc";  
	private static final String TABLA_SVC_PERSONA_SP = " SVC_PERSONA sp";
	private static final String TABLA_SVC_FINADO_SF = " SVC_FINADO sf"; 
	private static final String TABLA_SVC_PERSONA_SP2 =" SVC_PERSONA sp2"; 
	private static final String TABLA_SVC_INFORMACION_SERVICIO_SIS = " SVC_INFORMACION_SERVICIO sis"; 
	private static final String TABLA_SVC_INFORMACION_SERVICIO_VELACION_SISV = " SVC_INFORMACION_SERVICIO_VELACION sisv";  
	private static final String TABAL_SVT_DOMICILIO_SD = "SVT_DOMICILIO sd ";
	private static final String TABAL_SVT_DOMICILIO_SD2 = "SVT_DOMICILIO sd2 ";
	private static final String TABAL_SVT_DOMICILIO_SD3 = "SVT_DOMICILIO sd3 ";
	private static final String TABLA_SVC_CP_SC2 = " SVC_CP sc2";
	private static final String TABLA_SVC_CP_SC3 = " SVC_CP sc3";
	private static final String TABLA_SVC_CP_SC4 = " SVC_CP sc4";
	private static final String TABLA_SVT_PANTEON_SP3 = " SVT_PANTEON sp3";
	private static final String TABLA_SVC_VELATORIO_SV2 = " SVC_VELATORIO sv2";
	private static final String TABLA_SVC_SALA_SS = " SVC_SALA ss";
	private static final String TABALA_SVT_OPERADORES_SO = " SVT_OPERADORES so";
	private static final String TABLA_SVT_USUARIOS_SU = " SVT_USUARIOS su";

	private static final String CAMPO_SOS_ID_ORDEN_SERVICIO = "sos.ID_ORDEN_SERVICIO";
	private static final String CAMPO_SIS_ID_ORDEN_SERVICIO = "sis.ID_ORDEN_SERVICIO";
	private static final String CAMPO_SD_ID_DOMICILIO = "sd.ID_DOMICILIO";
	private static final String CAMPO_SC2_ID_CODIG_POSTAL = "sc2.ID_CODIGO_POSTAL";
	private static final String CAMPO_SISV_ID_DOMICILIO = "sisv.ID_DOMICILIO";
	private static final String CAMPO_SD_ID_CP = "sd.DES_CP";
	
	private static final String NOW = "CURRENT_TIMESTAMP()";
	private static final String JOIN = " JOIN ";
	private static final String LEFT_JOIN = " LEFT JOIN ";
	private static final String FROM = " FROM ";
	private static final String ON = " ON ";
	
	private static final String PARAM_IDVEHICULO = "idVehi";
	private static final String FECHA_ENTRADA_MAX = " AND DATE_FORMAT(sdv.FEC_ENTRADA,'%Y-%m-%d') <= '";
	
	private static final String VALIDACION_CAMPOS_VEHICULOS = " sdv.ID_VEHICULO  = sv.ID_VEHICULO ";

	
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
		this.fecDia = vehiculoRequest.getFecDia();
		this.idDelegacion = vehiculoRequest.getIdDelegacion();
		this.idDispVehiculo = vehiculoRequest.getIdDispVehiculo();
	}

	public DatosRequest consultarDisponibilidadVehiculos(DatosRequest request, String formatoFecha, String formatoHora) {
		String query = "SELECT sdv.id_disponibilidad_vehiculo AS idDisponibilidad, sv.ID_VEHICULO AS idVehiculo, sv.DES_VEHICULO AS descripcion,  IFNULL(sdv.NUM_DISPONIBLE,1) AS disponible"
				+ ", DATE_FORMAT(IFNULL(sdv.FEC_ENTRADA,sdv.FEC_SALIDA),'" +  formatoFecha + "') AS fecha"
				+ ", sv.DES_MARCA AS marca, sv.DES_MODELO AS modelo, sv.DES_PLACAS AS placas"
				+ ", TIME_FORMAT(IF(sdv.TIM_HORA_ENTRADA = '00:00:00' OR ISNULL(sdv.TIM_HORA_ENTRADA),sdv.TIM_HORA_SALIDA,sdv.TIM_HORA_ENTRADA), '" + formatoHora + "') AS hora "
				
				+ FROM + TABLA_SVT_VEHICULO_SV 
				+ LEFT_JOIN  + TABLA_SVT_DISPONIBILIDAD_VEHICULO_SDV + ON + VALIDACION_CAMPOS_VEHICULOS + " AND sdv.IND_ACTIVO = 1 "
				+ JOIN + TABLA_SVC_VELATORIO_SV2 + " ON sv2.ID_VELATORIO = sv.ID_VELATORIO";
		if(this.idDelegacion != null) {
			query = query + " WHERE sv2.ID_DELEGACION = " + this.idDelegacion;
			 if(this.idVelatorio != null) {
					query = query + " AND sv.ID_VELATORIO = " + this.idVelatorio;
				}
		}else if(this.idVelatorio != null) {
			query = query + " WHERE sv.ID_VELATORIO = " + this.idVelatorio;
		}
		request.getDatos().put(AppConstantes.QUERY, queryEncoded(query));

		return request;
	}
	public DatosRequest consultarDisponibilidadVehiculosCalendario(DatosRequest request) {
		String where="";
		String query = "SELECT sdv.id_disponibilidad_vehiculo AS idDisponibilidad, sv.ID_VEHICULO AS idVehiculo, sv.DES_VEHICULO AS descripcion,  IFNULL(sdv.NUM_DISPONIBLE,1) AS disponible"
				+ ", DATE_FORMAT(IFNULL(sdv.FEC_ENTRADA,sdv.FEC_SALIDA),'%Y-%m-%d') AS fecha"
				+ ", sv.DES_MARCA AS marca, sv.DES_MODELO AS modelo, sv.DES_PLACAS AS placas "
				+ FROM + TABLA_SVT_VEHICULO_SV 
				+ LEFT_JOIN  + TABLA_SVT_DISPONIBILIDAD_VEHICULO_SDV + ON + VALIDACION_CAMPOS_VEHICULOS
				+ JOIN + TABLA_SVC_VELATORIO_SV2 + " ON sv2.ID_VELATORIO = sv.ID_VELATORIO";
		if(this.idDelegacion != null) {
			query = query + " AND sv2.ID_DELEGACION = " + this.idDelegacion;
		}
		if(this.fecIniRepo != null || this.fecFinRepo != null){
			where = " WHERE (DATE_FORMAT(sdv.FEC_ENTRADA,'%Y-%m-%d') >= '" + this.fecIniRepo +"'"
					+ FECHA_ENTRADA_MAX + this.fecFinRepo + "')"
					+ " OR (DATE_FORMAT(sdv.FEC_SALIDA,'%Y-%m-%d') >= '" + this.fecIniRepo + "'"
					+ " AND DATE_FORMAT(sdv.FEC_SALIDA,'%Y-%m-%d') <= '" + this.fecFinRepo +"')";
		}else if (where.equals("")) {
			where = where + " WHERE sv.ID_VELATORIO = " + this.idVelatorio;
		}
		if(this.idVelatorio != null) {
			where = where + " AND sv.ID_VELATORIO = " + this.idVelatorio;
		}
		query = query + where + " GROUP BY fecha";

		request.getDatos().put(AppConstantes.QUERY, queryEncoded(query));

		return request;
	}
	

	public DatosRequest consultaDetalleVehiculo(DatosRequest request) {
		final String  query =" SELECT sdv.id_disponibilidad_vehiculo AS idDisponibilidad, sv.ID_VEHICULO AS idVehiculo, sv.DES_MARCA AS marca, sv.DES_MODELO AS modelo"
						+ ", sv.DES_PLACAS AS placas, sv.NUM_TARJETA_CIRCULACION AS   tarjetaCirculacion"
						+ ", sos.CVE_FOLIO AS folioODS, CONCAT(sp.NOM_PERSONA, ' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO ) AS nombreContratante"
						+ ", CONCAT(sp2.NOM_PERSONA, ' ' , sp2.NOM_PRIMER_APELLIDO, ' ', sp2.NOM_SEGUNDO_APELLIDO ) as nombreFinado"
						+ ", sc2.DES_MNPIO AS nombreDestino, " + CAMPO_SOS_ID_ORDEN_SERVICIO + " AS idODS, IFNULL(sdv.NUM_DISPONIBLE,1) AS disponible"
						+ ", IF(sv.ID_USOVEHICULO=1,0,1) AS ods"
						+ FROM + TABLA_SVT_VEHICULO_SV
						+ LEFT_JOIN + TABLA_SVT_DISPONIBILIDAD_VEHICULO_SDV + ON + VALIDACION_CAMPOS_VEHICULOS
				+ JOIN + TABLA_SVC_ORDEN_SERVICIO_SOS + " ON " + CAMPO_SOS_ID_ORDEN_SERVICIO + " = sdv.ID_ODS"
				+ JOIN + TABLA_SVC_CONTRATANTE_SC + " ON sc.ID_CONTRATANTE = sos.ID_CONTRATANTE"
				+ JOIN + TABLA_SVC_PERSONA_SP + " ON sp.ID_PERSONA = sc.ID_PERSONA"
				+ LEFT_JOIN  + TABLA_SVC_FINADO_SF + " ON sf.ID_ORDEN_SERVICIO = " + CAMPO_SOS_ID_ORDEN_SERVICIO 
				+ LEFT_JOIN  + TABLA_SVC_PERSONA_SP2 + " ON sp2.ID_PERSONA = sf.ID_PERSONA"
				+ JOIN + TABLA_SVC_INFORMACION_SERVICIO_SIS + " ON " +CAMPO_SIS_ID_ORDEN_SERVICIO + " = " + CAMPO_SOS_ID_ORDEN_SERVICIO 
				+ JOIN + TABLA_SVC_INFORMACION_SERVICIO_VELACION_SISV + " ON sisv.ID_INFORMACION_SERVICIO  = sis.ID_INFORMACION_SERVICIO"
				+ JOIN + TABAL_SVT_DOMICILIO_SD + " ON " + CAMPO_SD_ID_DOMICILIO + " = " + CAMPO_SISV_ID_DOMICILIO
				+ JOIN + TABLA_SVC_CP_SC2 + " ON " + CAMPO_SC2_ID_CODIG_POSTAL + " = " + CAMPO_SD_ID_CP
				+ " WHERE sv.ID_VEHICULO = " + this.idVehiculo
				+ " ORDER BY sdv.FEC_ALTA DESC "
				+ " Limit 1";

		request.getDatos().put(AppConstantes.QUERY, queryEncoded(query));

		return request;
	}
	
	
	public DatosRequest consultaDetalleVehiculoxDia(DatosRequest request, String formatoFecha, String formatoHora) {
		
		String query = "SELECT sdv.id_disponibilidad_vehiculo AS idDisponibilidad, sv.ID_VEHICULO AS idVehiculo, sv.DES_MARCA AS marca, sv.DES_MODELO AS modelo, sv.DES_PLACAS AS placas "
				+ ", sv.NUM_TARJETA_CIRCULACION AS   tarjetaCirculacion, sos.CVE_FOLIO AS folioODS "
				+ ", CONCAT(sp.NOM_PERSONA, ' ', sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO ) AS nombreContratante "
				+ ", CONCAT(sp2.NOM_PERSONA, ' ' , sp2.NOM_PRIMER_APELLIDO, ' ', sp2.NOM_SEGUNDO_APELLIDO ) as nombreFinado "
				+ ", sc2.DES_MNPIO AS nombreDestino, TIME_FORMAT(sdv.TIM_HORA_ENTRADA, '" + formatoHora + "') AS horaEntrada"
				+ ", TIME_FORMAT(sdv.TIM_HORA_SALIDA, '" + formatoHora + "') AS horaSalida, sdv.DES_NIVEL_GASOLINA_INICIAL AS nivelGasIni "
				+ ", sdv.DES_NIVEL_GASOLINA_FINAL AS nivelGasFin, sdv.NUM_KM_INICIAL AS kmInicial, sdv.NUM_KM_FINAL AS kmFin"
				+ ", DATE_FORMAT(sdv.FEC_ENTRADA,'" + formatoFecha + "') AS fechaEntrada, DATE_FORMAT(sdv.FEC_SALIDA,'" + formatoFecha + "') AS fechaSalida"
				+ ", IFNULL(sdv.NUM_DISPONIBLE,1) AS disponible"
				+ ", IF(sv.ID_USOVEHICULO=1,0,1) AS ods"
				+ FROM + TABLA_SVT_VEHICULO_SV
				+ LEFT_JOIN + TABLA_SVT_DISPONIBILIDAD_VEHICULO_SDV + " ON sdv.ID_VEHICULO  = sv.ID_VEHICULO "
				+ JOIN + TABLA_SVC_ORDEN_SERVICIO_SOS + " ON " + CAMPO_SOS_ID_ORDEN_SERVICIO + " = sdv.ID_ODS  "
				+ JOIN + TABLA_SVC_CONTRATANTE_SC + " ON sc.ID_CONTRATANTE = sos.ID_CONTRATANTE "
				+ JOIN + TABLA_SVC_PERSONA_SP + " ON sp.ID_PERSONA = sc.ID_PERSONA  "
				+ LEFT_JOIN + TABLA_SVC_FINADO_SF + " ON sf.ID_ORDEN_SERVICIO = " + CAMPO_SOS_ID_ORDEN_SERVICIO + " "
				+ LEFT_JOIN + TABLA_SVC_PERSONA_SP2 + " ON sp2.ID_PERSONA = sf.ID_PERSONA "
				+ JOIN + TABLA_SVC_INFORMACION_SERVICIO_SIS + " ON " + CAMPO_SIS_ID_ORDEN_SERVICIO + " = " + CAMPO_SOS_ID_ORDEN_SERVICIO + " "
				+ JOIN + TABLA_SVC_INFORMACION_SERVICIO_VELACION_SISV + " ON sisv.ID_INFORMACION_SERVICIO  = sis.ID_INFORMACION_SERVICIO "
				+ JOIN + TABAL_SVT_DOMICILIO_SD + " ON " + CAMPO_SD_ID_DOMICILIO + " = " + CAMPO_SISV_ID_DOMICILIO + " "
				+ JOIN + TABLA_SVC_CP_SC2 + " ON " + CAMPO_SC2_ID_CODIG_POSTAL + " = " + CAMPO_SD_ID_CP + " "
				+ " WHERE (sdv.FEC_ENTRADA = '" + this.fecDia + "' OR sdv.FEC_SALIDA = '" + this.fecDia + "') AND sv.ID_VEHICULO = " + this.idVehiculo 
				+ " ORDER BY sdv.ID_DISPONIBILIDAD_VEHICULO DESC";


		request.getDatos().put(AppConstantes.QUERY, queryEncoded(query));

		return request;
	}
	public DatosRequest consultaDetalleODS(DatosRequest request) {
		SelectQueryUtil queryUno = new SelectQueryUtil();
		SelectQueryUtil queryDos = new SelectQueryUtil();
		queryUno.select("concat(sp.NOM_PERSONA, ' ' , sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO ) as nombreContratante"
				,"concat(sp2.NOM_PERSONA, ' ', sp2.NOM_PRIMER_APELLIDO,' ', sp2.NOM_SEGUNDO_APELLIDO ) as nombreFinado"
				,"sc2.DES_MNPIO AS nombreOrigen"," sc4.DES_MNPIO AS nombreDestino")
				.from(TABLA_SVC_ORDEN_SERVICIO_SOS)
				.join(TABLA_SVC_CONTRATANTE_SC, "sos.ID_CONTRATANTE = sc.ID_CONTRATANTE")
				.join(TABLA_SVC_PERSONA_SP,"sc.ID_PERSONA = sp.ID_PERSONA")
				.leftJoin(TABLA_SVC_FINADO_SF, CAMPO_SOS_ID_ORDEN_SERVICIO + " = sf.ID_ORDEN_SERVICIO")
				.leftJoin(TABLA_SVC_PERSONA_SP2,"sp2.ID_PERSONA = sf.ID_PERSONA")
				.join(TABLA_SVC_INFORMACION_SERVICIO_SIS,CAMPO_SIS_ID_ORDEN_SERVICIO + " = " + CAMPO_SOS_ID_ORDEN_SERVICIO )
				.join(TABLA_SVC_INFORMACION_SERVICIO_VELACION_SISV,"sisv.ID_INFORMACION_SERVICIO = sis.ID_INFORMACION_SERVICIO")
				.join(TABAL_SVT_DOMICILIO_SD,CAMPO_SD_ID_DOMICILIO + " = " + CAMPO_SISV_ID_DOMICILIO + "")
				.join(TABLA_SVC_CP_SC2,CAMPO_SC2_ID_CODIG_POSTAL + " = " + CAMPO_SD_ID_CP + "")
				.join(TABAL_SVT_DOMICILIO_SD2,"sd2.ID_DOMICILIO = " + CAMPO_SISV_ID_DOMICILIO + "")
				.join(TABLA_SVC_CP_SC3,"sc3.ID_CODIGO_POSTAL = sd2.DES_CP")
				.leftJoin(TABLA_SVC_SALA_SS,"ss.ID_SALA  = sis.ID_SALA")
				.join(TABLA_SVC_VELATORIO_SV,"sv.ID_VELATORIO = ss.ID_VELATORIO")
				.join(TABAL_SVT_DOMICILIO_SD3,"sd3.ID_DOMICILIO = sv.ID_DOMICILIO") 
				.join(TABLA_SVC_CP_SC4,"sc4.ID_CODIGO_POSTAL = sd3.DES_CP")
				.where("sos.ID_ESTATUS_ORDEN_SERVICIO in (1,2)")
				.and("sos.CVE_FOLIO = :idODS" )
				.setParameter("idODS", this.idODS);
		queryDos.select("concat(sp.NOM_PERSONA, ' ' , sp.NOM_PRIMER_APELLIDO, ' ', sp.NOM_SEGUNDO_APELLIDO ) as nombreContratante"
				, "concat(sp2.NOM_PERSONA, ' '  , sp2.NOM_PRIMER_APELLIDO, ' ', sp2.NOM_SEGUNDO_APELLIDO ) as nombreFinado"
				, "sc2.DES_MNPIO AS nombreOrigen","sc4.DES_MNPIO AS nombreDestino")
				.from(TABLA_SVC_ORDEN_SERVICIO_SOS)
				.join(TABLA_SVC_CONTRATANTE_SC,"sos.ID_CONTRATANTE = sc.ID_CONTRATANTE")
				.join(TABLA_SVC_PERSONA_SP,"sc.ID_PERSONA = sp.ID_PERSONA")
				.leftJoin(TABLA_SVC_FINADO_SF,CAMPO_SOS_ID_ORDEN_SERVICIO + " = sf.ID_ORDEN_SERVICIO")
				.leftJoin(TABLA_SVC_PERSONA_SP2,"sp2.ID_PERSONA = sf.ID_PERSONA")
				.join(TABLA_SVC_INFORMACION_SERVICIO_SIS,CAMPO_SIS_ID_ORDEN_SERVICIO + " = " + CAMPO_SOS_ID_ORDEN_SERVICIO )
				.join(TABLA_SVC_INFORMACION_SERVICIO_VELACION_SISV,"sisv.ID_INFORMACION_SERVICIO = sis.ID_INFORMACION_SERVICIO")
				.join(TABAL_SVT_DOMICILIO_SD,CAMPO_SD_ID_DOMICILIO + " = " + CAMPO_SISV_ID_DOMICILIO + "")
				.join(TABLA_SVC_CP_SC2, CAMPO_SC2_ID_CODIG_POSTAL + " = " + CAMPO_SD_ID_CP + "")
				.join(TABAL_SVT_DOMICILIO_SD2,"sd2.ID_DOMICILIO = " + CAMPO_SISV_ID_DOMICILIO + "")
				.join(TABLA_SVC_CP_SC3,"sc3.ID_CODIGO_POSTAL = sd2.DES_CP")
				.leftJoin(TABLA_SVT_PANTEON_SP3,"sp3.ID_PANTEON = sis.ID_PANTEON")
				.join(TABAL_SVT_DOMICILIO_SD3,"sd3.ID_DOMICILIO = sp3.ID_DOMICILIO") 
				.join(TABLA_SVC_CP_SC4,"sc4.ID_CODIGO_POSTAL = sd3.DES_CP")
				.where("sos.ID_ESTATUS_ORDEN_SERVICIO in (1,2)")
				.and("sos.CVE_FOLIO = :idODS" )
				.setParameter("idODS", this.idODS);
		final String query = queryUno.union(queryDos);

		request.getDatos().put(AppConstantes.QUERY, queryEncoded(query));

		return request;
	}

	public DatosRequest consultaOperador(DatosRequest request) {
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil
				.select("so.ID_OPERADOR AS idResponsable"," CONCAT(su.NOM_USUARIO ,' ', su.NOM_APELLIDO_PATERNO ,' ', su.NOM_APELLIDO_MATERNO ) AS nombreResponsable")
				.from(TABALA_SVT_OPERADORES_SO)
				.innerJoin(TABLA_SVT_USUARIOS_SU, "su.ID_USUARIO  = so.ID_USUARIO")
				.where("so.ID_VEHICULO = :idVehi")
				.setParameter(PARAM_IDVEHICULO, this.idVehiculo);
		final String query = queryUtil.build();
		request.getDatos().put(AppConstantes.QUERY, queryEncoded(query));

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

		final QueryHelper q = new QueryHelper("INSERT INTO SVT_DISPONIBILIDAD_VEHICULO");
		q.agregarParametroValues("ID_VEHICULO", "'" + this.idVehiculo + "'");
		q.agregarParametroValues("ID_ODS", "'" + this.idODS + "'");
		q.agregarParametroValues("FEC_SALIDA", "'" + this.fecSalida + "'");
		q.agregarParametroValues("TIM_HORA_SALIDA", "'" + this.horaSalida + "'");
		q.agregarParametroValues("DES_NIVEL_GASOLINA_INICIAL", "'" + this.gasolinaInicial + "'");
		q.agregarParametroValues("NUM_KM_INICIAL", "'" + this.kmInicial + "'");
		q.agregarParametroValues("NUM_DISPONIBLE", "0");
		q.agregarParametroValues("ID_RESPONSABLE", "'" + this.idResponsable + "'");
		q.agregarParametroValues("ID_BITACORA", "1");
		q.agregarParametroValues("ID_USUARIO_ALTA", "'" + this.idUsuarioAlta + "'");
		q.agregarParametroValues("FEC_ALTA", NOW);
		q.agregarParametroValues("IND_ACTIVO", "1");
		String query = q.obtenerQueryInsertar();
		
		  String encoded =DatatypeConverter.printBase64Binary(query.getBytes());
		  request.getDatos().put(AppConstantes.QUERY, encoded);
		  
		  return request; 
	  }

	public DatosRequest actualizaVehiculosParaSalir(DatosRequest request) {

		String query = "UPDATE SVT_DISPONIBILIDAD_VEHICULO sdv SET "
		 + " sdv.IND_ACTIVO = 0 "
		 + " WHERE sdv.ID_VEHICULO = " + this.idVehiculo
	     + " AND DATE_FORMAT(sdv.FEC_ENTRADA,'%Y-%m-%d') = '" + this.fecSalida + "'";
		
		  String encoded =DatatypeConverter.printBase64Binary(query.getBytes());
		  request.getDatos().put(AppConstantes.QUERY, encoded);
		  
		  return request; 
	  }

	public DatosRequest actualizaVehiculosEntrada(DatosRequest request) {

		String query = "UPDATE SVT_DISPONIBILIDAD_VEHICULO sdv SET "
		 + " sdv.IND_ACTIVO = 1 "
		 + ", FEC_ENTRADA = '" + this.fecEntrada + "'"
		 + ", TIM_HORA_ENTRADA = '" + this.horaEntrada + "'"
		 + ", NUM_DISPONIBLE = 1 "
		 + " WHERE sdv.ID_DISPONIBILIDAD_VEHICULO = " + this.idDispVehiculo;
		
		  String encoded =DatatypeConverter.printBase64Binary(query.getBytes());
		  request.getDatos().put(AppConstantes.QUERY, encoded);
		  
		  return request; 
	  }
	public DatosRequest registrarVehiculoEntrada(DatosRequest request) {

		final QueryHelper q = new QueryHelper("INSERT INTO SVT_DISPONIBILIDAD_VEHICULO");
		q.agregarParametroValues("ID_VEHICULO", "'" + this.idVehiculo + "'");
		q.agregarParametroValues("ID_ODS", "'" + this.idODS + "'");
		q.agregarParametroValues("FEC_ENTRADA", "'" + this.fecEntrada + "'");
		q.agregarParametroValues("TIM_HORA_ENTRADA", "'" + this.horaEntrada + "'");
		q.agregarParametroValues("DES_NIVEL_GASOLINA_FINAL", "'" + this.gasolinaFinal + "'");
		q.agregarParametroValues("NUM_KM_FINAL", "'" + this.kmFinal + "'");
		q.agregarParametroValues("NUM_DISPONIBLE", "1");
		q.agregarParametroValues("ID_BITACORA", "1");
		q.agregarParametroValues("ID_USUARIO_ALTA", "'" + this.idUsuarioAlta + "'");
		q.agregarParametroValues("FEC_ALTA", NOW);
		q.agregarParametroValues("IND_ACTIVO", "1");
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
				+ FECHA_ENTRADA_MAX + this.fecFinRepo + "')"
				+ " OR (DATE_FORMAT(sdv.FEC_SALIDA ,'%Y-%m-%d') >= '" + this.fecIniRepo + "' "
				+ " AND DATE_FORMAT(sdv.FEC_SALIDA ,'%Y-%m-%d') <= '" + this.fecFinRepo + "') ";
		}
		envioDatos.put("condicion", condicion);
		envioDatos.put("tipoReporte", reporteDto.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfReportes);

		return envioDatos;
	}
	private String queryEncoded (String str) {
		return DatatypeConverter.printBase64Binary(str.getBytes(StandardCharsets.UTF_8));
	}
}
