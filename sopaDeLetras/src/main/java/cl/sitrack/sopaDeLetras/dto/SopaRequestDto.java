package cl.sitrack.sopaDeLetras.dto;

import lombok.*;

@Getter
@Setter
public class SopaRequestDto {
    private Integer w; //ancho
    private Integer h; //alto
    private Boolean ltr; //izq-der
    private Boolean rtl; //der-izq
    private Boolean ttb; //up-dowm
    private Boolean btt; //down-up
    private Boolean d; //diagonal
}
