package com.imss.sivimss.vehiculos.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BuscarVehiculosRequest {
    @JsonProperty
    private Integer velatorio;
    @JsonProperty
    private Integer nivelOficina;
    @JsonProperty
    private String placa;
    @JsonProperty
    private Integer idVehiculo;
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
