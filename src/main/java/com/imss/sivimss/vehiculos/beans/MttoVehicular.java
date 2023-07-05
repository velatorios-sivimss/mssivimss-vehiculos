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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class MttoVehicular {
    private static final Logger logger = LogManager.getLogger(MttoVehicular.class);

    private static final String FECHAACTUAL="CURRENT_TIMESTAMP()";

    public DatosRequest insertar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("INSERT INTO SVT_MTTO_VEHICULAR");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
     //   q.agregarParametroValues("ID_MTTOESTADO", request.getIdMttoestado().toString());
        q.agregarParametroValues("ID_VEHICULO", request.getIdVehiculo().toString());
        q.agregarParametroValues("ID_DELEGACION", request.getIdDelegacion().toString());
        q.agregarParametroValues("ID_VELATORIO", request.getIdVelatorio().toString());
        q.agregarParametroValues("FEC_ALTA", FECHAACTUAL);
        q.agregarParametroValues("ID_USUARIO_ALTA", user.getIdUsuario().toString());
        q.agregarParametroValues("IND_ACTIVO", "1");
        String query = q.obtenerQueryInsertar();
        logger.info(query);
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest modificar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_VEHICULAR");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_MTTOESTADO", request.getIdMttoestado().toString());
        q.agregarParametroValues("ID_VEHICULO", request.getIdVehiculo().toString());
        q.agregarParametroValues("ID_DELEGACION", request.getIdDelegacion().toString());
        q.agregarParametroValues("ID_VELATORIO", request.getIdVelatorio().toString());
        q.agregarParametroValues("FEC_ACTUALIZACION", FECHAACTUAL);
        q.agregarParametroValues("IND_ACTIVO", request.getIdEstatus().toString());
        q.addWhere("ID_MTTOVEHICULAR =" + request.getIdMttoVehicular());
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        logger.info(query);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest cambiarEstatus(Integer idMttoVehicular, Integer status,UsuarioDto user) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_VEHICULAR");
        q.agregarParametroValues("IND_ACTIVO", String.valueOf(status));
        if(status.equals(1)) {
            q.agregarParametroValues("FEC_ACTUALIZACION", FECHAACTUAL);
        } else if(status.equals(0)){
            q.agregarParametroValues("FEC_BAJA", FECHAACTUAL);
        }
        q.addWhere("ID_MTTOVEHICULAR =" + idMttoVehicular);
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        logger.info(query);
        return dr;
    }

    public DatosRequest existe(MttoVehicularRequest request) {
        String query=null;
        StringBuilder sql=new StringBuilder("SELECT MT.ID_MTTOVEHICULAR, MT.ID_VEHICULO, MT.IND_ACTIVO, MT.FEC_REGISTRO FROM SVT_MTTO_VEHICULAR MT WHERE MT.ID_MTTOESTADO IN (1,2,4)");
        sql.append(" AND MT.IND_ACTIVO=1").append(" AND MT.ID_VEHICULO="+request.getIdVehiculo()).append(";");
        query = sql.toString();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        logger.info(query);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest existeFechaRegistro(MttoVehicularRequest request) {
        String query=null;
        StringBuilder sql=new StringBuilder("SELECT MAX(MT.ID_MTTOVEHICULAR) AS ID_MTTOVEHICULAR, MT.ID_VEHICULO, MT.IND_ACTIVO, MT.FEC_REGISTRO FROM SVT_MTTO_VEHICULAR MT WHERE ");
        sql.append(" MT.IND_ACTIVO=1").append(" AND MT.ID_VEHICULO="+request.getIdVehiculo()).append(";");
        query = sql.toString();
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        logger.info(query);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest modificarEstatusMtto(Integer idMttoVehicular, Integer idMttoestado) {
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_VEHICULAR");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_MTTOESTADO", idMttoestado.toString());
        q.addWhere("ID_MTTOVEHICULAR =" + idMttoVehicular);
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
        parametro.put(AppConstantes.QUERY, encoded);
        logger.info(query);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest validaEstatusMtto() {
        String query=null;
        StringBuilder sql=new StringBuilder("SELECT MT.ID_MTTOVEHICULAR, MT.FEC_REGISTRO, MT.IND_ACTIVO, SOL.FEC_SOLICTUD,REG.FEC_REGISTRO_REG ");
        sql.append("FROM SVT_MTTO_VEHICULAR MT ");
        sql.append("LEFT JOIN SVT_MTTO_SOLICITUD SOL ON (SOL.ID_MTTOVEHICULAR=MT.ID_MTTOVEHICULAR) ");
        sql.append("LEFT JOIN SVT_MTTO_REGISTRO REG ON (REG.ID_MTTOVEHICULAR=MT.ID_MTTOVEHICULAR) ");
        sql.append("WHERE MT.IND_ACTIVO=1 AND MT.ID_MTTOESTADO!=3  ORDER BY MT.ID_MTTOVEHICULAR ASC");
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
