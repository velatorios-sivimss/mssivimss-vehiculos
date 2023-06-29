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
import com.imss.sivimss.vehiculos.service.EstatusMttoService;
import com.imss.sivimss.vehiculos.service.MttoVehicularService;
import com.imss.sivimss.vehiculos.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MttoVehicularServiceImpl implements MttoVehicularService {
    private static Logger log = LogManager.getLogger(MttoVehicularServiceImpl.class);

    @Value("${endpoints.dominio}")
    private String urlDominioConsulta;

    @Autowired
    private EstatusMttoService estatusMttoService;

    private static final String PATH_CONSULTA="/consulta";
    
    @Autowired
    private ProviderServiceRestTemplate providerRestTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    private MttoVehicular mttoVehicular=new MttoVehicular();

    private MttoVerifiInicio verifiInicio=new MttoVerifiInicio();

    private Solicitud solicitud=new Solicitud();

    private Registro registro=new Registro();

    SimpleDateFormat formatoConsulta = new SimpleDateFormat("yyyy-MM-dd");

    private Response<?> validarSolicitud(MttoVehicularRequest requestDto, Authentication authentication) throws IOException{
        String existe=null;
        String mensaje="";
        Response<?> existeMtto=null;
        List<Map<String, Object>> resultExiste=null;
        try {
            existeMtto = llamarServicio(solicitud.validarRN136(requestDto).getDatos(), urlDominioConsulta + PATH_CONSULTA, authentication);
            resultExiste= (List<Map<String, Object>>) existeMtto.getDatos();
        } catch (Exception ex){
            existe=null;
            log.info("Error al consulta si existe mtto del vehiculo");
        }
        if(resultExiste!=null && !resultExiste.isEmpty()) {
            for (Map<String, Object> map : resultExiste) {
                existe=(String) map.get("validacion");
            }
        }
        if(existe==null || existe.equals("DISPONIBLE")) {
            log.info("vehiculo disponible");
        } else if(existe!=null && existe.equals("DISPONIBLE")){
            log.info("vehiculo disponible");
        } else {
            if(requestDto.getSolicitud()!=null) {
                if (ValidacionRequestUtil.validarInt(requestDto.getSolicitud().getIdMttoTipo()) && requestDto.getSolicitud().getIdMttoTipo().equals(2)) {
                    //correctivo
                    if (ValidacionRequestUtil.validarInt(requestDto.getSolicitud().getIdMttoModalidad()) && requestDto.getSolicitud().getIdMttoModalidad().equals(1)) {
                        //semetral
                        mensaje = "Mantenimientos ya registrados para este vehículo";
                    } else {
                        //anual o frecuente
                        mensaje = "Mantenimientos ya registrados para este vehículo";
                    }
                } else if (ValidacionRequestUtil.validarInt(requestDto.getSolicitud().getIdMttoTipo()) && requestDto.getSolicitud().getIdMttoTipo().equals(1)) {
                    //preventivo
                    if (ValidacionRequestUtil.validarInt(requestDto.getSolicitud().getIdMttoModalidad()) && requestDto.getSolicitud().getIdMttoModalidad().equals(3)) {
                        //frecuente
                        mensaje = "Se debe efectuar primero el mantenimiento de Afinación y Cambio de Aceite";
                    } else {
                        mensaje = "Se debe efectuar primero el mantenimiento de Afinación y Cambio de Aceite";
                    }
                }
                return Response.builder().error(true).mensaje(mensaje).codigo(200).datos(null).build();
            }

        }
        return Response.builder().error(false).mensaje(null).codigo(200).datos(null).build();
    }

    @Override
    public Response<?> insertarMttoVehicular(DatosRequest request, Authentication authentication) throws IOException, ParseException {
        String path=urlDominioConsulta + "/crear";
        Gson json = new Gson();
        MttoVehicularRequest requestDto = json.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)),MttoVehicularRequest.class);
        UsuarioDto usuarioDto = json.fromJson(authentication.getPrincipal().toString(), UsuarioDto.class);
        Response<?> validacionMtto=this.validarSolicitud(requestDto,authentication);
        if(validacionMtto!=null && validacionMtto.getCodigo()==200 && validacionMtto.getMensaje()!=null){
            //enviamos el error
            return validacionMtto;
        }
        Integer idMtto=null;
        Date fechaRegistro=null;
        Response<?> existeMtto=null;
        if(requestDto!=null && requestDto.getIdMttoVehicular()!=null){
            idMtto=requestDto.getIdMttoVehicular();
            existeMtto = llamarServicio(mttoVehicular.existeFechaRegistro(requestDto).getDatos(), urlDominioConsulta + PATH_CONSULTA, authentication);
            List<Map<String, Object>> resultExiste= (List<Map<String, Object>>) existeMtto.getDatos();
            for (Map<String, Object> map : resultExiste) {
                fechaRegistro=formatoConsulta.parse((String) map.get("FEC_REGISTRO"));
            }
        }
        if(idMtto==null) {
            Response<?> response = llamarServicio(mttoVehicular.insertar(requestDto, usuarioDto).getDatos(), path, authentication);
            log.info(response.getCodigo());
            if (response.getCodigo() == 200) {
                log.info("Registro exitoso");
                if (requestDto.getVerificacionInicio() != null) {
                    requestDto.getVerificacionInicio().setIdMttoVehicular(Integer.parseInt(response.getDatos().toString()));
                    llamarServicio(verifiInicio.insertar(requestDto, usuarioDto).getDatos(), path, authentication);
                }
                if (requestDto.getSolicitud() != null) {
                    requestDto.getSolicitud().setIdMttoVehicular(Integer.parseInt(response.getDatos().toString()));
                    llamarServicio(solicitud.insertar(requestDto, usuarioDto).getDatos(), path, authentication);
                    this.validaFechas(fechaRegistro,requestDto.getSolicitud().getIdMttoVehicular(),requestDto.getSolicitud().getFecRegistro(),authentication);
                }
                if (requestDto.getRegistro() != null) {
                    requestDto.getRegistro().setIdMttoVehicular(Integer.parseInt(response.getDatos().toString()));
                    llamarServicio(registro.insertar(requestDto, usuarioDto).getDatos(), path, authentication);
                    this.validaFechas(fechaRegistro,requestDto.getRegistro().getIdMttoVehicular(),requestDto.getRegistro().getFecRegistro(),authentication);
                }
                return response;
            } else {
                throw new BadRequestException(HttpStatus.valueOf(response.getCodigo()), "Error al insertar registro");
            }
        } else {
            log.info("Ya existe el mtto");
            if (requestDto.getVerificacionInicio() != null) {
                requestDto.getVerificacionInicio().setIdMttoVehicular(idMtto);
                Response<?> existeMttoVI = llamarServicio(verifiInicio.existe(requestDto).getDatos(), urlDominioConsulta + PATH_CONSULTA, authentication);
                List<Map<String, Object>> resultExisteVI= (List<Map<String, Object>>) existeMttoVI.getDatos();
                Integer idMttoVI=null;
                for (Map<String, Object> map : resultExisteVI) {
                    idMttoVI=(Integer) map.get("ID_MTTOVERIFINICIO");
                }
                if(idMttoVI==null) {
                    llamarServicio(verifiInicio.insertar(requestDto, usuarioDto).getDatos(), path, authentication);
                } else {
                    requestDto.getVerificacionInicio().setIdMttoVerifInicio(idMttoVI);
                    llamarServicio(verifiInicio.modificar(requestDto, usuarioDto).getDatos(),path,authentication);
                }
            }
            if (requestDto.getSolicitud() != null) {
                requestDto.getSolicitud().setIdMttoVehicular(idMtto);
                Response<?> existeMttoSol = llamarServicio(solicitud.existe(requestDto).getDatos(), urlDominioConsulta + PATH_CONSULTA, authentication);
                List<Map<String, Object>> resultExisteSol= (List<Map<String, Object>>) existeMttoSol.getDatos();
                Integer idMttoSol=null;
                for (Map<String, Object> map : resultExisteSol) {
                    idMttoSol=(Integer) map.get("ID_MTTO_SOLICITUD");
                }
                if(idMttoSol==null) {
                    llamarServicio(solicitud.insertar(requestDto, usuarioDto).getDatos(), path, authentication);
                }else {
                    requestDto.getSolicitud().setIdMttoSolicitud(idMttoSol);
                    llamarServicio(solicitud.modificar(requestDto, usuarioDto).getDatos(),path,authentication);
                }
                this.validaFechas(fechaRegistro,requestDto.getSolicitud().getIdMttoVehicular(),requestDto.getSolicitud().getFecRegistro(),authentication);
            }
            if (requestDto.getRegistro() != null) {
                requestDto.getRegistro().setIdMttoVehicular(idMtto);
                Response<?> existeMttoReg = llamarServicio(registro.existe(requestDto).getDatos(), urlDominioConsulta + PATH_CONSULTA, authentication);
                List<Map<String, Object>> resultExisteReg= (List<Map<String, Object>>) existeMttoReg.getDatos();
                Integer idMttoReg=null;
                for (Map<String, Object> map : resultExisteReg) {
                    idMttoReg=(Integer) map.get("ID_MTTO_REGISTRO");
                }
                if(idMttoReg==null) {
                    llamarServicio(registro.insertar(requestDto, usuarioDto).getDatos(), path, authentication);
                } else {
                    requestDto.getRegistro().setIdMttoRegistro(idMttoReg);
                    llamarServicio(registro.modificar(requestDto, usuarioDto).getDatos(), path, authentication);
                }
                this.validaFechas(fechaRegistro,requestDto.getRegistro().getIdMttoVehicular(),requestDto.getRegistro().getFecRegistro(),authentication);
            }
            return existeMtto;
        }
    }

    @Override
    public Response<?> modificarMttoVehicular(DatosRequest request, Authentication authentication) throws IOException {
        String path=urlDominioConsulta + "/actualizar";
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
        String path=urlDominioConsulta + "/actualizar";
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

    private void validarEstatus(Date fechaRegistro, Integer idMttoVehicular, Date fechaMantenimiento, Authentication authentication) throws IOException {
        estatusMttoService.validarEstatusbyIdMtto(fechaRegistro, idMttoVehicular, fechaMantenimiento, authentication);
    }

    private void validaFechas(Date fechaRegistro, Integer idMttoVehicular, String fechaMantenimiento, Authentication authentication) throws ParseException, IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaMtto = simpleDateFormat.parse(fechaMantenimiento);
        if(fechaRegistro==null){
            fechaRegistro=new Date();
        }
        this.validarEstatus(fechaRegistro,idMttoVehicular,fechaMtto,authentication);
    }
}
