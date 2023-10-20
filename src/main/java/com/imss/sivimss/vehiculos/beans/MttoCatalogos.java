package com.imss.sivimss.vehiculos.beans;

import com.google.gson.Gson;
import com.imss.sivimss.vehiculos.model.request.*;
import com.imss.sivimss.vehiculos.util.AppConstantes;
import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.SelectQueryUtil;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MttoCatalogos {
    private static final Logger logger = LogManager.getLogger(MttoCatalogos.class);

    private static final Integer INDESTATUS=1;

    public DatosRequest getCatMttoUsoVehiculo() throws IOException {
        logger.info("Genera consulta para obtener lista de uso de vehiculo");
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("ID_USOVEHICULO",
                        "DES_USO",
                        "IND_ACTIVO")
                .from("SVC_USO_VEHICULO UV")
                .where("UV.IND_ACTIVO = :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest getCatMttoModalidad() throws IOException {
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("ID_MTTOMODALIDAD",
                        "DES_MODALIDAD",
                        "IND_ACTIVO")
                .from("SVC_MTTO_MODALIDAD MM")
                .where("MM.IND_ACTIVO = :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest getCatNivelOficina() throws IOException {
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("ID_OFICINA",
                        "DES_NIVELOFICINA")
                .from("SVC_NIVEL_OFICINA NO");
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest getCatMttoTipo() throws IOException {
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("ID_MTTO_TIPO",
                        "DES_MTTO_TIPO",
                        "IND_ACTIVO")
                .from("SVC_MTTO_TIPO MT")
                .where("MT.IND_ACTIVO = :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest getCatMttoEstado() throws IOException {
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("ID_MTTOESTADO",
                        "DES_MTTOESTADO",
                        "IND_ACTIVO")
                .from("SVC_MTTO_ESTADO ME")
                .where("ME.IND_ACTIVO = :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest getCatProveedores() throws IOException {
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("ID_PROVEEDOR",
                        "REF_PROVEEDOR",
                        "REF_BANCO",
                        "CVE_BANCARIA",
                        "ID_TIPO_PROVEEDOR",
                        "CVE_RFC",
                        "CVE_CURP AS DES_CURP",
                        "REF_TIPO_CONTRATO",
                        "FEC_VIGENCIA",
                        "REF_DIRECCION",
                        "NUM_EXTERIOR",
                        "NUM_INTERIOR",
                        "ID_CODIGO_POSTAL",
                        "REF_DIRECCION_REFE",
                        "NUM_EXTERIOR_REFE",
                        "NUM_INTERIOR_REFE",
                        "ID_CODIGO_POSTAL_REFE",
                        "NUM_TELEFONO",
                        "REF_CORREOE",
                        "REF_REGIMEN",
                        "REF_REPRESENTANTE_LEGAL",
                        "IND_ACTIVO",
                        "ID_USUARIO_ALTA")
                .from("SVT_PROVEEDOR PV")
                .where("PV.IND_ACTIVO = :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest getCatMttoNivel() throws IOException {
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("ID_MTTONIVEL",
                        "DES_NIVEL",
                        "IND_ACTIVO")
                .from("SVC_MTTO_NIVEL MN")
                .where("MN.IND_ACTIVO = :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest getCatMttoReporteTipo() throws IOException {
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("TR.ID_MTTO_REPORTE_TIPO",
                        "TR.DES_MTTO_REPORTE_TIPO",
                        "TR.IND_ACTIVO")
                .from("SVC_MTTO_REPORTE_TIPO TR")
                .where("TR.IND_ACTIVO = :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest getCatMttoPeriodo() throws IOException {
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("PE.ID_MTTO_PERIODO",
                        "PE.DES_PERIODO",
                        "PE.IND_ACTIVO")
                .from("SVC_MTTO_PERIODO PE")
                .where("PE.IND_ACTIVO = :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest getCatPlacasVehiculos(DatosRequest request, Authentication authentication) throws IOException {
        Gson json = new Gson();
        CatalogoVehiculosRequest buscarRequest=new CatalogoVehiculosRequest();
        String requestBoby=String.valueOf(request.getDatos().get(AppConstantes.DATOS));
        if(requestBoby!=null && requestBoby!="null" && requestBoby.trim().length()>0) {
            buscarRequest = json.fromJson(requestBoby, CatalogoVehiculosRequest.class);
        }
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("VE.ID_VEHICULO",
                        "VE.REF_PLACAS AS DES_PLACAS",
                        "VE.IND_ACTIVO")
                .from("SVT_VEHICULOS VE")
                .join("SVC_VELATORIO VELA","VE.ID_VELATORIO=VELA.ID_VELATORIO")
                .where("VE.IND_ACTIVO = :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        if(buscarRequest!=null && buscarRequest.getDelegacion()!=null && buscarRequest.getDelegacion()>0){
            queryUtil.where("VELA.ID_DELEGACION = :delegacion")
                    .setParameter("delegacion", buscarRequest.getDelegacion());
        }
        if(buscarRequest!=null && buscarRequest.getVelatorio()!=null && buscarRequest.getVelatorio()>0){
            queryUtil.where("VE.ID_VELATORIO = :velatorio")
                    .setParameter("velatorio", buscarRequest.getVelatorio());
        }
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }


    public DatosRequest getCatContratosProveedores(DatosRequest request, Authentication authentication) throws IOException {
        Gson json = new Gson();
        CatContratosProvRequest buscarRequest=new CatContratosProvRequest();
        String requestBoby=String.valueOf(request.getDatos().get(AppConstantes.DATOS));
        if(requestBoby!=null && requestBoby!="null" && requestBoby.trim().length()>0) {
            buscarRequest = json.fromJson(requestBoby, CatContratosProvRequest.class);
        }
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("CON.ID_CONTRATO",
                        "CON.CVE_CONTRATO AS NUM_CONTRATO",
                        "CON.DES_CONTRATO",
                        "CON.ID_PROVEEDOR",
                        "PRO.REF_PROVEEDOR")
                .from("SVT_CONTRATO CON")
                .join("SVT_PROVEEDOR PRO","PRO.ID_PROVEEDOR=CON.ID_PROVEEDOR")
                .where("PRO.IND_ACTIVO= :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        if(buscarRequest!=null && buscarRequest.getProveedor()!=null && buscarRequest.getProveedor()>0){
            queryUtil.where("PRO.ID_PROVEEDOR = :proveedor")
                    .setParameter("proveedor", buscarRequest.getProveedor());
        }
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest getRegistroMtto(DatosRequest request, Authentication authentication) throws IOException {
        Gson json = new Gson();
        FiltroMttoRequest buscarRequest=new FiltroMttoRequest();
        String requestBoby=String.valueOf(request.getDatos().get(AppConstantes.DATOS));
        if(requestBoby!=null && requestBoby!="null" && requestBoby.trim().length()>0) {
            buscarRequest = json.fromJson(requestBoby, FiltroMttoRequest.class);
        }
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("MT.ID_MTTOVEHICULAR",
                        "VEI.ID_MTTOVERIFINICIO",
                        "SOL.ID_MTTO_SOLICITUD",
                        "REG.ID_MTTO_REGISTRO",
                        "MT.ID_VEHICULO")
                .from("SVT_MTTO_VEHICULAR MT")
                .leftJoin("SVT_MTTO_VERIF_INICIO VEI","VEI.ID_MTTOVEHICULAR=MT.ID_MTTOVEHICULAR")
                .leftJoin("SVT_MTTO_SOLICITUD SOL","SOL.ID_MTTOVEHICULAR=MT.ID_MTTOVEHICULAR")
                .leftJoin("SVT_MTTO_REGISTRO REG","REG.ID_MTTOVEHICULAR=MT.ID_MTTOVEHICULAR")
                .where("MT.IND_ACTIVO= :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        if(buscarRequest!=null && buscarRequest.getIdMttoVehicular()!=null && buscarRequest.getIdMttoVehicular()>0){
            queryUtil.where("MT.ID_MTTOVEHICULAR = :idMttoVehicular")
                    .setParameter("idMttoVehicular", buscarRequest.getIdMttoVehicular());
        }
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }


    public DatosRequest getMttoTipoModalidad(DatosRequest request, Authentication authentication) throws IOException {
        Gson json = new Gson();
        FiltroGenericoRequest buscarRequest=new FiltroGenericoRequest();
        String requestBoby=String.valueOf(request.getDatos().get(AppConstantes.DATOS));
        if(requestBoby!=null && requestBoby!="null" && requestBoby.trim().length()>0) {
            buscarRequest = json.fromJson(requestBoby, FiltroGenericoRequest.class);
        }
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("MTM.ID_MTTO_MODALIDAD",
                        "MTM.DES_MTTO_MODALIDAD",
                        "MTM.ID_MTTOMODALIDAD",
                        "MTM.IND_ACTIVO")
                .from("SVT_MTTO_TIPO_MODALIDAD MTM")
                .where("MTM.IND_ACTIVO= :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        if(buscarRequest!=null && buscarRequest.getIdGenerico()!=null && buscarRequest.getIdGenerico()>0){
            queryUtil.where("MTM.ID_MTTOMODALIDAD = :idModalidad")
                    .setParameter("idModalidad", buscarRequest.getIdGenerico());
        }
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest getMttoTipoModalidadDetalle(DatosRequest request, Authentication authentication) throws IOException {
        Gson json = new Gson();
        FiltroGenericoRequest buscarRequest=new FiltroGenericoRequest();
        String requestBoby=String.valueOf(request.getDatos().get(AppConstantes.DATOS));
        if(requestBoby!=null && requestBoby!="null" && requestBoby.trim().length()>0) {
            buscarRequest = json.fromJson(requestBoby, FiltroGenericoRequest.class);
        }
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("MTMD.ID_MTTO_MODALIDAD_DET",
                        "MTMD.REF_MTTO_MODALIDAD_DET",
                        "MTMD.ID_MTTO_MODALIDAD",
                        "MTMD.IND_ACTIVO")
                .from("SVT_MTTO_TIPO_MODALIDAD_DET MTMD")
                .where("MTMD.IND_ACTIVO= :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        if(buscarRequest!=null && buscarRequest.getIdGenerico()!=null && buscarRequest.getIdGenerico()>0){
            queryUtil.where("MTMD.ID_MTTO_MODALIDAD = :idMttoModalidad")
                    .setParameter("idMttoModalidad", buscarRequest.getIdGenerico());
        }
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }
}
