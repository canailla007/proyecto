package io.proyecto.my_proyecto.model;

import jakarta.validation.constraints.Size;


public class ServidorDTO {

    private Long id;

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String servidor;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(final String servidor) {
        this.servidor = servidor;
    }

}
