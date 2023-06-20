package com.imss.sivimss.vehiculos.service.impl;

import com.imss.sivimss.vehiculos.beans.MttoVehicular;
import com.imss.sivimss.vehiculos.service.EstatusMttoService;
import com.imss.sivimss.vehiculos.util.ProviderServiceRestTemplate;
import com.imss.sivimss.vehiculos.util.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    SimpleDateFormat formatoConsulta = new SimpleDateFormat("yyyy-MM-dd");

    public void validarEstatusbyIdMtto(Date fechaRegistro, Integer idMttoVehicular, Date fechaMantenimiento, Authentication authentication) throws IOException {
        String path=urlDominioConsulta + "/actualizar";
        Long dias = ChronoUnit.DAYS.between(fechaRegistro.toInstant(), fechaMantenimiento.toInstant());
        log.info("Total de dias {}",dias);
        if(dias.intValue()< 0) {
            llamarServicio(mttoVehicular.modificarEstatusMtto(idMttoVehicular,3).getDatos(), path, authentication);
        } else if(dias.intValue()==0){
            llamarServicio(mttoVehicular.modificarEstatusMtto(idMttoVehicular,2).getDatos(), path, authentication);
        } else if(dias.intValue()<=15){
            llamarServicio(mttoVehicular.modificarEstatusMtto(idMttoVehicular,4).getDatos(), path, authentication);
        } else if(dias.intValue()>15){
            llamarServicio(mttoVehicular.modificarEstatusMtto(idMttoVehicular,1).getDatos(), path, authentication);
        } else {
            log.info("No se identifica el numero de dias");
        }
    }

    @Override
    public void validarEstatusMtto() throws IOException, ParseException {
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("key", "anon", AuthorityUtils.createAuthorityList("ANON")));
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String path=urlDominioConsulta + "/consulta";
        Response<?> listaMttos = llamarServicio(mttoVehicular.validaEstatusMtto().getDatos(), path, authentication);
        this.validarFechas((List<Map<String, Object>>) listaMttos.getDatos(), authentication);
    }

    private Response<?> llamarServicio(Map<String, Object> dato, String url, Authentication authentication) throws IOException {
        Response<?> response = providerRestTemplate.consumirServicio(dato,url,authentication);
        return response;
    }

    private void validarFechas(List<Map<String, Object>> result, Authentication authentication) throws IOException, ParseException {
        String path=urlDominioConsulta + "/actualizar";
        String nevaFecha=formatoConsulta.format(new Date());
        Date fechaRegistro=formatoConsulta.parse(nevaFecha);
        Date fechaSol=null;
        Date fechaReg=null;
        for (Map<String, Object> map : result) {
            Integer idMtto=(Integer) map.get("ID_MTTOVEHICULAR");
            try {
                fechaSol = formatoConsulta.parse((String) map.get("FEC_SOLICTUD"));
                fechaReg = formatoConsulta.parse((String) map.get("FEC_REGISTRO_REG"));
            } catch (Exception e){
                fechaSol=null;
                fechaReg=null;
                log.error("Error al convertir fecha {}", e);
            }
            if(fechaSol!=null) {
                this.validarEstatusbyIdMtto(fechaRegistro, idMtto, fechaSol, authentication);
            }
            if(fechaReg!=null) {
                this.validarEstatusbyIdMtto(fechaRegistro, idMtto, fechaReg, authentication);
            }
        }
    }
}
