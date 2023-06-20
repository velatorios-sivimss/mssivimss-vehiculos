package com.imss.sivimss.vehiculos.service.impl;

import com.imss.sivimss.vehiculos.beans.MttoVehicular;
import com.imss.sivimss.vehiculos.service.EstatusMttoService;
import com.imss.sivimss.vehiculos.util.ProviderServiceRestTemplate;
import com.imss.sivimss.vehiculos.util.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EstatusMttoServiceImpl implements EstatusMttoService {
    private static Logger log = LogManager.getLogger(EstatusMttoServiceImpl.class);

    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;

    @Value("${endpoints.dominio}")
    private String urlDominioConsulta;

    private MttoVehicular mttoVehicular=new MttoVehicular();

    @Override
    public void validarEstatusMtto(Authentication authentication, Date fechaMtto) throws IOException{
        String path=urlDominioConsulta + "/consulta";
        Response<?> listaMttos = llamarServicio(mttoVehicular.validaEstatusMtto().getDatos(), path, authentication);
        this.validarFechas((List<Map<String, Object>>) listaMttos.getDatos(), fechaMtto, authentication);
    }

    private Response<?> llamarServicio(Map<String, Object> dato, String url, Authentication authentication) throws IOException {
        Response<?> response = providerRestTemplate.consumirServicio(dato,url,authentication);
        return response;
    }

    private void validarFechas(List<Map<String, Object>> result, Date fechaMtto, Authentication authentication) throws IOException{
        String path=urlDominioConsulta + "/actualizar";
        for (Map<String, Object> map : result) {
            Integer idMtto=(Integer) map.get("ID_MTTOVEHICULAR");
            Date fechaRegistro=(Date) map.get("FEC_REGISTRO");
            int comparacion = fechaRegistro.compareTo(fechaMtto);
            log.info("Comparación de fecha: {}", comparacion);
            if(comparacion==0){
                llamarServicio(mttoVehicular.modificarEstatusMtto(idMtto,2).getDatos(), path, authentication);
            } else if(comparacion>0){
                llamarServicio(mttoVehicular.modificarEstatusMtto(idMtto,3).getDatos(), path, authentication);
            } else if(comparacion<0){
                //validamos los numeros de dias
                Long dias = ChronoUnit.DAYS.between(fechaRegistro.toInstant(), fechaMtto.toInstant());
                if(dias.intValue()<=15){
                    llamarServicio(mttoVehicular.modificarEstatusMtto(idMtto,4).getDatos(), path, authentication);
                } else if(dias.intValue()>15){
                    llamarServicio(mttoVehicular.modificarEstatusMtto(idMtto,1).getDatos(), path, authentication);
                } else {
                    log.info("No se identifica el numero de dias");
                }
            } else {
                log.info("No se identifica la fecha");
            }
        }
    }
}
