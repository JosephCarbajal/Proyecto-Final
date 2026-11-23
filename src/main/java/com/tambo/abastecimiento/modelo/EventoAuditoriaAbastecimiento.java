package com.tambo.abastecimiento.modelo;

public final class EventoAuditoriaAbastecimiento {
    private final String entidad;
    private final int entidadId;
    private final String accion;
    private final String usuario;
    private final String detalle;

    public EventoAuditoriaAbastecimiento(String entidad, int entidadId,
                                         String accion, String usuario,
                                         String detalle) {
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.accion = accion;
        this.usuario = usuario;
        this.detalle = detalle;
    }

    public String entidad() {
        return entidad;
    }

    public int entidadId() {
        return entidadId;
    }

    public String accion() {
        return accion;
    }

    public String usuario() {
        return usuario;
    }

    public String detalle() {
        return detalle;
    }
}