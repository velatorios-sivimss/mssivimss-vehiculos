package com.imss.sivimss.vehiculos.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteDto {

	private Integer numReporte;
	private Integer idDelegacion;
	private Integer idNivelOficina;
	private Integer idVelatorio;
	private String placas;
	private String fechaInicio;
	private String fechaFin;
	private String tipoReporte;
	private String rutaNombreReporte;
}
