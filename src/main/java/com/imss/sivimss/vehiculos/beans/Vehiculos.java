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
                        "VH.DES_VEHICULO",
                        "VH.FEC_ADQUISICION",
                        "VH.NUM_TOTAL",
                        "VH.IMP_PRIMA",
                        "VH.IND_ACTIVO",
                        "VH.ID_OFICINA",
                        "OFI.DES_NIVELOFICINA",
                        "VE.DES_VELATORIO",
                        "VE.NOM_RESPO_SANITARIO",
                        "VE.CVE_ASIGNACION",
                        "ME.DES_MTTOESTADO",
                        "TM.DES_MODALIDAD",
                        "MT.DES_MTTO_TIPO",
                        "MV.ID_MTTOVEHICULAR",
                        "MV.ID_DELEGACION",
                        "DL.DES_DELEGACION",
                        "VI.ID_MTTOVERIFINICIO",
                        "VI.ID_NIVELACEITE",
                        "VI.ID_NIVELAGUA",
                        "VI.ID_CALNEUTRASEROS",
                        "VI.ID_CALNEUDELANTEROS",
                        "VI.ID_NIVELCOMBUSTIBLE",
                        "VI.ID_NIVELBATERIA",
                        "VI.ID_CODIGOFALLO",
                        "VI.ID_LIMPIEZAINTERIOR",
                        "VI.ID_LIMPIEZAEXTERIOR",
                        "VI.FEC_REGISTRO AS FECHA_REGISTRO_VERI_INICIO",
                        "MS.ID_MTTO_SOLICITUD",
                        "MS.FEC_SOLICTUD",
                        "MS.NUM_KILOMETRAJE AS NUM_KILOMETRAJE_SOL",
                        "MS.KILOMETRAJE AS KILOMETRAJE_SOL",
                        "MS.DES_NOTAS AS DES_NOTAS_SOL",
                        "REG.ID_MTTO_REGISTRO",
                        "REG.ID_PROVEEDOR",
                        "REG.DES_NUMCONTRATO",
                        "REG.MON_COSTO_MTTO",
                        "REG.DES_NOMBRE_TALLER",
                        "REG.NUM_KILOMETRAJE AS NUM_KILOMETRAJE_REG",
                        "REG.COSTO_MTTO AS COSTO_MTTO_REG",
                        "REG.FEC_REGISTRO AS FEC_REGISTRO_REG",
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
        if (buscarRequest.getVelatorio() != null) {
            queryUtil.where("MV.ID_VELATORIO = :velatorio")
                    .setParameter("velatorio", buscarRequest.getVelatorio());
        }
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
        logger.info(query);
        return dr;
    }
}
