package com.imss.sivimss.vehiculos.service.impl;

import com.imss.sivimss.vehiculos.beans.MttoCatalogos;
import com.imss.sivimss.vehiculos.service.MttoCatalogosService;
import com.imss.sivimss.vehiculos.util.ProviderServiceRestTemplate;
import com.imss.sivimss.vehiculos.util.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MttoCatalogosServiceImpl implements MttoCatalogosService {
    private static Logger log = LogManager.getLogger(MttoVehicularServiceImpl.class);

    @Value("${endpoints.dominio-consulta}")
    private String urlDominioConsulta;

    private static final String PATH_BUSQUEDA_PAG="/generico/paginado";

    private MttoCatalogos mttoCatalogos=new MttoCatalogos();

    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;

    @Override
    public Response<?> getCatMttoUsoVehiculo(Authentication authentication) throws IOException {
        log.info("Obtiene lista  de uso vehicular");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatMttoUsoVehiculo().getDatos(), urlDominioConsulta + PATH_BUSQUEDA_PAG,
                authentication);
    }

    @Override
    public Response<?> getCatMttoModalidad(Authentication authentication) throws IOException {
        log.info("Obtiene lista de modalidad de mantenimiento");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatMttoModalidad().getDatos(), urlDominioConsulta + PATH_BUSQUEDA_PAG,
                authentication);
    }

    @Override
    public Response<?> getCatNivelOficina(Authentication authentication) throws IOException {
        log.info("Obtiene lista de nivel de oficina");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatNivelOficina().getDatos(), urlDominioConsulta + PATH_BUSQUEDA_PAG,
                authentication);
    }

    @Override
    public Response<?> getCatMttoTipo(Authentication authentication) throws IOException {
        log.info("Obtiene lista de tipo de mtto");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatMttoTipo().getDatos(), urlDominioConsulta + PATH_BUSQUEDA_PAG,
                authentication);
    }

    @Override
    public Response<?> getCatMttoEstado(Authentication authentication) throws IOException {
        log.info("Obtiene lista de estado de mtto");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatMttoEstado().getDatos(), urlDominioConsulta + PATH_BUSQUEDA_PAG,
                authentication);
    }

    @Override
    public Response<?> getCatProveedores(Authentication authentication) throws IOException {
        log.info("Obtiene lista de proveedores");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatProveedores().getDatos(), urlDominioConsulta + PATH_BUSQUEDA_PAG,
                authentication);
    }

    @Override
    public Response<?> getCatMttoNivel(Authentication authentication) throws IOException {
        log.info("Obtiene lista de niveles");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatMttoNivel().getDatos(), urlDominioConsulta + PATH_BUSQUEDA_PAG,
                authentication);
    }
}
