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
        q.agregarParametroValues("IND_ESTATUS", "1");
        String query = q.obtenerQueryInsertar();
        logger.info(query);
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
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
        q.agregarParametroValues("IND_ESTATUS", request.getIdEstatus().toString());
        q.addWhere("ID_MTTOVERIFINICIO =" + request.getVerificacionInicio().getIdMttoVerifInicio());
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest cambiarEstatus(Integer idMmttoVerifInicio, Integer status,UsuarioDto user) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_VERIF_INICIO");
        q.agregarParametroValues("IND_ESTATUS", String.valueOf(status));
        q.addWhere("ID_MTTOVERIFINICIO =" + idMmttoVerifInicio);
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
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
                        "VERI_IN.IND_ESTATUS",
                        "MTTO_VEH.ID_MTTOESTADO",
                        "MTTO_VEH.ID_VEHICULO",
                        "MTTO_VEH.ID_DELEGACION",
                        "MTTO_VEH.ID_VELATORIO",
                        "MTTO_VEH.FEC_ALTA",
                        "MTTO_VEH.FEC_ACTUALIZACION",
                        "MTTO_VEH.FEC_BAJA",
                        "MTTO_VEH.IND_ACTIVO",
                        "MTTO_VEH.ID_USUARIO_ALTA",
                        "SME.DES_MTTOESTADO",
                        "SV.DES_MARCA",
                        "SV.DES_MODELO",
                        "SV.DES_PLACAS",
                        "SV.DES_NUMSERIE",
                        "SV.DES_NUMMOTOR",
                        "SUV.DES_USO",
                        "SD.DES_DELEGACION",
                        "SVEL.NOM_VELATORIO",
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
                .join("SVT_MTTO_VEHICULAR MTTO_VEH", "VERI_IN.ID_MTTOVEHICULAR = MTTO_VEH.ID_MTTOVEHICULAR")
                .join("SVC_MTTO_ESTADO SME","MTTO_VEH.ID_MTTOESTADO=SME.ID_MTTOESTADO")
                .join("SVT_VEHICULOS SV","MTTO_VEH.ID_VEHICULO=SV.ID_VEHICULO")
                .join("SVC_USO_VEHICULO SUV","SV.ID_USOVEHICULO=SUV.ID_USOVEHICULO")
                .join("SVC_DELEGACION SD","MTTO_VEH.ID_DELEGACION=SD.ID_DELEGACION")
                .join("SVC_VELATORIO SVEL","MTTO_VEH.ID_VELATORIO=SVEL.ID_VELATORIO")
                .join("SVC_MTTO_NIVEL SMN_ACEITE","SMN_ACEITE.ID_MTTONIVEL=VERI_IN.ID_NIVELACEITE")
                .join("SVC_MTTO_NIVEL SMN_AGUA","SMN_AGUA.ID_MTTONIVEL=VERI_IN.ID_NIVELAGUA")
                .join("SVC_MTTO_NIVEL SMN_NETRA","SMN_NETRA.ID_MTTONIVEL=VERI_IN.ID_CALNEUTRASEROS")
                .join("SVC_MTTO_NIVEL SMN_NEDEL","SMN_NEDEL.ID_MTTONIVEL=VERI_IN.ID_CALNEUDELANTEROS")
                .join("SVC_MTTO_NIVEL SMN_COMB","SMN_COMB.ID_MTTONIVEL=VERI_IN.ID_NIVELCOMBUSTIBLE")
                .join("SVC_MTTO_NIVEL SMN_BATERIA","SMN_BATERIA.ID_MTTONIVEL=VERI_IN.ID_NIVELBATERIA")
                .join("SVC_MTTO_NIVEL SMN_CODIGO","SMN_CODIGO.ID_MTTONIVEL=VERI_IN.ID_CODIGOFALLO")
                .join("SVC_MTTO_NIVEL SMN_LIMPINT","SMN_LIMPINT.ID_MTTONIVEL=VERI_IN.ID_LIMPIEZAINTERIOR")
                .join("SVC_MTTO_NIVEL SMN_LIMPEXT","SMN_LIMPEXT.ID_MTTONIVEL=VERI_IN.ID_LIMPIEZAEXTERIOR");
        if(palabra!=null && palabra.trim().length()>0) {
            queryUtil.where("VERI_IN.ID_MTTOVERIFINICIO = :idVerifIni")
                    .setParameter("idVerifIni", Integer.parseInt(palabra));
        } else {
            queryUtil.orderBy("VERI_IN.ID_MTTOVERIFINICIO");
        }
        String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		logger.info(query);
		return request;
	}
}
