package com.imss.sivimss.vehiculos.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imss.sivimss.vehiculos.model.request.BuscarVehiculosRequest;
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
public class Vehiculos {
    private static final Logger logger = LogManager.getLogger(Vehiculos.class);

    public DatosRequest buscarVehiculos(DatosRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonResult = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request.getDatos());
        BuscarVehiculosRequest buscarRequest = mapper.readValue(jsonResult,BuscarVehiculosRequest.class);
        logger.info("pagina: {}", buscarRequest.getPagina());
        logger.info("tamanio: {}", buscarRequest.getTamanio());
        String query = "";
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("ID_VEHICULO",
                        "ID_USOVEHICULO",
                        "UV.DES_USO",
                        "ID_VELATORIO",
                        "DES_MARCA",
                        "DES_SUBMARCA",
                        "DES_MODELO",
                        "DES_PLACAS",
                        "DES_NUMSERIE",
                        "DES_NUMMOTOR",
                        "DESCRIPCION",
                        "FEC_ADQUISICION",
                        "TOTAL",
                        "IMPORTE_PRIMA",
                        "IND_ESTATUS",
                        "ID_OFICINA",
                        "OF.DES_NIVELOFICINA",
                        "VE.NOM_VELATORIO",
                        "VE.NOM_RESPO_SANITARIO",
                        "VE.CVE_ASIGNACION")
                .from("SVT_VEHICULOS VH")
                .join("SVC_USO_VEHICULO UV", "VH.ID_USOVEHICULO = UV.ID_USOVEHICULO")
                .join("SVC_VELATORIO VE", "VH.ID_VELATORIO = VE.ID_VELATORIO")
                .join("SVC_NIVEL_OFICINA OF", "VH.ID_OFICINA = OF.ID_OFICINA")
                .join("SVT_MTTO_VEHICULAR MV", "MV.ID_VEHICULO = VH.ID_VEHICULO")
                .join("SVC_MTTO_ESTADO ME", "ME.ID_MTTOESTADO = VH.ID_MTTOESTADO")
                .where("VH.IND_ESTATUS = :idEstatus")
                .setParameter("idEstatus", 1);
        if (buscarRequest.getNivelOficina() != null) {
            queryUtil.where("VH.ID_OFICINA = :nivelOficina")
                    .setParameter("nivelOficina", buscarRequest.getNivelOficina());
        }
        if (buscarRequest.getPlaca() != null && buscarRequest.getPlaca().trim().length()>0) {
            queryUtil.where("VH.DES_PLACAS = :placa")
                    .setParameter("placa", buscarRequest.getPlaca());
        }
        if (buscarRequest.getTipoMtto() != null) {
            queryUtil.where("VH.DES_TIPO = :tipoMtto")
                    .setParameter("tipoMtto", buscarRequest.getTipoMtto());
        }
        if (buscarRequest.getEstadoMtto() != null) {
            queryUtil.where("VH.DES_TIPO = :tipoMtto")
                    .setParameter("tipoMtto", buscarRequest.getEstadoMtto());
        }
        if (buscarRequest.getPeriodo()!= null) {
            queryUtil.where("VH.DES_PERIDO = :periodo")
                    .setParameter("periodo", buscarRequest.getPeriodo());
        }
        query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        request.getDatos().remove("datos");
        dr.setDatos(parametro);
        return dr;
    }
}
