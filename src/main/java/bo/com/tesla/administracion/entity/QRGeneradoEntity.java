package bo.com.tesla.administracion.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "qr_generado",  schema = "tesla")
public class QRGeneradoEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qr_generado_id", nullable = false)
    private Long qrGeneradoId;
    @Column(name = "deuda_cliente_id")
    private Long deudaClienteId;
    @Column(name = "alias")
    private String alias;
    @Column(name = "callback")
    private String callback;
    @Column(name = "detalle_glosa")
    private String detalleGlosa;
    @Column(name = "monto")
    private String monto;
    @Column(name = "moneda")
    private String moneda;
    @Column(name = "fecha_vencimiento")
    private String fechaVencimiento;
    @Column(name = "tipo_solicitud")
    private String tipoSolicitud;
    @Column(name = "unico_uso")
    private String unicoUso;
    @Column(name = "fecha_registro")
    private Date fechaRegistro;
    @Column(name = "estado")
    private String  estado;

}
