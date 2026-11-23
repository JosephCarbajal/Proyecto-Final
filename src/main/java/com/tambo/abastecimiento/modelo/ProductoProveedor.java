package com.tambo.abastecimiento.modelo;

public final class ProductoProveedor {
    private final int proveedorId;
    private final int productoId;
    private final long costoCentimos;
    private final boolean principal;

    public ProductoProveedor(int proveedorId, int productoId,
                             long costoCentimos, boolean principal) {
        this.proveedorId = proveedorId;
        this.productoId = productoId;
        this.costoCentimos = costoCentimos;
        this.principal = principal;
    }

    public int proveedorId() {
        return proveedorId;
    }

    public int productoId() {
        return productoId;
    }

    public long costoCentimos() {
        return costoCentimos;
    }

    public boolean principal() {
        return principal;
    }
}