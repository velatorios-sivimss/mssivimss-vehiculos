package com.imss.sivimss.vehiculos.beans;

import com.google.gson.Gson;
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
        Gson json = new Gson();
        BuscarVehiculosRequest  buscarRequest=new BuscarVehiculosRequest();
        String requestBoby=String.valueOf(request.getDatos().get(AppConstantes.DATOS));
        if(requestBoby!=null && requestBoby!="null" && requestBoby.trim().length()>0) {
            buscarRequest = json.fromJson(requestBoby, BuscarVehiculosRequest.class);
        }
        Integer pagina = Integer.valueOf(Integer.parseInt(request.getDatos().get("pagina").toString()));
        Integer tamanio = Integer.valueOf(Integer.parseInt(request.getDatos().get("tamanio").toString()));
        buscarRequest.setTamanio(tamanio.toString());
        buscarRequest.setPagina(pagina.toString());
        String query = "";
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("VH.ID_VEHICULO",
                        "VH.ID_USOVEHICULO",
                        "UV.DES_USO",
                        "VH.ID_VELATORIO",
                        "VH.DES_MARCA",
                        "VH.DES_SUBMARCA",
                        "VH.DES_MODELO",
                        "VH.DES_PLACAS",
                        "VH.DES_NUMSERIE",
                        "VH.DES_NUMMOTOR",
                        "VH.DESCRIPCION",
                        "VH.FEC_ADQUISICION",
                        "VH.TOTAL",
                        "VH.IMPORTE_PRIMA",
                        "VH.IND_ACTIVO",
                        "VH.ID_OFICINA",
                        "OFI.DES_NIVELOFICINA",
                        "VE.NOM_VELATORIO",
                        "VE.NOM_RESPO_SANITARIO",
                        "VE.CVE_ASIGNACION",
                        "ME.DES_MTTOESTADO",
                        "TM.DES_MODALIDAD",
                        "MT.DES_MTTO_TIPO",
                        "MV.ID_MTTOVEHICULAR",
                        "MS.ID_MTTO_SOLICITUD",
                        "VI.ID_MTTOVERIFINICIO",
                        "REG.ID_MTTO_REGISTRO",
                        "MV.ID_DELEGACION",
                        "DL.DES_DELEGACION",
                        "(select case count(mvt.ID_VEHICULO) when 0 then 'false' else 'true' end as verificacion from SVT_MTTO_VERIF_INICIO vit left join SVT_MTTO_VEHICULAR mvt on (vit.ID_MTTOVEHICULAR=mvt.ID_MTTOVEHICULAR) where mvt.ID_VEHICULO =VH.ID_VEHICULO and vit.FEC_REGISTRO =CURRENT_DATE()) as verificacionDia")
                .from("SVT_VEHICULOS VH")
                .join("SVC_USO_VEHICULO UV", "VH.ID_USOVEHICULO = UV.ID_USOVEHICULO")
                .join("SVC_VELATORIO VE", "VH.ID_VELATORIO = VE.ID_VELATORIO")
                .join("SVC_NIVEL_OFICINA OFI", "VH.ID_OFICINA = OFI.ID_OFICINA")
                .leftJoin("SVT_MTTO_VEHICULAR MV", "MV.ID_VEHICULO = VH.ID_VEHICULO")
                .leftJoin("SVT_MTTO_SOLICITUD MS", "MV.ID_MTTOVEHICULAR = MS.ID_MTTOVEHICULAR")
                .leftJoin("SVT_MTTO_VERIF_INICIO VI", "VI.ID_MTTOVEHICULAR = MV.ID_MTTOVEHICULAR")
                .leftJoin("SVT_MTTO_REGISTRO REG", "REG.ID_MTTOVEHICULAR = MV.ID_MTTOVEHICULAR")
                .leftJoin("SVC_MTTO_ESTADO ME", "ME.ID_MTTOESTADO = MV.ID_MTTOESTADO")
                .leftJoin("SVC_MTTO_MODALIDAD TM", "TM.ID_MTTOMODALIDAD = MS.ID_MTTOMODALIDAD")
                .leftJoin("SVC_MTTO_TIPO MT", "MT.ID_MTTO_TIPO = MS.ID_MTTO_TIPO")
                .leftJoin("SVC_DELEGACION DL", "DL.ID_DELEGACION = MV.ID_DELEGACION")
                .where("VH.IND_ACTIVO = :idEstatus")
                .setParameter("idEstatus", 1);
        if (buscarRequest.getNivelOficina() != null) {
            queryUtil.where("VH.ID_OFICINA = :nivelOficina")
                    .setParameter("nivelOficina", buscarRequest.getNivelOficina());
        }
        if (buscarRequest.getIdVehiculo() != null && buscarRequest.getIdVehiculo()>0) {
            queryUtil.where("VH.ID_VEHICULO = :idVehiculo")
                    .setParameter("idVehiculo", buscarRequest.getIdVehiculo());
        }
        if (buscarRequest.getPlaca() != null && buscarRequest.getPlaca().trim().length()>0) {
            queryUtil.where("VH.DES_PLACAS = :placa")
                    .setParameter("placa", buscarRequest.getPlaca());
        }
        if (buscarRequest.getTipoMtto() != null) {
            queryUtil.where("MS.ID_MTTO_TIPO = :tipoMtto")
                    .setParameter("tipoMtto", buscarRequest.getTipoMtto());
        }
        if (buscarRequest.getEstadoMtto() != null) {
            queryUtil.where("MV.ID_MTTOESTADO = :tipoMtto")
                    .setParameter("tipoMtto", buscarRequest.getEstadoMtto());
        }
        query = queryUtil.build();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        parametro.put("pagina",buscarRequest.getPagina());
        parametro.put("tamanio",buscarRequest.getTamanio());
        request.getDatos().remove("datos");
        dr.setDatos(parametro);
        return dr;
    }
}
