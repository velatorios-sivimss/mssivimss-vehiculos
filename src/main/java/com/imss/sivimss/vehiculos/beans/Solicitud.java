package com.imss.sivimss.vehiculos.beans;

import com.imss.sivimss.vehiculos.model.request.MttoVehicularRequest;
import com.imss.sivimss.vehiculos.model.request.UsuarioDto;
import com.imss.sivimss.vehiculos.util.AppConstantes;
import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.QueryHelper;
import com.imss.sivimss.vehiculos.util.SelectQueryUtil;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class Solicitud {
    private static final Logger logger = LogManager.getLogger(Solicitud.class);

    public DatosRequest insertar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("INSERT INTO SVT_MTTO_SOLICITUD");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_MTTOVEHICULAR", request.getSolicitud().getIdMttoVehicular().toString());
        q.agregarParametroValues("ID_MTTO_TIPO", request.getSolicitud().getIdMttoTipo().toString());
        q.agregarParametroValues("ID_MTTOMODALIDAD", request.getSolicitud().getIdMttoModalidad().toString());
        if(request.getSolicitud().getFecRegistro()==null){
            q.agregarParametroValues("FEC_REGISTRO", "CURRENT_TIMESTAMP()");
        } else {
            q.agregarParametroValues("FEC_REGISTRO", "'" + request.getSolicitud().getFecRegistro() + "'");
        }
        if(request.getSolicitud().getDesMttoCorrectivo()!=null) {
            q.agregarParametroValues("DES_MTTO_CORRECTIVO", "'" + request.getSolicitud().getDesMttoCorrectivo() + "'");
        }
        if(request.getSolicitud().getIdMttoModalidadDet()!=null) {
            q.agregarParametroValues("ID_MTTOMODALIDAD_DET", request.getSolicitud().getIdMttoModalidadDet().toString());
        }
        if(request.getSolicitud().getDesNotas()!=null) {
            q.agregarParametroValues("DES_NOTAS", "'" + request.getSolicitud().getDesNotas() + "'");
        }
        if(request.getSolicitud().getKilometraje()!=null) {
            q.agregarParametroValues("NUM_KILOMETRAJE", "'" + request.getSolicitud().getKilometraje() + "'");
        }
        q.agregarParametroValues("IND_ACTIVO", "1");
        if(request.getSolicitud().getFecRegistro2()!=null) {
            q.agregarParametroValues("FEC_REGISTRO_FIN", "'" + request.getSolicitud().getFecRegistro2() + "'");
        }
        String query = q.obtenerQueryInsertar();
        logger.info(query);
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest modificar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_SOLICITUD");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_MTTO_TIPO", request.getSolicitud().getIdMttoTipo().toString());
        q.agregarParametroValues("ID_MTTOMODALIDAD", request.getSolicitud().getIdMttoModalidad().toString());
        q.agregarParametroValues("FEC_REGISTRO", "'" + request.getSolicitud().getFecRegistro() + "'");
        if(request.getSolicitud().getDesMttoCorrectivo()!=null) {
            q.agregarParametroValues("DES_MTTO_CORRECTIVO", "'" + request.getSolicitud().getDesMttoCorrectivo() + "'");
        }
        if(request.getSolicitud().getIdMttoModalidadDet()!=null) {
            q.agregarParametroValues("ID_MTTOMODALIDAD_DET", "'" + request.getSolicitud().getIdMttoModalidadDet().toString()  + "'");
        }
        if(request.getSolicitud().getDesNotas()!=null) {
            q.agregarParametroValues("DES_NOTAS", "'" + request.getSolicitud().getDesNotas() + "'");
        }
        if(request.getSolicitud().getKilometraje()!=null) {
            q.agregarParametroValues("NUM_KILOMETRAJE", "'" + request.getSolicitud().getKilometraje() + "'");
        }
        q.agregarParametroValues("IND_ACTIVO", request.getIdEstatus().toString());
        q.agregarParametroValues("FEC_SOLICTUD","CURRENT_TIMESTAMP()");
        if(request.getSolicitud().getFecRegistro2()!=null) {
            q.agregarParametroValues("FEC_REGISTRO_FIN", "'" + request.getSolicitud().getFecRegistro2() + "'");
        } else {
            q.agregarParametroValues("FEC_REGISTRO_FIN", "NULL");
        }
        q.addWhere("ID_MTTO_SOLICITUD =" + request.getSolicitud().getIdMttoSolicitud());
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest cambiarEstatus(Integer idMttoSolicitud, Integer status,UsuarioDto user) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_SOLICITUD");
        q.agregarParametroValues("IND_ACTIVO", String.valueOf(status));
        q.addWhere("ID_MTTO_SOLICITUD =" + idMttoSolicitud);
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }
	public DatosRequest detalleSolicitud(DatosRequest request) {
		String palabra = request.getDatos().get("palabra").toString();
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("SOLI.ID_MTTO_SOLICITUD",
                        "SOLI.ID_MTTOVEHICULAR",
                        "SOLI.ID_MTTO_TIPO",
                        "SOLI.ID_MTTOMODALIDAD",
                        "DATE_FORMAT(SOLI.FEC_REGISTRO,'%d-%m-%Y') AS FEC_REGISTRO",
                        "SOLI.DES_MTTO_CORRECTIVO",
                        "SOLI.ID_MTTOMODALIDAD_DET",
                        "SOLI.IND_ACTIVO",
                        "SOLI.NUM_KILOMETRAJE",
                        "SOLI.DES_NOTAS",
                        "SOLI.KILOMETRAJE",
                        "DATE_FORMAT(SOLI.FEC_SOLICTUD,'%d-%m-%Y') AS FEC_SOLICTUD",
                        "DATE_FORMAT(SOLI.FEC_REGISTRO_FIN,'%d-%m-%Y') AS FEC_REGISTRO_FIN",
                        "MTTO_VEH.ID_MTTOESTADO",
                        "MTTO_VEH.ID_VEHICULO",
                        "MTTO_VEH.ID_DELEGACION",
                        "MTTO_VEH.ID_VELATORIO",
                        "DATE_FORMAT(MTTO_VEH.FEC_ALTA,'%d-%m-%Y') AS FEC_ALTA",
                        "DATE_FORMAT(MTTO_VEH.FEC_ACTUALIZACION,'%d-%m-%Y') AS FEC_ACTUALIZACION",
                        "DATE_FORMAT(MTTO_VEH.FEC_BAJA,'%d-%m-%Y') AS FEC_BAJA",
                        "MTTO_VEH.IND_ACTIVO",
                        "MTTO_VEH.ID_USUARIO_ALTA",
                        "SME.DES_MTTOESTADO",
                        "SV.DES_MARCA",
                        "SV.DES_SUBMARCA",
                        "SV.DES_MODELO",
                        "SV.DES_PLACAS",
                        "SV.DES_NUMSERIE",
                        "SV.DES_NUMMOTOR",
                        "SUV.DES_USO",
                        "SD.DES_DELEGACION",
                        "SVEL.DES_VELATORIO",
                        "SMT.DES_MTTO_TIPO",
                        "SMM.DES_MODALIDAD")
                .from("SVT_MTTO_SOLICITUD SOLI")
                .leftJoin("SVT_MTTO_VEHICULAR MTTO_VEH", "MTTO_VEH.ID_MTTOVEHICULAR = SOLI.ID_MTTOVEHICULAR")
                .leftJoin("SVC_MTTO_ESTADO SME","MTTO_VEH.ID_MTTOESTADO=SME.ID_MTTOESTADO")
                .leftJoin("SVT_VEHICULOS SV","MTTO_VEH.ID_VEHICULO=SV.ID_VEHICULO")
                .leftJoin("SVC_USO_VEHICULO SUV","SV.ID_USOVEHICULO=SUV.ID_USOVEHICULO")
                .leftJoin("SVC_DELEGACION SD","MTTO_VEH.ID_DELEGACION=SD.ID_DELEGACION")
                .leftJoin("SVC_VELATORIO SVEL","MTTO_VEH.ID_VELATORIO=SVEL.ID_VELATORIO")
                .leftJoin("SVC_MTTO_TIPO SMT","SMT.ID_MTTO_TIPO=SOLI.ID_MTTO_TIPO")
                .leftJoin("SVC_MTTO_MODALIDAD SMM","SMM.ID_MTTOMODALIDAD =SOLI.ID_MTTOMODALIDAD");
        if(palabra!=null && palabra.trim().length()>0) {
            queryUtil.where("SOLI.ID_MTTO_SOLICITUD = :idSolicitud")
                    .setParameter("idSolicitud", Integer.parseInt(palabra));
        } else {
            queryUtil.orderBy("SOLI.ID_MTTO_SOLICITUD");
        }
        String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		logger.info(query);
		return request;
	}

    public DatosRequest existe(MttoVehicularRequest request) {
        String query=null;
        StringBuilder sql=new StringBuilder("SELECT SOL.ID_MTTO_SOLICITUD, SOL.ID_MTTOVEHICULAR FROM SVT_MTTO_SOLICITUD SOL WHERE SOL.FEC_SOLICTUD=CURRENT_DATE()");
        sql.append(" AND SOL.IND_ACTIVO=1").append(" AND SOL.ID_MTTOVEHICULAR="+request.getSolicitud().getIdMttoVehicular()).append(";");
        query = sql.toString();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        logger.info(query);
        dr.setDatos(parametro);
        return dr;
    }

}
