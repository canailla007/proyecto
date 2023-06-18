package io.proyecto.my_proyecto.model;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;


public class ProyectoDTO {

    private Long id;

    @Size(max = 255)
    private String nombre;

    @Size(max = 255)
    private String url;

    private LocalDateTime fechaCreada;

    private LocalDateTime fechaActualizado;

    private Long servidor;

    private Long estado;

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

    public Long getServidor() {
        return servidor;
    }

    public void setServidor(final Long servidor) {
        this.servidor = servidor;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(final Long estado) {
        this.estado = estado;
    }

}
