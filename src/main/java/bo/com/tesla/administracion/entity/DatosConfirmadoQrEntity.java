package bo.com.tesla.administracion.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "datosconfirmado_qr",  schema = "tesla")
public class DatosConfirmadoQrEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "datosconfirmado_qr_id", nullable = false)
    private Long datosconfirmadoQrId;

    @Column(name = "alias_sip")
    private String aliasSip;
    @Column(name = "numero_orden_originante_sip")
    private String numeroOrdenOriginanteSip;
    @Column(name = "monto_sip")
    private String montoSip;
    @Column(name = "id_qr_sip")
    private String idQrSip;
    @Column(name = "moneda_sip")
    private String monedaSip;
    @Column(name = "fecha_proceso_sip")
    private String fechaProcesoSip;
    @Column(name = "cuenta_cliente_sip")
    private String cuentaClienteSip;
    @Column(name = "nombre_cliente_sip")
    private String nombreClienteSip;
    @Column(name = "documento_cliente_sip")
    private String documentoClienteSip;
    @Column(name = "json_sip")
    private String jsonSip;
    @Column(name = "fecha_registro")
    private Date fechaRegistro;
    @Column(name = "estado")
    private String  estado;
    @Column(name = "codigo_pago")
    private String  codigoPago;


}
