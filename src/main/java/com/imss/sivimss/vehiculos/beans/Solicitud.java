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
public class Solicitud {
    private static final Logger logger = LogManager.getLogger(Solicitud.class);

    public DatosRequest insertar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("INSERT INTO SVT_MTTO_SOLICITUD");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_MTTOVEHICULAR", request.getIdMttoVehicular().toString());
        q.agregarParametroValues("ID_MTTO_TIPO", request.getSolicitud().getIdMttoTipo().toString());
        q.agregarParametroValues("ID_MTTOMODALIDAD", request.getSolicitud().getIdMttoModalidad().toString());
        q.agregarParametroValues("FEC_REGISTRO", request.getSolicitud().getFecRegistro());
        if(request.getSolicitud().getDesMttoCorrectivo()!=null) {
            q.agregarParametroValues("DES_MTTO_CORRECTIVO", request.getSolicitud().getDesMttoCorrectivo());
        }
        if(request.getSolicitud().getIdMttoModalidadDet()!=null) {
            q.agregarParametroValues("ID_MTTOMODALIDAD_DET", request.getSolicitud().getIdMttoModalidadDet().toString());
        }
        q.agregarParametroValues("IND_ESTATUS", "1");
        String query = q.obtenerQueryInsertar();
        logger.info(query);
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest modificar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_SOLICITUD");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_MTTO_TIPO", request.getSolicitud().getIdMttoTipo().toString());
        q.agregarParametroValues("ID_MTTOMODALIDAD", request.getSolicitud().getIdMttoModalidad().toString());
        q.agregarParametroValues("FEC_REGISTRO", request.getSolicitud().getFecRegistro().toString());
        if(request.getSolicitud().getDesMttoCorrectivo()!=null) {
            q.agregarParametroValues("DES_MTTO_CORRECTIVO", request.getSolicitud().getDesMttoCorrectivo().toString());
        }
        if(request.getSolicitud().getIdMttoModalidadDet()!=null) {
            q.agregarParametroValues("ID_MTTOMODALIDAD_DET", request.getSolicitud().getIdMttoModalidadDet().toString());
        }
        q.agregarParametroValues("IND_ESTATUS", request.getIdEstatus().toString());
        q.addWhere("ID_MTTO_SOLICITUD =" + request.getSolicitud().getIdMttoSolicitud());
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest cambiarEstatus(Integer idMttoSolicitud, Integer status,UsuarioDto user) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_SOLICITUD");
        q.agregarParametroValues("IND_ESTATUS", String.valueOf(status));
        q.addWhere("ID_MTTO_SOLICITUD =" + idMttoSolicitud);
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }
}
