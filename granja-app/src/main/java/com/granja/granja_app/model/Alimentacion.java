package com.granja.granja_app.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "alimentacion")
public class Alimentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dosis;
    private String descripcion;

    @OneToMany(mappedBy = "alimentacion", cascade = CascadeType.ALL)
    private List<Porcino> porcinos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

