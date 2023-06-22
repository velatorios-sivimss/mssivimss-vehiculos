package com.imss.sivimss.vehiculos.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MttoSolicitudRequest {
    @JsonProperty
    private Integer idMttoSolicitud;
    @JsonProperty
    private Integer idMttoVehicular;
    @JsonProperty
    private Integer idMttoTipo;
    @JsonProperty
    private Integer  idMttoModalidad;
    @JsonProperty
    private String  fecRegistro;
    @JsonProperty
    private String  desMttoCorrectivo;
    @JsonProperty
    private Integer idMttoModalidadDet;
    @JsonProperty
    private String  desNotas;
    @JsonProperty
    private Integer idEstatus;
    @JsonProperty
    private Integer kilometraje;
    @JsonProperty
    private String  fecRegistro2;
}
