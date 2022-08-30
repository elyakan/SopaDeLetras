package cl.sitrack.sopaDeLetras.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "sopaDeLetras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SopaDeLetras {
    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "UUID", strategy = "uuid4")
    @Type(type = "uuid-char")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID Id;

    @Column(name = "nombre")
    private String Nombre;

    @Builder.Default
    @Column(name = "w",  nullable = false, columnDefinition="Integer default 15")
    private Integer w=15; //ancho  de la sopa, example = "15", required = true
    @Builder.Default
    @Column(name = "h",  nullable = false, columnDefinition="Integer default 15")
    private Integer h=15; //alto de la sopa", example = "15", required = true
    @Builder.Default
    @Column(name = "ltr",  nullable = false, columnDefinition="Boolean default true")
    private Boolean ltr=true; //izq-der "Permite buscar de izquierda a derecha"
    @Builder.Default
    @Column(name = "rtl",  nullable = false, columnDefinition="Boolean default false")
    private Boolean rtl=false; //der-izq Permite buscar de derecha a izquierda
    @Builder.Default
    @Column(name = "ttb",  nullable = false, columnDefinition="Boolean default false")
    private Boolean ttb=true; //up-dowm Permite buscar de arriba hacia abajo
    @Builder.Default
    @Column(name = "btt",  nullable = false, columnDefinition="Boolean default false")
    private Boolean btt=false; //down-up Permite buscar de abajo hacia arriba
    @Builder.Default
    @Column(name="d",  nullable = false, columnDefinition="Boolean default false")
    private Boolean d=false; //diagonal Permite buscar diagonal en cualquier dirección
    @Builder.Default
    @Column(name="sopa",  nullable = true, columnDefinition="Varchar(2000) default null")
    private String sopa=null;
    @Builder.Default
    @Column(name="palabrasEncontradas",  nullable = true, columnDefinition="Varchar(2000) default null")
    private String palabrasEncontradas=null;
    @Builder.Default
    @Column(name="palabrasDeLaSopa",  nullable = true, columnDefinition="Varchar(2000) default null")
    private String palabrasDeLaSopa=null;

    private DireccionPalabra direccionPalabra;

    public DireccionPalabra getDireccionPalabra() {
        return direccionPalabra;
    }

    public void setDireccionPalabra(DireccionPalabra direccionPalabra) {
        this.direccionPalabra = direccionPalabra;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Boolean getLtr() {
        return ltr;
    }

    public void setLtr(Boolean ltr) {
        this.ltr = ltr;
    }

    public Boolean getRtl() {
        return rtl;
    }

    public void setRtl(Boolean rtl) {
        this.rtl = rtl;
    }

    public Boolean getTtb() {
        return ttb;
    }

    public void setTtb(Boolean ttb) {
        this.ttb = ttb;
    }

    public Boolean getBtt() {
        return btt;
    }

    public void setBtt(Boolean btt) {
        this.btt = btt;
    }

    public Boolean getD() {
        return d;
    }

    public void setD(Boolean d) {
        this.d = d;
    }

    public String getSopa() {
        return sopa;
    }

    public void setSopa(String sopa) {
        this.sopa = sopa;
    }

    public String getPalabrasEncontradas() {
        return palabrasEncontradas;
    }

    public void setPalabrasEncontradas(String palabrasEncontradas) {
        this.palabrasEncontradas = palabrasEncontradas;
    }

    public String getPalabrasDeLaSopa() {
        return palabrasDeLaSopa;
    }

    public void setPalabrasDeLaSopa(String palabrasDeLaSopa) {
        this.palabrasDeLaSopa = palabrasDeLaSopa;
    }

}
