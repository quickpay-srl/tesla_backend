package bo.com.tesla.administracion.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "facturas", catalog = "exacta_tesla", schema = "tesla")
public class FacturaEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "factura_id", nullable = false)
    private Long facturaId;
    @Column(name = "response_cuf")
    private String responseCuf;
    @Column(name = "response_state")
    private String responseState;
    @Column(name = "response_nro_factura")
    private String responseNroFactura;
    @Column(name = "response_codigo_cliente")
    private String responseCodigoCliente;
    @Column(name = "response_numeroDocumento_cliente")
    private String responseNumeroDocumentoCliente;
    @Column(name = "response_razonSocial_cliente")
    private String responseRazonSocialCliente;
    @Column(name = "response_complemento_cliente")
    private String responseComplementoCliente;
    @Column(name = "response_email_cliente")
    private String responseEmailCliente;
    @Column(name = "response_pdf_repgrafica")
    private String responsePdfRepgrafica;
    @Column(name = "response_xml_repgrafica")
    private String responseXmlRepgrafica;
    @Column(name = "response_sin_repgrafica")
    private String responseSinRepgrafica;
    @Column(name = "response_rollo_repgrafica")
    private String responseRolloRepgrafica;
    @Column(name = "fecha_registro")
    private Date fechaRegistro;
    @Column(name = "estado")
    private String estado;


}
