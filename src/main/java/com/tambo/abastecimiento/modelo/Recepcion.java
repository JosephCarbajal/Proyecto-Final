package com.tambo.abastecimiento.modelo;

import java.util.List;

public final class Recepcion {
    private final String numero;
    private final int ordenId;
    private final List<DetalleRecepcion> detalles;

    public Recepcion(String numero, int ordenId,
                     List<DetalleRecepcion> detalles) {
        this.numero = numero;
        this.ordenId = ordenId;
        this.detalles = List.copyOf(detalles);
    }

    public String numero() {
        return numero;
    }

    public int ordenId() {
        return ordenId;
    }

    public List<DetalleRecepcion> detalles() {
        return detalles;
    }
}