package com.imss.sivimss.vehiculos.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MttoVehicularRequest {
    @JsonProperty
    private Integer idMttoVehicular;
    @JsonProperty
    private Integer idMttoestado;
    @JsonProperty
    private Integer  idVehiculo;
    @JsonProperty
    private Integer  idDelegacion;
    @JsonProperty
    private Integer  idVelatorio;
    @JsonProperty
    private MttoVerifInicioRequest verificacionInicio;
    @JsonProperty
    private MttoSolicitudRequest solicitud;
    @JsonProperty
    private MttoRegistroRequest registro;
    @JsonProperty
    private Integer idEstatus;
}
