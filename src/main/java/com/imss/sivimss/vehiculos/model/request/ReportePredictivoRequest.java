package com.imss.sivimss.vehiculos.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportePredictivoRequest {
    @JsonProperty
    private Integer nivelOficina;

    @JsonProperty
    private Integer velatorio;

    @JsonProperty
    private Integer delegacion;

    @JsonProperty
    private Integer tipoMtto;

    @JsonProperty
    private String placa;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty
    private String fechaInicio;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty
    private String fechaFinal;

    @JsonProperty
    private String pagina;

    @JsonProperty
    private String tamanio;
}
