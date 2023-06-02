package com.imss.sivimss.vehiculos.service.impl;

import com.imss.sivimss.vehiculos.beans.Vehiculos;
import com.imss.sivimss.vehiculos.service.BuscarVehiculosService;
import com.imss.sivimss.vehiculos.util.DatosRequest;
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
public class BuscarVehiculosServiceImpl implements BuscarVehiculosService {
    private static Logger log = LogManager.getLogger(BuscarVehiculosServiceImpl.class);

    @Value("${endpoints.dominio}")
    private String urlDominioConsulta;

    private static final String PATH_BUSQUEDA_PAG="/generico/paginado";

    private Vehiculos vehiculos=new Vehiculos();

    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;

    @Override
    public Response<?> getVehiculos(DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene lista de vehiculos");
        return providerRestTemplate.consumirServicio(vehiculos.buscarVehiculos(request).getDatos(), urlDominioConsulta + PATH_BUSQUEDA_PAG,
                authentication);
    }
}
