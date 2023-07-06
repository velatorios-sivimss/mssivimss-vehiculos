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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Response<?> validarSolicitud(MttoVehicularRequest request, Authentication authentication) throws IOException {
        Integer semetral=2;
        Integer anual=1;
        Response<?> responseMtto=null;
        List<Map<String, Object>> resultExiste=null;
        //validamos si es verificaciom vehicular
        Integer total=0;
        if(validarVerificacionSolicitud(request,authentication)){
            total=0;
            responseMtto = llamarServicio(solicitud.existeVerificacionVehicular(request).getDatos(), urlDominioConsulta + PATH_CONSULTA, authentication);
            resultExiste= (List<Map<String, Object>>) responseMtto.getDatos();
            for (Map<String, Object> map : resultExiste) {
                total = (Integer) map.get("SOLICITUDES");
            }
            if(total==0){
                return Response.builder().error(true).mensaje("Se debe efectuar primero el mantenimiento de Afinación y Cambio de Aceite").codigo(200).datos(null).build();
            }
        } else {
            //validamos si existe el mtto
            total=0;
            responseMtto = llamarServicio(solicitud.existeSolicitud(request).getDatos(), urlDominioConsulta + PATH_CONSULTA, authentication);
            resultExiste= (List<Map<String, Object>>) responseMtto.getDatos();
            for (Map<String, Object> map : resultExiste) {
                total = (Integer) map.get("SOLICITUDES");
            }
            //semestral 2 veces
            if(request.getSolicitud().getIdMttoModalidad()!= null && request.getSolicitud().getIdMttoModalidad()==1){
                if(semetral==total){
                    return Response.builder().error(true).mensaje("Mantenimientos ya registrados para este vehículo").codigo(200).datos(null).build();
                }
            }

            //anaul 1 vez
            if(request.getSolicitud().getIdMttoModalidad()!= null && request.getSolicitud().getIdMttoModalidad()==2) {
                if (anual==total) {
                    return Response.builder().error(true).mensaje("Mantenimientos ya registrados para este vehículo").codigo(200).datos(null).build();
                }
            }
        }
        return responseMtto;
    }

    public Response<?> validarRegistro(MttoVehicularRequest request, Authentication authentication) throws IOException {
        Integer semetral=2;
        Integer anual=1;
        Response<?> responseMtto=null;
        List<Map<String, Object>> resultExiste=null;
        //validamos si es verificaciom vehicular
        Integer total=0;
        if(validarVerificacionRegistro(request,authentication)){
            total=0;
            responseMtto = llamarServicio(registro.existeVerificacionVehicular(request).getDatos(), urlDominioConsulta + PATH_CONSULTA, authentication);
            resultExiste= (List<Map<String, Object>>) responseMtto.getDatos();
            for (Map<String, Object> map : resultExiste) {
                total = (Integer) map.get("SOLICITUDES");
            }
            if(total==0){
                return Response.builder().error(true).mensaje("Se debe efectuar primero el mantenimiento de Afinación y Cambio de Aceite").codigo(200).datos(null).build();
            }
        } else {
            //validamos si existe el mtto
            total=0;
            responseMtto = llamarServicio(registro.existeSolicitud(request).getDatos(), urlDominioConsulta + PATH_CONSULTA, authentication);
            resultExiste= (List<Map<String, Object>>) responseMtto.getDatos();
            for (Map<String, Object> map : resultExiste) {
                total = (Integer) map.get("SOLICITUDES");
            }
            //semestral 2 veces
            if(request.getRegistro().getIdMttoModalidad()!=null && request.getRegistro().getIdMttoModalidad()==1){
                if(total >= semetral){
                    return Response.builder().error(true).mensaje("Mantenimientos ya registrados para este vehículo").codigo(200).datos(null).build();
                }
            }

            //anaul 1 vez
            if(request.getRegistro().getIdMttoModalidad()!=null && request.getRegistro().getIdMttoModalidad()==2) {
                if (total >= anual) {
                    return Response.builder().error(true).mensaje("Mantenimientos ya registrados para este vehículo").codigo(200).datos(null).build();
                }
            }
        }
        return responseMtto;
    }



    private boolean validarVerificacionSolicitud(MttoVehicularRequest request, Authentication authentication) throws IOException{
        boolean esVerificacion=false;
        if(request.getSolicitud()!=null && request.getSolicitud().getIdMttoModalidad()==1){
            if(ValidacionRequestUtil.validarInt(request.getSolicitud().getIdMttoTipo()) && request.getSolicitud().getIdMttoTipo()==1){
                if(ValidacionRequestUtil.validarInt(request.getSolicitud().getIdMttoTipoModalidad()) && request.getSolicitud().getIdMttoTipoModalidad()==4){
                    if(ValidacionRequestUtil.validarInt(request.getSolicitud().getIdMttoTipoModalidadDet()) && request.getSolicitud().getIdMttoTipoModalidadDet()==20){
                        esVerificacion=true;
                    }
                }
            }
        }
        return esVerificacion;
    }

    private boolean validarVerificacionRegistro(MttoVehicularRequest request, Authentication authentication) throws IOException{
        boolean esVerificacion=false;
        if(request.getRegistro()!=null 
        		&& request.getRegistro().getIdMttoModalidad()!=null 
        		&& request.getRegistro().getIdMttoModalidad()==1){
            if(ValidacionRequestUtil.validarInt(request.getRegistro().getIdMantenimiento()) 
            		&& request.getRegistro().getIdMantenimiento()!=null
            		&& request.getRegistro().getIdMantenimiento()==1){
                if(ValidacionRequestUtil.validarInt(
                		request.getRegistro().getIdMttoTipoModalidad()) 
                		&& request.getRegistro().getIdMttoTipoModalidad()!=null
                		&& request.getRegistro().getIdMttoTipoModalidad()==4){
                    if(ValidacionRequestUtil.validarInt(
                    		request.getRegistro().getIdMttoTipoModalidadDet()) 
                    		&& request.getRegistro().getIdMttoTipoModalidadDet()!=null
                    		&& request.getRegistro().getIdMttoTipoModalidadDet()==20){
                        esVerificacion=true;
                    }
                }
            }
        }
        return esVerificacion;
    }

    @Override
    public Response<?> insertarMttoVehicular(DatosRequest request, Authentication authentication) throws IOException, ParseException {
        String path=urlDominioConsulta + "/crear";
        String pathMultiple=urlDominioConsulta + "/insertarMultiple";
        Gson json = new Gson();
        MttoVehicularRequest requestDto = json.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)),MttoVehicularRequest.class);
        UsuarioDto usuarioDto = json.fromJson(authentication.getPrincipal().toString(), UsuarioDto.class);

        //validamos solicitud
        if(requestDto!=null && requestDto.getSolicitud()!=null) {
            Response<?> validacionMttoSol = this.validarSolicitud(requestDto, authentication);
            if (validacionMttoSol != null && validacionMttoSol.getCodigo() == 200 && validacionMttoSol.getError()) {
                //enviamos el error
                return validacionMttoSol;
            }
        }
        //validamos registro
        if(requestDto!=null && requestDto.getRegistro()!=null) {
            Response<?> validacionMttoReg = this.validarRegistro(requestDto, authentication);
            if (validacionMttoReg != null && validacionMttoReg.getCodigo() == 200 && validacionMttoReg.getError()) {
                //enviamos el error
                return validacionMttoReg;
            }
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
                try {
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
				} catch (Exception e) {
					e.printStackTrace();
					return response;
				}
            } else {
                throw new BadRequestException(HttpStatus.valueOf(response.getCodigo()), "Error al insertar registro");
            }
        } else {
            try {
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
                        llamarServicio(solicitud.insertarMultiple(requestDto, usuarioDto).getDatos(), pathMultiple, authentication);
                        return existeMtto;
                    }else {
                        requestDto.getSolicitud().setIdMttoSolicitud(idMttoSol);
                        llamarServicio(solicitud.modificar(requestDto, usuarioDto).getDatos(),path,authentication);
                        Integer diferenciaDias = obtenerDif(requestDto, authentication); 
                		providerRestTemplate.consumirServicio(registro.actualizarEstatus(requestDto.getIdMttoVehicular(), diferenciaDias).getDatos(), urlDominioConsulta+"/actualizar",
                				authentication);
                    }
                  //  this.validaFechas(fechaRegistro,requestDto.getSolicitud().getIdMttoVehicular(),requestDto.getSolicitud().getFecRegistro(),authentication);
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
                       Integer diferenciaDias = obtenerDif(requestDto, authentication); 
                       log.info("-> " +diferenciaDias);
                		providerRestTemplate.consumirServicio(registro.actualizarEstatus(requestDto.getIdMttoVehicular(), diferenciaDias).getDatos(), urlDominioConsulta+"/actualizar",
                				authentication);
                        
                    } else {
                        requestDto.getRegistro().setIdMttoRegistro(idMttoReg);
                        llamarServicio(registro.modificar(requestDto, usuarioDto).getDatos(), path, authentication);
                        Integer diferenciaDias = obtenerDif(requestDto, authentication); 
                        log.info("-> " +diferenciaDias);
                		providerRestTemplate.consumirServicio(registro.actualizarEstatus(requestDto.getIdMttoVehicular(), diferenciaDias).getDatos(), urlDominioConsulta+"/actualizar",
                				authentication);
                    }
                  //  this.validaFechas(fechaRegistro,requestDto.getRegistro().getIdMttoVehicular(),requestDto.getRegistro().getFecRegistro(),authentication);
                }
                return existeMtto;
			} catch (Exception e) {
				e.printStackTrace();
				return existeMtto;
			}
        }
    }

	private Integer obtenerDif(MttoVehicularRequest requestDto, Authentication authentication) {
		Response<?> res = providerRestTemplate.consumirServicio(registro.validarSolicitud(requestDto).getDatos(), urlDominioConsulta+"/consulta",
				authentication);
        String respuesta = res.getDatos().toString();
        Integer diferencia = -1;
    	
		Pattern pattern = Pattern.compile("F=(\\d+)");
		Matcher matcher = pattern.matcher(respuesta);
		if (matcher.find()) {
		    diferencia = Integer.parseInt(matcher.group(1));
		    log.info("-> "+diferencia);
		}
		return diferencia;
	}

	@Override
    public Response<?> modificarMttoVehicular(DatosRequest request, Authentication authentication) throws IOException {
        String path=urlDominioConsulta + "/actualizar";
        Gson json = new Gson();
        MttoVehicularRequest requestDto = json.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)),MttoVehicularRequest.class);
        //validamos solicitud
        if(requestDto!=null && requestDto.getSolicitud()!=null) {
            Response<?> validacionMttoSol = this.validarSolicitud(requestDto, authentication);
            if (validacionMttoSol != null && validacionMttoSol.getCodigo() == 200 && validacionMttoSol.getError()) {
                //enviamos el error
                return validacionMttoSol;
            }
        }
        //validamos registro
        if(requestDto!=null && requestDto.getRegistro()!=null) {
            Response<?> validacionMttoReg = this.validarRegistro(requestDto, authentication);
            if (validacionMttoReg != null && validacionMttoReg.getCodigo() == 200 && validacionMttoReg.getError()) {
                //enviamos el error
                return validacionMttoReg;
            }
        }
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
