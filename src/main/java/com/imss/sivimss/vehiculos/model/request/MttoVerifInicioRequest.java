package com.imss.sivimss.vehiculos.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MttoVerifInicioRequest {
    @JsonProperty
    private Integer idMttoVerifInicio;
    @JsonProperty
    private Integer idMttoVehicular;

    @JsonProperty
    private Integer  idNivelAceite;
    @JsonProperty
    private Integer  idNivelAgua;
    @JsonProperty
    private Integer  idCalNeuTraseros;
    @JsonProperty
    private Integer  idCalNeuDelanteros;
    @JsonProperty
    private Integer  idNivelCombustible;
    @JsonProperty
    private Integer  idNivelBateria;
    @JsonProperty
    private Integer  idCodigoFallo;
    @JsonProperty
    private Integer  idLimpiezaInterior;
    @JsonProperty
    private Integer  idLimpiezaExterior;
}
