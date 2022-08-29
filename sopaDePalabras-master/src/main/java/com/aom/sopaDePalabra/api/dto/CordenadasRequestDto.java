package com.aom.sopaDePalabra.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
public class CordenadasRequestDto {

    @ApiModelProperty(value = "Fila donde comienza la palabra encontrada")
    public Integer sr;
    @ApiModelProperty(value = "Columna donde comienza la palabra encontrada")
    public Integer sc;
    @ApiModelProperty(value = "Fila donde termina la palabra encontrada")
    public Integer er;
    @ApiModelProperty(value = "Columna donde termina la palabra encontrada")
    public Integer ec;
}
