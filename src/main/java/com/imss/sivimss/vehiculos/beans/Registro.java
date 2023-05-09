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
public class Registro {
    private static final Logger logger = LogManager.getLogger(Registro.class);

    public DatosRequest insertar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("INSERT INTO SVT_MTTO_REGISTRO");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_MTTOVEHICULAR", request.getRegistro().getIdMttoVehicular().toString());
        q.agregarParametroValues("ID_MTTOMODALIDAD", request.getRegistro().getIdMttoModalidad().toString());
        q.agregarParametroValues("ID_MANTENIMIENTO", request.getRegistro().getIdMantenimiento().toString());
        q.agregarParametroValues("DES_NOTAS", "'"+request.getRegistro().getDesNotas() +"'");
        q.agregarParametroValues("ID_PROVEEDOR", request.getRegistro().getIdProveedor());
        q.agregarParametroValues("DES_NUMCONTRATO", "'" +request.getRegistro().getDesNumcontrato() + "'");
        q.agregarParametroValues("ID_USUARIO_ALTA", request.getIdEstatus().toString());
        q.agregarParametroValues("ID_USUARIO_ALTA", user.getIdUsuario().toString());
        q.agregarParametroValues("IND_ACTIVO", "1");
        q.agregarParametroValues("FEC_ALTA", "CURRENT_TIMESTAMP()");
        String query = q.obtenerQueryInsertar();
        logger.info(query);
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest modificar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_REGISTRO");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_MTTOMODALIDAD", request.getRegistro().getIdMttoModalidad().toString());
        q.agregarParametroValues("ID_MANTENIMIENTO", request.getRegistro().getIdMantenimiento().toString());
        q.agregarParametroValues("DES_NOTAS", "'" +request.getRegistro().getDesNotas() + "'");
        q.agregarParametroValues("ID_PROVEEDOR", request.getRegistro().getIdProveedor().toString());
        q.agregarParametroValues("DES_NUMCONTRATO", "'" +request.getRegistro().getDesNumcontrato() + "'");
        q.agregarParametroValues("IND_ACTIVO", request.getIdEstatus().toString());
        q.addWhere("ID_MTTO_REGISTRO =" + request.getRegistro().getIdMttoRegistro());
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest cambiarEstatus(Integer idMttoRegistro, Integer status,UsuarioDto user) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_REGISTRO");
        q.agregarParametroValues("IND_ACTIVO", String.valueOf(status));
        q.addWhere("ID_MTTO_REGISTRO =" + idMttoRegistro);
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

	public DatosRequest detalleRegistro(DatosRequest request) {
		String palabra = request.getDatos().get("palabra").toString();
        SelectQueryUtil queryUtil = new SelectQueryUtil();
        queryUtil.select("REG.ID_MTTO_REGISTRO",
                        "REG.ID_MTTOMODALIDAD",
                        "REG.ID_MANTENIMIENTO",
                        "REG.DES_NOTAS",
                        "REG.ID_PROVEEDOR",
                        "REG.DES_NUMCONTRATO",
                        "REG.IND_ACTIVO",
                        "REG.ID_USUARIO_ALTA",
                        "REG.FEC_ALTA",
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
                        "SMM.DES_MODALIDAD",
                        "SP.NOM_PROVEEDOR",
                        "SP.ID_TIPO_PROVEEDOR",
                        "SP.DES_TIPO_CONTRATO",
                        "SP.DES_REGIMEN")
                .from("SVT_MTTO_REGISTRO REG")
                .join("SVT_MTTO_VEHICULAR MTTO_VEH", "MTTO_VEH.ID_MTTOVEHICULAR = REG.ID_MTTOVEHICULAR")
                .join("SVC_MTTO_ESTADO SME","MTTO_VEH.ID_MTTOESTADO=SME.ID_MTTOESTADO")
                .join("SVT_VEHICULOS SV","MTTO_VEH.ID_VEHICULO=SV.ID_VEHICULO")
                .join("SVC_USO_VEHICULO SUV","SV.ID_USOVEHICULO=SUV.ID_USOVEHICULO")
                .join("SVC_DELEGACION SD","MTTO_VEH.ID_DELEGACION=SD.ID_DELEGACION")
                .join("SVC_VELATORIO SVEL","MTTO_VEH.ID_VELATORIO=SVEL.ID_VELATORIO")
                .join("SVC_MTTO_MODALIDAD SMM","SMM.ID_MTTOMODALIDAD =REG.ID_MTTOMODALIDAD")
                .join("SVT_PROVEEDOR SP","REG.ID_PROVEEDOR=SP.ID_PROVEEDOR");
        if(palabra!=null && palabra.trim().length()>0) {
            queryUtil.where("REG.ID_MTTO_REGISTRO = :idRegistro")
                    .setParameter("idRegistro", Integer.parseInt(palabra));
        } else {
            queryUtil.orderBy("REG.ID_MTTO_REGISTRO");
        }
        String query = queryUtil.build();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		logger.info(query);
		return request;
	}
}
