package cl.sitrack.sopaDeLetras.dto;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
public class SopaResponseIdDto {

    @Id
    @Type(type = "uuid-char")
    private UUID id;


}
