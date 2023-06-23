package com.imss.sivimss.vehiculos.service.impl;

import com.google.gson.Gson;
import com.imss.sivimss.vehiculos.beans.MttoReporte;
import com.imss.sivimss.vehiculos.beans.Vehiculos;
import com.imss.sivimss.vehiculos.exception.BadRequestException;
import com.imss.sivimss.vehiculos.model.request.ReporteDto;
import com.imss.sivimss.vehiculos.model.request.UsuarioDto;
import com.imss.sivimss.vehiculos.service.BuscarVehiculosService;
import com.imss.sivimss.vehiculos.util.AppConstantes;
import com.imss.sivimss.vehiculos.util.DatosRequest;
import com.imss.sivimss.vehiculos.util.ProviderServiceRestTemplate;
import com.imss.sivimss.vehiculos.util.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.logging.Level;

@Service
public class BuscarVehiculosServiceImpl implements BuscarVehiculosService {
    private static Logger log = LogManager.getLogger(BuscarVehiculosServiceImpl.class);

    @Value("${endpoints.dominio}")
    private String urlDominioConsulta;
    
    @Value("${url-reportes.ms-reportes}")
    private String urlReportes;

    private static final String PATH_BUSQUEDA_PAG="/paginado";

    private Vehiculos vehiculos=new Vehiculos();

    private MttoReporte mttoReporte=new MttoReporte();

    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;

    @Override
    public Response<?> getVehiculos(DatosRequest request, Authentication authentication) throws IOException, ParseException {
        log.info("Obtiene lista de vehiculos");
        String path=urlDominioConsulta + "/consulta";
        Integer pagina = Integer.valueOf(Integer.parseInt(request.getDatos().get("pagina").toString()));
        Integer tamanio = Integer.valueOf(Integer.parseInt(request.getDatos().get("tamanio").toString()));
        if(pagina!=null && pagina>0 && tamanio!=null && tamanio>0) {
            path=urlDominioConsulta + PATH_BUSQUEDA_PAG;
        }
        return providerRestTemplate.consumirServicio(vehiculos.buscarVehiculos(request, authentication).getDatos(), path, authentication);
    }

    @Override
    public Response<?> getReporteEncargado(DatosRequest request, Authentication authentication) throws IOException, ParseException {
        log.info("Obtiene reporte encargado");
        return providerRestTemplate.consumirServicio(mttoReporte.reporteEncargado(request, authentication).getDatos(), urlDominioConsulta + PATH_BUSQUEDA_PAG,
                authentication);
    }

    @Override
    public Response<?> getReportePredictivo(DatosRequest request, Authentication authentication) throws IOException, ParseException {
        log.info("Obtiene reporte predictivo");
        return providerRestTemplate.consumirServicio(mttoReporte.reportePredictivo(request, authentication).getDatos(), urlDominioConsulta + PATH_BUSQUEDA_PAG,
                authentication);
    }

	@Override
	public Response<?> reporteProgramarMttoVehicular(DatosRequest request, Authentication authentication)
			throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ReporteDto reporte= gson.fromJson(datosJson, ReporteDto.class);
		Map<String, Object> envioDatos = new MttoReporte().reporteProgramarMttoVehicular(reporte);
		return providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes,
				authentication);
	}

	@Override
	public Response<?> reporteEncargado(DatosRequest request, Authentication authentication) throws IOException, ParseException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ReporteDto reporte= gson.fromJson(datosJson, ReporteDto.class);
		 if(reporte.getIdVelatorio()==null || reporte.getIdDelegacion()==null) {
	        	UsuarioDto usuarioDto = gson.fromJson(authentication.getPrincipal().toString(), UsuarioDto.class);
	            reporte.setIdDelegacion(usuarioDto.getIdDelegacion());
	            reporte.setIdVelatorio(usuarioDto.getIdVelatorio());
	        }
		Map<String, Object> envioDatos = new MttoReporte().reporteEncargado(reporte);
		return providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes,
				authentication);
	}
}
