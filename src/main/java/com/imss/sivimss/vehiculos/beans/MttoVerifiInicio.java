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
public class MttoVerifiInicio {
    private static final Logger logger = LogManager.getLogger(MttoVerifiInicio.class);

    public DatosRequest insertar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("INSERT INTO SVT_MTTO_VERIF_INICIO");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_MTTOVEHICULAR", request.getVerificacionInicio().getIdMttoVehicular().toString());
        q.agregarParametroValues("ID_NIVELACEITE", request.getVerificacionInicio().getIdNivelAceite().toString());
        q.agregarParametroValues("ID_NIVELAGUA", request.getVerificacionInicio().getIdNivelAgua().toString());
        q.agregarParametroValues("ID_CALNEUTRASEROS", request.getVerificacionInicio().getIdCalNeuTraseros().toString());
        q.agregarParametroValues("ID_CALNEUDELANTEROS", request.getVerificacionInicio().getIdCalNeuDelanteros().toString());
        q.agregarParametroValues("ID_NIVELCOMBUSTIBLE", request.getVerificacionInicio().getIdNivelCombustible().toString());
        q.agregarParametroValues("ID_NIVELBATERIA", request.getVerificacionInicio().getIdNivelBateria().toString());
        q.agregarParametroValues("ID_CODIGOFALLO", request.getVerificacionInicio().getIdCodigoFallo().toString());
        q.agregarParametroValues("ID_LIMPIEZAINTERIOR", request.getVerificacionInicio().getIdLimpiezaInterior().toString());
        q.agregarParametroValues("ID_LIMPIEZAEXTERIOR", request.getVerificacionInicio().getIdLimpiezaExterior().toString());
        q.agregarParametroValues("CVE_ESTATUS", "1");
        q.agregarParametroValues("FEC_REGISTRO", "CURRENT_TIMESTAMP()");
        String query = q.obtenerQueryInsertar();
        logger.info(query);
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest modificar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_VERIF_INICIO");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_NIVELACEITE", request.getVerificacionInicio().getIdNivelAceite().toString());
        q.agregarParametroValues("ID_NIVELAGUA", request.getVerificacionInicio().getIdNivelAgua().toString());
        q.agregarParametroValues("ID_CALNEUTRASEROS", request.getVerificacionInicio().getIdCalNeuTraseros().toString());
        q.agregarParametroValues("ID_CALNEUDELANTEROS", request.getVerificacionInicio().getIdCalNeuDelanteros().toString());
        q.agregarParametroValues("ID_NIVELCOMBUSTIBLE", request.getVerificacionInicio().getIdNivelCombustible().toString());
        q.agregarParametroValues("ID_NIVELBATERIA", request.getVerificacionInicio().getIdNivelBateria().toString());
        q.agregarParametroValues("ID_CODIGOFALLO", request.getVerificacionInicio().getIdCodigoFallo().toString());
        q.agregarParametroValues("ID_LIMPIEZAINTERIOR", request.getVerificacionInicio().getIdLimpiezaInterior().toString());
        q.agregarParametroValues("ID_LIMPIEZAEXTERIOR", request.getVerificacionInicio().getIdLimpiezaExterior().toString());
        q.agregarParametroValues("CVE_ESTATUS", request.getIdEstatus().toString());
        q.agregarParametroValues("FEC_REGISTRO", "CURRENT_TIMESTAMP()");
        q.addWhere("ID_MTTOVERIFINICIO =" + request.getVerificacionInicio().getIdMttoVerifInicio());
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest cambiarEstatus(Integer idMmttoVerifInicio, Integer status,UsuarioDto user) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_VERIF_INICIO");
        q.agregarParametroValues("CVE_ESTATUS", String.valueOf(status));
        q.addWhere("ID_MTTOVERIFINICIO =" + idMmttoVerifInicio);
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest detalleVerificacion(DatosRequest request) {
		String palabra = request.getDatos().get("palabra").toString();
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("VERI_IN.ID_MTTOVERIFINICIO",
                        "VERI_IN.ID_MTTOVEHICULAR",
                        "VERI_IN.ID_NIVELACEITE",
                        "VERI_IN.ID_NIVELAGUA",
                        "VERI_IN.ID_CALNEUTRASEROS",
                        "VERI_IN.ID_CALNEUDELANTEROS",
                        "VERI_IN.ID_NIVELCOMBUSTIBLE",
                        "VERI_IN.ID_NIVELBATERIA",
                        "VERI_IN.ID_CODIGOFALLO",
                        "VERI_IN.ID_LIMPIEZAINTERIOR",
                        "VERI_IN.ID_LIMPIEZAEXTERIOR",
                        "VERI_IN.CVE_ESTATUS",
                        "DATE_FORMAT(VERI_IN.FEC_REGISTRO,'%d-%m-%Y') AS FEC_REGISTRO",
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
                        "SV.DES_MODELO",
                        "SV.DES_PLACAS",
                        "SV.DES_SUBMARCA",
                        "SV.DES_NUMSERIE",
                        "SV.DES_NUMMOTOR",
                        "SUV.DES_USO",
                        "SD.DES_DELEGACION",
                        "SVEL.DES_VELATORIO",
                        "SMN_ACEITE.DES_NIVEL as DES_NIVEL_ACEITE",
                        "SMN_AGUA.DES_NIVEL as DES_NIVEL_AGUA",
                        "SMN_NETRA.DES_NIVEL as DES_NIVEL_NEUMATRASE",
                        "SMN_NEDEL.DES_NIVEL as DES_NIVEL_NEUMADELA",
                        "SMN_COMB.DES_NIVEL as DES_NIVEL_COMBUSTIBLE",
                        "SMN_BATERIA.DES_NIVEL as DES_NIVEL_BATERIA",
                        "SMN_CODIGO.DES_NIVEL as DES_NIVEL_CODIGOFALLO",
                        "SMN_LIMPINT.DES_NIVEL as DES_NIVEL_LIMPIEZAINTERIOR",
                        "SMN_LIMPEXT.DES_NIVEL as DES_NIVEL_LIMPIEZAEXTERIOR")
                .from("SVT_MTTO_VERIF_INICIO VERI_IN")
                .leftJoin("SVT_MTTO_VEHICULAR MTTO_VEH", "VERI_IN.ID_MTTOVEHICULAR = MTTO_VEH.ID_MTTOVEHICULAR")
                .leftJoin("SVC_MTTO_ESTADO SME","MTTO_VEH.ID_MTTOESTADO=SME.ID_MTTOESTADO")
                .leftJoin("SVT_VEHICULOS SV","MTTO_VEH.ID_VEHICULO=SV.ID_VEHICULO")
                .leftJoin("SVC_USO_VEHICULO SUV","SV.ID_USOVEHICULO=SUV.ID_USOVEHICULO")
                .leftJoin("SVC_DELEGACION SD","MTTO_VEH.ID_DELEGACION=SD.ID_DELEGACION")
                .leftJoin("SVC_VELATORIO SVEL","MTTO_VEH.ID_VELATORIO=SVEL.ID_VELATORIO")
                .leftJoin("SVC_MTTO_NIVEL SMN_ACEITE","SMN_ACEITE.ID_MTTONIVEL=VERI_IN.ID_NIVELACEITE")
                .leftJoin("SVC_MTTO_NIVEL SMN_AGUA","SMN_AGUA.ID_MTTONIVEL=VERI_IN.ID_NIVELAGUA")
                .leftJoin("SVC_MTTO_NIVEL SMN_NETRA","SMN_NETRA.ID_MTTONIVEL=VERI_IN.ID_CALNEUTRASEROS")
                .leftJoin("SVC_MTTO_NIVEL SMN_NEDEL","SMN_NEDEL.ID_MTTONIVEL=VERI_IN.ID_CALNEUDELANTEROS")
                .leftJoin("SVC_MTTO_NIVEL SMN_COMB","SMN_COMB.ID_MTTONIVEL=VERI_IN.ID_NIVELCOMBUSTIBLE")
                .leftJoin("SVC_MTTO_NIVEL SMN_BATERIA","SMN_BATERIA.ID_MTTONIVEL=VERI_IN.ID_NIVELBATERIA")
                .leftJoin("SVC_MTTO_NIVEL SMN_CODIGO","SMN_CODIGO.ID_MTTONIVEL=VERI_IN.ID_CODIGOFALLO")
                .leftJoin("SVC_MTTO_NIVEL SMN_LIMPINT","SMN_LIMPINT.ID_MTTONIVEL=VERI_IN.ID_LIMPIEZAINTERIOR")
                .leftJoin("SVC_MTTO_NIVEL SMN_LIMPEXT","SMN_LIMPEXT.ID_MTTONIVEL=VERI_IN.ID_LIMPIEZAEXTERIOR");
        if(palabra!=null && palabra.trim().length()>0) {
            queryUtil.where("VERI_IN.ID_MTTOVERIFINICIO = :idVerifIni")
                    .setParameter("idVerifIni", Integer.parseInt(palabra));
        } else {
            queryUtil.orderBy("VERI_IN.ID_MTTOVERIFINICIO");
        }
        String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		logger.info(query);
		return request;
	}

    public DatosRequest existe(MttoVehicularRequest request) {
        String query=null;
        StringBuilder sql=new StringBuilder("SELECT VI.ID_MTTOVERIFINICIO, VI.ID_MTTOVEHICULAR FROM SVT_MTTO_VERIF_INICIO VI WHERE VI.FEC_REGISTRO=CURRENT_DATE()");
        sql.append(" AND VI.CVE_ESTATUS=1").append(" AND VI.ID_MTTOVEHICULAR="+request.getVerificacionInicio().getIdMttoVehicular()).append(";");
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
