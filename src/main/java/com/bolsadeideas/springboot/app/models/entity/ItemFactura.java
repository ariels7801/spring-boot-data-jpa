package com.bolsadeideas.springboot.app.models.entity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "facturas_items")
public class ItemFactura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Integer cantidad;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Long calcularImporte() {
        return cantidad.longValue();
    }
}
