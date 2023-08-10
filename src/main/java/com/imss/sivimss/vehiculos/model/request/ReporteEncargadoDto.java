package com.imss.sivimss.vehiculos.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReporteEncargadoDto {
	
	private Integer numReporte;
	private Integer delegacion;
	private Integer nivelOficina;
	private Integer velatorio;
	private String placas;
	private String fechaInicio;
	private String fechaFin;
	private String tipoReporte;
	private String rutaNombreReporte;

}
