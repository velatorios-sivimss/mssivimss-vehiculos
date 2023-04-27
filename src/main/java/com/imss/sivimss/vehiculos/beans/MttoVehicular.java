package com.imss.sivimss.vehiculos.beans;

import com.imss.sivimss.vehiculos.model.request.MttoVehicularRequest;
import com.imss.sivimss.vehiculos.model.request.UsuarioDto;
import com.imss.sivimss.vehiculos.util.AppConstantes;
import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.QueryHelper;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
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
        q.agregarParametroValues("ID_MTTOESTADO", request.getIdMttoestado().toString());
        q.agregarParametroValues("ID_VEHICULO", request.getIdVehiculo().toString());
        q.agregarParametroValues("ID_DELEGACION", request.getIdDelegacion().toString());
        q.agregarParametroValues("ID_VELATORIO", request.getIdVelatorio().toString());
        q.agregarParametroValues("FEC_ALTA", FECHAACTUAL);
        q.agregarParametroValues("FEC_ACTUALIZACION", null);
        q.agregarParametroValues("FEC_BAJA", null);
        q.agregarParametroValues("IND_ESTATUS", "1");
        String query = q.obtenerQueryInsertar();
        logger.info(query);
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
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
        q.agregarParametroValues("FEC_BAJA", null);
        q.agregarParametroValues("IND_ESTATUS", request.getIdEstatus().toString());
        q.addWhere("ID_MTTOVEHICULAR =" + request.getIdMttoVehicular());
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest cambiarEstatus(Integer idMttoVehicular, Integer status,UsuarioDto user) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_VEHICULAR");
        q.agregarParametroValues("IND_ESTATUS", String.valueOf(status));
        if(status.equals(1)) {
            q.agregarParametroValues("FEC_ACTUALIZACION", FECHAACTUAL);
        } else if(status.equals(0)){
            q.agregarParametroValues("FEC_BAJA", FECHAACTUAL);
        }
        q.addWhere("ID_MTTOVEHICULAR =" + idMttoVehicular);
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }
}
