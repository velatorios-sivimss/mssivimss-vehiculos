package com.imss.sivimss.vehiculos.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@JsonIgnoreType(value = true)
public class VehiculoRequest {	
	private Integer idVelatorio;
	private Integer idVehiculo;
	private String idODS;
	private String fecSalida;
	private String horaSalida;
	private String gasolinaInicial;
	private String kmInicial;
	private String fecEntrada;
	private String horaEntrada;
	private String gasolinaFinal;
	private String kmFinal;
	private Integer idResponsable;
	private String fecIniRepo;
	private String fecFinRepo;
	private String fecDia;
}
