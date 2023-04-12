package com.imss.sivimss.vehiculos.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MttoRegistroRequest {
    @JsonProperty
    private Integer idMttoRegistro;
    @JsonProperty
    private Integer idMttoVehicular;
    @JsonProperty
    private Integer idMttoModalidad;
    @JsonProperty
    private Integer  idMantenimiento;
    @JsonProperty
    private String  desNotas;
    @JsonProperty
    private String  idProveedor;
    @JsonProperty
    private String desNumcontrato;
}
