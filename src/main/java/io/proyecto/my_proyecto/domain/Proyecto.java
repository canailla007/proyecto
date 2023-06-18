package io.proyecto.my_proyecto.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;


@Entity
public class Proyecto {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    @Column(unique = true)
    private String url;

    @Column
    private LocalDateTime fechaCreada;

    @Column
    private LocalDateTime fechaActualizado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servidor_id")
    private Servidor servidor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estado_id")
    private Estado estado;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public LocalDateTime getFechaCreada() {
        return fechaCreada;
    }

    public void setFechaCreada(final LocalDateTime fechaCreada) {
        this.fechaCreada = fechaCreada;
    }

    public LocalDateTime getFechaActualizado() {
        return fechaActualizado;
    }

    public void setFechaActualizado(final LocalDateTime fechaActualizado) {
        this.fechaActualizado = fechaActualizado;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(final Servidor servidor) {
        this.servidor = servidor;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(final Estado estado) {
        this.estado = estado;
    }

}
