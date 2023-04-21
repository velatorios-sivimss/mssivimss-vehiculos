package com.imss.sivimss.vehiculos.beans;

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
public class MttoCatalogos {
    private static final Logger logger = LogManager.getLogger(MttoCatalogos.class);

    private static final Integer INDESTATUS=1;

    public DatosRequest getCatMttoUsoVehiculo() throws IOException {
        logger.info("Genera consulta para obtener lista de uso de vehiculo");
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("ID_USOVEHICULO",
                        "DES_USO",
                        "IND_ESTATUS")
                .from("SVC_USO_VEHICULO UV")
                .where("UV.IND_ESTATUS = :idEstatus")
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
                        "IND_ESTATUS")
                .from("SVC_MTTO_MODALIDAD MM")
                .where("MM.IND_ESTATUS = :idEstatus")
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
                        "IND_ESTATUS")
                .from("SVC_MTTO_TIPO MT")
                .where("MT.IND_ESTATUS = :idEstatus")
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
                        "IND_ESTATUS")
                .from("SVC_MTTO_ESTADO ME")
                .where("ME.IND_ESTATUS = :idEstatus")
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
                        "NOM_BANCO",
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
                        "CVE_ESTATUS",
                        "ID_USUARIO_ALTA")
                .from("SVT_PROVEEDOR PV")
                .where("PV.CVE_ESTATUS = :idEstatus")
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
                        "IND_ESTATUS")
                .from("SVC_MTTO_NIVEL MN")
                .where("MN.IND_ESTATUS = :idEstatus")
                .setParameter("idEstatus", INDESTATUS);
        String query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

}
