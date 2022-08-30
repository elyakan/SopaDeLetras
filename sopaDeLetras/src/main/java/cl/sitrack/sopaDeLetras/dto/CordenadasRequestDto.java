package cl.sitrack.sopaDeLetras.dto;
import lombok.*;

@Getter
@Setter
public class CordenadasRequestDto {
    //Fila donde comienza la palabra encontrada"
    public Integer sr;
    //Columna donde comienza la palabra encontrada"
    public Integer sc;
    //"Fila donde termina la palabra encontrada"
    public Integer er;
    // "Columna donde termina la palabra encontrada"
    public Integer ec;
}
