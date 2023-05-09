package com.imss.sivimss.vehiculos.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.imss.sivimss.vehiculos.beans.MttoVehicular;
import com.imss.sivimss.vehiculos.beans.MttoVerifiInicio;
import com.imss.sivimss.vehiculos.beans.Registro;
import com.imss.sivimss.vehiculos.beans.Solicitud;
import com.imss.sivimss.vehiculos.exception.BadRequestException;
import com.imss.sivimss.vehiculos.model.request.MttoVehicularRequest;
import com.imss.sivimss.vehiculos.model.request.UsuarioDto;
import com.imss.sivimss.vehiculos.service.MttoVehicularService;
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
import java.util.Map;

@Service
public class MttoVehicularServiceImpl implements MttoVehicularService {
    private static Logger log = LogManager.getLogger(MttoVehicularServiceImpl.class);

    @Value("${endpoints.dominio-consulta}")
    private String urlDominioConsulta;

    private static final String PATH_CONSULTA="/generico/consulta";
    
    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    private MttoVehicular mttoVehicular=new MttoVehicular();

    private MttoVerifiInicio verifiInicio=new MttoVerifiInicio();

    private Solicitud solicitud=new Solicitud();

    private Registro registro=new Registro();

    @Override
    public Response<?> insertarMttoVehicular(DatosRequest request, Authentication authentication) throws IOException {
        String path=urlDominioConsulta + "/generico/crear";
        Gson json = new Gson();
        MttoVehicularRequest requestDto = json.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)),MttoVehicularRequest.class);
        UsuarioDto usuarioDto = json.fromJson(authentication.getPrincipal().toString(), UsuarioDto.class);
        Response<?> response = llamarServicio(mttoVehicular.insertar(requestDto, usuarioDto).getDatos(), path, authentication);
        log.info(response.getCodigo());
        if (response.getCodigo() == 200) {
            log.info("Registro exitoso");

            if(requestDto.getVerificacionInicio()!=null){
                requestDto.getVerificacionInicio().setIdMttoVehicular(Integer.parseInt(response.getDatos().toString()));
                llamarServicio(verifiInicio.insertar(requestDto, usuarioDto).getDatos(), path, authentication);
            }
            if(requestDto.getSolicitud()!=null){
                requestDto.getSolicitud().setIdMttoVehicular(Integer.parseInt(response.getDatos().toString()));
                llamarServicio(solicitud.insertar(requestDto, usuarioDto).getDatos(), path, authentication);
            }
            if(requestDto.getRegistro()!=null){
                requestDto.getRegistro().setIdMttoVehicular(Integer.parseInt(response.getDatos().toString()));
                llamarServicio(registro.insertar(requestDto, usuarioDto).getDatos(), path, authentication);
            }
            return response;
        } else {
            throw new BadRequestException(HttpStatus.valueOf(response.getCodigo()), "Error al insertar registro");
        }
    }

    @Override
    public Response<?> modificarMttoVehicular(DatosRequest request, Authentication authentication) throws IOException {
        String path=urlDominioConsulta + "/generico/actualizar";
        Gson json = new Gson();
        MttoVehicularRequest requestDto = json.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)),MttoVehicularRequest.class);
        UsuarioDto usuarioDto = null;
        Response<?> response = llamarServicio(mttoVehicular.modificar(requestDto, usuarioDto).getDatos(), path, authentication);
        if (response.getCodigo() == 200) {
            log.info("Modificacion exitoso");
            if(requestDto.getVerificacionInicio()!=null) {
                llamarServicio(verifiInicio.modificar(requestDto, usuarioDto).getDatos(),path,authentication);
            }
            if(requestDto.getSolicitud()!=null){
                llamarServicio(solicitud.modificar(requestDto, usuarioDto).getDatos(),path,authentication);
            }
            if(requestDto.getRegistro()!=null){
                llamarServicio(registro.modificar(requestDto, usuarioDto).getDatos(), path, authentication);
            }
            return response;
        } else {
            throw new BadRequestException(HttpStatus.valueOf(response.getCodigo()), "Error al actualizar registro");
        }
    }

    @Override
    public Response<?> modificarEstatusMttoVehicular(DatosRequest request, Authentication authentication) throws IOException {
        String path=urlDominioConsulta + "/generico/actualizar";
        Gson json = new Gson();
        MttoVehicularRequest requestDto = json.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)),MttoVehicularRequest.class);
        UsuarioDto usuarioDto=null;
        log.info("Nuevo estatus {}", requestDto.getIdEstatus());
        Response<?> response = llamarServicio(mttoVehicular.cambiarEstatus(requestDto.getIdMttoVehicular(), requestDto.getIdEstatus(), usuarioDto).getDatos(), path, authentication);
        if (response.getCodigo() == 200) {
            log.info("Cambio de estatus exitoso");
            if(requestDto.getVerificacionInicio()!=null) {
                llamarServicio(verifiInicio.cambiarEstatus(requestDto.getVerificacionInicio().getIdMttoVerifInicio(), requestDto.getIdEstatus(), usuarioDto).getDatos(),path,authentication);
            }
            if(requestDto.getSolicitud()!=null){
                llamarServicio(solicitud.cambiarEstatus(requestDto.getSolicitud().getIdMttoSolicitud(), requestDto.getIdEstatus(),usuarioDto).getDatos(),path, authentication);
            }
            if(requestDto.getRegistro()!=null){
                llamarServicio(registro.cambiarEstatus(requestDto.getRegistro().getIdMttoRegistro(), requestDto.getIdEstatus(), usuarioDto).getDatos(), path, authentication);
            }
            return response;
        } else {
            throw new BadRequestException(HttpStatus.valueOf(response.getCodigo()), "Error al actualizar registro");
        }
    }

    private Response<?> llamarServicio(Map<String, Object> dato, String url, Authentication authentication) throws IOException{
        Response<?> response = providerRestTemplate.consumirServicio(dato,url,authentication);
        return response;
    }

	@Override
	public Response<?> detalleVerifInicio(DatosRequest request, Authentication authentication) throws IOException {
		  log.info("Obtiene detalle de la verificacion al inicio de la jornada");
	        return providerRestTemplate.consumirServicio(verifiInicio.detalleVerificacion(request).getDatos(), urlDominioConsulta + PATH_CONSULTA,
	                authentication);
	}

	@Override
	public Response<?> detalleSolicitudMtto(DatosRequest request, Authentication authentication) throws IOException {
		  log.info("Obtiene detalle de la solicitud de mantenimiento");
	        return providerRestTemplate.consumirServicio(solicitud.detalleSolicitud(request).getDatos(), urlDominioConsulta + PATH_CONSULTA,
	                authentication);
	}

	@Override
	public Response<?> detalleRegistroMtto(DatosRequest request, Authentication authentication) throws IOException {
		log.info("Obtiene detalle del registro de mantenimiento");
        return providerRestTemplate.consumirServicio(registro.detalleRegistro(request).getDatos(), urlDominioConsulta + PATH_CONSULTA,
                authentication);
	}
}
