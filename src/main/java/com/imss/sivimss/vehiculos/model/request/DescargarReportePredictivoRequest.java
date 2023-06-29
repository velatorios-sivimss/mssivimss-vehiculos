package com.imss.sivimss.vehiculos.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DescargarReportePredictivoRequest {
	
	private Integer idDelegacion;
	private Integer idNivelOficina;
	private Integer idVelatorio;
	private String tipoMtto;
	private String valor;
	private String placas;
	private String periodo;
	private String tipoReporte;
	private String rutaNombreReporte;

}
