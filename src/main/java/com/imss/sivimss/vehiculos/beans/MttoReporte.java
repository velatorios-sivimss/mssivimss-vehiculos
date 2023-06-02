package com.imss.sivimss.vehiculos.beans;

import com.google.gson.Gson;
import com.imss.sivimss.vehiculos.model.request.ReporteEncargadoRequest;
import com.imss.sivimss.vehiculos.model.request.ReportePredictivoRequest;
import com.imss.sivimss.vehiculos.util.AppConstantes;
import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.SelectQueryUtil;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MttoReporte {
    private static final Logger logger = LogManager.getLogger(MttoReporte.class);

    public DatosRequest reporteEncargado(DatosRequest request) throws IOException {
        Gson json = new Gson();
        ReporteEncargadoRequest reporteRequest=new ReporteEncargadoRequest();
        String requestBoby=String.valueOf(request.getDatos().get(AppConstantes.DATOS));
        if(requestBoby!=null && requestBoby!="null" && requestBoby.trim().length()>0) {
            reporteRequest = json.fromJson(requestBoby, ReporteEncargadoRequest.class);
        }
        Integer pagina = Integer.valueOf(Integer.parseInt(request.getDatos().get("pagina").toString()));
        Integer tamanio = Integer.valueOf(Integer.parseInt(request.getDatos().get("tamanio").toString()));
        reporteRequest.setTamanio(tamanio.toString());
        reporteRequest.setPagina(pagina.toString());
        StringBuilder query=new StringBuilder();
        query.append("SELECT MV.FEC_REGISTRO,VH.DES_PLACAS,SMN_ACEITE.DES_NIVEL as DES_NIVEL_ACEITE,SMN_AGUA.DES_NIVEL as DES_NIVEL_AGUA,\n" +
                "SMN_NETRA.DES_NIVEL as DES_NIVEL_NEUMATRASE,SMN_NEDEL.DES_NIVEL as DES_NIVEL_NEUMADELA,SMN_COMB.DES_NIVEL as DES_NIVEL_COMBUSTIBLE,\n" +
                "SMN_BATERIA.DES_NIVEL as DES_NIVEL_BATERIA,SMN_CODIGO.DES_NIVEL as DES_NIVEL_CODIGOFALLO,SMN_LIMPINT.DES_NIVEL as DES_NIVEL_LIMPIEZAINTERIOR,\n" +
                "SMN_LIMPEXT.DES_NIVEL as DES_NIVEL_LIMPIEZAEXTERIOR,VI.ID_MTTOVERIFINICIO,VH.ID_VEHICULO,MV.ID_MTTOVEHICULAR,MV.ID_DELEGACION,COUNT(MV.ID_VEHICULO) AS TOTAL_VEHICULOS\n" +
                "FROM SVT_VEHICULOS VH\n" +
                "INNER JOIN SVC_USO_VEHICULO UV ON VH.ID_USOVEHICULO = UV.ID_USOVEHICULO\n" +
                "LEFT JOIN SVT_MTTO_VEHICULAR MV ON MV.ID_VEHICULO = VH.ID_VEHICULO\n" +
                "LEFT JOIN SVT_MTTO_VERIF_INICIO VI ON VI.ID_MTTOVEHICULAR = MV.ID_MTTOVEHICULAR\n" +
                "LEFT JOIN SVC_MTTO_NIVEL SMN_ACEITE ON SMN_ACEITE.ID_MTTONIVEL=VI.ID_NIVELACEITE\n" +
                "LEFT JOIN SVC_MTTO_NIVEL SMN_AGUA ON SMN_AGUA.ID_MTTONIVEL=VI.ID_NIVELAGUA\n" +
                "LEFT JOIN SVC_MTTO_NIVEL SMN_NETRA ON SMN_NETRA.ID_MTTONIVEL=VI.ID_CALNEUTRASEROS\n" +
                "LEFT JOIN SVC_MTTO_NIVEL SMN_NEDEL ON SMN_NEDEL.ID_MTTONIVEL=VI.ID_CALNEUDELANTEROS\n" +
                "LEFT JOIN SVC_MTTO_NIVEL SMN_COMB ON SMN_COMB.ID_MTTONIVEL=VI.ID_NIVELCOMBUSTIBLE\n" +
                "LEFT JOIN SVC_MTTO_NIVEL SMN_BATERIA ON SMN_BATERIA.ID_MTTONIVEL=VI.ID_NIVELBATERIA\n" +
                "LEFT JOIN SVC_MTTO_NIVEL SMN_CODIGO ON SMN_CODIGO.ID_MTTONIVEL=VI.ID_CODIGOFALLO\n" +
                "LEFT JOIN SVC_MTTO_NIVEL SMN_LIMPINT ON SMN_LIMPINT.ID_MTTONIVEL=VI.ID_LIMPIEZAINTERIOR\n" +
                "LEFT JOIN SVC_MTTO_NIVEL SMN_LIMPEXT  ON SMN_LIMPEXT.ID_MTTONIVEL=VI.ID_LIMPIEZAEXTERIOR\n" +
                "WHERE VH.IND_ACTIVO = 1");

        if (reporteRequest.getPlaca() != null && reporteRequest.getPlaca().trim().length()>0) {
            query.append(" AND VH.DES_PLACAS =").append("'").append(reporteRequest.getPlaca()).append("'");
        }
        if (reporteRequest.getFechaInicio() != null) {
            query.append(" AND MV.FEC_REGISTRO >= CAST('").append(reporteRequest.getFechaInicio()).append("' AS DATE)");
        }
        if (reporteRequest.getFechaFinal() != null) {
            query.append(" AND MV.FEC_REGISTRO >= CAST('").append(reporteRequest.getFechaFinal()).append("' AS DATE)");
        }
        query. append(" GROUP BY MV.ID_VEHICULO");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        parametro.put("pagina",reporteRequest.getPagina());
        parametro.put("tamanio",reporteRequest.getTamanio());
        request.getDatos().remove("datos");
        dr.setDatos(parametro);
        logger.info(query.toString());
        return dr;
    }


    public DatosRequest reportePredictivo(DatosRequest request) throws IOException {
        Gson json = new Gson();
        ReportePredictivoRequest reporteRequest=new ReportePredictivoRequest();
        String requestBoby=String.valueOf(request.getDatos().get(AppConstantes.DATOS));
        if(requestBoby!=null && requestBoby!="null" && requestBoby.trim().length()>0) {
            reporteRequest = json.fromJson(requestBoby, ReportePredictivoRequest.class);
        }
        Integer pagina = Integer.valueOf(Integer.parseInt(request.getDatos().get("pagina").toString()));
        Integer tamanio = Integer.valueOf(Integer.parseInt(request.getDatos().get("tamanio").toString()));
        reporteRequest.setTamanio(tamanio.toString());
        reporteRequest.setPagina(pagina.toString());
        String query = "";
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("MV.FEC_REGISTRO",
                        "VH.DES_PLACAS",
                        "VH.DES_MARCA",
                        "VH.DES_SUBMARCA",
                        "VH.DES_MODELO",
                        "SMN_ACEITE.DES_NIVEL as DES_NIVEL_ACEITE",
                        "SMN_AGUA.DES_NIVEL as DES_NIVEL_AGUA",
                        "SMN_NETRA.DES_NIVEL as DES_NIVEL_NEUMATRASE",
                        "SMN_NEDEL.DES_NIVEL as DES_NIVEL_NEUMADELA",
                        "SMN_COMB.DES_NIVEL as DES_NIVEL_COMBUSTIBLE",
                        "SMN_BATERIA.DES_NIVEL as DES_NIVEL_BATERIA",
                        "SMN_CODIGO.DES_NIVEL as DES_NIVEL_CODIGOFALLO",
                        "SMN_LIMPINT.DES_NIVEL as DES_NIVEL_LIMPIEZAINTERIOR",
                        "SMN_LIMPEXT.DES_NIVEL as DES_NIVEL_LIMPIEZAEXTERIOR",
                        "VI.ID_MTTOVERIFINICIO",
                        "VH.ID_VEHICULO",
                        "MV.ID_MTTOVEHICULAR",
                        "MV.ID_DELEGACION",
                        "COUNT(MV.ID_VEHICULO) AS TOTAL_VEHICULOS"
                )
                .from("SVT_VEHICULOS VH")
                .join("SVC_USO_VEHICULO UV", "VH.ID_USOVEHICULO = UV.ID_USOVEHICULO")
                .leftJoin("SVT_MTTO_VEHICULAR MV", "MV.ID_VEHICULO = VH.ID_VEHICULO")
                .leftJoin("SVT_MTTO_VERIF_INICIO VI", "VI.ID_MTTOVEHICULAR = MV.ID_MTTOVEHICULAR")
                .leftJoin("SVC_MTTO_NIVEL SMN_ACEITE","SMN_ACEITE.ID_MTTONIVEL=VI.ID_NIVELACEITE")
                .leftJoin("SVC_MTTO_NIVEL SMN_AGUA","SMN_AGUA.ID_MTTONIVEL=VI.ID_NIVELAGUA")
                .leftJoin("SVC_MTTO_NIVEL SMN_NETRA","SMN_NETRA.ID_MTTONIVEL=VI.ID_CALNEUTRASEROS")
                .leftJoin("SVC_MTTO_NIVEL SMN_NEDEL","SMN_NEDEL.ID_MTTONIVEL=VI.ID_CALNEUDELANTEROS")
                .leftJoin("SVC_MTTO_NIVEL SMN_COMB","SMN_COMB.ID_MTTONIVEL=VI.ID_NIVELCOMBUSTIBLE")
                .leftJoin("SVC_MTTO_NIVEL SMN_BATERIA","SMN_BATERIA.ID_MTTONIVEL=VI.ID_NIVELBATERIA")
                .leftJoin("SVC_MTTO_NIVEL SMN_CODIGO","SMN_CODIGO.ID_MTTONIVEL=VI.ID_CODIGOFALLO")
                .leftJoin("SVC_MTTO_NIVEL SMN_LIMPINT","SMN_LIMPINT.ID_MTTONIVEL=VI.ID_LIMPIEZAINTERIOR")
                .leftJoin("SVC_MTTO_NIVEL SMN_LIMPEXT","SMN_LIMPEXT.ID_MTTONIVEL=VI.ID_LIMPIEZAEXTERIOR")
                .where("VH.IND_ACTIVO = :idEstatus")
                .setParameter("idEstatus", 1);
        if (reporteRequest.getNivelOficina() != null) {
            queryUtil.where("VH.ID_OFICINA = :oficina")
                    .setParameter("oficina", reporteRequest.getNivelOficina());
        }
        if (reporteRequest.getVelatorio()!=null) {
            queryUtil.where("MV.ID_VELATORIO = :velatorio")
                    .setParameter("velatorio", reporteRequest.getVelatorio());
        }
        if (reporteRequest.getDelegacion() !=null) {
            queryUtil.where("MV.ID_DELEGACION = :delegacion")
                    .setParameter("delegacion", reporteRequest.getDelegacion());
        }
        if (reporteRequest.getPlaca() != null && reporteRequest.getPlaca().trim().length()>0) {
            queryUtil.where("VH.DES_PLACAS = :placa")
                    .setParameter("placa", reporteRequest.getPlaca());
        }
        if (reporteRequest.getTipoMtto() != null) {
            if(reporteRequest.getTipoMtto().equals(1)){
                queryUtil.where("SMN_ACEITE.ID_MTTONIVEL = :nivelAceite")
                        .setParameter("nivelAceite", reporteRequest.getTipoMtto());
            }
            if(reporteRequest.getTipoMtto().equals(2)){
                queryUtil.where("SMN_AGUA.ID_MTTONIVEL = :nivelAgua")
                        .setParameter("nivelAgua", reporteRequest.getTipoMtto());
            }
            if(reporteRequest.getTipoMtto().equals(3)){
                queryUtil.where("SMN_NETRA.ID_MTTONIVEL = :nivelCalNeoTras")
                        .setParameter("nivelCalNeoTras", reporteRequest.getTipoMtto())
                .or("SMN_NEDEL.ID_MTTONIVEL = :nivelCalNeoDel")
                        .setParameter("nivelCalNeoTras",reporteRequest.getTipoMtto());
            }
            if(reporteRequest.getTipoMtto().equals(4)){
                queryUtil.where("SMN_COMB.ID_MTTONIVEL = :nivelComb")
                        .setParameter("nivelComb", reporteRequest.getTipoMtto());
            }
            if(reporteRequest.getTipoMtto().equals(5)){
                queryUtil.where("SMN_CODIGO.ID_MTTONIVEL = :nivelCodFallo")
                        .setParameter("nivelCodFallo", reporteRequest.getTipoMtto());
            }
            if(reporteRequest.getTipoMtto().equals(6)){
                queryUtil.where("SMN_BATERIA.ID_MTTONIVE = :nivelBateria")
                        .setParameter("nivelBateria", reporteRequest.getTipoMtto());
            }

        }
        if (reporteRequest.getFechaInicio() != null && reporteRequest.getFechaFinal() != null) {
            queryUtil.where("MV.FEC_REGISTRO >= CAST(:fechaInicio AS DATE)")
                    .setParameter("fechaInicio", "'" + reporteRequest.getFechaInicio() + "'");
            queryUtil.where("MV.FEC_REGISTRO <= CAST(:fechaFinal AS DATE)")
                    .setParameter("fechaFinal", "'" + reporteRequest.getFechaFinal() + "'");
        }
        queryUtil.groupBy("MV.ID_VEHICULO");
        query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        parametro.put("pagina",reporteRequest.getPagina());
        parametro.put("tamanio",reporteRequest.getTamanio());
        request.getDatos().remove("datos");
        dr.setDatos(parametro);
        logger.info(query);
        return dr;
    }
}
