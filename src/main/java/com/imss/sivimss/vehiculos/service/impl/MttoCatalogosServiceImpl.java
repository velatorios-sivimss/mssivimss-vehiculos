package com.imss.sivimss.vehiculos.service.impl;

import com.imss.sivimss.vehiculos.beans.MttoCatalogos;
import com.imss.sivimss.vehiculos.service.MttoCatalogosService;
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
public class MttoCatalogosServiceImpl implements MttoCatalogosService {
    private static Logger log = LogManager.getLogger(MttoVehicularServiceImpl.class);

    @Value("${endpoints.dominio}")
    private String urlDominioConsulta;

    private static final String PATH_BUSQUEDA="/consulta";

    private MttoCatalogos mttoCatalogos=new MttoCatalogos();

    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;

    @Override
    public Response<?> getCatMttoUsoVehiculo(Authentication authentication) throws IOException {
        log.info("Obtiene lista  de uso vehicular");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatMttoUsoVehiculo().getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getCatMttoModalidad(Authentication authentication) throws IOException {
        log.info("Obtiene lista de modalidad de mantenimiento");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatMttoModalidad().getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getCatNivelOficina(Authentication authentication) throws IOException {
        log.info("Obtiene lista de nivel de oficina");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatNivelOficina().getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getCatMttoTipo(Authentication authentication) throws IOException {
        log.info("Obtiene lista de tipo de mtto");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatMttoTipo().getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getCatMttoEstado(Authentication authentication) throws IOException {
        log.info("Obtiene lista de estado de mtto");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatMttoEstado().getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getCatProveedores(Authentication authentication) throws IOException {
        log.info("Obtiene lista de proveedores");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatProveedores().getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getCatMttoNivel(Authentication authentication) throws IOException {
        log.info("Obtiene lista de niveles");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatMttoNivel().getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getCatMttoReporteTipo(Authentication authentication) throws IOException {
        log.info("Obtiene lista tipo de reporte");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatMttoReporteTipo().getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getCatMttoPeriodo(Authentication authentication) throws IOException {
        log.info("Obtiene lista periodos");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatMttoPeriodo().getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getCatPlacasVehiculos(DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene lista de placas");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatPlacasVehiculos(request, authentication).getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getCatContratosProveedores(DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene lista de contrator de proveedores");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getCatContratosProveedores(request, authentication).getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getRegistroMtto(DatosRequest request, Authentication authentication) throws IOException {
        log.info("Obtiene los ids del registro de mtto");
        return providerRestTemplate.consumirServicio(mttoCatalogos.getRegistroMtto(request, authentication).getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getMttoTipoModalidad(DatosRequest request, Authentication authentication) throws IOException {
        return providerRestTemplate.consumirServicio(mttoCatalogos.getMttoTipoModalidad(request, authentication).getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

    @Override
    public Response<?> getMttoTipoModalidadDetalle(DatosRequest request, Authentication authentication) throws IOException {
        return providerRestTemplate.consumirServicio(mttoCatalogos.getMttoTipoModalidadDetalle(request, authentication).getDatos(), urlDominioConsulta + PATH_BUSQUEDA,
                authentication);
    }

}
