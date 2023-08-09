package com.imss.sivimss.vehiculos.beans;

import com.google.gson.Gson;
import com.imss.sivimss.vehiculos.model.request.BuscarVehiculosRequest;
import com.imss.sivimss.vehiculos.model.request.UsuarioDto;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class Vehiculos {
    private static final Logger logger = LogManager.getLogger(Vehiculos.class);
    private static SimpleDateFormat formatoRequest = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat formatoConsulta = new SimpleDateFormat("yyyy-MM-dd");

    public DatosRequest buscarVehiculos(DatosRequest request, Authentication authentication) throws IOException, ParseException {
        Gson json = new Gson();
        BuscarVehiculosRequest  buscarRequest=new BuscarVehiculosRequest();
        String requestBoby=String.valueOf(request.getDatos().get(AppConstantes.DATOS));
        if(requestBoby!=null && requestBoby!="null" && requestBoby.trim().length()>0) {
            buscarRequest = json.fromJson(requestBoby, BuscarVehiculosRequest.class);
        } else {
            //Obtiene valores del usuario
            UsuarioDto usuarioDto = json.fromJson(authentication.getPrincipal().toString(), UsuarioDto.class);
            buscarRequest.setDelegacion(usuarioDto.getIdDelegacion());
            buscarRequest.setVelatorio(usuarioDto.getIdVelatorio());
        }
        Integer pagina = Integer.valueOf(Integer.parseInt(request.getDatos().get("pagina").toString()));
        Integer tamanio = Integer.valueOf(Integer.parseInt(request.getDatos().get("tamanio").toString()));
        buscarRequest.setTamanio(tamanio.toString());
        buscarRequest.setPagina(pagina.toString());
        String query = "";
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        
        StringBuilder where = new StringBuilder();
        where.append("VH.IND_ACTIVO=1 " );
         //       .where("VH.IND_ACTIVO = :idEstatus")
           //     .setParameter("idEstatus", 1);
        if (buscarRequest.getDelegacion() != null && buscarRequest.getDelegacion()>0) {
            where.append(" AND VE.ID_DELEGACION = "+buscarRequest.getDelegacion()+"");
                    //.setParameter("delegacion", buscarRequest.getDelegacion());
        }
        if (buscarRequest.getVelatorio() != null && buscarRequest.getVelatorio() >0) {
        	  where.append(" AND VH.ID_VELATORIO = "+buscarRequest.getVelatorio()+"");
          //  queryUtil.where("VH.ID_VELATORIO = :velatorio")
            //        .setParameter("velatorio", buscarRequest.getVelatorio());
        }
        if (buscarRequest.getPlaca() != null && buscarRequest.getPlaca().trim().length()>0) {
        	 where.append(" AND VH.DES_PLACAS = '"+buscarRequest.getPlaca()+"' ");
           // queryUtil.where("VH.DES_PLACAS = :placa")
             //       .setParameter("placa", buscarRequest.getPlaca());
        }
        if (buscarRequest.getFecInicio() != null) {
            Date fechaFIRequest=formatoRequest.parse(buscarRequest.getFecInicio());
            where.append(" AND MV.FEC_REGISTRO >='"+formatoConsulta.format(fechaFIRequest)+"' ");
            //   queryUtil.where("MV.FEC_REGISTRO >= :fecInicio")
          //  .setParameter("fecInicio", formatoConsulta.format(fechaFIRequest));
        }
        if (buscarRequest.getFecFin() != null) {
        	  Date fechaFIRequest=formatoRequest.parse(buscarRequest.getFecFin());
            where.append(" AND MV.FEC_REGISTRO <='"+formatoConsulta.format(fechaFIRequest)+"' ");
        	  //  queryUtil.where("MV.FEC_REGISTRO <= :fecFin")
             // .setParameter("fecFin", formatoConsulta.format(fechaFIRequest));
              //queryUtil.groupBy("MV.ID_VEHICULO");
        }else {
        	queryUtil.orderBy("ID_MTTOVEHICULAR ASC"); 	
        }
        logger.info("where "+where.toString());
        
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
                        "DATE_FORMAT(VH.FEC_ADQUISICION, '%d-%m-%Y') AS FEC_ADQUISICION",
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
                        "DATE_FORMAT(VI.FEC_REGISTRO,'%d-%m-%Y') AS FECHA_REGISTRO_VERI_INICIO",
                        "MS.ID_MTTO_SOLICITUD",
                        "DATE_FORMAT(MS.FEC_SOLICTUD, '%d-%m-%Y') AS FEC_SOLICTUD",
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
                        "DATE_FORMAT(REG.FEC_REGISTRO_REG,'%d-%m-%Y') AS FEC_REGISTRO_REG",
                        "DATE_FORMAT(MS.FEC_REGISTRO,'%d-%m-%Y') AS FEC_MTTO_SOL",
                        "MS.DES_MTTO_CORRECTIVO",
                        "SP.NOM_PROVEEDOR",
                        "(select case count(mvt.ID_VEHICULO) when 0 then 'false' else 'true' end as verificacion from SVT_MTTO_VERIF_INICIO vit left join SVT_MTTO_VEHICULAR mvt on (vit.ID_MTTOVEHICULAR=mvt.ID_MTTOVEHICULAR) where mvt.ID_VEHICULO =VH.ID_VEHICULO and vit.FEC_REGISTRO =CURRENT_DATE()) as verificacionDia",
                        "(select\r\n"
                        + " SUM(REG.MON_COSTO_MTTO)\r\n"
                        + " from SVT_VEHICULOS VH  \r\n"
                        + " join SVC_USO_VEHICULO UV on VH.ID_USOVEHICULO = UV.ID_USOVEHICULO  \r\n"
                        + " join SVC_VELATORIO VE on VH.ID_VELATORIO = VE.ID_VELATORIO \r\n"
                        + " join SVC_NIVEL_OFICINA OFI on VH.ID_OFICINA = OFI.ID_OFICINA  \r\n"
                        + " left join SVT_MTTO_VEHICULAR MV on MV.ID_VEHICULO = VH.ID_VEHICULO  \r\n"
                        + " left join SVT_MTTO_SOLICITUD MS on MV.ID_MTTOVEHICULAR = MS.ID_MTTOVEHICULAR  \r\n"
                        + " left join SVT_MTTO_VERIF_INICIO VI on VI.ID_MTTOVEHICULAR = MV.ID_MTTOVEHICULAR\r\n"
                        + " left join SVT_MTTO_REGISTRO REG on REG.ID_MTTOVEHICULAR = MV.ID_MTTOVEHICULAR  \r\n"
                        + " left join SVT_PROVEEDOR SP on REG.ID_PROVEEDOR = SP.ID_PROVEEDOR \r\n"
                        + " left join SVC_MTTO_ESTADO ME on ME.ID_MTTOESTADO = MV.ID_MTTOESTADO  \r\n"
                        + " left join SVC_MTTO_MODALIDAD TM on TM.ID_MTTOMODALIDAD = MS.ID_MTTOMODALIDAD  \r\n"
                        + " left join SVC_MTTO_TIPO MT on MT.ID_MTTO_TIPO = MS.ID_MTTO_TIPO  \r\n"
                        + " left join SVC_DELEGACION DL on DL.ID_DELEGACION = MV.ID_DELEGACION  "
                        + " where "+where.toString()+") AS totalCosto ",
                        "MODAL.DES_MTTO_MODALIDAD_DET")
                .from("SVT_VEHICULOS VH")
                .join("SVC_USO_VEHICULO UV", "VH.ID_USOVEHICULO = UV.ID_USOVEHICULO")
                .join("SVC_VELATORIO VE", "VH.ID_VELATORIO = VE.ID_VELATORIO")
                .join("SVC_NIVEL_OFICINA OFI", "VH.ID_OFICINA = OFI.ID_OFICINA")
                .leftJoin("SVT_MTTO_VEHICULAR MV", "MV.ID_VEHICULO = VH.ID_VEHICULO")
                .leftJoin("SVT_MTTO_SOLICITUD MS", "MV.ID_MTTOVEHICULAR = MS.ID_MTTOVEHICULAR")
                .leftJoin("SVT_MTTO_TIPO_MODALIDAD_DET MODAL", "MS.ID_MTTO_MODALIDAD_DET  = MODAL.ID_MTTO_MODALIDAD_DET ")
                .leftJoin("SVT_MTTO_VERIF_INICIO VI", "VI.ID_MTTOVEHICULAR = MV.ID_MTTOVEHICULAR")
                .leftJoin("SVT_MTTO_REGISTRO REG", "REG.ID_MTTOVEHICULAR = MV.ID_MTTOVEHICULAR")
                .leftJoin("SVT_PROVEEDOR SP", "REG.ID_PROVEEDOR = SP.ID_PROVEEDOR ")
                .leftJoin("SVC_MTTO_ESTADO ME", "ME.ID_MTTOESTADO = MV.ID_MTTOESTADO")
                .leftJoin("SVC_MTTO_MODALIDAD TM", "TM.ID_MTTOMODALIDAD = MS.ID_MTTOMODALIDAD")
                .leftJoin("SVC_MTTO_TIPO MT", "MT.ID_MTTO_TIPO = MS.ID_MTTO_TIPO")
                .leftJoin("SVC_DELEGACION DL", "DL.ID_DELEGACION = MV.ID_DELEGACION");
   
        queryUtil.where(where.toString());
        query = queryUtil.build();
        logger.info("estoy en ----> "+query);
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        parametro.put("pagina", buscarRequest.getPagina());
        parametro.put("tamanio", buscarRequest.getTamanio());
        request.getDatos().remove("datos");
        dr.setDatos(parametro);
        return dr;
    }
}
