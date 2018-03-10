package com.bolsadeideas.springboot.app.models.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="facturas")
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descripcion;
    private String observacion;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

    // EAGER carga completa
    // LAZY carga perezosa cada que necesita
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente cliente;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // Join Column es cuando la relacion no es en ambos sentidos
    @JoinColumn(name = "factura_id")
    private List<ItemFactura> items;

    // Constructor

    public Factura() {
        this.items = new ArrayList<ItemFactura>();
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getCreateAt() {
        return createAt;
    }

    @PrePersist
    public void prePersist(){
        createAt = new Date();
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void  addItemFactura(ItemFactura item){
        this.items.add(item);
    }

    public List<ItemFactura> getItems() {
        return items;
    }

    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }
}
