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
public class MttoVerifiInicio {
    private static final Logger logger = LogManager.getLogger(MttoVerifiInicio.class);

    public DatosRequest insertar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("INSERT INTO SVT_MTTO_VERIF_INICIO");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_MTTOVEHICULAR", request.getIdMttoVehicular().toString());
        q.agregarParametroValues("ID_NIVELACEITE", request.getVerificacionInicio().getIdNivelAceite().toString());
        q.agregarParametroValues("ID_NIVELAGUA", request.getVerificacionInicio().getIdNivelAgua().toString());
        q.agregarParametroValues("ID_CALNEUTRASEROS", request.getVerificacionInicio().getIdCalNeuTraseros().toString());
        q.agregarParametroValues("ID_CALNEUDELANTEROS", request.getVerificacionInicio().getIdCalNeuDelanteros().toString());
        q.agregarParametroValues("ID_NIVELCOMBUSTIBLE", request.getVerificacionInicio().getIdNivelCombustible().toString());
        q.agregarParametroValues("ID_NIVELBATERIA", request.getVerificacionInicio().getIdNivelBateria().toString());
        q.agregarParametroValues("ID_CODIGOFALLO", request.getVerificacionInicio().getIdCodigoFallo().toString());
        q.agregarParametroValues("ID_LIMPIEZAINTERIOR", request.getVerificacionInicio().getIdLimpiezaInterior().toString());
        q.agregarParametroValues("ID_LIMPIEZAEXTERIOR", request.getVerificacionInicio().getIdLimpiezaExterior().toString());
        q.agregarParametroValues("IND_ESTATUS", "1");
        String query = q.obtenerQueryInsertar();
        logger.info(query);
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest modificar(MttoVehicularRequest request, UsuarioDto user) {
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_VERIF_INICIO");
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        q.agregarParametroValues("ID_NIVELACEITE", request.getVerificacionInicio().getIdNivelAceite().toString());
        q.agregarParametroValues("ID_NIVELAGUA", request.getVerificacionInicio().getIdNivelAgua().toString());
        q.agregarParametroValues("ID_CALNEUTRASEROS", request.getVerificacionInicio().getIdCalNeuTraseros().toString());
        q.agregarParametroValues("ID_CALNEUDELANTEROS", request.getVerificacionInicio().getIdCalNeuDelanteros().toString());
        q.agregarParametroValues("ID_NIVELCOMBUSTIBLE", request.getVerificacionInicio().getIdNivelCombustible().toString());
        q.agregarParametroValues("ID_NIVELBATERIA", request.getVerificacionInicio().getIdNivelBateria().toString());
        q.agregarParametroValues("ID_CODIGOFALLO", request.getVerificacionInicio().getIdCodigoFallo().toString());
        q.agregarParametroValues("ID_LIMPIEZAINTERIOR", request.getVerificacionInicio().getIdLimpiezaInterior().toString());
        q.agregarParametroValues("ID_LIMPIEZAEXTERIOR", request.getVerificacionInicio().getIdLimpiezaExterior().toString());
        q.agregarParametroValues("IND_ESTATUS", request.getIdEstatus().toString());
        q.addWhere("ID_MTTOVERIFINICIO =" + request.getVerificacionInicio().getIdMttoVerifInicio());
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }

    public DatosRequest cambiarEstatus(Integer idMmttoVerifInicio, Integer status,UsuarioDto user) {
        DatosRequest dr = new DatosRequest();
        Map<String, Object> parametro = new HashMap<>();
        final QueryHelper q = new QueryHelper("UPDATE SVT_MTTO_VERIF_INICIO");
        q.agregarParametroValues("IND_ESTATUS", String.valueOf(status));
        q.addWhere("ID_MTTOVERIFINICIO =" + idMmttoVerifInicio);
        String query = q.obtenerQueryActualizar();
        String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
        parametro.put(AppConstantes.QUERY, encoded);
        dr.setDatos(parametro);
        return dr;
    }
}
