package com.tambo.abastecimiento.modelo;

import java.util.List;

public final class OrdenCompra {
    private final String numero;
    private final int proveedorId;
    private final List<DetalleOrdenCompra> detalles;

    public OrdenCompra(String numero, int proveedorId,
                       List<DetalleOrdenCompra> detalles) {
        this.numero = numero;
        this.proveedorId = proveedorId;
        this.detalles = List.copyOf(detalles);
    }

    public String numero() {
        return numero;
    }

    public int proveedorId() {
        return proveedorId;
    }

    public List<DetalleOrdenCompra> detalles() {
        return detalles;
    }
}