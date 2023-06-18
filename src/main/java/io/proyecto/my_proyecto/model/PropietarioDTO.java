package io.proyecto.my_proyecto.model;

import jakarta.validation.constraints.Size;


public class PropietarioDTO {

    private Long id;

    @Size(max = 255)
    private String nombre;

    @Size(max = 255)
    private String apellido1;

    @Size(max = 255)
    private String apellido2;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String telefono;

    private Long proyecto;

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

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(final String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(final String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(final String telefono) {
        this.telefono = telefono;
    }

    public Long getProyecto() {
        return proyecto;
    }

    public void setProyecto(final Long proyecto) {
        this.proyecto = proyecto;
    }

}
