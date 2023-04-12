package com.imss.sivimss.vehiculos.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BuscarVehiculosRequest {
    @JsonProperty
    private Integer nivelOficina;
    @JsonProperty
    private String placa;
    @JsonProperty
    private Integer tipoMtto;
    @JsonProperty
    private Integer periodo;
    @JsonProperty
    private Integer estadoMtto;
    @JsonProperty
    private String pagina;
    @JsonProperty
    private String tamanio;
}
