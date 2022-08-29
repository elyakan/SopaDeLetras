package com.aom.sopaDePalabra.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ApiModel
public class SopaRequestDto {

    @ApiModelProperty(value = "Ancho de la sopa", example = "15", required = true)
    private Integer w; //ancho
    @ApiModelProperty(value = "Alto de la sopa", example = "15", required = true)
    private Integer h; //alto
    @ApiModelProperty(value = "Permite buscar de izquierda a derecha")
    private Boolean ltr; //izq-der
    @ApiModelProperty(value = "Permite buscar de derecha a izquierda")
    private Boolean rtl; //der-izq
    @ApiModelProperty(value = "Permite buscar de arriba hacia abajo")
    private Boolean ttb; //up-dowm
    @ApiModelProperty(value = "Permite buscar de abajo hacia arriba")
    private Boolean btt; //down-up
    @ApiModelProperty(value = "Permite buscar diagonal en cualquier dirección")
    private Boolean d; //diagonal

}

