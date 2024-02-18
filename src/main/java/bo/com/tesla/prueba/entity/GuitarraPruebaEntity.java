package bo.com.tesla.prueba.entity;

import bo.com.tesla.administracion.entity.HistoricoDeudaEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "guitarra_prueba", catalog = "exacta_tesla", schema = "prueba")
public class GuitarraPruebaEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guitarra_id")
    private Long guitarraId;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "costo")
    private Double costo;

    @Column(name = "img")
    private String img;

    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    @Column(name = "estado_id")
    private Integer estadoId;
}


