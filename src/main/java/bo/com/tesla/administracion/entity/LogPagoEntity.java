package bo.com.tesla.administracion.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "log_pagos", catalog = "exacta_tesla", schema = "tesla")
public class LogPagoEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_pago_id")
    private Long logPagoId;
    @Column(name = "alias")
    private String alias;
    @Column(name = "json")
    private String json;
    @Column(name = "mensaje_tecnico")
    private String mensajeTecnico;
    @Column(name = "mensaje_usuario")
    private String mensajeUsuario;
    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    private Date fechaFin;
    @Column(name = "host")
    private String host;
    @Column(name = "metodo")
    private String metodo;
    @Column(name = "tipo_log")
    private String tipoLog;
    @Column(name = "estado_id")
    private String estadoId;

}
