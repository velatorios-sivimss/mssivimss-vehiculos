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
public class Registro {
    private static final Logger logger = LogManager.getLogger(Registro.class);

    public DatosRequest insertar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("INSERT INTO SVT_MTTO_REGISTRO");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_MTTOVEHICULAR", request.getIdMttoVehicular().toString());
        q.agregarParametroValues("ID_MTTOMODALIDAD", request.getRegistro().getIdMttoModalidad().toString());
        q.agregarParametroValues("ID_MANTENIMIENTO", request.getRegistro().getIdMantenimiento().toString());
        q.agregarParametroValues("DES_NOTAS", request.getRegistro().getDesNotas());
        q.agregarParametroValues("ID_PROVEEDOR", request.getRegistro().getIdProveedor().toString());
        q.agregarParametroValues("DES_NUMCONTRATO", request.getRegistro().getDesNumcontrato());
        q.agregarParametroValues("IND_ESTATUS", "1");
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
        q.agregarParametroValues("DES_NOTAS", request.getRegistro().getDesNotas());
        q.agregarParametroValues("ID_PROVEEDOR", request.getRegistro().getIdProveedor().toString());
        q.agregarParametroValues("DES_NUMCONTRATO", request.getRegistro().getDesNumcontrato());
        q.agregarParametroValues("IND_ESTATUS", request.getIdEstatus().toString());
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
        q.agregarParametroValues("IND_ESTATUS", String.valueOf(status));
        q.addWhere("ID_MTTO_REGISTRO =" + idMttoRegistro);
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }
}
