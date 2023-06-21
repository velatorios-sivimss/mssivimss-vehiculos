package com.imss.sivimss.vehiculos.beans;

import com.google.gson.Gson;
import com.imss.sivimss.vehiculos.model.request.BuscarVehiculosRequest;
import com.imss.sivimss.vehiculos.model.request.CatalogoVehiculosRequest;
import com.imss.sivimss.vehiculos.util.AppConstantes;
import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.SelectQueryUtil;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
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
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
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
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
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
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
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
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
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
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest getCatProveedores() throws IOException {
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("ID_PROVEEDOR",
                        "NOM_PROVEEDOR",
                        "DES_BANCO",
                        "CVE_BANCARIA",
                        "ID_TIPO_PROVEEDOR",
                        "DES_RFC",
                        "DES_CURP",
                        "DES_TIPO_CONTRATO",
                        "FEC_VIGENCIA",
                        "DES_DIRECCION",
                        "NUM_EXTERIOR",
                        "NUM_INTERIOR",
                        "ID_CODIGO_POSTAL",
                        "DES_DIRECCION_REFE",
                        "NUM_EXTERIOR_REFE",
                        "NUM_INTERIOR_REFE",
                        "ID_CODIGO_POSTAL_REFE",
                        "NUM_TELEFONO",
                        "DES_CORREOE",
                        "DES_REGIMEN",
                        "DES_REPRESENTANTE_LEGAL",
                        "IND_ACTIVO",
                        "ID_USUARIO_ALTA")
                .from("SVT_PROVEEDOR PV")
                .where("PV.IND_ACTIVO = :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
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
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
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
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
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
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
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
                        "VE.DES_PLACAS",
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
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

}
