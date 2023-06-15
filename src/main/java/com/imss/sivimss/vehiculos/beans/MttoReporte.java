package com.imss.sivimss.vehiculos.beans;

import com.google.gson.Gson;
import com.imss.sivimss.vehiculos.model.request.ReporteDto;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MttoReporte {
    private static final Logger logger = LogManager.getLogger(MttoReporte.class);
    private static SimpleDateFormat formatoRequest = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat formatoConsulta = new SimpleDateFormat("yyyy-MM-dd");

    public DatosRequest reporteEncargado(DatosRequest request) throws IOException, ParseException {
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
            Date fechaFIRequest=formatoRequest.parse(reporteRequest.getFechaInicio());
            query.append(" AND MV.FEC_REGISTRO >= '").append(formatoConsulta.format(fechaFIRequest)).append("'");
        }
        if (reporteRequest.getFechaFinal() != null) {
            Date fechaFFRequest=formatoRequest.parse(reporteRequest.getFechaFinal());
            query.append(" AND MV.FEC_REGISTRO <= '").append(formatoConsulta.format(fechaFFRequest)).append("'");
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


    public DatosRequest reportePredictivo(DatosRequest request) throws IOException, ParseException {
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
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        StringBuilder query=new StringBuilder();
        query.append("SELECT MV.FEC_REGISTRO,VH.DES_PLACAS,VH.DES_MARCA,VH.DES_SUBMARCA,VH.DES_MODELO,\n" +
                "SMN_ACEITE.DES_NIVEL as DES_NIVEL_ACEITE,SMN_AGUA.DES_NIVEL as DES_NIVEL_AGUA,SMN_NETRA.DES_NIVEL as DES_NIVEL_NEUMATRASE,\n" +
                "SMN_NEDEL.DES_NIVEL as DES_NIVEL_NEUMADELA,SMN_COMB.DES_NIVEL as DES_NIVEL_COMBUSTIBLE,\n" +
                "SMN_BATERIA.DES_NIVEL as DES_NIVEL_BATERIA,SMN_CODIGO.DES_NIVEL as DES_NIVEL_CODIGOFALLO,\n" +
                "SMN_LIMPINT.DES_NIVEL as DES_NIVEL_LIMPIEZAINTERIOR,SMN_LIMPEXT.DES_NIVEL as DES_NIVEL_LIMPIEZAEXTERIOR,VI.ID_MTTOVERIFINICIO,VH.ID_VEHICULO,MV.ID_MTTOVEHICULAR,MV.ID_DELEGACION,\n" +
                "COUNT(MV.ID_VEHICULO) AS TOTAL_VEHICULOS\n" +
                "FROM SVT_VEHICULOS VH\n" +
                "INNER JOIN SVC_USO_VEHICULO UV ON VH.ID_USOVEHICULO = UV.ID_USOVEHICULO\n" +
                "INNER JOIN SVC_VELATORIO VE ON VH.ID_VELATORIO = VE.ID_VELATORIO\n" +
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
                "LEFT JOIN SVC_MTTO_NIVEL SMN_LIMPEXT ON SMN_LIMPEXT.ID_MTTONIVEL=VI.ID_LIMPIEZAEXTERIOR\n" +
                "WHERE VH.IND_ACTIVO = 1");
        if (reporteRequest.getNivelOficina() != null) {
            query.append(" AND VH.ID_OFICINA = ").append(reporteRequest.getNivelOficina());
        }
        if (reporteRequest.getVelatorio()!=null) {
            query.append(" AND MV.ID_VELATORIO = ").append(reporteRequest.getVelatorio());
        }
        if (reporteRequest.getDelegacion() !=null) {
            query.append(" AND VE.ID_DELEGACION = ").append(reporteRequest.getDelegacion());
        }
        if (reporteRequest.getPlaca() != null && reporteRequest.getPlaca().trim().length()>0) {
            query.append(" AND VH.DES_PLACAS = ").append("'" + reporteRequest.getPlaca() + "'");
        }
        if (reporteRequest.getTipoMtto() != null) {
            if(reporteRequest.getTipoMtto().equals(1)){
                query.append(" AND SMN_ACEITE.ID_MTTONIVEL =").append(reporteRequest.getTipoMtto());
            }
            if(reporteRequest.getTipoMtto().equals(2)){
                query.append(" AND SMN_AGUA.ID_MTTONIVEL =").append(reporteRequest.getTipoMtto());
            }
            if(reporteRequest.getTipoMtto().equals(3)){
                query.append(" AND (SMN_NETRA.ID_MTTONIVEL =").append(reporteRequest.getTipoMtto());
                query.append(" OR SMN_NEDEL.ID_MTTONIVEL =").append(reporteRequest.getTipoMtto()+ ")");
            }
            if(reporteRequest.getTipoMtto().equals(4)){
                query.append(" AND SMN_COMB.ID_MTTONIVEL =").append(reporteRequest.getTipoMtto());
            }
            if(reporteRequest.getTipoMtto().equals(5)){
                query.append(" AND SMN_CODIGO.ID_MTTONIVEL =").append(reporteRequest.getTipoMtto());
            }
            if(reporteRequest.getTipoMtto().equals(6)){
                query.append(" AND SMN_BATERIA.ID_MTTONIVE =").append(reporteRequest.getTipoMtto());
            }

        }
        if (reporteRequest.getFechaInicio() != null) {
            Date fechaFIRequest=formatoRequest.parse(reporteRequest.getFechaInicio());
            query.append(" AND MV.FEC_REGISTRO >= '").append(formatoConsulta.format(fechaFIRequest)).append("'");
        }
        if (reporteRequest.getFechaFinal() != null) {
            Date fechaFFRequest=formatoRequest.parse(reporteRequest.getFechaFinal());
            query.append(" AND MV.FEC_REGISTRO <= '").append(formatoConsulta.format(fechaFFRequest)).append("'");
        }
        query.append(" GROUP BY MV.ID_VEHICULO");
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


	public Map<String, Object> reporteProgramarMttoVehicular(ReporteDto reporte) {
		Map<String, Object> envioDatos = new HashMap<>();
		envioDatos.put("condition", " AND MV.ID_VELATORIO= "+reporte.getIdVelatorio()+"  AND VH.DES_PLACAS= '"+reporte.getPlacas()+"'");	
		envioDatos.put("rutaNombreReporte", reporte.getRutaNombreReporte());
		envioDatos.put("tipoReporte", reporte.getTipoReporte());
		if(reporte.getTipoReporte().equals("xls")) {
			envioDatos.put("IS_IGNORE_PAGINATION", true);
		}
		return envioDatos;
	}
	}
