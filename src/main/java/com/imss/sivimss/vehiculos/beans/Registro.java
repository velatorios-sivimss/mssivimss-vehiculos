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
        if(request.getRegistro().getIdMttoModalidad()!=null) {
            q.agregarParametroValues("ID_MTTOMODALIDAD", request.getRegistro().getIdMttoModalidad().toString());
        }
        if(request.getRegistro().getIdMantenimiento()!=null) {
            q.agregarParametroValues("ID_MANTENIMIENTO", request.getRegistro().getIdMantenimiento().toString());
        }
        if(request.getRegistro().getDesNotas()!=null) {
            q.agregarParametroValues("DES_NOTAS", "'" + request.getRegistro().getDesNotas() + "'");
        }
        if(request.getRegistro().getIdProveedor()!=null) {
            q.agregarParametroValues("ID_PROVEEDOR", request.getRegistro().getIdProveedor().toString());
        }
        if(request.getRegistro().getDesNumcontrato()!=null) {
            q.agregarParametroValues("DES_NUMCONTRATO", "'" + request.getRegistro().getDesNumcontrato() + "'");
        }
        q.agregarParametroValues("ID_USUARIO_ALTA", request.getIdEstatus().toString());
        q.agregarParametroValues("ID_USUARIO_ALTA", user.getIdUsuario().toString());
        q.agregarParametroValues("IND_ACTIVO", "1");
        q.agregarParametroValues("FEC_ALTA", "CURRENT_TIMESTAMP()");
        if(request.getRegistro().getKilometraje()!=null) {
            q.agregarParametroValues("NUM_KILOMETRAJE", "'" + request.getRegistro().getKilometraje() + "'");
        }
        if(request.getRegistro().getDesNombreTaller()!=null) {
            q.agregarParametroValues("DES_NOMBRE_TALLER", "'" + request.getRegistro().getDesNombreTaller() + "'");
        }
        if(request.getRegistro().getCostoMtto()!=null) {
            q.agregarParametroValues("MON_COSTO_MTTO", "'" + request.getRegistro().getCostoMtto() + "'");
        }
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
        if(request.getRegistro().getIdMttoModalidad()!=null) {
            q.agregarParametroValues("ID_MTTOMODALIDAD", request.getRegistro().getIdMttoModalidad().toString());
        }
        if(request.getRegistro().getIdMantenimiento()!=null) {
            q.agregarParametroValues("ID_MANTENIMIENTO", request.getRegistro().getIdMantenimiento().toString());
        }
        if(request.getRegistro().getDesNotas()!=null) {
            q.agregarParametroValues("DES_NOTAS", "'" + request.getRegistro().getDesNotas() + "'");
        }
        if(request.getRegistro().getIdProveedor()!=null) {
            q.agregarParametroValues("ID_PROVEEDOR", request.getRegistro().getIdProveedor().toString());
        }
        if(request.getRegistro().getDesNumcontrato()!=null) {
            q.agregarParametroValues("DES_NUMCONTRATO", "'" + request.getRegistro().getDesNumcontrato() + "'");
        }
        q.agregarParametroValues("ID_USUARIO_ALTA", request.getIdEstatus().toString());
        q.agregarParametroValues("ID_USUARIO_ALTA", user.getIdUsuario().toString());
        q.agregarParametroValues("IND_ACTIVO", "1");
        q.agregarParametroValues("FEC_ALTA", "CURRENT_TIMESTAMP()");
        if(request.getRegistro().getKilometraje()!=null) {
            q.agregarParametroValues("NUM_KILOMETRAJE", "'" + request.getRegistro().getKilometraje() + "'");
        }
        if(request.getRegistro().getDesNombreTaller()!=null) {
            q.agregarParametroValues("DES_NOMBRE_TALLER", "'" + request.getRegistro().getDesNombreTaller() + "'");
        }
        if(request.getRegistro().getCostoMtto()!=null) {
            q.agregarParametroValues("MON_COSTO_MTTO", "'" + request.getRegistro().getCostoMtto() + "'");
        }
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
                        "REG.NUM_KILOMETRAJE",
                        "REG.DES_NOMBRE_TALLER",
                        "REG.MON_COSTO_MTTO",
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
                        "SV.DES_SUBMARCA",
                        "SV.DES_MODELO",
                        "SV.DES_PLACAS",
                        "SV.DES_NUMSERIE",
                        "SV.DES_NUMMOTOR",
                        "SUV.DES_USO",
                        "SD.DES_DELEGACION",
                        "SVEL.DES_VELATORIO",
                        "SMM.DES_MODALIDAD",
                        "SP.NOM_PROVEEDOR",
                        "SP.ID_TIPO_PROVEEDOR",
                        "SP.DES_TIPO_CONTRATO",
                        "SP.DES_REGIMEN")
                .from("SVT_MTTO_REGISTRO REG")
                .leftJoin("SVT_MTTO_VEHICULAR MTTO_VEH", "MTTO_VEH.ID_MTTOVEHICULAR = REG.ID_MTTOVEHICULAR")
                .leftJoin("SVC_MTTO_ESTADO SME","MTTO_VEH.ID_MTTOESTADO=SME.ID_MTTOESTADO")
                .leftJoin("SVT_VEHICULOS SV","MTTO_VEH.ID_VEHICULO=SV.ID_VEHICULO")
                .leftJoin("SVC_USO_VEHICULO SUV","SV.ID_USOVEHICULO=SUV.ID_USOVEHICULO")
                .leftJoin("SVC_DELEGACION SD","MTTO_VEH.ID_DELEGACION=SD.ID_DELEGACION")
                .leftJoin("SVC_VELATORIO SVEL","MTTO_VEH.ID_VELATORIO=SVEL.ID_VELATORIO")
                .leftJoin("SVC_MTTO_MODALIDAD SMM","SMM.ID_MTTOMODALIDAD =REG.ID_MTTOMODALIDAD")
                .leftJoin("SVT_PROVEEDOR SP","REG.ID_PROVEEDOR=SP.ID_PROVEEDOR");
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

    public DatosRequest existe(MttoVehicularRequest request) {
        String query=null;
        StringBuilder sql=new StringBuilder("SELECT REG.ID_MTTO_REGISTRO, REG.ID_MTTOVEHICULAR FROM SVT_MTTO_REGISTRO REG WHERE REG.FEC_REGISTRO=CURRENT_DATE()");
        sql.append(" AND REG.IND_ACTIVO=1").append(" AND REG.ID_MTTOVEHICULAR="+request.getRegistro().getIdMttoVehicular()).append(";");
        query = sql.toString();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        logger.info(query);
        dr.setDatos(parametro);
        return dr;
    }
}
